package cz.csas.cscore.locker;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * The type Locker unlock success test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /12/15.
 */
public class LockerUnlockTest extends LockerTest {

    private final String X_JUDGE_SESSION_HEADER_UNLOCK = "core.locker.unlock.session";
    private CountDownLatch mUnlockSignal;
    private RegistrationOrUnlockResponse mUnlockResponse;
    private State mState;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_UNLOCK;
        super.setUp();
        mUnlockSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);

        mLocker.setOnLockerStatusChangeListener(new OnLockerStatusChangeListener() {
            @Override
            public void onLockerStatusChanged(State state) {
                mState = state;
            }
        });
        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
        LockerUtils.lockerRegister(mLocker);
        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_UNLOCK, mXJudgeSessionHeader);
    }

    /**
     * Test.
     */
    @Test
    public void testUnlock(){

        mLocker.unlock(Constants.PASSWORD_TEST, new Callback<RegistrationOrUnlockResponse>() {
            @Override
            public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
                mUnlockResponse = registrationOrUnlockResponse;
                mUnlockSignal.countDown();
            }

            @Override
            public void failure(CsRestError error) {
                mUnlockSignal.countDown();
            }
        });


        try {
            mUnlockSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(State.USER_UNLOCKED, mState);
        assertEquals(mUnlockResponse.getAccessToken().getAccessToken(), Constants.ACCESS_TOKEN_TEST);
        assertEquals(mUnlockResponse.getAccessToken().getAccessTokenExpiration(), Constants.ACCESS_TOKEN_EXPIRATION_TEST);
        assertTrue(mLocker.getStatus().hasAesEncryptionKey());
        assertTrue(mLocker.getStatus().hasOneTimePasswordKey());
        assertEquals(mLocker.getAccessToken().getAccessToken(),mUnlockResponse.getAccessToken().getAccessToken());
        assertEquals(mLocker.getAccessToken().getAccessTokenExpiration(),mUnlockResponse.getAccessToken().getAccessTokenExpiration());
    }
}

package cz.csas.cscore.locker;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.mime.CsCallback;
import cz.csas.cscore.error.CsLockerError;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 10/03/16.
 */
public class LockerUnlockEmptyAccessTokenTest extends LockerTest {

    private final String X_JUDGE_CASE_HEADER_UNLOCK_EMPTY_ACCESS_TOKEN = "core.locker.unlockEmptyAccesToken";
    private final String X_JUDGE_SESSION_HEADER_UNLOCK_EMPTY_ACCESS_TOKEN = "core.locker.unlockEmptyAccesToken.session";
    private CountDownLatch mUnlockSignal;
    private State mState;
    private CsSDKError mError;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_UNLOCK_EMPTY_ACCESS_TOKEN;
        super.setUp();
        mUnlockSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);

        mLocker.setOnLockerStatusChangeListener(new OnLockerStatusChangeListener() {
            @Override
            public void onLockerStatusChanged(State state) {
                mState = state;
            }
        });
        JudgeUtils.setJudge(mJudgeClient, cz.csas.cscore.judge.Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
        LockerUtils.lockerRegister(mLocker);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_HEADER_UNLOCK_EMPTY_ACCESS_TOKEN, mXJudgeSessionHeader);
    }

    /**
     * Test.
     */
    @Test
    public void testLockerUnlockEmptyAccessToken() {

        mLocker.unlock(cz.csas.cscore.judge.Constants.PASSWORD_TEST, new CsCallback<RegistrationOrUnlockResponse>() {
            @Override
            public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
                mUnlockSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mError = error;
                mUnlockSignal.countDown();
            }
        });


        try {
            mUnlockSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // empty access token => unregistered.
        assertEquals(CsLockerError.Kind.PARSE_ERROR, ((CsLockerError) mError.getCause()).getKind());
        assertEquals(State.USER_UNREGISTERED, mState);
    }
}

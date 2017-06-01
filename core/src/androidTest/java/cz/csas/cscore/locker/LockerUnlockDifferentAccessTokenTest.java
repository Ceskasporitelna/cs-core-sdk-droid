package cz.csas.cscore.locker;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.error.CsSDKError;
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
public class LockerUnlockDifferentAccessTokenTest extends LockerTest {

    private final String X_JUDGE_CASE_HEADER_UNLOCK_DIFFERENT_ACCESS_TOKEN = "core.locker.unlockDifferentAccessToken";
    private final String X_JUDGE_SESSION_HEADER_UNLOCK_DIFFERENT_ACCESS_TOKEN = "core.locker.unlockDifferentAccessToken.session";
    private CountDownLatch mUnlockSignal;
    private RegistrationOrUnlockResponse mUnlockResponse;
    private State mState;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_UNLOCK_DIFFERENT_ACCESS_TOKEN;
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
    public void testUnlockWithDifferentAccessToken() {

        final CountDownLatch lockSignal = new CountDownLatch(1);

        mLocker.lock(new CsCallback<LockerStatus>() {
            @Override
            public void success(LockerStatus lockerStatus, Response response) {
                lockSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {

            }
        });

        mLocker.unlock(Constants.PASSWORD_TEST, new CsCallback<RegistrationOrUnlockResponse>() {
            @Override
            public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
                mUnlockResponse = registrationOrUnlockResponse;
                mUnlockSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
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
        assertEquals(mLocker.getAccessToken().getAccessToken(), mUnlockResponse.getAccessToken().getAccessToken());

        final CountDownLatch lockSignal2 = new CountDownLatch(1);

        mLocker.lock(new CsCallback<LockerStatus>() {
            @Override
            public void success(LockerStatus lockerStatus, Response response) {
                lockSignal2.countDown();
            }

            @Override
            public void failure(CsSDKError error) {

            }
        });

        try {
            lockSignal.await(20, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final CountDownLatch secondUnlockSignal = new CountDownLatch(1);

        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_HEADER_UNLOCK_DIFFERENT_ACCESS_TOKEN, mXJudgeSessionHeader);
        mLocker.unlock(Constants.PASSWORD_TEST, new CsCallback<RegistrationOrUnlockResponse>() {
            @Override
            public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
                mUnlockResponse = registrationOrUnlockResponse;
                secondUnlockSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                secondUnlockSignal.countDown();
            }
        });

        try {
            secondUnlockSignal.await(20, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        assertEquals(State.USER_UNLOCKED, mState);
        assertEquals(mUnlockResponse.getAccessToken().getAccessToken(), "DIFFERENT_ACCESS_TOKEN");
        assertEquals(mUnlockResponse.getAccessToken().getAccessTokenExpiration(), Constants.ACCESS_TOKEN_EXPIRATION_TEST);
        assertTrue(mLocker.getStatus().hasAesEncryptionKey());
        assertTrue(mLocker.getStatus().hasOneTimePasswordKey());
        assertEquals(mLocker.getAccessToken().getAccessToken(), mUnlockResponse.getAccessToken().getAccessToken());


    }
}

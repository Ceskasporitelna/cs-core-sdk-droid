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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * The type Locker unlock success test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /12/15.
 */
public class LockerUnlockServerUnavailableTest extends LockerTest {
    private final String X_JUDGE_CASE_HEADER_UNLOCK_UNAVAILABLE = "core.locker.unlock.serverUnavailable";
    private final String X_JUDGE_SESSION_HEADER_UNLOCK_UNAVAILABLE = "core.locker.unlock.serverUnavailable.session";
    private CountDownLatch mUnlockSignal;
    private CountDownLatch mLockSignal;
    private RegistrationOrUnlockResponse mUnlockResponse;
    private State mState;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_UNLOCK_UNAVAILABLE;
        super.setUp();
        mLockSignal = new CountDownLatch(1);
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

        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_HEADER_UNLOCK_UNAVAILABLE, mXJudgeSessionHeader);
    }

    /**
     * Test.
     */
    @Test
    public void testUnlockServerUnavailable() {
        mLocker.lock(new CsCallback<LockerStatus>() {
            @Override
            public void success(LockerStatus lockerStatus, Response response) {
                assertEquals(State.USER_LOCKED, lockerStatus.getState());
                mLockSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mLockSignal.countDown();
            }
        });
        try {
            mLockSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

        assertEquals(State.USER_LOCKED, mState);
        assertEquals(State.USER_LOCKED, mLocker.getStatus().getState());
        assertFalse(mLocker.getStatus().hasAesEncryptionKey());
        assertTrue(mLocker.getStatus().hasOneTimePasswordKey());
    }
}

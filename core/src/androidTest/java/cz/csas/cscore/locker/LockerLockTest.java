package cz.csas.cscore.locker;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CallbackBasic;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06/12/15.
 */
public class LockerLockTest extends LockerTest {

    private static final String X_JUDGE_SESSION_HEADER_LOCK = "core.locker.lock.session";
    private CountDownLatch mUnlockSignal;
    private CountDownLatch mLockSignal;
    private State mState;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_LOCK;
        super.setUp();
        mUnlockSignal = new CountDownLatch(1);
        mLockSignal = new CountDownLatch(1);
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
        mLocker.unlock(Constants.PASSWORD_TEST, new Callback<RegistrationOrUnlockResponse>() {
            @Override
            public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
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
    }

    @Test
    public void testLock() {

        mLocker.lock(new CallbackBasic<LockerStatus>() {
            @Override
            public void success(LockerStatus lockerStatus) {
                mLockSignal.countDown();
            }

            @Override
            public void failure() {
                mLockSignal.countDown();
            }
        });

        try {
            mLockSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(State.USER_LOCKED, mState);
        assertEquals(LockType.FINGERPRINT, mLocker.getStatus().getLockType());
        assertEquals(Constants.CID_TEST, mLocker.getStatus().getClientId());
        assertFalse(mLocker.getStatus().hasAesEncryptionKey());
        assertTrue(mLocker.getStatus().hasOneTimePasswordKey());

    }
}

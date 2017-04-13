package cz.csas.cscore.locker;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.CallbackBasic;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * The type Locker unregistration test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /12/15.
 */
public class LockerUnregistrationTest extends LockerTest {

    private final String X_JUDGE_SESSION_HEADER_UNREGISTER = "core.locker.unregister";
    private CountDownLatch mUnregisterSignal;
    private State mState;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_UNREGISTER;
        super.setUp();
        mUnregisterSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);
        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
        LockerUtils.lockerRegister(mLocker);
        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_UNREGISTER, mXJudgeSessionHeader);
    }


    /**
     * Test.
     */
    @Test
    public void testUnregistration() {

        mLocker.unregister(new CallbackBasic<LockerStatus>(){

            @Override
            public void success(LockerStatus lockerStatus) {
                mState = lockerStatus.getState();
                mUnregisterSignal.countDown();
            }

            @Override
            public void failure() {

            }
        });

        try {
            mUnregisterSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(State.USER_UNREGISTERED, mState);
        assertFalse(mLocker.getStatus().hasAesEncryptionKey());
        assertFalse(mLocker.getStatus().hasOneTimePasswordKey());

    }

}

package cz.csas.cscore.locker;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.mime.CsCallback;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06/12/15.
 */
public class LockerUnlockUnregisterTest extends LockerTest {

    private final String X_JUDGE_CASE_HEADER_UNLOCK_UNREGISTER = "core.locker.unlock.unregister";
    private final String X_JUDGE_SESSION_HEADER_UNLOCK_UNREGISTER = "core.locker.unlock.unregister.session";
    private CountDownLatch mUnlockSignal;
    private State mState;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_UNLOCK_UNREGISTER;
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
        JudgeUtils.setJudge( mJudgeClient, X_JUDGE_CASE_HEADER_UNLOCK_UNREGISTER, mXJudgeSessionHeader);
    }

    /**
     * Test.
     */
    @Test
    public void testUnlockUnregister(){

        mLocker.unlock(Constants.PASSWORD_TEST, new CsCallback<RegistrationOrUnlockResponse>() {
            @Override
            public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
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

        assertEquals(State.USER_UNREGISTERED, mState);
        assertFalse(mLocker.getStatus().hasAesEncryptionKey());
        assertFalse(mLocker.getStatus().hasOneTimePasswordKey());
    }
}


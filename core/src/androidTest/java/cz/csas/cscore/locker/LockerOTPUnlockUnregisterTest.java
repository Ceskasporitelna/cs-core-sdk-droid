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
import static junit.framework.Assert.assertFalse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06/12/15.
 */
public class LockerOTPUnlockUnregisterTest extends LockerTest {

    private final String X_JUDGE_CASE_HEADER_UNLOCK_WITH_ONE_TIME_PASSWORD_UNREGISTER = "core.locker.unlockWithOneTimePassword.unregister";
    private final String X_JUDGE_SESSION_HEADER_UNLOCK_WITH_ONE_TIME_PASSWORD_UNREGISTER = "core.locker.unlockWithOneTimePassword.unregister.session";
    private CountDownLatch mOTPUnlockSignal;
    private RegistrationOrUnlockResponse mUnlockResponse;
    private State mState;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_UNLOCK_WITH_ONE_TIME_PASSWORD_UNREGISTER;
        super.setUp();
        mOTPUnlockSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);
        mLocker.setOnLockerStatusChangeListener(new OnLockerStatusChangeListener() {
            @Override
            public void onLockerStatusChanged(State state) {
                mState = state;
            }
        });
        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
        LockerUtils.lockerRegister(mLocker);
        JudgeUtils.setJudge(mJudgeClient,X_JUDGE_CASE_HEADER_UNLOCK_WITH_ONE_TIME_PASSWORD_UNREGISTER, mXJudgeSessionHeader);
    }

    @Test
    public void testOneTimePassword(){

        mLocker.unlockWithOneTimePassword(new Callback<RegistrationOrUnlockResponse>() {
            @Override
            public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
                mUnlockResponse = registrationOrUnlockResponse;
                mOTPUnlockSignal.countDown();
            }

            @Override
            public void failure(CsRestError error) {
                mOTPUnlockSignal.countDown();
            }
        });

        try {
            mOTPUnlockSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(State.USER_UNREGISTERED, mState);
        assertFalse(mLocker.getStatus().hasAesEncryptionKey());
        assertFalse(mLocker.getStatus().hasOneTimePasswordKey());
    }
}

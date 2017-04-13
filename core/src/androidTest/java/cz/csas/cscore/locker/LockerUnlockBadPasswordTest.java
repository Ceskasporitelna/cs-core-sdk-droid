package cz.csas.cscore.locker;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;

/**
 * The type Locker unlock bad password test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 10 /03/16.
 */
public class LockerUnlockBadPasswordTest extends LockerTest {

    private final String X_JUDGE_CASE_HEADER_UNLOCK_BAD_PASSWORD = "core.locker.unlock.badPassword";
    private final String X_JUDGE_SESSION_HEADER_UNLOCK_BAD_PASSWORD = "core.locker.unlock.badPassword.session";
    private CountDownLatch mUnlockSignal;
    private RegistrationOrUnlockResponse mUnlockResponse;
    private State mState;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_UNLOCK_BAD_PASSWORD;
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
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_HEADER_UNLOCK_BAD_PASSWORD , mXJudgeSessionHeader);
    }

    /**
     * Test.
     */
    @Test
    public void testUnlock(){

        mLocker.unlock(cz.csas.cscore.judge.Constants.PASSWORD_TEST, new Callback<RegistrationOrUnlockResponse>() {
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

        assertEquals("2",mUnlockResponse.getRemainingAttempts());
    }
}

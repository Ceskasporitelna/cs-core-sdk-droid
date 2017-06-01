package cz.csas.cscore.locker;

import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.CallbackBasic;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * The type Locker registration test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 05 /12/15.
 */
public class LockerRegistrationTest extends LockerTest {

    private final String X_JUDGE_SESSION_HEADER_REGISTRATION = "core.locker.register.session";
    private CountDownLatch mRegisterSignal;
    private RegistrationOrUnlockResponse mRegistrationResponse;
    private State mState;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_REGISTRATION;
        super.setUp();
        mRegisterSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);

        mLocker.setOnLockerStatusChangeListener(new OnLockerStatusChangeListener() {
            @Override
            public void onLockerStatusChanged(State state) {
                mState = state;
            }
        });
        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
    }


    /**
     * Test.
     */
    @Test
    public void testRegistration() {
        mLocker.register(InstrumentationRegistry.getTargetContext(), new CallbackBasic<LockerRegistrationProcess>() {
            @Override
            public void success(LockerRegistrationProcess lockerRegistrationProcess) {
                lockerRegistrationProcess.finishRegistration(new Password(LockType.GESTURE, Constants.PASSWORD_TEST));
            }

            @Override
            public void failure() {
                mRegisterSignal.countDown();
            }
        }, new CsCallback<RegistrationOrUnlockResponse>() {
            @Override
            public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
                mRegistrationResponse = registrationOrUnlockResponse;
                mRegisterSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mRegisterSignal.countDown();
            }
        });

        mLocker.processUrl(Constants.CODE_URL_TEST);

        try {
            mRegisterSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(State.USER_UNLOCKED, mState);
        assertEquals(mRegistrationResponse.getAccessToken().getAccessToken(), Constants.ACCESS_TOKEN_TEST);
        assertEquals(mRegistrationResponse.getAccessToken().getAccessTokenExpiration(), Constants.ACCESS_TOKEN_EXPIRATION_TEST);
        Assert.assertEquals(mLocker.getStatus().getClientId(), Constants.CID_TEST);
        Assert.assertEquals(mLocker.getStatus().getLockType(), LockType.GESTURE);
        assertTrue(mLocker.getStatus().hasAesEncryptionKey());
        assertTrue(mLocker.getStatus().hasOneTimePasswordKey());

    }
}

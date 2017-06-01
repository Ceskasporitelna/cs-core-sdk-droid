package cz.csas.cscore.locker;

import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.CallbackBasic;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.error.CsLockerError;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;

/**
 * The type Locker registration test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 05 /12/15.
 */
public class LockerRegisterNoAccessTokenTest extends LockerTest {

    private final String X_JUDGE_CASE_HEADER_REGISTER_NO_ACCESS_TOKEN = "core.locker.registerNoAccesToken.sanitized";
    private final String X_JUDGE_SESSION_HEADER_REGISTER_NO_ACCESS_TOKEN = "core.locker.registerNoAccesToken.session";
    private CountDownLatch mRegisterSignal;
    private CsSDKError mError;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_REGISTER_NO_ACCESS_TOKEN;
        super.setUp();
        mRegisterSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_HEADER_REGISTER_NO_ACCESS_TOKEN, mXJudgeSessionHeader);
    }


    /**
     * Test.
     */
    @Test
    public void testLockerRegisterNoAccessToken() {
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
                mRegisterSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mError = error;
                mRegisterSignal.countDown();
            }
        });

        mLocker.processUrl(Constants.CODE_URL_TEST);

        try {
            mRegisterSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // no access token => unregistered.
        assertEquals(CsLockerError.Kind.NO_ACCESS_TOKEN, ((CsLockerError) mError.getCause()).getKind());
        assertEquals(State.USER_UNREGISTERED, mLocker.getStatus().getState());

    }
}

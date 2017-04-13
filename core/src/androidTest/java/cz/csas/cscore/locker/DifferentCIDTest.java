package cz.csas.cscore.locker;

import android.support.test.InstrumentationRegistry;

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
import static org.junit.Assert.assertNotEquals;

/**
 * The type Different cid test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 25 /02/16.
 */
public class DifferentCIDTest extends LockerTest {

    private final String X_JUDGE_CASE_HEADER_REGISTER_SECOND_USER = "core.locker.register.second.user.sanitized";
    private final String X_JUDGE_SESSION_HEADER_REGISTER_SECOND_USER = "core.locker.register.second.user.session";
    private CountDownLatch mRegisterSignal;
    private CountDownLatch mUnregisterSignal;
    private CountDownLatch mRegisterSignalSecondUser;
    private String mCid;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_REGISTER_SECOND_USER;
        super.setUp();
        mRegisterSignal = new CountDownLatch(1);
        mRegisterSignalSecondUser = new CountDownLatch(1);
        mUnregisterSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);

        mLocker.setOnLockerStatusChangeListener(new OnLockerStatusChangeListener() {
            @Override
            public void onLockerStatusChanged(State state) {
            }
        });
        JudgeUtils.setJudge(mJudgeClient, cz.csas.cscore.judge.Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
    }


    /**
     * Test.
     */
    @Test
    public void testDifferentCID() {
        mLocker.register(InstrumentationRegistry.getTargetContext(), new CallbackBasic<LockerRegistrationProcess>() {
            @Override
            public void success(LockerRegistrationProcess lockerRegistrationProcess) {
                lockerRegistrationProcess.finishRegistration(new Password(LockType.GESTURE, cz.csas.cscore.judge.Constants.PASSWORD_TEST));
            }

            @Override
            public void failure() {
                mRegisterSignal.countDown();
            }
        }, new Callback<RegistrationOrUnlockResponse>() {
            @Override
            public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
                mRegisterSignal.countDown();
            }

            @Override
            public void failure(CsRestError error) {
                mRegisterSignal.countDown();
            }
        });

        mLocker.processUrl(cz.csas.cscore.judge.Constants.CODE_URL_TEST);

        try {
            mRegisterSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mCid = mLocker.getStatus().getClientId();
        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_UNREGISTER, mXJudgeSessionHeader);

        mLocker.unregister(new CallbackBasic<LockerStatus>() {

            @Override
            public void success(LockerStatus lockerStatus) {

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

        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_HEADER_REGISTER_SECOND_USER, mXJudgeSessionHeader);
        mLocker.register(InstrumentationRegistry.getTargetContext(), new CallbackBasic<LockerRegistrationProcess>() {
            @Override
            public void success(LockerRegistrationProcess lockerRegistrationProcess) {
                lockerRegistrationProcess.finishRegistration(new Password(LockType.GESTURE, cz.csas.cscore.judge.Constants.PASSWORD_TEST));
            }

            @Override
            public void failure() {
                mRegisterSignalSecondUser.countDown();
            }
        }, new Callback<RegistrationOrUnlockResponse>() {
            @Override
            public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
                mRegisterSignalSecondUser.countDown();
            }

            @Override
            public void failure(CsRestError error) {
                mRegisterSignalSecondUser.countDown();
            }
        });

        mLocker.processUrl(cz.csas.cscore.judge.Constants.CODE_URL_TEST);

        try {
            mRegisterSignalSecondUser.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(Constants.CID_TEST, mCid);
        assertEquals(Constants.SECOND_CID_TEST, mLocker.getStatus().getClientId());
        assertNotEquals(mCid, mLocker.getStatus().getClientId());

    }
}

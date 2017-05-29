package cz.csas.cscore.locker;

import junit.framework.Assert;

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
import static junit.framework.Assert.assertTrue;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06/12/15.
 */
public class LockerPasswordTest extends LockerTest {

    private final String X_JUDGE_CASE_HEADER_PASSWORD = "core.locker.password";
    private final String X_JUDGE_SESSION_HEADER_PASSWORD = "core.locker.password.session";
    private CountDownLatch mPasswordSignal;
    private PasswordResponse mPasswordResponse;
    private State mState;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_PASSWORD;
        super.setUp();
        mPasswordSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);

        mLocker.setOnLockerStatusChangeListener(new OnLockerStatusChangeListener() {
            @Override
            public void onLockerStatusChanged(State state) {
                mState = state;
            }
        });
        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
        LockerUtils.lockerRegister(mLocker);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_HEADER_PASSWORD, mXJudgeSessionHeader);
    }

    @Test
    public void testPassword(){

        mLocker.changePassword(Constants.PASSWORD_TEST, new Password(LockType.PIN, Constants.NEW_PASSWORD_TEST), new CsCallback<PasswordResponse>() {
            @Override
            public void success(PasswordResponse passwordResponse, Response response) {
                mPasswordResponse = passwordResponse;
                mPasswordSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mPasswordSignal.countDown();
            }
        });

        try {
            mPasswordSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(LockType.PIN, mLocker.getStatus().getLockType());
        assertEquals(null, mPasswordResponse);
        Assert.assertEquals(mLocker.getStatus().getState(), State.USER_UNLOCKED);
        assertTrue(mLocker.getStatus().hasOneTimePasswordKey());
        assertTrue(mLocker.getStatus().hasAesEncryptionKey());
    }

}

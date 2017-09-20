package cz.csas.cscore.locker;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;

/**
 * The type Locker unlock success test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /12/15.
 */
public class LockerUnlockMigrationPasswordServerError extends LockerTest {

    private final String X_JUDGE_SESSION_HEADER_UNLOCK_MIGRATION_PASSWORD_SERVER_ERROR = "core.locker.unlockMigration.password.serverError";
    private CountDownLatch mUnlockSignal;
    private State mState;
    private CsRestError mError;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_UNLOCK_MIGRATION_PASSWORD_SERVER_ERROR;
        super.setUp();
        mUnlockSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);

        mLocker.setOnLockerStatusChangeListener(new OnLockerStatusChangeListener() {
            @Override
            public void onLockerStatusChanged(State state) {
                mState = state;
            }
        });
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_SESSION_HEADER_UNLOCK_MIGRATION_PASSWORD_SERVER_ERROR, mXJudgeSessionHeader);
    }

    /**
     * Test.
     */
    @Test
    public void testUnlockMigrationPasswordServerError() {

        mLocker.unlockAfterMigration(new Password(LockType.FINGERPRINT, Constants.PASSWORD_TEST),
                new PasswordHashProcess() {
                    @Override
                    public String hashPassword(String password) {
                        return mCryptoManager.encodeSha256(password, Constants.DFP_TEST);
                    }
                },
                new LockerMigrationData.Builder()
                        .setClientId(Constants.CID_TEST)
                        .setDeviceFingerprint(Constants.DFP_TEST)
                        .setOneTimePasswordKey(Constants.ONE_TIME_PASSWORD_KEY_TEST)
                        .create(),
                new CsCallback<RegistrationOrUnlockResponse>() {
                    @Override
                    public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
                        mUnlockSignal.countDown();
                    }

                    @Override
                    public void failure(CsSDKError error) {
                        mError = (CsRestError) error;
                        mUnlockSignal.countDown();
                    }
                });


        try {
            mUnlockSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(State.USER_UNREGISTERED, mState);
        assertEquals(mError.getKind(), CsRestError.Kind.HTTP);
        assertEquals(mError.getResponse().getStatus(), 500);
    }
}

package cz.csas.cscore.locker;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * The type Locker unlock success test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /12/15.
 */
public class LockerUnlockMigration extends LockerTest {

    private final String X_JUDGE_SESSION_HEADER_UNLOCK_MIGRATION = "core.locker.unlockMigration";
    private CountDownLatch mUnlockSignal;
    private RegistrationOrUnlockResponse mUnlockResponse;
    private State mState;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_UNLOCK_MIGRATION;
        super.setUp();
        mUnlockSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);

        mLocker.setOnLockerStatusChangeListener(new OnLockerStatusChangeListener() {
            @Override
            public void onLockerStatusChanged(State state) {
                mState = state;
            }
        });
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_SESSION_HEADER_UNLOCK_MIGRATION, mXJudgeSessionHeader);
    }

    /**
     * Test.
     */
    @Test
    public void testUnlockMigration() {

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
                        .setEncryptionKey(Constants.SEK_BASE64_TEST)
                        .create(),
                new CsCallback<RegistrationOrUnlockResponse>() {
                    @Override
                    public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
                        mUnlockResponse = registrationOrUnlockResponse;
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

        assertEquals(State.USER_UNLOCKED, mState);
        assertEquals(mUnlockResponse.getAccessToken().getAccessToken(), Constants.ACCESS_TOKEN_TEST);
        assertEquals(mUnlockResponse.getAccessToken().getAccessTokenExpiration(), Constants.ACCESS_TOKEN_EXPIRATION_TEST);
        assertTrue(mLocker.getStatus().hasAesEncryptionKey());
        assertTrue(mLocker.getStatus().hasOneTimePasswordKey());
        assertEquals(mLocker.getAccessToken().getAccessToken(), mUnlockResponse.getAccessToken().getAccessToken());
        assertEquals(mLocker.getAccessToken().getAccessTokenExpiration(), mUnlockResponse.getAccessToken().getAccessTokenExpiration());
    }
}

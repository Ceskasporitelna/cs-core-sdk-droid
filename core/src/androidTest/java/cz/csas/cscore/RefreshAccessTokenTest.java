package cz.csas.cscore;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;
import cz.csas.cscore.locker.LockerStatus;
import cz.csas.cscore.locker.LockerUtils;

import static junit.framework.Assert.assertEquals;

/**
 * The type Refresh access token test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 11 /04/16.
 */
public class RefreshAccessTokenTest extends LockerTest {

    private final String X_JUDGE_CASE_HEADER_REFRESH = "core.locker.refreshAccessToken.sanitized";
    private final String X_JUDGE_SESSION_HEADER_REFRESH = "core.locker.refreshAccessToken.session";
    private CountDownLatch mRefreshSignal;
    private LockerStatus mLockerStatus;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_REFRESH;
        super.setUp();
        mRefreshSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);
        JudgeUtils.setJudge(mJudgeClient, cz.csas.cscore.judge.Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
        LockerUtils.lockerRegister(mLocker);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_HEADER_REFRESH, mXJudgeSessionHeader);
    }

    /**
     * Test.
     */
    @Test
    public void testRefreshToken() {
        mLocker.refreshToken(new Callback<LockerStatus>() {
            @Override
            public void success(LockerStatus lockerStatus, Response response) {
                mLockerStatus = lockerStatus;
                mRefreshSignal.countDown();
            }

            @Override
            public void failure(CsRestError error) {
                mRefreshSignal.countDown();
            }
        });

        try {
            mRefreshSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String expectedExpiration = String.valueOf(Constants.TIME_TEST + (3600 - 5) * 1000);

        assertEquals(expectedExpiration, mLockerStatus.getAccessTokenExpiration());
        assertEquals(expectedExpiration, mLocker.getAccessToken().getAccessTokenExpiration());
        assertEquals(Constants.ACCESS_TOKEN_REFRESH_TEST, mLocker.getAccessToken().getAccessToken());

    }
}

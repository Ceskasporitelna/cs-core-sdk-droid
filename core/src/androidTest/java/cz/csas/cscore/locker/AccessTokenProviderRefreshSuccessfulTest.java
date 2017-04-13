package cz.csas.cscore.locker;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.cscore.webapi.TestWebApiClient;
import cz.csas.cscore.webapi.User;
import cz.csas.cscore.webapi.UserListResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * The type Access token provider refresh successful test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 15 /05/16.
 */
public class AccessTokenProviderRefreshSuccessfulTest extends LockerTest {

    private final String X_JUDGE_CASE_ATP_REFRESH_SUCCESS = "accessTokenProvider.refresh.succesful.sanitized";
    private final String X_JUDGE_SESSION_ATP_REFRESH_SUCCESS = "accessTokenProvider.refresh.succesful.sanitized.session";
    private CountDownLatch mGetSignal;
    private List<User> mUsers;
    /**
     * The M test web api client.
     */
    protected TestWebApiClient mTestWebApiClient;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_ATP_REFRESH_SUCCESS;
        super.setUp();
        mGetSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);
        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
        LockerUtils.lockerRegister(mLocker);
        String fixedApiContextBaseUrl = mCoreSDK.getWebApiConfiguration().getEnvironment().getApiContextBaseUrl().replace("/webapi", "");
        mCoreSDK.getWebApiConfiguration().getEnvironment().setApiContextBaseUrl(fixedApiContextBaseUrl);
        mTestWebApiClient = new TestWebApiClient(mCoreSDK.getWebApiConfiguration());
        mTestWebApiClient.setAccessTokenProvider(mCoreSDK.getSharedContext());
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_ATP_REFRESH_SUCCESS, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    /**
     * Test create failed.
     */
    @Test
    public void testAccessTokenProviderRefreshSuccessful() {

        assertEquals(Constants.ACCESS_TOKEN_TEST, mLocker.getAccessToken().getAccessToken());
        assertFalse(CoreSDK.getInstance().isAccessTokenExpired());

        mTestWebApiClient.getUsersResource().list(null, new CallbackWebApi<UserListResponse>() {
            @Override
            public void success(UserListResponse userListResponse) {
                mUsers = userListResponse.getUsers();
                mGetSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mGetSignal.countDown();
            }
        });

        try {
            mGetSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(3, mUsers.get(0).getUserId());
        assertEquals("Captain America ", mUsers.get(0).getName());
        assertEquals(4, mUsers.get(1).getUserId());
        assertEquals("Mr Noname", mUsers.get(1).getName());
        assertEquals(Constants.ACCESS_TOKEN_REFRESH_TEST, mLocker.getAccessToken().getAccessToken());
        assertEquals(TimeUtils.getMilisecondsTimestamp(Constants.TIME_TEST, 3600), mLocker.getAccessToken().getAccessTokenExpiration());
    }
}

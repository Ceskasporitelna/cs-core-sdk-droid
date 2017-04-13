package cz.csas.cscore.locker;

import junit.framework.Assert;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;
import cz.csas.cscore.webapi.TestWebApiClient;
import cz.csas.cscore.webapi.UserListResponse;

import static junit.framework.TestCase.assertEquals;

/**
 * The type Access token provider refresh forbidden test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 15 /05/16.
 */
public class AccessTokenProviderRefreshForbiddenTest extends LockerTest {

    private final String X_JUDGE_CASE_ATP_REFRESH_FORBIDDEN = "accessTokenProvider.refresh.forbidden.sanitized";
    private final String X_JUDGE_SESSION_ATP_REFRESH_FORBIDDEN = "accessTokenProvider.refresh.forbidden.session";
    private CountDownLatch mGetSignal;
    private CsServerErrorResponse mError;
    /**
     * The M test web api client.
     */
    protected TestWebApiClient mTestWebApiClient;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_ATP_REFRESH_FORBIDDEN;
        super.setUp();
        mGetSignal = new CountDownLatch(1);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);
        String fixedApiContextBaseUrl = mCoreSDK.getWebApiConfiguration().getEnvironment().getApiContextBaseUrl().replace("/webapi", "");
        mCoreSDK.getWebApiConfiguration().getEnvironment().setApiContextBaseUrl(fixedApiContextBaseUrl);
        mTestWebApiClient = new TestWebApiClient(mCoreSDK.getWebApiConfiguration());
        mTestWebApiClient.setAccessTokenProvider(mCoreSDK.getSharedContext());
        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
        LockerUtils.lockerRegister(mLocker);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_ATP_REFRESH_FORBIDDEN, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    /**
     * Test create failed.
     */
    @Test
    public void testAccessTokenProviderRefreshForbidden() {

        Assert.assertEquals(Constants.ACCESS_TOKEN_TEST, mLocker.getAccessToken().getAccessToken());
        Assert.assertEquals(Constants.ACCESS_TOKEN_EXPIRATION_TEST, mLocker.getAccessToken().getAccessTokenExpiration());

        mTestWebApiClient.getUsersResource().list(null, new CallbackWebApi<UserListResponse>() {
            @Override
            public void success(UserListResponse userListResponse) {
                mGetSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mError = ((CsRestError) error).getResponse().getBodyObject(CsServerErrorResponse.class);
                mGetSignal.countDown();
            }
        });

        try {
            mGetSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(403,mError.getStatus());
        assertEquals("OAUTH2 failed to TOKEN_INFO with response: {\"error\":\"invalid_request\",\"error_description\":\"Token or Scope not found or invalid\"}", mError.getErrors().get(0).getError());

    }
}

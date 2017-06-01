package cz.csas.cscore.webapi;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.RestClient;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 22/02/16.
 */
public class GetServerNotFoundTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_USERS_UPDATE = "webapi.posts.list.server.not.available";
    private CountDownLatch mSignal;
    private CsSDKError mError;

    @Override
    public void setUp() {
        super.setUp();
        mSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_USERS_UPDATE, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        headers.put(RestClient.CONTENT_TYPE_HEADER_NAME, RestClient.CONTENT_TYPE_HEADER_VALUE_JSON);
        mTestWebApiClient.setGlobalHeaders(headers);
    }


    @Test
    public void testUpdate() {
        mTestWebApiClient.callApi(CallApiType.NORMAL, "posts", Method.GET, null, null, null, null, new CsCallback<Object>() {
            @Override
            public void success(Object o, Response response) {
                mSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mError = error;
                mSignal.countDown();
            }
        }, null);

        try {
            mSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(((CsRestError) mError).getResponse().getStatus(), 500);
        assertEquals(((CsRestError) mError).getKind(), CsRestError.Kind.HTTP);
    }
}

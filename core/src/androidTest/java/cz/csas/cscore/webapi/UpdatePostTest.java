package cz.csas.cscore.webapi;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * The type Update post test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 04 /05/16.
 */
public class UpdatePostTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_POSTS_UPDATE = "webapi.posts.update";
    private CountDownLatch mUpdateSignal;
    private WebApiEmptyResponse mWebApiEmptyResponse;

    @Override
    public void setUp() {
        super.setUp();
        mUpdateSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_POSTS_UPDATE, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    /**
     * Test update.
     */
    @Test
    public void testUpdate() {
        UpdatePostRequest request = new UpdatePostRequest(1, 1, "update sunt aut facere repellat provident occaecati excepturi optio reprehenderit", "update quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");
        mTestWebApiClient.getPostsResource().withId("1").update(request, new CallbackWebApi<WebApiEmptyResponse>() {
            @Override
            public void success(WebApiEmptyResponse webApiEmptyResponse) {
                mWebApiEmptyResponse = webApiEmptyResponse;
                mUpdateSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                CsSDKError error1 = error;
                mUpdateSignal.countDown();
            }
        });


        try {
            mUpdateSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNotNull(mWebApiEmptyResponse);
        assertEquals(PostResource.class, mWebApiEmptyResponse.getResource().getClass());
    }
}
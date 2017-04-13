package cz.csas.cscore.webapi;

import android.os.Looper;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.RestClient;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * The type Get test.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 26 /12/15.
 */
public class GetTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_POSTS_GET = "webapi.posts.get";
    private CountDownLatch mGetSignal;
    private int mPostId;
    private int mPostUserId;

    @Override
    public void setUp() {
        super.setUp();
        mGetSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_POSTS_GET, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        headers.put(RestClient.CONTENT_TYPE_HEADER_NAME, RestClient.CONTENT_TYPE_HEADER_VALUE_JSON);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    /**
     * Test get.
     */
    @Test
    public void testGet(){
        mTestWebApiClient.getPostsResource().withId(1).get(new CallbackWebApi<Post>() {
            @Override
            public void success(Post post) {
                mPostId = post.getId();
                mPostUserId = post.getUserId();
                assertTrue((Looper.myLooper() == Looper.getMainLooper()));
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

        assertEquals(1,mPostId);
        assertEquals(1,mPostUserId);
    }
}

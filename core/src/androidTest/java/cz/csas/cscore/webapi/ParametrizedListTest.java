package cz.csas.cscore.webapi;

import android.os.Looper;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.RestClient;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 27/12/15.
 */
public class ParametrizedListTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_POSTS_PARAMETRIZED_LIST = "webapi.posts.parametrized.list";
    private CountDownLatch mListSignal;
    private List<Post> posts;

    @Override
    public void setUp() {
        super.setUp();
        mListSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_POSTS_PARAMETRIZED_LIST, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        headers.put(RestClient.CONTENT_TYPE_HEADER_NAME, RestClient.CONTENT_TYPE_HEADER_VALUE_JSON);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    @Test
    public void testParametrizedList(){
        PostListParameters parameters = new PostListParameters(1);
        mTestWebApiClient.getPostsResource().list(parameters, new CallbackWebApi<PostListResponse>() {
            @Override
            public void success(PostListResponse postListResponse) {
                posts = postListResponse.getPosts();
                assertTrue((Looper.myLooper() == Looper.getMainLooper()));
                mListSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mListSignal.countDown();
            }
        });

        try {
            mListSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNotNull(posts);
        for (int i = 0; i < posts.size();i++){
            assertEquals(1,posts.get(i).getUserId());
        }
    }
}

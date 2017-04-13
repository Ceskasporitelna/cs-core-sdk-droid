package cz.csas.cscore.webapi;

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

import static junit.framework.Assert.assertNotNull;

/**
 * The type List not found test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 07 /02/16.
 */
public class ListNotFoundTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_POSTS_LIST_NOT_FOUND = "webapi.posts.list.notFound";
    private CountDownLatch mListSignal;
    private List<Post> mPosts;

    @Override
    public void setUp() {
        super.setUp();
        mListSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_POSTS_LIST_NOT_FOUND, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        headers.put(RestClient.CONTENT_TYPE_HEADER_NAME, RestClient.CONTENT_TYPE_HEADER_VALUE_JSON);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    /**
     * Test list not found.
     */
    @Test
    public void testListNotFound(){
        mTestWebApiClient.getPostsResource().list(null,new CallbackWebApi<PostListResponse>() {
            @Override
            public void success(PostListResponse postListResponse) {
                mPosts = postListResponse.getPosts();
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

        assertNotNull(mPosts);
    }

}

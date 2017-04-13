package cz.csas.cscore.webapi;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.error.CsSigningError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;
import cz.csas.cscore.webapi.signing.AuthorizationType;
import cz.csas.cscore.webapi.signing.FilledSigningObject;
import cz.csas.cscore.webapi.signing.MobileCaseSigningProcess;
import cz.csas.cscore.webapi.signing.SigningObject;
import cz.csas.cscore.webapi.signing.SigningState;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * The type Create signable immutable test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 04 /05/16.
 */
public class CreateSignableImmutableAuthTypeInvalidTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_POSTS_CREATE_SIGNABLE_INVALID = "webapi.posts.create.signable.immutable.authorizationType.invalid";
    private CountDownLatch mCreateSignal;
    private CountDownLatch mSigningSignalGET;
    private CountDownLatch mSigningSignalPOST;
    private CsSDKError mError;
    private Post mPost;
    private SigningObject mSigningObject;
    private FilledSigningObject mFilledSigningObject;
    private MobileCaseSigningProcess mMobileCaseSigningProcess;

    @Override
    public void setUp() {
        super.setUp();
        mCreateSignal = new CountDownLatch(1);
        mSigningSignalGET = new CountDownLatch(1);
        mSigningSignalPOST = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_POSTS_CREATE_SIGNABLE_INVALID, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    /**
     * Test create.
     */
    @Test
    public void testCreateSignableImmutableAuthTypeInvalid() {
        CreatePostRequest createPostRequest = new CreatePostRequest(1, 1, "sunt aut facere repellat provident occaecati excepturi optio reprehenderit", "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");
        mTestWebApiClient.getPostsResource().create(createPostRequest, new CallbackWebApi<Post>() {
            @Override
            public void success(Post post) {
                mPost = post;
                mCreateSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mCreateSignal.countDown();
            }
        });

        try {
            mCreateSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(1, mPost.getUserId());
        assertEquals(1, mPost.getId());
        assertEquals("sunt aut facere repellat provident occaecati excepturi optio reprehenderit", mPost.getTitle());
        assertEquals("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto", mPost.getBody());

        mSigningObject = mPost.signing();

        assertNotNull(mSigningObject);
        assertEquals("876598766654", mSigningObject.getSignId());
        assertEquals(SigningState.OPEN, mSigningObject.getSigningState());
        assertEquals(SigningState.OPEN, mPost.signing().getSigningState());

        mSigningObject.getInfo(new CallbackWebApi<FilledSigningObject>() {
            @Override
            public void success(FilledSigningObject filledSigningObject) {
                mFilledSigningObject = filledSigningObject;
                mSigningSignalGET.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mSigningSignalGET.countDown();
            }
        });

        try {
            mSigningSignalGET.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNotNull(mFilledSigningObject);
        assertEquals("876598766654", mFilledSigningObject.getSignId());
        assertEquals(SigningState.OPEN, mFilledSigningObject.getSigningState());
        assertEquals(SigningState.OPEN, mPost.signing().getSigningState());
        assertEquals(AuthorizationType.TAC, mFilledSigningObject.getAuthorizationType());
        assertTrue(mFilledSigningObject.getPossibleAuthorizationType().contains(AuthorizationType.TAC));
        assertTrue(mFilledSigningObject.canBeSignedWith(AuthorizationType.TAC));
        assertFalse(mFilledSigningObject.canBeSignedWith(AuthorizationType.MOBILE_CASE));
        assertEquals(AuthorizationType.TAC, mFilledSigningObject.getScenarios().get(0).get(0));


        mFilledSigningObject.startSigningWithMobileCase(new CallbackWebApi<MobileCaseSigningProcess>() {
            @Override
            public void success(MobileCaseSigningProcess mobileCaseSigningProcess) {
                mMobileCaseSigningProcess = mobileCaseSigningProcess;
                mSigningSignalPOST.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mError = error;
                mSigningSignalPOST.countDown();
            }
        });

        try {
            mSigningSignalPOST.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNull(mMobileCaseSigningProcess);
        assertEquals(CsSigningError.Kind.INVALID_AUTHORIZATION_TYPE, ((CsSigningError) mError).getKind());

    }
}
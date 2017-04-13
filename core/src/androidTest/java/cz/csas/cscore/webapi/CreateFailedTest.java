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

/**
 * The type Create failed test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 07 /02/16.
 */
public class CreateFailedTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_USERS_CREATE = "webapi.users.createFailed";
    private CountDownLatch mCreateSignal;
    private String mErrorMessage;
    private String mErrorCode;

    @Override
    public void setUp() {
        super.setUp();
        mCreateSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_USERS_CREATE, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    /**
     * Test create failed.
     */
    @Test
    public void testCreateFailed(){
        CreateUserRequest createUserRequest = new CreateUserRequest("Prince Charles","The Monarch","https://en.wikipedia.org/wiki/Charles,_Prince_of_Wales");

        mTestWebApiClient.getUsersResource().create(createUserRequest, new CallbackWebApi<UserDetail>() {
            @Override
            public void success(UserDetail userDetail) {
                mCreateSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                if(error instanceof WebApiTestError) {
                    mErrorMessage = ((WebApiTestError) error).getErrorMessage();
                    mErrorCode = ((WebApiTestError) error).getErrorCode();
                }
                mCreateSignal.countDown();
            }
        });

        try {
            mCreateSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("NAME_IS_NOT_PUNK_ENOUGH", mErrorCode);
        assertEquals("User creation failed. Name does not have enough PUNK in it.", mErrorMessage);

    }
}

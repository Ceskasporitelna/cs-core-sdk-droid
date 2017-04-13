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
 * The type Get on entity failure test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 07 /02/16.
 */
public class GetOnEntityFailureTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_USERS_DETAIL_42 = "webapi.users.detail.42";
    private final int DOG_ID = 42;
    private CountDownLatch mGetSignal;
    private String mErrorMessage;
    private String mErrorCode;


    @Override
    public void setUp() {
        super.setUp();
        mGetSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_USERS_DETAIL_42, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    /**
     * Test get on entity failure.
     */
    @Test
    public void testGetOnEntityFailure(){
        mTestWebApiClient.getUsersResource().withId(DOG_ID).get(new CallbackWebApi<UserDetail>() {
            @Override
            public void success(UserDetail userDetail) {
                mGetSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                if(error instanceof WebApiTestError) {
                    mErrorMessage = ((WebApiTestError) error).getErrorMessage();
                    mErrorCode = ((WebApiTestError) error).getErrorCode();
                }
                mGetSignal.countDown();
            }
        });

        try {
            mGetSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("Sadly, this is not human", mErrorMessage);
        assertEquals("THIS_IS_NOT_HUMAN",mErrorCode);
    }
}

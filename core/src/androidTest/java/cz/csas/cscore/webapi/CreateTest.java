package cz.csas.cscore.webapi;

import android.os.Looper;

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
import static junit.framework.Assert.assertTrue;

/**
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 26/12/15.
 */
public class CreateTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_USERS_CREATE = "webapi.users.create";
    private CountDownLatch mCreateSignal;
    private UserDetail mUserDetail;

    @Override
    public void setUp() {
        super.setUp();
        mCreateSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_USERS_CREATE, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    @Test
    public void testCreate(){
        CreateUserRequest createUserRequest = new CreateUserRequest("Iron Man","Rich Scientist","https://en.wikipedia.org/wiki/Iron_Man");
        mTestWebApiClient.getUsersResource().create(createUserRequest, new CallbackWebApi<UserDetail>() {
            @Override
            public void success(UserDetail userDetail) {
                mUserDetail = userDetail;
                assertTrue((Looper.myLooper() == Looper.getMainLooper()));
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

        assertEquals(5,mUserDetail.getUserId());
        assertEquals("Iron Man",mUserDetail.getName());
        assertEquals("Rich Scientist",mUserDetail.getPosition());
        assertEquals( "https://en.wikipedia.org/wiki/Iron_Man",mUserDetail.getFullProfileUrl());
    }
}

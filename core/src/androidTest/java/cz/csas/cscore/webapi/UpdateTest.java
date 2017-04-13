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
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 27/12/15.
 */
public class UpdateTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_USERS_UPDATE = "webapi.users.update";
    private CountDownLatch mUpdateSignal;
    private UserDetail mUserDetail;

    @Override
    public void setUp() {
        super.setUp();
        mUpdateSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_USERS_UPDATE, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        headers.put(RestClient.CONTENT_TYPE_HEADER_NAME, RestClient.CONTENT_TYPE_HEADER_VALUE_JSON);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    @Test
    public void testUpdate(){
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Iron Man","Poor Scientist","https://en.wikipedia.org/wiki/Iron_Man");
        mTestWebApiClient.getUsersResource().withId(5).update(updateUserRequest, new CallbackWebApi<UserDetail>() {
            @Override
            public void success(UserDetail userDetail) {
                mUserDetail = userDetail;
                assertTrue((Looper.myLooper() == Looper.getMainLooper()));
                mUpdateSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mUpdateSignal.countDown();
            }
        });

        try {
            mUpdateSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(5,mUserDetail.getUserId());
        assertEquals("Iron Man",mUserDetail.getName());
        assertEquals("Poor Scientist",mUserDetail.getPosition());
        assertEquals("https://en.wikipedia.org/wiki/Iron_Man",mUserDetail.getFullProfileUrl());
    }
}

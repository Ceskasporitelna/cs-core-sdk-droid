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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 28/12/15.
 */
public class GetOnEntityTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_USERS_DETAIL_1 = "webapi.users.detail.1";
    private CountDownLatch mListSignal;
    private CountDownLatch mGetSignal;
    private User mUser;
    private UserDetail mUserDetail;

    @Override
    public void setUp() {
        super.setUp();
        mListSignal = new CountDownLatch(1);
        mGetSignal = new CountDownLatch(1);

        UserListParameters userListParameters = new UserListParameters.Builder()
                .setPagination(new Pagination(1,2))
                .setSortBy(Sort.by(UserSortableFields.other("name"), SortDirection.ASCENDING).thenBy(UserSortableFields.USER_ID,SortDirection.DESCENDING))
                .create();

        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_USERS_LIST_PAGINATED_1, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mTestWebApiClient.setGlobalHeaders(headers);
        mTestWebApiClient.getUsersResource().list(userListParameters, new CallbackWebApi<UserListResponse>() {
            @Override
            public void success(UserListResponse userListResponse) {
                mUser = userListResponse.getUsers().get(0);
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

        assertNotNull(mUser);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_USERS_DETAIL_1, mXJudgeSessionHeader);

    }

    @Test
    public void testGetOnEntity(){
        mUser.get(new CallbackWebApi<UserDetail>() {
            @Override
            public void success(UserDetail userDetail) {
                mUserDetail = userDetail;
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

        assertEquals("Gordon Freeman", mUserDetail.getName());
        assertEquals(1, mUserDetail.getUserId());
        assertEquals("Scientist", mUserDetail.getPosition());
        assertEquals("https://en.wikipedia.org/wiki/Gordon_Freeman",mUserDetail.getFullProfileUrl());

    }
}

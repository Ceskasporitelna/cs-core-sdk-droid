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
 * @since 29/12/15.
 */
public class PaginatedListTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_USERS_LIST_PAGINATED_2 = "webapi.users.list.paginated.2";
    private CountDownLatch mList1Signal;
    private CountDownLatch mList2Signal;
    private UserListResponse mUserPaginatedListResponse1;
    private UserListResponse mUserPaginatedListResponse2;

    @Override
    public void setUp() {
        super.setUp();
        mList1Signal = new CountDownLatch(1);
        mList2Signal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_USERS_LIST_PAGINATED_1, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        headers.put(RestClient.CONTENT_TYPE_HEADER_NAME, RestClient.CONTENT_TYPE_HEADER_VALUE_JSON);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    @Test
    public void testPaginatedList(){

        UserListParameters userListParameters = new UserListParameters.Builder()
                .setPagination(new Pagination(1,2))
                .setSortBy(Sort.by(UserSortableFields.NAME, SortDirection.ASCENDING).thenBy(UserSortableFields.USER_ID, SortDirection.DESCENDING))
                .create();

        mTestWebApiClient.getUsersResource().list(userListParameters, new CallbackWebApi<UserListResponse>() {
            @Override
            public void success(UserListResponse userListResponse) {
                mUserPaginatedListResponse1 = userListResponse;
                assertTrue((Looper.myLooper() == Looper.getMainLooper()));
                mList1Signal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mList1Signal.countDown();
            }
        });

        try {
            mList1Signal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("Gordon Freeman", mUserPaginatedListResponse1.getUsers().get(0).getName());
        assertEquals(1, mUserPaginatedListResponse1.getUsers().get(0).getUserId());

        assertTrue(mUserPaginatedListResponse1.hasNextPage());

        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_USERS_LIST_PAGINATED_2, mXJudgeSessionHeader);

        mUserPaginatedListResponse1.nextPage(new CallbackWebApi<UserListResponse>() {

            @Override
            public void success(UserListResponse userListResponse) {
                mUserPaginatedListResponse2 = userListResponse;
                assertTrue((Looper.myLooper() == Looper.getMainLooper()));
                mList2Signal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mList2Signal.countDown();
            }
        });

        try {
            mList2Signal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("Luke Skywalker", mUserPaginatedListResponse2.getUsers().get(0).getName());
        assertEquals(3, mUserPaginatedListResponse2.getUsers().get(0).getUserId());
    }
}

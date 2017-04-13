package cz.csas.cscore.webapi;

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
public class PaginatedParametrizedTransformationListTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_USERS_LIST_PAGINATED_PARAMETRIZED_TRANSFORMATION_1 = "webapi.users.list.paginated.parametrized.transformation.1";
    private final String X_JUDGE_CASE_USERS_LIST_PAGINATED_PARAMETRIZED_TRANSFORMATION_2 = "webapi.users.list.paginated.parametrized.transformation.2";
    private CountDownLatch mList1Signal;
    private CountDownLatch mList2Signal;
    private UserListResponse mUserListResponse1;
    private UserListResponse mUserListResponse2;

    @Override
    public void setUp() {
        super.setUp();
        mList1Signal = new CountDownLatch(1);
        mList2Signal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_USERS_LIST_PAGINATED_PARAMETRIZED_TRANSFORMATION_1, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        headers.put(RestClient.CONTENT_TYPE_HEADER_NAME, RestClient.CONTENT_TYPE_HEADER_VALUE_JSON);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    @Test
    public void testPaginatedParametrizedTransformationList(){
        UserListParameters parameters = new UserListParameters(new Pagination(1,2),null,Gender.MALE);
        mTestWebApiClient.getUsersResource().list(parameters, new CallbackWebApi<UserListResponse>() {
            @Override
            public void success(UserListResponse userListResponse) {
                mUserListResponse1 = userListResponse;
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

        assertEquals("Mr Noname", mUserListResponse1.getUsers().get(0).getName());
        assertEquals(1, mUserListResponse1.getUsers().get(0).getUserId());
        assertEquals("Marigold", mUserListResponse1.getUsers().get(1).getName());
        assertEquals(2, mUserListResponse1.getUsers().get(1).getUserId());

        assertTrue(mUserListResponse1.hasNextPage());

        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_USERS_LIST_PAGINATED_PARAMETRIZED_TRANSFORMATION_2, mXJudgeSessionHeader);

        mUserListResponse1.nextPage(new CallbackWebApi<UserListResponse>() {

            @Override
            public void success(UserListResponse userListResponse) {
                mUserListResponse2 = userListResponse;
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

        assertEquals("Captain America ", mUserListResponse2.getUsers().get(0).getName());
        assertEquals(3, mUserListResponse2.getUsers().get(0).getUserId());
        assertEquals("Mr Noname", mUserListResponse2.getUsers().get(1).getName());
        assertEquals(4, mUserListResponse2.getUsers().get(1).getUserId());
    }
}

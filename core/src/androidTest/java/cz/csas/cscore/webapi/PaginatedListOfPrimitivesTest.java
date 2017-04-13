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

/**
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 29/12/15.
 */
public class PaginatedListOfPrimitivesTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_NOTES_LIST_PAGINATED_1 = "webapi.notes.list.paginated.1";
    private CountDownLatch mList1Signal;
    private CountDownLatch mList2Signal;
    private NotesPaginatedListResponse mNotesPaginatedListResponse1;
    private NotesPaginatedListResponse mNotesPaginatedListResponse2;

    @Override
    public void setUp() {
        super.setUp();
        mList1Signal = new CountDownLatch(1);
        mList2Signal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_NOTES_LIST_PAGINATED_1, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        headers.put(RestClient.CONTENT_TYPE_HEADER_NAME, RestClient.CONTENT_TYPE_HEADER_VALUE_JSON);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    @Test
    public void testPaginatedListOfPrimitives(){
        NotesListPaginatedParameters notesListPaginatedParameters = new NotesListPaginatedParameters(new Pagination(1,2),null);
        mTestWebApiClient.getNotesResource().list(notesListPaginatedParameters, new CallbackWebApi<NotesPaginatedListResponse>() {
            @Override
            public void success(NotesPaginatedListResponse notesPaginatedListResponse) {
                mNotesPaginatedListResponse1 = notesPaginatedListResponse;
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

        assertEquals("This is my second cat note", mNotesPaginatedListResponse1.getItems().get(1));
        assertEquals(true, mNotesPaginatedListResponse1.hasNextPage());

        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_NOTES_LIST_PAGINATED_2, mXJudgeSessionHeader);

        mNotesPaginatedListResponse1.nextPage(new CallbackWebApi<NotesPaginatedListResponse>() {
            @Override
            public void success(NotesPaginatedListResponse notesPaginatedListResponse) {
                mNotesPaginatedListResponse2 = notesPaginatedListResponse;
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

        assertEquals("This is my fifth checked dog note", mNotesPaginatedListResponse2.getNotes().get(1));
        assertEquals(null,mNotesPaginatedListResponse2.getNextPage());
    }
}

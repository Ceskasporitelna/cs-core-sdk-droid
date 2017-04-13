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

import static junit.framework.TestCase.assertEquals;


/**
 * The type Paginated parametrized transformation list of primitives test.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 29 /12/15.
 */
public class PaginatedParametrizedTransformationListOfPrimitivesTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_NOTES_LIST_PAGINATED_PARAMETRIZED_TRANSFORMATION_1 = "webapi.notes.list.paginated.parametrized.transformation.2";
    private CountDownLatch mList1Signal;
    private NotesPaginatedListResponse mNotesPaginatedListResponse;

    @Override
    public void setUp() {
        super.setUp();
        mList1Signal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_NOTES_LIST_PAGINATED_PARAMETRIZED_TRANSFORMATION_1, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        headers.put(RestClient.CONTENT_TYPE_HEADER_NAME, RestClient.CONTENT_TYPE_HEADER_VALUE_JSON);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    /**
     * Test paginated parametrized transformation list of primitives.
     */
    @Test
    public void testPaginatedParametrizedTransformationListOfPrimitives(){
        NotesListPaginatedParameters notesListPaginatedParameters = new NotesListPaginatedParameters(new Pagination(2,2),Animal.DOG);
        mTestWebApiClient.getNotesResource().list(notesListPaginatedParameters, new CallbackWebApi<NotesPaginatedListResponse>() {
            @Override
            public void success(NotesPaginatedListResponse notesPaginatedListResponse) {
                mNotesPaginatedListResponse = notesPaginatedListResponse;
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

        assertEquals("This is my fifth checked dog note", mNotesPaginatedListResponse.getNotes().get(0));
        assertEquals(null,mNotesPaginatedListResponse.getNextPage());

    }
}

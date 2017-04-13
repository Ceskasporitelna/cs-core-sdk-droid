package cz.csas.cscore.webapi;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.RestClient;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 27/12/15.
 */
public class ListOfPrimitivesTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_NOTES_LIST = "webapi.notes.list";
    private CountDownLatch mListSignal;
    private List<String> notes;

    @Override
    public void setUp() {
        super.setUp();
        mListSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_NOTES_LIST, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        headers.put(RestClient.CONTENT_TYPE_HEADER_NAME, RestClient.CONTENT_TYPE_HEADER_VALUE_JSON);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    @Test
    public void testListOfPrimitives(){
        mTestWebApiClient.getNotesResource().list(new CallbackWebApi<NotesListResponse>() {
            @Override
            public void success(NotesListResponse notesListResponse) {
                notes = notesListResponse.getNotes();
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

        assertNotNull(notes);
        assertEquals("This is my third dog note", notes.get(2));
    }
}

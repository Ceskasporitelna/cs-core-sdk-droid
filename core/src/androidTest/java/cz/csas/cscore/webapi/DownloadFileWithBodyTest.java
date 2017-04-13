package cz.csas.cscore.webapi;

import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;
import cz.csas.cscore.utils.TimeUtils;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;

/**
 * The type Download file test.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 26 /04/16.
 */
public class DownloadFileWithBodyTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_FILE_UPLOAD = "webapi.file.export.withBody";
    private CountDownLatch mFileSignal;
    private WebApiStream mWebApiStream;

    @Override
    public void setUp() {
        super.setUp();
        mFileSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_FILE_UPLOAD, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mTestWebApiClient.setGlobalHeaders(headers);
    }


    /**
     * Test download file.
     */
    @Test
    public void testDownloadFileWithBody() {

        Date dateFrom = TimeUtils.getISO8601Date("1999-09-27T00:00:00+02:00");
        Date dateTo = TimeUtils.getISO8601Date("2000-09-27T00:00:00+02:00");
        DownloadFileRequest request = new DownloadFileRequest("Anna", "Aleš");


        DownloadFileParameters parameters = new DownloadFileParameters(Arrays.asList("export_field"), true, dateFrom, dateTo);
        mTestWebApiClient.getFileResource().downloadPost(parameters, request, new CallbackWebApi<WebApiStream>() {
            @Override
            public void success(WebApiStream webApiStream) {
                mWebApiStream = webApiStream;
                mFileSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mFileSignal.countDown();
            }
        });

        try {
            mFileSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNotNull(mWebApiStream);
        assertEquals("test-pdf.pdf", mWebApiStream.getFilename());
        assertEquals(Long.valueOf(7945), mWebApiStream.getContentLength());
        assertEquals("application/pdf", mWebApiStream.getContentType());
        assertEquals("attachment; filename=\"test-pdf.pdf\"", mWebApiStream.getContentDisposition());
    }
}

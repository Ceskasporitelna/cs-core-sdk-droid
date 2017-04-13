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
public class DownloadFileTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_FILE_DOWNLOAD = "webapi.file.export";
    private final String X_JUDGE_CASE_FILE_DOWNLOAD_GET = "webapi.file.export.get";
    private CountDownLatch mFileGetSignal;
    private CountDownLatch mFilePostSignal;
    private WebApiStream mWebApiStream;

    @Override
    public void setUp() {
        super.setUp();
        mFilePostSignal = new CountDownLatch(1);
        mFileGetSignal = new CountDownLatch(1);
        mXJudgeSessionHeader = "download-file-session-header";
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_FILE_DOWNLOAD, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mTestWebApiClient.setGlobalHeaders(headers);
    }


    /**
     * Test download file.
     */
    @Test
    public void testDownloadFile() {

        Date dateFrom = TimeUtils.getISO8601Date("1999-09-27T00:00:00+02:00");
        Date dateTo = TimeUtils.getISO8601Date("2000-09-27T00:00:00+02:00");

        DownloadFileParameters parameters = new DownloadFileParameters(Arrays.asList("export_field"), true, dateFrom, dateTo);
        mTestWebApiClient.getFileResource().downloadPost(parameters, null, new CallbackWebApi<WebApiStream>() {
            @Override
            public void success(WebApiStream webApiStream) {
                mWebApiStream = webApiStream;
                mFilePostSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mFilePostSignal.countDown();
            }
        });

        try {
            mFilePostSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNotNull(mWebApiStream);
        assertEquals("test-pdf.pdf", mWebApiStream.getFilename());
        assertEquals(Long.valueOf(7945), mWebApiStream.getContentLength());
        assertEquals("application/pdf", mWebApiStream.getContentType());
        assertEquals("attachment; filename=\"test-pdf.pdf\"", mWebApiStream.getContentDisposition());

        mWebApiStream = null;
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_FILE_DOWNLOAD_GET, mXJudgeSessionHeader);

        mTestWebApiClient.getFileResource().downloadGet(parameters, null, new CallbackWebApi<WebApiStream>() {
            @Override
            public void success(WebApiStream webApiStream) {
                mWebApiStream = webApiStream;
                mFileGetSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mFileGetSignal.countDown();
            }
        });

        try {
            mFileGetSignal.await(20, TimeUnit.SECONDS); // wait for callback
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

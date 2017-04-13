package cz.csas.cscore.webapi;

import android.os.Looper;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
import static junit.framework.TestCase.assertNotNull;

/**
 * The type Upload file test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /12/15.
 */
public class UploadFileTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_FILE_UPLOAD = "webapi.file.upload";
    private CountDownLatch mFileSignal;
    private UploadedFile mUploadedFile;

    @Override
    public void setUp() {
        super.setUp();
        mFileSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_FILE_UPLOAD, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        headers.put(RestClient.CONTENT_TYPE_HEADER_NAME, RestClient.CONTENT_TYPE_HEADER_VALUE_OCTET_STREAM);
        mTestWebApiClient.setGlobalHeaders(headers);
    }


    /**
     * Test upload file.
     */
    @Test
    public void testUploadFile(){
        File file = readFileFromResources();
        assertNotNull(file);

        CreateFileRequest createFileRequest = new CreateFileRequest(file,file.getName());
        mTestWebApiClient.getFileResource().upload(createFileRequest, new CallbackWebApi<UploadedFile>() {
            @Override
            public void success(UploadedFile uploadedFile) {
                mUploadedFile = uploadedFile;
                assertTrue((Looper.myLooper() == Looper.getMainLooper()));
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

        assertEquals("phREvNnb6rDadlCYGa5O", mUploadedFile.getId());
        assertTrue(mUploadedFile.isOk());
    }

    private File readFileFromResources(){

        File f = new File(InstrumentationRegistry.getContext().getCacheDir()+"/test-file.png");
        if (!f.exists()) try {

            InputStream is = InstrumentationRegistry.getContext().getAssets().open("test-file.png");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();


            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) { throw new RuntimeException(e); }
       return f;
    }
}

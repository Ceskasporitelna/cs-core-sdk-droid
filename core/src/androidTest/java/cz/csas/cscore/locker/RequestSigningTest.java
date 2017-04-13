package cz.csas.cscore.locker;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.RestClient;
import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.client.WebApiConfigurationImpl;
import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Header;
import cz.csas.cscore.client.rest.client.Request;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeClient;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 08/12/15.
 */
@RunWith(AndroidJUnit4.class)
public class RequestSigningTest  {

    private final String X_JUDGE_SESSION_HEADER_REQUEST_SIGNING = "core.locker.request.signing.session";
    private CountDownLatch registerSignal;
    private RestClient client;
    private String mXJudgeSessionHeader;
    private Request request;
    private int signatureIndex;
    private int nonceIndex;

    @Before
    public void setUp(){
        registerSignal = new CountDownLatch(1);
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_REQUEST_SIGNING;
        WebApiConfiguration webApiConfiguration = new WebApiConfigurationImpl();
        webApiConfiguration.setWebApiKey(Constants.WEB_API_KEY_SIGNING_TEST);
        webApiConfiguration.setPrivateSigningKey(Constants.PRIVATE_SIGNING_KEY_TEST);
        client = new LockerClient(webApiConfiguration);
        client.getRestAdapter().setNonce(Constants.NONCE_SIGNING);
        JudgeUtils.setJudge(new JudgeClient(Constants.TEST_BASE_URL),Constants.X_JUDGE_CASE_HEADER_REGISTER,mXJudgeSessionHeader);
    }

    @Test
    public void testRequestSigningFixedNonce(){
        signatureIndex = 0;
        nonceIndex = 0;

        ((LockerRestService)client.getService()).register(new LockerRequest("some_session", "some_data"), new Callback<LockerResponse>() {
            @Override
            public void success(LockerResponse lockerResponse, Response response) {
                registerSignal.countDown();
            }

            @Override
            public void failure(CsRestError error) {
                registerSignal.countDown();
            }
        });

        try {
            registerSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        request = client.getRestAdapter().getRequest();
        List<Header> headers = request.getHeaders();

        for (int i = 0; i < headers.size(); i++) {
            if(headers.get(i).getName().equals("signature"))
                signatureIndex = i;
            else if(headers.get(i).getName().equals("nonce"))
                nonceIndex = i;
        }

        /**
         * signature = webApiKey + nonce + url + data
         * url is not absolute. Strip server name
         *
         * f.e.
         * https://api.csas.cz/sandbox/webapi/api/v1/locker => /sandbox/webapi/api/v1/locker
         *
         */
        assertNotNull(request);
        assertEquals(headers.get(signatureIndex).getName(), "signature");
        assertEquals("/15pOf5UDowfasSFOPo9nIBMfq8=", headers.get(signatureIndex).getValue());
        assertEquals(headers.get(nonceIndex).getName(), "nonce");
        assertEquals("123456", headers.get(nonceIndex).getValue());
    }

    @Test
    public void testRequestSigningRandomNonce(){
        signatureIndex = 0;
        nonceIndex = 0;

        client.getRestAdapter().setNonce(null);
        ((LockerRestService)client.getService()).register(new LockerRequest("some_session", "some_data"), new Callback<LockerResponse>() {
            @Override
            public void success(LockerResponse lockerResponse, Response response) {
                registerSignal.countDown();
            }

            @Override
            public void failure(CsRestError error) {
                registerSignal.countDown();
            }
        });

        try {
            registerSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        request = client.getRestAdapter().getRequest();
        List<Header> headers = request.getHeaders();

        for (int i = 0; i < headers.size(); i++) {
            if(headers.get(i).getName().equals("signature"))
                signatureIndex = i;
            else if(headers.get(i).getName().equals("nonce"))
                nonceIndex = i;
        }

        assertNotNull(request);
        assertEquals(headers.get(signatureIndex).getName(), "signature");
        assertFalse("/15pOf5UDowfasSFOPo9nIBMfq8=".equals(headers.get(signatureIndex).getValue()));
        assertEquals(headers.get(nonceIndex).getName(), "nonce");
        assertFalse("123456".equals(headers.get(nonceIndex).getValue()));
    }
}

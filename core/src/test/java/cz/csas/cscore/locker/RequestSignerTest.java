package cz.csas.cscore.locker;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.client.WebApiConfigurationImpl;
import cz.csas.cscore.client.crypto.CryptoManager;
import cz.csas.cscore.client.crypto.CryptoManagerImpl;
import cz.csas.cscore.client.rest.CsRestAdapter;
import cz.csas.cscore.client.rest.RequestInterceptor;
import cz.csas.cscore.client.rest.RequestSigner;
import cz.csas.cscore.client.rest.RequestSignerImpl;

/**
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 07/12/15.
 */
public class RequestSignerTest extends TestCase {

    private RequestSigner requestSigner;
    private CsRestAdapter csRestAdapter;

    @Before
    public void setUp(){
        CryptoManager cryptoManager = new CryptoManagerImpl();
        final WebApiConfiguration webApiConfiguration = new WebApiConfigurationImpl();
        webApiConfiguration.setWebApiKey("adae3c38-be9a-4529-94d7-3c7a33c1201a");
        webApiConfiguration.setPrivateSigningKey("MDhiNjE4NmQtZjhlMi00ZmMzLTk3YmMtY2NhNmQ4N2FhNDZm");
        requestSigner = new RequestSignerImpl(webApiConfiguration);
        csRestAdapter = new CsRestAdapter.Builder()
                .setRequestSigner(requestSigner)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        if (webApiConfiguration.getPrivateSigningKey() != null)
                            request.signRequest();
                    }
                })
                .setEndpoint("http://www.droidrocks.cz")
                .build();
    }

    @Test
    public void testSignatureWithDataGeneration(){
        String signature = requestSigner.generateSignatureForRequest("/some/nice/path", "{\"someNice\":\"data\"}", "123456");
        assertEquals("TNjt8aVBNmDn76xTzfDAQaziDvA=", signature);
    }

    @Test
    public void testSignatureWithoutDataGeneration(){
        String signature = requestSigner.generateSignatureForRequest("/some/nice/path", "", "123456");
        assertEquals("1Yy8df6/CXueKUdN5xU4HXnjKO0=", signature);
    }

}

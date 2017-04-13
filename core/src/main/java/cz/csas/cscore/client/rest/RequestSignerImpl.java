package cz.csas.cscore.client.rest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.client.crypto.CryptoManager;
import cz.csas.cscore.client.crypto.CryptoManagerImpl;
import cz.csas.cscore.client.rest.client.Header;

/**
 * The type Request signer.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 07 /12/15.
 */
public class RequestSignerImpl implements RequestSigner {

    private final String REQUEST_SIGNING_SIGNATURE_HEADER_NAME = "signature";
    private final String NONCE_HEADER_NAME = "nonce";
    private WebApiConfiguration webApiConfiguration;
    private CryptoManager cryptoManager;
    private String nonce;

    /**
     * Instantiates a new Request signer.
     *
     * @param webApiConfiguration the config manager
     */
    public RequestSignerImpl(WebApiConfiguration webApiConfiguration) {
        this.webApiConfiguration = webApiConfiguration;
        this.cryptoManager = new CryptoManagerImpl();
    }

    @Override
    public void signRequest(String requestUrl, String requestData, List<Header> headers) {
        String signature = generateSignatureForRequest(stripServer(requestUrl), requestData, nonce);
        List<Header> localHeaders = headers;
        if (localHeaders == null) {
            headers = localHeaders = new ArrayList<Header>(2);
        }
        localHeaders.add(new Header(REQUEST_SIGNING_SIGNATURE_HEADER_NAME, signature));
        localHeaders.add(new Header(NONCE_HEADER_NAME, getNonce()));
    }

    @Override
    public String generateSignatureForRequest(String requestUrl, String requestData, String nonce) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(cryptoManager.decodeBase64(webApiConfiguration.getPrivateSigningKey()), "HmacSHA1");
            String payload = constructPayload(requestUrl, requestData, nonce);
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
            byte[] signature = mac.doFinal(payload.getBytes());
            return cryptoManager.encodeBase64(signature);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException e) {
            throw new IllegalStateException(e);
        }

    }

    /**
     * Set nonce.
     *
     * @param nonce the nonce
     */
    void setNonce(String nonce) {
        this.nonce = nonce;
    }

    private String constructPayload(String requestUrl, String requestData, String nonce) {
        return webApiConfiguration.getWebApiKey() + nonce + requestUrl + requestData;
    }

    private String getNonce() {
        if (nonce != null)
            return nonce;
        return UUID.randomUUID().toString();
    }

    private String stripServer(String absoluteUrl) {
        int i, j;
        if ((i = absoluteUrl.indexOf("://")) > 0 && (j = absoluteUrl.substring(i + 3).indexOf('/')) > 0 && (j += i + 3) + 1 < absoluteUrl.length())
            return absoluteUrl.substring(j);
        return absoluteUrl;
    }

}

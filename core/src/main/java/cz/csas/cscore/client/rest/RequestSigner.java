package cz.csas.cscore.client.rest;

import java.util.List;

import cz.csas.cscore.client.rest.client.Header;

/**
 * The interface Request signer.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 07 /12/15.
 */
public interface RequestSigner {

    /**
     * Sign request.
     *
     * @param requestUrl  the request url
     * @param requestData the request data
     * @param headers     the headers
     */
    public void signRequest(String requestUrl, String requestData, List<Header> headers);

    /**
     * Generate signature for request string.
     *
     * @param requestUrl  the request url
     * @param requestData the request data
     * @param nonce       the nonce
     * @return the string
     */
    public String generateSignatureForRequest(String requestUrl, String requestData, String nonce);

}

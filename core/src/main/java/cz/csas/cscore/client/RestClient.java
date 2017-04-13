package cz.csas.cscore.client;

import java.util.Map;

import cz.csas.cscore.client.rest.CsRestAdapter;

/**
 * The interface Rest client.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 10 /11/15.
 */
public interface RestClient {

    /**
     * The constant WEB_API_KEY_HEADER_NAME.
     */
    final String WEB_API_KEY_HEADER_NAME = "web-api-key";

    /**
     * The constant AUTHORIZATION_HEADER_NAME.
     */
    final String AUTHORIZATION_HEADER_NAME = "Authorization";

    /**
     * The constant ACCEPT_HEADER_NAME.
     */
    final String ACCEPT_HEADER_NAME = "Accept";

    /**
     * The constant ACCEPT_HEADER_VALUE.
     */
    final String ACCEPT_HEADER_VALUE = "application/json";

    /**
     * The constant ACCEPT_LANGUAGE_HEADER_NAME.
     */
    final String ACCEPT_LANGUAGE_HEADER_NAME = "Accept-Language";

    /**
     * The constant CONTENT_TYPE_HEADER_NAME.
     */
    final String CONTENT_TYPE_HEADER_NAME = "Content-Type";

    /**
     * The constant CONTENT_TYPE_HEADER_VALUE.
     */
    final String CONTENT_TYPE_HEADER_VALUE_JSON = "application/json";

    /**
     * The constant CONTENT_TYPE_HEADER_VALUE_WWW_URL_ENCODED.
     */
    final String CONTENT_TYPE_HEADER_VALUE_WWW_URL_ENCODED = "application/x-www-form-urlencoded";

    /**
     * The constant CONTENT_TYPE_HEADER_VALUE_OCTET_STREAM.
     */
    final String CONTENT_TYPE_HEADER_VALUE_OCTET_STREAM = "application/octet-stream";

    /**
     * Gets service.
     *
     * @return the service
     */
    public RestService getService();

    /**
     * Sets global headers.
     *
     * @param headers the headers
     */
    public void setGlobalHeaders(Map<String, String> headers);

    /**
     * Gets rest adapter.
     *
     * @return the rest adapter
     */
    public CsRestAdapter getRestAdapter();
}

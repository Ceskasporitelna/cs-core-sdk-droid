package cz.csas.cscore.judge;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Request.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 20 /11/15.
 */
public class Request {

    @CsExpose
    private String method;

    @CsExpose
    private String url;

    @CsExpose
    private String headers;

    @CsExpose
    private String data;

    /**
     * Gets method.
     *
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets headers.
     *
     * @return the headers
     */
    public String getHeaders() {
        return headers;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public String getData() {
        return data;
    }
}
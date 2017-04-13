package cz.csas.cscore.judge;

import java.util.List;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Response.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 20 /11/15.
 */
public class Response {

    @CsExpose
    private int code;

    @CsExpose
    private String headers;

    @CsExpose
    private List<String> data;

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
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
    public List<String> getData() {
        return data;
    }
}

package cz.csas.cscore.judge;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Test case.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 20 /11/15.
 */
public class TestCase {

    @CsExpose
    private Request request;

    @CsExpose
    private Response response;

    /**
     * Gets request.
     *
     * @return the request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Gets response.
     *
     * @return the response
     */
    public Response getResponse() {
        return response;
    }
}

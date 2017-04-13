package cz.csas.cscore.locker;

import cz.csas.cscore.client.rest.CsRequest;
import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Registration request.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 10 /11/15.
 */
class LockerRequest implements CsRequest {

    @CsExpose
    private String session;

    @CsExpose
    private String data;

    /**
     * Instantiates a new Registration request.
     *
     * @param session the session
     * @param data    the data
     */
    public LockerRequest(String session, String data) {
        this.session = session;
        this.data = data;
    }

    /**
     * Gets session.
     *
     * @return the session
     */
    public String getSession() {
        return session;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "LockerRequest{" +
                "session='" + session + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}

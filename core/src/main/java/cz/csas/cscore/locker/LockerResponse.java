package cz.csas.cscore.locker;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Locker response.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 11 /11/15.
 */
class LockerResponse {

    @CsExpose
    private String data;

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
        return "LockerResponse{" +
                "data='" + data + '\'' +
                '}';
    }
}

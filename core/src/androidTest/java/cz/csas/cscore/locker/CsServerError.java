package cz.csas.cscore.locker;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Cs server error.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 15 /05/16.
 */
public class CsServerError {

    @CsExpose
    private String error;

    /**
     * Gets error.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }
}

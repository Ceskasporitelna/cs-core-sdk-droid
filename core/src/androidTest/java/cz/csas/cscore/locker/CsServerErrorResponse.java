package cz.csas.cscore.locker;

import java.util.List;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Cs server error response.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 15 /05/16.
 */
public class CsServerErrorResponse {

    @CsExpose
    private int status;

    @CsExpose
    private List<CsServerError> errors;

    /**
     * Gets status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Gets errors.
     *
     * @return the errors
     */
    public List<CsServerError> getErrors() {
        return errors;
    }
}

package cz.csas.cscore.webapi;

import cz.csas.cscore.error.CsSDKError;

/**
 * The type Web api test error.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 15 /02/16.
 */
public class WebApiTestError extends CsSDKError {

    private String errorCode;

    private String errorMessage;

    /**
     * Instantiates a new Web api test error.
     *
     * @param detailMessage the detail message
     * @param errorCode     the error code
     */
    public WebApiTestError(String detailMessage, String errorCode) {
        super(detailMessage);
        this.errorMessage = detailMessage;
        this.errorCode = errorCode;
    }

    /**
     * Gets error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Gets error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets error code.
     *
     * @param errorCode the error code
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Sets error message.
     *
     * @param errorMessage the error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

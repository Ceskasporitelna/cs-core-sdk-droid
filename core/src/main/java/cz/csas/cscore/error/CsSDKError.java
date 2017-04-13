package cz.csas.cscore.error;

/**
 * The type Cs error.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 04 /12/15.
 */
public abstract class CsSDKError extends RuntimeException {

    /**
     * Instantiates a new Cs error.
     */
    public CsSDKError() {
    }

    /**
     * Instantiates a new Cs error.
     *
     * @param detailMessage the detail message
     */
    public CsSDKError(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Instantiates a new Cs error.
     *
     * @param detailMessage the detail message
     * @param throwable     the throwable
     */
    public CsSDKError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Instantiates a new Cs error.
     *
     * @param throwable the throwable
     */
    public CsSDKError(Throwable throwable) {
        super(throwable);
    }
}

package cz.csas.cscore.webapi.signing;

/**
 * The interface Signable.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09 /04/16.
 */
public interface Signable {

    /**
     * Signing.
     *
     * @return the signing object
     */
    public SigningObject signing();

    /**
     * Set signing object.
     *
     * @param signingObject the signing object
     */
    public void setSigningObject(SigningObject signingObject);
}

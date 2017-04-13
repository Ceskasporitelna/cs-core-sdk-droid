package cz.csas.cscore.webapi.signing;

import cz.csas.cscore.client.rest.Callback;

/**
 * The type Signing process provides the signing flow. It wraps the {@link FilledSigningObject} and
 * allows to cancel signing.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
abstract class SigningProcess {

    /**
     * The Signing object.
     */
    protected SigningObject signingObject;

    /**
     * Instantiates a new Signing process.
     *
     * @param signingObject the signing object
     */
    public SigningProcess(SigningObject signingObject) {
        this.signingObject = signingObject;
    }

    /**
     * Get filled signing object.
     *
     * @return the filled signing object
     */
    public SigningObject getSigningObject() {
        return signingObject;
    }

    /**
     * Cancel the signing process.
     *
     * @param callback the callback
     */
    public void cancel(Callback<FilledSigningObject> callback) {
        // TODO
    }
}

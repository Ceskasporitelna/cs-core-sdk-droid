package cz.csas.cscore.webapi.signing;

import cz.csas.cscore.client.rest.CallbackWebApi;

/**
 * The type No authorization signing process provides the concrete type of SigningProcess.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
public class NoAuthorizationSigningProcess extends SigningProcess {

    /**
     * Instantiates a new Signing process.
     *
     * @param signingObject the filled signing object
     */
    public NoAuthorizationSigningProcess(SigningObject signingObject) {
        super(signingObject);
    }

    /**
     * Finish signing.
     *
     * @param callback the callback
     */
    public void finishSigning(CallbackWebApi<SigningObject> callback) {
        signingObject.requestSigner.finishSigningNoAuthorization(signingObject.getSignId(), callback);
    }
}

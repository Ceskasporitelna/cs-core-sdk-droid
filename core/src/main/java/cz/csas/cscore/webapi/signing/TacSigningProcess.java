package cz.csas.cscore.webapi.signing;

import cz.csas.cscore.client.rest.CallbackWebApi;

/**
 * The concrete type of {@link SigningProcess}
 * See also {@link AuthorizationType}
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
public class TacSigningProcess extends SigningProcess {

    /**
     * Instantiates a new Signing process.
     *
     * @param signingObject the signing object
     */
    public TacSigningProcess(SigningObject signingObject) {
        super(signingObject);
    }

    /**
     * Finish signing with TAC.
     *
     * @param oneTimePassword the one time password to authorize signing
     * @param callback        the callback
     */
    public void finishSigning(String oneTimePassword, CallbackWebApi<SigningObject> callback) {
        signingObject.requestSigner.finishSigningWithTac(signingObject.signId, oneTimePassword, callback);
    }
}

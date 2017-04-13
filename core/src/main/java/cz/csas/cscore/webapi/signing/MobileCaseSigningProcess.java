package cz.csas.cscore.webapi.signing;

import cz.csas.cscore.client.rest.CallbackWebApi;

/**
 * The type Mobile case signing process provides the concrete type of SigningProcess.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
public class MobileCaseSigningProcess extends SigningProcess {

    /**
     * Instantiates a new Signing process.
     *
     * @param signingObject the filled signing object
     */
    public MobileCaseSigningProcess(SigningObject signingObject) {
        super(signingObject);
    }

    /**
     * Finish signing.
     *
     * @param oneTimePassword the one time password
     * @param callback        the callback
     */
    public void finishSigning(String oneTimePassword, CallbackWebApi<SigningObject> callback){
        signingObject.requestSigner.finishSigningWithMobileCase(signingObject.getSignId(),oneTimePassword,callback);
    }
}

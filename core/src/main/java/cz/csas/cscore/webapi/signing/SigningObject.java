package cz.csas.cscore.webapi.signing;

import cz.csas.cscore.client.rest.CallbackWebApi;

/**
 * The type Signing object provides signing info and signing flow.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
public class SigningObject {

    private SigningState signingState;
    protected String signId;
    protected RequestSigner requestSigner;

    /**
     * Instantiates a new Signing object.
     *
     * @param signingState  the actual state of signing process
     * @param signId        the signing related sign id
     * @param requestSigner the request signer to be used for signing
     */
    public SigningObject(SigningState signingState, String signId, RequestSigner requestSigner) {
        this.signingState = signingState;
        this.signId = signId;
        this.requestSigner = requestSigner;
    }

    /**
     * Set signing state.
     *
     * @param signingState
     */
    protected void setSigningState(SigningState signingState) {
        this.signingState = signingState;
    }

    /**
     * Set sign id.
     *
     * @param signId
     */
    protected void setSignId(String signId) {
        this.signId = signId;
    }

    /**
     * Get signing state.
     *
     * @return the signing state
     */
    public SigningState getSigningState() {
        return signingState;
    }

    /**
     * Get sign id.
     *
     * @return the sign id
     */
    public String getSignId() {
        return signId;
    }

    /**
     * Get signing info.
     *
     * @param callback the callback
     */
    public void getInfo(final CallbackWebApi<FilledSigningObject> callback) {
        requestSigner.setSigningObject(this);
        requestSigner.getSigningInfo(getSignId(), callback);
    }

    /**
     * Is done boolean.
     *
     * @return the isSigningDone boolean
     */
    public boolean isDone() {
        return signingState == SigningState.DONE;
    }

    /**
     * Is open boolean.
     *
     * @return the isSigningOpen boolean
     */
    public boolean isOpen() {
        return signingState == SigningState.OPEN;
    }

    /**
     * Is canceled boolean.
     *
     * @return the isSigningCanceled boolean
     */
    public boolean isCanceled() {
        return signingState == SigningState.CANCELED;
    }

    @Override
    public String toString() {
        return "SigningObject{" +
                "signingState=" + signingState +
                ", signId='" + signId + '\'' +
                ", requestSigner=" + requestSigner +
                '}';
    }
}

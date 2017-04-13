package cz.csas.cscore.webapi.signing;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.webapi.WebApiRequest;

/**
 * The type Signing request.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09 /04/16.
 */
class SigningRequest extends WebApiRequest {

    @CsExpose
    private AuthorizationType authorizationType;

    /**
     * Instantiates a new Signing request.
     *
     * @param authorizationType the authorization type to be signed with
     */
    public SigningRequest(AuthorizationType authorizationType) {
        this.authorizationType = authorizationType;
    }

    /**
     * Set authorization type to be signed with.
     *
     * @param authorizationType the authorization type
     */
    public void setAuthorizationType(AuthorizationType authorizationType) {
        this.authorizationType = authorizationType;
    }

    /**
     * Get authorization type to be signed with.
     *
     * @return the authorization type
     */
    public AuthorizationType getAuthorizationType() {
        return authorizationType;
    }

    @Override
    public String toString() {
        return "SigningRequest{" +
                "authorizationType=" + authorizationType +
                '}';
    }
}

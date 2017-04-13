package cz.csas.cscore.webapi.signing;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.webapi.WebApiRequest;

/**
 * The type Finish signing request wraps the request information to finish signing.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09 /04/16.
 */
class FinishSigningRequest extends WebApiRequest {

    @CsExpose
    private AuthorizationType authorizationType;

    @CsExpose
    private String oneTimePassword;

    /**
     * Instantiates a new Finish signing request.
     *
     * @param authorizationType the authorization type
     * @param oneTimePassword   the one time password
     */
    public FinishSigningRequest(AuthorizationType authorizationType, String oneTimePassword) {
        this.authorizationType = authorizationType;
        this.oneTimePassword = oneTimePassword;
    }

    /**
     * Get authorization type.
     *
     * @return the authorization type
     */
    public AuthorizationType getAuthorizationType() {
        return authorizationType;
    }

    /**
     * Set authorization type.
     *
     * @param authorizationType the authorization type
     */
    public void setAuthorizationType(AuthorizationType authorizationType) {
        this.authorizationType = authorizationType;
    }

    /**
     * Get one time password.
     *
     * @return the one time password
     */
    public String getOneTimePassword() {
        return oneTimePassword;
    }

    /**
     * Set one time password.
     *
     * @param oneTimePassword the one time password
     */
    public void setOneTimePassword(String oneTimePassword) {
        this.oneTimePassword = oneTimePassword;
    }
}

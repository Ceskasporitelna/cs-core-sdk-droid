package cz.csas.cscore.webapi.signing;

import java.util.ArrayList;
import java.util.List;

import cz.csas.cscore.client.rest.CallbackWebApi;

/**
 * The type Filled signing object is the concrete filled type of SigningObject.
 * Contains all the specific signing info needed to realize the request signing.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
public class FilledSigningObject extends SigningObject {

    private AuthorizationType authorizationType;
    private List<List<AuthorizationType>> scenarios;

    /**
     * Instantiates a new Filled signing object.
     *
     * @param signingState      the signing state
     * @param signId            the sign id
     * @param requestSigner     the request signer
     * @param authorizationType the authorization type
     * @param scenarios         the scenarios
     */
    public FilledSigningObject(SigningState signingState, String signId, RequestSigner requestSigner, AuthorizationType authorizationType, List<List<AuthorizationType>> scenarios) {
        super(signingState, signId, requestSigner);
        this.authorizationType = authorizationType;
        this.scenarios = scenarios;
    }

    /**
     * Set authorization type.
     *
     * @param authorizationType the authorization type
     */
    protected void setAuthorizationType(AuthorizationType authorizationType) {
        this.authorizationType = authorizationType;
    }

    /**
     * Set scenarios.
     *
     * @param scenarios the scenarios
     */
    protected void setScenarios(List<List<AuthorizationType>> scenarios) {
        this.scenarios = scenarios;
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
     * Get scenarios.
     *
     * @return the scenarios
     */
    public List<List<AuthorizationType>> getScenarios() {
        return scenarios;
    }


    /**
     * Start signing with tac.
     *
     * @param callback the callback
     */
    public void startSigningWithTac(final CallbackWebApi<TacSigningProcess> callback) {
        requestSigner.startSigningWithTac(signId, callback);
    }

    /**
     * Start signing with mobile case.
     *
     * @param callback the callback
     */
    public void startSigningWithMobileCase(final CallbackWebApi<MobileCaseSigningProcess> callback) {
        requestSigner.startSigningWithMobileCase(signId, callback);
    }

    /**
     * Start signing with no authorization.
     *
     * @param callback the callback
     */
    public void startSigningWithNoAuthorization(final CallbackWebApi<NoAuthorizationSigningProcess> callback) {
        requestSigner.startSigningWithNoAuthorization(signId, callback);
    }

    /**
     * Can be signed with boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public boolean canBeSignedWith(AuthorizationType type) {
        return type == authorizationType || containsType(type);
    }

    /**
     * Get possible authorization type.
     *
     * @return the possible authorization type
     */
    public List<AuthorizationType> getPossibleAuthorizationType() {
        List<AuthorizationType> authorizationTypes = new ArrayList<>();
        if (scenarios != null) {
            for (List<AuthorizationType> list : scenarios) {
                authorizationTypes.add(list.get(0));
            }
        }
        return authorizationTypes;
    }

    private boolean containsType(AuthorizationType type) {
        if (scenarios != null) {
            for (List<AuthorizationType> list : scenarios) {
                if (list.get(0) == type)
                    return true;
            }
        }
        return false;
    }

}

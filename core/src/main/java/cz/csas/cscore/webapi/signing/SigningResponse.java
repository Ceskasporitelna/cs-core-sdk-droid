package cz.csas.cscore.webapi.signing;

import java.util.List;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Signing response.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09 /04/16.
 */
class SigningResponse {

    @CsExpose
    private AuthorizationType authorizationType;

    @CsExpose
    private String challengeImage;

    @CsExpose
    private Poll poll;

    @CsExpose
    private SignInfo signInfo;

    @CsExpose
    private List<List<AuthorizationType>> scenarios;

    /**
     * Get authorization type.
     *
     * @return the authorization type
     */
    public AuthorizationType getAuthorizationType() {
        return authorizationType;
    }

    /**
     * Get the challenge image to authorize the signing.
     *
     * @return the challenge image
     */
    public String getChallengeImage() {
        return challengeImage;
    }

    /**
     * Get the poll info.
     *
     * @return the poll
     */
    public Poll getPoll() {
        return poll;
    }

    /**
     * Get the webapientity related sign info.
     *
     * @return the sign info
     */
    public SignInfo getSignInfo() {
        return signInfo;
    }

    /**
     * Get the available scenarios.
     *
     * @return the scenarios
     */
    public List<List<AuthorizationType>> getScenarios() {
        return scenarios;
    }

    @Override
    public String toString() {
        return "SigningResponse{" +
                "authorizationType=" + authorizationType +
                ", challengeImage='" + challengeImage + '\'' +
                ", poll=" + poll +
                ", signInfo=" + signInfo +
                ", scenarios=" + scenarios +
                '}';
    }
}

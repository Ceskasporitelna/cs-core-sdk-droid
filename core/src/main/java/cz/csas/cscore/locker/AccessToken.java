package cz.csas.cscore.locker;

/**
 * The type Access token.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
public class AccessToken {

    private String accessToken;

    private String accessTokenExpiration;

    /**
     * Instantiates a new Access token.
     *
     * @param accessToken           the access token
     * @param accessTokenExpiration the access token expiration
     */
    public AccessToken(String accessToken, String accessTokenExpiration) {
        this.accessToken = accessToken;
        this.accessTokenExpiration = accessTokenExpiration;
    }

    /**
     * Gets access token.
     *
     * @return the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Gets access token expiration.
     *
     * @return the access token expiration
     */
    public String getAccessTokenExpiration() {
        return accessTokenExpiration;
    }
}

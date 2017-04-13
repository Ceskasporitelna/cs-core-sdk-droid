package cz.csas.cscore.locker;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Registration or unlock response json.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
class RegistrationOrUnlockResponseJson {

    @CsExpose
    private String encryptionKey;

    @CsExpose
    private String id;

    @CsExpose
    private String accessToken;

    @CsExpose
    private String accessTokenExpiration;

    @CsExpose
    private String oneTimePasswordKey;

    @CsExpose
    private String refreshToken;

    @CsExpose
    private String remainingAttempts;


    /**
     * Gets refresh token.
     *
     * @return the refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Gets remaining attempts.
     *
     * @return the remaining attempts
     */
    public String getRemainingAttempts() {
        return remainingAttempts;
    }

    /**
     * Gets encryption key.
     *
     * @return the encryption key
     */
    public String getEncryptionKey() {
        return encryptionKey;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
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

    /**
     * Gets one time password key.
     *
     * @return the one time password key
     */
    public String getOneTimePasswordKey() {
        return oneTimePasswordKey;
    }

    @Override
    public String toString() {
        return "RegistrationOrUnlockResponseJson{" +
                "encryptionKey='" + encryptionKey + '\'' +
                ", id='" + id + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", accessTokenExpiration='" + accessTokenExpiration + '\'' +
                ", oneTimePasswordKey='" + oneTimePasswordKey + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", remainingAttempts='" + remainingAttempts + '\'' +
                '}';
    }
}

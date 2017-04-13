package cz.csas.cscore.locker;

/**
 * The type Registration or unlock response.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
public class RegistrationOrUnlockResponse {

    private AccessToken accessToken;

    private String remainingAttempts;

    /**
     * Instantiates a new Registration or unlock response.
     *
     * @param accessToken       the access token
     * @param remainingAttempts the remaining attempts
     */
    public RegistrationOrUnlockResponse(AccessToken accessToken, String remainingAttempts) {
        this.accessToken = accessToken;
        this.remainingAttempts = remainingAttempts;
    }

    /**
     * Gets access token.
     *
     * @return the access token
     */
    public AccessToken getAccessToken() {
        return accessToken;
    }

    /**
     * Gets remaining attempts.
     *
     * @return the remaining attempts
     */
    public String getRemainingAttempts() {
        return remainingAttempts;
    }
}

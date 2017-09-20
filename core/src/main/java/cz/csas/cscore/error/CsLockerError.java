package cz.csas.cscore.error;

/**
 * The type Cs locker error.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 04 /12/15.
 */
public class CsLockerError extends CsSDKError {

    /**
     * The enum Kind.
     */
    public enum Kind {
        /**
         * Attempt to finish registration before finishing the OAuth2 flow.
         */
        WRONG_OAUTH2_URL("You have to first success OAuth2 and then process url."),
        /**
         * Thrown when register operation is invoked and user is already registered.
         */
        REGISTRATION_FAILED("You are already registered."),
        /**
         * Thrown when operation for registered user only is invoked.
         */
        UNREGISTRATION_FAILED("You are already unregistered."),
        /**
         * Thrown then operation unlock is invoked and user is not registered.
         */
        UNLOCK_FAILED("You are unregistered."),
        /**
         * Thrown then operation OTP unlock is invoked and user is not registered.
         */
        OTP_UNLOCK_FAILED("You are unregistered"),
        /**
         * Thrown when migration fails.
         */
        MIGRATION_UNLOCK_FAILED("You are unregistered."),
        /**
         * Thrown then lock operation is invoked for already locked or unregistered user.
         */
        LOCK_FAILED("You are not registered or already locked."),
        /**
         * Thrown when password change is invoked and user is not unlocked.
         */
        PASSWORD_CHANGE_FAILED("You are not unlocked"),
        /**
         * Provided refresh token is null.
         */
        REFRESH_TOKEN_FAILED("Refresh token cannot be null"),
        /**
         * Thrown when bad locker configuration provided.
         */
        BAD_LOCKER_CONFIG("Bad Locker Config, one of the params is null."),
        /**
         * Thrown when provided context is null.
         */
        BAD_CONTEXT("Context cannot be null"),
        /**
         * Registration is needed to enable offline verification
         */
        OFFLINE_VERIFICATION("Registration is needed to enable offline verification."),
        /**
         * Operation failed, no access token was provided from the server.
         */
        NO_ACCESS_TOKEN("No access token was provided from the server"),
        /**
         * It was not possible to parse server response, try again later.
         */
        PARSE_ERROR("It was not possible to properly parse the server response."),
        /**
         * Other kind
         */
        OTHER("");

        private String detailedMessage;

        Kind(String detailedMessage) {
            this.detailedMessage = detailedMessage;
        }
    }

    private final Kind kind;

    /**
     * Instantiates a new Cs locker error.
     *
     * @param kind          the kind
     * @param detailMessage the detail message
     */
    public CsLockerError(Kind kind, String detailMessage) {
        super(detailMessage);
        this.kind = kind;
    }

    /**
     * Instantiates a new Cs locker error.
     *
     * @param kind the kind
     */
    public CsLockerError(Kind kind) {
        super(kind.detailedMessage);
        this.kind = kind;
    }

    /**
     * Gets kind.
     *
     * @return the kind
     */
    public Kind getKind() {
        return kind;
    }
}

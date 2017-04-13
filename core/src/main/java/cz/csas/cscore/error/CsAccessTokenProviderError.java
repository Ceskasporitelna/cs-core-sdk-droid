package cz.csas.cscore.error;

/**
 * The type Cs access token provider error.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /05/16.
 */
public class CsAccessTokenProviderError extends CsSDKError {

    /**
     * The enum Kind.
     */
    public enum Kind {
        /**
         * Unregistered kind.
         * User is not registered => no identity available
         */
        UNREGISTERED("No identity found. Register first, please."),
        /**
         * Locked kind.
         * User is locker => no accessToken available
         */
        LOCKED("User locked. Unlock first, please."),
        /**
         * Not initialized kind.
         * AccessTokenProvider cannot be null.
         */
        NOT_INITIALIZED("AccessTokenProvider is not set. Please call .useLocker during configuration of CoreSDK"),
        /**
         * Not available kind.
         * Access token is not available => two possible cases cause this accessTokenProviderError
         * Kind:
         * 1) access token is not saved in keychain properly
         * 2) refresh token api call failure
         */
        NOT_AVAILABLE("AccessToken is not available.");

        private String detailedMessage;

        Kind(String detailedMessage) {
            this.detailedMessage = detailedMessage;
        }
    }

    private final Kind kind;

    /**
     * Instantiates a new Cs access token provider error.
     *
     * @param kind          the kind
     * @param detailMessage the detail message
     */
    public CsAccessTokenProviderError(Kind kind, String detailMessage) {
        super(detailMessage);
        this.kind = kind;
    }

    /**
     * Instantiates a new Cs access token provider error.
     *
     * @param kind the kind
     */
    public CsAccessTokenProviderError(Kind kind) {
        super(kind.detailedMessage);
        this.kind = kind;
    }

    /**
     * Get kind.
     *
     * @return the kind
     */
    public Kind getKind() {
        return kind;
    }
}

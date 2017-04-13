package cz.csas.cscore.error;

/**
 * The type Cs core error.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 04 /12/15.
 */
public class CsCoreError extends CsSDKError {

    /**
     * The enum Kind.
     */
    public enum Kind {

        /**
         * Thrown when provided access token is not valid.
         */
        BAD_ACCESS_TOKEN("Valid access token is not available."),
        /**
         * Thrown when provided context is null.
         */
        BAD_CONTEXT("Context cannot be null"),
        /**
         * Thrown when provided environment is null.
         */
        BAD_ENVIRONMENT("Environment cannot be null."),
        /**
         * Thrown when provided environment is not allowed with testing javascript.
         */
        BAD_ENVIRONMENT_FOR_TESTING_JS("Provided environment is not allowed for testing javascript."),
        /**
         * Thrown when provided language is null.
         */
        BAD_LANGUAGE("Language cannot be null."),
        /**
         * Thrown when useLocker() is called before proper Locker initialization.
         */
        BAD_LOCKER_INITIALIZATION("Locker is not initialized yet. Call CoreSDK.getInstance().useLocker(); at first."),
        /**
         * Thrown when provided private key is null.
         */
        BAD_PRIVATE_KEY("Private key cannot be null."),
        /**
         * Thrown when provided web-api key is null.
         */
        BAD_WEB_API_KEY("Web-api key cannot be null."),
        /**
         * Webapi transformation cannot be applied because transformed webapi entity or its reosurce is null.
         */
        TRANSFORM("Default object or resource cannot be null"),
        /**
         * Thrown when cryptographic operation failed
         */
        CRYPTO("Crypto operation failed. See underlying error."),
        /**
         * Other kind.
         */
        OTHER("");

        private String detailedMessage;

        Kind(String detailedMessage) {
            this.detailedMessage = detailedMessage;
        }
    }

    private final Kind kind;

    /**
     * Instantiates a new Cs core error.
     *
     * @param kind the kind
     */
    public CsCoreError(Kind kind) {
        super(kind.detailedMessage);
        this.kind = kind;
    }

    /**
     * Instantiates a new Cs core error.
     *
     * @param kind      the kind
     * @param throwable the throwable
     */
    public CsCoreError(Kind kind, Throwable throwable) {
        super(kind.detailedMessage, throwable);
        this.kind = kind;
    }

    /**
     * Instantiates a new Cs core error.
     *
     * @param kind          the kind
     * @param detailMessage the detail message
     */
    public CsCoreError(Kind kind, String detailMessage) {
        super(detailMessage);
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

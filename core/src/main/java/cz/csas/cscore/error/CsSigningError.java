package cz.csas.cscore.error;

/**
 * The type Cs signing error.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09 /04/16.
 */
public class CsSigningError extends CsSDKError {

    /**
     * The enum Kind.
     */
    public enum Kind {

        /**
         * The requested resource is in a state that can not be signed (either not yet ready, or
         * already signed).
         */
        UNSIGNABLE_ENTITY("Unsignable entity."),
        /**
         * This entity signing failed on the server side
         */
        SIGNING_FAILED("Signing failed."),
        /**
         * There is already one sign process in progress.
         */
        SIGNING_IN_PROGRESS("Signing already in progress."),
        /**
         * One Time Password was invalid when signing using TAC
         */
        INVALID_OTP("Invalid OTP."),
        /**
         * The provided signId doesn't exist.
         */
        INVALID_SIGN_ID("Invalid sign Id"),
        /**
         * Invalid authorization type selected.
         */
        INVALID_AUTHORIZATION_TYPE("Invalid authorization type."),
        /**
         * This resource can not be signed using selected authorization method right now as the
         * (daily/transaction) limit for this method has been already reached.
         */
        AUTH_LIMIT_EXCEEDED("Daily/transaction limit for this authorization type was exceeded."),
        /**
         * There is no authorization method available for this order.
         */
        NO_AUTH_AVAILABLE("No authorization method available for this order."),
        /**
         * Authorization method is locked due to all user allowed attempts to enter valid OTP
         * failed. User can start signing again and select other authorization method.
         */
        AUTH_METHOD_LOCKED("Authorization method is locked due to all user allowed attempts to enter valid OTP failed."),
        /**
         * The previous OTPs were wrong, only one attempt remains. This code will be provided in
         * addition to OTP_INVALID.
         */
        ONE_ATTEMPT_LEFT("Previous OTP was wrong. One attempt left."),
        /**
         * All allowed attempts failed. User is now blocked. This code will be provided in addition
         * to TAC_INVALID.
         */
        USER_LOCKED("All allowed attempts failed. User is now blocked."),
        /**
         * Signing process was successful but BE haven't responded in time thus we do not know
         * whether order was processed or not.
         */
        ORDER_DELIVERY_UNCERTAIN("Signing process was successful but no response received. It is not clear whether order was processed or not."),
        /**
         * Amount to be transferred is higher than current disposable balance on the account.
         */
        INSUFFICIENT_BALANCE("Insuficient balance on the account."),
        /**
         * Phone number is blocked.
         */
        PHONE_NUMBER_BLOCKED("Phone number is blocked."),
        /**
         * Daily/monthly limit reached.
         */
        LIMIT_EXCEEDED("Daily or monthly limit was exceeded."),
        /**
         * Signing failed from other reasons (Network, etc...)
         */
        OTHER("Other error.");

        private String detailedMessage;

        Kind(String detailedMessage) {
            this.detailedMessage = detailedMessage;
        }
    }

    private Kind kind;

    /**
     * Instantiates a new Cs signing error.
     *
     * @param kind          the kind
     * @param detailMessage the detail message
     */
    public CsSigningError(Kind kind, String detailMessage) {
        super(detailMessage);
        this.kind = kind;
    }

    /**
     * Instantiates a new Cs signing error.
     *
     * @param kind the kind
     */
    public CsSigningError(Kind kind) {
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

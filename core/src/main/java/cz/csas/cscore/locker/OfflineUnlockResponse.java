package cz.csas.cscore.locker;

import cz.csas.cscore.error.CsSDKError;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 28/07/16.
 */
public class OfflineUnlockResponse extends RegistrationOrUnlockResponse {

    private CsSDKError error;

    /**
     * Instantiates a new Offline unlock response.
     *
     * @param remainingAttempts the remaining attempts
     * @param error             the error to handle offline authentication in LockerUI 
     */
    public OfflineUnlockResponse(String remainingAttempts, CsSDKError error) {
        super(null, remainingAttempts);
        this.error = error;
    }

    /**
     * Get error to handle offline authentication in LockerUI
     *
     * @return the error
     */
    public CsSDKError getError() {
        return error;
    }
}

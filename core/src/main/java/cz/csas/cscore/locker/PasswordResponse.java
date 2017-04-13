package cz.csas.cscore.locker;

/**
 * The type Remaining attempts.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
public class PasswordResponse {

    private Integer remainingAttempts;

    /**
     * Instantiates a new Remaining attempts.
     *
     * @param remainingAttempts the remaining attempts
     */
    public PasswordResponse(Integer remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }

    /**
     * Gets remaining attempts.
     *
     * @return the remaining attempts
     */
    public Integer getRemainingAttempts() {
        return remainingAttempts;
    }
}

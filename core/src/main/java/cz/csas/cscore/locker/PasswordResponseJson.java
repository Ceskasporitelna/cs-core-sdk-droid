package cz.csas.cscore.locker;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Password response json.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
class PasswordResponseJson {

    @CsExpose
    private Integer remainingAttempts;

    /**
     * Instantiates a new Password response json.
     *
     * @param remainingAttempts the remaining attempts
     */
    public PasswordResponseJson(Integer remainingAttempts) {
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

    @Override
    public String toString() {
        return "PasswordResponseJson{" +
                "remainingAttempts=" + remainingAttempts +
                '}';
    }
}

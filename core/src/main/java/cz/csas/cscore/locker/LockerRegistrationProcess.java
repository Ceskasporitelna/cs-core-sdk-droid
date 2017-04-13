package cz.csas.cscore.locker;

/**
 * The interface Locker registration factory.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /11/15.
 */
public interface LockerRegistrationProcess {

    /**
     * Finish registration. This object is dispatched to the developer through Locker.register() method.
     * It allows you to finish your registration after oAuth2 authentication.
     *
     * @param password the password
     */
    public void finishRegistration(Password password);
}

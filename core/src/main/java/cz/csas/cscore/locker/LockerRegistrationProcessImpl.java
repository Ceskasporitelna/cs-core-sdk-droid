package cz.csas.cscore.locker;

/**
 * The type Locker registration factory.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /11/15.
 */
class LockerRegistrationProcessImpl implements LockerRegistrationProcess {

    @Override
    public void finishRegistration(Password password) {
        ((LockerImpl) LockerFactory.createLocker()).finishRegistration(password);
    }
}

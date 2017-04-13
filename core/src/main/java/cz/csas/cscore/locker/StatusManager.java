package cz.csas.cscore.locker;

/**
 * The interface State manager.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
interface StatusManager {


    /**
     * Gets status.
     *
     * @return the status
     */
    public LockerStatus getStatus();

    /**
     * Sets locker status changed listener.
     *
     * @param onLockerStatusChangeListener the on locker status changed listener
     */
    public void setLockerStatusChangedListener(OnLockerStatusChangeListener onLockerStatusChangeListener);

    /**
     * Sets offline unlock success flag.
     *
     * @param isVerifiedOffline the offline unlock success flag
     */
    public void setIsVerifiedOffline(boolean isVerifiedOffline);

    /**
     * Is offline unlock active boolean.
     *
     * @return the boolean
     */
    public boolean isVerifiedOffline();

    /**
     * Sets state.
     */
    public void setState();

    /**
     * Gets state.
     *
     * @return the state
     */
    public State getState();

    /**
     * Sets lock type.
     *
     * @param lockType the lock type
     */
    public void setLockType(LockType lockType);

    /**
     * Gets lock type.
     *
     * @return the lock type
     */
    public LockType getLockType();

    /**
     * Gets client id.
     *
     * @return the client id
     */
    public String getClientId();

    /**
     * Has one time password key boolean.
     *
     * @return the boolean
     */
    public boolean hasOneTimePasswordKey();

    /**
     * Has aes encryption key boolean.
     *
     * @return the boolean
     */
    public boolean hasAesEncryptionKey();

    /**
     * Gets access token expiration.
     *
     * @return the access token expiration
     */
    public String getAccessTokenExpiration();
}

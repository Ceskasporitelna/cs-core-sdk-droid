package cz.csas.cscore.locker;

/**
 * The type Locker status.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
public class LockerStatus {

    private State mState = State.USER_UNREGISTERED;
    private boolean mIsVerifiedOffline;
    private LockType mLockType;
    private String mCliendId;
    private boolean mHasOneTimePasswordKey;
    private boolean mHasAesEncryptionKey;
    private String mAccessTokenExpiration;

    /**
     * Instantiates a new Locker status.
     *
     * @param state                 the state
     * @param lockType              the lock type
     * @param cliendId              the cliend id
     * @param hasAesEncryptionKey   the has aes encryption key
     * @param hasOneTimePasswordKey the has one time password key
     * @param accessTokenExpiration the access token expiration
     */
    public LockerStatus(State state, boolean isVerifiedOffline, LockType lockType, String cliendId, boolean hasAesEncryptionKey, boolean hasOneTimePasswordKey, String accessTokenExpiration) {
        mState = state;
        mIsVerifiedOffline = isVerifiedOffline;
        mLockType = lockType;
        mCliendId = cliendId;
        mHasAesEncryptionKey = hasAesEncryptionKey;
        mHasOneTimePasswordKey = hasOneTimePasswordKey;
        mAccessTokenExpiration = accessTokenExpiration;
    }

    /**
     * Gets mState.
     *
     * @return the mState
     */
    public State getState() {
        return mState;
    }

    /**
     * Is verified offline boolean.
     *
     * @return the boolean
     */
    public boolean isVerifiedOffline() {
        return mIsVerifiedOffline;
    }

    /**
     * Gets lock type.
     *
     * @return the lock type
     */
    public LockType getLockType() {
        return mLockType;
    }

    /**
     * Gets cliend id.
     *
     * @return the cliend id
     */
    public String getClientId() {
        return mCliendId;
    }

    /**
     * Ism has one time password key boolean.
     *
     * @return the boolean
     */
    public boolean hasOneTimePasswordKey() {
        return mHasOneTimePasswordKey;
    }

    /**
     * Ism has aes encryption key boolean.
     *
     * @return the boolean
     */
    public boolean hasAesEncryptionKey() {
        return mHasAesEncryptionKey;
    }

    /**
     * Gets access token expiration.
     *
     * @return the access token expiration
     */
    public String getAccessTokenExpiration() {
        return mAccessTokenExpiration;
    }
}

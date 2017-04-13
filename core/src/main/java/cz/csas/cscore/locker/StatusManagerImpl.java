package cz.csas.cscore.locker;

import cz.csas.cscore.storage.KeychainManager;

/**
 * The type Status manager.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
class StatusManagerImpl implements StatusManager {

    private State mLastState;
    private KeychainManager mKeychainManager;
    private OnLockerStatusChangeListener mOnLockerStatusChangeListener;
    private boolean mIsVerifiedOffline = false;

    /**
     * Instantiates a new Status manager.
     *
     * @param keychainManager the keychain manager
     */
    public StatusManagerImpl(KeychainManager keychainManager) {
        mKeychainManager = keychainManager;
    }

    @Override
    public LockerStatus getStatus() {
        return new LockerStatus(getState(), isVerifiedOffline(), getLockType(), getClientId(), hasAesEncryptionKey(), hasOneTimePasswordKey(), getAccessTokenExpiration());
    }

    @Override
    public void setLockerStatusChangedListener(OnLockerStatusChangeListener onLockerStatusChangeListener) {
        mOnLockerStatusChangeListener = onLockerStatusChangeListener;
    }

    @Override
    public void setIsVerifiedOffline(boolean isVerifiedOffline) {
        mIsVerifiedOffline = isVerifiedOffline;
    }

    @Override
    public boolean isVerifiedOffline() {
        return mIsVerifiedOffline;
    }

    @Override
    public void setState() {
        if (mOnLockerStatusChangeListener != null && getState() != mLastState)
            mOnLockerStatusChangeListener.onLockerStatusChanged(getState());
        mLastState = getState();
    }

    @Override
    public State getState() {
        if (hasAesEncryptionKey())
            return State.USER_UNLOCKED;
        else if (getClientId() == null)
            return State.USER_UNREGISTERED;
        else
            return State.USER_LOCKED;
    }

    @Override
    public void setLockType(LockType lockType) {
        mKeychainManager.storeLockType(lockType);
    }

    @Override
    public LockType getLockType() {
        return mKeychainManager.retrieveLockType();
    }

    @Override
    public String getClientId() {
        return mKeychainManager.retrieveCID();
    }

    @Override
    public boolean hasOneTimePasswordKey() {
        return mKeychainManager.hasOneTimePasswordKey();
    }

    @Override
    public boolean hasAesEncryptionKey() {
        return mKeychainManager.hasEK();
    }

    @Override
    public String getAccessTokenExpiration() {
        AccessToken accessToken = mKeychainManager.retrieveAccessToken();
        if (accessToken != null)
            return accessToken.getAccessTokenExpiration();
        return null;
    }
}

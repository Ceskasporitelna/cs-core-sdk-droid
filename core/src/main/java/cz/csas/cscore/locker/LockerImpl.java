package cz.csas.cscore.locker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.client.crypto.CryptoManager;
import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CallbackBasic;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.EmptyResponse;
import cz.csas.cscore.client.rest.android.BackgroundQueue;
import cz.csas.cscore.client.rest.android.BackgroundThreadExecutor;
import cz.csas.cscore.client.rest.android.MainThreadExecutor;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsLockerError;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.logger.LogLevel;
import cz.csas.cscore.logger.LogManager;
import cz.csas.cscore.storage.KeychainManager;
import cz.csas.cscore.utils.StringUtils;
import cz.csas.cscore.utils.csjson.CsJson;

/**
 * The type Locker.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 11 /11/15.
 */
class LockerImpl implements Locker {

    private final int OFFLINE_AUTH_MAX_ATTEMPTS = 2;

    private final String RESPONSE_TYPE = "code";
    private final String STATE = "profile";
    private final String CODE_QUERY = "code";
    private LockerClient mLockerClient;
    private LockerConfig mLockerConfig;
    private StatusManager mStatusManager;
    private KeychainManager mKeychainManager;
    private CryptoManager mCryptoManager;
    private LogManager mLogManager;
    private Context mContext;
    private CsJson mCsJson;
    private CsCallback<RegistrationOrUnlockResponse> mCallbackRegister;
    private CallbackBasic<LockerRegistrationProcess> mCallbackOAuth;
    private BackgroundThreadExecutor mBackgroundThreadExecutor;
    private BackgroundQueue mBackgroundQueue = new BackgroundQueue("cs-core-background-queue");
    private MainThreadExecutor mMainThreadExecutor;
    private String mJavascript;
    private OAuthLoginActivityOptions mOAuthLoginActivityOptions = new OAuthLoginActivityOptions.Builder().create();

    // Test objects
    private String mTestNonce;
    private String mTestRandom;
    private String mTestNewRandom;
    private String mTestCode;
    private String mTestDFP;
    private byte[] mTestSEK;
    private Long mTestTime;
    private boolean mTestFlag = false;

    /**
     * Instantiates a new Locker.
     *
     * @param keychainManager the keychain manager
     * @param cryptoManager   the crypto manager
     * @param lockerConfig    the locker config
     * @param logManager      the log manager
     * @param context         the context
     */
    public LockerImpl(KeychainManager keychainManager, CryptoManager cryptoManager, LockerConfig lockerConfig, LogManager logManager, Context context) {
        mKeychainManager = keychainManager;
        mLockerConfig = lockerConfig;
        mCryptoManager = cryptoManager;
        mLogManager = logManager;
        mContext = context;
        mLockerClient = new LockerClient(CoreSDK.getInstance().getWebApiConfiguration());
        mStatusManager = new StatusManagerImpl(mKeychainManager);
        mCsJson = new CsJson();
        checkEnvironmentHash(mLockerConfig.getClientId(), mLockerConfig.getClientSecret());
        mBackgroundThreadExecutor = new BackgroundThreadExecutor();
        mMainThreadExecutor = new MainThreadExecutor();
    }

    @Override
    public void register(Context context, CallbackBasic<LockerRegistrationProcess> callbackWithReturn, CsCallback<RegistrationOrUnlockResponse> callback) {
        if (getStatus().getState() != State.USER_UNREGISTERED)
            callback.failure(new CsLockerError(CsLockerError.Kind.REGISTRATION_FAILED));
        else {
            mKeychainManager.storeLocalEK(mCryptoManager.encodeSha256(getDFP(), mCryptoManager.generateRandomString()));
            mCallbackRegister = callback;
            mCallbackOAuth = callbackWithReturn;
            initiateOAuth2(context);
        }
    }

    /**
     * Finish registration.
     *
     * @param password the password
     */
    void finishRegistration(final Password password) {
        if (getCode() != null) {
            final String random = getRandom();
            RegistrationRequestJson registrationRequestJson = new RegistrationRequestJson(getCode(), mCryptoManager.encodeSha256(password.getPassword(), getDFP() + random), getDFP(), mLockerConfig.getScope(), getNonce());
            ((LockerRestService) mLockerClient.getService()).register(new LockerRequest(encryptSession(), encryptData(registrationRequestJson)), new LockerCallback() {
                @Override
                public void success(LockerResponse lockerResponse, Response response) {
                    RegistrationOrUnlockResponseJson registrationOrUnlockResponseJson = decryptRegistrationOrUnlockData(lockerResponse.getData());
                    // check access token (error when empty or null)
                    AccessToken accessToken;
                    CsRestError error = checkAccessTokenPresenceOrReturnError(registrationOrUnlockResponseJson, response);
                    if (error != null) {
                        mCallbackRegister.failure(error);
                        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Registration failed with error:" + error.getLocalizedMessage()), LogLevel.INFO);
                        return;
                    } else
                        accessToken = new AccessToken(registrationOrUnlockResponseJson.getAccessToken(), registrationOrUnlockResponseJson.getAccessTokenExpiration());
                    mKeychainManager.storePwdRandom(random);
                    mKeychainManager.setEK(registrationOrUnlockResponseJson.getEncryptionKey().getBytes());
                    mKeychainManager.storeAccessToken(accessToken);
                    mKeychainManager.storeCID(registrationOrUnlockResponseJson.getId());
                    mKeychainManager.storeOneTimePasswordKey(registrationOrUnlockResponseJson.getOneTimePasswordKey());
                    mKeychainManager.storeRefreshToken(registrationOrUnlockResponseJson.getRefreshToken());

                    LockType lockType = password.getLockType();
                    mStatusManager.setLockType(lockType);
                    if (lockType == LockType.GESTURE || lockType == LockType.PIN)
                        mKeychainManager.storePasswordInputSpaceSize(password.getPasswordSpaceSize());
                    else if (password.getLockType() == LockType.NONE)
                        mKeychainManager.storeNoLockPassword(password.getPassword());
                    if (mLockerConfig.isOfflineAuthEnabled()) {
                        storeOfflineHash(password);
                    }
                    mStatusManager.setState();
                    mCallbackRegister.success(new RegistrationOrUnlockResponse(accessToken, registrationOrUnlockResponseJson.getRemainingAttempts()), response);
                    mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "REGISTERED", "Registration with " + lockType + " was successful."), LogLevel.INFO);
                }

                @Override
                public void failure(CsRestError error) {
                    mCallbackRegister.failure(error);
                    mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Registration failed with error:" + error.getLocalizedMessage()), LogLevel.INFO);
                }
            });
        } else
            mCallbackRegister.failure(new CsLockerError(CsLockerError.Kind.WRONG_OAUTH2_URL));
    }

    @Override
    public void unregister(final CsCallback<LockerStatus> callback) {
        if (getStatus().getState() != State.USER_UNREGISTERED) {
            UnregisterRequestJson unregisterRequestJson = new UnregisterRequestJson(mKeychainManager.retrieveCID(), getDFP(), getNonce());
            ((LockerRestService) mLockerClient.getService()).unregister(new LockerRequest(encryptSession(), encryptData(unregisterRequestJson)), new Callback<EmptyResponse>() {
                @Override
                public void success(EmptyResponse emptyResponse, Response response) {
                    if (mTestFlag)
                        callback.success(getStatus(), response);
                }

                @Override
                public void failure(CsRestError error) {
                }
            });
            unregisterUserInternally();
            if (!mTestFlag)
                callback.success(getStatus(), null);
            mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Unregistration was successful"), LogLevel.INFO);
        } else
            callback.failure(new CsLockerError(CsLockerError.Kind.UNREGISTRATION_FAILED));
    }

    @Override
    public void unlock(final String password, final CsCallback<RegistrationOrUnlockResponse> callback) {
        mBackgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                String passwordPom = password;
                if (getStatus().getState() == State.USER_UNREGISTERED)
                    callback.failure(new CsLockerError(CsLockerError.Kind.UNLOCK_FAILED));
                if (getStatus().getLockType() == LockType.NONE)
                    passwordPom = mKeychainManager.retrieveNoLockPassword();
                UnlockRequestJson unlockRequestJson = new UnlockRequestJson(mKeychainManager.retrieveCID(), mCryptoManager.encodeSha256(passwordPom, getDFP() + getRandom()), getDFP(), getNonce());
                final String finalPassword = passwordPom;
                ((LockerRestService) mLockerClient.getService()).unlock(new LockerRequest(encryptSession(), encryptData(unlockRequestJson)), new LockerCallback() {
                    @Override
                    public void success(LockerResponse lockerResponse, Response response) {
                        RegistrationOrUnlockResponseJson registrationOrUnlockResponseJson = decryptRegistrationOrUnlockData(lockerResponse.getData());
                        if (registrationOrUnlockResponseJson.getRemainingAttempts() != null) {
                            executeSuccessOnMainThreadAndFreeQueue(callback, new RegistrationOrUnlockResponse(null, registrationOrUnlockResponseJson.getRemainingAttempts()), response);
                            mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "LOCKED", "Unlock failed. Remaining attempts: " + registrationOrUnlockResponseJson.getRemainingAttempts()), LogLevel.INFO);
                        } else {
                            // check access token
                            AccessToken accessToken;
                            CsRestError error = checkAccessTokenPresenceOrReturnError(registrationOrUnlockResponseJson, response);
                            if (error != null) {
                                unregisterUserInternally();
                                executeFailureOnMainThreadAndFreeQueue(callback, error);
                                mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Unlock failed with error:" + error.getLocalizedMessage()), LogLevel.INFO);
                                return;
                            } else
                                accessToken = new AccessToken(registrationOrUnlockResponseJson.getAccessToken(), registrationOrUnlockResponseJson.getAccessTokenExpiration());
                            mKeychainManager.setEK(registrationOrUnlockResponseJson.getEncryptionKey().getBytes());
                            mKeychainManager.storeAccessToken(accessToken);
                            if (registrationOrUnlockResponseJson.getRefreshToken() != null)
                                mKeychainManager.storeRefreshToken(registrationOrUnlockResponseJson.getRefreshToken());
                            mStatusManager.setState();
                            if (mLockerConfig.isOfflineAuthEnabled()) {
                                LockType lockType = getStatus().getLockType();
                                if (mKeychainManager.retrievePasswordInputSpaceSize() == null && (lockType == LockType.GESTURE || lockType == LockType.PIN)) {
                                    unregisterUserInternally();
                                    executeFailureOnMainThreadAndFreeQueue(callback, CsRestError.unexpectedError(response.getUrl(), new CsLockerError(CsLockerError.Kind.OFFLINE_VERIFICATION)));
                                    mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Unlock failed. Configuration changed. New registration needed."), LogLevel.INFO);
                                } else {
                                    storeOfflineHash(new Password(getStatus().getLockType(), finalPassword, mKeychainManager.retrievePasswordInputSpaceSize()));
                                    executeSuccessOnMainThreadAndFreeQueue(callback, new RegistrationOrUnlockResponse(accessToken, registrationOrUnlockResponseJson.getRemainingAttempts()), response);
                                    mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNLOCKED", "Unlock by " + getStatus().getLockType() + " was succesful."), LogLevel.INFO);
                                }
                            } else {
                                executeSuccessOnMainThreadAndFreeQueue(callback, new RegistrationOrUnlockResponse(accessToken, registrationOrUnlockResponseJson.getRemainingAttempts()), response);
                                mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNLOCKED", "Unlock by " + getStatus().getLockType() + " was succesful."), LogLevel.INFO);
                            }
                        }
                    }

                    @Override
                    public void failure(CsRestError error) {
                        if (shouldUnregister(error)) {
                            unregisterUserInternally();
                            executeFailureOnMainThreadAndFreeQueue(callback, error);
                            mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Unlock failed. No more attempts. User unregistered."), LogLevel.INFO);
                        } else if (shouldUnlockOffline(error)) {
                            LockType lockType = getStatus().getLockType();
                            if (mKeychainManager.retrievePasswordInputSpaceSize() == null && (lockType == LockType.GESTURE || lockType == LockType.PIN)) {
                                unregisterUserInternally();
                                executeFailureOnMainThreadAndFreeQueue(callback, CsRestError.unexpectedError(error.getUrl(), new CsLockerError(CsLockerError.Kind.OFFLINE_VERIFICATION)));
                                mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Unlock failed. Configuration changed. New registration needed."), LogLevel.INFO);
                            } else
                                unlockOffline(finalPassword, callback, error);
                        } else {
                            executeFailureOnMainThreadAndFreeQueue(callback, error);
                            mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "LOCKED", "Unlock failed with error: " + error.getLocalizedMessage()), LogLevel.INFO);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void unlockAfterMigration(final Password password, final PasswordHashProcess passwordHashProcess, final LockerMigrationData data, final CsCallback<RegistrationOrUnlockResponse> callback) {
        State state = getStatus().getState();
        if (state != State.USER_UNREGISTERED) {
            callback.failure(new CsLockerError(CsLockerError.Kind.MIGRATION_UNLOCK_FAILED));
            mLogManager.log(StringUtils.logLine(LOCKER_MODULE, state.name(), "Migration unlock failed. You have to be in UNREGISTERED state."), LogLevel.INFO);
        } else {
            mBackgroundQueue.addToQueue(new Runnable() {
                @Override
                public void run() {
                    // store necessary information
                    String clientId = data.getClientId();
                    final LockType lockType = password.getLockType();
                    String passwordString = password.getPassword();
                    mKeychainManager.storeLocalEK(mCryptoManager.encodeSha256(getDFP(), mCryptoManager.generateRandomString()));
                    mKeychainManager.setEK(data.getEncryptionKey().getBytes());
                    mKeychainManager.storeLockType(lockType);
                    mKeychainManager.storeCID(clientId);
                    mKeychainManager.storeDFP(data.getDeviceFingerprint());
                    mKeychainManager.storeOneTimePasswordKey(data.getOneTimePasswordKey());
                    mKeychainManager.storeRefreshToken(data.getRefreshToken());
                    // check for none lock type and save password as no lock password
                    if (lockType == LockType.NONE)
                        mKeychainManager.storeNoLockPassword(passwordString);
                    // check state for unregistered - client id already stored => state is registered
                    if (getStatus().getState() == State.USER_UNREGISTERED) {
                        CsLockerError error = new CsLockerError(CsLockerError.Kind.MIGRATION_UNLOCK_FAILED);
                        executeFailureOnMainThreadAndFreeQueue(callback, error);
                        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Migration unlock failed with error:" + error.getLocalizedMessage()), LogLevel.INFO);
                    }
                    // create unlock request using password hash process
                    UnlockRequestJson unlockRequestJson = new UnlockRequestJson(clientId, passwordHashProcess.hashPassword(passwordString), mKeychainManager.retrieveDFP(), getNonce());
                    // call unlock
                    ((LockerRestService) mLockerClient.getService()).unlock(new LockerRequest(encryptSession(), encryptData(unlockRequestJson)), new Callback<LockerResponse>() {
                        @Override
                        public void success(LockerResponse lockerResponse, Response response) {
                            final RegistrationOrUnlockResponseJson registrationOrUnlockResponseJson = decryptRegistrationOrUnlockData(lockerResponse.getData());
                            // check remaining attempts and access token presence
                            CsSDKError error;
                            if (registrationOrUnlockResponseJson.getRemainingAttempts() != null)
                                error = new CsLockerError(CsLockerError.Kind.MIGRATION_UNLOCK_FAILED);
                            else
                                error = checkAccessTokenPresenceOrReturnError(registrationOrUnlockResponseJson, response);
                            // unregister if error found
                            if (error != null) {
                                unregisterUserInternally();
                                executeFailureOnMainThreadAndFreeQueue(callback, error);
                                mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Migration unlock failed with error:" + error.getLocalizedMessage()), LogLevel.INFO);
                                return;
                            }
                            final AccessToken accessToken = new AccessToken(registrationOrUnlockResponseJson.getAccessToken(), registrationOrUnlockResponseJson.getAccessTokenExpiration());
                            mKeychainManager.setEK(registrationOrUnlockResponseJson.getEncryptionKey().getBytes());
                            mKeychainManager.storeAccessToken(accessToken);
                            if (registrationOrUnlockResponseJson.getRefreshToken() != null)
                                mKeychainManager.storeRefreshToken(registrationOrUnlockResponseJson.getRefreshToken());
                            mStatusManager.setState();

                            // change password on background to rehash it according to locker hash algorithm
                            changePasswordWithCustomHashAlgorithm(password, passwordHashProcess, new CsCallback<PasswordResponse>() {
                                @Override
                                public void success(PasswordResponse passwordResponse, Response response) {
                                    executeSuccessOnMainThreadAndFreeQueue(callback, new RegistrationOrUnlockResponse(accessToken, registrationOrUnlockResponseJson.getRemainingAttempts()), response);
                                    mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNLOCKED", "Migration unlock by " + getStatus().getLockType() + " was successful."), LogLevel.INFO);
                                }

                                @Override
                                public void failure(CsSDKError error) {
                                    // log messages already done
                                    unregisterUserInternally();
                                    executeFailureOnMainThreadAndFreeQueue(callback, error);
                                }
                            });
                        }

                        @Override
                        public void failure(CsRestError error) {
                            if (shouldUnregister(error))
                                mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Migration unlock failed with error:" + error.getLocalizedMessage() + ". User unregistered."), LogLevel.INFO);
                            else
                                mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Migration unlock failed with error:" + error.getLocalizedMessage() + ". Internet connection failed, please try again later. "), LogLevel.INFO);
                            unregisterUserInternally();
                            executeFailureOnMainThreadAndFreeQueue(callback, error);
                        }
                    });

                }
            });
        }
    }

    @Override
    public void unlockWithOneTimePassword(final CsCallback<RegistrationOrUnlockResponse> callback) {
        mBackgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                if (getStatus().getState() == State.USER_UNREGISTERED)
                    callback.failure(new CsLockerError(CsLockerError.Kind.OTP_UNLOCK_FAILED));
                String oneTimePassword = mCryptoManager.generatePassword(mKeychainManager.retrieveCID() + getDFP(), 4, mCryptoManager.decodeBase64(mKeychainManager.retrieveOneTimePasswordKey()), getTime());
                OneTimePasswordUnlockRequestJson oneTimePasswordUnlockRequestJson = new OneTimePasswordUnlockRequestJson(mKeychainManager.retrieveCID(), oneTimePassword, getDFP(), getNonce());
                ((LockerRestService) mLockerClient.getService()).unlockWithOneTimePassword(new LockerRequest(encryptSession(), encryptData(oneTimePasswordUnlockRequestJson)), new LockerCallback() {
                    @Override
                    public void success(LockerResponse lockerResponse, Response response) {
                        RegistrationOrUnlockResponseJson registrationOrUnlockResponseJson = decryptRegistrationOrUnlockData(lockerResponse.getData());
                        if (registrationOrUnlockResponseJson.getRemainingAttempts() != null) {
                            executeSuccessOnMainThreadAndFreeQueue(callback, new RegistrationOrUnlockResponse(null, registrationOrUnlockResponseJson.getRemainingAttempts()), response);
                            mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "LOCKED", "One time password unlock failed. Remaining attempts: " + registrationOrUnlockResponseJson.getRemainingAttempts()), LogLevel.INFO);
                        } else {
                            // check access token
                            AccessToken accessToken;
                            CsRestError error = checkAccessTokenPresenceOrReturnError(registrationOrUnlockResponseJson, response);
                            if (error != null) {
                                unregisterUserInternally();
                                executeFailureOnMainThreadAndFreeQueue(callback, error);
                                mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "One time password unlock failed with error:" + error.getLocalizedMessage()), LogLevel.INFO);
                                return;
                            } else
                                accessToken = new AccessToken(registrationOrUnlockResponseJson.getAccessToken(), registrationOrUnlockResponseJson.getAccessTokenExpiration());
                            mKeychainManager.setEK(registrationOrUnlockResponseJson.getEncryptionKey().getBytes());
                            mKeychainManager.storeAccessToken(accessToken);
                            mStatusManager.setState();
                            executeSuccessOnMainThreadAndFreeQueue(callback, new RegistrationOrUnlockResponse(accessToken, registrationOrUnlockResponseJson.getRemainingAttempts()), response);
                            mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNLOCKED", "One time password unlock was successful."), LogLevel.INFO);
                        }
                    }

                    @Override
                    public void failure(CsRestError error) {
                        if (shouldUnregister(error)) {
                            unregisterUserInternally();
                            mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "One time password unlock failed. No more attempts. User unregistered."), LogLevel.INFO);
                        } else
                            mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "LOCKED", "One time password unlock failed with error: " + error.getLocalizedMessage()), LogLevel.INFO);
                        executeFailureOnMainThreadAndFreeQueue(callback, error);
                    }
                });
            }
        });
    }

    @Override
    public void lock(final CsCallback<LockerStatus> lockerStatusCallback) {
        mBackgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                if (getStatus().getState() == State.USER_UNLOCKED || mStatusManager.isVerifiedOffline()) {
                    mKeychainManager.clearVolatileKeys();
                    mStatusManager.setIsVerifiedOffline(false);
                    mStatusManager.setState();
                    executeSuccessOnMainThreadAndFreeQueue(lockerStatusCallback, getStatus(), null);
                    mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "LOCKED", "Lock was successful."), LogLevel.INFO);
                } else
                    lockerStatusCallback.failure(new CsLockerError(CsLockerError.Kind.LOCK_FAILED));
            }
        });
    }

    @Override
    public void changePassword(String password, final Password newPassword, final CsCallback<PasswordResponse> callback) {
        if (getStatus().getState() == State.USER_UNLOCKED) {
            if (getStatus().getLockType() == LockType.NONE)
                password = mKeychainManager.retrieveNoLockPassword();
            final String random = getNewRandom();
            PasswordRequestJson passwordRequestJson = new PasswordRequestJson(mKeychainManager.retrieveCID(), mCryptoManager.encodeSha256(password, getDFP() + getRandom()), mCryptoManager.encodeSha256(newPassword.getPassword(), getDFP() + random), getDFP(), getNonce());
            ((LockerRestService) mLockerClient.getService()).changePassword(new LockerRequest(encryptSession(), encryptData(passwordRequestJson)), new LockerCallback() {
                @Override
                public void success(LockerResponse lockerResponse, Response response) {
                    PasswordResponseJson passwordResponseJson = null;
                    if (lockerResponse != null) {
                        passwordResponseJson = decryptPasswordData(lockerResponse.getData());
                    }
                    if (lockerResponse != null && passwordResponseJson.getRemainingAttempts() != null) {
                        callback.success(new PasswordResponse(passwordResponseJson.getRemainingAttempts()), response);
                        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNLOCKED", "Password change failed. Remaining attempts: " + passwordResponseJson.getRemainingAttempts()), LogLevel.INFO);
                    } else {
                        mKeychainManager.wipeNoLockPassword();
                        mKeychainManager.storePwdRandom(random);

                        LockType lockType = newPassword.getLockType();
                        mStatusManager.setLockType(lockType);
                        if (lockType == LockType.GESTURE || lockType == LockType.PIN)
                            mKeychainManager.storePasswordInputSpaceSize(newPassword.getPasswordSpaceSize());
                        else if (lockType == LockType.NONE)
                            mKeychainManager.storeNoLockPassword(newPassword.getPassword());
                        storeOfflineHash(newPassword);
                        callback.success(null, response);
                        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNLOCKED", "Password change with " + lockType + " was successful."), LogLevel.INFO);
                    }
                }

                @Override
                public void failure(CsRestError error) {
                    if (shouldUnregister(error)) {
                        unregisterUserInternally();
                        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Password change failed. No more attempts. User unregistered."), LogLevel.INFO);
                    } else
                        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNLOCKED", "Password change failed with error: " + error.getLocalizedMessage()), LogLevel.INFO);
                    callback.failure(error);
                }
            });
        } else
            callback.failure(new CsLockerError(CsLockerError.Kind.PASSWORD_CHANGE_FAILED));
    }

    private void changePasswordWithCustomHashAlgorithm(final Password password, PasswordHashProcess passwordHashProcess, final CsCallback<PasswordResponse> callback) {
        if (getStatus().getState() == State.USER_UNLOCKED) {
            String passwordString = password.getPassword();
            final String random = getNewRandom();
            PasswordRequestJson passwordRequestJson = new PasswordRequestJson(mKeychainManager.retrieveCID(), passwordHashProcess.hashPassword(passwordString), mCryptoManager.encodeSha256(passwordString, getDFP() + random), getDFP(), getNonce());
            ((LockerRestService) mLockerClient.getService()).changePassword(new LockerRequest(encryptSession(), encryptData(passwordRequestJson)), new LockerCallback() {
                @Override
                public void success(LockerResponse lockerResponse, Response response) {
                    PasswordResponseJson passwordResponseJson = null;
                    if (lockerResponse != null)
                        passwordResponseJson = decryptPasswordData(lockerResponse.getData());
                    if (lockerResponse != null && passwordResponseJson.getRemainingAttempts() != null) {
                        unregisterUserInternally();
                        CsLockerError error = new CsLockerError(CsLockerError.Kind.MIGRATION_UNLOCK_FAILED);
                        callback.failure(error);
                        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Migration change password failed with error:" + error.getLocalizedMessage()), LogLevel.INFO);
                    } else {
                        mKeychainManager.wipeNoLockPassword();
                        mKeychainManager.storePwdRandom(random);

                        LockType lockType = password.getLockType();
                        mStatusManager.setLockType(lockType);
                        if (lockType == LockType.GESTURE || lockType == LockType.PIN)
                            mKeychainManager.storePasswordInputSpaceSize(password.getPasswordSpaceSize());
                        else if (lockType == LockType.NONE)
                            mKeychainManager.storeNoLockPassword(password.getPassword());
                        storeOfflineHash(password);
                        callback.success(null, response);
                        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNLOCKED", "Migration password change with " + lockType + " was successful."), LogLevel.INFO);
                    }
                }

                @Override
                public void failure(CsRestError error) {
                    if (shouldUnregister(error))
                        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Migration change password failed. with error:" + error.getLocalizedMessage()), LogLevel.INFO);
                    else
                        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "UNREGISTERED", "Migration change password failed with error:" + error.getLocalizedMessage() + ". Please try again later. "), LogLevel.INFO);
                    callback.failure(error);
                }
            });
        } else
            callback.failure(new CsLockerError(CsLockerError.Kind.MIGRATION_UNLOCK_FAILED));
    }

    @Override
    public boolean processUrl(String url) {
        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "OAuth2", "Got OAuth2 url: " + url), LogLevel.DEBUG);
        if (url != null && url.startsWith(mLockerConfig.getRedirectUrl())) {
            String code = parseCodeFromUrl(url);
            if (code != null) {
                mKeychainManager.storeCode(code);
                LockerRegistrationProcess lockerRegistrationFactory = new LockerRegistrationProcessImpl();
                mCallbackOAuth.success(lockerRegistrationFactory);
                return true;
            }
            mCallbackOAuth.failure();
        }
        return false;
    }

    @Override
    public void setOnLockerStatusChangeListener(OnLockerStatusChangeListener onLockerStatusChangeListener) {
        mStatusManager.setLockerStatusChangedListener(onLockerStatusChangeListener);
    }

    @Override
    public void refreshToken(CsCallback<LockerStatus> callback) {
        CoreSDK.getInstance().refreshToken(mKeychainManager.retrieveRefreshToken(), callback);
    }

    @Override
    public void injectTestingJSForRegistration(String javascript) {
        mJavascript = javascript;
    }

    @Override
    public void setOAuthLoginActivityOptions(OAuthLoginActivityOptions oAuthLoginActivityOptions) {
        mOAuthLoginActivityOptions = oAuthLoginActivityOptions;
    }


    @Override
    public LockerStatus getStatus() {
        return mStatusManager.getStatus();
    }

    @Override
    public AccessToken getAccessToken() {
        return mKeychainManager.retrieveAccessToken();
    }

    @Override
    public boolean cancelOAuthLoginActivity() {
        return OAuthLoginActivity.finishOAuthLoginActivity();
    }

    @Override
    public void storeEncryptedSecret(String secret) {
        mKeychainManager.storeEncryptedSecret(secret);
    }

    @Override
    public String retrieveEncryptedSecret() {
        return mKeychainManager.retrieveEncryptedSecret();
    }

    @Override
    public void wipeEncryptedSecret() {
        mKeychainManager.wipeEncryptedSecret();
    }

    @Override
    public void storeIvSecret(String iv) {
        mKeychainManager.storeIvSecret(iv);
    }

    @Override
    public String retrieveIvSecret() {
        return mKeychainManager.retrieveIvSecret();
    }

    @Override
    public void wipeIvSecret() {
        mKeychainManager.wipeIvSecret();
    }

    private CsRestError checkAccessTokenPresenceOrReturnError(RegistrationOrUnlockResponseJson responseJson, Response response) {
        if (responseJson.getAccessToken() == null || responseJson.getAccessTokenExpiration() == null)
            return CsRestError.unexpectedError(response.getUrl(), new CsLockerError(CsLockerError.Kind.NO_ACCESS_TOKEN));
        else if (responseJson.getAccessToken().isEmpty() || responseJson.getAccessTokenExpiration().isEmpty())
            return CsRestError.unexpectedError(response.getUrl(), new CsLockerError(CsLockerError.Kind.PARSE_ERROR));
        return null;
    }

    private void initiateOAuth2(Context context) {
        if (!mTestFlag) {
            if (context != null) {
                String url = mLockerConfig.getEnvironment().getOAuth2ContextBaseUrl() + "/auth?state=" + STATE + "&redirect_uri=" + mLockerConfig.getRedirectUrl() + "&client_id=" + mLockerConfig.getClientId() + "&response_type=" + RESPONSE_TYPE + "&access_type=offline&approval_prompt=force";
                Intent intent = new Intent(context, OAuthLoginActivity.class);
                intent.putExtra(Constants.OAUTH_LOGIN_ACTIVITY_OPTIONS_EXTRA, mCsJson.toJson(mOAuthLoginActivityOptions));
                intent.putExtra(Constants.OAUTH_URL_EXTRA, url);
                intent.putExtra(Constants.TESTING_JS_EXTRA, mJavascript);
                intent.putExtra(Constants.REDIRECT_URL_EXTRA, mLockerConfig.getRedirectUrl());
                intent.putExtra(Constants.ALLOW_UNTRUSTED_CERTIFICATES_EXTRA, getConfigManager().getEnvironment().isAllowUntrustedCertificates());
                mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "OAuth2", "Request has been send with url: " + url), LogLevel.DEBUG);
                ((Activity) context).startActivityForResult(intent, Constants.OAUTH_REQUEST_CODE);
            } else
                mCallbackRegister.failure(new CsLockerError(CsLockerError.Kind.BAD_CONTEXT));
        }
    }

    private void storeOfflineHash(final Password password) {
        if (password.getLockType() != LockType.NONE) {
            mBackgroundThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    // wipe offline password
                    mKeychainManager.wipeOfflinePasswordHash();
                    // reset num of offline auth attempts to zero
                    mKeychainManager.storeNumOfOfflineAuthAttempts(0);
                    // check offline auth enabled and password validity, null or empty password cause crash
                    if (mLockerConfig.isOfflineAuthEnabled() && password.getPassword() != null && !password.getPassword().isEmpty())
                        mKeychainManager.storeOfflinePasswordHash(password);
                }
            });
        }
    }

    private boolean shouldUnlockOffline(CsRestError error) {
        // offline auth has to be enabled
        return mLockerConfig.isOfflineAuthEnabled()
                // offline auth is not allowed for LockType.NONE
                && getStatus().getLockType() != LockType.NONE
                // network kind error has to be received
                && error.getKind().equals(CsRestError.Kind.NETWORK)
                // Offline password hash is available
                && mKeychainManager.retrieveOfflinePasswordHash() != null
                // only two attempts available
                && mKeychainManager.retrieveNumOfOfflineAuthAttempts() < OFFLINE_AUTH_MAX_ATTEMPTS;
    }

    private void unlockOffline(final String passwordString, final CsCallback<RegistrationOrUnlockResponse> callback, final CsRestError error) {
        mBackgroundThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // increase num of offline auth attempts
                mKeychainManager.storeNumOfOfflineAuthAttempts(mKeychainManager.retrieveNumOfOfflineAuthAttempts() + 1);
                // result for offline verification is false by default
                boolean result = false;
                // check password validity, null or empty password cause crash
                if (passwordString != null && !passwordString.isEmpty()) {
                    // get required offline password hash
                    String required = mKeychainManager.retrieveOfflinePasswordHash();
                    // create password object to include lock type
                    LockType lockType = getStatus().getLockType();
                    Password password = new Password(lockType, passwordString);
                    if (lockType == LockType.GESTURE || lockType == LockType.PIN)
                        password.setPasswordSpaceSize(mKeychainManager.retrievePasswordInputSpaceSize());
                    // translate password to take into consideration the particular num of collisions
                    String translatedPassword = mCryptoManager.createOfflinePasswordWithCollision(password);
                    // generate actual offline password hash
                    String actual = mCryptoManager.encodeBase64(mCryptoManager.encryptPBKDF2(translatedPassword, getDFP() + getRandom()));
                    // compare actual and required passwords
                    result = required != null && actual != null && required.equals(actual);
                }
                // return callback
                // NO ACCESS TOKEN PROVIDED!
                returnUnlockOffline(result, callback, error);
            }
        });
    }

    private void returnUnlockOffline(final boolean result, final CsCallback<RegistrationOrUnlockResponse> callback, final CsRestError error) {
        if (result) {
            mStatusManager.setIsVerifiedOffline(true);
            mStatusManager.setState();
            mKeychainManager.storeNumOfOfflineAuthAttempts(0);
            executeSuccessOnMainThreadAndFreeQueue(callback, new OfflineUnlockResponse(null, error), error.getResponse());
            mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "VERIFIED OFFLINE", "Offline verification was successful"), LogLevel.INFO);
        } else {
            executeSuccessOnMainThreadAndFreeQueue(callback, new OfflineUnlockResponse(String.valueOf(OFFLINE_AUTH_MAX_ATTEMPTS - mKeychainManager.retrieveNumOfOfflineAuthAttempts()), error), error.getResponse());
            mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "NOT VERIFIED OFFLINE", "Offline verification failed. Remaining attempts: " + String.valueOf(OFFLINE_AUTH_MAX_ATTEMPTS - mKeychainManager.retrieveNumOfOfflineAuthAttempts())), LogLevel.INFO);
        }
    }


    /**
     * Unregister user internally.
     */
    void unregisterUserInternally() {
        mCallbackOAuth = null;
        mCallbackRegister = null;
        mKeychainManager.clearAll();
        mKeychainManager.clearVolatileKeys();
        mStatusManager.setState();
        mStatusManager.setIsVerifiedOffline(false);
    }

    private String encryptSession() {
        String sekBase64 = mCryptoManager.encodeBase64(getSEK(true));
        byte[] encryptedSession = mCryptoManager.encryptRSA(sekBase64, mCryptoManager.generatePublicKey(mLockerConfig.getPublicKey()));
        String base64encryptedSession = mCryptoManager.encodeBase64(encryptedSession);
        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "SessionEncryption", "Session before encryption: " + sekBase64 + ", session after encryption: " + base64encryptedSession), LogLevel.DETAILED_DEBUG);
        return base64encryptedSession;
    }

    private String encryptData(LockerRequestJson lockerRequestJson) {
        String data = mCsJson.toJson(lockerRequestJson);
        byte[] aesEncryptedData = mCryptoManager.encryptAES(data, getSEK(false), null);
        String base64encryptedData = mCryptoManager.encodeBase64(aesEncryptedData);
        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "DataEncryption", "Data before encryption: " + data + ", data after encryption: " + base64encryptedData), LogLevel.DETAILED_DEBUG);
        return base64encryptedData;
    }

    private RegistrationOrUnlockResponseJson decryptRegistrationOrUnlockData(String response) {
        byte[] data = mCryptoManager.decodeBase64(response);
        String dataString = mCryptoManager.decryptAES(data, getSEK(false), null);
        RegistrationOrUnlockResponseJson registrationOrUnlockResponseJson = mCsJson.fromJson(dataString, RegistrationOrUnlockResponseJson.class);
        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "RegistrationOrUnlockResponseDecryption", "Response before decryption: " + response + ", response after decryption: " + dataString), LogLevel.DETAILED_DEBUG);
        return registrationOrUnlockResponseJson;
    }

    private PasswordResponseJson decryptPasswordData(String response) {
        byte[] data = mCryptoManager.decodeBase64(response);
        String dataString = mCryptoManager.decryptAES(data, getSEK(false), null);
        PasswordResponseJson passwordResponseJson = mCsJson.fromJson(dataString, PasswordResponseJson.class);
        mLogManager.log(StringUtils.logLine(LOCKER_MODULE, "PasswordResponseDecryption", "Response before decryption: " + response + ", response after decryption: " + dataString), LogLevel.DETAILED_DEBUG);
        return passwordResponseJson;
    }

    private boolean shouldUnregister(CsRestError cause) {
        CsRestError.Kind kind = cause.getKind();
        if (
                CsRestError.Kind.NETWORK.equals(kind) ||
                        CsRestError.Kind.UNEXPECTED.equals(kind) ||
                        CsRestError.Kind.CONVERSION.equals(kind)) {
            return false;
        }

        //Lets check HTTP status code for more info
        if (CsRestError.Kind.HTTP.equals(kind) && cause.getResponse() != null) {
            int statusCode = cause.getResponse().getStatus();
            //Only deregister on Unauthorized code
            if (statusCode == 401) {
                return true;
            } else {
                //Let other errors pass
                return false;
            }
        }
        return true;
    }

    private String parseCodeFromUrl(String url) {
        String query = url.substring(url.lastIndexOf("?") + 1);
        Map<String, String> map = getQueryMap(query);
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if (key.equals(CODE_QUERY))
                return map.get(key);
        }
        return null;
    }

    private Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    private void checkEnvironmentHash(String clientId, String clientSecret) {
        String input = clientId + ":" + clientSecret;
        String hash = mCryptoManager.encodeSha1(input);
        String oldHash = mKeychainManager.retrieveEnvironmentHash();
        if (oldHash == null)
            mKeychainManager.storeEnvironmentHash(hash);
        else if (hash != null && !hash.equals(oldHash)) {
            mKeychainManager.clearAll();
            mKeychainManager.storeEnvironmentHash(hash);
        }
    }

    private void executeSuccessOnMainThreadAndFreeQueue(final CsCallback callback, final Object object, final Response response) {
        mMainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                callback.success(object, response);
                mBackgroundQueue.onQueueAvailable();
            }
        });
    }

    private void executeFailureOnMainThreadAndFreeQueue(final CsCallback callback, final CsSDKError error) {
        mMainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                callback.failure(error);
                mBackgroundQueue.onQueueAvailable();
            }
        });
    }

    /**
     * Set test nonce.
     *
     * @param nonce the nonce
     */
    void setTestNonce(String nonce) {
        mTestNonce = nonce;
    }

    /**
     * Set test random.
     *
     * @param random the random
     */
    void setTestRandom(String random) {
        mTestRandom = random;
    }

    /**
     * Set test new random.
     *
     * @param random the random
     */
    void setTestNewRandom(String random) {
        mTestNewRandom = random;
    }

    /**
     * Set test code.
     *
     * @param code the code
     */
    void setTestCode(String code) {
        mTestCode = code;
    }

    /**
     * Set test dfp.
     *
     * @param dfp the dfp
     */
    void setTestDFP(String dfp) {
        mTestDFP = dfp;
    }

    /**
     * Set test sek.
     *
     * @param sek the sek
     */
    void setTestSEK(byte[] sek) {
        mTestSEK = sek;
    }

    /**
     * Set test flag.
     */
    void setTestFlag() {
        mTestFlag = true;
    }

    /**
     * Set test time.
     *
     * @param time the time
     */
    void setTestTime(long time) {
        mTestTime = time;
    }

    /**
     * Get locker client locker client.
     *
     * @return the locker client
     */
    LockerClient getLockerClient() {
        return mLockerClient;
    }

    /**
     * Get config manager config manager.
     *
     * @return the config manager
     */
    LockerConfig getConfigManager() {
        return mLockerConfig;
    }

    private String getRandom() {
        if (mTestRandom != null)
            return mTestRandom;
        else if (mKeychainManager != null && mKeychainManager.retrievePwdRandom() != null)
            return mKeychainManager.retrievePwdRandom();
        else
            return mCryptoManager.generateRandomString();
    }

    private String getNewRandom() {
        if (mTestNewRandom != null)
            return mTestNewRandom;
        else
            return mCryptoManager.generateRandomString();
    }

    private String getNonce() {
        if (mTestNonce != null)
            return mTestNonce;
        else
            return mKeychainManager.getNonce();
    }

    private String getCode() {
        if (mTestCode != null)
            return mTestCode;
        else
            return mKeychainManager.retrieveCode();
    }

    private String getDFP() {
        if (mTestDFP != null)
            return mTestDFP;
        else
            return mKeychainManager.retrieveDFP();
    }

    private byte[] getSEK(boolean isEncryptSession) {
        if (mTestSEK != null)
            return mTestSEK;
        else if (isEncryptSession)
            return mKeychainManager.generateSEK();
        else
            return mKeychainManager.getSEK();
    }

    private long getTime() {
        if (mTestTime != null)
            return mTestTime;
        else
            return System.currentTimeMillis() / 1000;
    }

}

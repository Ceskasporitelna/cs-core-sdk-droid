package cz.csas.cscore.locker;

import android.content.Context;

import cz.csas.cscore.client.rest.CallbackBasic;
import cz.csas.cscore.client.rest.CsCallback;

/**
 * The interface Locker.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 11 /11/15.
 */
public interface Locker {

    /**
     * The constant LOCKER_MODULE.
     */
    static final String LOCKER_MODULE = "Locker";

    /**
     * Register. This method starts registration flow through oAuth2 authentication in webview and
     * registration itself.
     * Two callbacks are specified, because of oAuth2. The typical flow is as follows. At first,
     * you need to specify OAuthLoginActivity in your manifest file like this
     * <activity android:name="cz.csas.cscore.locker.OAuthLoginActivity" />
     * After calling register method, Locker will bring you to login activity and you will have to
     * handle activity result and call processUrl(String url) method.
     * If your url is correct, you will receive LockerRegistrationProcess object in the first callback.
     * This object is powerful enough to handle the rest of registration after user will choose his
     * LockType and password. Call lockerRegistrationProcess.finishRegistration(..) and wait for
     * response from the second callback. Then you will receive you server response as
     * RegistrationOrUnlockResponse.
     *
     * @param context            the context
     * @param callbackWithReturn the callback with return
     * @param callback           the callback
     */
    public void register(Context context, CallbackBasic<LockerRegistrationProcess> callbackWithReturn, CsCallback<RegistrationOrUnlockResponse> callback);

    /**
     * Unregister. This method is quite simple. It will unregister the registered user and returns
     * LockerStatus object. Check its documentation to know, what to expect.
     *
     * @param callback the callback
     */
    public void unregister(CsCallback<LockerStatus> callback);

    /**
     * Unlock. This method allow you to login the user. You need to pass through his password and if
     * everything goes right, you will receive RegistrationOrUnlockResponse. Check its own documentation
     * to know what to expect inside.  In case of .Failure see the remainingAttempts attribute. In
     * case of remainingAttempts == 0, the user is automatically unregistered.
     *
     * @param password the password
     * @param callback the callback
     */
    public void unlock(String password, CsCallback<RegistrationOrUnlockResponse> callback);

    /**
     * Unlock with one time password. This method will unlock you as the previous one, but the difference
     * is, that you dont use password to unlock. There is no remainingAttempts returned from server.
     * After an unsuccessfull unlock is the user automatically unregistered.
     *
     * @param callback the callback
     */
    public void unlockWithOneTimePassword(CsCallback<RegistrationOrUnlockResponse> callback);

    /**
     * Unlock after migration. This method will unlock you using the provided data and should be
     * used as the migration bridge to unlock without unnecessary new user registration.
     *
     * @param password                 in raw format
     * @param passwordMigrationProcess providing your actual hash algorithm and password transformation to our new format
     * @param data                     locker migration data
     * @param callback                 the callback
     */
    public void unlockAfterMigration(final Password password, PasswordMigrationProcess passwordMigrationProcess, final LockerMigrationData data, final CsCallback<RegistrationOrUnlockResponse> callback);

    /**
     * Lock the user. This method does not communicate with server.
     *
     * @param callback the callback
     */
    public void lock(CsCallback<LockerStatus> callback);

    /**
     * Change password method allows you to change users password. You will pass through old password,
     * new password and you will receive PasswordResponse. You can change LockType as well.
     *
     * @param password    the password
     * @param newPassword the new password
     * @param callback    the callback
     */
    public void changePassword(String password, Password newPassword, CsCallback<PasswordResponse> callback);

    /**
     * Process url boolean is method described with register method. It verify your received url and
     * process it.
     *
     * @param url the url
     * @return the boolean
     */
    public boolean processUrl(String url);

    /**
     * Sets on locker status change listener. This is LockerStatus listener, it wil notify you every
     * time LockerStatus changes.
     *
     * @param onLockerStatusChangeListener the on locker status change listener
     */
    public void setOnLockerStatusChangeListener(OnLockerStatusChangeListener onLockerStatusChangeListener);

    /**
     * Refresh token. Invokes the accessToken refresh using the stored registration code and current
     * access token.
     *
     * @param callback the callback
     */
    public void refreshToken(CsCallback<LockerStatus> callback);


    /**
     * Inject testing js for registration. This method accepts valid JavaScript that should be
     * injected into the page and executed when the login page finishes loading.
     *
     * @param javascript the javascript
     */
    public void injectTestingJSForRegistration(String javascript);

    /**
     * Sets o auth login activity options.
     *
     * @param oAuthLoginActivityOptions the o auth login activity options
     */
    public void setOAuthLoginActivityOptions(OAuthLoginActivityOptions oAuthLoginActivityOptions);

    /**
     * Gets status. Returns actual LockerStatus as LockerStatusChangeListener.
     *
     * @return the status
     */
    public LockerStatus getStatus();

    /**
     * Gets access token. Returns your accessToken.
     *
     * @return the access token
     */
    public AccessToken getAccessToken();

    /**
     * Cancel OAuthLoginActivity.
     * OAuth flow control method.
     *
     * @return the boolean
     */
    public boolean cancelOAuthLoginActivity();

    /**
     * Store encrypted secret.
     * Secret associated to users account
     *
     * @param secret the secret
     */
    public void storeEncryptedSecret(String secret);

    /**
     * Retrieve encrypted secret string.
     * Secret associated to users account
     *
     * @return the string
     */
    public String retrieveEncryptedSecret();

    /**
     * Wipe encrypted secret.
     * Secret associated to users account
     */
    public void wipeEncryptedSecret();

    /**
     * Store iv secret.
     * Secret init vector associated to users account
     *
     * @param iv the iv
     */
    public void storeIvSecret(String iv);

    /**
     * Retrieve iv secret string.
     * Secret init vector associated to users account
     *
     * @return the string
     */
    public String retrieveIvSecret();

    /**
     * Wipe iv secret.
     * Secret init vector associated to users account
     */
    public void wipeIvSecret();

}

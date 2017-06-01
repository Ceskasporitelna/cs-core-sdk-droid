package cz.csas.cscore;

import android.content.Context;

import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.locker.Locker;
import cz.csas.cscore.locker.LockerConfig;
import cz.csas.cscore.locker.LockerStatus;
import cz.csas.cscore.logger.LogManager;

/**
 * The type Core sdk.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13 /11/15.
 */
public abstract class CoreSDK {

    private static CoreSDK sharedInstance;

    /**
     * Get instance core sdk. So you can call all the methods below. Necessary is to call initialization
     * methods, before you use locker of other features.
     * <p/>
     * initialization methods are:
     * <p/>
     * REQUIRED:
     * - useContext(Context context);
     * - useWebApiKey(String webApiKey);
     * - useEnvironment(Environment environment);
     * OPTIONAL:
     * - useLocker(LockerConfig config);
     * - useLanguage(String language);
     * - useRequestSigning();
     * <p/>
     * All the methods are chainable, it is possible to call it as follows:
     * <p/>
     * CoreSDK.getInstance().useContext(context).useWebApiKey(webApiKey). etc.
     *
     * @return the core sdk
     */
    public static CoreSDK getInstance() {
        if (sharedInstance == null)
            sharedInstance = new CoreSDKImpl();
        return sharedInstance;
    }

    /**
     * Use locker method specifies the Locker attributes through the LockerConfig file. Must be called
     * before first locker property call, otherwise an exception is thrown.
     *
     * @param config the locker config
     * @return the core sdk
     */
    public abstract CoreSDK useLocker(LockerConfig config);

    /**
     * Use context for Core SDK oAuth handling and data loading.
     *
     * @param context the context
     * @return the core sdk
     */
    public abstract CoreSDK useContext(Context context);


    /**
     * Get locker returns initialized locker object. Core SDK has to be initialized before this method
     * is called.
     *
     * @return the locker
     */
    public abstract Locker getLocker();

    /**
     * Use web api key core sdk. It will set web api key fore Core SDK.
     *
     * @param webApiKey the web api key
     * @return the core sdk
     */
    public abstract CoreSDK useWebApiKey(String webApiKey);

    /**
     * Use environment core sdk. It will set environment for Core SDK.
     *
     * @param environment the environment
     * @return the core sdk
     */
    public abstract CoreSDK useEnvironment(Environment environment);

    /**
     * Use language core sdk. It will set language for Core SDK. Default language is cs-CZ.
     *
     * @param language the language
     * @return the core sdk
     */
    public abstract CoreSDK useLanguage(String language);

    /**
     * Use request signing core sdk. It will set request signing for Core SDK.
     *
     * @param privateKey the private key
     * @return the core sdk
     */
    public abstract CoreSDK useRequestSigning(String privateKey);

    /**
     * Use logger core sdk. It is possible to pass here any instance of {@link cz.csas.cscore.logger.LogManager}.
     * <p/>
     * Default logger uses the Android Log and should be obfuscated by proguard.
     *
     * @param logManager the log manager
     * @return the core sdk
     */
    public abstract CoreSDK useLogger(LogManager logManager);

    /**
     * Refresh token core sdk. This method will refresh your access token, so you don't have to ask
     * for a new one. You will receive LockerStatus in callback.
     *
     * @param refreshToken the refresh token
     * @param callback     the callback
     * @return the core sdk
     */
    public abstract void refreshToken(String refreshToken, CsCallback<LockerStatus> callback);

    /**
     * Is access token expired boolean.
     *
     * @return the boolean
     */
    public abstract boolean isAccessTokenExpired();

    /**
     * Get web api configuration.
     *
     * @return the web api configuration
     */
    public abstract WebApiConfiguration getWebApiConfiguration();

    /**
     * Get shared context.
     *
     * @return the shared context
     */
    public abstract SharedContext getSharedContext();

    /**
     * Gets logger.
     *
     * @return the logger
     */
    public abstract LogManager getLogger();

}

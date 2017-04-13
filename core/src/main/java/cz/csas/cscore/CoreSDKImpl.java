package cz.csas.cscore;

import android.content.Context;

import java.util.Map;

import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.client.WebApiConfigurationImpl;
import cz.csas.cscore.client.crypto.CryptoManager;
import cz.csas.cscore.client.crypto.CryptoManagerImpl;
import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsCoreError;
import cz.csas.cscore.error.CsLockerError;
import cz.csas.cscore.locker.AccessToken;
import cz.csas.cscore.locker.Locker;
import cz.csas.cscore.locker.LockerAccessTokenProvider;
import cz.csas.cscore.locker.LockerConfig;
import cz.csas.cscore.locker.LockerFactory;
import cz.csas.cscore.locker.LockerStatus;
import cz.csas.cscore.logger.LogLevel;
import cz.csas.cscore.logger.LogManager;
import cz.csas.cscore.logger.LogManagerImpl;
import cz.csas.cscore.storage.KeychainManager;
import cz.csas.cscore.storage.KeychainManagerImpl;
import cz.csas.cscore.utils.StringUtils;
import cz.csas.cscore.utils.TimeUtils;

/**
 * The type Core sdk.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 11 /11/15.
 */
class CoreSDKImpl extends CoreSDK {

    private final String GRANT_TYPE = "refresh_token";
    private final String TAG = "CS_LOG";
    private KeychainManager mKeychainManager;
    private CryptoManager mCryptoManager;
    private WebApiConfiguration mWebApiConfiguration;
    private LogManager mLogManager;
    private Context mContext;
    private Locker mLocker;
    private LockerConfig mLockerConfig;
    private SharedContext mSharedContext;

    // Test objects
    private Map<String, String> mTestHeaders;
    private String mTestRedirectUri;
    private Long mTestCurrentTime;

    /**
     * Instantiates a new Core sdk.
     */
    public CoreSDKImpl() {
        mCryptoManager = new CryptoManagerImpl();
        mWebApiConfiguration = new WebApiConfigurationImpl();
        mLogManager = new LogManagerImpl(TAG, LogLevel.INFO);
        mSharedContext = new SharedContext();
    }

    @Override
    public Locker getLocker() {
        if (mLocker != null)
            return mLocker;
        else
            throw new CsCoreError(CsCoreError.Kind.BAD_LOCKER_INITIALIZATION);
    }

    @Override
    public CoreSDK useLocker(LockerConfig config) {
        if (config.getClientSecret() == null || config.getClientId() == null || config.getPublicKey() == null || config.getRedirectUrl() == null || config.getScope() == null)
            throw new CsLockerError(CsLockerError.Kind.BAD_LOCKER_CONFIG);
        else if (mWebApiConfiguration.getWebApiKey() == null)
            throw new CsCoreError(CsCoreError.Kind.BAD_WEB_API_KEY);
        else if (mContext == null)
            throw new CsCoreError(CsCoreError.Kind.BAD_CONTEXT);
        else {
            mLockerConfig = config;
            setLocker();
        }
        return this;
    }

    @Override
    public CoreSDK useContext(Context context) {
        if (context == null)
            throw new CsCoreError(CsCoreError.Kind.BAD_CONTEXT);
        mContext = context;
        mKeychainManager = new KeychainManagerImpl(mContext, mCryptoManager);
        return this;
    }

    @Override
    public CoreSDK useWebApiKey(String webApiKey) {
        if (webApiKey == null)
            throw new CsCoreError(CsCoreError.Kind.BAD_WEB_API_KEY);
        else
            mWebApiConfiguration.setWebApiKey(webApiKey);
        return this;
    }

    @Override
    public CoreSDK useEnvironment(Environment environment) {
        if (environment == null)
            throw new CsCoreError(CsCoreError.Kind.BAD_ENVIRONMENT);
        else
            mWebApiConfiguration.setEnvironment(environment);
        if (mLocker != null)
            setLocker();
        return this;
    }

    @Override
    public CoreSDK useLanguage(String language) {
        if (language == null)
            throw new CsCoreError(CsCoreError.Kind.BAD_LANGUAGE);
        else
            mWebApiConfiguration.setLanguage(language);
        if (mLocker != null)
            setLocker();
        return this;
    }

    @Override
    public CoreSDKImpl useRequestSigning(String privateKey) {
        if (privateKey == null)
            throw new CsCoreError(CsCoreError.Kind.BAD_PRIVATE_KEY);
        else
            mWebApiConfiguration.setPrivateSigningKey(privateKey);
        if (mLocker != null)
            setLocker();
        return this;
    }

    /*
     * Logger is Locker specific parameter, so Locker has to be reset if it is already initialized
     */
    @Override
    public CoreSDK useLogger(LogManager logManager) {
        mLogManager = logManager;
        if (mLocker != null)
            setLocker();
        return this;
    }

    @Override
    public WebApiConfiguration getWebApiConfiguration() {
        return mWebApiConfiguration;
    }

    @Override
    public SharedContext getSharedContext() {
        return mSharedContext;
    }

    @Override
    public LogManager getLogger() {
        return mLogManager;
    }


    @Override
    public void refreshToken(String refreshToken, final Callback<LockerStatus> callback) {
        if (refreshToken == null)
            throw new CsLockerError(CsLockerError.Kind.REFRESH_TOKEN_FAILED);
        else {
            CoreClient coreClient = new CoreClient(mWebApiConfiguration);
            coreClient.setGlobalHeaders(getHeaders());
            ((CoreRestService) coreClient.getService()).refreshToken(mKeychainManager.retrieveCode(), mLockerConfig.getClientId(), mLockerConfig.getClientSecret(), getRedirectUri(), GRANT_TYPE, refreshToken, new RefreshTokenCallback() {
                @Override
                public void success(RefreshTokenResponse refreshTokenResponse, Response response) {
                    if (refreshTokenResponse.getAccessToken() == null || refreshTokenResponse.getExpiresIn() == null) {
                        CsRestError error = CsRestError.unexpectedError(response.getUrl(), new CsCoreError(CsCoreError.Kind.BAD_ACCESS_TOKEN));
                        callback.failure(error);
                        mLogManager.log(StringUtils.logLine(Locker.LOCKER_MODULE, "RefreshTokenError", "Refresh token failed with error: " + error.getLocalizedMessage()), LogLevel.DEBUG);
                    } else {
                        String expiresIn = TimeUtils.getMilisecondsTimestamp(getCurrentTime(), refreshTokenResponse.getExpiresIn());
                        mKeychainManager.storeAccessToken(new AccessToken(refreshTokenResponse.getAccessToken(), expiresIn));
                        callback.success(getLocker().getStatus(), response);
                        mLogManager.log(StringUtils.logLine(Locker.LOCKER_MODULE, "RefreshToken", "Refresh token successful."), LogLevel.DEBUG);
                    }
                }

                @Override
                public void failure(CsRestError error) {
                    mLogManager.log(StringUtils.logLine(Locker.LOCKER_MODULE, "RefreshTokenError", "Refresh token failed with error: " + error.getLocalizedMessage()), LogLevel.DEBUG);
                    callback.failure(error);
                }
            });
        }
    }

    @Override
    public boolean isAccessTokenExpired() {
        AccessToken accessToken = mKeychainManager.retrieveAccessToken();
        return accessToken != null && Long.valueOf(accessToken.getAccessTokenExpiration()) < getCurrentTime();
    }

    /**
     * Get context context.
     *
     * @return the context
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Gets keychain manager.
     *
     * @return the keychain manager
     */
    public KeychainManager getKeychainManager() {
        return mKeychainManager;
    }

    /**
     * Gets crypto manager.
     *
     * @return the crypto manager
     */
    public CryptoManager getCryptoManager() {
        return mCryptoManager;
    }

    /**
     * Set test headers.
     *
     * @param headers the headers
     */
    void setTestHeaders(Map<String, String> headers) {
        mTestHeaders = headers;
    }

    /**
     * Set test redirect uri.
     *
     * @param testRedirectUri the test redirect uri
     */
    void setTestRedirectUri(String testRedirectUri) {
        mTestRedirectUri = testRedirectUri;
    }

    /**
     * Set test current time.
     *
     * @param testCurrentTime the test current time
     */
    void setTestCurrentTime(Long testCurrentTime) {
        mTestCurrentTime = testCurrentTime;
    }

    private Map<String, String> getHeaders() {
        if (mTestHeaders != null)
            return mTestHeaders;
        return null;
    }

    private String getRedirectUri() {
        if (mTestRedirectUri != null)
            return mTestRedirectUri;
        return mLockerConfig.getRedirectUrl();
    }

    private long getCurrentTime() {
        if (mTestCurrentTime != null)
            return mTestCurrentTime;
        return System.currentTimeMillis();
    }

    private void setLocker() {
        mLockerConfig.setEnvironment(mWebApiConfiguration.getEnvironment());
        mLocker = LockerFactory.createLocker(mKeychainManager, mLockerConfig, mCryptoManager, mLogManager, mContext);
        mSharedContext.setLockerAccessTokenProvider(new LockerAccessTokenProvider(mLocker, mLogManager));

    }

    /**
     * Wipe keychain.
     *
     * @return the core sdk
     */
    public CoreSDK wipeKeychain() {
        mKeychainManager.clearAll();
        return this;
    }
}

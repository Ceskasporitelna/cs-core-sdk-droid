package cz.csas.cscore.locker;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.error.CsAccessTokenProviderError;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.logger.LogLevel;
import cz.csas.cscore.logger.LogManager;
import cz.csas.cscore.utils.StringUtils;
import cz.csas.cscore.webapi.AccessTokenProvider;

/**
 * The type LockerAccessTokenProvider is a specific instance of AccessTokenProvider using
 * {@link Locker} as an provider of AccessToken.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /05/16.
 */
public class LockerAccessTokenProvider implements AccessTokenProvider {


    private Locker locker;
    private LogManager logManager;

    /**
     * Instantiates a new Locker access token provider.
     *
     * @param locker the locker
     */
    public LockerAccessTokenProvider(Locker locker, LogManager logManager) {
        this.locker = locker;
        this.logManager = logManager;
    }

    @Override
    public void getAccessToken(CallbackWebApi<AccessToken> callback) {
        CsAccessTokenProviderError error = null;
        if (locker.getStatus().getClientId() != null) {
            if (locker.getStatus().getState() == State.USER_UNLOCKED) {
                AccessToken accessToken = locker.getAccessToken();
                if (accessToken != null &&
                        accessToken.getAccessToken() != null &&
                        accessToken.getAccessTokenExpiration() != null) {
                    if (!CoreSDK.getInstance().isAccessTokenExpired()) {
                        callback.success(accessToken);
                        logManager.log(StringUtils.logLine(ACCESS_TOKEN_PROVIDER_MODULE, "ProvideToken", "Providing access token successful."), LogLevel.DEBUG);
                        return;
                    }
                    refreshAccessToken(callback);
                } else {
                    error = new CsAccessTokenProviderError(CsAccessTokenProviderError.Kind.NOT_AVAILABLE);
                    callback.failure(error);
                }
            } else {
                error = new CsAccessTokenProviderError(CsAccessTokenProviderError.Kind.LOCKED);
                callback.failure(error);
            }
        } else {
            error = new CsAccessTokenProviderError(CsAccessTokenProviderError.Kind.UNREGISTERED);
            callback.failure(error);
        }
        if (error != null)
            logManager.log(StringUtils.logLine(ACCESS_TOKEN_PROVIDER_MODULE, "ProvideTokenError", "Providing access token failed with error: " + error.getLocalizedMessage()), LogLevel.DEBUG);
    }

    @Override
    public void refreshAccessToken(final CallbackWebApi<AccessToken> callback) {
        locker.refreshToken(new CsCallback<LockerStatus>() {
            @Override
            public void success(LockerStatus lockerStatus, Response response) {
                callback.success(locker.getAccessToken());
            }

            @Override
            public void failure(CsSDKError error) {
                callback.failure(new CsAccessTokenProviderError(CsAccessTokenProviderError.Kind.NOT_AVAILABLE));
            }
        });
    }
}

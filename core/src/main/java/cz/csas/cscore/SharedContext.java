package cz.csas.cscore;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsAccessTokenProviderError;
import cz.csas.cscore.locker.AccessToken;
import cz.csas.cscore.locker.LockerAccessTokenProvider;
import cz.csas.cscore.webapi.AccessTokenProvider;

/**
 * The type Shared context provides context based utilities.
 * The accessToken orchestration is provided through sharedContext.
 * See aldo {@link LockerAccessTokenProvider}
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /05/16.
 */
public class SharedContext implements AccessTokenProvider {

    private LockerAccessTokenProvider lockerAccessTokenProvider;

    @Override
    public void getAccessToken(CallbackWebApi<AccessToken> callback) {
        if (lockerAccessTokenProvider != null)
            lockerAccessTokenProvider.getAccessToken(callback);
        else
            callback.failure(new CsAccessTokenProviderError(CsAccessTokenProviderError.Kind.NOT_INITIALIZED));
    }

    @Override
    public void refreshAccessToken(CallbackWebApi<AccessToken> callback) {
        if (lockerAccessTokenProvider != null)
            lockerAccessTokenProvider.refreshAccessToken(callback);
        else
            callback.failure(new CsAccessTokenProviderError(CsAccessTokenProviderError.Kind.NOT_INITIALIZED));
    }

    /**
     * Get locker access token provider.
     *
     * @return the locker access token provider
     */
    public LockerAccessTokenProvider getLockerAccessTokenProvider() {
        return lockerAccessTokenProvider;
    }

    /**
     * Set locker access token provider.
     *
     * @param lockerAccessTokenProvider
     */
    void setLockerAccessTokenProvider(LockerAccessTokenProvider lockerAccessTokenProvider) {
        this.lockerAccessTokenProvider = lockerAccessTokenProvider;
    }
}

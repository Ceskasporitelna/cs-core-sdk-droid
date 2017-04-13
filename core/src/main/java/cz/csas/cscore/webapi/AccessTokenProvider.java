package cz.csas.cscore.webapi;

import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.locker.AccessToken;

/**
 * The interface Access token provider handles the access token dispatching to the
 * {@link WebApiClient} which need header authorization. It also handle the access token refresh in
 * a case of expired token.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /05/16.
 */
public interface AccessTokenProvider {

    static final String ACCESS_TOKEN_PROVIDER_MODULE = "AccessTokenProvider";

    /**
     * Get access token provides the access token dispatching.
     *
     * @param callback the callback
     */
    public void getAccessToken(CallbackWebApi<AccessToken> callback);

    /**
     * Refresh access token.
     * Convenience method for {@link cz.csas.cscore.CoreSDK#refreshToken(String, Callback)}
     *
     * @param callback the callback
     */
    public void refreshAccessToken(CallbackWebApi<AccessToken> callback);
}

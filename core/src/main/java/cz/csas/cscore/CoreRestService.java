package cz.csas.cscore;

import cz.csas.cscore.client.RestService;
import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.http.CsField;
import cz.csas.cscore.client.rest.http.CsFormUrlEncoded;
import cz.csas.cscore.client.rest.http.CsPost;

/**
 * The interface Core rest service.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16 /11/15.
 */
interface CoreRestService extends RestService {

    /**
     * Refresh token.
     *
     * @param code         the code
     * @param clientId     the client id
     * @param clientSecret the client secret
     * @param redirectUrl  the redirect url
     * @param grantType    the grant type
     * @param refreshToken the refresh token
     * @param callback     the callback
     */
    @CsFormUrlEncoded
    @CsPost("/token")
    public void refreshToken(@CsField("code") String code, @CsField("client_id") String clientId, @CsField("client_secret") String clientSecret, @CsField("redirect_uri") String redirectUrl, @CsField("grant_type") String grantType, @CsField("refresh_token") String refreshToken, Callback<RefreshTokenResponse> callback);

}

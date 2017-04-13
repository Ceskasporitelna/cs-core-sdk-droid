package cz.csas.cscore;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.utils.csjson.annotations.CsSerializedName;

/**
 * The type Refresh token response.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16 /11/15.
 */
class RefreshTokenResponse {

    @CsExpose
    @CsSerializedName(value = "access_token")
    private String accessToken;

    @CsExpose
    @CsSerializedName(value = "expires_in")
    private Long expiresIn;

    @CsExpose
    @CsSerializedName(value = "token_type")
    private String tokenType;

    /**
     * Gets access token.
     *
     * @return the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Gets expires in.
     *
     * @return the expires in
     */
    public Long getExpiresIn() {
        return expiresIn;
    }

    /**
     * Gets token type.
     *
     * @return the token type
     */
    public String getTokenType() {
        return tokenType;
    }
}

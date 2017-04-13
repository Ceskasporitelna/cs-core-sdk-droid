package cz.csas.cscore;

/**
 * The type Environment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16 /11/15.
 */
public class Environment {

    private static final String PRODUCTION_BASE_URL_OAUTH = "https://www.csas.cz/widp/oauth2";
    private static final String PRODUCTION_BASE_URL = "https://www.csas.cz/webapi";
    private static final String SANDBOX_BASE_URL_OAUTH = "https://api.csas.cz/sandbox/widp/oauth2";
    private static final String SANDBOX_BASE_URL = "https://api.csas.cz/sandbox/webapi";

    /**
     * The constant PRODUCTION.
     */
    public static Environment PRODUCTION = new Environment(PRODUCTION_BASE_URL, PRODUCTION_BASE_URL_OAUTH,false);
    /**
     * The constant SANDBOX.
     */
    public static Environment SANDBOX = new Environment(SANDBOX_BASE_URL, SANDBOX_BASE_URL_OAUTH,false);

    private String mApiContextBaseUrl;
    private String mOAuth2ContextBaseUrl;
    private boolean mAllowUntrustedCertificates;

    /**
     * Instantiates a new Environment.
     *
     * @param apiContextBaseUrl          the api context base url
     * @param oAuth2ContextBaseUrl       the o auth 2 context base url
     * @param allowUntrustedCertificates the allow untrusted certificates
     */
    public Environment(String apiContextBaseUrl, String oAuth2ContextBaseUrl, boolean allowUntrustedCertificates) {
        mApiContextBaseUrl = apiContextBaseUrl;
        mOAuth2ContextBaseUrl = oAuth2ContextBaseUrl;
        mAllowUntrustedCertificates = allowUntrustedCertificates;
    }

    /**
     * Sets api context base url.
     *
     * @param mApiContextBaseUrl the api context base url
     */
    public void setApiContextBaseUrl(String mApiContextBaseUrl) {
        this.mApiContextBaseUrl = mApiContextBaseUrl;
    }

    /**
     * Sets auth 2 context base url.
     *
     * @param mOAuth2ContextBaseUrl the o auth 2 context base url
     */
    public void setOAuth2ContextBaseUrl(String mOAuth2ContextBaseUrl) {
        this.mOAuth2ContextBaseUrl = mOAuth2ContextBaseUrl;
    }

    /**
     * Sets allow untrusted certificates.
     *
     * @param mAllowUntrustedCertificates the m allow untrusted certificates
     */
    public void setAllowUntrustedCertificates(boolean mAllowUntrustedCertificates) {
        this.mAllowUntrustedCertificates = mAllowUntrustedCertificates;
    }

    /**
     * Gets api context base url.
     *
     * @return the api context base url
     */
    public String getApiContextBaseUrl() {
        return mApiContextBaseUrl;
    }

    /**
     * Gets auth 2 context base url.
     *
     * @return the auth 2 context base url
     */
    public String getOAuth2ContextBaseUrl() {
        return mOAuth2ContextBaseUrl;
    }

    /**
     * Is allow untrusted certificates boolean.
     *
     * @return the boolean
     */
    public boolean isAllowUntrustedCertificates() {
        return mAllowUntrustedCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Environment)) return false;

        Environment that = (Environment) o;

        if (mApiContextBaseUrl != null ? !mApiContextBaseUrl.equals(that.mApiContextBaseUrl) : that.mApiContextBaseUrl != null)
            return false;
        return !(mOAuth2ContextBaseUrl != null ? !mOAuth2ContextBaseUrl.equals(that.mOAuth2ContextBaseUrl) : that.mOAuth2ContextBaseUrl != null);

    }

    @Override
    public int hashCode() {
        int result = mApiContextBaseUrl != null ? mApiContextBaseUrl.hashCode() : 0;
        result = 31 * result + (mOAuth2ContextBaseUrl != null ? mOAuth2ContextBaseUrl.hashCode() : 0);
        return result;
    }
}

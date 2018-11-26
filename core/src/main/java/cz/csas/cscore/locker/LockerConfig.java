package cz.csas.cscore.locker;

import cz.csas.cscore.Environment;

/**
 * The type Locker config.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 11 /11/15.
 */
public class LockerConfig {


    /**
     * Locker attributes are specified through LockerConfig file.
     * You can use Builder to create LockerConfig.
     * <p/>
     * clientId - The client identifier
     * clientSecret
     * redirectUrl - Specifies url scheme and host. f.e. csastest://auth-completed where csastest is scheme and auth-completed is host
     * scope - A locker scope, such as "/v1/netbanking".
     * publicKey - A WebApi public key to encrypt request data.
     */
    private String clientId;
    private String clientSecret;
    private String caseMobileRedirectUrl;
    private String redirectUrl;
    private String scope;
    private String publicKey;
    private boolean offlineAuthEnabled = false;
    private Environment environment;

    private LockerConfig(String clientId, String clientSecret, String redirectUrl, String scope, String publicKey, boolean offlineAuthEnabled, String caseMobileRedirectUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
        this.scope = scope;
        this.publicKey = publicKey;
        this.offlineAuthEnabled = offlineAuthEnabled;
        this.caseMobileRedirectUrl = caseMobileRedirectUrl;
    }

    /**
     * Gets client id.
     *
     * @return the client id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Gets client secret.
     *
     * @return the client secret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Gets redirect url.
     *
     * @return the redirect url
     */
    public String getRedirectUrl() {
        return redirectUrl;
    }

    /**
     * Gets scope.
     *
     * @return the scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * Gets public key.
     *
     * @return the public key
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Is offline auth enabled boolean.
     *
     * @return the boolean
     */
    public boolean isOfflineAuthEnabled() {
        return offlineAuthEnabled;
    }

    /**
     * Gets environment.
     *
     * @return the environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Sets environment.
     *
     * @param environment the environment
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    /**
     * Gets case mobile redirect url.
     *
     * @return the case mobile redirect url.
     */
    public String getCaseMobileRedirectUrl() {
        return caseMobileRedirectUrl;
    }


    /**
     * The type Builder.
     */
    public static class Builder {

        private String clientId;

        private String clientSecret;

        private String caseMobileRedirectUrl;

        private String redirectUrl;

        private String scope;

        private String publicKey;

        private boolean offlineAuthEnabled = false;

        /**
         * Set client id builder.
         *
         * @param clientId the client id
         * @return the builder
         */
        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        /**
         * Set client secret builder.
         *
         * @param clientSecret the client secret
         * @return the builder
         */
        public Builder setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        /**
         * Set redirect url builder.
         *
         * @param redirectUrl the redirect url
         * @return the builder
         */
        public Builder setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }

        /**
         * Set scope builder.
         *
         * @param scope the scope
         * @return the builder
         */
        public Builder setScope(String scope) {
            this.scope = scope;
            return this;
        }

        /**
         * Set public key builder.
         *
         * @param publicKey the public key
         * @return the builder
         */
        public Builder setPublicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        /**
         * Sets offline auth enabled.
         *
         * @return the offline auth enabled
         */
        public Builder setOfflineAuthEnabled() {
            this.offlineAuthEnabled = true;
            return this;
        }

        /**
         * Sets case mobile redirect url.
         *
         * @param caseMobileRedirectUrl the case mobile redirect url.
         */
        public Builder setCaseMobileRedirectUrl(String caseMobileRedirectUrl) {
            this.caseMobileRedirectUrl = caseMobileRedirectUrl;
            return this;
        }


        /**
         * Create locker config.
         *
         * @return the locker config
         */
        public LockerConfig create() {
            return new LockerConfig(clientId, clientSecret, redirectUrl, scope, publicKey, offlineAuthEnabled, caseMobileRedirectUrl);
        }


    }
}

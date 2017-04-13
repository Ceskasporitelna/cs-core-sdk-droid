package cz.csas.cscore.client;

import cz.csas.cscore.Environment;

/**
 * The type Config manager.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
public class WebApiConfigurationImpl implements WebApiConfiguration {

    private final String CS_LANGUAGE = "cs-CZ";
    private String mPrivateSigningKey;
    private String mWebApiKey;
    private Environment mEnvironment;
    private String mLanguage;

    /**
     * Instantiates a new Cs configuration.
     */
    public WebApiConfigurationImpl() {
    }

    /**
     * Instantiates a new Cs configuration.
     *
     * @param webApiKey         the web api key
     * @param environment       the environment
     * @param language          the language
     * @param privateSigningKey the private signing key
     */
    public WebApiConfigurationImpl(String webApiKey, Environment environment, String language, String privateSigningKey) {
        mWebApiKey = webApiKey;
        mEnvironment = environment;
        mLanguage = language;
        mPrivateSigningKey = privateSigningKey;
    }

    @Override
    public void setWebApiKey(String webApiKey) {
        mWebApiKey = webApiKey;
    }

    @Override
    public String getWebApiKey() {
        return mWebApiKey;
    }

    @Override
    public void setEnvironment(Environment environment) {
        mEnvironment = environment;
    }

    @Override
    public Environment getEnvironment() {
        if (mEnvironment == null)
            return Environment.SANDBOX;
        return mEnvironment;
    }

    @Override
    public void setLanguage(String language) {
        mLanguage = language;
    }

    @Override
    public String getLanguage() {
        if (mLanguage == null)
            return CS_LANGUAGE;
        return mLanguage;
    }

    @Override
    public String getPrivateSigningKey() {
        return mPrivateSigningKey;
    }

    @Override
    public void setPrivateSigningKey(String privateKey) {
        mPrivateSigningKey = privateKey;
    }

}

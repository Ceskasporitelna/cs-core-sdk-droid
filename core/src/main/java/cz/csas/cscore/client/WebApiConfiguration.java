package cz.csas.cscore.client;

import cz.csas.cscore.Environment;

/**
 * The interface Config manager.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
public interface WebApiConfiguration {

    /**
     * Sets web api key.
     *
     * @param webApiKey the web api key
     */
    public void setWebApiKey(String webApiKey);

    /**
     * Gets web api key.
     *
     * @return the web api key
     */
    public String getWebApiKey();

    /**
     * Sets environment.
     *
     * @param environment the environment
     */
    public void setEnvironment(Environment environment);

    /**
     * Gets web api key.
     *
     * @return the web api key
     */
    public Environment getEnvironment();

    /**
     * Sets language.
     *
     * @param Language the language
     */
    public void setLanguage(String Language);

    /**
     * Gets language.
     *
     * @return the language
     */
    public String getLanguage();

    /**
     * Gets private signing key.
     *
     * @return the private signing key
     */
    public String getPrivateSigningKey();

    /**
     * Sets private signing key.
     *
     * @param privateKey the private key
     */
    public void setPrivateSigningKey(String privateKey);

}

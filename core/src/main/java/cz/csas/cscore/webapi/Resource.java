package cz.csas.cscore.webapi;

/**
 * The type Resource.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public abstract class Resource {

    /**
     * The Base path.
     */
    protected String basePath;
    /**
     * The Client.
     */
    protected WebApiClient client;

    /**
     * Instantiates a new Resource.
     *
     * @param basePath the base path
     * @param client   the client
     */
    public Resource(String basePath, WebApiClient client) {
        this.basePath = basePath;
        this.client = client;
    }

    /**
     * Append path with string.
     *
     * @param appendix the appendix
     * @return the string
     */
    protected String appendPathWith(String appendix){
        if(appendix == null)
            return getBasePath();
        return getBasePath() + "/" + appendix;
    }

    /**
     * Get base path string.
     *
     * @return the string
     */
    public String getBasePath(){
        return basePath;
    }

    /**
     * Get client web api client base.
     *
     * @return the web api client base
     */
    public WebApiClient getClient(){
        return client;
    }

}

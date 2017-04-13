package cz.csas.cscore.webapi;

/**
 * The type Instance resource.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public abstract class InstanceResource extends Resource {

    private Object id;

    /**
     * Instantiates a new Instance resource.
     *
     * @param id       the id
     * @param basePath the base path
     * @param client   the client
     */
    public InstanceResource(Object id, String basePath, WebApiClient client) {
        super(basePath, client);
        this.id = id;
    }

    @Override
    public String getBasePath() {
        return basePath + "/" + getId();
    }

    /**
     * Get id string.
     *
     * @return the string
     */
    public String getId() {
        return id.toString();
    }
}

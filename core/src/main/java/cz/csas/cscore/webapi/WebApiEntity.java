package cz.csas.cscore.webapi;

/**
 * The type Web api entity.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 30 /12/15.
 */
public abstract class WebApiEntity {

    /**
     * The M resource.
     */
    protected Resource resource;

    /**
     * The Path suffix.
     */
    protected String pathSuffix;

    /**
     * Sets resource.
     *
     * @param resource the resource
     */
    public void setResource(Resource resource){
        this.resource = resource;
    }

    /**
     * Sets path suffix.
     *
     * @param pathSuffix the path suffix
     */
    public void setPathSuffix(String pathSuffix) {
        this.pathSuffix = pathSuffix;
    }

    /**
     * Gets path suffix.
     *S
     * @return the path suffix
     */
    public String getPathSuffix() {
        return pathSuffix;
    }

    /**
     * Gets resource.
     *
     * @return the resource
     */
    public Resource getResource() {
        return resource;
    }

}

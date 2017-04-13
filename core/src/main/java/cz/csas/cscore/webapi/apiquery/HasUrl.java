package cz.csas.cscore.webapi.apiquery;

/**
 * Marks Resource or Entity that supports .url Verb.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
public interface HasUrl {

    /**
     * Returns an url for given resource
     *
     * @return the string
     */
    public String url();
}

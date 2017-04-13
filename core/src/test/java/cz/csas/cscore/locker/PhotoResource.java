package cz.csas.cscore.locker;

import cz.csas.cscore.utils.UrlUtils;
import cz.csas.cscore.webapi.Resource;
import cz.csas.cscore.webapi.WebApiClient;
import cz.csas.cscore.webapi.apiquery.HasParametrizedUrl;
import cz.csas.cscore.webapi.apiquery.HasUrl;

/**
 * The type Photo resource.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 18 /04/16.
 */
public class PhotoResource extends Resource implements HasUrl, HasParametrizedUrl<PhotoParameters> {
    /**
     * Instantiates a new Resource.
     *
     * @param basePath the base path
     * @param client   the client
     */
    public PhotoResource(String basePath, WebApiClient client) {
        super(basePath, client);
    }

    @Override
    public String url() {
        return getBasePath();
    }

    @Override
    public String url(PhotoParameters parameters) {
        return getBasePath()+ UrlUtils.toQueryString(parameters.toDictionary());
    }
}
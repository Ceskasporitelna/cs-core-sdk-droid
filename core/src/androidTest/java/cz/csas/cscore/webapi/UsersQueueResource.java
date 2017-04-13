package cz.csas.cscore.webapi;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.apiquery.ListEnabled;

/**
 * The type Users queue resource.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 25 /03/16.
 */
public class UsersQueueResource extends Resource implements ListEnabled<UserQueueListResponse> {

    /**
     * Instantiates a new Resource.
     *
     * @param basePath the base path
     * @param client   the client
     */
    public UsersQueueResource(String basePath, WebApiClient client) {
        super(basePath, client);
    }

    @Override
    public void list(CallbackWebApi<UserQueueListResponse> callback) {
        ResourceUtils.callList(this,null,UserQueueListResponse.class,callback);
    }
}

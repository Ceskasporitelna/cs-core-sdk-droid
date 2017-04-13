package cz.csas.cscore.webapi;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.apiquery.GetEnabled;
import cz.csas.cscore.webapi.apiquery.UpdateEnabled;

/**
 * The type Post resource.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 26 /12/15.
 */
public class PostResource extends InstanceResource implements GetEnabled<Post>, UpdateEnabled<UpdatePostRequest, WebApiEmptyResponse> {

    /**
     * Instantiates a new Instance resource.
     *
     * @param id       the id
     * @param basePath the base path
     * @param client   the client
     */
    public PostResource(Object id, String basePath, WebApiClient client) {
        super(id, basePath, client);
    }

    @Override
    public void get(CallbackWebApi<Post> callback) {
        ResourceUtils.callGet(this, null, null, Post.class, callback);
    }

    @Override
    public void update(UpdatePostRequest request, CallbackWebApi<WebApiEmptyResponse> callback) {
        ResourceUtils.callUpdate(this, request, null, WebApiEmptyResponse.class, callback);
    }
}

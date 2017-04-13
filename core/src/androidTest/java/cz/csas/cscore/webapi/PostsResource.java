package cz.csas.cscore.webapi;

import java.util.ArrayList;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.apiquery.CreateEnabled;
import cz.csas.cscore.webapi.apiquery.HasInstanceResource;
import cz.csas.cscore.webapi.apiquery.ParametrizedListEnabled;

/**
 * The type Posts resource.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 26 /12/15.
 */
public class PostsResource extends Resource implements HasInstanceResource<PostResource>, CreateEnabled<CreatePostRequest, Post>, ParametrizedListEnabled<PostListParameters, PostListResponse> {

    /**
     * Instantiates a new Posts resource.
     *
     * @param basePath the base path
     * @param client   the client
     */
    public PostsResource(String basePath, WebApiClient client) {
        super(basePath, client);
    }

    @Override
    public PostResource withId(Object id) {
        return new PostResource(id, getBasePath(), getClient());
    }

    @Override
    public void list(PostListParameters parameters, final CallbackWebApi<PostListResponse> callback) {
        ResourceUtils.callParametrizedList(this, parameters, new Transform<PostListResponse>() {
            @Override
            protected PostListResponse doTransform(PostListResponse entity, CsSDKError error, Response response) throws CsSDKError {
                if (entity != null)
                    return entity;
                else {
                    if (response.getStatus() == 404)
                        return new PostListResponse(new ArrayList<Post>());
                    throw error;
                }
            }
        }, PostListResponse.class, callback);
    }

    @Override
    public void create(CreatePostRequest request, CallbackWebApi<Post> callback) {
        ResourceUtils.callCreate(this, request, null, Post.class, callback);
    }
}

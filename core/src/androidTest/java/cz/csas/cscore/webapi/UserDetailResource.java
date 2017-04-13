package cz.csas.cscore.webapi;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.apiquery.GetEnabled;
import cz.csas.cscore.webapi.apiquery.UpdateEnabled;

/**
 * The type User detail resource.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 26 /12/15.
 */
public class UserDetailResource extends InstanceResource implements GetEnabled<UserDetail>, UpdateEnabled<UpdateUserRequest,UserDetail> {

    /**
     * Instantiates a new User detail resource.
     *
     * @param id       the id
     * @param basePath the base path
     * @param client   the client
     */
    public UserDetailResource(Object id, String basePath, WebApiClient client) {
        super(id, basePath, client);
    }

    @Override
    public void get(CallbackWebApi<UserDetail> callback) {
        ResourceUtils.callGet(this, null, new Transform<UserDetail>() {
            @Override
            protected UserDetail doTransform(UserDetail entity, CsSDKError error, Response response) throws CsSDKError {
                if(entity != null){
                    if (entity.getName().equals("Dog"))
                        throw new WebApiTestError("Sadly, this is not human", "THIS_IS_NOT_HUMAN");
                    return entity;
                }else
                    throw error;
            }
        }, UserDetail.class, callback);
    }

    @Override
    public void update(UpdateUserRequest request, CallbackWebApi<UserDetail> callback) {
        request.setUserId(Integer.valueOf(getId()));
        ResourceUtils.callUpdate(this, request,null, UserDetail.class, callback);
    }
}

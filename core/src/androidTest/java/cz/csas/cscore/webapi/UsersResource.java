package cz.csas.cscore.webapi;

import java.util.Map;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.apiquery.CreateEnabled;
import cz.csas.cscore.webapi.apiquery.HasInstanceResource;
import cz.csas.cscore.webapi.apiquery.PaginatedListEnabled;

/**
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 26/12/15.
 */
public class UsersResource extends Resource implements HasInstanceResource<UserDetailResource>, PaginatedListEnabled<UserListParameters, UserListResponse>, CreateEnabled<CreateUserRequest, UserDetail> {

    public UsersResource(String basePath, WebApiClient client) {
        super(basePath, client);
    }

    @Override
    public void create(CreateUserRequest request, CallbackWebApi<UserDetail> callback) {
        ResourceUtils.callCreate(this, request, new Transform<UserDetail>() {
            @Override
            protected UserDetail doTransform(UserDetail entity, CsSDKError error, Response response) throws CsSDKError {
                if (entity != null)
                    return entity;
                else if (response.getStatus() >= 400 && response.getStatus() < 599) {
                    String errorCode = null;
                    String errorMessage = null;

                    Map<String, Object> jsonDictionary = response.getBodyJsonDictionary();
                    for (String key : jsonDictionary.keySet()) {
                        if (key.equals("errorCode"))
                            errorCode = (String) jsonDictionary.get(key);
                        else if (key.equals("errorMessage"))
                            errorMessage = (String) jsonDictionary.get(key);
                    }
                    if (errorMessage != null && errorCode != null)
                        throw new WebApiTestError(errorMessage, errorCode);
                }
                throw error;
            }
        }, UserDetail.class, callback);
    }

    @Override
    public UserDetailResource withId(Object id) {
        return new UserDetailResource(id, getBasePath(), getClient());
    }

    @Override
    public void list(UserListParameters parameters, final CallbackWebApi<UserListResponse> callback) {
        ResourceUtils.callPaginatedList(this, parameters, new Transform<UserListResponse>() {
            @Override
            protected UserListResponse doTransform(UserListResponse userListResponse, CsSDKError error, Response response) throws CsSDKError {
                if (userListResponse != null) {
                    for (User user : userListResponse.getUsers()) {
                        if (user.getName() == null)
                            user.setName("Mr Noname");
                    }
                }
                return userListResponse;
            }
        }, UserListResponse.class, callback);
    }
}

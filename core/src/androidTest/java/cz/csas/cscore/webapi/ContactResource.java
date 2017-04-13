package cz.csas.cscore.webapi;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.apiquery.UpdateEnabled;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 01/09/16.
 */
public class ContactResource extends InstanceResource implements UpdateEnabled<UpdateContactRequest, UpdateContactResponse> {
    /**
     * Instantiates a new Instance resource.
     *
     * @param id       the id
     * @param basePath the base path
     * @param client   the client
     */
    public ContactResource(Object id, String basePath, WebApiClient client) {
        super(id, basePath, client);
    }

    @Override
    public void update(UpdateContactRequest request, CallbackWebApi<UpdateContactResponse> callback) {
        ResourceUtils.callUpdate(this, request, null, UpdateContactResponse.class, callback);
    }
}

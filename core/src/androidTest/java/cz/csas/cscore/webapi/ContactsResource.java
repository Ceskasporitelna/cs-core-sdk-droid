package cz.csas.cscore.webapi;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.apiquery.CreateEnabled;
import cz.csas.cscore.webapi.apiquery.HasInstanceResource;

/**
 * The type Contacts resource.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 04 /05/16.
 */
public class ContactsResource extends Resource implements HasInstanceResource<ContactResource>, CreateEnabled<CreateContactRequest, Contact> {
    /**
     * Instantiates a new Resource.
     *
     * @param basePath the base path
     * @param client   the client
     */
    public ContactsResource(String basePath, WebApiClient client) {
        super(basePath, client);
    }

    @Override
    public ContactResource withId(Object id) {
        return new ContactResource(id, getBasePath(), getClient());
    }

    @Override
    public void create(CreateContactRequest request, CallbackWebApi<Contact> callback) {
        ResourceUtils.callCreate(this, request, null, Contact.class, callback);
    }

}

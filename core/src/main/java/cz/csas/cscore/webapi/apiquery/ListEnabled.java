package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.ListResponse;

/**
 * Marks Resource or Entity that supports .list verb
 *
 * @param <T> is the contrete type of ListResponse
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public interface ListEnabled<T extends ListResponse> {

    /**
     * Makes a GET call to WebApi and returns a `CallbackWebApi<T>` returned from server.
     *
     * @param callback the callback of type CallbackWebApi<T>
     */
    public void list(CallbackWebApi<T> callback);
}

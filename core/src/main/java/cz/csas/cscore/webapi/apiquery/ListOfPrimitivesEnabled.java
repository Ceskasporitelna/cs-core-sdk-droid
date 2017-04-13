package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.ListOfPrimitivesResponse;

/**
 * Marks Resource or Entity that supports .list verb
 *
 * @param <T> the type parameter
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 30 /03/16.
 */
public interface ListOfPrimitivesEnabled<T extends ListOfPrimitivesResponse> {

    /**
     * Makes a GET call to WebApi and returns a `CallbackWebApi<T>` returned from server.
     *
     * @param callback the callback of type CallbackWebApi<T>
     */
    public void list(CallbackWebApi<T> callback);
}

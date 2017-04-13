package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.WebApiEntity;
import cz.csas.cscore.webapi.WebApiRequest;

/**
 * Marks Resource or Entity that supports .create verb
 *
 * @param <R> is the concrete type of WebApiRequest
 * @param <T> is the contrete type of response based on WebApiEntity
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 26 /12/15.
 */
public interface CreateEnabled<R extends WebApiRequest, T extends WebApiEntity> {

    /**
     * Makes a POST call to WebApi and returns a `CallbackWebApi<T>` returned from server.
     *
     * @param request  the request of type R
     * @param callback the callback of type CallbackWebApi<T>
     */
    public void create(R request, CallbackWebApi<T> callback);
}

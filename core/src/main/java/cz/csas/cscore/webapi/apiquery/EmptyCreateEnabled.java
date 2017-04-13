package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.WebApiEntity;

/**
 * Marks Resource or Entity that supports .create verb without any parameters
 *
 * @param <T> is the contrete type of response based on WebApiEntity
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 26 /12/15.
 */
public interface EmptyCreateEnabled<T extends WebApiEntity> {

    /**
     * Makes a POST call to WebApi and returns a `Callback<T>` returned from server.
     *
     * @param callback the callback of type CallbackWebApi<T>
     */
    public void create(CallbackWebApi<T> callback);
}


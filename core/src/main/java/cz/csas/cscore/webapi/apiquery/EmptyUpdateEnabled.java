package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.WebApiEntity;

/**
 * Marks Resource or Entity that supports .update Verb without any parameters
 *
 * @param <T> is the contrete type of response based on WebApiEntity
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public interface EmptyUpdateEnabled<T extends WebApiEntity> {

    /**
     * Makes a PUT call to WebApi and returns a `CallbackWebApi<T>` returned from server.
     *
     * @param callback the callback of type CallbackWebApi<T>
     */
    public void update(CallbackWebApi<T> callback);
}

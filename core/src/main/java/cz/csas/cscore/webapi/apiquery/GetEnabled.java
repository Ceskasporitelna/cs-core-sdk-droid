package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.WebApiEntity;

/**
 * Marks Resource or Entity that supports .get Verb.
 *
 * @param <T> is the contrete type of response based on WebApiEntity
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public interface GetEnabled<T extends WebApiEntity> {

    /**
     * Makes a GET call to WebApi and returns a `Callback<T>` back.
     *
     * @param callback the callback of type CallbackWebApi<T>
     */
    public void get(CallbackWebApi<T> callback);
}

package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.Parameters;
import cz.csas.cscore.webapi.WebApiEntity;

/**
 * Marks Resource that supports .get verb with parameters.
 *
 * @param <P> is the concrete type of Parameter class
 * @param <T> is the contrete type of response based on WebApiEntity
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 26 /12/15.
 */
public interface ParametrizedGetEnabled<P extends Parameters,T extends WebApiEntity> {

    /**
     * Makes a GET call to WebApi and returns a `CallbackWebApi<T>` returned from server.
     *
     * @param parameters the parameters of concrete type of Parameter class
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public void get(P parameters, CallbackWebApi<T> callback);

}

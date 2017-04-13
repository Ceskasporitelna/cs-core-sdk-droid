package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.Parameters;
import cz.csas.cscore.webapi.WebApiEntity;

/**
 * Marks Resource or Entity that supports .delete verb
 *
 * @param <P> is the concrete type of parameter class
 * @param <T> is the contrete type of response based on WebApiEntity
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public interface ParametrizedDeleteEnabled<P extends Parameters, T extends WebApiEntity> {

    /**
     * Makes a DELETE call to WebApi and returns a `CallbackWebApi<T>` returned from server.
     *
     * @param parameters the parameters of type Parameters
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public void delete(P parameters, CallbackWebApi<T> callback);

}

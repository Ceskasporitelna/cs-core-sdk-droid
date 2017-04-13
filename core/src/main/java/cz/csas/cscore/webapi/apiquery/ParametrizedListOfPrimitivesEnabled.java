package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.ListOfPrimitivesResponse;
import cz.csas.cscore.webapi.Parameters;

/**
 * Marks Resource or Entity that supports .list verb with parameters
 *
 * @param <P> is the concrete type of Parameter class
 * @param <T> is the contrete type of ListResponse
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public interface ParametrizedListOfPrimitivesEnabled<P extends Parameters,T extends ListOfPrimitivesResponse> {

    /**
     * Makes a GET call to WebApi and returns a `CallbackWebApi<T>` returned from server.
     *
     * @param parameters the parameters of concrete type of Parameter class
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public void list(P parameters, CallbackWebApi<T> callback);
}

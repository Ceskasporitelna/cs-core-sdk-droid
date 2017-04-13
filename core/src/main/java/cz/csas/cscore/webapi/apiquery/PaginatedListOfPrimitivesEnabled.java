package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.Paginated;
import cz.csas.cscore.webapi.PaginatedListOfPrimitivesResponse;

/**
 * Marks Resource or Entity that supports .list verb with paginated list of primitives result.
 *
 * @param <P> the type parameter
 * @param <T> the type parameter
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 30 /03/16.
 */
public interface PaginatedListOfPrimitivesEnabled<P extends Paginated, T extends PaginatedListOfPrimitivesResponse> {

    /**
     * Calls WebApi and returns paginated list of primitive items in the callback according to the given pagination parameters.
     * <p/>
     * There is no default limit on how much items get returned. If pagination is `null`, all records are returned in one large list. However, some calls might introduce a size limit due the fact that a certain backend would be overloaded by returning too many items in one call - in such a case, the individual call descriptions will state that clearly.
     *
     * @param parameters the parameters of type Parameter
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public void list(P parameters,CallbackWebApi<T> callback);
}

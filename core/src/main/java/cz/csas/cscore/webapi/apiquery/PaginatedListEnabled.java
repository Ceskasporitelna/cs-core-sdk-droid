package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.Paginated;
import cz.csas.cscore.webapi.PaginatedListResponse;

/**
 * Marks Resource or Entity that supports .list verb with paginated list result.
 *
 * @param <P> is the concrete type of Parameter class
 * @param <T> is the contrete type of PaginatedListResponse
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 26 /12/15.
 */
public interface PaginatedListEnabled<P extends Paginated, T extends PaginatedListResponse> {

    /**
     * Calls WebApi and returns paginated list of items in the callback according to the given pagination parameters.
     *
     * There is no default limit on how much items get returned. If pagination is `null`, all records are returned in one large list. However, some calls might introduce a size limit due the fact that a certain backend would be overloaded by returning too many items in one call - in such a case, the individual call descriptions will state that clearly.
     *
     * @param parameters the parameters of type Parameter
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public void list(P parameters,CallbackWebApi<T> callback);
}

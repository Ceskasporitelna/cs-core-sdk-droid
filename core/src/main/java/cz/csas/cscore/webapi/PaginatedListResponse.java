package cz.csas.cscore.webapi;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Paginated list response.
 *
 * @param <T> is the concrete type of WebApiEntity
 * @param <Q> is the concrete type of PaginatedListResponse (necessary to implement method nextPage, it should be the same type as defined class)
 *
 *           f.e.
 *           public class UserListResponse extends PaginatedListResponse<User,UsersResource,UserListResponse> {}
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /12/15.
 */
public abstract class PaginatedListResponse<T extends WebApiEntity,Q extends PaginatedListResponse> extends ListResponse<T> implements ResponsePagination {

    /**
     * The Page number.
     */
    @CsExpose
    Long pageNumber;

    /**
     * The Page count.
     */
    @CsExpose
    Long pageCount;

    /**
     * The Next page.
     */
    @CsExpose
    Long nextPage;

    /**
     * The Page size.
     */
    @CsExpose
    Long pageSize;

    private PaginatedParameters parameters;

    private Transform<Q> transform;

    private Class<Q> qClass;

    @Override
    public Long getPageNumber() {
        return pageNumber;
    }

    @Override
    public Long getPageSize() {
        return pageSize;
    }

    @Override
    public Long getPageCount() {
        return pageCount;
    }

    @Override
    public boolean hasNextPage() {
        return nextPage != null;
    }

    @Override
    public Long getNextPage() {
        return nextPage;
    }

    protected void setParameters(PaginatedParameters parameters) {
        this.parameters = parameters;
    }

    protected void setTransform(Transform<Q> transform) {
        this.transform = transform;
    }

    protected void setClass(Class<Q> qClass){
        this.qClass = qClass;
    }


    /**
     *
     * Allows you to get next page of PaginatedListResponse.
     *
     * Necessary to check is hasNextPage()!
     *
     * @param callback the callback of type CallbackWebApi<Q>
     */
    public void nextPage(CallbackWebApi<Q> callback){
        parameters.setPagination(new Pagination(getNextPage(),getPageSize()));
        ResourceUtils.callPaginatedList(resource,parameters,transform,qClass,callback);
    }
}

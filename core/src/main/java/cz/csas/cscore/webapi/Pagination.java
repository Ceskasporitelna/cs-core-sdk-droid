package cz.csas.cscore.webapi;

import java.util.Map;

/**
 * The type Pagination.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public class Pagination {

    private final String PAGINATION_SIZE_PARAMETER = "size";
    private final String PAGINATION_PAGE_NUMBER_PARAMETER = "page";
    private long pageNumber;
    private long pageSize;

    /**
     * Instantiates a new Pagination.
     *
     * @param pageNumber the page number
     * @param pageSize   the page size
     */
    public Pagination(long pageNumber, long pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    /**
     * Gets page number.
     *
     * @return the page number
     */
    public long getPageNumber() {
        return pageNumber;
    }

    /**
     * Gets page size.
     *
     * @return the page size
     */
    public long getPageSize() {
        return pageSize;
    }

    /**
     * Add pagination parameters map.
     *
     * Controls the name value pair mapping to query params.
     *
     * @param queryMap the query map
     * @return the map
     */
    public Map<String,String> addPaginationParameters(Map<String, String> queryMap){
        queryMap.put(PAGINATION_SIZE_PARAMETER,String.valueOf(getPageSize()));
        queryMap.put(PAGINATION_PAGE_NUMBER_PARAMETER,String.valueOf(getPageNumber()));
        return queryMap;
    }
}

package cz.csas.cscore.webapi;

/**
 * The interface Response pagination.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public interface ResponsePagination {

    /**
     * Gets page number.
     *
     * @return the page number
     */
    public Long getPageNumber();

    /**
     * Gets page size.
     *
     * @return the page size
     */
    public Long getPageSize();

    /**
     * Gets page count.
     *
     * @return the page count
     */
    public Long getPageCount();

    /**
     * Has next page boolean.
     *
     * @return the boolean
     */
    public boolean hasNextPage();

    /**
     * Gets next page.
     *
     * @return the next page
     */
    public Long getNextPage();
}

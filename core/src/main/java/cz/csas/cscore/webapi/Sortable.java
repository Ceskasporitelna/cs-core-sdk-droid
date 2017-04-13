package cz.csas.cscore.webapi;

/**
 * Class implementing this interface allows for sorting parameter to be set. This is usually used
 * within Parameters class for PaginatedListEnabled endpoints to indicate the WebApi how the
 * returned entities should be sorted.
 *
 * @param <T> the type parameter of enum implementing HasValue.
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 31 /12/15.
 */
public interface Sortable<T extends HasValue> {

    /**
     * Gets sort by.
     *
     * @return the sort by
     */
    public SortParameter<T> getSortBy();

    /**
     * Sets sort by.
     *
     * @param sortBy the sort by
     */
    public void setSortBy(SortParameter<T> sortBy);
}

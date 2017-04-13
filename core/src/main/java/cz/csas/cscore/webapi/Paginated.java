package cz.csas.cscore.webapi;

/**
 * The interface Paginated.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public interface Paginated {

    /**
     * Gets pagination.
     *
     * @return the pagination
     */
    public Pagination getPagination();

    /**
     * Sets pagination.
     *
     * @param pagination the pagination
     */
    public void setPagination(Pagination pagination);
}

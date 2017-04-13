package cz.csas.cscore.webapi;

/**
 * The type Paginated parameters.
 * Every paginated list parameter object should extend this Parameter class.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 17 /03/16.
 */
public class PaginatedParameters extends Parameters implements Paginated {

    private Pagination pagination;

    /**
     * Instantiates a new Paginated parameters.
     *
     * @param pagination the pagination
     */
    public PaginatedParameters(Pagination pagination) {
        this.pagination = pagination;
    }

    @Override
    public Pagination getPagination() {
        return pagination;
    }

    @Override
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}

package cz.csas.cscore.webapi;

/**
 * The enum Sort direction.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 31 /12/15.
 */
public enum SortDirection implements HasValue{

    /**
     * Ascending sort direction.
     */
    ASCENDING("asc"),

    /**
     * Descending sort direction.
     */
    DESCENDING("desc");

    private String value;

    /**
     * Instantiates a new Sort direction.
     *
     * @param value the value
     */
    SortDirection(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}

package cz.csas.cscore.webapi;

/**
 * Used for chaining multiple sorting fields with thenBy method in succession during
 * SortParameter creation
 *
 * @param <T> the type parameter
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 10 /04/16.
 */
public interface ChainedSort<T extends HasValue> extends SortParameter<T> {

    /**
     * thenBy method to create ChainedSort follows by method of Sort object.
     *
     * f.e.
     * Sort.by(Enum enum1,SortDirection direction1).thenBy(Enum enum1, SortDirection direction2);
     *
     * what produces query sort like "sort=enum1value,enum2value&order=direciton1,direction2
     *
     * @param field     the field
     * @param direction the direction
     * @return the chained sort
     */
    public ChainedSort<T> thenBy(T field, SortDirection direction);
}

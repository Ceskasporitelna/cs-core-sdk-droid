package cz.csas.cscore.webapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.csas.cscore.utils.UrlUtils;

/**
 * Objects of this class describes how entities in ListResponse from WebApi should be sorted.
 * It is usually used in Parameters that implement Sortable protocol
 *
 * @param <T> the type of enum implementing HasValue. This enum lists all the available sorting
 *            fields.
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 31 /12/15.
 */
public class Sort<T extends HasValue> implements SortParameter<T>, ChainedSort<T> {

    protected final String SORT_PARAMETER = "sort";
    protected final String SORT_ORDER_PARAMETER = "order";
    private static List<String> fields;
    private static List<SortDirection> directions;

    /**
     * by method to create ChainedSort with thenBy method.
     * <p/>
     * f.e.
     * Sort.by(Enum enum1,SortDirection direction1).thenBy(Enum enum1, SortDirection direction2);
     * <p/>
     * what produces query sort like "sort=enum1value,enum2value&order=direciton1,direction2
     *
     * @param <T>       the type parameter
     * @param field     the field
     * @param direction the direction
     * @return the chained sort
     */
    public static <T extends HasValue> ChainedSort<T> by(T field, SortDirection direction) {
        fields = new ArrayList<>();
        directions = new ArrayList<>();
        addField(field, direction);
        return new Sort<T>();
    }

    @Override
    public ChainedSort<T> thenBy(T field, SortDirection direction) {
        addField(field.getValue(), direction);
        return this;
    }

    @Override
    public Map<String, String> addSortParameters(Map<String, String> queryMap) {
        if (fields != null && fields.size() > 0 && directions != null && directions.size() > 0) {
            queryMap.put(SORT_PARAMETER, UrlUtils.joinStrings(fields));
            queryMap.put(SORT_ORDER_PARAMETER, UrlUtils.joinEnums(directions));
        }
        return queryMap;
    }

    private static <T> void addField(T field, SortDirection direction) {
        if (field != null) {
            if (field instanceof HasValue && ((HasValue) field).getValue() != null) {
                fields.add(((HasValue) field).getValue());
            } else if (field instanceof String) {
                fields.add((String) field);
            } else
                return;
            if (direction != null)
                directions.add(direction);
            else
                directions.add(SortDirection.ASCENDING);
        }
    }


    /**
     * Gets fields.
     *
     * @return the fields
     */
    public List<String> getFields() {
        return fields;
    }

    /**
     * Gets directions.
     *
     * @return the directions
     */
    public List<SortDirection> getDirections() {
        return directions;
    }


}

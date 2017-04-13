package cz.csas.cscore.webapi;

import java.util.Map;

/**
 * This interface wrapping Sort functionality to be dispatch to developer via Parameters classes.
 *
 * @param <T> the type parameter of enum implementing HasValue.
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 10 /04/16.
 */
public interface SortParameter<T extends HasValue> {

    /**
     * Add sort parameters to query Map<String,String>
     *
     * @param queryMap the query map
     * @return the map
     */
    public Map<String, String> addSortParameters(Map<String, String> queryMap);
}

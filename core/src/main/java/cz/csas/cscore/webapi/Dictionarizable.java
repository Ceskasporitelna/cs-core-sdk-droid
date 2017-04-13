package cz.csas.cscore.webapi;

import java.util.Map;

/**
 * The interface Dictionarizable. It makes possible to map query parameters in api calls.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 30 /12/15.
 */
public interface Dictionarizable {

    /**
     * To dictionary map. Creates the name, value pair for query parameter.
     *
     * @return the map of type Map<String,String> (so called dictionary)
     */
    public Map<String, String> toDictionary();
}

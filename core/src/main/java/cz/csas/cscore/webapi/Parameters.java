package cz.csas.cscore.webapi;

import java.util.HashMap;
import java.util.Map;

/**
 * The interface Parameters with its dictionary.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 14 /12/15.
 */
public abstract class Parameters implements Dictionarizable {

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> toDictionary() {
        Map<String,String> dictionary = new HashMap<>();
        if(this instanceof Paginated){
            Pagination pagination = ((Paginated) this).getPagination();
            if(pagination != null)
                dictionary = pagination.addPaginationParameters(dictionary);
        }
        if(this instanceof Sortable){
            SortParameter sort = ((Sortable) this).getSortBy();
            if(sort != null)
                dictionary = sort.addSortParameters(dictionary);
        }
        return dictionary;
    }
}

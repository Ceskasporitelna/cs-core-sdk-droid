package cz.csas.cscore.webapi;

import java.util.List;

/**
 * The type List of primitives response.
 *
 * @param <T> is the concrete type of Primite type
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public abstract class ListOfPrimitivesResponse<T> extends WebApiEntity {

    /**
     * Returns list of T of concrete type of primitive type
     * <p/>
     * Each ListOfPrimitives response should also implement a concrete getItems() method.
     * <p/>
     * f.e.
     * NotesListResponse object should implement
     * <p/>
     * public List<String> getNotes(){};
     *
     * @return the items
     */
    protected abstract List<T> getItems();

}

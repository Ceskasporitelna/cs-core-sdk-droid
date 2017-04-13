package cz.csas.cscore.webapi;

import java.util.List;

/**
 * The type List response.
 *
 * @param <T> is the concrete type of WebApiEntity
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public abstract class ListResponse<T extends WebApiEntity> extends WebApiEntity {

    /**
     * Returns list of T of concrete type of WebApiEntity.
     *
     * Each List response should also implement a concrete getItems() method.
     *
     * f.e.
     * PostListResponse object should implement
     *
     * public List<Post> getPosts(){};
     *
     * @return the items
     */
    protected abstract List<T> getItems();

}

package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.webapi.Resource;

/**
 * Marks Resource or Entity that returns InstanceResource through .withId call
 *
 * @param <R> is the contrete type of Resource
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /12/15.
 */
public interface HasInstanceResource<R extends Resource> {

    /**
     * Get an instance resource with a given id.
     *
     * returns: an InstanceResource that represents resource on URL path `./PathToThisReosurce/id`
     *
     * note: This method does not make any calls to the WebApi by itself. Use  other methods, such as `.get()` on the returned `InstanceResource`
     * to make the call.
     *
     * @param id the id of InstanceResource
     * @return the r is a concrete type of Resource
     */
    public R withId(Object id);

}

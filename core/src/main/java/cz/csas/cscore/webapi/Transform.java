package cz.csas.cscore.webapi;

import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;

/**
 * The type Transform.
 *
 * @param <T> the type parameter
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 31 /12/15.
 */
public abstract class Transform<T> {

    private Response response;

    /**
     * Transform t.
     *
     * @param entity   the entity
     * @param error    the error
     * @param response the response
     * @return the object of type T
     */
    public T transform(T entity, CsSDKError error, Response response){
        this.response = response;
        return doTransform(entity,error,response);
    }

    /**
     * Do transform t.
     * <p/>
     * Define your concrete transformation with T response object here.
     * <p/>
     * To get body as a String call response.getBodyString()
     * To get body as a JsonDictionary (Map) call response.getBodyJsonDictionary()
     * To get body as a Transform defined object T call response.getBodyObject()
     *
     * @param entity   the entity
     * @param error    the error
     * @param response the response
     * @return the object of type T
     * @throws CsSDKError the cs sdk error
     */
    protected abstract T doTransform(T entity, CsSDKError error, Response response) throws CsSDKError;

}

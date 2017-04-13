package cz.csas.cscore.client.rest;

/**
 * The interface Callback basic.
 *
 * @param <T> the type parameter
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /11/15.
 */
public interface CallbackBasic<T> {

    /**
     * Success.
     *
     * @param t the t
     */
    void success(T t);

    /**
     * Failure.
     */
    void failure();

}

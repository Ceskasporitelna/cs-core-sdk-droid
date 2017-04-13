package cz.csas.cscore.client.rest;

/**
 * The interface Callback ui.
 *
 * @param <T> the type parameter
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 04 /12/15.
 */
public interface CallbackUI<T> {

    /**
     * Success.
     *
     * @param t the t
     */
    void success(T t);

    /**
     * Failure.
     *
     * @param error the error
     */
    void failure(CsRestError error);

}

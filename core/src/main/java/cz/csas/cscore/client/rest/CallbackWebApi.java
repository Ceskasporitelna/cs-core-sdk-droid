package cz.csas.cscore.client.rest;

import cz.csas.cscore.error.CsSDKError;

/**
 * The interface Callback web api.
 *
 * @param <T> the type parameter
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 31 /12/15.
 */
public interface CallbackWebApi<T> {

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
    void failure(CsSDKError error);
}

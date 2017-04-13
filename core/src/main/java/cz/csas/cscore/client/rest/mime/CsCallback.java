package cz.csas.cscore.client.rest.mime;

import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;

/**
 * The interface Cs callback.
 *
 * @param <T> the type parameter
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14 /05/16.
 */
public interface CsCallback<T> {
    /**
     * Success.
     *
     * @param t        the t
     * @param response the response
     */
    void success(T t, Response response);

    /**
     * Failure.
     *
     * @param error the error
     */
    void failure(CsSDKError error);
}

package cz.csas.cscore.locker;

import cz.csas.cscore.client.RestService;
import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CsRequest;
import cz.csas.cscore.client.rest.EmptyResponse;
import cz.csas.cscore.client.rest.http.CsBody;
import cz.csas.cscore.client.rest.http.CsDelete;
import cz.csas.cscore.client.rest.http.CsPost;

/**
 * The interface Locker rest service.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 10 /11/15.
 */
interface LockerRestService extends RestService {

    /**
     * Registration.
     *
     * @param request  the request
     * @param callback the callback
     */
    @CsPost("/api/v1/locker")
    public void register(@CsBody CsRequest request, Callback<LockerResponse> callback);

    /**
     * Unregister.
     *
     * @param request  the request
     * @param callback the callback
     */
    @CsDelete("/api/v1/locker")
    public void unregister(@CsBody CsRequest request, Callback<EmptyResponse> callback);

    /**
     * Unlock.
     *
     * @param request  the request
     * @param callback the callback
     */
    @CsPost("/api/v1/locker/unlock")
    public void unlock(@CsBody CsRequest request, Callback<LockerResponse> callback);

    /**
     * Unlock with one time password.
     *
     * @param request  the request
     * @param callback the callback
     */
    @CsPost("/api/v1/locker/unlock")
    public void unlockWithOneTimePassword(@CsBody CsRequest request, Callback<LockerResponse> callback);

    /**
     * Change password.
     *
     * @param request  the request
     * @param callback the callback
     */
    @CsPost("/api/v1/locker/password")
    public void changePassword(@CsBody CsRequest request, Callback<LockerResponse> callback);

}

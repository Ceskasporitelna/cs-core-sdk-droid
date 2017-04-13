package cz.csas.cscore.webapi;

import java.util.Map;

import cz.csas.cscore.client.RestService;
import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.http.CsBody;
import cz.csas.cscore.client.rest.http.CsDelete;
import cz.csas.cscore.client.rest.http.CsGet;
import cz.csas.cscore.client.rest.http.CsPath;
import cz.csas.cscore.client.rest.http.CsPost;
import cz.csas.cscore.client.rest.http.CsPut;
import cz.csas.cscore.client.rest.http.CsQueryMap;
import cz.csas.cscore.client.rest.http.CsStreaming;
import cz.csas.cscore.client.rest.mime.TypedFile;

/**
 * The interface Web api service.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 30 /12/15.
 */
interface WebApiService extends RestService {

    /**
     * Get.
     *
     * @param path     the path
     * @param map      the map
     * @param callback the callback
     */
    @CsGet("/{path}")
    void get(@CsPath(value = "path", encode = false) String path, @CsQueryMap Map<String, String> map, Callback<Object> callback);

    /**
     * Post.
     *
     * @param path          the path
     * @param map           the map
     * @param webApiRequest the web api request
     * @param callback      the callback
     */
    @CsPost("/{path}")
    void post(@CsPath(value = "path", encode = false) String path, @CsQueryMap Map<String, String> map, @CsBody WebApiRequest webApiRequest, Callback<Object> callback);

    /**
     * Put.
     *
     * @param path          the path
     * @param map           the map
     * @param webApiRequest the web api request
     * @param callback      the callback
     */
    @CsPut("/{path}")
    void put(@CsPath(value = "path", encode = false) String path, @CsQueryMap Map<String, String> map, @CsBody WebApiRequest webApiRequest, Callback<Object> callback);

    /**
     * Delete.
     *
     * @param path     the path
     * @param map      the map
     * @param callback the callback
     */
    @CsDelete("/{path}")
    void delete(@CsPath(value = "path", encode = false) String path, @CsQueryMap Map<String, String> map, Callback<Object> callback);

    /**
     * Upload.
     *
     * @param path     the path
     * @param file     the file
     * @param callback the callback
     */
    @CsPost("/{path}")
    void upload(@CsPath(value = "path", encode = false) String path, @CsBody TypedFile file, Callback<Object> callback);

    /**
     * Download post.
     *
     * @param path     the path
     * @param map      the map
     * @param callback the callback
     */
    @CsPost("/{path}")
    @CsStreaming
    void downloadPost(@CsPath(value = "path", encode = false) String path, @CsQueryMap Map<String, String> map, Callback<WebApiStream> callback);

    /**
     * Download get.
     *
     * @param path     the path
     * @param map      the map
     * @param callback the callback
     */
    @CsGet("/{path}")
    @CsStreaming
    void downloadGet(@CsPath(value = "path", encode = false) String path, @CsQueryMap Map<String, String> map, Callback<WebApiStream> callback);

    /**
     * Download with body.
     *
     * @param path          the path
     * @param map           the map
     * @param webApiRequest the web api request
     * @param callback      the callback
     */
    @CsPost("/{path}")
    @CsStreaming
    void downloadWithBody(@CsPath(value = "path", encode = false) String path, @CsQueryMap Map<String, String> map, @CsBody WebApiRequest webApiRequest, Callback<WebApiStream> callback);

}

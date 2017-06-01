package cz.csas.cscore.webapi;

import java.io.File;
import java.util.Map;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.EmptyResponse;
import cz.csas.cscore.client.rest.android.MainThreadExecutor;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.error.CsCoreError;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.utils.csjson.JsonElement;
import cz.csas.cscore.utils.csjson.JsonObject;
import cz.csas.cscore.utils.csjson.JsonParser;
import cz.csas.cscore.webapi.signing.FilledSigningObject;
import cz.csas.cscore.webapi.signing.RequestSigner;
import cz.csas.cscore.webapi.signing.SignInfo;
import cz.csas.cscore.webapi.signing.Signable;

/**
 * The type Resource utils.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 28 /12/15.
 */
public class ResourceUtils {

    private static MainThreadExecutor mainThreadExecutor = new MainThreadExecutor();

    /**
     * Call get.
     *
     * @param <T>        is the concrete type of WebApiEntity
     * @param resource   the resource
     * @param parameters the parameters
     * @param transform  the transform
     * @param tClass     the t class (necessary to json parsing)
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public static <T extends WebApiEntity> void callGet(Resource resource, Parameters parameters, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callGet(resource, null, parameters, transform, tClass, callback);
    }

    /**
     * Call get.
     *
     * @param <T>        is the concrete type of WebApiEntity
     * @param resource   the resource
     * @param pathSuffix the path suffix
     * @param parameters the parameters
     * @param transform  the transform
     * @param tClass     the t class (necessary to json parsing)
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public static <T extends WebApiEntity> void callGet(final Resource resource, final String pathSuffix, Parameters parameters, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        resource.getClient().callApi(CallApiType.NORMAL, resource.appendPathWith(pathSuffix), Method.GET, null, null, parameters, null, new ResourceUtilsCallback<T>(transform, pathSuffix, resource, callback, tClass), null);
    }

    /**
     * Call create.
     *
     * @param <T>           the type parameter
     * @param resource      the resource
     * @param webApiRequest the web api request
     * @param transform     the transform
     * @param tClass        the t class
     * @param callback      the callback
     */
    public static <T extends WebApiEntity> void callCreate(Resource resource, WebApiRequest webApiRequest, Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callCreate(resource, null, null, webApiRequest, transform, tClass, callback);
    }

    /**
     * Call create.
     *
     * @param <T>           the type parameter
     * @param resource      the resource
     * @param pathSuffix    the path suffix
     * @param webApiRequest the web api request
     * @param transform     the transform
     * @param tClass        the t class
     * @param callback      the callback
     */
    public static <T extends WebApiEntity> void callCreate(Resource resource, String pathSuffix, WebApiRequest webApiRequest, Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callCreate(resource, pathSuffix, null, webApiRequest, transform, tClass, callback);
    }

    /**
     * Call create.
     *
     * @param <T>           is the concrete type of WebApiEntity
     * @param resource      the resource
     * @param pathSuffix    the path suffix
     * @param parameters    the parameters
     * @param webApiRequest the web api request
     * @param transform     the transform
     * @param tClass        the t class (necessary to json parsing)
     * @param callback      the callback of type CallbackWebApi<T>
     */
    public static <T extends WebApiEntity> void callCreate(Resource resource, String pathSuffix, Parameters parameters, WebApiRequest webApiRequest, Transform<T> transform, Class<T> tClass, CallbackWebApi<T> callback) {
        resource.getClient().callApi(CallApiType.NORMAL, resource.appendPathWith(pathSuffix), Method.POST, webApiRequest, null, parameters, null, new ResourceUtilsCallback<T>(transform, pathSuffix, resource, callback, tClass), null);
    }

    /**
     * Call delete.
     *
     * @param <T>        is the concrete type of WebApiEntity
     * @param resource   the resource
     * @param parameters the parameters
     * @param transform  the transform
     * @param tClass     the t class (necessary to json parsing)
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public static <T extends WebApiEntity> void callDelete(Resource resource, Parameters parameters, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callDelete(resource, null, parameters, transform, tClass, callback);
    }

    /**
     * Call delete.
     *
     * @param <T>        is the concrete type of WebApiEntity
     * @param resource   the resource
     * @param pathSuffix the path suffix
     * @param parameters the parameters
     * @param transform  the transform
     * @param tClass     the t class (necessary to json parsing)
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public static <T extends WebApiEntity> void callDelete(final Resource resource, final String pathSuffix, Parameters parameters, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        resource.getClient().callApi(CallApiType.NORMAL, resource.appendPathWith(pathSuffix), Method.DELETE, null, null, parameters, null, new ResourceUtilsCallback<T>(transform, pathSuffix, resource, callback, tClass), null);
    }

    /**
     * Call update.
     *
     * @param <T>           is the concrete type of WebApiEntity
     * @param resource      the resource
     * @param webApiRequest the web api request
     * @param transform     the transform
     * @param tClass        the t class (necessary to json parsing)
     * @param callback      the callback of type CallbackWebApi<T>
     */
    public static <T extends WebApiEntity> void callUpdate(final Resource resource, WebApiRequest webApiRequest, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callUpdate(resource, null, null, webApiRequest, transform, tClass, callback);
    }

    /**
     * Call update.
     *
     * @param <T>           is the concrete type of WebApiEntity
     * @param resource      the resource
     * @param pathSuffix    the path suffix
     * @param webApiRequest the web api request
     * @param transform     the transform
     * @param tClass        the t class (necessary to json parsing)
     * @param callback      the callback of type CallbackWebApi<T>
     */
    public static <T extends WebApiEntity> void callUpdate(final Resource resource, final String pathSuffix, WebApiRequest webApiRequest, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callUpdate(resource, pathSuffix, null, webApiRequest, transform, tClass, callback);
    }

    /**
     * Call update with parameters.
     *
     * @param <T>           is the concrete type of WebApiEntity
     * @param resource      the resource
     * @param pathSuffix    the path suffix
     * @param parameters    the parameters
     * @param webApiRequest the web api request
     * @param transform     the transform
     * @param tClass        the t class (necessary to json parsing)
     * @param callback      the callback of type CallbackWebApi<T>
     */
    public static <T extends WebApiEntity> void callUpdate(Resource resource, String pathSuffix, Parameters parameters, WebApiRequest webApiRequest, Transform<T> transform, Class<T> tClass, CallbackWebApi<T> callback) {
        resource.getClient().callApi(CallApiType.NORMAL, resource.appendPathWith(pathSuffix), Method.PUT, webApiRequest, null, parameters, null, new ResourceUtilsCallback<T>(transform, pathSuffix, resource, callback, tClass), null);
    }


    /**
     * Call list.
     *
     * @param <T>       is the concrete type of ListResponse
     * @param resource  the resource
     * @param transform the transform
     * @param tClass    the t class (necessary to json parsing)
     * @param callback  the callback of type CallbackWebApi<T>
     */
    public static <T extends ListResponse> void callList(final Resource resource, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callList(resource, null, transform, tClass, callback);
    }

    /**
     * Call list.
     *
     * @param <T>        is the concrete type of ListResponse
     * @param resource   the resource
     * @param pathSuffix the path suffix
     * @param transform  the transform
     * @param tClass     the t class (necessary to json parsing)
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public static <T extends ListResponse> void callList(final Resource resource, final String pathSuffix, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        resource.getClient().callApi(CallApiType.NORMAL, resource.appendPathWith(pathSuffix), Method.GET, null, null, null, null, new ResourceUtilsCallback<T>(transform, pathSuffix, resource, callback, tClass), null);
    }

    /**
     * Call list of primitives.
     *
     * @param <T>       the type parameter
     * @param resource  the resource
     * @param transform the transform
     * @param tClass    the t class
     * @param callback  the callback
     */
    public static <T extends ListOfPrimitivesResponse> void callListOfPrimitives(final Resource resource, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callListOfPrimitives(resource, null, transform, tClass, callback);
    }

    /**
     * Call list of primitives.
     *
     * @param <T>        the type parameter
     * @param resource   the resource
     * @param pathSuffix the path suffix
     * @param transform  the transform
     * @param tClass     the t class
     * @param callback   the callback
     */
    public static <T extends ListOfPrimitivesResponse> void callListOfPrimitives(final Resource resource, final String pathSuffix, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        resource.getClient().callApi(CallApiType.NORMAL, resource.appendPathWith(pathSuffix), Method.GET, null, null, null, null, new ResourceUtilsCallback<T>(transform, pathSuffix, resource, callback, tClass), null);
    }

    /**
     * Call parametrized list.
     *
     * @param <T>        the type parameter
     * @param resource   the resource
     * @param parameters the parameters
     * @param transform  the transform
     * @param tClass     the t class
     * @param callback   the callback
     */
    public static <T extends ListResponse> void callParametrizedList(final Resource resource, Parameters parameters, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callParametrizedList(resource, null, parameters, transform, tClass, callback);
    }

    /**
     * Call parametrized list.
     *
     * @param <T>        the type parameter
     * @param resource   the resource
     * @param pathSuffix the path suffix
     * @param parameters the parameters
     * @param transform  the transform
     * @param tClass     the t class
     * @param callback   the callback
     */
    public static <T extends ListResponse> void callParametrizedList(final Resource resource, final String pathSuffix, Parameters parameters, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        resource.getClient().callApi(CallApiType.NORMAL, resource.appendPathWith(pathSuffix), Method.GET, null, null, parameters, null, new ResourceUtilsCallback<T>(transform, pathSuffix, resource, callback, tClass), null);
    }

    /**
     * Call parametrized list of primitives.
     *
     * @param <T>        the type parameter
     * @param resource   the resource
     * @param parameters the parameters
     * @param transform  the transform
     * @param tClass     the t class
     * @param callback   the callback
     */
    public static <T extends ListOfPrimitivesResponse> void callParametrizedListOfPrimitives(final Resource resource, Parameters parameters, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callParametrizedListOfPrimitives(resource, null, parameters, transform, tClass, callback);
    }

    /**
     * Call parametrized list of primitives.
     *
     * @param <T>        the type parameter
     * @param resource   the resource
     * @param pathSuffix the path suffix
     * @param parameters the parameters
     * @param transform  the transform
     * @param tClass     the t class
     * @param callback   the callback
     */
    public static <T extends ListOfPrimitivesResponse> void callParametrizedListOfPrimitives(final Resource resource, final String pathSuffix, Parameters parameters, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        resource.getClient().callApi(CallApiType.NORMAL, resource.appendPathWith(pathSuffix), Method.GET, null, null, parameters, null, new ResourceUtilsCallback<T>(transform, pathSuffix, resource, callback, tClass), null);
    }

    /**
     * Call paginated list.
     *
     * @param <T>        is the concrete type of PaginatedListResponse
     * @param resource   the resource
     * @param parameters the parameters
     * @param transform  the transform
     * @param tClass     the t class (necessary to json parsing)
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public static <T extends PaginatedListResponse> void callPaginatedList(final Resource resource, PaginatedParameters parameters, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callPaginatedList(resource, null, parameters, transform, tClass, callback);
    }

    /**
     * Call paginated list.
     *
     * @param <T>        is the concrete type of PaginatedListResponse
     * @param resource   the resource
     * @param pathSuffix the path suffix
     * @param parameters the parameters
     * @param transform  the transform
     * @param tClass     the t class (necessary to json parsing)
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public static <T extends PaginatedListResponse> void callPaginatedList(final Resource resource, final String pathSuffix, PaginatedParameters parameters, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        resource.getClient().callApi(CallApiType.NORMAL, resource.appendPathWith(pathSuffix), Method.GET, null, null, parameters, null, new ResourceUtilsCallback<T>(transform, pathSuffix, resource, callback, tClass, parameters), null);
    }

    /**
     * Call paginated list of primitives.
     *
     * @param <T>        the type parameter
     * @param resource   the resource
     * @param parameters the parameters
     * @param transform  the transform
     * @param tClass     the t class
     * @param callback   the callback
     */
    public static <T extends PaginatedListOfPrimitivesResponse> void callPaginatedListOfPrimitives(final Resource resource, PaginatedParameters parameters, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callPaginatedListOfPrimitives(resource, null, parameters, transform, tClass, callback);
    }

    /**
     * Call paginated list of primitives.
     *
     * @param <T>        the type parameter
     * @param resource   the resource
     * @param pathSuffix the path suffix
     * @param parameters the parameters
     * @param transform  the transform
     * @param tClass     the t class
     * @param callback   the callback
     */
    public static <T extends PaginatedListOfPrimitivesResponse> void callPaginatedListOfPrimitives(final Resource resource, final String pathSuffix, PaginatedParameters parameters, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        resource.getClient().callApi(CallApiType.NORMAL, resource.appendPathWith(pathSuffix), Method.GET, null, null, parameters, null, new ResourceUtilsCallback<T>(transform, pathSuffix, resource, callback, tClass, parameters), null);
    }

    /**
     * Call upload.
     *
     * @param <T>       is the concrete type of WebApiEntity
     * @param resource  the resource
     * @param file      the file
     * @param headers   the Map<String, String> of headers
     * @param transform the transform
     * @param tClass    the t class (necessary to json parsing)
     * @param callback  the callback of type CallbackWebApi<T>
     */
    public static <T extends WebApiEntity> void callUpload(final Resource resource, File file, Map<String, String> headers, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        callUpload(resource, null, file, headers, transform, tClass, callback);
    }

    /**
     * Call upload.
     *
     * @param <T>        is the concrete type of WebApiEntity
     * @param resource   the resource
     * @param pathSuffix the path suffix
     * @param file       the file
     * @param headers    the Map<String, String> of headers
     * @param transform  the transform
     * @param tClass     the t class (necessary to json parsing)
     * @param callback   the callback of type CallbackWebApi<T>
     */
    public static <T extends WebApiEntity> void callUpload(final Resource resource, final String pathSuffix, File file, Map<String, String> headers, final Transform<T> transform, final Class<T> tClass, final CallbackWebApi<T> callback) {
        resource.getClient().callApi(CallApiType.UPLOAD, resource.appendPathWith(pathSuffix), null, null, file, null, headers, new ResourceUtilsCallback<T>(transform, pathSuffix, resource, callback, tClass), null);
    }

    /**
     * Call download.
     *
     * @param method        the method
     * @param resource      the resource
     * @param webApiRequest the web api request
     * @param headers       the headers
     * @param transform     the transform
     * @param callback      the callback
     */
    public static void callDownload(Method method, Resource resource, WebApiRequest webApiRequest, Map<String, String> headers, Transform<WebApiStream> transform, CallbackWebApi<WebApiStream> callback) {
        callDownload(method, resource, null, null, webApiRequest, headers, transform, callback);
    }

    /**
     * Call download.
     *
     * @param method        the method
     * @param resource      the resource
     * @param pathSuffix    the path suffix
     * @param webApiRequest the web api request
     * @param headers       the headers
     * @param transform     the transform
     * @param callback      the callback
     */
    public static void callDownload(Method method, Resource resource, final String pathSuffix, WebApiRequest webApiRequest, Map<String, String> headers, Transform<WebApiStream> transform, final CallbackWebApi<WebApiStream> callback) {
        callDownload(method, resource, pathSuffix, null, webApiRequest, headers, transform, callback);
    }

    /**
     * Call download.
     *
     * @param method        the method
     * @param resource      the resource
     * @param pathSuffix    the path suffix
     * @param parameters    the parameters
     * @param webApiRequest the web api request
     * @param headers       the headers
     * @param transform     the transform
     * @param callback      the callback
     */
    public static void callDownload(Method method, Resource resource, String pathSuffix, Parameters parameters, WebApiRequest webApiRequest, Map<String, String> headers, Transform<WebApiStream> transform, CallbackWebApi<WebApiStream> callback) {
        final ResourceUtilsCallback<WebApiStream> resourceUtilsCallback = new ResourceUtilsCallback<WebApiStream>(transform, pathSuffix, resource, callback, WebApiStream.class);
        resource.getClient().callApi(CallApiType.DOWNLOAD, resource.appendPathWith(pathSuffix), method, webApiRequest, null, parameters, headers, null, new CsCallback<WebApiStream>() {
            @Override
            public void success(WebApiStream webApiStream, Response response) {
                resourceUtilsCallback.success(webApiStream, response);
            }

            @Override
            public void failure(CsSDKError error) {
                resourceUtilsCallback.failure(error);
            }
        });
    }

    private static <T extends WebApiEntity> T setReturnEntityTransformation(T object, String pathSuffix, Resource resource) throws CsSDKError {
        if (object != null && resource != null) {
            object.setResource(resource);
            object.setPathSuffix(pathSuffix);
            if (object instanceof ListResponse && ((ListResponse) object).getItems() != null) {
                for (Object o : ((ListResponse) object).getItems()) {
                    ((WebApiEntity) o).setResource(resource);
                    ((WebApiEntity) o).setPathSuffix(pathSuffix);
                }
            }
            return object;
        }
        throw new CsCoreError(CsCoreError.Kind.TRANSFORM);
    }

    private static <T extends WebApiEntity> T setPaginatedListAttributes(T object, PaginatedParameters parameters, Transform<T> transform, Class<T> tClass) {
        if (object instanceof PaginatedListResponse) {
            ((PaginatedListResponse) object).setParameters(parameters);
            ((PaginatedListResponse) object).setTransform(transform);
            ((PaginatedListResponse) object).setClass(tClass);
        }
        if (object instanceof PaginatedListOfPrimitivesResponse) {
            ((PaginatedListOfPrimitivesResponse) object).setParameters(parameters);
            ((PaginatedListOfPrimitivesResponse) object).setTransform(transform);
            ((PaginatedListOfPrimitivesResponse) object).setClass(tClass);
        }
        return object;
    }

    private static <T extends WebApiEntity> T setSigningInfo(T object, String json) {
        if (object instanceof Signable) {
            RequestSigner requestSigner = new RequestSigner(object);
            SignInfo signInfo = SignableResponseParser.parseSignInfoFromJson(json);
            if (signInfo == null)
                return object;
            ((Signable) object).setSigningObject(new FilledSigningObject(signInfo.getState(), signInfo.getSignId(), requestSigner, null, null));

        } else if (object instanceof ListResponse &&
                ((ListResponse) object).getItems() != null &&
                ((ListResponse) object).getItems().size() != 0) {
            JsonElement element = new JsonParser().parse(json);
            JsonObject jsonObject = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                if (jsonObject.get(entry.getKey()).isJsonArray()) {
                    for (int i = 0; i < ((ListResponse) object).getItems().size(); i++) {
                        setSigningInfo((WebApiEntity) ((ListResponse) object).getItems().get(i),
                                jsonObject.get(entry.getKey()).getAsJsonArray().get(i).toString());
                    }
                    break;
                }
            }
        }
        return object;
    }

    private static void executeSuccessOnMainThread(final CallbackWebApi callback, final WebApiEntity result) {
        mainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                callback.success(result);
            }
        });
    }

    private static void executeFailureOnMainThread(final CallbackWebApi callback, final CsSDKError error) {
        mainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                callback.failure(error);
            }
        });
    }

    private static class ResourceUtilsCallback<T extends WebApiEntity> implements CsCallback<Object> {

        private Transform<T> transform;
        private String pathSuffix;
        private Resource resource;
        private CallbackWebApi<T> callback;
        private Class<T> tClass;
        private PaginatedParameters parameters;

        /**
         * Instantiates a new Resource callback.
         *
         * @param transform  the transform
         * @param pathSuffix the path suffix
         * @param resource   the resource
         * @param callback   the callback
         * @param tClass     the t class
         */
        public ResourceUtilsCallback(Transform<T> transform, String pathSuffix, Resource resource, CallbackWebApi<T> callback, Class<T> tClass) {
            this.transform = transform;
            this.pathSuffix = pathSuffix;
            this.resource = resource;
            this.callback = callback;
            this.tClass = tClass;
        }

        /**
         * Instantiates a new Resource utils callback.
         *
         * @param transform  the transform
         * @param pathSuffix the path suffix
         * @param resource   the resource
         * @param callback   the callback
         * @param tClass     the t class
         * @param parameters the parameters
         */
        public ResourceUtilsCallback(Transform<T> transform, String pathSuffix, Resource resource, CallbackWebApi<T> callback, Class<T> tClass, PaginatedParameters parameters) {
            this.transform = transform;
            this.pathSuffix = pathSuffix;
            this.resource = resource;
            this.callback = callback;
            this.tClass = tClass;
            this.parameters = parameters;
        }

        @Override
        public void success(Object object, Response response) {
            try {
                T result;
                if (object instanceof WebApiStream)
                    result = (T) object;
                /**
                 * Check whether the response is of type Void, then cast result to WebApiVoid to be
                 * able to map resource and other attributes. 
                 */
                else if (object instanceof EmptyResponse)
                    result = (T) new WebApiEmptyResponse();
                else
                    result = response.getBodyObject(tClass);
                if (transform != null)
                    result = transform.transform(result, null, response);
                result = setReturnEntityTransformation(result, pathSuffix, resource);
                result = setPaginatedListAttributes(result, parameters, transform, tClass);
                result = setSigningInfo(result, response.getBodyString());
                executeSuccessOnMainThread(callback, result);
            } catch (CsSDKError error) {
                executeFailureOnMainThread(callback, error);
            }
        }

        @Override
        public void failure(CsSDKError error) {
            try {
                if (transform != null && error instanceof CsRestError &&
                        ((CsRestError) error).getResponse() != null &&
                        ((CsRestError) error).getResponse().getStatus() != 403) {
                    T result = transform.transform(null, error, ((CsRestError) error).getResponse());
                    result = setReturnEntityTransformation(result, pathSuffix, resource);
                    result = setPaginatedListAttributes(result, parameters, transform, tClass);
                    result = setSigningInfo(result, ((CsRestError) error).getResponse().getBodyString());
                    executeSuccessOnMainThread(callback, result);
                } else
                    executeFailureOnMainThread(callback, error);
            } catch (CsSDKError csSDKError) {
                executeFailureOnMainThread(callback, csSDKError);
            }
        }
    }

}

package cz.csas.cscore.webapi;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.client.RestClient;
import cz.csas.cscore.client.RestService;
import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.CsRestAdapter;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.RequestInterceptor;
import cz.csas.cscore.client.rest.RequestSignerImpl;
import cz.csas.cscore.client.rest.android.BackgroundThreadExecutor;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.converter.GsonConverter;
import cz.csas.cscore.client.rest.mime.CsCallback;
import cz.csas.cscore.client.rest.mime.TypedFile;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.locker.AccessToken;
import cz.csas.cscore.utils.csjson.CsJson;
import cz.csas.cscore.utils.csjson.CsJsonBuilder;

/**
 * The type Web api client base.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 14 /12/15.
 */
public abstract class WebApiClient implements RestClient {

    private boolean refreshTokenFlag = true;
    private WebApiService mWebApiRestService;
    private CsRestAdapter mWebApiAdapter;
    private Map<String, String> mGlobalHeaders;
    private Map<String, String> mLocalHeaders;
    private Map<String, String> mHeaders;

    /**
     * The M access token provider.
     */
    protected AccessTokenProvider mAccessTokenProvider;

    /**
     * Instantiates a new Web api client base.
     *
     * @param webApiConfiguration the cs configuration
     */
    public WebApiClient(final WebApiConfiguration webApiConfiguration) {

        CsJson csJson = new CsJsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        mWebApiAdapter = new CsRestAdapter.Builder()
                .setLog(CoreSDK.getInstance().getLogger())
                .setRequestSigner(new RequestSignerImpl(webApiConfiguration))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader(WEB_API_KEY_HEADER_NAME, webApiConfiguration.getWebApiKey());
                        request.addHeader(ACCEPT_HEADER_NAME, ACCEPT_HEADER_VALUE);
                        request.addHeader(ACCEPT_LANGUAGE_HEADER_NAME, webApiConfiguration.getLanguage());
                        request.addHeader(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE_HEADER_VALUE_JSON);

                        if (mGlobalHeaders != null) {
                            for (Map.Entry<String, String> entry : mGlobalHeaders.entrySet()) {
                                request.addHeader(entry.getKey(), entry.getValue());
                            }
                        }
                        if (mLocalHeaders != null) {
                            for (Map.Entry<String, String> entry : mLocalHeaders.entrySet()) {
                                request.addHeader(entry.getKey(), entry.getValue());
                            }
                        }

                        if (webApiConfiguration.getPrivateSigningKey() != null)
                            request.signRequest();
                    }
                })
                .setExecutors(new BackgroundThreadExecutor(), new BackgroundThreadExecutor())
                .setEndpoint(webApiConfiguration.getEnvironment().getApiContextBaseUrl() + getClientPath())
                .setConverter(new GsonConverter(csJson))
                .build();

        mWebApiRestService = mWebApiAdapter.create(WebApiService.class);
    }


    /**
     * Get client path.
     * <p/>
     * note: has to start with "/"
     *
     * @return the client path
     */
    protected abstract String getClientPath();

    @Override
    public RestService getService() {
        return mWebApiRestService;
    }

    @Override
    public CsRestAdapter getRestAdapter() {
        return mWebApiAdapter;
    }

    @Override
    public void setGlobalHeaders(Map<String, String> headers) {
        mGlobalHeaders = headers;
    }

    /**
     * Sets access token provider.
     *
     * @param accessTokenProvider the access token provider
     */
    public void setAccessTokenProvider(AccessTokenProvider accessTokenProvider) {
        mAccessTokenProvider = accessTokenProvider;
    }

    /**
     * This method provides all api calls to be executed by the webApiClient.
     *
     * @param type           of the call which should be called (NORMAL, UPLOAD, DOWNLOAD)
     * @param path           the path
     * @param method         the method
     * @param webApiRequest  the web api request
     * @param file           the file
     * @param parameters     the parameters
     * @param headers        the headers
     * @param callback       the callback
     * @param callbackStream the callback stream
     */
    public void callApi(final CallApiType type, final String path, final Method method,
                            final WebApiRequest webApiRequest, final File file,
                            final Parameters parameters, final Map<String, String> headers,
                            final CsCallback<Object> callback,
                            final CsCallback<WebApiStream> callbackStream) {
        if (mAccessTokenProvider != null) {
            mHeaders = headers != null ? headers : new HashMap<String, String>();
            mAccessTokenProvider.getAccessToken(new CallbackWebApi<AccessToken>() {
                @Override
                public void success(AccessToken accessToken) {
                    mHeaders.put(AUTHORIZATION_HEADER_NAME, "bearer " + accessToken.getAccessToken());
                    callApiType(type, path, method, webApiRequest, file, parameters, mHeaders,
                            callback, callbackStream);
                }

                @Override
                public void failure(CsSDKError error) {
                    callApiFailure(type, callback, callbackStream, error);
                }
            });
        } else
            callApiType(type, path, method, webApiRequest, file, parameters, headers, callback,
                    callbackStream);
    }

    private void callApiType(CallApiType type, String path, Method method,
                                 WebApiRequest webApiRequest, File file, Parameters parameters,
                                 Map<String, String> headers, CsCallback<Object> callback,
                                 CsCallback<WebApiStream> callbackStream) {
        switch (type) {
            case NORMAL:
                callApi(path, method, webApiRequest, parameters, headers, callback);
                break;
            case UPLOAD:
                callApi(path, file, headers, callback);
                break;
            case DOWNLOAD:
                callApiDownload(path, method, webApiRequest, parameters, headers, callbackStream);
                break;
        }
    }

    private void callApi(final String path, final Method method, final WebApiRequest webApiRequest,
                             final Parameters parameters, final Map<String, String> headers,
                             final CsCallback<Object> callback) {
        mLocalHeaders = headers;
        Map<String, String> queryMap = null;
        if (parameters != null)
            queryMap = parameters.toDictionary();
        CallbackWrapper<Object> callbackWrapper = new CallbackWrapper<Object>(path, method, webApiRequest, parameters, headers, callback);
        switch (method) {
            case GET:
                mWebApiRestService.get(path, queryMap, callbackWrapper);
                break;
            case POST:
                mWebApiRestService.post(path, queryMap, webApiRequest, callbackWrapper);
                break;
            case PUT:
                mWebApiRestService.put(path, queryMap, webApiRequest, callbackWrapper);
                break;
            case DELETE:
                mWebApiRestService.delete(path, queryMap, callbackWrapper);
                break;
        }
    }

    private void callApi(final String path, final File file, final Map<String, String> headers,
                             final CsCallback<Object> callback) {
        mLocalHeaders = headers;
        mWebApiRestService.upload(path, new TypedFile(CONTENT_TYPE_HEADER_VALUE_OCTET_STREAM, file),
                new CallbackWrapper<Object>(path, file, headers, callback));
    }

    private void callApiDownload(final String path, Method method, final WebApiRequest webApiRequest,
                                 final Parameters parameters, final Map<String, String> headers,
                                 final CsCallback<WebApiStream> callback) {
        mLocalHeaders = headers;
        Map<String, String> queryMap = null;
        if (parameters != null)
            queryMap = parameters.toDictionary();
        CallbackWrapper<WebApiStream> callbackWrapper = new CallbackWrapper<>(path, webApiRequest,
                parameters, headers, callback);
        switch (method) {
            case GET:
                mWebApiRestService.downloadGet(path, queryMap, callbackWrapper);
                break;
            case POST:
                if (webApiRequest != null)
                    mWebApiRestService.downloadWithBody(path, queryMap, webApiRequest, callbackWrapper);
                else
                    mWebApiRestService.downloadPost(path, queryMap, callbackWrapper);
                break;
        }
    }

    private void callApiFailure(CallApiType type, CsCallback callback,
                                CsCallback<WebApiStream> callbackStream, CsSDKError error) {
        if (type == CallApiType.DOWNLOAD)
            callbackStream.failure(error);
        else
            callback.failure(error);
    }

    /**
     * CallbackWrapper provides the access token provider logic and api calls repeating.
     *
     * @param <T> return field for Callback
     */
    private class CallbackWrapper<T> implements Callback<T> {

        private CallApiType type;
        private String path;
        private Method method;
        private WebApiRequest webApiRequest;
        private File file;
        private Parameters parameters;
        private Map<String, String> headers;
        private CsCallback<Object> callback;
        private CsCallback<WebApiStream> callbackStream;


        /**
         * Instantiates a new Callback wrapper.
         *
         * @param path          the path
         * @param method        the method
         * @param webApiRequest the web api request
         * @param parameters    the parameters
         * @param headers       the headers
         * @param callback      the callback
         */
        public CallbackWrapper(String path, Method method, WebApiRequest webApiRequest,
                                   Parameters parameters, Map<String, String> headers,
                                   CsCallback<Object> callback) {
            this.type = CallApiType.NORMAL;
            this.path = path;
            this.method = method;
            this.webApiRequest = webApiRequest;
            this.parameters = parameters;
            this.headers = headers;
            this.callback = callback;
        }

        /**
         * Instantiates a new Callback wrapper.
         *
         * @param path     the path
         * @param file     the file
         * @param headers  the headers
         * @param callback the callback
         */
        public CallbackWrapper(String path, File file, Map<String, String> headers,
                                   CsCallback<Object> callback) {
            this.type = CallApiType.UPLOAD;
            this.path = path;
            this.file = file;
            this.headers = headers;
            this.callback = callback;
        }

        /**
         * Instantiates a new Callback wrapper.
         *
         * @param path           the path
         * @param webApiRequest  the web api request
         * @param parameters     the parameters
         * @param headers        the headers
         * @param callbackStream the callback stream
         */
        public CallbackWrapper(String path, WebApiRequest webApiRequest, Parameters parameters,
                               Map<String, String> headers,
                               CsCallback<WebApiStream> callbackStream) {
            this.type = CallApiType.DOWNLOAD;
            this.path = path;
            this.parameters = parameters;
            this.headers = headers;
            this.webApiRequest = webApiRequest;
            this.callbackStream = callbackStream;
        }

        @Override
        public void success(T t, Response response) {
            refreshTokenFlag = true;
            if (type == CallApiType.DOWNLOAD)
                callbackStream.success((WebApiStream) t, response);
            else
                callback.success(t, response);
        }

        @Override
        public void failure(CsRestError error) {
            if (mAccessTokenProvider != null && refreshTokenFlag && error.getResponse() != null && error.getResponse().getStatus() == 403) {
                refreshTokenFlag = false;
                mAccessTokenProvider.refreshAccessToken(new CallbackWebApi<AccessToken>() {
                    @Override
                    public void success(AccessToken accessToken) {
                        callApi(type, path, method, webApiRequest, file, parameters, headers,
                                callback, callbackStream);
                    }

                    @Override
                    public void failure(CsSDKError error) {
                        refreshTokenFlag = true;
                        callback.failure(error);
                    }
                });
            } else {
                refreshTokenFlag = true;
                callApiFailure(type, callback, callbackStream, error);
            }
        }
    }
}

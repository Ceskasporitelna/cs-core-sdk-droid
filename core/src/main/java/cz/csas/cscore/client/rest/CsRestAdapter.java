/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.csas.cscore.client.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.rest.client.Client;
import cz.csas.cscore.client.rest.client.Header;
import cz.csas.cscore.client.rest.client.Request;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.converter.ConversionException;
import cz.csas.cscore.client.rest.converter.Converter;
import cz.csas.cscore.client.rest.http.CsBody;
import cz.csas.cscore.client.rest.http.CsDelete;
import cz.csas.cscore.client.rest.http.CsField;
import cz.csas.cscore.client.rest.http.CsFormUrlEncoded;
import cz.csas.cscore.client.rest.http.CsGet;
import cz.csas.cscore.client.rest.http.CsHead;
import cz.csas.cscore.client.rest.http.CsHeader;
import cz.csas.cscore.client.rest.http.CsHeaders;
import cz.csas.cscore.client.rest.http.CsMultipart;
import cz.csas.cscore.client.rest.http.CsPart;
import cz.csas.cscore.client.rest.http.CsPath;
import cz.csas.cscore.client.rest.http.CsPost;
import cz.csas.cscore.client.rest.http.CsPut;
import cz.csas.cscore.client.rest.http.CsQuery;
import cz.csas.cscore.client.rest.http.CsRestMethod;
import cz.csas.cscore.client.rest.mime.MimeUtil;
import cz.csas.cscore.client.rest.mime.TypedByteArray;
import cz.csas.cscore.client.rest.mime.TypedInput;
import cz.csas.cscore.client.rest.mime.TypedOutput;
import cz.csas.cscore.logger.LogLevel;
import cz.csas.cscore.logger.LogManager;
import cz.csas.cscore.utils.StringUtils;
import cz.csas.cscore.webapi.WebApiStream;

/**
 * Adapts a Java interface to a REST API.
 * <p>
 * API endpoints are defined as methods on an interface with annotations providing metadata about
 * the form in which the HTTP call should be made.
 * <p>
 * The relative path for a given method is obtained from an annotation on the method describing
 * the request type. The built-in methods are {@link CsGet GET},
 * {@link CsPut PUT}, {@link CsPost POST}, {@link CsHead HEAD},
 * and {@link CsDelete DELETE}. You can define your own HTTP method by creating an
 * annotation that takes a {code String} value and itself is annotated with
 * {@link CsRestMethod @RestMethod}.
 * <p>
 * Method parameters can be used to replace parts of the URL by annotating them with
 * {@link CsPath @Path}. Replacement sections are denoted by an identifier surrounded
 * by curly braces (e.g., "{foo}"). To add items to the query string of a URL use
 * {@link CsQuery @Query}.
 * <p>
 * HTTP requests happen in one of two ways:
 * <ul>
 * <li>On the provided HTTP {@link Executor} with callbacks marshaled to the callback
 * {@link Executor}. The last method parameter should be of type {@link Callback}. The HTTP
 * response will be converted to the callback's parameter type using the specified
 * {@link cz.csas.cscore.client.rest.converter.Converter Converter}. If the callback parameter type uses a wildcard,
 * the lower bound will be used as the conversion type.
 * <li>On the current thread returning the response or throwing a {@link CsRestError}. The HTTP
 * response will be converted to the method's return type using the specified
 * {@link cz.csas.cscore.client.rest.converter.Converter Converter}.
 * </ul>
 * <p>
 * The body of a request is denoted by the {@link CsBody @Body} annotation. The object
 * will be converted to request representation by a call to
 * {@link cz.csas.cscore.client.rest.converter.Converter#toBody(Object) toBody} on the supplied
 * {@link cz.csas.cscore.client.rest.converter.Converter Converter} for this instance. The body can also be a
 * {@link TypedOutput} where it will be used directly.
 * <p>
 * Alternative request body formats are supported by method annotations and corresponding parameter
 * annotations:
 * <ul>
 * <li>{@link CsFormUrlEncoded @FormUrlEncoded} - Form-encoded data with key-value
 * pairs specified by the {@link CsField @Field} parameter annotation.
 * <li>{@link CsMultipart @Multipart} - RFC 2387-compliant multi-part data with parts
 * specified by the {@link CsPart @Part} parameter annotation.
 * </ul>
 * <p>
 * Additional static headers can be added for an endpoint using the
 * {@link CsHeaders @Headers} method annotation. For per-request control over a header
 * annotate a parameter with {@link CsHeader @Header}.
 * <p>
 * For example:
 * <pre>
 * public interface MyApi {
 *   &#64;POST("/category/{cat}") // Asynchronous execution.
 *   void categoryList(@Path("cat") String a, @Query("page") int b,
 *                     Callback&lt;List&lt;Item&gt;&gt; cb);
 *   &#64;POST("/category/{cat}") // Synchronous execution.
 *   List&lt;Item&gt; categoryList(@Path("cat") String a, @Query("page") int b);
 * }
 * </pre>
 * <p>
 * Calling {@link #create(Class)} with {@code MyApi.class} will validate and create a new
 * implementation of the API.
 *
 * @author Bob Lee (bob@squareup.com)
 * @author Jake Wharton (jw@squareup.com)
 */
public class CsRestAdapter {
    static final String WEB_API_CLIENT_MODULE = "WebApiClient";
    static final String THREAD_PREFIX = "CsCoreRest-";
    static final String IDLE_THREAD_NAME = THREAD_PREFIX + "Idle";

    private final Map<Class<?>, Map<Method, RestMethodInfo>> serviceMethodInfoCache =
            new LinkedHashMap<Class<?>, Map<Method, RestMethodInfo>>();

    final Endpoint server;
    final Executor httpExecutor;
    final Executor callbackExecutor;
    final RequestInterceptor requestInterceptor;
    final Converter converter;
    final LogManager log;
    final RequestSigner requestSigner;
    final ErrorHandler errorHandler;

    private final Client.Provider clientProvider;
    private final Profiler profiler;
    private Request request;

    private CsRestAdapter(Endpoint server, Client.Provider clientProvider, Executor httpExecutor,
                          Executor callbackExecutor, RequestInterceptor requestInterceptor, RequestSigner requestSigner, Converter converter,
                          Profiler profiler, ErrorHandler errorHandler, LogManager log) {
        this.server = server;
        this.clientProvider = clientProvider;
        this.httpExecutor = httpExecutor;
        this.callbackExecutor = callbackExecutor;
        this.requestInterceptor = requestInterceptor;
        this.requestSigner = requestSigner;
        this.converter = converter;
        this.profiler = profiler;
        this.errorHandler = errorHandler;
        this.log = log;
    }

    /**
     * Create an implementation of the API defined by the specified {@code service} interface.
     */
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> service) {
        //Utils.validateServiceClass(service);
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new RestHandler(getMethodInfoCache(service)));
    }

    Map<Method, RestMethodInfo> getMethodInfoCache(Class<?> service) {
        synchronized (serviceMethodInfoCache) {
            Map<Method, RestMethodInfo> methodInfoCache = serviceMethodInfoCache.get(service);
            if (methodInfoCache == null) {
                methodInfoCache = new LinkedHashMap<Method, RestMethodInfo>();
                serviceMethodInfoCache.put(service, methodInfoCache);
            }
            return methodInfoCache;
        }
    }

    static RestMethodInfo getMethodInfo(Map<Method, RestMethodInfo> cache, Method method) {
        synchronized (cache) {
            RestMethodInfo methodInfo = cache.get(method);
            if (methodInfo == null) {
                methodInfo = new RestMethodInfo(method);
                cache.put(method, methodInfo);
            }
            return methodInfo;
        }
    }

    private void setRequest(Request request) {
        this.request = request;
    }

    public void setNonce(String nonce) {
        ((RequestSignerImpl) requestSigner).setNonce(nonce);
    }

    public Request getRequest() {
        return request;
    }

    private class RestHandler implements InvocationHandler {
        private final Map<Method, RestMethodInfo> methodDetailsCache;

        RestHandler(Map<Method, RestMethodInfo> methodDetailsCache) {
            this.methodDetailsCache = methodDetailsCache;
        }

        @SuppressWarnings("unchecked") //
        @Override
        public Object invoke(Object proxy, Method method, final Object[] args)
                throws Throwable {
            // If the method is a method from Object then defer to normal invocation.
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }

            // Load or create the details cache for the current method.
            final RestMethodInfo methodInfo = getMethodInfo(methodDetailsCache, method);

            if (methodInfo.isSynchronous) {
                try {
                    return invokeRequest(requestInterceptor, methodInfo, args);
                } catch (CsRestError error) {
                    Throwable newError = errorHandler.handleError(error);
                    if (newError == null) {
                        throw new IllegalStateException("Error handler returned null for wrapped exception.",
                                error);
                    }
                    throw newError;
                }
            }

            if (httpExecutor == null || callbackExecutor == null) {
                throw new IllegalStateException("Asynchronous invocation requires calling setExecutors.");
            }

            // Apply the interceptor synchronously, recording the interception so we can replay it later.
            // This way we still defer argument serialization to the background thread.
            final RequestInterceptorTape interceptorTape = new RequestInterceptorTape();
            requestInterceptor.intercept(interceptorTape);

            Callback<?> callback = (Callback<?>) args[args.length - 1];
            httpExecutor.execute(new CallbackRunnable(callback, callbackExecutor, errorHandler) {
                @Override
                public ResponseWrapper obtainResponse() {
                    return (ResponseWrapper) invokeRequest(interceptorTape, methodInfo, args);
                }
            });
            return null; // Asynchronous methods should have return type of void.
        }

        /**
         * Execute an HTTP request.
         *
         * @return HTTP response object of specified {@code type} or {@code null}.
         * @throws CsRestError if any error occurs during the HTTP request.
         */
        private Object invokeRequest(RequestInterceptor requestInterceptor, RestMethodInfo methodInfo,
                                     Object[] args) {
            String url = null;
            try {
                methodInfo.init(); // Ensure all relevant method information has been loaded.

                String serverUrl = server.getUrl();
                RequestBuilder requestBuilder = new RequestBuilder(serverUrl, methodInfo, converter, requestSigner);
                requestBuilder.setArguments(args);

                requestInterceptor.intercept(requestBuilder);

                Request request = requestBuilder.build();
                setRequest(request);
                url = request.getUrl();

                if (!methodInfo.isSynchronous) {
                    // If we are executing asynchronously then update the current thread with a useful name.
                    int substrEnd = url.indexOf("?", serverUrl.length());
                    if (substrEnd == -1) {
                        substrEnd = url.length();
                    }
                    Thread.currentThread().setName(THREAD_PREFIX
                            + url.substring(serverUrl.length(), substrEnd));
                }

                // Log the request data.
                request = logAndReplaceRequest(request, args);

                Object profilerObject = null;
                if (profiler != null) {
                    profilerObject = profiler.beforeCall();
                }

                long start = System.nanoTime();
                Response response = clientProvider.get().execute(request);
                long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

                int statusCode = response.getStatus();
                if (profiler != null) {
                    Profiler.RequestInformation requestInfo = getRequestInfo(serverUrl, methodInfo, request);
                    //noinspection unchecked
                    profiler.afterCall(requestInfo, elapsedTime, statusCode, profilerObject);
                }

                // Log the response data.
                response = logAndReplaceResponse(url, response, elapsedTime);

                Type type = methodInfo.responseObjectType;

                if (statusCode >= 200 && statusCode < 300) { // 2XX == successful request
                    // Caller requested the raw Response object directly.
                    if (type.equals(WebApiStream.class)) {
                        if (methodInfo.isStreaming) {
                            // Read the entire stream and replace with one backed by a byte[].
                            response = Utils.readBodyToBytesIfNecessary(response);
                            WebApiStream webApiStream = setWebApiStream(response);

                            if (methodInfo.isSynchronous) {
                                return webApiStream;
                            }
                            return new ResponseWrapper(response, webApiStream);
                        }
                    } else if (type.equals(response)) {
                        if (methodInfo.isSynchronous) {
                            return response;
                        }
                        return new ResponseWrapper(response, response);
                    }

                    TypedInput body = response.getBody();
                    if (body == null) {
                        if (methodInfo.isSynchronous) {
                            return null;
                        }
                        return new ResponseWrapper(response, null);
                    }

                    ExceptionCatchingTypedInput wrapped = new ExceptionCatchingTypedInput(body);
                    try {
                        Object convert = converter.fromBody(wrapped, type);
                        /**
                         * Empty response handling. It is possible to either pass to Callback type
                         * the Object of Void. Both returns Void class in a case of empty body.
                         *
                         * WebApi uses Object for all responses.
                         *
                         * Different return types always returns null in a case of empty body.
                         * (used in LockerClient, LockerResponse)
                         */
                        if (convert == null && (type == Object.class || type == EmptyResponse.class))
                            convert = new EmptyResponse();
                        logResponseBody(body, convert);
                        if (methodInfo.isSynchronous) {
                            return convert;
                        }
                        return new ResponseWrapper(response, convert);
                    } catch (ConversionException e) {
                        // If the underlying input stream threw an exception, propagate that rather than
                        // indicating that it was a conversion exception.
                        if (wrapped.threwException()) {
                            throw wrapped.getThrownException();
                        }

                        // The response body was partially read by the converter. Replace it with null.
                        response = Utils.replaceResponseBody(response, null);

                        throw CsRestError.conversionError(url, response, converter, type, e);
                    }
                }

                response = Utils.readBodyToBytesIfNecessary(response);
                throw CsRestError.httpError(url, response, converter, type);
            } catch (CsRestError e) {
                throw e; // Pass through our own errors.
            } catch (IOException e) {
                logException(e, url);

                throw CsRestError.networkError(url, e);
            } catch (Throwable t) {
                logException(t, url);

                throw CsRestError.unexpectedError(url, t);
            } finally {
                if (!methodInfo.isSynchronous) {
                    Thread.currentThread().setName(IDLE_THREAD_NAME);
                }
            }
        }
    }

    /**
     * Log request headers and body. Consumes request body and returns identical replacement.
     */
    Request logAndReplaceRequest(Request request, Object[] args) throws IOException {
        log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, String.format("---> HTTP %s %s", request.getMethod(), request.getUrl())), LogLevel.INFO);
        for (Header header : request.getHeaders()) {
            log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, header.toString()), LogLevel.DEBUG);
        }

        String bodySize = "no";
        TypedOutput body = request.getBody();
        if (body != null) {
            String bodyMime = body.mimeType();
            if (bodyMime != null) {
                log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, "Content-Type: " + bodyMime), LogLevel.DEBUG);
            }

            long bodyLength = body.length();
            bodySize = bodyLength + "-byte";
            if (bodyLength != -1) {
                log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, "Content-Length: " + bodyLength), LogLevel.DEBUG);
            }

            if (!request.getHeaders().isEmpty()) {
                log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, null), LogLevel.DEBUG);
            }
            if (!(body instanceof TypedByteArray)) {
                // Read the entire response body to we can log it and replace the original response
                request = Utils.readBodyToBytesIfNecessary(request);
                body = request.getBody();
            }

            byte[] bodyBytes = ((TypedByteArray) body).getBytes();
            String bodyCharset = MimeUtil.parseCharset(body.mimeType(), "UTF-8");
            log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, new String(bodyBytes, bodyCharset)), LogLevel.DEBUG);
            //} else if (logLevel.ordinal() >= LogLevel.HEADERS_AND_ARGS.ordinal()) {
            if (!request.getHeaders().isEmpty()) {
                log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, "---> REQUEST:"), LogLevel.DEBUG);
            }
            for (int i = 0; i < args.length; i++) {
                log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, "#" + i + ": " + args[i]), LogLevel.DEBUG);
            }
        }

        log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, String.format("---> END HTTP (%s body)", bodySize)), LogLevel.INFO);

        return request;
    }

    private WebApiStream setWebApiStream(Response response) throws IOException {
        WebApiStream webApiStream = null;
        String contentType = null;
        Long contentLength = null;
        String filename = null;
        String contentDisposition = null;
        List<Header> responseHeaders = response.getHeaders();
        for (Header header : responseHeaders) {
            if (header.getName() != null) {
                if (header.getName().equalsIgnoreCase("Content-Type")) {
                    contentType = header.getValue();
                } else if (header.getName().equalsIgnoreCase("Content-Length")) {
                    contentLength = Long.parseLong(header.getValue());
                } else if (header.getName().equalsIgnoreCase("Content-Disposition")) {
                    contentDisposition = header.getValue();
                    filename = parseFilename(contentDisposition);
                }
            }
        }
        if (response.getBody() != null)
            webApiStream = new WebApiStream(filename, contentType, contentLength, contentDisposition, response.getBody().in());
        return webApiStream;
    }

    private static String parseFilename(String contentDisposition) {
        String[] disposition = contentDisposition.split(";");
        for (String field : disposition) {
            if (field.contains("filename"))
                return field.substring(field.indexOf("\"") + 1, field.lastIndexOf("\""));
        }
        return null;
    }

    /**
     * Log response headers and body. Consumes response body and returns identical replacement.
     */
    private Response logAndReplaceResponse(String url, Response response, long elapsedTime)
            throws IOException {
        log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, String.format("%s", response.getStatus()), String.format("<--- %s (%sms)", url, elapsedTime)), LogLevel.INFO);
        for (Header header : response.getHeaders()) {
            log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, header.toString()), LogLevel.DEBUG);
        }

        long bodySize = 0;
        TypedInput body = response.getBody();
        if (body != null) {
            bodySize = body.length();

            if (!response.getHeaders().isEmpty()) {
                log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, null), LogLevel.DEBUG);
            }

            if (!(body instanceof TypedByteArray)) {
                // Read the entire response body so we can log it and replace the original response
                response = Utils.readBodyToBytesIfNecessary(response);
                body = response.getBody();
            }

            byte[] bodyBytes = ((TypedByteArray) body).getBytes();
            bodySize = bodyBytes.length;
            String bodyMime = body.mimeType();
            String bodyCharset = MimeUtil.parseCharset(bodyMime, "UTF-8");
            log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, new String(bodyBytes, bodyCharset)), LogLevel.DEBUG);
        }
        log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, String.format("<--- END HTTP (%s-byte body)", bodySize)), LogLevel.INFO);

        return response;
    }

    private void logResponseBody(TypedInput body, Object convert) {
        if (convert != null) {
            log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, "<--- BODY:"), LogLevel.DEBUG);
            log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, convert.toString()), LogLevel.DEBUG);
        }

    }

    /**
     * Log an exception that occurred during the processing of a request or response.
     */
    void logException(Throwable t, String url) {
        log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, String.format("---- ERROR %s", url != null ? url : "")), LogLevel.DEBUG);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, sw.toString()), LogLevel.DEBUG);
        log.log(StringUtils.logLine(WEB_API_CLIENT_MODULE, null, "---- END ERROR"), LogLevel.DEBUG);
    }

    private static Profiler.RequestInformation getRequestInfo(String serverUrl,
                                                              RestMethodInfo methodDetails, Request request) {
        long contentLength = 0;
        String contentType = null;

        TypedOutput body = request.getBody();
        if (body != null) {
            contentLength = body.length();
            contentType = body.mimeType();
        }

        return new Profiler.RequestInformation(methodDetails.requestMethod, serverUrl,
                methodDetails.requestUrl, contentLength, contentType);
    }

    /**
     * Build a new {@link CsRestAdapter}.
     * <p>
     * Calling the following methods is required before calling {@link #build()}:
     * <ul>
     * <li>{@link #setEndpoint(Endpoint)}</li>
     * <li>{@link #setClient(Client.Provider)}</li>
     * <li>{@link #setConverter(Converter)}</li>
     * </ul>
     * <p>
     * If you are using asynchronous execution (i.e., with {@link Callback Callbacks}) the following
     * is also required:
     * <ul>
     * <li>{@link #setExecutors(Executor, Executor)}</li>
     * </ul>
     */
    public static class Builder {
        private Endpoint endpoint;
        private Client.Provider clientProvider;
        private Executor httpExecutor;
        private Executor callbackExecutor;
        private RequestInterceptor requestInterceptor;
        private Converter converter;
        private Profiler profiler;
        private ErrorHandler errorHandler;
        private LogManager log;
        private RequestSigner requestSigner;

        /**
         * API endpoint URL.
         */
        public Builder setEndpoint(String endpoint) {
            if (endpoint == null || endpoint.trim().length() == 0) {
                throw new NullPointerException("Endpoint may not be blank.");
            }
            this.endpoint = Endpoints.newFixedEndpoint(endpoint);
            return this;
        }

        /**
         * API endpoint.
         */
        public Builder setEndpoint(Endpoint endpoint) {
            if (endpoint == null) {
                throw new NullPointerException("Endpoint may not be null.");
            }
            this.endpoint = endpoint;
            return this;
        }

        /**
         * The HTTP client used for requests.
         */
        public Builder setClient(final Client client) {
            if (client == null) {
                throw new NullPointerException("Client may not be null.");
            }
            return setClient(new Client.Provider() {
                @Override
                public Client get() {
                    return client;
                }
            });
        }

        /**
         * The HTTP client used for requests.
         */
        public Builder setClient(Client.Provider clientProvider) {
            if (clientProvider == null) {
                throw new NullPointerException("Client provider may not be null.");
            }
            this.clientProvider = clientProvider;
            return this;
        }

        /**
         * Executors used for asynchronous HTTP client downloads and callbacks.
         *
         * @param httpExecutor     Executor on which HTTP client calls will be made.
         * @param callbackExecutor Executor on which any {@link Callback} methods will be invoked. If
         *                         this argument is {@code null} then callback methods will be run on the same thread as the
         *                         HTTP client.
         */
        public Builder setExecutors(Executor httpExecutor, Executor callbackExecutor) {
            if (httpExecutor == null) {
                throw new NullPointerException("HTTP executor may not be null.");
            }
            if (callbackExecutor == null) {
                callbackExecutor = new Utils.SynchronousExecutor();
            }
            this.httpExecutor = httpExecutor;
            this.callbackExecutor = callbackExecutor;
            return this;
        }

        /**
         * A request interceptor for adding data to every request.
         */
        public Builder setRequestInterceptor(RequestInterceptor requestInterceptor) {
            if (requestInterceptor == null) {
                throw new NullPointerException("Request interceptor may not be null.");
            }
            this.requestInterceptor = requestInterceptor;
            return this;
        }

        public Builder setRequestSigner(RequestSigner requestSigner) {
            if (requestSigner == null) {
                throw new NullPointerException("Request signer may not be null");
            }
            this.requestSigner = requestSigner;
            return this;
        }

        /**
         * The converter used for serialization and deserialization of objects.
         */
        public Builder setConverter(Converter converter) {
            if (converter == null) {
                throw new NullPointerException("Converter may not be null.");
            }
            this.converter = converter;
            return this;
        }

        /**
         * Set the profiler used to measure requests.
         */
        public Builder setProfiler(Profiler profiler) {
            if (profiler == null) {
                throw new NullPointerException("Profiler may not be null.");
            }
            this.profiler = profiler;
            return this;
        }

        /**
         * The error handler allows you to customize the type of exception thrown for errors on
         * synchronous requests.
         */
        public Builder setErrorHandler(ErrorHandler errorHandler) {
            if (errorHandler == null) {
                throw new NullPointerException("Error handler may not be null.");
            }
            this.errorHandler = errorHandler;
            return this;
        }

        /**
         * Configure debug logging mechanism.
         */
        public Builder setLog(LogManager log) {
            if (log == null) {
                throw new NullPointerException("Log may not be null.");
            }
            this.log = log;
            return this;
        }

        /**
         * Create the {@link CsRestAdapter} instances.
         */
        public CsRestAdapter build() {
            if (endpoint == null) {
                throw new IllegalArgumentException("Endpoint may not be null.");
            }
            ensureSaneDefaults();
            return new CsRestAdapter(endpoint, clientProvider, httpExecutor, callbackExecutor,
                    requestInterceptor, requestSigner, converter, profiler, errorHandler, log);
        }

        private void ensureSaneDefaults() {
            if (converter == null) {
                converter = Platform.get().defaultConverter();
            }
            if (clientProvider == null) {
                clientProvider = Platform.get().defaultClient();
            }
            if (httpExecutor == null) {
                httpExecutor = Platform.get().defaultHttpExecutor();
            }
            if (callbackExecutor == null) {
                callbackExecutor = Platform.get().defaultCallbackExecutor();
            }
            if (errorHandler == null) {
                errorHandler = ErrorHandler.DEFAULT;
            }
            if (log == null) {
                log = Platform.get().defaultLog();
            }
            if (requestInterceptor == null) {
                requestInterceptor = RequestInterceptor.NONE;
            }
        }
    }
}

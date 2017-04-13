package cz.csas.cscore.client.rest;

import cz.csas.cscore.client.rest.http.CsPath;

/**
 * Intercept every request before it is executed in order to add additional data.
 */
public interface RequestInterceptor {
  /**
   * Called for every request. Add data using methods on the supplied {@link RequestFacade}.  @param request the request
   */
  void intercept(RequestFacade request);

  /**
   * The interface Request facade.
   */
  interface RequestFacade {
    /**
     * Add a header to the request. This will not replace any existing headers.  @param name the name
     *
     * @param value the value
     */
    void addHeader(String name, String value);

    /**
     * Add a path parameter replacement. This works exactly like a {@link CsPath
     * &#64;Path}*-annotated method argument.
     *
     * @param name  the name
     * @param value the value
     */
    void addPathParam(String name, String value);

    /**
     * Add a path parameter replacement without first URI encoding. This works exactly like a
     * {@link CsPath &#64;Path}-annotated method argument with {@code encode=false}.
     *
     * @param name  the name
     * @param value the value
     */
    void addEncodedPathParam(String name, String value);

    /**
     * Add an additional query parameter. This will not replace any existing query parameters.  @param name the name
     *
     * @param value the value
     */
    void addQueryParam(String name, String value);

    /**
     * Add an additional query parameter without first URI encoding. This will not replace any
     * existing query parameters.
     *
     * @param name  the name
     * @param value the value
     */
    void addEncodedQueryParam(String name, String value);

    /**
     * Sign request.
     */
    void signRequest();
  }

  /**
   * A {@link RequestInterceptor} which does no modification of requests.
   */
  RequestInterceptor NONE = new RequestInterceptor() {
    @Override public void intercept(RequestFacade request) {
      // Do nothing.
    }
  };
}

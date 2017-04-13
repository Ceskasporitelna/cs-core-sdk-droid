/*
 * Copyright (C) 2013 Square, Inc.
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
package cz.csas.cscore.client.rest.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.csas.cscore.client.rest.http.CsStreaming;
import cz.csas.cscore.client.rest.mime.TypedByteArray;
import cz.csas.cscore.client.rest.mime.TypedInput;
import cz.csas.cscore.utils.csjson.CsJsonBuilder;

/**
 * An HTTP response.
 * <p/>
 * When used directly as a data type for an interface method, the response body is buffered to a
 * {@code byte[]}. Annotate the method with {@link CsStreaming @Streaming} for an
 * unbuffered stream from the network.
 */
public final class Response {
  private final String url;
  private final int status;
  private final String reason;
  private final List<Header> headers;
  private final TypedInput body;

  /**
   * Instantiates a new Response.
   *
   * @param url     the url
   * @param status  the status
   * @param reason  the reason
   * @param headers the headers
   * @param body    the body
   */
  public Response(String url, int status, String reason, List<Header> headers, TypedInput body) {
    if (url == null) {
      throw new IllegalArgumentException("url == null");
    }
    if (status < 200) {
      throw new IllegalArgumentException("Invalid status code: " + status);
    }
    if (reason == null) {
      throw new IllegalArgumentException("reason == null");
    }
    if (headers == null) {
      throw new IllegalArgumentException("headers == null");
    }

    this.url = url;
    this.status = status;
    this.reason = reason;
    this.headers = Collections.unmodifiableList(new ArrayList<Header>(headers));
    this.body = body;
  }

  /**
   * Request URL.  @return the url
   *
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * Status line code.  @return the status
   *
   * @return the status
   */
  public int getStatus() {
    return status;
  }

  /**
   * Status line reason phrase.  @return the reason
   *
   * @return the reason
   */
  public String getReason() {
    return reason;
  }

  /**
   * An unmodifiable collection of headers.  @return the headers
   *
   * @return the headers
   */
  public List<Header> getHeaders() {
    return headers;
  }

  /**
   * Response body. May be {@code null}.  @return the body
   *
   * @return the body
   */
  public TypedInput getBody() {
    return body;
  }

  /**
   * Response body. May be {@code null}.  @return the json body string
   *
   * @return the string
   */
  public String getBodyString(){
    if (body != null)
      return correctJson(new String(((TypedByteArray) getBody()).getBytes()));
    return null;
  }

  /**
   * Response body. May be {@code null}.  @return the json dictionary
   *
   * @return the map
   */
  public Map<String, Object> getBodyJsonDictionary(){
    if( body != null) {
      Map<String, Object> map = new HashMap<String, Object>();
      return (Map<String, Object>) new CsJsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(correctJson(new String(((TypedByteArray) getBody()).getBytes())), map.getClass());
    }
    return null;
  }

  /**
   * Response body. May be {@code null}.  @return the json body object defined with <T> param
   *
   * @param <T>    the type parameter
   * @param tClass the t class
   * @return the t
   */
  public <T> T getBodyObject(Class<T> tClass){
    if(body != null)
      return new CsJsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(correctJson(new String(((TypedByteArray) getBody()).getBytes())), tClass);
    return null;
  }

  private String correctJson(String json){
    StringBuilder stringBuilder = new StringBuilder();
    if(json.startsWith("["))
      stringBuilder.append("{ \"items\":");
    stringBuilder.append(json);
    if(json.endsWith("]"))
      stringBuilder.append("}");
    return stringBuilder.toString();
  }
}

/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.csas.cscore.utils.csjson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cz.csas.cscore.utils.csjson.CsJson;
import cz.csas.cscore.utils.csjson.CsJsonBuilder;

/**
 * An annotation that indicates this member should be serialized to JSON with
 * the provided name value as its field name.
 *
 * <p>This annotation will override any {@link cz.csas.cscore.utils.csjson.FieldNamingPolicy}, including
 * the default field naming policy, that may have been set on the {@link CsJson}
 * instance.  A different naming policy can set using the {@code CsJsonBuilder} class.  See
 * {@link CsJsonBuilder#setFieldNamingPolicy(cz.csas.cscore.utils.csjson.FieldNamingPolicy)}
 * for more information.</p>
 *
 * <p>Here is an example of how this annotation is meant to be used:</p>
 * <pre>
 * public class MyClass {
 *   &#64SerializedName("name") String a;
 *   &#64SerializedName(value="name1", alternate={"name2", "name3"}) String b;
 *   String c;
 *
 *   public MyClass(String a, String b, String c) {
 *     this.a = a;
 *     this.b = b;
 *     this.c = c;
 *   }
 * }
 * </pre>
 *
 * <p>The following shows the output that is generated when serializing an instance of the
 * above example class:</p>
 * <pre>
 * MyClass target = new MyClass("v1", "v2", "v3");
 * CsJson csJson = new CsJson();
 * String json = csJson.toJson(target);
 * System.out.println(json);
 *
 * ===== OUTPUT =====
 * {"name":"v1","name1":"v2","c":"v3"}
 * </pre>
 *
 * <p>NOTE: The value you specify in this annotation must be a valid JSON field name.</p>
 * While deserializing, all values specified in the annotation will be deserialized into the field.
 * For example:
 * <pre>
 *   MyClass target = csJson.fromJson("{'name1':'v1'}", MyClass.class);
 *   assertEquals("v1", target.b);
 *   target = csJson.fromJson("{'name2':'v2'}", MyClass.class);
 *   assertEquals("v2", target.b);
 *   target = csJson.fromJson("{'name3':'v3'}", MyClass.class);
 *   assertEquals("v3", target.b);
 * </pre>
 * Note that MyClass.b is now deserialized from either name1, name2 or name3.
 *
 * @see cz.csas.cscore.utils.csjson.FieldNamingPolicy
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface CsSerializedName {

  /**
   * @return the desired names of the field when it is deserialized or serialized. All of the specified names will be deserialized from.
   *   The specified first name is what is used for serialization.
   */
  String value();
  String[] alternate() default {};
}

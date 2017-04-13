/*
 * Copyright (C) 2014 Google Inc.
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

package cz.csas.cscore.utils.csjson.internal.bind;

import cz.csas.cscore.utils.csjson.CsJson;
import cz.csas.cscore.utils.csjson.TypeAdapter;
import cz.csas.cscore.utils.csjson.TypeAdapterFactory;
import cz.csas.cscore.utils.csjson.annotations.CsJsonAdapter;
import cz.csas.cscore.utils.csjson.internal.ConstructorConstructor;
import cz.csas.cscore.utils.csjson.reflect.TypeToken;

/**
 * Given a type T, looks for the annotation {@link CsJsonAdapter} and uses an instance of the
 * specified class as the default type adapter.
 *
 * @since 2.3
 */
public final class JsonAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory {

  private final ConstructorConstructor constructorConstructor;

  public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
    this.constructorConstructor = constructorConstructor;
  }

  @SuppressWarnings("unchecked")
  public <T> TypeAdapter<T> create(CsJson csJson, TypeToken<T> targetType) {
    CsJsonAdapter annotation = targetType.getRawType().getAnnotation(CsJsonAdapter.class);
    if (annotation == null) {
      return null;
    }
    return (TypeAdapter<T>) getTypeAdapter(constructorConstructor, csJson, targetType, annotation);
  }

  @SuppressWarnings("unchecked") // Casts guarded by conditionals.
  static TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, CsJson csJson,
      TypeToken<?> fieldType, CsJsonAdapter annotation) {
    Class<?> value = annotation.value();
    if (TypeAdapter.class.isAssignableFrom(value)) {
          Class<TypeAdapter<?>> typeAdapter = (Class<TypeAdapter<?>>) value;
      return constructorConstructor.get(TypeToken.get(typeAdapter)).construct();
    }
    if (TypeAdapterFactory.class.isAssignableFrom(value)) {
          Class<TypeAdapterFactory> typeAdapterFactory = (Class<TypeAdapterFactory>) value;
      return constructorConstructor.get(TypeToken.get(typeAdapterFactory))
          .construct()
          .create(csJson, fieldType);
    }

    throw new IllegalArgumentException(
        "@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.");
  }
}

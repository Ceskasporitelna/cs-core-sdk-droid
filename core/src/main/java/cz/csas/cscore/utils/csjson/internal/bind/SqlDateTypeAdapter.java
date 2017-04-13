/*
 * Copyright (C) 2011 Google Inc.
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

package cz.csas.cscore.utils.csjson.internal.bind;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cz.csas.cscore.utils.csjson.CsJson;
import cz.csas.cscore.utils.csjson.JsonSyntaxException;
import cz.csas.cscore.utils.csjson.TypeAdapter;
import cz.csas.cscore.utils.csjson.TypeAdapterFactory;
import cz.csas.cscore.utils.csjson.reflect.TypeToken;
import cz.csas.cscore.utils.csjson.stream.JsonReader;
import cz.csas.cscore.utils.csjson.stream.JsonToken;
import cz.csas.cscore.utils.csjson.stream.JsonWriter;

/**
 * Adapter for java.sql.Date. Although this class appears stateless, it is not.
 * DateFormat captures its time zone and locale when it is created, which gives
 * this class state. DateFormat isn't thread safe either, so this class has
 * to synchronize its read and write methods.
 */
public final class SqlDateTypeAdapter extends TypeAdapter<Date> {
  public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
    @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
    public <T> TypeAdapter<T> create(CsJson csJson, TypeToken<T> typeToken) {
      return typeToken.getRawType() == java.sql.Date.class
          ? (TypeAdapter<T>) new SqlDateTypeAdapter() : null;
    }
  };

  private final DateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.US);

  @Override
  public synchronized java.sql.Date read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    try {
      final long utilDate = format.parse(in.nextString()).getTime();
      return new java.sql.Date(utilDate);
    } catch (ParseException e) {
      throw new JsonSyntaxException(e);
    }
  }

  @Override
  public synchronized void write(JsonWriter out, java.sql.Date value) throws IOException {
    out.value(value == null ? null : format.format(value));
  }
}

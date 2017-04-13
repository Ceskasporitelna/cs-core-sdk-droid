package cz.csas.cscore.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import cz.csas.cscore.error.CsSigningError;
import cz.csas.cscore.webapi.HasValue;
import cz.csas.cscore.webapi.apiquery.CsSignUrl;

/**
 * The type Url utils.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
public class UrlUtils {

    /**
     * Get sign url string.
     *
     * @param object the entity
     * @return the string
     */
    public static String getSignUrl(Object object) {
        if (object.getClass().isAnnotationPresent(CsSignUrl.class)) {
            Annotation annotation = object.getClass().getAnnotation(CsSignUrl.class);
            CsSignUrl csSignUrl = (CsSignUrl) annotation;
            return mapToParameters(csSignUrl.signUrl(), object);
        }
        return null;
    }

    /**
     * To query string string.
     *
     * @param dictionary the dictionary
     * @return the string
     */
    public static String toQueryString(Map<String, String> dictionary) {
        if (dictionary == null)
            return "";
        return "?" + mapToString(dictionary);
    }

    /**
     * Joins enums into string separated by comma
     *
     * @param <T>  the type parameter
     * @param list the list
     * @return the string
     */
    public static <T extends HasValue> String joinEnums(List<T> list) {
        if (list != null && list.size() != 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i).getValue());
                if (i != list.size() - 1)
                    sb.append(",");
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * Joins List of Strings into String separated by commas
     *
     * @param list the list
     * @return the string
     */
    public static String joinStrings(List<String> list) {
        if (list != null && list.size() != 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i));
                if (i != list.size() - 1)
                    sb.append(",");
            }
            return sb.toString();
        }
        return "";
    }

    private static String mapToString(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value = map.get(key);
            stringBuilder.append((key != null ? key : ""));
            stringBuilder.append("=");
            stringBuilder.append(value != null ? value : "");
        }

        return stringBuilder.toString();
    }

    private static String mapToParameters(String url, Object object) {
        while (url.contains("{")) {
            String name = url.substring(url.indexOf("{") + 1);
            name = name.substring(0, name.indexOf("}"));
            Field field;
            try {
                field = object.getClass().getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                try {
                    field = object.getClass().getSuperclass().getDeclaredField(name);
                } catch (NoSuchFieldException e1) {
                    throw new CsSigningError(CsSigningError.Kind.OTHER, "Signing url is malformed." + name + " is not an object field.");
                }
            }
            field.setAccessible(true);
            try {
                url = url.replace("{" + name + "}", String.valueOf(field.get(object)));
            } catch (IllegalAccessException e) {
                throw new CsSigningError(CsSigningError.Kind.OTHER, "Signing url is malformed." + name + " is not an object field.");
            }
        }
        return url;
    }
}

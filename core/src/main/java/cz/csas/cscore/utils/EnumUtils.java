package cz.csas.cscore.utils;

import cz.csas.cscore.webapi.HasValue;

/**
 * The type Enum utils.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 25 /10/16.
 */
public class EnumUtils {

    /**
     * Translate string to enum.
     * Enum has to extends HasValue interface otherwise null is returned.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @param value the value
     * @return the t
     */
    public static <T extends Enum<T>> T translateToEnum(Class<T> clazz, String value) {
        T[] constants = clazz.getEnumConstants();
        T result = iterateEnum(constants, value, false);
        /**
         * Fallback to OTHER
         */
        if (result == null && value != null)
            result = iterateEnum(constants, null, true);
        return result;
    }

    private static <T extends Enum<T>> T iterateEnum(T[] constants, String value, boolean allowOther) {
        for (T constant : constants) {
            if (constant instanceof HasValue) {
                if ((allowOther && value == null && ((HasValue) constant).getValue() == null)
                        || (value != null && value.equals(((HasValue) constant).getValue()))) {
                    return constant;
                }
            } else {
                break;
            }
        }
        return null;
    }
}

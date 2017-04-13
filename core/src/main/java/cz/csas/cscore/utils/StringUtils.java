package cz.csas.cscore.utils;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 15/08/16.
 */
public class StringUtils {

    public static String logLine(String module, String action, String message) {
        return action != null ? "[" + module + " " + action + "] " + message : "[" + module + "] " + message;
    }

}

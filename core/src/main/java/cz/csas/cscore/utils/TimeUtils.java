package cz.csas.cscore.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Time utils.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13 /03/16.
 */
public class TimeUtils {

    /**
     * The constant COLON_INDEX.
     */
    final static int COLON_INDEX = 22;

    /**
     * Get iso 8601 date.
     *
     * @param time the time
     * @return the date
     */
    public static Date getISO8601Date(String time) {
        if (time == null)
            return null;
        if (!time.contains("+"))
            time = time + "Z";
        String s = time.replace("Z", "+00:00");
        try {
            if (s.length() > COLON_INDEX && s.charAt(COLON_INDEX) == ':') {
                s = s.substring(0, COLON_INDEX) + s.substring(COLON_INDEX + 1);  // to get rid of the ":"
            }
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).parse(s);
            return date;
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Get iso 8601 string.
     *
     * @param time the time
     * @return the string
     */
    public static String getISO8601String(Date time) {
        if (time == null)
            return null;
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
                .format(time);
        return formatted.substring(0, COLON_INDEX) + ":" + formatted.substring(COLON_INDEX);
    }

    /**
     * Get plain date.
     *
     * @param date the date
     * @return the plain date
     */
    public static Date getPlainDate(String date) {
        if (date == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get plain date string.
     *
     * @param date the date
     * @return the plain date string
     */
    public static String getPlainDateString(Date date) {
        if (date == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(date);
    }

    /**
     * Get miliseconds timestamp. Convert to millisecond timestamp -> We have to multiply it by 1000
     * because the returned time interval is in seconds
     *
     * @param currentTime the current time in milliseconds
     * @param expiresIn   the expires in in seconds
     * @return the miliseconds timestamp
     */
    public static String getMilisecondsTimestamp(long currentTime, long expiresIn) {
        return String.valueOf(currentTime + (expiresIn - 5) * 1000);
    }

}

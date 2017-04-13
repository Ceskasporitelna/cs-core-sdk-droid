package cz.csas.cscore.locker;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.Date;
import java.util.regex.Pattern;

import cz.csas.cscore.utils.TimeUtils;

/**
 * @author Frantisek Kratochvil <kratochvilf@gmail.com>
 * @since 11.04.16.
 */
public class TimeUtilsTest extends TestCase {

    @Test
    public void testDateToString() {
        long expectedDateTimestamp = 938383200000L;
        String dateString = "1999-09-27T00:00:00+02:00";
        Date dateFromString = TimeUtils.getISO8601Date(dateString);
        String stringFromDate = TimeUtils.getISO8601String(new Date(expectedDateTimestamp));

        assertEquals(expectedDateTimestamp, dateFromString.getTime());
        assertTrue(Pattern.compile("^1999-09-\\d{2}T\\d{2}:00:00[-+]\\d{2}:00$").matcher(stringFromDate).matches());
    }

    @Test
    public void testTimeZoneWithoutColon() {
        long expectedDateTimestamp = 938383200000L;
        String dateString = "1999-09-27T00:00:00+0200";
        Date dateFromString = TimeUtils.getISO8601Date(dateString);

        assertEquals(expectedDateTimestamp, dateFromString.getTime());
    }

    @Test
    public void testWithoutTimeZone() {
        long expectedDateTimestamp = 938390400000L;
        String dateString = "1999-09-27T00:00:00";
        Date dateFromString = TimeUtils.getISO8601Date(dateString);

        assertEquals(expectedDateTimestamp, dateFromString.getTime());
    }

    @Test
    public void testZulu() {
        long expectedDateTimestamp = 1370090701000L;
        String dateString = "2013-06-01T12:45:01Z";
        Date dateFromString = TimeUtils.getISO8601Date(dateString);

        assertEquals(expectedDateTimestamp, dateFromString.getTime());
    }

}

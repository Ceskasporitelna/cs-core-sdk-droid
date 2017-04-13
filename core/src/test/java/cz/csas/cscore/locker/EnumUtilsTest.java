package cz.csas.cscore.locker;

import junit.framework.TestCase;

import cz.csas.cscore.utils.EnumUtils;
import cz.csas.cscore.webapi.HasValue;

/**
 * The type Enum utils test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 25 /10/16.
 */
public class EnumUtilsTest extends TestCase {

    /**
     * Test enum has value.
     */
    public void testEnumHasValue() {
        assertEquals(Extended.ONE, EnumUtils.translateToEnum(Extended.class, "number one"));
        assertEquals(Extended.TWO, EnumUtils.translateToEnum(Extended.class, "number two"));
        assertEquals(Extended.THREE, EnumUtils.translateToEnum(Extended.class, "number three"));
        assertEquals(Extended.OTHER, EnumUtils.translateToEnum(Extended.class, "number four"));
        assertEquals(null, EnumUtils.translateToEnum(Extended.class, null));

    }

    /**
     * Test enum.
     */
    public void testEnum() {
        assertEquals(null, EnumUtils.translateToEnum(Normal.class, "number one"));
        assertEquals(null, EnumUtils.translateToEnum(Normal.class, null));
    }

    private enum Normal {
        /**
         * One normal.
         */
        ONE, /**
         * Two normal.
         */
        TWO, /**
         * Three normal.
         */
        THREE;
    }

    private enum Extended implements HasValue {
        /**
         * The One.
         */
        ONE("number one"),

        /**
         * The Two.
         */
        TWO("number two"),

        /**
         * The Three.
         */
        THREE("number three"),

        /**
         * Other extended.
         */
        OTHER(null);

        private String value;

        Extended(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }
}

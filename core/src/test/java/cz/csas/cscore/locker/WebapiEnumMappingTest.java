package cz.csas.cscore.locker;

import junit.framework.TestCase;

import org.junit.Before;

import cz.csas.cscore.utils.EnumUtils;
import cz.csas.cscore.utils.csjson.CsJson;
import cz.csas.cscore.utils.csjson.CsJsonBuilder;
import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.utils.csjson.annotations.CsSerializedName;
import cz.csas.cscore.webapi.HasValue;

/**
 * The type Enum type adapter test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16 /03/16.
 */
public class WebapiEnumMappingTest extends TestCase {

    private CsJson csJson;

    @Before
    public void setUp() {
        csJson = new CsJsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    /**
     * Test enum type adapter.
     */
    public void testToJson() {
        // to json
        assertEquals("\"This is planet Mercur.\"", csJson.toJson(Planet.MERCUR));
        assertEquals("null", csJson.toJson(Planet.OTHER));
        assertEquals("\"MILKY_WAY\"", csJson.toJson(Galaxy.MILKY_WAY));

    }

    /**
     * Test from json.
     */
    public void testFromJson() {
        // from json
        assertEquals(Planet.MERCUR, csJson.fromJson("\"This is planet Mercur.\"", Planet.class));
        assertEquals(null, csJson.fromJson("null", Planet.class));
        assertEquals(Galaxy.ANDROMEDA, csJson.fromJson("\"ANDROMEDA\"", Galaxy.class));
    }

    /**
     * Test created.
     */
    public void testCreated() {
        // created enum
        Planet planet = Planet.OTHER;
        assertEquals("null", csJson.toJson(planet));
        assertEquals(Planet.OTHER, csJson.fromJson("\"This is my planet B612.\"", Planet.class));
    }

    /**
     * Test unknown.
     */
    public void testUnknown() {
        // unknown enum
        assertEquals(Planet.OTHER, csJson.fromJson("\"This is totally new planet.\"", Planet.class));
        assertEquals(Planet.OTHER, csJson.fromJson("\"This is a brand new planet.\"", Planet.class));
    }


    /**
     * Test object.
     */
    public void testObject() {
        SolarSystem solarSystemRaw = new SolarSystem("This is planet Mercur.");
        assertEquals(Planet.MERCUR, solarSystemRaw.getEarth());
        assertEquals("This is planet Mercur.", solarSystemRaw.getEarth().getValue());
        assertEquals("This is planet Mercur.", solarSystemRaw.getEarthRaw());
        assertEquals("{\"earth\":\"This is planet Mercur.\"}", csJson.toJson(solarSystemRaw, SolarSystem.class));

        SolarSystem solarSystem = new SolarSystem(Planet.MERCUR);
        assertEquals(Planet.MERCUR, solarSystem.getEarth());
        assertEquals("This is planet Mercur.", solarSystem.getEarth().getValue());
        assertEquals("This is planet Mercur.", solarSystem.getEarthRaw());
        assertEquals("{\"earth\":\"This is planet Mercur.\"}", csJson.toJson(solarSystemRaw, SolarSystem.class));

        SolarSystem solarSystemOther = new SolarSystem(Planet.OTHER);
        assertEquals(Planet.OTHER, solarSystemOther.getEarth());
        assertEquals(null, solarSystemOther.getEarth().getValue());
        assertEquals(null, solarSystemOther.getEarthRaw());
        assertEquals("{}", csJson.toJson(solarSystemOther, SolarSystem.class));

        SolarSystem solarSystemOtherRaw = new SolarSystem("This is planet Earth.");
        assertEquals(Planet.OTHER, solarSystemOtherRaw.getEarth());
        assertEquals(null, solarSystemOtherRaw.getEarth().getValue());
        assertEquals("This is planet Earth.", solarSystemOtherRaw.getEarthRaw());
        assertEquals("{\"earth\":\"This is planet Earth.\"}", csJson.toJson(solarSystemOtherRaw, SolarSystem.class));
    }


    /**
     * Example of object including HasValue enum and its mapping by
     * {@link EnumUtils@translateToEnum}
     */
    private class SolarSystem {

        private Planet earth;

        @CsExpose
        @CsSerializedName("earth")
        private String earthRaw;

        /**
         * Instantiates a new Solar system.
         *
         * @param earthRaw the earth raw
         */
        public SolarSystem(String earthRaw) {
            this.earthRaw = earthRaw;
        }

        /**
         * Instantiates a new Solar system.
         *
         * @param earth the earth
         */
        public SolarSystem(Planet earth) {
            this.earth = earth;
            if(earth != Planet.OTHER)
                this.earthRaw = earth.getValue();
        }

        /**
         * Get earth.
         *
         * @return the earth
         */
        public Planet getEarth() {
            if (earth == null && earthRaw != null)
                return EnumUtils.translateToEnum(Planet.class, earthRaw);
            return earth;
        }

        /**
         * Set earth.
         *
         * @param earth the earth
         */
        public void setEarth(Planet earth) {
            this.earthRaw = earth.getValue();
            this.earth = earth;
        }

        /**
         * Set earth.
         *
         * @param earthRaw the earth raw
         */
        public void setEarth(String earthRaw) {
            this.earthRaw = earthRaw;
        }

        /**
         * Get earth raw.
         *
         * @return the earth raw
         */
        public String getEarthRaw() {
            return earthRaw;
        }
    }

    /**
     * Example of HasValue enum
     */
    private enum Planet implements HasValue {

        /**
         * The Mercur.
         */
        MERCUR("This is planet Mercur."),

        /**
         * The Venus.
         */
        OTHER(null);

        private String value;

        /**
         * Instantiates a new Planet.
         *
         * @param value the value
         */
        Planet(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        private void setValue(String value) {
            this.value = value;
        }

        /**
         * Other planet.
         *
         * @param value the value
         * @return the planet
         */
        public static Planet other(String value) {
            Planet planet = Planet.OTHER;
            planet.setValue(value);
            return planet;
        }
    }

    private enum Galaxy {

        /**
         * Milky way galaxy.
         */
        MILKY_WAY,

        /**
         * Andromeda galaxy.
         */
        ANDROMEDA;
    }

}

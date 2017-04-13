package cz.csas.cscore.locker;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * The type Enum type adapter test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16 /03/16.
 */
public class HasUrlTest extends TestCase {

    private final String URL = "www.myapi.cz/photo.jpg";
    private PhotoResource photoResource;

    @Before
    public void setUp() {
        photoResource = new PhotoResource(URL, null);
    }

    /**
     * Test enum type adapter.
     */
    @Test
    public void testHasUrl() {
        PhotoParameters parameters = new PhotoParameters(true,"small");
        assertEquals(URL,photoResource.url());
        assertTrue(photoResource.url(parameters).contains("blackAndWhite=true"));
        assertTrue(photoResource.url(parameters).contains("size=small"));
        assertTrue(photoResource.url(parameters).contains(URL));
    }

}

package cz.csas.cscore.locker;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;

import cz.csas.cscore.webapi.apiquery.CsSignUrl;

/**
 * The type Sign url test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 30 /03/16.
 */
public class SignUrlTest extends TestCase {

    private final String BASE_PATH = "/resourcePart/id";
    private SignUrlExample signUrlExample;
    private SignUrlExampleEmpty signUrlExampleEmpty;

    @Before
    public void setUp() {
        signUrlExample = new SignUrlExample(BASE_PATH);
        signUrlExampleEmpty = new SignUrlExampleEmpty(BASE_PATH);
    }


    /**
     * Test sign url.
     * Java Reflection to identify CsSignUrl
     */
    @Test
    public void testSignUrl(){
        String normalized = null;
        String normalizedEmpty = null;
        String signUrl = getSignUrl(SignUrlExample.class);
        String signUrlEmpty = getSignUrl(SignUrlExampleEmpty.class);

        assertEquals("/../resourceSpec/",signUrl);
        assertEquals("",signUrlEmpty);
        try {
            normalized = new URI(signUrlExample.getBasePath()+signUrl+signUrlExample.getSignUrlPath()).normalize().getPath();
            normalizedEmpty = new URI(signUrlExampleEmpty.getBasePath()+signUrlEmpty+signUrlExampleEmpty.getSignUrlPath()).normalize().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        assertEquals("/resourcePart/resourceSpec/id/sign/signId",normalized);
        assertEquals("/resourcePart/id/sign/signId",normalizedEmpty);
    }

    private String getSignUrl(Class obj){
        if(obj.isAnnotationPresent(CsSignUrl.class)){
            Annotation annotation = obj.getAnnotation(CsSignUrl.class);
            CsSignUrl csSignUrl = (CsSignUrl) annotation;
            return csSignUrl.signUrl();
        }
        return null;
    }
}

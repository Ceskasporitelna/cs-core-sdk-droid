package cz.csas.cscore.webapi.apiquery;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CsSignUrl annotation to define sign url path
 * <p/>
 * This path is canonical normalized from base path and sign url.
 * <p/>
 * f.e.
 * base path: /resourcePart/id
 * <p/>
 * class def:
 * \@CsSignUrl(signUrl="/../resourceSpec")
 * public class SignUrlExample
 * <p/>
 * result signPath: /resourcePart/resourceSpec/id/sign/signId
 * <p/>
 * see the example in unit test variant cz.csas.cscore.locker.SignUrlTest
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 30 /03/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CsSignUrl {

    /**
     * Sign url string.
     *
     * @return the sign url of resource to be signed with
     */
    String signUrl() default "";
}

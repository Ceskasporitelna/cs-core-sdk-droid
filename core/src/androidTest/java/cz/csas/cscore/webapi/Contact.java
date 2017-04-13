package cz.csas.cscore.webapi;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.webapi.apiquery.CsSignUrl;
import cz.csas.cscore.webapi.signing.Signable;
import cz.csas.cscore.webapi.signing.SigningObject;

/**
 * The type Contact.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 04 /05/16.
 */
@CsSignUrl(signUrl = "/../{id}")
public class Contact extends WebApiEntity implements Signable {

    @CsExpose
    private Integer id;

    @CsExpose
    private String name;

    @CsExpose
    private String phoneNumber;

    private SigningObject signingObject;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public SigningObject signing() {
        return signingObject;
    }

    @Override
    public void setSigningObject(SigningObject signingObject) {
        this.signingObject = signingObject;
    }
}

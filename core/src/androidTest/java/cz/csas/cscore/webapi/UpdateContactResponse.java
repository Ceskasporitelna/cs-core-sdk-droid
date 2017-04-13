package cz.csas.cscore.webapi;

import cz.csas.cscore.webapi.apiquery.CsSignUrl;
import cz.csas.cscore.webapi.signing.Signable;
import cz.csas.cscore.webapi.signing.SigningObject;

/**
 * The type Update contact response.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 01 /09/16.
 */
@CsSignUrl()
public class UpdateContactResponse extends WebApiEntity implements Signable {

    private SigningObject signingObject;

    @Override
    public SigningObject signing() {
        return signingObject;
    }

    @Override
    public void setSigningObject(SigningObject signingObject) {
        this.signingObject = signingObject;
    }
}

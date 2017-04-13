package cz.csas.cscore.webapi;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Create contact request.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 04 /05/16.
 */
public class CreateContactRequest extends WebApiRequest {

    @CsExpose
    private String name;

    @CsExpose
    private String phoneNumber;

    /**
     * Instantiates a new Create contact request.
     *
     * @param name        the name
     * @param phoneNumber the phone number
     */
    public CreateContactRequest(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}

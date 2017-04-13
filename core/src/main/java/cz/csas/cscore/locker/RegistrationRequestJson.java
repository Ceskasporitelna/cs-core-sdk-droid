package cz.csas.cscore.locker;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Registration json.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 10 /11/15.
 */
class RegistrationRequestJson implements LockerRequestJson {

    @CsExpose
    private String code;

    @CsExpose
    private String password;

    @CsExpose
    private String deviceFingerprint;

    @CsExpose
    private String scope;

    @CsExpose
    private String nonce;

    /**
     * Instantiates a new Registration json.
     *
     * @param code              the code
     * @param password          the password
     * @param deviceFingerprint the device fingerprint
     * @param scope             the scope
     * @param nonce             the nonce
     */
    public RegistrationRequestJson(String code, String password, String deviceFingerprint, String scope, String nonce) {
        this.code = code;
        this.password = password;
        this.deviceFingerprint = deviceFingerprint;
        this.scope = scope;
        this.nonce = nonce;
    }

    @Override
    public String toString() {
        return "RegistrationRequestJson{" +
                "nonce='" + nonce + '\'' +
                ", scope='" + scope + '\'' +
                ", deviceFingerprint='" + deviceFingerprint + '\'' +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}

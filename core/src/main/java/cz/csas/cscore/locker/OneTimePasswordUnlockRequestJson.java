package cz.csas.cscore.locker;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type One time password unlock request json.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 22 /11/15.
 */
public class OneTimePasswordUnlockRequestJson implements LockerRequestJson{

    @CsExpose
    private String id;

    @CsExpose
    private String oneTimePassword;

    @CsExpose
    private String deviceFingerprint;

    @CsExpose
    private String nonce;

    /**
     * Instantiates a new Unlock request json.
     *
     * @param id                the id
     * @param oneTimePassword   the one time password
     * @param deviceFingerprint the device fingerprint
     * @param nonce             the nonce
     */
    public OneTimePasswordUnlockRequestJson(String id, String oneTimePassword, String deviceFingerprint, String nonce) {
        this.id = id;
        this.oneTimePassword = oneTimePassword;
        this.deviceFingerprint = deviceFingerprint;
        this.nonce = nonce;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getOneTimePassword() {
        return oneTimePassword;
    }

    /**
     * Gets device fingerprint.
     *
     * @return the device fingerprint
     */
    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    /**
     * Gets nonce.
     *
     * @return the nonce
     */
    public String getNonce() {
        return nonce;
    }

    @Override
    public String toString() {
        return "OneTimePasswordUnlockRequestJson{" +
                "id='" + id + '\'' +
                ", oneTimePassword='" + oneTimePassword + '\'' +
                ", deviceFingerprint='" + deviceFingerprint + '\'' +
                ", nonce='" + nonce + '\'' +
                '}';
    }
}

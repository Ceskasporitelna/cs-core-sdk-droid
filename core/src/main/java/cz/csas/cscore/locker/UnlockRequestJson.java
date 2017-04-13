package cz.csas.cscore.locker;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Unlock request json.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 11 /11/15.
 */
class UnlockRequestJson implements LockerRequestJson {

    @CsExpose
    private String id;

    @CsExpose
    private String password;

    @CsExpose
    private String deviceFingerprint;

    @CsExpose
    private String nonce;

    /**
     * Instantiates a new Unlock request json.
     *
     * @param id                the id
     * @param password          the password
     * @param deviceFingerprint the device fingerprint
     * @param nonce             the nonce
     */
    public UnlockRequestJson(String id, String password, String deviceFingerprint, String nonce) {
        this.id = id;
        this.password = password;
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
    public String getPassword() {
        return password;
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
        return "UnlockRequestJson{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", deviceFingerprint='" + deviceFingerprint + '\'' +
                ", nonce='" + nonce + '\'' +
                '}';
    }
}



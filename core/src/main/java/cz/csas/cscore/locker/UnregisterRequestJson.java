package cz.csas.cscore.locker;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Unregister request json.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
class UnregisterRequestJson implements LockerRequestJson {

    @CsExpose
    private String id;

    @CsExpose
    private String deviceFingerprint;

    @CsExpose
    private String nonce;

    /**
     * Instantiates a new Unregister request json.
     *
     * @param id                the id
     * @param deviceFingerprint the device fingerprint
     * @param nonce             the nonce
     */
    public UnregisterRequestJson(String id, String deviceFingerprint, String nonce) {
        this.id = id;
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
        return "UnregisterRequestJson{" +
                "id='" + id + '\'' +
                ", deviceFingerprint='" + deviceFingerprint + '\'' +
                ", nonce='" + nonce + '\'' +
                '}';
    }
}

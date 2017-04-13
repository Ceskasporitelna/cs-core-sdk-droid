package cz.csas.cscore.locker;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Password request json.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
class PasswordRequestJson implements LockerRequestJson{

    @CsExpose
    private String id;

    @CsExpose
    private String password;

    @CsExpose
    private String newPassword;

    @CsExpose
    private String deviceFingerprint;

    @CsExpose
    private String nonce;

    /**
     * Instantiates a new Unlock request json.
     *
     * @param id                the id
     * @param password          the password
     * @param newPassword       the new password
     * @param deviceFingerprint the device fingerprint
     * @param nonce             the nonce
     */
    public PasswordRequestJson(String id, String password,String newPassword, String deviceFingerprint, String nonce) {
        this.id = id;
        this.password = password;
        this.newPassword = newPassword;
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
     * Gets new password.
     *
     * @return the new password
     */
    public String getNewPassword() {
        return newPassword;
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
        return "PasswordRequestJson{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", deviceFingerprint='" + deviceFingerprint + '\'' +
                ", nonce='" + nonce + '\'' +
                '}';
    }
}

package cz.csas.cscore.storage;

import cz.csas.cscore.locker.AccessToken;
import cz.csas.cscore.locker.LockType;
import cz.csas.cscore.locker.Password;

/**
 * The interface Keychain manager.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 11 /11/15.
 */
public interface KeychainManager {

    /**
     * Retrieve device fingerprint string.
     *
     * @return the string
     */
    public String retrieveDFP();

    /**
     * Store cid.
     *
     * @param cid the cid
     */
    public void storeCID(String cid);

    /**
     * Retrieve cid string.
     *
     * @return the string
     */
    public String retrieveCID();

    /**
     * Store access token.
     *
     * @param accessToken the access token
     */
    public void storeAccessToken(AccessToken accessToken);

    /**
     * Retrieve access token string.
     *
     * @return the string
     */
    public AccessToken retrieveAccessToken();

    /**
     * Store refresh token.
     *
     * @param refreshToken the refresh token
     */
    public void storeRefreshToken(String refreshToken);

    /**
     * Retrieve refresh token string.
     *
     * @return the string
     */
    public String retrieveRefreshToken();

    /**
     * Store one time password.
     *
     * @param oneTimePasswordKey the one time password key
     */
    public void storeOneTimePasswordKey(String oneTimePasswordKey);

    /**
     * Retrieve one time password key string.
     *
     * @return the string
     */
    public String retrieveOneTimePasswordKey();

    /**
     * Has one time password key boolean.
     *
     * @return the boolean
     */
    public boolean hasOneTimePasswordKey();


    /**
     * Store code.
     *
     * @param code the code
     */
    public void storeCode(String code);

    /**
     * Retrieve code string.
     *
     * @return the string
     */
    public String retrieveCode();

    /**
     * Store lock type.
     *
     * @param lockType the lock type
     */
    public void storeLockType(LockType lockType);

    /**
     * Retrieve lock type lock type.
     *
     * @return the lock type
     */
    public LockType retrieveLockType();

    /**
     * Store gesture grid size.
     *
     * @param size the size
     */
    public void storePasswordInputSpaceSize(Integer size);

    /**
     * Retrieve gesture grid size int.
     *
     * @return the Integer
     */
    public Integer retrievePasswordInputSpaceSize();

    /**
     * Sets ek.
     *
     * @param ek the ek
     */
    public void storeLocalEK(String ek);

    /**
     * Gets ek.
     *
     * @return the ek
     */
    public byte[] retrieveLocalEK();

    /**
     * Store pwd random.
     *
     * @param random the random
     */
    public void storePwdRandom(String random);

    /**
     * Retrieve pwd random string.
     *
     * @return the string
     */
    public String retrievePwdRandom();

    /**
     * Store no lock password.
     *
     * @param password the password
     */
    public void storeNoLockPassword(String password);

    /**
     * Retrieve no lock password string.
     *
     * @return the string
     */
    public String retrieveNoLockPassword();

    /**
     * Wipe no lock password string.
     *
     * @return the string
     */
    public void wipeNoLockPassword();

    /**
     * Sets offline password hash.
     *
     * @param password the hash
     */
    public void storeOfflinePasswordHash(Password password);

    /**
     * Gets offline password hash.
     *
     * @return the offline password hash
     */
    public String retrieveOfflinePasswordHash();

    /**
     * Wipe offline password hash.
     */
    public void wipeOfflinePasswordHash();

    /**
     * Store num of offline auth attempts.
     *
     * @param numOfattempts the num ofattempts
     */
    public void storeNumOfOfflineAuthAttempts(int numOfattempts);

    /**
     * Retrieve num of offline auth attempts int.
     *
     * @return the int
     */
    public int retrieveNumOfOfflineAuthAttempts();

    /**
     * Sets password random.
     *
     * @param random the random
     */
    public void setPasswordRandom(String random);

    /**
     * Gets password random.
     *
     * @return the password random
     */
    public String getPasswordRandom();

    /**
     * Sets ek.
     *
     * @param ek the ek
     */
    public void setEK(byte[] ek);

    /**
     * Has ek boolean.
     *
     * @return the boolean
     */
    public boolean hasEK();

    /**
     * Gets sek.
     *
     * @return the sek
     */
    public byte[] getSEK();

    /**
     * Generate sek string.
     *
     * @return the string
     */
    public byte[] generateSEK();

    /**
     * Gets nonce.
     *
     * @return the nonce
     */
    public String getNonce();

    /**
     * Store encrypted secret.
     *
     * @param string the string
     */
    public void storeEncryptedSecret(String string);

    /**
     * Retrieve encrypted secret string.
     *
     * @return the string
     */
    public String retrieveEncryptedSecret();

    /**
     * Wipe encrypted secret.
     */
    public void wipeEncryptedSecret();

    /**
     * Store iv secret.
     *
     * @param iv the iv
     */
    public void storeIvSecret(String iv);

    /**
     * Retrieve iv secret string.
     *
     * @return the string
     */
    public String retrieveIvSecret();

    /**
     * Wipe iv secret.
     */
    public void wipeIvSecret();

    /**
     * Store environment hash.
     *
     * @param hash the hash
     */
    public void storeEnvironmentHash(String hash);

    /**
     * Retrieve environment hash.
     */
    public String retrieveEnvironmentHash();

    /**
     * Clear all.
     */
    public void clearAll();

    /**
     * Clear volatile keys.
     */
    public void clearVolatileKeys();
}

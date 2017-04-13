package cz.csas.cscore.client.crypto;

import java.security.PublicKey;

import cz.csas.cscore.locker.Password;

/**
 * The interface Crypto manager.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 10 /11/15.
 */
public interface CryptoManager {

    /**
     * AES encrypt function
     *
     * @param original the original
     * @param key      16, 24, 32 bytes available
     * @param iv       initial vector (16 bytes) - if null: ECB mode, otherwise: CBC mode
     * @return the byte [ ]
     */
    public byte[] encryptAES(String original, byte[] key, byte[] iv);

    /**
     * AES decrypt function
     *
     * @param encrypted the encrypted
     * @param key       16, 24, 32 bytes available
     * @param iv        initial vector (16 bytes) - if null: ECB mode, otherwise: CBC mode
     * @return the byte [ ]
     */
    public String decryptAES(byte[] encrypted, byte[] key, byte[] iv);

    /**
     * Generate public key public key.
     *
     * @param key the key
     * @return the public key
     */
    public PublicKey generatePublicKey(String key);

    /**
     * RSA encrypt function (RSA / ECB / PKCS1-Padding)
     *
     * @param original the original
     * @param key      the key
     * @return the byte [ ]
     */
    public byte[] encryptRSA(String original, PublicKey key);

    /**
     * Encrypt pbkdf 2 byte [ ].
     *
     * @param original the original
     * @param salt     the salt
     * @return the byte [ ]
     */
    public byte[] encryptPBKDF2(String original, String salt);

    /**
     * Encode base 64 string.
     *
     * @param text the text
     * @return the string
     */
    public String encodeBase64(byte[] text);

    /**
     * Decode base 64 byte [ ].
     *
     * @param text the text
     * @return the byte [ ]
     */
    public byte[] decodeBase64(String text);

    /**
     * Pbkdf string.
     *
     * @param password the password
     * @param salt     the salt
     * @return the string
     */
    public String encodeSha256(String password, String salt);

    /**
     * Encode sha 1 string.
     *
     * @param string the string
     * @return the string
     */
    public String encodeSha1(String string);

    /**
     * Random string.
     *
     * @return the string
     */
    public String generateRandomString();

    /**
     * Generate password string.
     *
     * @param salt  the salt
     * @param bytes the bytes
     * @param key   the key
     * @param time  the time
     * @return the string
     */
    public String generatePassword(String salt, int bytes, byte[] key, long time);

    /**
     * Translate password string.
     *
     * @param password the password
     * @return the string
     */
    public String createOfflinePasswordWithCollision(Password password);
}

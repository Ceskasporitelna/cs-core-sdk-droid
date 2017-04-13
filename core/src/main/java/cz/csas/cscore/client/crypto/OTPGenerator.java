package cz.csas.cscore.client.crypto;

/**
 * The interface Otp generator.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 07 /12/15.
 */
public interface OTPGenerator {

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
     * Construct payload string.
     *
     * @param time the time
     * @param salt the salt
     * @return the string
     */
    public String constructPayload(long time, String salt);
}

package cz.csas.cscore.client.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * The type Otp generator.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 06 /12/15.
 */
public class OTPGeneratorImpl implements OTPGenerator {

    private static final long OTP_START = 1010101010;
    private static final int OTP_INTERVAL = 30;
    private final int OTP_LENGTH = 7;
    private final long OTP_POWER = 10000000;

    @Override
    public String generatePassword(String salt, int bytes, byte[] key, long time) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA256");
            String payload = constructPayload(time, salt);
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            byte[] hash = mac.doFinal(payload.getBytes());
            int offset = hash.length - 4;
            byte[] truncated = Arrays.copyOfRange(hash, offset, offset + bytes);
            long code = bytesToLong(truncated);
            code &= 0x7FFFFFFF;
            code = code % OTP_POWER;
            return leftPad(String.valueOf(code), OTP_LENGTH, '0');
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String constructPayload(long time, String salt) {
        return generateTimeStamp(time) + salt;
    }

    private long bytesToLong(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value += ((long) bytes[i] & 0xFFL) << (8 * i);
        }
        return value;
    }

    private String leftPad(String code, int length, char character) {
        while (code.length() < length) {
            code = character + code;
        }
        return code;
    }

    private long generateTimeStamp(long currentTime) {
        return (long) Math.floor((currentTime - OTP_START / 1000) / (OTP_INTERVAL));
    }

}

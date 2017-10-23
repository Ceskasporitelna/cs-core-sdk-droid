package cz.csas.cscore.client.crypto;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import cz.csas.cscore.error.CsCoreError;
import cz.csas.cscore.locker.Password;
import cz.csas.cscore.utils.Base64;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 10/11/15.
 */
public class CryptoManagerImpl implements CryptoManager {

    private OTPGenerator mOTPGenerator;
    private final int MAX_LENGTH = 50;

    public CryptoManagerImpl() {
        mOTPGenerator = new OTPGeneratorImpl();
    }

    @SuppressLint("GetInstance")
    @Override
    public byte[] encryptAES(String original, byte[] key, byte[] iv) {
        if (key == null || (key.length != 16 && key.length != 24 && key.length != 32)) {
            return null;
        }
        try {
            SecretKeySpec keySpec = null;
            Cipher cipher = null;
            // CsSDK doesnt use initialization vector!
            if (iv != null) {
                keySpec = new SecretKeySpec(key, "AES");
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
            } else {
                keySpec = new SecretKeySpec(key, "AES");
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            }

            return cipher.doFinal(original.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new CsCoreError(CsCoreError.Kind.CRYPTO, e);
        }
    }

    @SuppressLint("GetInstance")
    @Override
    public String decryptAES(byte[] encrypted, byte[] key, byte[] iv) {
        if (key == null || (key.length != 16 && key.length != 24 && key.length != 32)) {
            return null;
        }
        try {
            SecretKeySpec keySpec = null;
            Cipher cipher = null;
            if (iv != null) {
                keySpec = new SecretKeySpec(key, "AES");
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
            } else {
                keySpec = new SecretKeySpec(key, "AES");
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, keySpec);
            }
            return new String(cipher.doFinal(encrypted), "UTF-8");
        } catch (Exception e) {
            throw new CsCoreError(CsCoreError.Kind.CRYPTO, e);
        }
    }

    @Override
    public PublicKey generatePublicKey(String key) {
        try {
            byte[] keyBytes = Base64.decode(key, Base64.DEFAULT);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            throw new CsCoreError(CsCoreError.Kind.CRYPTO, e);
        }
    }

    @Override
    public byte[] encryptRSA(String original, PublicKey key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(original.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new CsCoreError(CsCoreError.Kind.CRYPTO, e);
        }
    }

    @Override
    public byte[] encryptPBKDF2(String original, String salt) {
        try {
            int iterations = 200;
            char[] passwordChars = original.toCharArray();
            byte[] saltBytes = salt.getBytes("UTF-8");

            PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            return skf.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new CsCoreError(CsCoreError.Kind.CRYPTO, e);
        }
    }

    @Override
    public String encodeBase64(byte[] data) {
        return new String(Base64.encode(data, Base64.NO_WRAP));
    }

    @Override
    public byte[] decodeBase64(String data) {
        return Base64.decode(data, Base64.DEFAULT);
    }

    @Override
    public String encodeSha256(String password, String salt) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update((password + salt).getBytes("utf-8"));
        } catch (Exception e) {
            throw new CsCoreError(CsCoreError.Kind.CRYPTO, e);
        }

        byte[] digest = md.digest();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    @Override
    public String encodeSha1(String string) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update((string).getBytes("utf-8"));
        } catch (Exception e) {
            throw new CsCoreError(CsCoreError.Kind.CRYPTO, e);
        }

        byte[] digest = md.digest();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    @Override
    public String generateRandomString() {
        String outputRandom = null;
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        outputRandom = randomStringBuilder.toString();
        if (outputRandom == null || outputRandom.isEmpty())
            outputRandom = generateRandomString();
        return outputRandom;
    }

    @Override
    public String generatePassword(String salt, int bytes, byte[] key, long time) {
        return mOTPGenerator.generatePassword(salt, bytes, key, time);
    }

    @Override
    public String createOfflinePasswordWithCollision(Password password) {
        switch (password.getLockType()) {
            case PIN:
                return createOfflinePINPasswordWithCollision(password.getPassword());
            case GESTURE:
                return createOfflineGesturePasswordWithCollision(password.getPassword(),
                        password.getPasswordSpaceSize());
            case FINGERPRINT:
                return password.getPassword();
            default:
                return null;
        }
    }

    private String createOfflinePINPasswordWithCollision(String password) {
        if (password.matches("^\\d+$")) {
            String[] passwordChars = password.split("(?!^)");
            int n = password.length();
            int passwordNumber = 0;

            // password number calculated as sum(i = 0 .. n-1)(x(i) * 10^(n-i-1))
            // "1234" = 1234
            for (int i = 0; i < n; i++) {
                passwordNumber += Integer.parseInt(passwordChars[i]) * Math.pow(10, (n - i - 1));
            }

            int modulo;
            if (n == 4)
                modulo = 23;
            else
                modulo = (int) Math.pow(10, n) / 1023;

            /*
             * For short pin
             *      - modulo 23 => it promises 4.34% passwords will collide
             * For long pin
             *      - modulo = 10^n/1023 where n is the pin size
             *      - 10^n is a number of possible combinations
             *      - 1023 is a size of colliding password space. It promises 1023/10^n% passwords
             *        will collide. It also provides 0.5% probability of passing the online auth using
             *        all the five attempts
             */

            return String.valueOf(passwordNumber % modulo);
        }
        return null;
    }

    private String createOfflineGesturePasswordWithCollision(String password, Integer[] gridSize) {
        // Split gesture code
        String[] passwordArray = password.split("&");
        Integer[] passwordDigits = new Integer[passwordArray.length];

        // Initialize BigInteger num of permutations
        BigInteger numOfPermutations = BigInteger.valueOf(0);
        for (int i = 0; i < passwordArray.length; i++) {
            // Each point of gesture is defined as "row-column" taken from left to right, from top
            // to bottom, from zero to cell size
            String[] pointDef = passwordArray[i].split("-");

            // Find out the value for each cell point as column + 1 + columnMax * row
            passwordDigits[i] = (Integer.parseInt(pointDef[1]) + 1)
                    + gridSize[1] * Integer.parseInt(pointDef[0]);

            // Find smaller digits already used in password code before index
            int smallerDigitsUsed = getSmallerDigitsUsed(passwordDigits, i);

            // Calculate all the permutations lexycographically less than the password code
            if (passwordDigits[i] - 1 - smallerDigitsUsed > 0)
                numOfPermutations = numOfPermutations.add(getFactorial(passwordDigits[i] - 1
                        - smallerDigitsUsed));
        }

        BigInteger modulo = getFactorial(gridSize[0] * gridSize[1]).divide(BigInteger.valueOf(1000));

        /*
         * - modulo = (k^2)!/1000 where k is the gesture grid size
         * - (k^2)! is a number of possible combinations
         * - 1000 is a size of colliding password space. It promises 1000/(k^2)!% passwords
         *   will collide. It also provides 0.5% probability of passing the online auth using
         *   all the five attempts
         */
        return numOfPermutations.mod(modulo).toString();
    }

    private BigInteger getFactorial(int num) {
        BigInteger fact = BigInteger.valueOf(1);
        for (int i = 1; i <= num; i++)
            fact = fact.multiply(BigInteger.valueOf(i));
        return fact;
    }

    private int getSmallerDigitsUsed(Integer[] numbers, int index) {
        int result = 0;
        for (int i = 0; i < index; i++) {
            if (numbers[i] < numbers[index]) {
                result++;
            }
        }
        return result;
    }


}

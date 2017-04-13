package cz.csas.cscore.locker;

import org.junit.Before;
import org.junit.Test;

import java.security.PublicKey;

import cz.csas.cscore.client.crypto.CryptoManager;
import cz.csas.cscore.client.crypto.CryptoManagerImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 20/07/16.
 */
public class CryptoManagerTest {

    private final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhmgBlAsGkJpbFOuNC7gRbSwmffpf83hC0zTSGE08Mq1xR6cjylZ9tUBV6nS4YlKhsgjr+WuAyKMruPf4b3uyjkZabY7EB1DXV9wzm07+f38PO7jU5Ceo0Rv0LAX/BnKV3uMkXBlQSXPkXMda354qmu7DUD8JjbJTjcpBTRhdy5r0guTC+pjKfdPZM2eDqN3fClaHtLsn4YTI64g1hV18siJxelyXT8EeQGVOfs4ojloieRxqGlrJDQORakHW+4WECG4eWkd8r6VPWl6Ycnvx3Fh0apOZiE1MrqD6ztnxaC74pdAXrhImrIuidccMWKEIorcxJ0dNm5KqZUi66v3ZPwIDAQAB";
    private final String PASSWORD = "unbreakablePassword";
    private final String SALT = "unpredictableSalt";
    private final byte[] ENCRYPTION_KEY = new byte[32];
    private CryptoManager cryptoManager;

    @Before
    public void setUp() {
        cryptoManager = new CryptoManagerImpl();
    }


    @Test
    public void testEncryptAES() {
        // CS Core does not use initialization vector
        String AESHash = cryptoManager.encodeBase64(cryptoManager.encryptAES(PASSWORD, ENCRYPTION_KEY, null));
        assertEquals("/Jy0PYB8vYbW7RQHLggw8q9tpilid7zuwLdq0vPKXH0=", AESHash);
    }

    @Test
    public void testDecryptAES() {
        String AESHash = cryptoManager.encodeBase64(cryptoManager.encryptAES(PASSWORD, ENCRYPTION_KEY, null));
        assertEquals("/Jy0PYB8vYbW7RQHLggw8q9tpilid7zuwLdq0vPKXH0=", AESHash);
        String password = cryptoManager.decryptAES(cryptoManager.decodeBase64(AESHash), ENCRYPTION_KEY, null);
        assertEquals(PASSWORD, password);
    }

    @Test
    public void testGeneratePublicKey() {
        PublicKey key = cryptoManager.generatePublicKey(PUBLIC_KEY);
        assertEquals("Sun RSA public key, 2048 bits\n" +
                "  modulus: 16967220217021671152178892477231009015243495790313719336161299644899167961631365381815645464834341952086105015002662293275335687492352591089840240146588297777178564523515491894604665809095396170182116633672542921547250684813393536458314143277909816532097239934410857998427489948003439916361692336608031133208798996907314832115199705190798786876113308913114574331236989511958868732918114006314318425934008306964864325557308778880460509022538796098155408384003919493211364364102030637670903174626186020240493772334999127871175182596795729590257590160107132972309306402168098119666548899453790653994931917100578085132607\n" +
                "  public exponent: 65537", key.toString());
    }

    @Test
    public void testEncryptRSA() {
        String RSAHash = cryptoManager.encodeBase64(cryptoManager.encryptRSA(PASSWORD, cryptoManager.generatePublicKey(PUBLIC_KEY)));
        assertNotNull(RSAHash);
    }

    @Test
    public void testEncryptPBKDF2() {
        String PBKDF2Hash = cryptoManager.encodeBase64(cryptoManager.encryptPBKDF2(PASSWORD, SALT));
        assertEquals("sonu1HLpIG828PkhCDK4qWabprO6CikeSuV++YmD4qt6swh3OLV19EJUZYMStjDj0GjdVRB/KfRRYfvqior7RQ==", PBKDF2Hash);

        String PBKDF2Hash2 = cryptoManager.encodeBase64(cryptoManager.encryptPBKDF2(PASSWORD, SALT));
        assertEquals(PBKDF2Hash, PBKDF2Hash2);
    }

    @Test
    public void testEncodeBase64() {
        String encoded = cryptoManager.encodeBase64(new byte[32]);
        assertEquals("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=", encoded);
    }

    @Test
    public void testDecodeBase64() {
        byte[] decoded = cryptoManager.decodeBase64("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=");
        assertNotNull(decoded);
    }

    @Test
    public void testEncodeSha256() {
        String hash = cryptoManager.encodeSha256(PASSWORD, SALT);
        assertEquals("f2a565dbc3e3b56331d3eb40ab24c540612a5be17678e21265eab9fcf79b535e", hash);
    }

    @Test
    public void testEncodeSha1() {
        String hash = cryptoManager.encodeSha1(PASSWORD);
        assertEquals("9e9920b4749eab82c6b6e378f1c4ab8c9a83a71a", hash);
    }

    @Test
    public void testGenerateRandomString() {
        String random = cryptoManager.generateRandomString();
        assertNotNull(random);
    }

    @Test
    public void testTranslatePassword() {
        // PIN test short
        Password pinPassword = new Password(LockType.PIN, "1234");
        Password pinPasswordConflicting = new Password(LockType.PIN, "1257");
        String translatedPinPassword = cryptoManager.createOfflinePasswordWithCollision(pinPassword);
        String translatedPinPasswordConflicting = cryptoManager.createOfflinePasswordWithCollision(pinPasswordConflicting);
        assertEquals("15", translatedPinPassword);
        assertEquals(translatedPinPassword, translatedPinPasswordConflicting);


        // PIN test long
        Password pinPasswordLong = new Password(LockType.PIN, "12345");
        Password pinPasswordConflictingLong = new Password(LockType.PIN, "12539");
        String translatedPinPasswordLong = cryptoManager.createOfflinePasswordWithCollision(pinPasswordLong);
        String translatedPinPasswordConflictingLong = cryptoManager.createOfflinePasswordWithCollision(pinPasswordConflictingLong);
        assertEquals("26", translatedPinPasswordLong);
        assertEquals(translatedPinPasswordLong, translatedPinPasswordConflictingLong);

        // Gesture test
        Password gesturePassword = new Password(LockType.GESTURE, "0-0&0-1&0-2&2-2&1-2", 3);
        Password gesturePasswordConflicting = new Password(LockType.GESTURE, "0-0&0-1&2-2&2-1&1-2", 3);
        String translatedGesturePassword = cryptoManager.createOfflinePasswordWithCollision(gesturePassword);
        String translatedGesturePasswordConflicting = cryptoManager.createOfflinePasswordWithCollision(gesturePasswordConflicting);
        assertEquals("122", translatedGesturePassword);
        assertEquals(translatedGesturePassword, translatedGesturePasswordConflicting);

        // More complicated 5x5 gesture (10 points)
        Password gesturePasswordComplicated = new Password(LockType.GESTURE, "4-4&2-1&0-2&0-0&0-1&1-2&3-3&3-2&0-4&2-4", 5);
        String translatedGesturePasswordComplicated = cryptoManager.createOfflinePasswordWithCollision(gesturePasswordComplicated);
        assertEquals("6745979547", translatedGesturePasswordComplicated);
    }
}

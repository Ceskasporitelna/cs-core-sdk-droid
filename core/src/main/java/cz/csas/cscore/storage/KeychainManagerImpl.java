package cz.csas.cscore.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.client.crypto.CryptoManager;
import cz.csas.cscore.locker.AccessToken;
import cz.csas.cscore.locker.LockType;
import cz.csas.cscore.locker.Locker;
import cz.csas.cscore.locker.Password;
import cz.csas.cscore.logger.LogLevel;
import cz.csas.cscore.logger.LogManager;
import cz.csas.cscore.utils.StringUtils;
import cz.csas.cscore.utils.csjson.CsJson;

/**
 * The type Keychain manager.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 11 /11/15.
 */
public class KeychainManagerImpl implements KeychainManager {

    private final String KEYCHAIN_CORE_PREFS = "CsKeychainCorePrefs";
    private final String LOCAL_EK_KEY = "local_ek_key";

    // EK encryption
    private final String ACCESS_TOKEN_KEY = "access_token_key";
    private final String REFRESH_TOKEN_KEY = "refresh_token_key";

    // Local EK encryption
    private final String CID_KEY = "cid_key";
    private final String DFP_KEY = "dfp_key";
    private final String OTP_KEY = "otp_key";
    private final String CODE_KEY = "code_key";
    private final String NO_LOCK_PWD_KEY = "no_lock_pwd_key";
    private final String LOCK_TYPE_KEY = "lock_type_key";
    private final String GESTURE_GRID_SIZE_KEY = "gesture_grid_size_key";
    private final String OFFLINE_PWD_KEY = "offline_pwd_key";
    private final String OFFLINE_AUTH_ATTEMPTS_KEY = "offline_auth_attempts_key";

    private final String PWD_RANDOM_KEY = "pwd_random_key";
    private final String ENC_SECRET_KEY = "enc_secret_key";
    private final String IV_SECRET_KEY = "iv_secret_key";
    private final String ENV_HASH_KEY = "env_hash_key";

    private SharedPreferences mSharedPreferencesKeychainCore;
    private CsJson mCsJson;
    private String mPasswordRandom;
    private byte[] mSek;
    private byte[] mEk;

    private Context mContext;
    private CryptoManager mCryptoManager;
    private LogManager mLogManager;

    /**
     * Instantiates a new Keychain manager.
     */
    public KeychainManagerImpl(Context context, CryptoManager cryptoManager) {
        mContext = context;
        mCryptoManager = cryptoManager;
        mSharedPreferencesKeychainCore = mContext.getSharedPreferences(KEYCHAIN_CORE_PREFS, Context.MODE_PRIVATE);
        mCsJson = new CsJson();
        mLogManager = CoreSDK.getInstance().getLogger();
    }

    @Override
    public void storeDFP(String deviceFingerprint) {
        encryptToKeychain(DFP_KEY, deviceFingerprint, retrieveLocalEK());
    }

    @SuppressLint("HardwareIds")
    @Override
    public String retrieveDFP() {
        if (mSharedPreferencesKeychainCore.contains(DFP_KEY))
            return decryptFromKeychain(DFP_KEY, retrieveLocalEK());
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    public void storeCID(String cid) {
        encryptToKeychain(CID_KEY, cid, retrieveLocalEK());
    }

    @Override
    public String retrieveCID() {
        return decryptFromKeychain(CID_KEY, retrieveLocalEK());
    }

    @Override
    public void storeAccessToken(AccessToken accessToken) {
        encryptToKeychain(ACCESS_TOKEN_KEY, mCsJson.toJson(accessToken), mEk);
    }

    @Override
    public AccessToken retrieveAccessToken() {
        String value = decryptFromKeychain(ACCESS_TOKEN_KEY, mEk);
        if (value != null)
            return mCsJson.fromJson(value, AccessToken.class);
        return null;
    }

    @Override
    public void storeRefreshToken(String refreshToken) {
        encryptToKeychain(REFRESH_TOKEN_KEY, refreshToken, mEk);
    }

    @Override
    public String retrieveRefreshToken() {
        return decryptFromKeychain(REFRESH_TOKEN_KEY, mEk);
    }

    @Override
    public void storeOneTimePasswordKey(String oneTimePasswordKey) {
        encryptToKeychain(OTP_KEY, oneTimePasswordKey, retrieveLocalEK());
    }

    @Override
    public String retrieveOneTimePasswordKey() {
        return decryptFromKeychain(OTP_KEY, retrieveLocalEK());
    }

    @Override
    public boolean hasOneTimePasswordKey() {
        return mSharedPreferencesKeychainCore.contains(OTP_KEY);
    }

    @Override
    public void storeCode(String code) {
        encryptToKeychain(CODE_KEY, code, retrieveLocalEK());
    }

    @Override
    public String retrieveCode() {
        return decryptFromKeychain(CODE_KEY, retrieveLocalEK());
    }

    @Override
    public void storeLockType(LockType lockType) {
        encryptToKeychain(LOCK_TYPE_KEY, mCsJson.toJson(lockType), retrieveLocalEK());
    }

    @Override
    public LockType retrieveLockType() {
        String value = decryptFromKeychain(LOCK_TYPE_KEY, retrieveLocalEK());
        if (value != null)
            return mCsJson.fromJson(value, LockType.class);
        return null;
    }

    @Override
    public void storePasswordInputSpaceSize(Integer[] size) {
        if (size != null) {
            StringBuilder builder = new StringBuilder();
            int length = size.length;
            for (int i = 0; i < length; i++) {
                builder.append(size[i]);
                if (length > 1 && i != length - 1)
                    // save gesture grid size with "," delimiter
                    builder.append(",");
            }
            encryptToKeychain(GESTURE_GRID_SIZE_KEY, builder.toString(), retrieveLocalEK());
        }
    }

    @Override
    public Integer[] retrievePasswordInputSpaceSize() {
        String sizeString = decryptFromKeychain(GESTURE_GRID_SIZE_KEY, retrieveLocalEK());
        if (sizeString != null && !sizeString.equals("")) {
            // check is gesture grid size, then parse with delimiter ","
            if (sizeString.contains(",")) {
                StringTokenizer tokenizer = new StringTokenizer(sizeString, ",");
                Integer[] size = new Integer[2];
                for (int i = 0; i < 2; i++) {
                    size[i] = Integer.parseInt(tokenizer.nextToken());
                }
                return size;
            } else {
                Integer size = Integer.parseInt(sizeString);
                // otherwise pin password size, no delimiter
                return new Integer[]{size, size};
            }
        }
        return null;
    }

    @Override
    public void storeLocalEK(String ek) {
        if (ek != null) {
            mSharedPreferencesKeychainCore.edit().putString(LOCAL_EK_KEY, ek).apply();
        }
    }

    @Override
    public byte[] retrieveLocalEK() {
        if (mSharedPreferencesKeychainCore.contains(LOCAL_EK_KEY))
            return Arrays.copyOfRange(mSharedPreferencesKeychainCore.getString(LOCAL_EK_KEY, "").getBytes(), 0, 32);
        return null;
    }

    @Override
    public void storePwdRandom(String random) {
        if (random != null) {
            mSharedPreferencesKeychainCore.edit().putString(PWD_RANDOM_KEY, random).apply();
        }
    }

    @Override
    public String retrievePwdRandom() {
        if (mSharedPreferencesKeychainCore.contains(PWD_RANDOM_KEY))
            return mSharedPreferencesKeychainCore.getString(PWD_RANDOM_KEY, "");
        return null;
    }

    @Override
    public void storeNoLockPassword(String password) {
        encryptToKeychain(NO_LOCK_PWD_KEY, password, retrieveLocalEK());
    }

    @Override
    public String retrieveNoLockPassword() {
        return decryptFromKeychain(NO_LOCK_PWD_KEY, retrieveLocalEK());
    }

    @Override
    public void wipeNoLockPassword() {
        wipeFromKeychain(NO_LOCK_PWD_KEY);
    }

    @Override
    public void storeOfflinePasswordHash(Password password) {
        String passwordString = password.getPassword();
        // check password before translation
        String translatedPassword = passwordString != null && !passwordString.isEmpty() ? mCryptoManager.createOfflinePasswordWithCollision(password) : null;
        if (translatedPassword != null)
            encryptToKeychain(OFFLINE_PWD_KEY, mCryptoManager.encodeBase64(mCryptoManager.encryptPBKDF2(translatedPassword, retrieveDFP() + retrievePwdRandom())), retrieveLocalEK());
    }

    @Override
    public String retrieveOfflinePasswordHash() {
        return decryptFromKeychain(OFFLINE_PWD_KEY, retrieveLocalEK());
    }

    @Override
    public void wipeOfflinePasswordHash() {
        wipeFromKeychain(OFFLINE_PWD_KEY);
    }

    @Override
    public void storeNumOfOfflineAuthAttempts(int numOfAttempts) {
        mSharedPreferencesKeychainCore.edit().putInt(OFFLINE_AUTH_ATTEMPTS_KEY, numOfAttempts).apply();
    }

    @Override
    public int retrieveNumOfOfflineAuthAttempts() {
        return mSharedPreferencesKeychainCore.getInt(OFFLINE_AUTH_ATTEMPTS_KEY, 0);
    }

    @Override
    public void setPasswordRandom(String random) {
        mPasswordRandom = random;
    }

    @Override
    public String getPasswordRandom() {
        return mPasswordRandom;
    }

    @Override
    public void setEK(byte[] ek) {
        if (ek != null) {
            mEk = Arrays.copyOfRange(ek, 0, 32);
        }
    }

    @Override
    public boolean hasEK() {
        return mEk != null;
    }

    @Override
    public byte[] getSEK() {
        return mSek;
    }

    @Override
    public byte[] generateSEK() {
        final byte[] sek = new byte[32];
        new Random().nextBytes(sek);
        mSek = sek;
        return mSek;
    }

    @Override
    public String getNonce() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void storeEncryptedSecret(String secret) {
        if (secret != null) {
            mSharedPreferencesKeychainCore.edit().putString(ENC_SECRET_KEY, secret).apply();
        }
    }

    @Override
    public String retrieveEncryptedSecret() {
        if (mSharedPreferencesKeychainCore.contains(ENC_SECRET_KEY))
            return mSharedPreferencesKeychainCore.getString(ENC_SECRET_KEY, "");
        return null;
    }

    @Override
    public void wipeEncryptedSecret() {
        wipeFromKeychain(ENC_SECRET_KEY);
    }

    @Override
    public void storeIvSecret(String iv) {
        if (iv != null) {
            mSharedPreferencesKeychainCore.edit().putString(IV_SECRET_KEY, iv).apply();
        }
    }

    @Override
    public String retrieveIvSecret() {
        if (mSharedPreferencesKeychainCore.contains(IV_SECRET_KEY))
            return mSharedPreferencesKeychainCore.getString(IV_SECRET_KEY, "");
        return null;
    }


    @Override
    public void wipeIvSecret() {
        wipeFromKeychain(IV_SECRET_KEY);
    }

    @Override
    public void storeEnvironmentHash(String hash) {
        if (hash != null) {
            mSharedPreferencesKeychainCore.edit().putString(ENV_HASH_KEY, hash).apply();
        }
    }

    @Override
    public String retrieveEnvironmentHash() {
        if (mSharedPreferencesKeychainCore.contains(ENV_HASH_KEY))
            return mSharedPreferencesKeychainCore.getString(ENV_HASH_KEY, "");
        return null;
    }

    @Override
    public void clearAll() {
        mSharedPreferencesKeychainCore.edit().clear().apply();
        mLogManager.log(StringUtils.logLine(Locker.LOCKER_MODULE, "KeychainWipe", "Keychain was successfully wiped."), LogLevel.INFO);
    }

    @Override
    public void clearVolatileKeys() {
        mSek = null;
        mEk = null;
    }

    private void encryptToKeychain(String key, String value, byte[] ek) {
        if (value != null) {
            byte[] encryptedValue = mCryptoManager.encryptAES(value, ek, null);
            if (encryptedValue != null)
                mSharedPreferencesKeychainCore.edit().putString(key, mCryptoManager.encodeBase64(encryptedValue)).apply();
        }
    }

    private String decryptFromKeychain(String key, byte[] ek) {
        if (mSharedPreferencesKeychainCore.contains(key))
            return mCryptoManager.decryptAES(mCryptoManager.decodeBase64(mSharedPreferencesKeychainCore.getString(key, "")), ek, null);
        return null;
    }

    private void wipeFromKeychain(String key) {
        mSharedPreferencesKeychainCore.edit().remove(key).apply();
    }
}

package cz.csas.cscore.locker;

import android.content.Context;

import cz.csas.cscore.client.crypto.CryptoManager;
import cz.csas.cscore.error.CsCoreError;
import cz.csas.cscore.logger.LogManager;
import cz.csas.cscore.storage.KeychainManager;

/**
 * The type Locker factory.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13 /11/15.
 */
public class LockerFactory {

    private static Locker locker;

    /**
     * Create locker locker.
     *
     * @param keychainManager the keychain manager
     * @param lockerConfig    the locker config
     * @param cryptoManager   the crypto manager
     * @param logManager      the log manager
     * @param context         the context
     * @return the locker
     */
    public static Locker createLocker(KeychainManager keychainManager, LockerConfig lockerConfig, CryptoManager cryptoManager, LogManager logManager, Context context) {
        locker = new LockerImpl(keychainManager, cryptoManager, lockerConfig, logManager, context);
        return locker;
    }

    /**
     * Create locker locker.
     *
     * @return the locker
     */
    static Locker createLocker() {
        if (locker == null)
            throw new CsCoreError(CsCoreError.Kind.BAD_LOCKER_INITIALIZATION);
        return locker;
    }
}

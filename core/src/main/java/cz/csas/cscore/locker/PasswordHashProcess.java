package cz.csas.cscore.locker;

import cz.csas.cscore.client.rest.CsCallback;

/**
 * Password hash process is required for
 * {@link Locker#unlockAfterMigration(Password, PasswordHashProcess, LockerMigrationData, CsCallback)}
 * to handle custom password hashing before migration.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 15/09/2017.
 */
public interface PasswordHashProcess {

    /**
     * Transform password according to used hashing algorithm with appropriate salt.
     *
     * @param password in a raw format before hashing
     * @return hashed password
     */
    public String hashPassword(String password);
}

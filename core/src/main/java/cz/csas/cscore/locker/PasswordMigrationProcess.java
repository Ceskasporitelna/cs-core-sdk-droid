package cz.csas.cscore.locker;

import cz.csas.cscore.client.rest.CsCallback;

/**
 * Password migration process is required for
 * {@link Locker#unlockAfterMigration(Password, PasswordMigrationProcess, LockerMigrationData, CsCallback)}
 * to handle custom password hashing before migration and password transform during migration.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 15/09/2017.
 */
public interface PasswordMigrationProcess {

    /**
     * Transform password according to used hashing algorithm with appropriate salt.
     *
     * @param password in a raw format before hashing
     * @return hashed password
     */
    public String hashPassword(String password);

    /**
     * Migrate old password string to locker type password string.
     * Required only for lock types {@link LockType#GESTURE} and {@link LockType#PIN}
     * --------------------------------------------------
     * GESTURE format: 00{Y1}-00{X1}&00{Y2}-00{X2}&...
     * where Xn,Yn are x, y (column, row) coordinates from grid of size k x l, where k is number of
     * rows and l is number of columns, n is the gesture length. Also note that origin of the
     * coordinate system is situated into up-left corner.
     * example (gesture grid 4x4, gesture length 4):
     * ---------
     * |0|0|0|0|
     * ---------
     * |0|0|3|4|
     * ---------
     * |0|2|0|0|
     * ---------
     * |1|0|0|0|
     * ---------
     * hash = "003-000&002-001&001-002&001-003"
     * --------------------------------------------------
     * PIN format: "WXYZ"
     * where W is the first hit number, X is the second hit number and so on
     * example (pin length 4):
     * -------------------
     * |1(1.)|2(2.)|3(3.)|
     * |4(4.)|5----|6----|
     * |7----|8----|9----|
     * |-----|0----|-----|
     * -------------------
     * hash = "1234"
     * --------------------------------------------------
     *
     * @param oldPassword old password in a format used in previous implementation
     * @return the new password in a format used in Locker SDK implementation. See above description
     */
    public String transformPassword(String oldPassword);
}

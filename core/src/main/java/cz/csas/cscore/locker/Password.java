package cz.csas.cscore.locker;

/**
 * The type Password.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13 /11/15.
 */
public class Password {

    private LockType lockType;

    private String password;

    private Integer passwordSpaceSize;

    /**
     * Instantiates a new Password. You can choose you LockType (PIN,GESTURE,NONE) and define your
     * password.
     *
     * @param lockType the lock type
     * @param password the password
     */
    public Password(LockType lockType, String password) {
        this.lockType = lockType;
        this.password = password;
        this.passwordSpaceSize = null;
    }

    /**
     * Instantiates a new Password.
     *
     * @param lockType the lock type
     * @param password the password
     * @param passwordSpaceSize the grid size
     */
    public Password(LockType lockType, String password, Integer passwordSpaceSize) {
        this.lockType = lockType;
        this.password = password;
        this.passwordSpaceSize = passwordSpaceSize;
    }

    /**
     * Gets lock type.
     *
     * @return the lock type
     */
    public LockType getLockType() {
        return lockType;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets grid size.
     *
     * @param passwordSpaceSize the grid size
     */
    public void setPasswordSpaceSize(Integer passwordSpaceSize) {
        this.passwordSpaceSize = passwordSpaceSize;
    }

    /**
     * Gets password space size.
     *
     * @return the password space size
     */
    public Integer getPasswordSpaceSize() {
        return passwordSpaceSize;
    }
}

package cz.csas.cscore.locker;

/**
 * Locker data necessary for unlock locker after migration from some older version
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 15/09/2017.
 */
public class LockerMigrationData {

    private LockType lockType;
    private String clientId;
    private String deviceFingerprint;
    private String oneTimePasswordKey;

    public LockerMigrationData(LockType lockType, String clientId, String deviceFingerprint, String oneTimePasswordKey) {
        this.lockType = lockType;
        this.clientId = clientId;
        this.deviceFingerprint = deviceFingerprint;
        this.oneTimePasswordKey = oneTimePasswordKey;
    }

    private LockerMigrationData(Builder builder) {
        this.lockType = builder.lockType;
        this.clientId = builder.clientId;
        this.deviceFingerprint = builder.deviceFingerprint;
        this.oneTimePasswordKey = builder.oneTimePasswordKey;
    }

    public LockType getLockType() {
        return lockType;
    }

    public void setLockType(LockType lockType) {
        this.lockType = lockType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    public void setDeviceFingerprint(String deviceFingerprint) {
        this.deviceFingerprint = deviceFingerprint;
    }

    public String getOneTimePasswordKey() {
        return oneTimePasswordKey;
    }

    public void setOneTimePasswordKey(String oneTimePasswordKey) {
        this.oneTimePasswordKey = oneTimePasswordKey;
    }

    public static class Builder {
        private LockType lockType;
        private String clientId;
        private String deviceFingerprint;
        private String oneTimePasswordKey;

        public Builder setLockType(LockType lockType) {
            this.lockType = lockType;
            return this;
        }

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setDeviceFingerprint(String deviceFingerprint) {
            this.deviceFingerprint = deviceFingerprint;
            return this;
        }

        public Builder setOneTimePasswordKey(String oneTimePasswordKey) {
            this.oneTimePasswordKey = oneTimePasswordKey;
            return this;
        }

        public LockerMigrationData create() {
            return new LockerMigrationData(this);
        }
    }
}

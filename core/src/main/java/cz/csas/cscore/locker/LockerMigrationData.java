package cz.csas.cscore.locker;

/**
 * Locker data necessary for unlock locker after migration from some older version
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 15/09/2017.
 */
public class LockerMigrationData {

    private String clientId;
    private String deviceFingerprint;
    private String encryptionKey;
    private String oneTimePasswordKey;
    private String refreshToken;

    public LockerMigrationData(String clientId, String deviceFingerprint, String encryptionKey, String oneTimePasswordKey, String refreshToken) {
        this.clientId = clientId;
        this.deviceFingerprint = deviceFingerprint;
        this.encryptionKey = encryptionKey;
        this.oneTimePasswordKey = oneTimePasswordKey;
        this.refreshToken = refreshToken;
    }

    private LockerMigrationData(Builder builder) {
        this.clientId = builder.clientId;
        this.deviceFingerprint = builder.deviceFingerprint;
        this.encryptionKey = builder.encryptionKey;
        this.oneTimePasswordKey = builder.oneTimePasswordKey;
        this.refreshToken = builder.refreshToken;
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public static class Builder {
        private String clientId;
        private String deviceFingerprint;
        private String encryptionKey;
        private String oneTimePasswordKey;
        private String refreshToken;

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setDeviceFingerprint(String deviceFingerprint) {
            this.deviceFingerprint = deviceFingerprint;
            return this;
        }

        public Builder setEncryptionKey(String encryptionKey) {
            this.encryptionKey = encryptionKey;
            return this;
        }

        public Builder setOneTimePasswordKey(String oneTimePasswordKey) {
            this.oneTimePasswordKey = oneTimePasswordKey;
            return this;
        }

        public Builder setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public LockerMigrationData create() {
            return new LockerMigrationData(this);
        }
    }
}

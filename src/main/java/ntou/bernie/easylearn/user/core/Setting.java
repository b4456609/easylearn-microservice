package ntou.bernie.easylearn.user.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Embedded
public class Setting {
    private static final Logger LOGGER = LoggerFactory.getLogger(Setting.class);
    @JsonProperty
    boolean wifiSync;
    @JsonProperty
    boolean mobileNetworkSync;
    @JsonProperty
    int version;
    @Transient
    @JsonProperty
    boolean modified;
    @JsonProperty
    long lastSyncTime;

    public Setting(boolean wifiSync, boolean mobileNetworkSync, int version, boolean modified, long lastSyncTime) {
        this.wifiSync = wifiSync;
        this.mobileNetworkSync = mobileNetworkSync;
        this.version = version;
        this.modified = modified;
        this.lastSyncTime = lastSyncTime;
    }

    public Setting() {
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public long getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(long lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public boolean isConflict(int dbVersion) {
        LOGGER.debug("dbVersion:" + dbVersion + ", version:" + this.version + ", modified:" + this.modified + ((dbVersion != version) && modified));
        // user is exist and use new device
        if (version == 0) {
            return false;
        }

        // conflict happen
        if ((dbVersion != version) && modified) {
            LOGGER.info("conflict");

            return true;
        }

        return false;
    }

    /**
     * @return the wifiSync
     */
    @JsonProperty
    public boolean isWifiSync() {
        return wifiSync;
    }

    /**
     * @param wifiSync the wifiSync to set
     */
    @JsonProperty
    public void setWifiSync(boolean wifiSync) {
        this.wifiSync = wifiSync;
    }

    /**
     * @return the mobileNetworkSync
     */
    @JsonProperty
    public boolean isMobileNetworkSync() {
        return mobileNetworkSync;
    }

    /**
     * @param mobileNetworkSync the mobileNetworkSync to set
     */
    @JsonProperty
    public void setMobileNetworkSync(boolean mobileNetworkSync) {
        this.mobileNetworkSync = mobileNetworkSync;
    }

    /**
     * @return the version
     */
    @JsonProperty
    public int getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    @JsonProperty
    public void setVersion(int version) {
        this.version = version;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Setting [wifiSync=" + wifiSync + ", mobileNetworkSync=" + mobileNetworkSync + ", version=" + version
                + ", modified=" + modified + "]";
    }
}

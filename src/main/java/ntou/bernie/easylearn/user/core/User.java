/**
 *
 */
package ntou.bernie.easylearn.user.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author bernie
 */
@Entity
public class User {

    @Transient
    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    @Id
    private ObjectId _id;
    @Indexed(options = @IndexOptions(unique = true))
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonIgnore
    private long createTime;
    @JsonIgnore
    private long lastUpTime;
    @Embedded
    @NotNull
    @JsonProperty
    private Setting setting;
    @Embedded
    @NotNull
    @JsonProperty
    private List<Folder> folder;
    @Embedded
    @NotNull
    @JsonProperty
    private List<Bookmark> bookmark;

    public User() {
    }


    /**
     * @param id
     * @param name
     * @param setting
     * @param folder
     * @param bookmark
     */
    public User(String id, String name, Setting setting, List<Folder> folder,
                List<Bookmark> bookmark) {
        this.id = id;
        this.name = name;
        this.setting = setting;
        this.folder = folder;
        this.bookmark = bookmark;
    }

    /**
     * @return the userId
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the userId to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the createTime
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the lastUpTime
     */
    public long getLastUpTime() {
        return lastUpTime;
    }

    /**
     * @param lastUpTime the lastUpTime to set
     */
    public void setLastUpTime(long lastUpTime) {
        this.lastUpTime = lastUpTime;
    }

    /**
     * @return the setting
     */
    public Setting getSetting() {
        return setting;
    }

    /**
     * @param setting the setting to set
     */
    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    /**
     * @return the folder
     */
    public List<Folder> getFolder() {
        return folder;
    }

    /**
     * @param folder the folder to set
     */
    public void setFolder(List<Folder> folder) {
        this.folder = folder;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "User [_id=" + _id + ", id=" + id + ", name=" + name + ", createTime=" + createTime + ", lastUpTime="
                + lastUpTime + ", setting=" + setting + ", folder=" + folder + ", bookmark=" + bookmark + "]";
    }


    /**
     * @return the bookmark
     */
    public List<Bookmark> getBookmark() {
        return bookmark;
    }


    /**
     * @param bookmark the bookmark to set
     */
    public void setBookmark(List<Bookmark> bookmark) {
        this.bookmark = bookmark;
    }

}

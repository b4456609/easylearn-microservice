package ntou.bernie.easylearn.pack.core;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.UpdateOperations;

import javax.validation.constraints.NotNull;

@Entity
public class Pack {
    @Id
    private ObjectId _id;
    @NotNull
    private String name;
    @NotNull
    private String id;
    @NotNull
    private String description;
    @NotNull
    private String createTime;
    @NotNull
    private String tags;
    @NotNull
    private String isPublic;
    @NotNull
    private String creatorUserId;
    @NotNull
    private String creatorUserName;
    @NotNull
    private String coverFilename;


    @NotNull
    @Embedded
    private List<Version> version;

    /**
     *
     */
    public Pack() {
    }

    /**
     * @param name
     * @param id
     * @param description
     * @param createTime
     * @param tags
     * @param isPublic
     * @param creatorUserId
     * @param creatorUserName
     * @param coverFilename
     * @param version
     * @param file
     */
    public Pack(String name, String id, String description, String createTime, String tags, String isPublic,
                String creatorUserId, String creatorUserName, String coverFilename, List<Version> version,
                Set<String> file) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.createTime = createTime;
        this.tags = tags;
        this.isPublic = isPublic;
        this.creatorUserId = creatorUserId;
        this.creatorUserName = creatorUserName;
        this.coverFilename = coverFilename;
        this.version = version;
    }

    public Version getVersionById(String id) {
        for (Version item : version) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }


    public String getCoverFilename() {
        return coverFilename;
    }

    public void setCoverFilename(String coverFilename) {
        this.coverFilename = coverFilename;
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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return the isPublic
     */
    public String getIsPublic() {
        return isPublic;
    }

    /**
     * @param isPublic the isPublic to set
     */
    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * @return the creatorUserId
     */
    public String getCreatorUserId() {
        return creatorUserId;
    }

    /**
     * @param creatorUserId the creatorUserId to set
     */
    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    /**
     * @return the creatorUserName
     */
    public String getCreatorUserName() {
        return creatorUserName;
    }

    /**
     * @param creatorUserName the creatorUserName to set
     */
    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    /**
     * @return the coverFileName
     */
    public String getCoverFileName() {
        return coverFilename;
    }

    /**
     * @param coverFileName the coverFileName to set
     */
    public void setCoverFileName(String coverFileName) {
        this.coverFilename = coverFileName;
    }

    /**
     * @return the version
     */
    public List<Version> getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(List<Version> version) {
        this.version = version;
    }

}

package ntou.bernie.easylearn.pack.core;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Embedded
public class Version {
    @Transient
    private static final Logger LOGGER = LoggerFactory.getLogger(Version.class);

    @NotNull
    private String id;
    @NotNull
    private String content;
    @NotNull
    private long createTime;
    @NotNull
    private boolean isPublic;
    @NotNull
    private String creatorUserId;
    @NotNull
    private String creatorUserName;
    @NotNull
    private long version;
    @NotNull
    private long viewCount;
    @NotNull
    private long privateId;
    @NotNull
    private String modified;
    @NotNull
    private Set<String> file;
    @NotNull
    private List<String> noteId;

    /**
     *
     */
    public Version() {
    }

    public Version(String id, String content, long createTime, boolean isPublic, String creatorUserId, String creatorUserName, int version, long viewCount, long privateId, String modified, Set<String> file, List<String> noteId) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.isPublic = isPublic;
        this.creatorUserId = creatorUserId;
        this.creatorUserName = creatorUserName;
        this.version = version;
        this.viewCount = viewCount;
        this.privateId = privateId;
        this.modified = modified;
        this.file = file;
        this.noteId = noteId;
    }

    public List<String> getNoteId() {
        return noteId;
    }

    public void setNoteId(List<String> noteId) {
        this.noteId = noteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Version version1 = (Version) o;

        if (isPublic != version1.isPublic) return false;
        if (version != version1.version) return false;
        if (viewCount != version1.viewCount) return false;
        if (privateId != version1.privateId) return false;
        if (!id.equals(version1.id)) return false;
        if (!content.equals(version1.content)) return false;
        if (createTime != createTime) return false;
        if (!creatorUserId.equals(version1.creatorUserId)) return false;
        if (!creatorUserName.equals(version1.creatorUserName)) return false;
        if (modified != null ? !modified.equals(version1.modified) : version1.modified != null) return false;
        if (file != null ? !file.equals(version1.file) : version1.file != null) return false;
        return noteId != null ? noteId.equals(version1.noteId) : version1.noteId == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + (isPublic ? 1 : 0);
        result = 31 * result + creatorUserId.hashCode();
        result = 31 * result + creatorUserName.hashCode();
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (int) (viewCount ^ (viewCount >>> 32));
        result = 31 * result + (int) (privateId ^ (privateId >>> 32));
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        result = 31 * result + (file != null ? file.hashCode() : 0);
        result = 31 * result + (noteId != null ? noteId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Version{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", isPublic='" + isPublic + '\'' +
                ", creatorUserId='" + creatorUserId + '\'' +
                ", creatorUserName='" + creatorUserName + '\'' +
                ", version=" + version +
                ", viewCount=" + viewCount +
                ", privateId='" + privateId + '\'' +
                '}';
    }

    public void sync(Version dbVersion) {
        this.content = syncNote(dbVersion.content);
    }


    public String syncNote(String dbContent) {
        if (content.equals(dbContent)) {
            return dbContent;
        }


        StringBuffer dbContentBuffer = new StringBuffer(dbContent);
        StringBuffer contentBuffer = new StringBuffer(content);

        LOGGER.debug("dbContentBuffer is {} contentBuffer is {}", dbContentBuffer, contentBuffer);

        int index = 0;
        int clientIndex = contentBuffer
                .indexOf("<span class=\"note", index);
        int dbIndex = dbContentBuffer.indexOf("<span class=\"note", index);

        if (clientIndex == -1 && dbIndex == -1)
            return dbContent;
        else if (clientIndex >= 0 && dbIndex == -1) {
            // client has newer versin
            return content;
        } else if (clientIndex == -1 && dbIndex >= 0) {
            // return do nothing
            // db has new
            return dbContent;
        }

        while (true) {
            clientIndex = contentBuffer
                    .indexOf("<span class=\"note", index);
            dbIndex = dbContentBuffer.indexOf("<span class=\"note", index);
            LOGGER.debug("clientIndex is {}", clientIndex);
            LOGGER.debug("dbIndex is {}", dbIndex);
            if (clientIndex == -1 && dbIndex == -1) {
                // result
                //db.updateVersion(id, contentBuffer.toString());
                return dbContentBuffer.toString();
            } else if (clientIndex != -1 && dbIndex == -1) {
                versionInsert(index, clientIndex, contentBuffer,
                        dbContentBuffer);
            } else if (clientIndex == -1 && dbIndex != -1) {
                versionInsert(index, dbIndex, dbContentBuffer,
                        contentBuffer);
            } else if (clientIndex == dbIndex) {
                index = clientIndex + 1;
            } else if (clientIndex < dbIndex) {
                versionInsert(index, clientIndex, contentBuffer,
                        dbContentBuffer);
            } else if (clientIndex > dbIndex) {
                versionInsert(index, dbIndex, dbContentBuffer,
                        contentBuffer);
            } else {
                System.out.println("error");
            }
        }


        /*Document parse = Jsoup.parse(content, "", Parser.xmlParser());
        Document dbparse = Jsoup.parse(dbContent, "", Parser.xmlParser());

        Elements notes = parse.getElementsByClass("note");
        Elements dbnotes = dbparse.getElementsByClass("note");


        Set<String> noteId = new HashSet<String>();
        Set<String> dbNoteId = new HashSet<String>();


        for (Element element : notes) {
            Attributes attributes = element.attributes();
            noteId.add(attributes.get("noteid"));
        }

        for (Element element : dbnotes) {
            Attributes attributes = element.attributes();
            dbNoteId.add(attributes.get("noteid"));

        }


        LOGGER.debug(noteId.toString());
        LOGGER.debug(dbNoteId.toString());

        Set<String> dbNoteIdAdd = new HashSet<String>();
        dbNoteIdAdd.addAll(dbNoteId);
        dbNoteIdAdd.addAll(noteId);
        dbNoteIdAdd.removeAll(dbNoteId);

        //no need to add note
        if (dbNoteIdAdd.size() == 0) {
            return dbContent;
        }

        LOGGER.debug(dbNoteIdAdd.toString());

        for (String id : dbNoteIdAdd) {
            LOGGER.debug(id);
            Element noteParent = parse.getElementsByAttributeValue("noteid", id).parents().get(0);
            LOGGER.debug(noteParent.html());
        }*/
    }

    private int versionInsert(int index, int clientIndex,
                              StringBuffer contentBuffer, StringBuffer dbContentBuffer) {
        index = clientIndex;
        int last = contentBuffer.indexOf(">", index);
        String newStr = contentBuffer.substring(index, last + 1);
        dbContentBuffer.insert(index, newStr);
        index = last;

        index = contentBuffer.indexOf("</span>", index);
        // deal with last index
        dbContentBuffer.insert(index, "</span>");
        index = index + 7;
        return index;
    }

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
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
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
     * @return the isPublic
     */
    public boolean getIsPublic() {
        return isPublic;
    }

    /**
     * @param isPublic the isPublic to set
     */
    public void setIsPublic(boolean isPublic) {
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
     * @return the version
     */
    public long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return the viewCount
     */
    public long getViewCount() {
        return viewCount;
    }

    /**
     * @param viewCount the viewCount to set
     */
    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * @return the privateId
     */
    public long getPrivateId() {
        return privateId;
    }

    /**
     * @param privateId the privateId to set
     */
    public void setPrivateId(long privateId) {
        this.privateId = privateId;
    }


    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Set<String> getFile() {
        return file;
    }

    public void setFile(Set<String> file) {
        this.file = file;
    }
}

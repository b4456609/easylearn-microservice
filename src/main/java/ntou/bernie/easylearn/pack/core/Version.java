package ntou.bernie.easylearn.pack.core;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
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
    private String createTime;
    @NotNull
    private String isPublic;
    @NotNull
    private String creatorUserId;
    @NotNull
    private String creatorUserName;
    @NotNull
    private int version;
    @NotNull
    private long viewCount;
    @NotNull
    private String private_id;
    @NotNull
    private String modified;
    @NotNull
    private Set<String> file;


    /**
     *
     */
    public Version() {
    }

    /**
     * @param id
     * @param content
     * @param createTime
     * @param isPublic
     * @param creatorUserId
     * @param creatorUserName
     * @param version
     * @param viewCount
     * @param private_id
     * @param file
     */
    public Version(String id, String content, String createTime, String isPublic, String creatorUserId,
                   String creatorUserName, int version, long viewCount, String private_id, Set<String> file) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.isPublic = isPublic;
        this.creatorUserId = creatorUserId;
        this.creatorUserName = creatorUserName;
        this.version = version;
        this.viewCount = viewCount;
        this.private_id = private_id;
        this.file = file;
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
                ", private_id='" + private_id + '\'' +
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
     * @return the version
     */
    public int getVersion() {
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
     * @return the private_id
     */
    public String getPrivate_id() {
        return private_id;
    }

    /**
     * @param private_id the private_id to set
     */
    public void setPrivate_id(String private_id) {
        this.private_id = private_id;
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

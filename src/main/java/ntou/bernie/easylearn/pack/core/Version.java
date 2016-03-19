package ntou.bernie.easylearn.pack.core;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Transient
    private long userViewCount;
    @NotNull
    private String privateId;
    @NotNull
    private String modified;
    @NotNull
    private Set<String> file;
    @NotNull
    private List<String> note;

    /**
     *
     */
    public Version() {
    }

    public Version(String id, String content, long createTime, boolean isPublic, String creatorUserId, String creatorUserName, long version, long viewCount, long userViewCount, String privateId, String modified, Set<String> file, List<String> note) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.isPublic = isPublic;
        this.creatorUserId = creatorUserId;
        this.creatorUserName = creatorUserName;
        this.version = version;
        this.viewCount = viewCount;
        this.userViewCount = userViewCount;
        this.privateId = privateId;
        this.modified = modified;
        this.file = file;
        this.note = note;
    }

    public void sync(Version dbVersion) {
        //update view count
        viewCount += userViewCount;
        userViewCount = 0;
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


        Set<String> note = new HashSet<String>();
        Set<String> dbNoteId = new HashSet<String>();


        for (Element element : notes) {
            Attributes attributes = element.attributes();
            note.add(attributes.get("noteid"));
        }

        for (Element element : dbnotes) {
            Attributes attributes = element.attributes();
            dbNoteId.add(attributes.get("noteid"));

        }


        LOGGER.debug(note.toString());
        LOGGER.debug(dbNoteId.toString());

        Set<String> dbNoteIdAdd = new HashSet<String>();
        dbNoteIdAdd.addAll(dbNoteId);
        dbNoteIdAdd.addAll(note);
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

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @JsonProperty("is_public")
    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public long getUserViewCount() {
        return userViewCount;
    }

    public void setUserViewCount(long userViewCount) {
        this.userViewCount = userViewCount;
    }

    public String getPrivateId() {
        return privateId;
    }

    public void setPrivateId(String privateId) {
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

    public List<String> getNote() {
        return note;
    }

    public void setNote(List<String> note) {
        this.note = note;
    }
}

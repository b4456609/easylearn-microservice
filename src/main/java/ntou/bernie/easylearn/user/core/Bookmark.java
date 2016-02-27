package ntou.bernie.easylearn.user.core;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Bookmark {
    private String id;
    private String versionId;
    private String name;

    /**
     *
     */
    public Bookmark() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param versionId
     * @param name
     */
    public Bookmark(String id, String versionId, String name) {
        this.id = id;
        this.versionId = versionId;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Bookmark [id=" + id + ", name=" + name + "]";
    }

    /**
     * @return the versionId
     */
    public String getVersionId() {
        return versionId;
    }

    /**
     * @param versionId the versionId to set
     */
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

}

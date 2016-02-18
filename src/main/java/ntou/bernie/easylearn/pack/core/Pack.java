package ntou.bernie.easylearn.pack.core;

import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class Pack {
	@Id
	private ObjectId _id;
	private String name;
	private String id;
	private String description;
	private String createTime;
	private Set<String> tags;
	private String isPublic;
	private String creatorUserId;
	private String creatorUserName;
	private String coverFilename;
	@Embedded
	private List<Version> versions;
	private Set<String> file;
	/**
	 * 
	 */
	public Pack() {
		// TODO Auto-generated constructor stub
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
	 * @param versions
	 * @param file
	 */
	public Pack(String name, String id, String description, String createTime, Set<String> tags, String isPublic,
			String creatorUserId, String creatorUserName, String coverFilename, List<Version> versions,
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
		this.versions = versions;
		this.file = file;
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
	public Set<String> getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(Set<String> tags) {
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
	 * @return the versions
	 */
	public List<Version> getVersions() {
		return versions;
	}
	/**
	 * @param versions the versions to set
	 */
	public void setVersions(List<Version> versions) {
		this.versions = versions;
	}
	/**
	 * @return the file
	 */
	public Set<String> getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(Set<String> file) {
		this.file = file;
	}
	
	
}

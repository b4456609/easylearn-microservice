package ntou.bernie.easylearn.pack.core;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Version {
	private String id;
	private String content;
	private String createTime;
	private String isPublic;
	private String creatorUserId;
	private String creatorUserName;
	private int version;
	private long viewCount;
	private String private_id;

	/**
	 * 
	 */
	public Version() {
		// TODO Auto-generated constructor stub
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
	 */
	public Version(String id, String content, String createTime, String isPublic, String creatorUserId,
			String creatorUserName, int version, long viewCount, String private_id) {
		this.id = id;
		this.content = content;
		this.createTime = createTime;
		this.isPublic = isPublic;
		this.creatorUserId = creatorUserId;
		this.creatorUserName = creatorUserName;
		this.version = version;
		this.viewCount = viewCount;
		this.private_id = private_id;
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
	
}

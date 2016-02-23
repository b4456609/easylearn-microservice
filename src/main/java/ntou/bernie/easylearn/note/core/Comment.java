package ntou.bernie.easylearn.note.core;

import com.google.common.base.Optional;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Embedded;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Embedded
public class Comment {
	@NotEmpty
	private String id;
	@NotEmpty
	private String content;
	@NotEmpty
	private String createTime;
	@NotEmpty
	private String userId;
	@NotEmpty
	private String userName;

	/**
	 * 
	 */
	public Comment() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param content
	 * @param createTime
	 * @param userId
	 * @param userName
	 */
	@JsonCreator
	public Comment(@JsonProperty("id") String id, @JsonProperty("content") String content,
			@JsonProperty("create_time") String createTime, @JsonProperty("user_id") String userId,
			@JsonProperty("user_name") String userName) {
		this.id = id;
		this.content = content;
		this.createTime = createTime;
		this.userId = userId;
		this.userName = userName;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param content
	 *            the content to set
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
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

}

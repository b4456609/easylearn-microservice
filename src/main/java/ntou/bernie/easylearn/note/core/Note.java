package ntou.bernie.easylearn.note.core;

import java.util.List;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.jackson.JsonSnakeCase;

@Entity
@JsonSnakeCase
public class Note {
	@Id
	private ObjectId _id;
	@NotEmpty
	@Indexed(options = @IndexOptions(unique = true))
	private String id;
	@NotEmpty
	private String content;
	@NotEmpty
	private String createTime;
	@NotEmpty
	private String userId;
	@NotEmpty
	private String userName;
	@NotEmpty
	private String versionId;
	@Embedded
	@NotEmpty
	private List<Comment> comments;

	/**
	 * 
	 */
	public Note() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param content
	 * @param createTime
	 * @param userId
	 * @param userName
	 * @param versionId
	 * @param comments
	 */
	@JsonCreator
	public Note(@JsonProperty("id") String id, @JsonProperty("content") String content,
			@JsonProperty("create_time") String createTime, @JsonProperty("user_id") String userId,
			@JsonProperty("user_name") String userName, @JsonProperty("version_id") String versionId,
			@JsonProperty("comment") List<Comment> comments) {
		this.id = id;
		this.content = content;
		this.createTime = createTime;
		this.userId = userId;
		this.userName = userName;
		this.versionId = versionId;
		this.comments = comments;
	}

	/**
	 * @return the noteId
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param noteId
	 *            the noteId to set
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

	/**
	 * @return the versionId
	 */
	public String getVersionId() {
		return versionId;
	}

	/**
	 * @param versionId
	 *            the versionId to set
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}

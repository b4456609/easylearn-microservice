package ntou.bernie.easylearn.image.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by bernie on 2016/3/23.
 */
@Entity
public class ImgItem {
    @Id
    private ObjectId _id;
    @JsonProperty
    private String id;
    @JsonProperty
    private String deletehash;

    public ImgItem() {
    }

    public ImgItem(String id, String deletehash) {
        this.id = id;
        this.deletehash = deletehash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeletehash() {
        return deletehash;
    }

    public void setDeletehash(String deletehash) {
        this.deletehash = deletehash;
    }
}

package ntou.bernie.easylearn.user.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongodb.morphia.annotations.Embedded;

import javax.validation.constraints.NotNull;
import java.util.List;


@Embedded
public class Folder {
    @JsonProperty
    @NotNull
    private String id;
    @JsonProperty
    @NotNull
    private String name;
    @JsonProperty
    @NotNull
    private List<String> pack;

    public Folder() {
    }

    /**
     * @param id
     * @param name
     * @param pack
     */
    public Folder(String id, String name, List<String> pack) {
        this.id = id;
        this.name = name;
        this.pack = pack;
    }


    /**
     * @return the id
     */
    @JsonProperty
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @JsonProperty
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    @JsonProperty
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the pack
     */
    public List<String> getPack() {
        return pack;
    }


    /**
     * @param pack the pack to set
     */
    public void setPack(List<String> pack) {
        this.pack = pack;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Folder [id=" + id + ", name=" + name + ", pack=" + pack + "]";
    }


}

package ntou.bernie.easylearn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.MongoClient;
import org.hibernate.validator.constraints.NotEmpty;

public class DatabaseConfiguration {
    @NotEmpty
    private String host;
    private int port;

    /**
     * @return the host
     */
    @JsonProperty
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    @JsonProperty
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    @JsonProperty
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    @JsonProperty
    public void setPort(int port) {
        this.port = port;
    }

    public MongoClient getMongoClient() throws Exception {
        return new MongoClient(getHost(), getPort());
    }
}

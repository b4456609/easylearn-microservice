package ntou.bernie.easylearn.pack.resource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.dropwizard.testing.junit.ResourceTestRule;
import ntou.bernie.easylearn.pack.client.PackNoteClient;
import ntou.bernie.easylearn.pack.client.PackUserClient;
import ntou.bernie.easylearn.pack.core.Pack;
import ntou.bernie.easylearn.pack.db.PackDAO;
import ntou.bernie.easylearn.user.db.UserDAO;
import ntou.bernie.easylearn.user.resource.UserResource;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by bernie on 2016/2/26.
 */
public class PackResourceTest {
    private ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

    private static PackDAO packDAO = mock(PackDAO.class);
    private static PackUserClient packUserClient = mock(PackUserClient.class);
    private static PackNoteClient packNoteClient = mock(PackNoteClient.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new PackResource(packDAO, packUserClient, packNoteClient))
            .build();
    @Test
    public void testSyncPacks() throws Exception {
        String json = "{\"user\":{\"id\":\"1009840175700426\",\"name\":\"范振原\",\"setting\":{\"wifi_sync\":true,\"mobile_network_sync\":true,\"last_sync_time\":1455874940000,\"modified\":false,\"version\":26}},\"folder\":[{\"name\":\"全部的懶人包\",\"id\":\"allfolder\",\"pack\":[\"pack1439355907459\",\"pack1439367500493\",\"pack1439370245981\",\"pack1439372921598\",\"pack1439381800612\",\"pack1439385129482\",\"pack1439394796784\",\"pack1439451391246\",\"pack1439471856230\"]},{\"name\":\"All\",\"id\":\"allPackId\",\"pack\":[\"pack1439355907459\",\"pack1439370245981\",\"pack1439372921598\",\"pack1439381800612\",\"pack1439385129482\",\"pack1439394796784\",\"pack1450275282853\",\"pack1450347155192\"]},{\"name\":\"我的最愛\",\"id\":\"fjoeiwjowfe\",\"pack\":[]},{\"name\":\"與你分享懶人包\",\"id\":\"shareFolder\",\"pack\":[\"pack1439394796784\"]}],\"pack1439355907459\":{\"creator_user_id\":\"1009840175700426\",\"cover_filename\":\"62oLIgA.jpg\",\"create_time\":1439355907000,\"creator_user_name\":\"范振原\",\"name\":\"一次了解「八仙樂園粉塵爆炸事件」\",\"is_public\":true,\"description\":\"水上樂園粉塵爆炸造成500多人灼傷，打破台灣救災史一次燒燙傷人數的最新紀錄\",\"version\":[{\"creator_user_id\":\"1009840175700426\",\"note\":[],\"create_time\":1439357781000,\"pack_id\":\"pack1439355907459\",\"user_view_count\":0,\"version\":0,\"content\":\"<p><s</ol>\",\"bookmark\":[],\"file\":[\"AiJQ1L5.jpg\",\"cwvI1zQ.jpg\",\"FkUPHMQ.gif\",\"jtZHxVo.jpg\",\"MBodnQg.jpg\",\"OOxshqA.jpg\",\"PFIDg7a.jpg\",\"Sada0o9.jpg\",\"TIXHiEy.gif\"],\"creator_user_name\":\"范振原\",\"is_public\":true,\"modified\":\"false\",\"private_id\":\"\",\"id\":\"version1439355907607\",\"view_count\":6},{\"creator_user_id\":\"1009840175700426\",\"note\":[],\"create_time\":1439358382000,\"pack_id\":\"pack1439355907459\",\"user_view_count\":0,\"version\":0,\"content\":\"<p>\",\"bookmark\":[],\"file\":[],\"creator_user_name\":\"范振原\",\"is_public\":true,\"modified\":\"false\",\"private_id\":\"\",\"id\":\"version1439357056319\",\"view_count\":12}],\"tags\":\"\"},\"pack1439367500493\":{\"creator_user_id\":\"10204250001235141\",\"cover_filename\":\"F2BqN25.jpg\",\"create_time\":1439367500000,\"creator_user_name\":\"莊晏\",\"name\":\"號外號外~4G方案大解析\",\"is_public\":true,\"description\":\"4G\",\"version\":[{\"creator_user_id\":\"10204250001235141\",\"note\":[],\"create_time\":1439367500000,\"pack_id\":\"pack1439367500493\",\"user_view_count\":0,\"version\":0,\"content\":\"fjiewfwef\",\"bookmark\":[],\"file\":[\"6iVwceH.jpg\",\"C5TVlUo.jpg\",\"g6lD5Gg.jpg\",\"lgbsg6A.jpg\",\"RpeUmdN.jpg\"],\"creator_user_name\":\"莊晏\",\"is_public\":true,\"modified\":\"false\",\"private_id\":\"\",\"id\":\"version1439367500497\",\"view_count\":0}],\"tags\":\"\"}}";

        when(packUserClient.getUserFolder(anyString())).thenReturn("[{\"id\":\"id\",\"name\":\"name\",\"pack\":[\"pack\",\"pasck\"]},{\"id\":\"ids\",\"name\":\"namse\",\"pack\":[\"pasck\",\"paack\"]}]");

        Response result = resources.client().target("/pack/sync").request().post(Entity.json(json));

        verify(packNoteClient, times(2)).syncNote(anyString());
        assertThat(result.getStatus(), is(200));
    }
}
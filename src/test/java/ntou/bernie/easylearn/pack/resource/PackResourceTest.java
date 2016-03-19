package ntou.bernie.easylearn.pack.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.type.ReferenceType;
import io.dropwizard.testing.junit.ResourceTestRule;
import ntou.bernie.easylearn.pack.client.PackNoteClient;
import ntou.bernie.easylearn.pack.client.PackUserClient;
import ntou.bernie.easylearn.pack.core.Pack;
import ntou.bernie.easylearn.pack.db.PackDAO;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by bernie on 2016/2/26.
 */
public class PackResourceTest {
    private static PackDAO packDAO = mock(PackDAO.class);
    private static PackUserClient packUserClient = mock(PackUserClient.class);
    private static PackNoteClient packNoteClient = mock(PackNoteClient.class);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new PackResource(packDAO, packUserClient, packNoteClient))
            .build();
    private ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

    @After
    public void tearDown() {
        // we have to reset the mock after each test because of the
        // @ClassRule, or use a @Rule as mentioned below.
        reset(packDAO);
        reset(packUserClient);
        reset(packNoteClient);
    }

    @Test
    public void testSyncPacks() throws Exception {

        Response result = resources.client().target("/pack/sync").request().post(Entity.json(json));

        assertThat(result.getStatus(), is(200));
    }

    @Test
    public void getUserPacks() throws Exception {
        when(packUserClient.getUserPacks(any(),any())).thenReturn(Collections.emptyList());
        List<Pack> packs = objectMapper.readValue(fixture("pack/packNonNote.json"), new TypeReference<List<Pack>>() {
        });
        when(packDAO.getPacksById(any())).thenReturn(packs);
        when(packNoteClient.getNoteByVersionId(any(), any())).thenReturn(objectMapper.readTree(fixture("note/notes.json")));

        Response result = resources.client().target("/pack/user/userid").request().get();

        String json = result.readEntity(String.class);
        String expect = fixture("pack/packWithNote.json");

        JsonNode expected = objectMapper.readTree(expect);
        JsonNode actual = objectMapper.readTree(json);

        assertThat(result.getStatus(), is(200));
        assertTrue(expected.equals(actual));
    }
}
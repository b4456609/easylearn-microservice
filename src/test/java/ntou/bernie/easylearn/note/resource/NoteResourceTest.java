package ntou.bernie.easylearn.note.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import ntou.bernie.easylearn.note.core.Note;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by bernie on 2016/2/19.
 */
public class NoteResourceTest {
    @Test
    public void testJsonPath() throws IOException {
        String json = "{\n" +
                "  \"id\": \"1009840175700426\",\n" +
                "  \"creator_user_id\": \"1009840175700426\",\n" +
                "  \"cover_filename\": \"AgkHDg6.jpg\",\n" +
                "  \"create_time\": 1439381801000,\n" +
                "  \"creator_user_name\": \"范振原\",\n" +
                "  \"name\": \"Fires in California\",\n" +
                "  \"is_public\": true,\n" +
                "  \"description\": \"In five days, this raging wildfire has doubled in size.\",\n" +
                "  \"tags\": \"\",\n" +
                "  \"version\": [\n" +
                "    {\n" +
                "      \"creator_user_id\": \"1009840175700426\",\n" +
                "      \"note\": [\n" +
                "        {\n" +
                "          \"create_time\": 1439381999000,\n" +
                "          \"user_id\": \"1009840175700426\",\n" +
                "          \"user_name\": \"范振原\",\n" +
                "          \"comment\": [],\n" +
                "          \"id\": \"note1439381999482\",\n" +
                "          \"content\": \"憤怒的\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"create_time\": 1439382017000,\n" +
                "          \"user_id\": \"1009840175700426\",\n" +
                "          \"user_name\": \"范振原\",\n" +
                "          \"comment\": [\n" +
                "            {\n" +
                "              \"note_id\": \"note1439382017021\",\n" +
                "              \"create_time\": 1439382101000,\n" +
                "              \"user_id\": \"1009840175700426\",\n" +
                "              \"user_name\": \"范振原\",\n" +
                "              \"id\": \"comment1439382101148\",\n" +
                "              \"content\": \"好可怕\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"note_id\": \"note1439382017021\",\n" +
                "              \"create_time\": 1439382245000,\n" +
                "              \"user_id\": \"1030089010343091\",\n" +
                "              \"user_name\": \"Erin Fan\",\n" +
                "              \"id\": \"comment1439382245078\",\n" +
                "              \"content\": \"對啊\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"id\": \"note1439382017021\",\n" +
                "          \"content\": \"燒焦的\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"create_time\": 1439382427000,\n" +
                "          \"user_id\": \"1009840175700426\",\n" +
                "          \"user_name\": \"范振原\",\n" +
                "          \"comment\": [\n" +
                "            {\n" +
                "              \"note_id\": \"note1439382422748\",\n" +
                "              \"create_time\": 1439388559000,\n" +
                "              \"user_id\": \"1030089010343091\",\n" +
                "              \"user_name\": \"Erin Fan\",\n" +
                "              \"id\": \"comment1439388559166\",\n" +
                "              \"content\": \"火焰好大\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"note_id\": \"note1439382422748\",\n" +
                "              \"create_time\": 1439388605000,\n" +
                "              \"user_id\": \"1009840175700426\",\n" +
                "              \"user_name\": \"范振原\",\n" +
                "              \"id\": \"comment1439388605368\",\n" +
                "              \"content\": \"對啊\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"id\": \"note1439382422748\",\n" +
                "          \"content\": \"火焰\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"create_time\": 1439382550000,\n" +
                "          \"user_id\": \"1009840175700426\",\n" +
                "          \"user_name\": \"范振原\",\n" +
                "          \"comment\": [\n" +
                "            {\n" +
                "              \"note_id\": \"note1439382542987\",\n" +
                "              \"create_time\": 1439382577000,\n" +
                "              \"user_id\": \"1030089010343091\",\n" +
                "              \"user_name\": \"Erin Fan\",\n" +
                "              \"id\": \"comment1439382577382\",\n" +
                "              \"content\": \"這個是灌木嗎\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"note_id\": \"note1439382542987\",\n" +
                "              \"create_time\": 1439382608000,\n" +
                "              \"user_id\": \"1009840175700426\",\n" +
                "              \"user_name\": \"范振原\",\n" +
                "              \"id\": \"comment1439382608143\",\n" +
                "              \"content\": \"是的\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"note_id\": \"note1439382542987\",\n" +
                "              \"create_time\": 1439390008000,\n" +
                "              \"user_id\": \"1030089010343091\",\n" +
                "              \"user_name\": \"Erin Fan\",\n" +
                "              \"id\": \"comment1439390008174\",\n" +
                "              \"content\": \"灌木不是bush嗎\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"note_id\": \"note1439382542987\",\n" +
                "              \"create_time\": 1439390032000,\n" +
                "              \"user_id\": \"1009840175700426\",\n" +
                "              \"user_name\": \"范振原\",\n" +
                "              \"id\": \"comment1439390032644\",\n" +
                "              \"content\": \"對阿 這個也是同樣的意思\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"id\": \"note1439382542987\",\n" +
                "          \"content\": \"灌木\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"create_time\": 1439381801000,\n" +
                "      \"pack_id\": \"pack1439381800612\",\n" +
                "      \"user_view_count\": 0,\n" +
                "      \"version\": 0,\n" +
                "      \"content\": \"<div id=\\\"MBRcIYG350g\\\" class=\\\"youtube video-container\\\"><iframe src=\\\"http://www.youtube.com/embed/MBRcIYG350g?controls=1&amp;disablekb=1&amp;modestbranding=1&amp;showinfo=0&amp;rel=0\\\" width=\\\"560\\\" height=\\\"315\\\" frameborder=\\\"0\\\" allowfullscreen=\\\"\\\"></iframe></div>\\n<p>In five days, this <strong><span class=\\\"note note-teal\\\" noteid=\\\"note1439381999482\\\" title=\\\"憤怒的\\\">raging</span></strong> wildfire has doubled in size. It’s left two dozen homes in <strong><span class=\\\"note note-indigo\\\" noteid=\\\"note1439382017021\\\">charred</span></strong> ruins and thousands of people have had to leave. The fire has <strong>scorched</strong> about 54,000 acres in an area east of Lower Lake, which is about 110 miles (177 kilometres) north of San Francisco.</p>\\n<p>It's the worst of 20 large fires being battled by nine thousand firefighters across the state. A separate <strong><span class=\\\"note note-orange\\\" noteid=\\\"note1439382422748\\\">blaze</span></strong> that killed a US forest ranger on Thursday near the Oregon border has also expanded, but remains a fraction of the size.</p>\\n<p>Some 20,000 acres of <strong><span class=\\\"note note-purple\\\" noteid=\\\"note1439382542987\\\">shrub</span></strong> oak and birch were <strong>ravaged</strong> by the fire, over a five-hour period on Saturday night. By Sunday evening, the fire had blackened another 7,000 acres along eastern <strong>flanks</strong> of California's northern coast <strong>ranges</strong>.</p>\\n<p>Authorities said the fire destroyed 24 homes and 26 <strong>outbuildings</strong> and continues to threaten a further 6,300 structures. More than 12,000 people have received <strong>mandatory</strong> evacuation orders. 2,070 personnel which is around a third of the state's force were battling the fire on Sunday evening.</p>\\n<p><img id=\\\"AmB1wt8\\\" class=\\\"slideshare-img fluffy22_california-wildfires-51282566 \\\" style=\\\"max-width: 100% !important; height: auto;\\\" src=\\\"FILE_STORAGE_PATHpack1439381800612/AmB1wt8.jpg\\\" alt=\\\"\\\"><br><br>Read more: <a href=\\\"http://www.newsinlevels.com/products/fires-in-california-level-3/?utm_source=copy&amp;utm_medium=paste&amp;utm_campaign=copypaste&amp;utm_content=http%3A%2F%2Fwww.newsinlevels.com%2Fproducts%2Ffires-in-california-level-3%2F\\\">http://www.newsinlevels.com/products/fires-in-california-level-3/</a></p><div id=\\\"pack_refrence\\\"><h1>引用資料</h1><h2>Youtube</h2><ol><li><a href=\\\"#\\\" onclick=\\\"window.open('http://www.youtube.com/watch?v=MBRcIYG350g', '_system');\\\">Wildfire Jumps Highway in California and Vehicles Catch Fire | World News Tonight | ABC News</a></li></ol><h2>Slideshare</h2><ol><li><a href=\\\"#\\\" onclick=\\\"window.open('http://www.slideshare.net/fluffy22/california-wildfires-51282566', '_system');\\\">California Wildfires - Andrew </a></li></ol></div>\",\n" +
                "      \"bookmark\": [],\n" +
                "      \"file\": [\n" +
                "        \"AmB1wt8.jpg\"\n" +
                "      ],\n" +
                "      \"creator_user_name\": \"范振原\",\n" +
                "      \"is_public\": true,\n" +
                "      \"modified\": \"false\",\n" +
                "      \"private_id\": \"\",\n" +
                "      \"id\": \"version1439381801259\",\n" +
                "      \"view_count\": 28\n" +
                "    }\n" +
                "  ]\n" +
                "}";


        ReadContext ctx = JsonPath.parse(json);
        List<String> notesJson = ctx.read("$.version[*].note.*");


        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        List<Note> notes = mapper.readValue(notesJson.toString(), new TypeReference<List<Note>>() {
        });


        System.out.println(notes);
    }
}
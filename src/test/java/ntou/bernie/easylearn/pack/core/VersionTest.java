package ntou.bernie.easylearn.pack.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by bernie on 2016/2/18.
 */
public class VersionTest {

    @Test
    public void testSync() throws Exception {
        String json = "{\"creator_user_id\":\"1009840175700426\",\"create_time\":1439381801000,\"pack_id\":\"pack1439381800612\",\"user_view_count\":1,\"version\":0,\"content\":\"<div id=\\\"MBRcIYG350g\\\" class=\\\"youtube video-container\\\"><iframe src=\\\"http://www.youtube.com/embed/MBRcIYG350g?controls=1&amp;disablekb=1&amp;modestbranding=1&amp;showinfo=0&amp;rel=0\\\" width=\\\"560\\\" height=\\\"315\\\" frameborder=\\\"0\\\" allowfullscreen=\\\"\\\"></iframe></div>\\n<p>In five days, this <strong><span class=\\\"note note-teal\\\" noteid=\\\"note1439381999482\\\" title=\\\"憤怒的\\\">raging</span></strong> wildfire has doubled in size. It’s left two dozen homes in <strong><span class=\\\"note note-indigo\\\" noteid=\\\"note1439382017021\\\">charred</span></strong> ruins and thousands of people have had to leave. The fire has <strong>scorched</strong> about 54,000 acres in an area east of Lower Lake, which is about 110 miles (177 kilometres) north of San Francisco.</p>\\n<p>It's the worst of 20 large fires being battled by nine thousand firefighters across the state. A separate <strong><span class=\\\"note note-orange\\\" noteid=\\\"note1439382422748\\\">blaze</span></strong> that killed a US forest ranger on Thursday near the Oregon border has also expanded, but remains a fraction of the size.</p>\\n<p>Some 20,000 acres of <strong><span class=\\\"note note-purple\\\" noteid=\\\"note1439382542987\\\">shrub</span></strong> oak and birch were <strong>ravaged</strong> by the fire, over a five-hour period on Saturday night. By Sunday evening, the fire had blackened another 7,000 acres along eastern <strong>flanks</strong> of California's northern coast <strong>ranges</strong>.</p>\\n<p>Authorities said the fire destroyed 24 homes and 26 <strong>outbuildings</strong> and continues to threaten a further 6,300 structures. More than 12,000 people have received <strong>mandatory</strong> evacuation orders. 2,070 personnel which is around a third of the state's force were battling the fire on Sunday evening.</p>\\n<p><img id=\\\"AmB1wt8\\\" class=\\\"slideshare-img fluffy22_california-wildfires-51282566 \\\" style=\\\"max-width: 100% !important; height: auto;\\\" src=\\\"FILE_STORAGE_PATHpack1439381800612/AmB1wt8.jpg\\\" alt=\\\"\\\"><br><br>Read more: <a href=\\\"http://www.newsinlevels.com/products/fires-in-california-level-3/?utm_source=copy&amp;utm_medium=paste&amp;utm_campaign=copypaste&amp;utm_content=http%3A%2F%2Fwww.newsinlevels.com%2Fproducts%2Ffires-in-california-level-3%2F\\\">http://www.newsinlevels.com/products/fires-in-california-level-3/</a></p><div id=\\\"pack_refrence\\\"><h1>引用資料</h1><h2>Youtube</h2><ol><li><a href=\\\"#\\\" onclick=\\\"window.open('http://www.youtube.com/watch?v=MBRcIYG350g', '_system');\\\">Wildfire Jumps Highway in California and Vehicles Catch Fire | World News Tonight | ABC News</a></li></ol><h2>Slideshare</h2><ol><li><a href=\\\"#\\\" onclick=\\\"window.open('http://www.slideshare.net/fluffy22/california-wildfires-51282566', '_system');\\\">California Wildfires - Andrew </a></li></ol></div>\",\"creator_user_name\":\"范振原\",\"is_public\":true,\"modified\":\"false\",\"private_id\":\"\",\"id\":\"version1439381801259\",\"view_count\":27}";
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);


        Version version = mapper.readValue(json, Version.class);
        Document parse = Jsoup.parse(version.getContent(), "", Parser.xmlParser());
        Elements elements = parse.getElementsByClass("note");


        //System.out.println(elements.outerHtml());
        //System.out.println(version);
    }

    @Test
    public void testNoteSync() {
        String dbContent = "<p>It's the worst of 20 large fires being battled by nine thousand firefighters across the state. A separate <strong><span class=\"note note-orange\" noteid=\"note1439382422748\">blaze</span></strong> that killed a US forest ranger on Thursday near the Oregon border has also expanded, but remains a fraction of the size.</p>\n" +
                "<p>Some 20,000 acres of <strong><span class=\"note note-purple\" noteid=\"note1439382542987\">shrub</span></strong> oak and birch were <strong>ravaged</strong> by the fire, over a five-hour period on Saturday night. By Sunday evening, the fire had blackened another 7,000 acres along eastern <strong>flanks</strong> of California's northern coast <strong>ranges</strong>.</p>\n";
        String syncContent = "<p>It's the worst of 20 large fires being battled by nine thousand firefighters across the state. A separate <strong><span class=\"note note-orange\" noteid=\"note1439382422748\">blaze</span></strong> that killed a US forest ranger on Thursday near the Oregon border has also expanded, but remains a fraction of the size.</p>\n" +
                "<p>Some 20,000 acres of <strong><span class=\"note note-purple\" noteid=\"note1439382542987\">shrub</span></strong> oak and birch were <strong>ravaged</strong> by the fire, over a <span class=\"note note-purple\" noteid=\"note14393825429s7\">five-hour</span> period on Saturday night. By Sunday evening, the fire had blackened another 7,000 acres along eastern <strong>flanks</strong> of California's northern coast <strong>ranges</strong>.</p>\n";

        Version version = new Version();
        version.setContent(syncContent);

        String result = version.syncNote(dbContent);
        assertThat(result, is(syncContent));
    }
}
package ntou.bernie.easylearn.pack.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by bernie on 2016/2/19.
 */
public class PackTest {

    @Test
    public void testDeserialize() throws IOException {
        String json = "{\n" +
                "    \"id\": \"pack1439471856230\",\n" +
                "    \"creator_user_id\": \"10204250001235141\",\n" +
                "    \"cover_filename\": \"\",\n" +
                "    \"create_time\": 1439471857000,\n" +
                "    \"creator_user_name\": \"莊晏\",\n" +
                "    \"name\": \"Easylearn 懶人包\",\n" +
                "    \"is_public\": true,\n" +
                "    \"description\": \"Easylearn 的說明與應用\",\n" +
                "    \"version\": [\n" +
                "      {\n" +
                "        \"creator_user_id\": \"10204250001235141\",\n" +
                "        \"note\": [],\n" +
                "        \"create_time\": 1439471857000,\n" +
                "        \"pack_id\": \"pack1439471856230\",\n" +
                "        \"user_view_count\": 0,\n" +
                "        \"version\": 0,\n" +
                "        \"content\": \"s\",\n" +
                "        \"bookmark\": [],\n" +
                "        \"file\": [\n" +
                "          \"14BbpTh.jpg\",\n" +
                "          \"2GKyS4L.jpg\",\n" +
                "          \"6avTHWN.jpg\",\n" +
                "          \"COTv4sU.jpg\",\n" +
                "          \"d9SYMi7.jpg\",\n" +
                "          \"dSIPzrf.jpg\",\n" +
                "          \"IzZZXHi.jpg\",\n" +
                "          \"JKbP1of.jpg\",\n" +
                "          \"k8Mt1wv.jpg\",\n" +
                "          \"Kxdy0Fs.jpg\",\n" +
                "          \"Mt8cqeG.jpg\",\n" +
                "          \"mto3I4k.jpg\",\n" +
                "          \"mXeU2pa.jpg\",\n" +
                "          \"PEiqigb.jpg\",\n" +
                "          \"SgKedna.jpg\",\n" +
                "          \"Su6Ar5E.jpg\",\n" +
                "          \"tEWYYHt.jpg\"\n" +
                "        ],\n" +
                "        \"creator_user_name\": \"莊晏\",\n" +
                "        \"is_public\": true,\n" +
                "        \"modified\": \"false\",\n" +
                "        \"private_id\": \"\",\n" +
                "        \"id\": \"version1439471857459\",\n" +
                "        \"view_count\": 0\n" +
                "      },\n" +
                "      {\n" +
                "        \"creator_user_id\": \"1009840175700426\",\n" +
                "        \"note\": [],\n" +
                "        \"create_time\": 1439491278000,\n" +
                "        \"pack_id\": \"pack1439471856230\",\n" +
                "        \"user_view_count\": 0,\n" +
                "        \"version\": 0,\n" +
                "        \"content\": \"s\",\n" +
                "        \"bookmark\": [],\n" +
                "        \"file\": [],\n" +
                "        \"creator_user_name\": \"范振原\",\n" +
                "        \"is_public\": true,\n" +
                "        \"modified\": \"false\",\n" +
                "        \"private_id\": \"\",\n" +
                "        \"id\": \"version1439491278862\",\n" +
                "        \"view_count\": 0\n" +
                "      },\n" +
                "      {\n" +
                "        \"creator_user_id\": \"1009840175700426\",\n" +
                "        \"note\": [],\n" +
                "        \"create_time\": 1439555486000,\n" +
                "        \"pack_id\": \"pack1439471856230\",\n" +
                "        \"user_view_count\": 0,\n" +
                "        \"version\": 0,\n" +
                "        \"content\": \"s\",\n" +
                "        \"bookmark\": [],\n" +
                "        \"file\": [],\n" +
                "        \"creator_user_name\": \"范振原\",\n" +
                "        \"is_public\": true,\n" +
                "        \"modified\": \"false\",\n" +
                "        \"private_id\": \"\",\n" +
                "        \"id\": \"version1439555486405\",\n" +
                "        \"view_count\": 0\n" +
                "      }\n" +
                "    ],\n" +
                "    \"tags\": \"\"\n" +
                "  }";


        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        Pack pack = mapper.readValue(json, Pack.class);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Pack>> constraintViolations = validator.validate(pack);

        for (ConstraintViolation<Pack> constraintViolation : constraintViolations) {
            System.out.println(constraintViolation.toString());
        }

        assertThat(constraintViolations.size(), is(0));
    }

}
package ntou.bernie.easylearn.user.core;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class FolderTest {

    private static Validator validator;
    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    @Test
    public void testJsonDeserlization() throws JsonParseException, JsonMappingException, IOException {
        String json = "[{\"name\":\"\u5168\u90E8\u7684\u61F6\u4EBA\u5305\",\"id\":\"allfolder\",\"pack\":[\"pack1439355907459\",\"pack1439367500493\",\"pack1439370245981\",\"pack1439372921598\",\"pack1439381800612\",\"pack1439385129482\",\"pack1439394796784\",\"pack1439451391246\",\"pack1439471856230\"]},{\"name\":\"All\",\"id\":\"allPackId\",\"pack\":[\"pack1439355907459\",\"pack1439370245981\",\"pack1439372921598\",\"pack1439381800612\",\"pack1439385129482\",\"pack1439394796784\",\"pack1450275282853\",\"pack1450347155192\"]},{\"name\":\"\u6211\u7684\u6700\u611B\",\"id\":\"fjoeiwjowfe\",\"pack\":[]},{\"name\":\"\u8207\u4F60\u5206\u4EAB\u61F6\u4EBA\u5305\",\"id\":\"shareFolder\",\"pack\":[\"pack1439394796784\"]}]";

        List<Folder> folders = objectMapper.readValue(json, new TypeReference<List<Folder>>() {
        });

        Set<ConstraintViolation<Folder>> constraintViolations = validator.validate(folders.get(0));
        assertEquals(0, constraintViolations.size());
        assertEquals(4, folders.size());
        assertEquals("全部的懶人包", folders.get(0).getName());
        assertEquals("allfolder", folders.get(0).getId());
        assertEquals("pack1439355907459", folders.get(0).getPack().get(0));
    }

    @Test
    public void testJsonDeserlizationBoundary() throws JsonParseException, JsonMappingException, IOException {
        // empty array
        String json = "[]";
        List<Folder> folders = objectMapper.readValue(json, new TypeReference<List<Folder>>() {
        });
        assertEquals(0, folders.size());

        // 1 normal json
        json = "[{\"name\":\"All\",\"id\":\"allPackId\",\"pack\":[\"pack1439355907459\",\"pack1439370245981\",\"pack1439372921598\",\"pack1439381800612\",\"pack1439385129482\",\"pack1439394796784\",\"pack1450275282853\",\"pack1450347155192\"]}]";
        folders = objectMapper.readValue(json, new TypeReference<List<Folder>>() {
        });
        Set<ConstraintViolation<Folder>> constraintViolations = validator.validate(folders.get(0));
        assertEquals(0, constraintViolations.size());
        assertEquals(1, folders.size());

        // add additional json field
        json = "[{\"name\":\"All\",\"id\":\"allPackId\",\"pack\":[\"pack1439355907459\",\"pack1439370245981\",\"pack1439372921598\",\"pack1439381800612\",\"pack1439385129482\",\"pack1439394796784\",\"pack1450275282853\",\"pack1450347155192\"],\"status\":\"test\"}]";
        folders = objectMapper.readValue(json, new TypeReference<List<Folder>>() {
        });
        constraintViolations = validator.validate(folders.get(0));
        assertEquals(0, constraintViolations.size());
        assertEquals(1, folders.size());
    }

    @Test(expected = Exception.class)
    public void testJsonDeserlizationBoundaryWrongJsonException()
            throws JsonParseException, JsonMappingException, IOException {
        // wrong json
        String json = "[afsadf]";
        List<Folder> folders = objectMapper.readValue(json, new TypeReference<List<Folder>>() {
        });
    }

    @Test
    public void testJsonValidation() throws JsonParseException, JsonMappingException, IOException {
        String json = "[{\"name\":\"All\",\"pack\":[\"pack1439355907459\",\"pack1439370245981\",\"pack1439372921598\",\"pack1439381800612\",\"pack1439385129482\",\"pack1439394796784\",\"pack1450275282853\",\"pack1450347155192\"]}]";
        List<Folder> folders = objectMapper.readValue(json, new TypeReference<List<Folder>>() {
        });
        Set<ConstraintViolation<Folder>> constraintViolations = validator.validate(folders.get(0));
        assertEquals(1, constraintViolations.size());

        json = "[{\"name\":\"All\",\"id\":null,\"pack\":[\"pack1439355907459\",\"pack1439370245981\",\"pack1439372921598\",\"pack1439381800612\",\"pack1439385129482\",\"pack1439394796784\",\"pack1450275282853\",\"pack1450347155192\"]}]";
        folders = objectMapper.readValue(json, new TypeReference<List<Folder>>() {
        });
        constraintViolations = validator.validate(folders.get(0));
        for (ConstraintViolation<Folder> constraintViolation : constraintViolations) {
            assertThat(constraintViolation.toString(), CoreMatchers.containsString("NotNull"));
        }
        assertEquals(1, constraintViolations.size());
    }

}

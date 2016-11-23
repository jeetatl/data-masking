package com.jeetatl.datamasking.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeetatl.datamasking.config.MaskingConfiguration;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class JsonPayloadMaskerTests {

    @Test
    public void testConstructor() {
        MaskingConfiguration config = new MaskingConfiguration("apple:~3");
        JsonPayloadMasker masker = new JsonPayloadMasker(config);
        Assert.assertEquals(config, masker.getConfig());
    }

    @Test
    public void testConfigSetter() {
        MaskingConfiguration config = new MaskingConfiguration("apple:~3");
        JsonPayloadMasker masker = new JsonPayloadMasker(config);

        MaskingConfiguration config2 = new MaskingConfiguration("orange:~5");
        masker.setConfig(config2);

        Assert.assertEquals(config2, masker.getConfig());
    }

    @Test
    public void testSimpleStringMasking() {
        MaskingConfiguration config = new MaskingConfiguration("field1:+100%");
        JsonPayloadMasker masker = new JsonPayloadMasker(config);
        String maskedField = "XXXX";

        String jsonPayload = "{\n" +
                "\t\"field1\": \"asdf\",\n" +
                "\t\"b\": 323.234,\n" +
                "\t\"c\": 23,\n" +
                "\t\"d\": true\n" +
                "}";

        String maskedJsonPayload = masker.getMasked(jsonPayload);
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> maskedMap = mapper.readValue(maskedJsonPayload, Map.class);
            Assert.assertEquals(maskedField, maskedMap.get("field1"));
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testSimpleDoubleMasking() {
        MaskingConfiguration config = new MaskingConfiguration("field2:+100%");
        JsonPayloadMasker masker = new JsonPayloadMasker(config);
        String maskedField = "XXXXXXX";

        String jsonPayload = "{\n" +
                "\t\"field1\": \"asdf\",\n" +
                "\t\"field2\": 323.234,\n" +
                "\t\"c\": 23,\n" +
                "\t\"d\": true\n" +
                "}";

        String maskedJsonPayload = masker.getMasked(jsonPayload);
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> maskedMap = mapper.readValue(maskedJsonPayload, Map.class);
            Assert.assertEquals(maskedField, maskedMap.get("field2"));
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testSimpleIntegerMasking() {
        MaskingConfiguration config = new MaskingConfiguration("field3:+100%");
        JsonPayloadMasker masker = new JsonPayloadMasker(config);
        String maskedField = "XX";

        String jsonPayload = "{\n" +
                "\t\"field1\": \"asdf\",\n" +
                "\t\"field2\": 323.234,\n" +
                "\t\"field3\": 23,\n" +
                "\t\"d\": true\n" +
                "}";

        String maskedJsonPayload = masker.getMasked(jsonPayload);
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> maskedMap = mapper.readValue(maskedJsonPayload, Map.class);
            Assert.assertEquals(maskedField, maskedMap.get("field3"));
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testSimpleBooleanMasking() {
        MaskingConfiguration config = new MaskingConfiguration("field4:+100%");
        JsonPayloadMasker masker = new JsonPayloadMasker(config);
        String maskedField = "XXXX";

        String jsonPayload = "{\n" +
                "\t\"field1\": \"asdf\",\n" +
                "\t\"field2\": 323.234,\n" +
                "\t\"field3\": 23,\n" +
                "\t\"field4\": true\n" +
                "}";

        String maskedJsonPayload = masker.getMasked(jsonPayload);
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> maskedMap = mapper.readValue(maskedJsonPayload, Map.class);
            Assert.assertEquals(maskedField, maskedMap.get("field4"));
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testNestedMasking() {
        MaskingConfiguration config = new MaskingConfiguration("field5:+100%");
        JsonPayloadMasker masker = new JsonPayloadMasker(config);
        String maskedField = "XXXX";

        String jsonPayload = "{\n" +
                "\t\"field1\": \"asdf\",\n" +
                "\t\"field2\": 323.234,\n" +
                "\t\"field3\": 23,\n" +
                "\t\"field4\": true,\n" +
                "\t\"sub\": {\n" +
                "\t\t\"field5\": true,\n" +
                "\t\t\"field6\": 98.89\n" +
                "\t}\n" +

                "}";

        String maskedJsonPayload = masker.getMasked(jsonPayload);
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> maskedMap = mapper.readValue(maskedJsonPayload, Map.class);
            Assert.assertEquals(maskedField, ((Map<String, Object>) maskedMap.get("sub")).get("field5"));
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testBadJson() {
        MaskingConfiguration config = new MaskingConfiguration("field1:+100%");
        JsonPayloadMasker masker = new JsonPayloadMasker(config);
        String badJson = "{\"field1\": true";   // no closing curly brace
        String response = null;

        response = masker.getMasked(badJson);

        Assert.assertEquals(badJson, response);
    }

    @Test
    public void testNullJson() {
        MaskingConfiguration config = new MaskingConfiguration("field1:+100%");
        JsonPayloadMasker masker = new JsonPayloadMasker(config);
        String badJson = null;
        String response = null;

        response = masker.getMasked(badJson);

        Assert.assertEquals(badJson, response);
    }

    @Test
    public void testEmptyJson() {
        MaskingConfiguration config = new MaskingConfiguration("field1:+100%");
        JsonPayloadMasker masker = new JsonPayloadMasker(config);
        String badJson = "";
        String response = "";

        response = masker.getMasked(badJson);

        Assert.assertEquals(badJson, response);
    }
}

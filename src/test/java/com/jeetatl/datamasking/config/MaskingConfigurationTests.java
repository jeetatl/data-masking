package com.jeetatl.datamasking.config;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mkhokhar on 11/4/16.
 */
public class MaskingConfigurationTests {

    @Test
    public void testDefaults() {
        MaskingConfiguration config = new MaskingConfiguration();
        String testString = "1234567890";
        String respString = null;

        respString = config.apply("unknownField", testString);
        Assert.assertTrue(testString.equals(respString));
    }

    @Test
    public void testConstructor() {
        String configString = null;
        String expectedString = null;
        String testString = null;
        String resultString = null;
        MaskingConfiguration config = null;

        configString = "field:+5";
        testString = "1234567890";
        expectedString = "XXXXX67890";
        config = new MaskingConfiguration(configString);
        resultString = config.apply("field", testString);
        Assert.assertEquals(expectedString, resultString);

        expectedString = "1234567890";
        resultString = config.apply("unknownfield", testString);
        Assert.assertEquals(expectedString, resultString);

        configString = "field:-5";
        testString = "1234567890";
        expectedString = "12345XXXXX";
        config = new MaskingConfiguration(configString);
        resultString = config.apply("field", testString);
        Assert.assertEquals(expectedString, resultString);

        configString = "field:+1%";
        testString = "1234567890";
        expectedString = "X234567890";
        config = new MaskingConfiguration(configString);
        resultString = config.apply("field", testString);
        Assert.assertEquals(expectedString, resultString);

        configString = "field:-1%";
        testString = "1234567890";
        expectedString = "123456789X";
        config = new MaskingConfiguration(configString);
        resultString = config.apply("field", testString);
        Assert.assertEquals(expectedString, resultString);

        configString = "field:~1";
        testString = "1234567890";
        expectedString = "X234567890";
        config = new MaskingConfiguration(configString);
        resultString = config.apply("field", testString);
        Assert.assertEquals(expectedString, resultString);

        configString = "field:~1-5";
        testString = "1234567890";
        expectedString = "XXXXX67890";
        config = new MaskingConfiguration(configString);
        resultString = config.apply("field", testString);
        Assert.assertEquals(expectedString, resultString);

        configString = "field:~3-5";
        testString = "1234567890";
        expectedString = "12XXX67890";
        config = new MaskingConfiguration(configString);
        resultString = config.apply("field", testString);
        Assert.assertEquals(expectedString, resultString);

        configString = "field:~3-20";
        testString = "1234567890";
        expectedString = "12XXXXXXXX";
        config = new MaskingConfiguration(configString);
        resultString = config.apply("field", testString);
        Assert.assertEquals(expectedString, resultString);

        configString = "field:-1%";
        String defaultMasking = "+3,-3";
        testString = "1234567890";
        expectedString = "XXX4567XXX";
        config = new MaskingConfiguration(configString, defaultMasking);
        resultString = config.apply("unknownfield", testString);
        Assert.assertEquals(expectedString, resultString);

    }

    @Test
    public void testMultiFieldMultiMaskConfigString() {
        String configString = null;
        String expectedString = null;
        String testString = null;
        String resultString = null;
        MaskingConfiguration config = null;

        configString = "field:+5,-4";
        testString = "1234567890";
        expectedString = "XXXXX6XXXX";
        config = new MaskingConfiguration(configString);
        resultString = config.apply("field", testString);
        Assert.assertEquals(expectedString, resultString);

        configString = "field:+5,-4%";
        testString = "1234567890";
        expectedString = "XXXXX6789X";
        config = new MaskingConfiguration(configString);
        resultString = config.apply("field", testString);
        Assert.assertEquals(expectedString, resultString);

        configString = "field:+5,-4%";
        testString = null;
        expectedString = null;
        config = new MaskingConfiguration(configString);
        resultString = config.apply("field", testString);
        Assert.assertEquals(expectedString, resultString);

        configString = "field:+5,-4%|field2:+3,-3";
        testString = "1234567890";
        expectedString = "XXXXX6789X";
        config = new MaskingConfiguration(configString);
        resultString = config.apply("field", testString);
        Assert.assertEquals(expectedString, resultString);
        expectedString = "XXX4567XXX";
        resultString = config.apply("field2", testString);
        Assert.assertEquals(expectedString, resultString);
    }

    @Test
    public void testStringBuilderMultiFieldMultiMask() {
        String configString = null;
        StringBuilder testSB = null;
        String expectedString = null;
        MaskingConfiguration config = null;

        configString = "field:+5,-4";
        testSB = new StringBuilder("1234567890");
        expectedString = "XXXXX6XXXX";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:+5,-4%";
        testSB = new StringBuilder("1234567890");
        expectedString = "XXXXX6789X";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:+5%,-4%";
        testSB = new StringBuilder("1234567890");
        expectedString = "X23456789X";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:+5%,-4";
        testSB = new StringBuilder("1234567890");
        expectedString = "X23456XXXX";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:+5%,-4,~3";
        testSB = new StringBuilder("1234567890");
        expectedString = "X2X456XXXX";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:~1-5,~6,~7,~8,~10";
        testSB = new StringBuilder("1234567890");
        expectedString = "XXXXXXXX9X";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:~3-20";
        testSB = new StringBuilder("1234567890");
        expectedString = "12XXXXXXXX";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:+5%,-4";
        testSB = new StringBuilder("1234567890");
        expectedString = "1234567890";
        config = new MaskingConfiguration(configString);
        config.apply("unknownfield", testSB);
        Assert.assertEquals(expectedString, testSB.toString());
    }

    @Test
    public void testStringBufferMultiFieldMultiMask() {
        String configString = null;
        StringBuffer testSB = null;
        String expectedString = null;
        MaskingConfiguration config = null;

        configString = "field:+5,-4";
        testSB = new StringBuffer("1234567890");
        expectedString = "XXXXX6XXXX";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:+5,-4%";
        testSB = new StringBuffer("1234567890");
        expectedString = "XXXXX6789X";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:+5%,-4%";
        testSB = new StringBuffer("1234567890");
        expectedString = "X23456789X";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:+5%,-4";
        testSB = new StringBuffer("1234567890");
        expectedString = "X23456XXXX";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:~3-20";
        testSB = new StringBuffer("1234567890");
        expectedString = "12XXXXXXXX";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:+5%,-4";
        testSB = new StringBuffer("1234567890");
        expectedString = "1234567890";
        config = new MaskingConfiguration(configString);
        config.apply("unknownfield", testSB);
        Assert.assertEquals(expectedString, testSB.toString());
    }

    @Test
    public void testConfigStringSetter() {
        String configString = null;
        StringBuffer testSB = null;
        String expectedString = null;
        MaskingConfiguration config = null;

        configString = "field:+5,-4";
        testSB = new StringBuffer("1234567890");
        expectedString = "XXXXX6XXXX";
        config = new MaskingConfiguration(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());

        configString = "field:+2,-2";
        testSB = new StringBuffer("1234567890");
        expectedString = "XX345678XX";
        config.setConfigString(configString);
        config.apply("field", testSB);
        Assert.assertEquals(expectedString, testSB.toString());
    }

















}

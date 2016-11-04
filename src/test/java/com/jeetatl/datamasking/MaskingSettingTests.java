package com.jeetatl.datamasking;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mkhokhar on 11/4/16.
 */
public class MaskingSettingTests {

    @Test
    public void testDefaults() {
        MaskingSetting ms = new MaskingSetting();
        Assert.assertEquals(0.0, ms.getPercentMaskLeft(), 0.001);
        Assert.assertEquals(0.0, ms.getPercentMaskRight(), 0.001);
        Assert.assertEquals(0, ms.getCharactersMaskLeft());
        Assert.assertEquals(0, ms.getCharactersMaskRight());
        Assert.assertEquals('X', ms.getMaskingCharacter());
        Assert.assertEquals("X", ms.getMaskingString());
    }

    @Test
    public void testConstructor() {
        MaskingSetting ms = new MaskingSetting(1,2,1.0,2.0,'A');
        Assert.assertEquals(1.0, ms.getPercentMaskLeft(), 0.001);
        Assert.assertEquals(2.0, ms.getPercentMaskRight(), 0.001);
        Assert.assertEquals(1, ms.getCharactersMaskLeft());
        Assert.assertEquals(2, ms.getCharactersMaskRight());
        Assert.assertEquals('A', ms.getMaskingCharacter());
        Assert.assertEquals("A", ms.getMaskingString());
    }

    @Test
    public void testSetters() {
        MaskingSetting ms = new MaskingSetting();
        ms.setMaskingCharacter('A');
        ms.setPercentMaskLeft(1.0);
        ms.setPercentMaskRight(2.0);
        ms.setCharactersMaskLeft(1);
        ms.setCharactersMaskRight(2);

        Assert.assertEquals(1.0, ms.getPercentMaskLeft(), 0.001);
        Assert.assertEquals(2.0, ms.getPercentMaskRight(), 0.001);
        Assert.assertEquals(1, ms.getCharactersMaskLeft());
        Assert.assertEquals(2, ms.getCharactersMaskRight());
        Assert.assertEquals('A', ms.getMaskingCharacter());
        Assert.assertEquals("A", ms.getMaskingString());
    }

    @Test
    public void testStringBuilderLeftCharactersMasking() {
        MaskingSetting ms = new MaskingSetting();
        ms.setMaskingCharacter('A');
        ms.setCharactersMaskLeft(5);

        String testStr = "AAAAA67890";
        StringBuilder sb = new StringBuilder();
        sb.append("1234567890");
        ms.apply(sb);

        Assert.assertTrue(testStr.equals(sb.toString()));
    }

    @Test
    public void testStringBuilderRightCharactersMasking() {
        MaskingSetting ms = new MaskingSetting();
        ms.setMaskingCharacter('A');
        ms.setCharactersMaskRight(5);

        String testStr = "12345AAAAA";
        StringBuilder sb = new StringBuilder();
        sb.append("1234567890");
        ms.apply(sb);

        Assert.assertTrue(testStr.equals(sb.toString()));
    }

    @Test
    public void testStringBuilderLeftPercentMasking() {
        MaskingSetting ms = new MaskingSetting();
        ms.setMaskingCharacter('A');
        ms.setPercentMaskLeft(25);

        String testStr = "AAA4567890";
        StringBuilder sb = new StringBuilder();
        sb.append("1234567890");
        ms.apply(sb);

        Assert.assertTrue(testStr.equals(sb.toString()));
    }

    @Test
    public void testStringBuilderRightPercentMasking() {
        MaskingSetting ms = new MaskingSetting();
        ms.setMaskingCharacter('A');
        ms.setPercentMaskRight(25);

        String testStr = "1234567AAA";
        StringBuilder sb = new StringBuilder();
        sb.append("1234567890");
        ms.apply(sb);

        Assert.assertTrue(testStr.equals(sb.toString()));
    }

    @Test
    public void testStringBufferLeftCharactersMasking() {
        MaskingSetting ms = new MaskingSetting();
        ms.setMaskingCharacter('A');
        ms.setCharactersMaskLeft(5);

        String testStr = "AAAAA67890";
        StringBuffer sb = new StringBuffer();
        sb.append("1234567890");
        ms.apply(sb);

        Assert.assertTrue(testStr.equals(sb.toString()));
    }

    @Test
    public void testStringBufferRightCharactersMasking() {
        MaskingSetting ms = new MaskingSetting();
        ms.setMaskingCharacter('A');
        ms.setCharactersMaskRight(5);

        String testStr = "12345AAAAA";
        StringBuffer sb = new StringBuffer();
        sb.append("1234567890");
        ms.apply(sb);

        Assert.assertTrue(testStr.equals(sb.toString()));
    }

    @Test
    public void testStringBufferLeftPercentMasking() {
        MaskingSetting ms = new MaskingSetting();
        ms.setMaskingCharacter('A');
        ms.setPercentMaskLeft(25);

        String testStr = "AAA4567890";
        StringBuffer sb = new StringBuffer();
        sb.append("1234567890");
        ms.apply(sb);

        Assert.assertTrue(testStr.equals(sb.toString()));
    }

    @Test
    public void testStringBufferRightPercentMasking() {
        MaskingSetting ms = new MaskingSetting();
        ms.setMaskingCharacter('A');
        ms.setPercentMaskRight(25);

        String testStr = "1234567AAA";
        StringBuffer sb = new StringBuffer();
        sb.append("1234567890");
        ms.apply(sb);

        Assert.assertTrue(testStr.equals(sb.toString()));
    }

    @Test
    public void testStringGetNumCharactersToMask() {
        MaskingSetting ms = null;
        String testStr = "1234567890";

        ms = new MaskingSetting();
        ms.setPercentMaskRight(0);
        Assert.assertEquals(0, ms.getNumCharactersToMaskRight(testStr));

        ms = new MaskingSetting();
        ms.setPercentMaskRight(25);
        Assert.assertEquals(3, ms.getNumCharactersToMaskRight(testStr));

        ms = new MaskingSetting();
        ms.setPercentMaskRight(59);
        Assert.assertEquals(6, ms.getNumCharactersToMaskRight(testStr));

        ms = new MaskingSetting();
        ms.setPercentMaskRight(100);
        Assert.assertEquals(10, ms.getNumCharactersToMaskRight(testStr));

        ms = new MaskingSetting();
        ms.setPercentMaskRight(200);
        Assert.assertEquals(10, ms.getNumCharactersToMaskRight(testStr));

        ms = new MaskingSetting();
        ms.setCharactersMaskRight(5);
        Assert.assertEquals(5, ms.getNumCharactersToMaskRight(testStr));

        ms = new MaskingSetting();
        ms.setCharactersMaskRight(20);
        Assert.assertEquals(10, ms.getNumCharactersToMaskRight(testStr));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(0);
        Assert.assertEquals(0, ms.getNumCharactersToMaskLeft(testStr));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(25);
        Assert.assertEquals(3, ms.getNumCharactersToMaskLeft(testStr));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(59);
        Assert.assertEquals(6, ms.getNumCharactersToMaskLeft(testStr));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(100);
        Assert.assertEquals(10, ms.getNumCharactersToMaskLeft(testStr));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(200);
        Assert.assertEquals(10, ms.getNumCharactersToMaskLeft(testStr));

        ms = new MaskingSetting();
        ms.setCharactersMaskLeft(5);
        Assert.assertEquals(5, ms.getNumCharactersToMaskLeft(testStr));

        ms = new MaskingSetting();
        ms.setCharactersMaskLeft(20);
        Assert.assertEquals(10, ms.getNumCharactersToMaskLeft(testStr));
    }

    @Test
    public void testStringBuilderGetNumCharactersToMask() {
        MaskingSetting ms = null;
        StringBuilder nullSB = null;

        ms = new MaskingSetting();
        Assert.assertEquals(0, ms.getNumCharactersToMaskRight(nullSB));

        StringBuilder stringBuilder = new StringBuilder("1234567890");

        ms = new MaskingSetting();
        ms.setPercentMaskRight(0);
        Assert.assertEquals(0, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskRight(25);
        Assert.assertEquals(3, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskRight(59);
        Assert.assertEquals(6, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskRight(100);
        Assert.assertEquals(10, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskRight(200);
        Assert.assertEquals(10, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        ms.setCharactersMaskRight(5);
        Assert.assertEquals(5, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        ms.setCharactersMaskRight(20);
        Assert.assertEquals(10, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        Assert.assertEquals(0, ms.getNumCharactersToMaskLeft(nullSB));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(0);
        Assert.assertEquals(0, ms.getNumCharactersToMaskLeft(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(25);
        Assert.assertEquals(3, ms.getNumCharactersToMaskLeft(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(59);
        Assert.assertEquals(6, ms.getNumCharactersToMaskLeft(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(100);
        Assert.assertEquals(10, ms.getNumCharactersToMaskLeft(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(200);
        Assert.assertEquals(10, ms.getNumCharactersToMaskLeft(stringBuilder));

        ms = new MaskingSetting();
        ms.setCharactersMaskLeft(5);
        Assert.assertEquals(5, ms.getNumCharactersToMaskLeft(stringBuilder));

        ms = new MaskingSetting();
        ms.setCharactersMaskLeft(20);
        Assert.assertEquals(10, ms.getNumCharactersToMaskLeft(stringBuilder));
    }

    @Test
    public void testStringBufferGetNumCharactersToMask() {
        MaskingSetting ms = null;
        StringBuffer nullSB = null;

        ms = new MaskingSetting();
        Assert.assertEquals(0, ms.getNumCharactersToMaskRight(nullSB));

        StringBuilder stringBuilder = new StringBuilder("1234567890");

        ms = new MaskingSetting();
        ms.setPercentMaskRight(0);
        Assert.assertEquals(0, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskRight(25);
        Assert.assertEquals(3, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskRight(59);
        Assert.assertEquals(6, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskRight(100);
        Assert.assertEquals(10, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskRight(200);
        Assert.assertEquals(10, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        ms.setCharactersMaskRight(5);
        Assert.assertEquals(5, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        ms.setCharactersMaskRight(20);
        Assert.assertEquals(10, ms.getNumCharactersToMaskRight(stringBuilder));

        ms = new MaskingSetting();
        Assert.assertEquals(0, ms.getNumCharactersToMaskLeft(nullSB));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(0);
        Assert.assertEquals(0, ms.getNumCharactersToMaskLeft(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(25);
        Assert.assertEquals(3, ms.getNumCharactersToMaskLeft(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(59);
        Assert.assertEquals(6, ms.getNumCharactersToMaskLeft(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(100);
        Assert.assertEquals(10, ms.getNumCharactersToMaskLeft(stringBuilder));

        ms = new MaskingSetting();
        ms.setPercentMaskLeft(200);
        Assert.assertEquals(10, ms.getNumCharactersToMaskLeft(stringBuilder));

        ms = new MaskingSetting();
        ms.setCharactersMaskLeft(5);
        Assert.assertEquals(5, ms.getNumCharactersToMaskLeft(stringBuilder));

        ms = new MaskingSetting();
        ms.setCharactersMaskLeft(20);
        Assert.assertEquals(10, ms.getNumCharactersToMaskLeft(stringBuilder));
    }

    @Test
    public void testApplyStringBuilder() {
        MaskingSetting ms = null;
        StringBuilder sb = new StringBuilder("1234567890");

        ms = new MaskingSetting();
        ms.setCharactersMaskLeft(20);
        ms.setCharactersMaskRight(5);

        ms.apply(sb);
        Assert.assertEquals("XXXXXXXXXX", sb.toString());

        ms = new MaskingSetting();
        ms.setCharactersMaskLeft(5);
        ms.setCharactersMaskRight(20);

        ms.apply(sb);
        Assert.assertEquals("XXXXXXXXXX", sb.toString());

        sb = null;
        ms.apply(sb);
    }

    @Test
    public void testApplyStringBuffer() {
        MaskingSetting ms = null;
        StringBuffer sb = new StringBuffer("1234567890");

        ms = new MaskingSetting();
        ms.setCharactersMaskLeft(20);
        ms.setCharactersMaskRight(5);

        ms.apply(sb);
        Assert.assertEquals("XXXXXXXXXX", sb.toString());

        ms = new MaskingSetting();
        ms.setCharactersMaskLeft(5);
        ms.setCharactersMaskRight(20);

        ms.apply(sb);
        Assert.assertEquals("XXXXXXXXXX", sb.toString());

        sb = null;
        ms.apply(sb);
    }

    @Test
    public void testApplyString() {
        MaskingSetting ms = null;
        String str = new String("1234567890");
        String nullString = null;

        ms = new MaskingSetting();
        ms.setCharactersMaskLeft(20);
        ms.setCharactersMaskRight(5);

        String respStr = ms.apply(str);
        Assert.assertEquals("XXXXXXXXXX", respStr);

        ms = new MaskingSetting();
        ms.setCharactersMaskLeft(5);
        ms.setCharactersMaskRight(20);

        respStr = ms.apply(str);
        Assert.assertEquals("XXXXXXXXXX", respStr);

        respStr = null;
        respStr = ms.apply(nullString);
        Assert.assertEquals(null, respStr);
    }

    @Test
    public void testToString() {
        // compete coverage. not necessary
        MaskingSetting ms = new MaskingSetting();
        ms.toString();
        Assert.assertTrue(true);
    }

}

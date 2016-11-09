package com.jeetatl.datamasking.xml;

import com.jeetatl.datamasking.config.MaskingConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by mkhokhar on 11/9/16.
 */
public class XmlPayloadMaskerTests {

    @Test
    public void testConstructor() {
        MaskingConfiguration config = new MaskingConfiguration("apple:~3");
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);
        Assert.assertEquals(config, xmlPayloadMasker.getConfig());
    }

    @Test
    public void testConfigSetter() {
        MaskingConfiguration config = new MaskingConfiguration("apple:~3");
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);

        MaskingConfiguration config2 = new MaskingConfiguration("orange:~5");
        xmlPayloadMasker.setConfig(config2);

        Assert.assertEquals(config2, xmlPayloadMasker.getConfig());
    }

    @Test
    public void testSimpleElementMasking() {
        MaskingConfiguration config = new MaskingConfiguration("element1:+100%");
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);

        String xml = "<root><element1>value</element1></root>";
        String maskedElemStr = "XXXXX";
        String maskedXml = xmlPayloadMasker.getMasked(xml);

        Document maskedDoc = getDocument(maskedXml);
        Node node = maskedDoc.getElementsByTagName("element1").item(0);

        Assert.assertEquals(maskedElemStr, node.getTextContent());
    }

    @Test
    public void testSpaceSimpleElementMasking() {
        MaskingConfiguration config = new MaskingConfiguration("element1:+100%");
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);

        String xml = "<root     ><element1 >value</element1></root>";
        String maskedElemStr = "XXXXX";
        String maskedXml = xmlPayloadMasker.getMasked(xml);

        Document maskedDoc = getDocument(maskedXml);
        Node node = maskedDoc.getElementsByTagName("element1").item(0);

        Assert.assertEquals(maskedElemStr, node.getTextContent());
    }

    @Test
    public void testNamespaceElementMasking() {
        MaskingConfiguration config = new MaskingConfiguration("element1:+100%");
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);

        String xml = "<root xmlns:test=\"http://example.com/X\"><test:element1>value</test:element1></root>";
        String maskedElemStr = "XXXXX";
        String maskedXml = xmlPayloadMasker.getMasked(xml);

        Document maskedDoc = getDocument(maskedXml);
        Node node = maskedDoc.getElementsByTagNameNS("http://example.com/X", "element1").item(0);

        Assert.assertEquals(maskedElemStr, node.getTextContent());
    }



    @Test
    public void testNamespaceElementsMasking() {
        MaskingConfiguration config = new MaskingConfiguration("element1:+100%");
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);

        String xml = "<root xmlns:test=\"http://example.com/X\"><test:element1>value</test:element1><element1>value</element1></root>";
        String maskedElemStr = "XXXXX";
        String maskedXml = xmlPayloadMasker.getMasked(xml);

        Document maskedDoc = getDocument(maskedXml);
        Node node = null;
        node = maskedDoc.getElementsByTagNameNS("http://example.com/X", "element1").item(0);
        Assert.assertEquals(maskedElemStr, node.getTextContent());

        node = maskedDoc.getElementsByTagName("element1").item(0);
        Assert.assertEquals(maskedElemStr, node.getTextContent());

    }

    @Test
    public void testParentElementMasking() {
        MaskingConfiguration config = new MaskingConfiguration("root:+100%");
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);

        String xml = "<root xmlns:test=\"http://example.com/X\"><test:element1>value</test:element1><element1>value</element1></root>";
        String maskedXml = xmlPayloadMasker.getMasked(xml);
        String expectedXml = "value";

        Document maskedDoc = getDocument(maskedXml);
        Node node = null;
        node = maskedDoc.getElementsByTagName("element1").item(0);
        Assert.assertNull(node);
    }

    @Test
    public void testParentChildElementMasking() {
        MaskingConfiguration config = new MaskingConfiguration("root:+100%|element1:+100%");
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);

        String xml = "<root xmlns:test=\"http://example.com/X\"><test:element1>value</test:element1><element1>value</element1></root>";
        String maskedXml = xmlPayloadMasker.getMasked(xml);

        Document maskedDoc = getDocument(maskedXml);
        Assert.assertTrue(maskedDoc.getElementsByTagName("element1").getLength() == 0);
    }

    @Test
    public void testAttributeMasking() {
        MaskingConfiguration config = new MaskingConfiguration("attribute1:+100%");
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);

        String xml = "<root xmlns:test=\"http://example.com/X\"><test:element1 attribute1=\"VALUE\">value</test:element1><element1>value</element1></root>";
        String maskedXml = xmlPayloadMasker.getMasked(xml);
        String expectedStr = "XXXXX";

        Document maskedDoc = getDocument(maskedXml);
        Node node = maskedDoc.getElementsByTagNameNS("http://example.com/X","element1").item(0).getAttributes().item(0);
        Assert.assertTrue(expectedStr.equals(node.getTextContent()));
    }

    @Test
    public void testAttributeDisabledMaskingMasking() {
        MaskingConfiguration config = new MaskingConfiguration("attribute1:+100%");
        config.setAttributeMaskEnabled(false);
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);

        String xml = "<root xmlns:test=\"http://example.com/X\"><test:element1 attribute1=\"VALUE\">value</test:element1><element1>value</element1></root>";
        String maskedXml = xmlPayloadMasker.getMasked(xml);
        String expectedStr = "VALUE";

        Document maskedDoc = getDocument(maskedXml);
        Node node = maskedDoc.getElementsByTagNameNS("http://example.com/X","element1").item(0).getAttributes().item(0);
        Assert.assertTrue(expectedStr.equals(node.getTextContent()));
    }

    @Test
    public void testBadXml() {
        MaskingConfiguration config = new MaskingConfiguration("element1:+100%");
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);

        String xml = "<Bad></xml>";

        String maskedXml = xmlPayloadMasker.getMasked(xml);
        Assert.assertTrue(xml.equals(maskedXml));
    }

    @Test
    public void testBlankXml() {
        MaskingConfiguration config = new MaskingConfiguration("element1:+100%");
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);

        String xml = "";

        String maskedXml = xmlPayloadMasker.getMasked(xml);
        Assert.assertTrue(xml.equals(maskedXml));
    }

    @Test
    public void testNullXml() {
        MaskingConfiguration config = new MaskingConfiguration("element1:+100%");
        XmlPayloadMasker xmlPayloadMasker = new XmlPayloadMasker(config);

        String xml = null;

        String maskedXml = xmlPayloadMasker.getMasked(xml);
        Assert.assertNull(maskedXml);
    }

    private Document getDocument(String input) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document document = null;

        try {
            document = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return document;
    }
}

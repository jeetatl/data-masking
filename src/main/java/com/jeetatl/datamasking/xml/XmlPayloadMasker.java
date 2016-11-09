package com.jeetatl.datamasking.xml;

import com.jeetatl.datamasking.config.MaskingConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * <p>{@code XmlPayloadMasker} is a concrete class used to apply {@link MaskingConfiguration} to
 * XML payloads.</p>
 *
 * The masking is applied to any element name that is currently configured within the
 * {@link MaskingConfiguration}.  Using default settings, the attributes are also masked.
 * This behavior can be changed by disabling attribute masking via the
 * {@link MaskingConfiguration#setAttributeMaskEnabled(boolean)} method.
 */
public class XmlPayloadMasker {


    MaskingConfiguration config = null;

    /**
     *  Constructs an {@code XmlPayloadMasker} with the provided configuration settings.
     * @param config Configuration settings to use with this {@code XmlPayloadMasker}.
     */
    public XmlPayloadMasker(MaskingConfiguration config) {
        this.config = config;
    }

    /**
     * This method is used to apply the masking settings to the xml payload.
     * @param input A string containing well-formed xml.
     * @return A string containing xml payload after the masking settings have been applied.
     */
    public String getMasked(String input) {
        if (input == null) {
            return input;
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document document = null;
        StringWriter sw = null;

        try {
            document = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

            maskNodeTree(document.getFirstChild());

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            sw = new StringWriter();
            t.transform(new DOMSource(document), new StreamResult(sw));

        } catch (ParserConfigurationException | SAXException | IOException  | TransformerException e) {
            return input;
        }

        return sw.toString();
    }

    /**
     * A helper method to recursively search for all elements and
     * attributes and to apply masking settings.
     * @param node The root node of the document.
     */
    private void maskNodeTree(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                maskNodeTree(nodeList.item(i));
            }
        }

        if (config.containsMaskingSettingForField(node.getLocalName())) {
            node.setTextContent(config.apply(node.getLocalName(), node.getTextContent()));
        }
        if (config.isAttributesMaskEnabled() && node.hasAttributes()){
            NamedNodeMap nodeMap = node.getAttributes();
            for (int i = 0; i < nodeMap.getLength(); i++) {
                Node attrNode = nodeMap.item(i);
                if (config.containsMaskingSettingForField(attrNode.getLocalName())) {
                    attrNode.setTextContent(config.apply(attrNode.getLocalName(), attrNode.getTextContent()));
                }
            }
        }
    }

    /**
     * @return The masking configuration
     */
    public MaskingConfiguration getConfig() {
        return config;
    }

    /**
     * Set the masking configuration.
     * @param config Masking configuration to set.
     */
    public void setConfig(MaskingConfiguration config) {
        this.config = config;
    }
}

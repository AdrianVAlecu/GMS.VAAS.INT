package com.gms.datasource.summit;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gms on 6/3/2017.
 */
public class SWrapDOM {
    static private DocumentBuilderFactory factory;
    static private DocumentBuilder builder;
    static final String XMLVERSION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>";

    private static Document getDocument(String text) throws IOException, SAXException, ParserConfigurationException {
        try {
            if ( factory == null || builder == null ) {
                factory = DocumentBuilderFactory.newInstance();
                builder = factory.newDocumentBuilder();
            }

            return builder.parse(new InputSource(new StringReader(text)));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public List<List<String>> convertQueryResult(String response){

        List<List<String>> queryResult = new ArrayList<List<String>>();

        try {
            Document doc = getDocument(XMLVERSION + response);

            NodeList dataList = doc.getElementsByTagName("rs:data");

            if (dataList.getLength() > 0 ) {
                NodeList rows  = dataList.item(0).getChildNodes();

                for (int i = 0; i < rows.getLength(); i++) {
                    List<String> oneRowResult = new ArrayList<String>();
                    NamedNodeMap row = rows.item(i).getAttributes();

                    for (int j = 0; j < row.getLength(); j++) {
                        oneRowResult.add(row.item(j).getNodeValue());
                    }
                    queryResult.add(oneRowResult);
                }
            }
        }catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return queryResult;
    }

    public Map<String, String> convertZeroResult(String response){

        Map<String, String> queryResult = new HashMap<>();

        try {
            Document doc = getDocument(XMLVERSION + response);

            NodeList curveDataList = doc.getElementsByTagName("CurveData");
            if (curveDataList.getLength() > 0 ) {
                NodeList rows  = curveDataList.item(0).getChildNodes();

                for (int i = 0; i < rows.getLength(); i++) {
                    NodeList row = rows.item(i).getChildNodes();

                    queryResult.put(row.item(0).getTextContent(),
                                row.item(1).getTextContent());
                }
            }
        }catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return queryResult;
    }

}

package com.gms.datasource.summit;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import summit.etkapi_ws.SU_eToolkitAPI;
import summit.etkapi_ws.SU_eToolkitAPIException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

public class EToolKitWrapper {
	private SU_eToolkitAPI etkAPI;
	private Map<String, String> entities;

	public EToolKitWrapper(SU_eToolkitAPI etkAPI, String user, String pass, String application, String type, String dbEnv, String extraParams)  throws SU_eToolkitAPIException, Exception {
        System.out.println("We are using constructor injector injection for SU_eToolkitAPI, user: " + user +
                " pass: " + pass + " context: " + application + " type: " + type + " dbEnv: " + dbEnv + " extraParams: " + extraParams );
        this.etkAPI = etkAPI;
        entities = new HashMap<String, String>();
        try
        {
            this.etkAPI.Connect(user, pass, application, type, dbEnv, extraParams);
        }
        catch(SU_eToolkitAPIException e)
        {
            System.out.print(e.GetMsg());
        }
    }

    public void Disconnect(){
        try {
            if (this.etkAPI != null ) {
                this.etkAPI.Disconnect();
            }
        }catch(SU_eToolkitAPIException e){
            e.printStackTrace();
        }

    }

    List<List<String>> executeDBQuery(String sql) throws SU_eToolkitAPIException{
        StringBuffer outXMLResponse = new StringBuffer();
        Vector<String> messageList = new Vector<String>();

        try {
            etkAPI.Execute("s_base::DBQuery", "<Request><SummitSQL>" + sql + "</SummitSQL></Request>", outXMLResponse, messageList);
        }catch (SU_eToolkitAPIException e){
            e.printStackTrace();
            throw e;
        }

        List<List<String>> queryResult = new ArrayList<List<String>>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>" + outXMLResponse.toString())));
            NodeList dataList = doc.getElementsByTagName("rs:data");
            if (dataList.getLength() > 0 ){
                Node data = dataList.item(0);
                NodeList rows = data.getChildNodes();
                for ( int i = 0 ; i < rows.getLength() ; i ++ ){
                    List<String> oneRowResult = new ArrayList<String>();

                    Node rowsData = rows.item(i);
                    NamedNodeMap row = rowsData.getAttributes();
                    for ( int j = 0 ; j < row.getLength() ; j ++ ) {
                        Node rowItem = row.item(j);
                        oneRowResult.add(rowItem.getNodeValue());
                    }
                    queryResult.add(oneRowResult);
                }
            }

        }catch(ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }

        return queryResult;
    }

    String executeEntityCreate(String entity) throws SU_eToolkitAPIException{

        if ( entities.containsKey(entity) ) {
            return entities.get(entity);
        }

        StringBuffer outXMLResponse = new StringBuffer();
        Vector<String> messageList = new Vector<String>();

        try {
            etkAPI.Execute("s_base::EntityCreate", "<Request><EntityName>" + entity + "</EntityName></Request>", outXMLResponse, messageList);
        }catch (SU_eToolkitAPIException e){
            e.printStackTrace();
            throw e;
        }

        List<List<String>> queryResult = new ArrayList<List<String>>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //try {
        String response = outXMLResponse.toString();
        StringBuffer entityStr = new StringBuffer(response);
        entityStr.replace(response.indexOf("</Response>"), response.indexOf("</Response>") + "</Response>".length(), "");
        entityStr.replace(response.indexOf("<Response>"), response.indexOf("<Response>") + "<Response>".length(), "");
            //DocumentBuilder builder = factory.newDocumentBuilder();
            //Document doc = builder.parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>" + outXMLResponse.toString())));
            //NodeList entityList = doc.getElementsByTagName("Entity");
            //Node elem = entityList.item(0);//Your Node
            //StringWriter buf = new StringWriter();
            //Transformer xform = TransformerFactory.newInstance().newTransformer();
            //xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            //xform.setOutputProperty(OutputKeys.INDENT, "yes");
            //xform.transform(new DOMSource(elem), new StreamResult(buf));
            //String entityStr = buf.toString(); // your string

        entities.put(entity, entityStr.toString());

        //}catch(ParserConfigurationException | SAXException | IOException | TransformerException e){
        //    e.printStackTrace();
        //}

        return entities.get(entity);
    }

}

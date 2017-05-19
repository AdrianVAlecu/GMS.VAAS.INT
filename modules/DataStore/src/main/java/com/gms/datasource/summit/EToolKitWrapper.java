package com.gms.datasource.summit;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import summit.etkapi_ws.SU_eToolkitAPI;
import summit.etkapi_ws.SU_eToolkitAPIException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class EToolKitWrapper {
	private SU_eToolkitAPI etkAPI;

	public EToolKitWrapper(SU_eToolkitAPI etkAPI, String user, String pass, String application, String type, String dbEnv, String extraParams)  throws SU_eToolkitAPIException, Exception {
        System.out.println("We are using constructor injector injection for SU_eToolkitAPI, user: " + user +
                " pass: " + pass + " context: " + application + " type: " + type + " dbEnv: " + dbEnv + " extraParams: " + extraParams );
        this.etkAPI = etkAPI;
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
}

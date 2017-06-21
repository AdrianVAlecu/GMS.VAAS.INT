package com.gms.datasource.summit;

import org.springframework.core.io.ClassPathResource;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Created by gms on 6/3/2017.
 */
public class SWrapXSLT {
    Transformer entListTrans;
    static final String XMLVERSION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>";

    public SWrapXSLT(){
        try {
            InputStream xsltFile = new ClassPathResource("Stylesheet_EntList.xsl").getInputStream();
            StreamSource entListXslt = new StreamSource(xsltFile);
            //File xsltFile = new File(getClass().getClassLoader().
            //        getResource("Stylesheet_EntList.xsl").getFile());
            //StreamSource entListXslt = new StreamSource(xsltFile.getAbsolutePath());
            //StreamSource entListXslt = new StreamSource(getClass().getClassLoader().
            //        getResourceAsStream("Stylesheet_EntList.xsl"));

            TransformerFactory transFactory = TransformerFactory.newInstance();
            entListTrans = transFactory.newTransformer(entListXslt);
        } catch (TransformerConfigurationException | IOException e) {
        //} catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String applyEntList(String entityStr){
        try {
            Source tradeSource = new StreamSource(new StringReader(
                    XMLVERSION + entityStr));

            StringWriter resultWriter = new StringWriter();
            StreamResult result = new StreamResult(resultWriter);
            entListTrans.transform(tradeSource, result);

            return resultWriter.toString();
        }catch (TransformerException e) {
            e.printStackTrace();
        }
        return "";
    }
}

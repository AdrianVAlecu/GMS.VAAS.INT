package com.gms.datasource.summit;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by gms on 6/3/2017.
 */
public class SWrapFile {

    private String documentPath;

    public SWrapFile(String documentPath){
        this.documentPath = documentPath;
    }

    public void writeStringToFile(String path, String data){
        try {
            String fileName = documentPath + "/" + path;
            File file = new File(fileName);
            System.out.println("Trying to write file to disk: " + file.getCanonicalPath());
            FileUtils.writeStringToFile(new File(fileName), data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

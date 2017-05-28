package com.gms.datasource.summit;

import org.apache.commons.io.FileUtils;
import summit.etkapi_ws.SU_eToolkitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gms on 5/27/2017.
 */
public class SummitAsOfDatesDAO {
    public Map<String, String> dates;
    private EToolKitWrapper etkWrap;

    SummitAsOfDatesDAO(EToolKitWrapper etkWrap, String documentPath) throws SU_eToolkitAPIException, InterruptedException {
        this.etkWrap = etkWrap;
        dates = new HashMap<>();

        String sql = "select LocationName, max(Today) from dmLOCATION group by LocationName";
        List<List<String>> queryResult = this.etkWrap.executeDBQuery(sql);

        for(List<String> location : queryResult ) {
            dates.put(location.get(0), convertDate(location.get(1)));

            try {

                String fileName = documentPath + "/location_" + location.get(0) + ".json";
                File jsonFile = new File(fileName);
                System.out.println("Trying to write file to disk: " + jsonFile.getCanonicalPath());
                FileUtils.writeStringToFile(new File(fileName), location.get(1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String convertDate(String date){
        return date;
    }

    List<String> getValues(){
        List<String> values = new ArrayList<>();
        for(Map.Entry<String, String> location : dates.entrySet() ) {

            if (!values.contains(location.getValue())) {
                values.add(location.getValue());
            }
        }

        return values;
    }

}

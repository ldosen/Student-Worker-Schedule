import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Sheets sheet;
        Map<String, ArrayList<String>> theMap = new HashMap<>();
        Map<String, ArrayList<String>> timeKeys = new HashMap<>();
        try {
            sheet = SheetsServiceUtil.getSheetsService();
            String SPREADSHEET_ID = "1BAvWAR78ghD7UAqESnxe8V5L6xtwazJ89hkhz3waQyM";
            List<String> ranges = Arrays.asList("Sheet1");
            Sheets.Spreadsheets.Values.BatchGet request = sheet.spreadsheets().values().batchGet(SPREADSHEET_ID);
            request.setRanges(ranges);
            request.setMajorDimension("COLUMNS");
            BatchGetValuesResponse response = request.execute();
                                                          ///0             //col    ///row
            System.out.println(response.getValueRanges().get(0).getValues().get(0).get(1));
            int numberOfCols = response.getValueRanges().get(0).getValues().size();
            System.out.println(response.getValueRanges().get(0).getValues());
            ArrayList<String> times = new ArrayList<>();
            ArrayList<String> dates = new ArrayList<>();
            for (int i=0; i<response.getValueRanges().get(0).getValues().get(0).size(); i++){
                times.add( (String) response.getValueRanges().get(0).getValues().get(0).get(i));  //fills a times array up with values
            }
            for (int i=0; i<numberOfCols; i++){
                dates.add((String) response.getValueRanges().get(0).getValues().get(i).get(0));
            }
            int numberOfRows = times.size();
             // going to implement so it doesn't have to keep checking for names in same row. Once it discovers a name it will add it.
            //It almost seems unavoidable to not use these nested for loops. We should make sure to do whatever we are doing efficiently in the for loop to cut down on time.
            String time = "";
            for (int i = 0; i<numberOfCols; i++) {
                Boolean columnEmpty = response.getValueRanges().get(0).getValues().get(i).isEmpty();
                int numberOfRowsToProcess = response.getValueRanges().get(0).getValues().get(i).size();
                if (columnEmpty){
                    continue;
                }
                for (int j = 0; j < numberOfRowsToProcess; j++) {
                    Boolean timeChanged = !(times.get(j).equals(time)) && !(times.get(j) == "" );
                    if (i==0){
                        continue;

                    }
                    String name = "";
                    String timeDate = "";
                    if(timeChanged){
                        time = times.get(j);
                    }
                    boolean meetsCriteriaToAddName = (j > 0 && !(response.getValueRanges().get(0).getValues().get(i).get(j)==""));
                    if (j > 0 && !(time.isEmpty())) { //we can redo this (j>1) to account for the multiple rows that the date takes up in the real sheet.
                        timeDate = time + " " + dates.get(i);
                    }
                    if (meetsCriteriaToAddName) {
                        name = (String) response.getValueRanges().get(0).getValues().get(i).get(j); //simplify this so we don't have to do it every time.
                    }
                    if (theMap.containsKey(name) && timeDate != "") {
                        theMap.get(name).add(timeDate);
                    } else {
                        if (name != "" && timeDate != ""){
                            ArrayList<String> toAdd = new ArrayList();
                            toAdd.add(timeDate);
                            theMap.put(name, toAdd);
                        }
                    }
                    if (timeKeys.containsKey(timeDate) && timeDate != "") {
                        if (!(name.isEmpty())) {
                            timeKeys.get(timeDate).add(name);
                        }
                    }
                    else {
                        if ( !(name.isEmpty()) && !timeDate.isEmpty()){
                            ArrayList<String> toAdd = new ArrayList();
                            toAdd.add(name);
                            timeKeys.put(timeDate, toAdd);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        for(Map.Entry<String,ArrayList<String>> entry  : theMap.entrySet()){
            System.out.println("Name: " + entry.getKey());
            System.out.println("Schedule: " + entry.getValue());
        }
        for(Map.Entry<String,ArrayList<String>> entry : timeKeys.entrySet()){
            System.out.println("Time: " + entry.getKey());
            System.out.println("Who's available: " + entry.getValue());
        }
    }
}

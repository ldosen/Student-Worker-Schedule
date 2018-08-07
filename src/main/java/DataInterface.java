import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.Arrays;
import com.google.api.services.sheets.v4.model.*;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collections;

/**
 * This class deals with the API. The API stores values in a nested fashion. Therefore, we have to unpack the values into something that is readily accessible
 * We chose to read the values into two HashMaps. The PersonHashMap contains the person's name as a key and a person object as a value. TheTimeMap is a schedule that
 * uses a string that contains the date and time as a key and a time slot object as a value. In order to work properly, a person must have edit access to the spreadsheet.
 * This class is very messy because of the way the values are nested in the API's batchGet method which is the most convenient way to unpack the spreadsheet info.
 */


public class DataInterface {
    Sheets sheet;
    private int numberOfInputEntries; //WARNING SPREADSHEET must meet formatting to get an accurate number.
    private int numberOfDates; //WARNING: SPREADSHEET must meet formatting to get an accurate number.
    private int defaultMin = 5;
    private int defaultMax = 6;
    private String readSheetID = "1UHWD0gPBFV-ab7qa3FQ0pDbSYTxsrFpmXZGp71xFQwQ";
    private String writeSheetID = "1hRLbsjpvW20V1b_QytLXNQ7TqQshh7eHdbP5ZD5NrPw";
    final String inputValueOption = "RAW";
    public void getDataFromSpreadsheet(final PersonMapHash peopleMap, final TheTimeMap timeMap) {
        peopleMap.clear();
        timeMap.clear();
        try {
            sheet = SheetsServiceUtil.getSheetsService();
            String SPREADSHEET_ID = readSheetID;
            List<String> ranges = Arrays.asList("Sheet1");
            Sheets.Spreadsheets.Values.BatchGet request = sheet.spreadsheets().values().batchGet(SPREADSHEET_ID);
            request.setRanges(ranges);
            request.setMajorDimension("COLUMNS");
            BatchGetValuesResponse response = request.execute();
            ///0             //col    ///row
            System.out.println(response.getValueRanges().get(0).getValues().get(0).get(1));
            int numberOfCols = response.getValueRanges().get(0).getValues().size();
            ArrayList<String> times = new ArrayList<>();
            ArrayList<String> dates = new ArrayList<>();
            int maxRows = 0;
            for (int i = 0; i < response.getValueRanges().get(0).getValues().get(0).size(); i++) {
                times.add((String) response.getValueRanges().get(0).getValues().get(0).get(i));  //fills a times array up with values
            }
            ArrayList<String> daysToRemove = new ArrayList<>();
            daysToRemove.add("Sun");
            for (int i = 0; i < numberOfCols; i++) {
                if (response.getValueRanges().get(0).getValues().get(i).size() == 0){
                    dates.add(null);
                    continue;
                }
                String date = (String) response.getValueRanges().get(0).getValues().get(i).get(0);
                boolean removeDate = false;
                for (int itr = 0; itr< daysToRemove.size(); itr++){
                    if(date.contains(daysToRemove.get(itr))){
                        removeDate = true;
                    }
                }
                if (removeDate){
                    dates.add(null);
                    continue;
                }
                dates.add(date);
            }
            int numberOfRows = times.size();
            String time = "";
            for (int i = 0; i < numberOfCols; i++) {
                Boolean columnEmpty = response.getValueRanges().get(0).getValues().get(i).isEmpty();
                if (columnEmpty) {
                    continue;
                }
                for (int j = 0; j < numberOfRows; j++) {
                    Boolean timeChanged = !(times.get(j).equals(time)) && !(times.get(j).equals(""));
                    if (i == 0) {
                        continue;
                    }
                    if (dates.get(i) == null) {
                        continue;
                    }
                    String name = "";
                    String timeDate = "";
                    Person newPerson = new Person();
                    if (timeChanged) {
                        time = times.get(j);
                    }
                    boolean meetsCriteriaToAddName = (j > 0);
                    if (j > 0 && !(time.isEmpty())) { //we can redo this (j>1) to account for the multiple rows that the date takes up in the real sheet.
                        timeDate = time + " " + dates.get(i);
                    }
                    if (meetsCriteriaToAddName) {
                        if (!(j >= response.getValueRanges().get(0).getValues().get(i).size())) {
                            name = (String) response.getValueRanges().get(0).getValues().get(i).get(j);
                            name = name.trim();
                        }
                        newPerson.setName(name);
                    }
                    try {
                        timeMap.tryToAddPersonToAvailableWithMap(newPerson, defaultMin, defaultMax, timeDate, time, dates.get(i), peopleMap);
                    }catch (RuntimeException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }   catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public UpdateValuesResponse updateValues(final String range, final List<List<Object>> _values) throws IOException {
        // [START sheets_update_values]
        List<List<Object>> values;
        // [START_EXCLUDE silent]
        values = _values;
        // [END_EXCLUDE]
        ValueRange body = new ValueRange()
                .setValues(values);
        UpdateValuesResponse result =
                sheet.spreadsheets().values().update(writeSheetID, range, body)
                        .setValueInputOption(inputValueOption)
                        .execute();
        System.out.printf("%d cells updated.", result.getUpdatedCells());
        // [END sheets_update_values]
        return result;
    }

    public int getDefaultMin(){
        return defaultMin;
    }
    public int getDefaultMax(){
        return defaultMax;
    }
    public void setDefaultMin(int i){
        defaultMin = i;
    }
    public void setDefaultMax(int i){
        defaultMax = i;
    }
    public String getReadSheetID() {
        return readSheetID;
    }
    public void setReadSheetID(String readSheetID) {
        String unchanged = this.readSheetID;
        if (readSheetID == null || readSheetID.equals("")){
            this.readSheetID = unchanged;
        }
        else{
            this.readSheetID = readSheetID;
        }
    }
    public String getWriteSheetID() {
        return writeSheetID;
    }
    public void setWriteSheetID(String writeSheetID) {
        String unchanged = this.writeSheetID;
        if (writeSheetID == null || writeSheetID.equals("")){
            this.writeSheetID = unchanged;
        }
        else{
            this.writeSheetID = writeSheetID;
        }

    }
}
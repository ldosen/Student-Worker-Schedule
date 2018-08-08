import com.google.api.services.sheets.v4.Sheets;
import java.util.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        final Sheets sheet;
        final PersonMapHash peopleHash = new PersonMapHash(); // This is necessary to avoid infinite recursion utilizing the Comparator on the String key. It's worth the sacrirfice in space. HashMap is constant lookup time versus O(log n)
        final TheTimeMap schedule = new TheTimeMap();
        final DataInterface theDataInterface = new DataInterface();
        theDataInterface.getDataFromSpreadsheet(peopleHash, schedule);
        final ArrayList<String> timeToRemove = new ArrayList();
        final String inputValueOption = "RAW";
        final String[] writeRanges = new String[] {
                "Sheet1!B3:B12", "Sheet1!B10:B15", "Sheet1!B17:B22", "Sheet1!B24:B30", "Sheet1!B31:B36",
                "Sheet1!C3:C12", "Sheet1!C10:C15", "Sheet1!C17:C22", "Sheet1!C24:C30", "Sheet1!C31:C36",
                "Sheet1!D3:D12", "Sheet1!D10:D15", "Sheet1!D17:D22", "Sheet1!D24:D30", "Sheet1!D31:D36",
                "Sheet1!E3:E12", "Sheet1!E10:E15", "Sheet1!E17:E22", "Sheet1!E24:E30", "Sheet1!E31:E36",
                "Sheet1!F3:F12", "Sheet1!F10:F15", "Sheet1!F17:F22", "Sheet1!F24:F30", "Sheet1!F31:F36"};
        /*
         Scanner input = new Scanner(System.in);
        System.out.println("Enter the ID of the spreadsheet to read from:");
        theDataInterface.setReadSheetID(input.nextLine());
         System.out.println("Enter the ID of the spreadsheet to write to:");
        theDataInterface.setWriteSheetID(input.nextLine());
        */
        for (Map.Entry<String, Person> entry : peopleHash.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue().getNumberInitiallyAvailable());
        }
        for (Map.Entry<String, Slot> entry : schedule.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().getPeopleAvailableNamems() + entry.getValue().getPeopleAvailableNamems().size());
        }
        Conflictable.applyThisCharRuleToEntireSchedule(schedule, 'A', "9:45", "10:45");
        Conflictable.applyThisCharRuleToEntireSchedule(schedule, 'B', "12:45", "1:15");
        for (Map.Entry<String, Slot> entry : schedule.entrySet()) {
            ArrayList<Person> guides = entry.getValue().getPeopleAvailable();
            while (!(entry.getValue().atMax()) && !(guides.isEmpty())) {
                Collections.sort(guides, new ComparePerson());
                Person current = guides.get(0);
                entry.getValue().addPersontoPeopleWorking(current);
                guides.remove(current);
            }
        }
        for (Map.Entry<String, Slot> entry : schedule.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().getPeopleWorkingNames());
        }
        for (Map.Entry<String, Person> entry : peopleHash.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue().getNumberScheduled());
        }
        /*
        for (Map.Entry<String, Slot> entry : schedule.entrySet()){
            for (String range : writeRanges){
                List<List<Object>> values = Collections.singletonList(Arrays.asList(entry.getValue().getPeopleWorkingNames().toArray()));
                theDataInterface.updateValues(range, values);
            }
        }
        */
        //for testing purposes only!
        List<List<Object>> values = Collections.singletonList(Arrays.asList(schedule.getTimeSlot("9:45 AM Mon").getPeopleWorkingNames().toArray()));
        theDataInterface.updateValues("Sheet1!B3:B12", values);
    }
}
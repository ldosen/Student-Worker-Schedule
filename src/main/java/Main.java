import com.google.api.services.sheets.v4.Sheets;
import java.util.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Sheets sheet;
        PersonMapHash peopleHash = new PersonMapHash(); // This is necessary to avoid infinite recursion utilizing the Comparator on the String key. It's worth the sacrirfice in space. HashMap is constant lookup time versus O(log n)
        TheTimeMap schedule = new TheTimeMap();
        DataInterface theDataInterface = new DataInterface();
        theDataInterface.getDataFromSpreadsheet(peopleHash, schedule);
        ArrayList<String> timeToRemove = new ArrayList();
        final String inputValueOption = "RAW";
        /*
         Scanner input = new Scanner(System.in);
        System.out.println("Enter the ID of the spreadsheet to read from:");
        theDataInterface.setReadSheetID(input.nextLine());
         System.out.println("Enter the ID of the spreadsheet to write to:");
        theDataInterface.setWriteSheetID(input.nextLine());
        */

        for (Map.Entry<String, Slot> entry : schedule.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue().getPeopleAvailableNamems() + entry.getValue().getPeopleAvailableNamems().size());
        }
        for (Map.Entry<String, Slot> entry : schedule.entrySet()) {
            ArrayList<Person> guides = entry.getValue().getPeopleAvailable();
            while (entry.getValue().getNumberOfPeopleWorking() < entry.getValue().getMax() && !(guides.isEmpty())) {
                Collections.sort(guides, new ComparePerson());
                Person current = guides.get(0);
                entry.getValue().addPersontoPeopleWorking(current);
                guides.remove(current);
            }
        }
        for (Map.Entry<String, Slot> entry : schedule.entrySet()) {
            for (String str: entry.getValue().getPeopleAvailableNamems()){
                System.out.println(str);
            }
        }
    }
}

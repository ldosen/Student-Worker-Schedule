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
        for (Map.Entry<String, Person> entry : peopleHash.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue().getNumberInitiallyAvailable());
        }
        for (Map.Entry<String, Slot> entry : schedule.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().getPeopleAvailableNamems() + entry.getValue().getPeopleAvailableNamems().size());
        }
        int integer = 0;
        String date = "";
        for (Map.Entry<String, Slot> entry : schedule.entrySet()){
            String oldDate = date;
            date = entry.getValue().getDate();
            Character character = 'A';
            if (oldDate.equalsIgnoreCase(date)){
                if (entry.getKey().contains("9:45") || entry.getKey().contains("10:45")){
                    entry.getValue().addConflictMarker(character, integer);
                }
            }
            else {
                integer++;
                if (entry.getKey().contains("9:45") || entry.getKey().contains("10:45")) {
                    System.out.println("Integer" + integer);
                    entry.getValue().addConflictMarker(character, integer);
                }
            }
        }
        int integerTwo = 0;
        String dateTwo = "";
        for (Map.Entry<String, Slot> entry : schedule.entrySet()){
            String oldDate = dateTwo;
            dateTwo = entry.getValue().getDate();
            System.out.println(dateTwo);
            System.out.println("Hey" + oldDate);
            Character character = 'C';
            if (oldDate.equalsIgnoreCase(dateTwo)){
                if (entry.getKey().contains("12:45") || entry.getKey().contains("1:15")){
                    entry.getValue().addConflictMarker(character, integerTwo);
                }
            }
            else {
                integerTwo++;
                System.out.println("BRUDDA");
                if (entry.getKey().contains("12:45") || entry.getKey().contains("1:15")){
                    System.out.println("Dog");
                    entry.getValue().addConflictMarker(character, integerTwo);
                }
            }
        }
        for (Map.Entry<String, Slot> entry : schedule.entrySet()) {
            ArrayList<Person> guides = entry.getValue().getPeopleAvailable();
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().getPeopleAvailableNamems());
            for (Person person : guides){
                System.out.println(person.getName());
                entry.getValue().displayConflictMarkers();
                System.out.println("--------");
                person.displayConflictMarkers();
            }
            while (entry.getValue().getNumberOfPeopleWorking() < entry.getValue().getMax() && !(guides.isEmpty())) {
                Collections.sort(guides, new ComparePerson());
                Person current = guides.get(0);
                entry.getValue().addPersontoPeopleWorking(current); //May or may not add the person.
                guides.remove(current);
            }
        }
        for (Map.Entry<String, Slot> entry : schedule.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().getPeopleWorkingNames());
        }
        for (Map.Entry<String, Person> entry : peopleHash.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue().getNumberScheduled());
        }


        System.out.println("---------------------------------");
    }
}
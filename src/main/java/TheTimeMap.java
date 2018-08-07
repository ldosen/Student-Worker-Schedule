import exception.PersonDuplicateException;
import exception.TimeDateInvalidFormatException;

import java.util.*;

public class TheTimeMap implements Schedule {
    private final LinkedHashMap<String, Slot> timeSlotMap;
    private int numberOfTimeSlots;

    public TheTimeMap() {
        timeSlotMap = new LinkedHashMap<>(491);
        numberOfTimeSlots = 0;
    }

    public TheTimeMap(int initialCapacity) {
        timeSlotMap = new LinkedHashMap<>(initialCapacity);
        numberOfTimeSlots = 0;
    }

    public TheTimeMap(int initialCapacity, float loadFactor) {
        timeSlotMap = new LinkedHashMap<>(initialCapacity, loadFactor);
        numberOfTimeSlots = 0;
    }

    public boolean containsTimeSlot(Slot value) {
        return timeSlotMap.containsValue(value);
    }

    //* This is the one that we would use to see if there is a time on the schedule*/
    public boolean containsTime(String key) {
        return timeSlotMap.containsKey(key);
    }

    /**
     * display all of the time slots followed by all of the people working at the time and who is available at the time.
     */
    @Override
    public void displaySchedule() {

        for (Map.Entry<String, Slot> entry : entrySet()){
            System.out.println("Time " + entry.getValue().getTime() + " " + entry.getValue().getDate());
            System.out.println("People who are available to fill the time slot: " + entry.getValue().getPeopleAvailableNamems());
            System.out.println("People who are working this time slot: " + entry.getValue().getPeopleWorkingNames());
        }
    }

    /**
     * display the people's names who are available for a given time and date
     *
     * @param timeDate
     */
    @Override
    public void displayPeopleAvailable(String timeDate) {
        Slot entry = timeSlotMap.get(timeDate);
        if (entry == null){
            throw new TimeDateNotFoundException("Time Date not found!");
        }
        System.out.println("People who are available to fill the time slot: " + entry.getPeopleAvailableNamems());
    }

    /**
     * display the people's names who are working for a time and date.
     *
     * @param timeDate
     */
    @Override
    public void displayPeopleWorking(String timeDate) {
        Slot entry = timeSlotMap.get(timeDate);
        if (entry == null){
            throw new TimeDateNotFoundException("Time date not found!");
        }

        System.out.println("People who are available to fill the time slot: " + entry.getPeopleWorkingNames());
    }

    /**
     * displays a times min and max number of people who need to/can work a time.
     * @param timeDate
     */
    @Override
    public void getMinAndMax(String timeDate) {
        Slot slotToCheck = timeSlotMap.get(timeDate);

        if (slotToCheck == null){
            throw new TimeDateNotFoundException("Time date not found!");
        }

        int minimum = slotToCheck.getMinimumRequired();
        int maximum = slotToCheck.getMax();
        //print min
        System.out.println("The minimum number of people who need to work at " + timeDate + " is " + minimum);

        System.out.println("The maximum number of people who can work  this time is " + maximum);

    }

    public Slot getTimeSlot(String key) {
        return timeSlotMap.get(key);
    }

    public Slot removeTimeSlot(String key) {
        numberOfTimeSlots--;
        return timeSlotMap.remove(key);
    }

    public boolean modifyTimeSlot(String key, int min, int max) {
        if (timeSlotMap.containsKey(key)) {
            Slot theSlot = timeSlotMap.get(key);
            theSlot.setMinimumRequired(min);
            theSlot.setMax(max);
            return true;
        } else {
            return false;
        }
    }
    public void clear(){
        timeSlotMap.clear();
        numberOfTimeSlots = 0;
    }

    public boolean putNewTimeSlotOnSchedule(String time, Slot timeSlotObj) {
        if (timeSlotMap.containsKey(time)) {
            return false;
        }
        timeSlotMap.put(time, timeSlotObj);
        numberOfTimeSlots++;
        return true;
    }
    public Set<String> setOfTimeStrings() {
        return timeSlotMap.keySet();
    }
    public Collection<Slot> collectionOfSlots() {
        return timeSlotMap.values();
    }
    /* This method guarantees the order of the entries in the entrySet will be the same as they order they
    were inserted when we iterate over them.
     */
    public Set<Map.Entry<String, Slot>> entrySet() {
        return timeSlotMap.entrySet();
    }


    public PriorityQueue<Slot> slotPriorityQueue(Comparator<Slot> slotComparator){
        PriorityQueue<Slot> slotPriorityQueue = new PriorityQueue<>(numberOfTimeSlots, slotComparator);
        slotPriorityQueue.addAll(timeSlotMap.values());
        return slotPriorityQueue;
    }

    public void deleteDayTimes(ArrayList<String> dayTimes){
        Iterator<String> itr = timeSlotMap.keySet().iterator();
        while (itr.hasNext()){
            for (String str : dayTimes){
                if (itr.next().contains(str)) {
                    itr.remove();
                    numberOfTimeSlots--;
                }
            }
        }
    }

    public boolean tryToAddPersonToAvailableWithMap(Person person, int min, int max, String timeDate, String time, String date, PersonMapHash mapToReadAndUpdate) throws TimeDateInvalidFormatException {
        if (timeDate == null || timeDate.equals("")) {
            throw new TimeDateInvalidFormatException("Time Date is null or empty");
        }
        if (person == null || person.getName() == null ||  person.getName().equals("")){
            throw new IllegalArgumentException("Person or Name of person is null or empty");
        }

        if (mapToReadAndUpdate.containsKey(person.getName())) {
            person = mapToReadAndUpdate.get(person.getName());
        }
        return eliminateDry(person, min, max, timeDate, time, date, mapToReadAndUpdate);
    }

    private boolean eliminateDry(Person person, int min, int max, String timeDate, String time, String date, PersonMapHash personMapHash){

        this.putNewTimeSlotOnSchedule(timeDate, new Slot(min, max, date, time));

        personMapHash.put(person.getName(), person);
        if (this.getTimeSlot(timeDate).containsInAvailable(person)) {

            throw new PersonDuplicateException("The person existing in the time slot already");

        }
        this.getTimeSlot(timeDate).addPersonToPeopleAvailable(person);

        return true;

    }
}
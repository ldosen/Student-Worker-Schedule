import exception.PersonDuplicateException;
import exception.TimeDateInvalidFormatException;
import exception.TimeDateNotFoundException;

import java.util.*;

/**
 * This class implements the schedule interface. It serves as the master schedule for our main method. It's key is a string that represents a date and time. It's values are slot objects.
 */

public class TheTimeMap implements Schedule {
    private final LinkedHashMap<String, Slot> timeSlotMap;
    private int numberOfTimeSlots;

    public TheTimeMap() {
        timeSlotMap = new LinkedHashMap<>(491);
        numberOfTimeSlots = 0;
    }

    public TheTimeMap(final int initialCapacity) {
        timeSlotMap = new LinkedHashMap<>(initialCapacity);
        numberOfTimeSlots = 0;
    }

    public TheTimeMap(final int initialCapacity, final float loadFactor) {
        timeSlotMap = new LinkedHashMap<>(initialCapacity, loadFactor);
        numberOfTimeSlots = 0;
    }

     /**
     *
     * @param key The time and date string to check if this time map has.
     * @return True if the schedule contains the time and date. False if the time is not on the schedule
     */
    public boolean containsTime(final String key) {
        return timeSlotMap.containsKey(key);
    }

    /**
     * display all of the time slots followed by all of the people working at the time and who is available at the time.
     */
    @Override
    public void displaySchedule() {
        for (final Map.Entry<String, Slot> entry : entrySet()){
            System.out.println("Time " + entry.getValue().getTime() + " " + entry.getValue().getDate());
            System.out.println("People who are available to fill the time slot: " + entry.getValue().getPeopleAvailableNamems());
            System.out.println("People who are working this time slot: " + entry.getValue().getPeopleWorkingNames());
        }
    }

    /**
     * display the people's names who are available for a given time and date
     *
     * @param timeDate A string representing the time and date.
     */
    @Override
    public void displayPeopleAvailable(final String timeDate) {
        final Slot entry = timeSlotMap.get(timeDate);
        if (entry == null){
            throw new TimeDateNotFoundException("Time Date not found!");
        }
        System.out.println("People who are available to fill the time slot: " + entry.getPeopleAvailableNamems());
    }

    /**
     * display the people's names who are working for a time and date.
     *
     * @param timeDate A string representing the time and date
     */
    @Override
    public void displayPeopleWorking(final String timeDate) {
        final Slot entry = timeSlotMap.get(timeDate);
        if (entry == null){
            throw new TimeDateNotFoundException("Time date not found!");
        }

        System.out.println("People who are available to fill the time slot: " + entry.getPeopleWorkingNames());
    }

    /**
     * displays a times min and max number of people who need to/can work a time.
     * @param timeDate A string representing the time and date
     */
    @Override
    public void getMinAndMax(final String timeDate) {
        final Slot slotToCheck = timeSlotMap.get(timeDate);

        if (slotToCheck == null){
            throw new TimeDateNotFoundException("Time date not found!");
        }

        final int minimum = slotToCheck.getMinimumRequired();
        final int maximum = slotToCheck.getMax();
        //print min
        System.out.println("The minimum number of people who need to work at " + timeDate + " is " + minimum);

        System.out.println("The maximum number of people who can work  this time is " + maximum);

    }

    /**
     *
     * @param key The time and date String that corresponds to the slot slot object to retrieve
     * @return A time slot object with the given time and date
     */
    public Slot getTimeSlot(final String key) {
        return timeSlotMap.get(key);
    }

    /**
     * Removes the time Slot from the map if it is present.
     * @param key A string representing the time and date.
     * @return The time slot object that was removed or null if no time slot object was removed.
     */
    public Slot removeTimeSlot(final String key) {
        numberOfTimeSlots--;
        return timeSlotMap.remove(key);
    }

    /**
     * Modifies a time slots min and max.
     * @param key A string representing the time and date
     * @param min An integer specify the minimum required number of people that need to be working
     * @param max An integer specifying the maximum or ideal number of people working in the time slot.
     * @return True if the slot was found and modified. False if the time slot was not found and modified.
     */
    public boolean modifyTimeSlot(final String key, final int min, final int max) {
        if (timeSlotMap.containsKey(key)) {
            final Slot theSlot = timeSlotMap.get(key);
            theSlot.setMinimumRequired(min);
            theSlot.setMax(max);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clears the schedule and resets the numberOfTimeSlots to 0.
     */
    public void clear(){
        timeSlotMap.clear();
        numberOfTimeSlots = 0;
    }

    /**
     * Attempts to put a new time on the schedule if it is not already present.
     * @param time A string representing the time and date
     * @param timeSlotObj actual slot object to be put on the schedule
     * @return True if the time and date was not on the schedule and the slot was inserted as a value. False if the time and date was already on the schedule.
     */
    public boolean putNewTimeSlotOnSchedule(final String time, final Slot timeSlotObj) {
        if (timeSlotMap.containsKey(time)) {
            return false;
        }
        timeSlotMap.put(time, timeSlotObj);
        numberOfTimeSlots++;
        return true;
    }

    /**
     *
     * @return A set of strings representing the time and date keys on the timeSlotMap.
     */
    public Set<String> setOfTimeStrings() {
        return timeSlotMap.keySet();
    }

    /**
     *
     * @return A collection of time slot objects on the schedule.
     */
    public Collection<Slot> collectionOfSlots() {
        return timeSlotMap.values();
    }

    /**
     *
     * @return An entry set of the timeSlotMap.
     */
    public Set<Map.Entry<String, Slot>> entrySet() {
        return timeSlotMap.entrySet();
    }

    /**
     * Deletes the day times from the schedule that are contained in the dayTimes parameter.
     * @param dayTimes An arrayList of dayTimes to delete from the schedule
     */
    public void deleteDayTimes(final ArrayList<String> dayTimes){
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

    /**
     * This method is used in the data interface to take the the input data and insert it into a TimeMapObject and a PersonHashMap object.
     * @param person A person object
     * @param min Min to set the slot to insert to
     * @param max Min to set the slot to max on
     * @param timeDate timeDate to add to the Map or check if on the map
     * @param time time from timeDate
     * @param date date from timeDate
     * @param mapToReadAndUpdate The personMapHash to read and update people from.
     * @return True if person was added to a time slot, false if something prevented the person(for example null values) from being added.
     * @throws TimeDateInvalidFormatException If the timeDate is null or empty
     */
    public boolean tryToAddPersonToAvailableWithMap(Person person, final int min, final int max, final String timeDate, final String time, final String date, final PersonMapHash mapToReadAndUpdate) throws TimeDateInvalidFormatException {
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

    private boolean eliminateDry(final Person person, final int min, final int max, final String timeDate, final String time, final String date, final PersonMapHash personMapHash){

        this.putNewTimeSlotOnSchedule(timeDate, new Slot(min, max, date, time));

        personMapHash.put(person.getName(), person);
        if (this.getTimeSlot(timeDate).containsInAvailable(person)) {

            throw new PersonDuplicateException("The person exists in the time slot already");

        }
        this.getTimeSlot(timeDate).addPersonToPeopleAvailable(person);

        return true;

    }
}
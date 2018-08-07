import java.util.*;

public class Slot extends Conflictable {
    private final ArrayList<Person> peopleAvailable = new ArrayList<>();
    private final ArrayList<Person> peopleWorking = new ArrayList<>();
    private int numberOfPeopleAvailable = 0;
    private int numberOfPeopleWorking = 0;
    private int minimumRequired = 0;
    private int max;
    private int leftToMax = max - numberOfPeopleWorking;
    private String date;
    private String time;

    /**
     *  The default must be provided in the client code
     */
    private Slot()
    {
        max = 6;
        minimumRequired = 5;
        date = "";
        time = "";
    }
    public Slot(final int min, final int max, final String date, final String time){
        if (min < 0 || max < 0 ){
            throw new IllegalArgumentException("Min and max both need to be at least zero");
        }
        this.minimumRequired = min;
        this.max = max;
        this.date = date;
        this.time = time;
    }

    /**
     * private not allowed, the default must be provided in the client code
     * @param date
     * @param time
     */
    private Slot(final String date, final String time){
        this(5, 6, date, time);
    }

    /**
     *
     * @return The minimum number of people that should be working this time slot. This may not be achievable based on all of the tour guides' availability.
     */
    public int getMinimumRequired(){
        return minimumRequired;
    }

    /**
     *
     * @return The number of people working during this time slot.
     */
    public int getNumberOfPeopleWorking(){
        return numberOfPeopleWorking;
    }

    /**
     *
     * @return The max or ideal number of people that can work this shift.
     */
    public int getMax(){
        return max;
    }

    /**
     *
     * @return The date that this slot is on
     */
    public String getDate(){
        return date;
    }

    /**
     *
     * @return True if the slot is at or beyond it's maximum number of people. For our purposes it should not go beyond the max.
     */
    public boolean atMax(){
        return numberOfPeopleWorking >= max;
    }

    /**
     *
     * @return An ArrayList of people who are available for this time slot.
     */
    public ArrayList<Person> getPeopleAvailable() {
        final ArrayList<Person> people = new ArrayList<>(peopleAvailable.size());
        people.addAll(peopleAvailable);
        return people;
    }

    /**
     *
     * @return An ArrayList of Strings that are the names of the people available for this time slot.
     */
    public ArrayList<String> getPeopleAvailableNamems() {
        final ArrayList<String> people = new ArrayList<>(peopleAvailable.size());
        for (int i = 0; i < peopleAvailable.size(); i++) {
            people.add(peopleAvailable.get(i).getName());
        }
        return people;
    }

    /**
     *
     * @return An ArrayList of Strings that are the names of the people who are working this time slot.
     */
    public ArrayList<String> getPeopleWorkingNames() {
        final ArrayList<String> people = new ArrayList<>(peopleWorking.size());
        for (final Person person : peopleWorking) {
            people.add(person.getName());
        }
        return people;
    }

    /**
     *
     * @return True if the number of people working this time slot is greater than or equal to the minimum number of people who should be working this time slot.
     */
    public boolean isFilledToMin() {
        return numberOfPeopleWorking >= minimumRequired;
    }

    /**
     *
     * @return The number of people working this time slot.
     */
    public int getNumberOfPeople() {
        return numberOfPeopleWorking;
    }

    /**
     * This method attempts to add a person to the people who are available. If there is a conflict or if the person is already available, the person will not be added.
     * @param person The person to add to the ArrayList of people who are available
     */
    public void addPersonToPeopleAvailable(final Person person) {
        if (this.checkForConflicts(person)){
            System.out.println("There is a conflict that prevents this person from being available at this time");
            return;
        }
        if (containsInAvailable(person)) {
            System.out.println("Person is already in available!");
            return;
        }
        peopleAvailable.add(person);
        person.addTimeFree(this.time + " " + this.date);
        numberOfPeopleAvailable++;
    }

    /**
     * This considers a peron's capacity, if they are already working the time slot, and if there is a conflict with another time before adding a person to the Arraylist of people who are working the time slot.
     * @param person The person to add to the ArrayList of people working this time slot.
     * @return True if the person was successfully added to the time slot. False if the person was not added to the time slot for any number of reasons.
     */
    public boolean addPersontoPeopleWorking(final Person person) {
        if (containsInWorking(person)){
            return false;
        }
        if (this.checkForConflicts(person)) {
            return false;
        }
        if (person.atCapacity()){
            return false;
        } else{
            peopleWorking.add(person);
            person.addTimeWorking(this.time + " " + this.date);
            this.addAllConflictMarkersToObj(person);
            numberOfPeopleWorking++;
            return true;
        }
    }

    /**
     *
     * @param person The person to check if they are contained in the ArrayList of people working for this time slot.
     * @return True if the person is in peopleWorking and false if they are not.
     */
    public boolean containsInWorking(final Person person){
        boolean found = false;
        for (final Person personItr : peopleWorking) {
            if (personItr.equals(person)){
                found = true;
            }
        }
        return found;
    }

    /**
     *
     * @param person The person to remove from the list of people who were initially available.
     * @return True if the person was contained in peopleAvailable and removed. False if the person was not contained in peopleAvailable and not successfully removed.
     */
    public boolean removePersonFromPeopleAvailable(final Person person) {
        Boolean removed = peopleAvailable.remove(person);
        if (removed) {
            person.removeTimeFree(this.time + " " + this.date);
            numberOfPeopleAvailable--;
        }
        return removed;
    }

    /**
     *
     * @param person The person to remove from the list of people who are working this time slot.
     * @return True if the person was contained in peopleWorking and removed. False if the person was not contained in peopleWorking and not successfully removed.
     */
    public boolean removePersonFromPeopleWorking(final Person person) {
        Boolean removed = peopleWorking.remove(person);
        if (removed) {
            numberOfPeopleWorking--;
            person.removeTimeWorking(this.time + " " + this.date);
            this.removeLinkedConflictsFromOtherConflictable(person);
        }
        return removed;
    }

    /**
     *
     * @return True if nobody is working this time slot. False if at least one person is working this time slot.
     */
    public boolean nobodyWorking() {
        return numberOfPeopleWorking > 0;
    }

    /**
     *
     * @param person The person to check if they are in peopleAvailable
     * @return True if the person is in peopleAvailable. False if the person was not found.
     */
    public boolean containsInAvailable(final Person person) {
        boolean found = false;
        for (final Person personItr : peopleAvailable) {
            if (personItr.equals(person)){
                found = true;
            }
        }
        return found;
    }

    /**
     *
     * @param otherConflictable Another conflictable object
     * @return True if there is a conflict between the two conflictable objects(this and the otherConflictable). False if there is not a conflict.
     */
    public boolean checkForConflicts(final Conflictable otherConflictable) {
        return super.checkForPotentialConflicts(otherConflictable);
    }

    /**
     * Creates a new conflict marker object with the specified character and int value and attempts to add it to the slot. If it is already present the method will return false.
     * @param character The character value of the conflict marker to add.
     * @param i The int value of the conflict marker to add.
     * @return False if the conflict marker is already present. True if the conflict marker was successfully added.
     */
    public boolean addConflictMarker(final Character character, final int i) {
        final ConflictMarker conflictMarkerToAdd = new ConflictMarker(character, i);
        return super.addConflictMarkerToInstance(conflictMarkerToAdd);
    }

    /**
     *  Removes the conflict marker with the specified character and int values from all of the people in the slot and the slot itself.
     * @param character The character value of a conflict marker
     * @param i The int value of a conflict marker.
     * @return True if the conflict marker was found and removed from this slot. False if the conflcit marker was not found and removed from this slot.
     */
    public boolean removeAllConflictMarkerRelatedToSlotFromAll(final Character character, final int i) {
       final ConflictMarker conflictMarkerToRemove = new ConflictMarker(character, i);
        for (final Person person : peopleAvailable) {
            // if (person.containsMarker) // TODO
            person.removeConflictMarker(character, i);
        }
        for (final Person person : peopleWorking) {
            // if (person.containsMarker) // TODO
            person.removeConflictMarker(character, i);
        }
        return super.removeConflictMarkerFromInstance(conflictMarkerToRemove);
    }

    /**
     *  Sets the maximum number of people that should be working this time slot.
     * @param i The max value to set for this slot.
     */
    public void setMax(final int i){
        if (i < 0){
            throw new IllegalArgumentException("Please enter a maximum value equal to or above zero");
        }
        max = i;
    }

    /**
     * Sets the minimum number of people that should be working this time slot.
     * @param i The min value to set for this slot.
     */
    public void setMinimumRequired(final int i){
        if (i < 0 ){
            throw new IllegalArgumentException("Please enter a minimum value equal to or above zero");
        }
        minimumRequired = i;
    }

    /**
     *
     * @return A string that represents the time that this slot is on.
     */
    public String getTime(){
        return time;
    }
}
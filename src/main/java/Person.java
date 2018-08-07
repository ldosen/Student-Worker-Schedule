import java.util.*;

/**
 * A person object that represents a person on the schedule.
 */

public class Person extends Conflictable{
    private static int capacity = 110;
    private int numberInitiallyAvailable;
    private int numberScheduled = 0;
    private String name;
    private final HashSet<String> timesFree = new HashSet<>();
    private final HashSet<String> timesWorking = new HashSet<>();


    public Person(){
        this.name = "";
        numberInitiallyAvailable = 0;
    }
    /**
     * Constructor that sets a persons name field. It converts it to lower case to avoid any issues with the input.
     */
    public Person(String name){
        this.name = name.toLowerCase();
        numberInitiallyAvailable = 0;
    }

    /**
     *
     * @return An ArrayList of Strings with the times a person was initially free.
     */
    public ArrayList<String> getTimesFree(){
        ArrayList<String> arrayToReturn = new ArrayList<>(timesFree.size());
        arrayToReturn.addAll(timesFree);
        return arrayToReturn;
    }

    /**
     *
     * @return An ArrayList of Strings with the times a person is working.
     */
    public ArrayList<String> getTimesWorking(){
        ArrayList<String> arrayToReturn = new ArrayList<>(timesFree.size());
        arrayToReturn.addAll(timesFree);
        return arrayToReturn;
    }

    /**
     * Add's the timeDate to a person's HashSet of time's free.
     * @param timeDate The time and Date that a person is free.
     * @Post The p
     */
    public void addTimeFree(String timeDate){
            timesFree.add(timeDate);
            numberInitiallyAvailable++;
        }

    /**
     * Remove's a time from the person's HashSet of time's free
     * @param timeDate The time and date that a person is no longer free
     * @post The Person's timesFree HashSet will not contain the timeDate
     *
     */
    public void removeTimeFree(String timeDate){
        timesFree.remove(timeDate);
    }

    /**
     * Add's a time to a person's HashSet of the time's they are working.
     * @param timeDate The time and date that a person is now working
     * @post The person's timesWorking HashSet will now contain the timeDate string.
     */
    public void addTimeWorking(String timeDate){
        timesWorking.add(timeDate);
        numberScheduled++;
    }

    /**
     * Determines if a person is at or higher than their max number of tours they can work.
     * @return True if a person is at or higher than their capacity. False if a person is not.
     */
    public boolean atCapacity(){
        return numberScheduled >= capacity;
    }

    /**
     *
     * @return An integer value that represents the number of time's this person is working.
     */
    public int getNumberScheduled() {
        return numberScheduled;
    }

    /**
     *
     * @return An integer value that represents the number of times this person was initially available after the data was first collected.
     */
    public int getNumberInitiallyAvailable(){
        return numberInitiallyAvailable;
    }

    /**
     *
     * @return A string that represents the person's name
     */
    public String getName(){
        return name;
    }

    /**
     * Set's the name field to the specified string after converting it to lower case
     * @param newName The name to give a person object
     * @post Person's name field will now be newName
     */
    public void setName(String newName){
        this.name = newName.toLowerCase();
    }

    /**
     *
     * @param character The character that must be passed to the Conflict Marker Object
     * @param i An integer that must be passed to the Conflict Marker Object
     * @return True if the conflict Marker was successfully added to a person. False if the conflict marker was not added.
     * @Post conflictMarker will be present in the person object.
     */
    public boolean addConflictMarker(Character character, int i){
        boolean added = false;
        ConflictMarker conflictMarker = new ConflictMarker(character, i);
        System.out.println("Attempting to add a ConflictMarker object " + conflictMarker.hashCode() + " to person " + this.getName());
        added = this.addConflictMarkerToInstance(conflictMarker);
        if (added){
            System.out.println("Marker was successfully added");
        }
        else {
            System.out.println("Marker already present");
        }
        return added;
    }

    /**
     *  This method is unsupported by the Person class.
     * @param conflictMarker A conflict marker object.
     * @return Always returns false.
     */
    @Override
    public boolean removeConflictMarkerFromAllConflictMarkers(ConflictMarker conflictMarker){
        System.out.println("Unsupported operation for person. If you intended to remove this type of conflict from the master set of conflicts please use another class that supports this operation like slot.");
        return false;
    }

    /**
     * This method is not supported by the Person class.
     * @param conflictable Another Conflictable object
     */
    @Override
    public void removeLinkedConflictsFromOtherConflictable(Conflictable conflictable){
        System.out.println("removeLinkedConflictsFromOtherConflictable is not supported by class Person");
    }

    /**
     *
     * @param character The conflict Marker's character
     * @param i The conflict Marker's int
     * @return True if the conflict marker was removed. False if the conflict marker was not removed from the person.
     * @post ConflictMarker will not be present in the person object.
     */
    public boolean removeConflictMarker(Character character, int i) {
        ConflictMarker conflictMarkerToRemove = new ConflictMarker(character, i);
        System.out.println("Attempting to remove a conflict marker " + conflictMarkerToRemove.hashCode() + "from person " + this.getName());
        boolean removed = this.removeConflictMarkerFromInstance(conflictMarkerToRemove);
        if (removed){
            System.out.println("Marker was successfully removed from person");
        }
        if (!removed){
            System.out.println("Marker was not found");
        }
        return removed;
    }
    public void decrementNumberAvailable(){
        if (numberInitiallyAvailable <= 0) {
            throw new IllegalStateException("Person cannot have a negative number of times available");
        }
        numberInitiallyAvailable--;
    }
    public void removeTimeWorking(String dateTime){
        timesWorking.remove(dateTime);
        numberScheduled--;
    }

    /**
     * Set's the capacity of the Person class to the specified number if it is at least 1.
     * @param i The maximum capacity of the person class to set.
     */
    public static void setCapacity(int i){
        if (i < 1){
            throw new IllegalArgumentException("Person class capacity cannot be less than one.");
        }
        Person.capacity = i;
    }

    /**
     *
     * @return The capacity of the Person class.
     */
    public static int getCapacity(){
        return capacity;
    }

    /**
     *
     * @param obj
     * @return True if the objects are the same, or if the object is an instance of person and has the same name. False if the object obj is not an instance of person or if the two objects do not share the same name.
     */
    @Override
    public boolean equals(Object obj){
        if (obj == this){
            return true;
        }
        if (! (obj instanceof Person) ){
            return false;
        }
        Person person = (Person) obj;
        return person.getName().equalsIgnoreCase(this.getName());
    }
}

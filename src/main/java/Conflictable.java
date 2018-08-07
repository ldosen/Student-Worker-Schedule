import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is the super class of Slot and Person. It is meant to help manage conflict between time slots and people. It allows for a slot to check if a
 * person has a conflict that prevents them from working that time slot.
 */

public abstract class Conflictable {
    private final HashSet<ConflictMarker> setOfPotentialConflicts = new HashSet<>();
    private final static HashSet<ConflictMarker> allConflictMarkers = new HashSet<>();

    /**
     * Add's a conflict marker to the setOfPotentialConflictMarkers if possible.
     * @param conflictMarker A marker that indicates that there is a specific type of conflict. For example A1 might be used for both 9:30 and 10:30 on July 1st to signify that there is a conflict at these times.
     * @return True if the conflict marker was not already contained in the setOfPotentialConflicts. False if the marker was already present.
     */
    public boolean addConflictMarkerToInstance(final ConflictMarker conflictMarker) {
        if (setOfPotentialConflicts.contains(conflictMarker)) {
            return false;
        }
        setOfPotentialConflicts.add(conflictMarker);
        allConflictMarkers.add(conflictMarker);
        return true;
    }

    /**
     * Checks if there are any conflicts between this conflictable object and another conflictable object.
     * @param conflictable Another Conflictable object
     * @return True if there is a conflict. False if there is not a conflict.
     */
    public boolean checkForPotentialConflicts(final Conflictable conflictable) {
        Boolean answer = false;
        if (this.setOfPotentialConflicts.isEmpty() || conflictable.setOfPotentialConflicts.isEmpty()) {
            return false;
        } else {
            final Iterator<ConflictMarker> itr = this.setOfPotentialConflicts.iterator();
            while (itr.hasNext()) {
                if (conflictable.setOfPotentialConflicts.contains(itr.next())) {
                    answer = true;
                }
            }
        }
        return answer;
    }

    /**
     * This method removes all the conflict markers from another conflicable object that are also on this object.
     * @param conflictable Another Conflictable object
     */
    public void removeLinkedConflictsFromOtherConflictable(final Conflictable conflictable) {
        {
            conflictable.setOfPotentialConflicts.removeAll(this.setOfPotentialConflicts);
        }
    }

    /**
     * Removes a conflict marker from the object.
     * @param conflictMarker The conflictMarker to remove from this object's set of potential conflicts.
     * @return True if the object was removed. False if the object was not removed(not present).
     */
    public boolean removeConflictMarkerFromInstance(final ConflictMarker conflictMarker) {
        return setOfPotentialConflicts.remove(conflictMarker);
    }

    /**
     * Add's all of this object's conflict markers from it's set of potential conflicts to the other objects set of potential conflicts.
     * @param conflictable The conflictable object to add this object's conflict markers to.
     */
    public void addAllConflictMarkersToObj(final Conflictable conflictable) {
        conflictable.setOfPotentialConflicts.addAll(this.setOfPotentialConflicts);
    }

    /**
     * This method removes the specified conflict marker from the set of all conflict markers if it is present.
     * @param conflictMarker A conflict marker object.
     * @return True if conflict marker was present and removed. False if conflict marker was not present and not removed.
     */
    public boolean removeConflictMarkerFromAllConflictMarkers(final ConflictMarker conflictMarker) {
        return allConflictMarkers.remove(conflictMarker);
    }

    /**
     * Displays all the conflict markers of this object on the screen.
     */
    public void displayConflictMarkers() {
        for (ConflictMarker conflictMarker : setOfPotentialConflicts) {
            System.out.println(conflictMarker.thingToHash);
        }
    }

    /**
     * Add's unique conflict markers for each day to the time slots if they are at the times specified. Conflict Markers added to the same day will be the same. But conflict markers added to different days
     * will have a different integer value for the conflict Marker. For example, Monday We might apply conflict marker A1 to 9:30 and 10:30. Tuesday(or the next date on the schedule) it will be A2. Weds will be A3 and so forth.
     * @pre The ConflictMarkers in allConflictMarkers should not contain a character field of the specified character for this method.
     * @param timeMap The schedule to add conflict markers to.
     * @param character A character specifying the type of times. For example A might specify 9:30 and 10:30. This is arbitrary.
     * @param timeOne The first time string that conflicts with the second time
     * @param timeTwo The second time string that conflicts with the first time
     */
    public static void applyThisCharRuleToEntireSchedule(final TheTimeMap timeMap, final Character character, final String timeOne, final String timeTwo){
        for (final ConflictMarker conflictMarker : allConflictMarkers){
            if (conflictMarker.getChar().equals(character)){
                throw new IllegalArgumentException("Character has already been used. Please choose a new character to specify a type of conflict");
            }
        }
        int integer = 0;
        String date = "";
        for (Map.Entry<String, Slot> entry : timeMap.entrySet()){
            String oldDate = date;
            date = entry.getValue().getDate();
            if (oldDate.equalsIgnoreCase(date)){
                if (entry.getKey().contains(timeOne) || entry.getKey().contains(timeTwo)){
                    entry.getValue().addConflictMarker(character, integer);
                }
            }
            else {
                integer++;
                if (entry.getKey().contains(timeOne) || entry.getKey().contains(timeTwo)) {
                    entry.getValue().addConflictMarker(character, integer);
                }
            }
        }
    }
}

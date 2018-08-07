import java.util.HashSet;
import java.util.Iterator;

public abstract class Conflictable {
    private HashSet<ConflictMarker> setOfPotentialConflicts = new HashSet<>();
    private boolean isConflict = false;
    private static HashSet<ConflictMarker> allConflictMarkers = new HashSet<>();

    public boolean addConflictMarkerToInstance(ConflictMarker conflictMarker) {
        if (setOfPotentialConflicts.contains(conflictMarker)) {
            return false;
        }
        setOfPotentialConflicts.add(conflictMarker);
        allConflictMarkers.add(conflictMarker);
        return true;
    }

    public boolean checkForPotentialConflicts(Conflictable conflictable) {
        Boolean answer = false;
        if (this.setOfPotentialConflicts.isEmpty() || conflictable.setOfPotentialConflicts.isEmpty()) {
            return false;
        } else {
            Iterator<ConflictMarker> itr = this.setOfPotentialConflicts.iterator();
            while (itr.hasNext()) {
                if (conflictable.setOfPotentialConflicts.contains(itr.next())) {
                    answer = true;
                }
            }
        }
        return answer;
    }

    public void removeLinkedConflictsFromOtherConflictable(Conflictable conflictable) {
        {
            conflictable.setOfPotentialConflicts.removeAll(this.setOfPotentialConflicts);
        }
    }

    public boolean removeConflictMarkerFromInstance(ConflictMarker conflictMarker) {
        return setOfPotentialConflicts.remove(conflictMarker);
    }

    public void addAllConflictMarkersToObj(Conflictable conflictable) {
        conflictable.setOfPotentialConflicts.addAll(this.setOfPotentialConflicts);
    }

    public boolean removeConflictMarkerFromAllConflictMarkers(ConflictMarker conflictMarker) {
        return allConflictMarkers.remove(conflictMarker);
    }

    public void displayConflictMarkers() {
        for (ConflictMarker conflictMarker : setOfPotentialConflicts) {
            System.out.println(conflictMarker + conflictMarker.thingToHash);
        }
    }
}

import java.util.*;
public class ConflictMarker {
    private int integerValue; //allows us to use the same character for different days
    private Character character ; // specifies that a TIME is possibly conflicting with ANOTHER time
    public String thingToHash = null;

    public ConflictMarker() {
        System.out.println("Warning! This is Conflict Marker does not contain any meaningful data. Please use the second constructor to initialize it properly");
    }

    public ConflictMarker(Character character, int integer) {
        this.character = character;
        integerValue = integer;
        thingToHash = Character.toString(character) + Integer.toString(integerValue);
    }
    public int getInt(){
        return integerValue;
    }
    public Character getChar(){
        return character;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ConflictMarker)) {
            return false;
        } else {
            ConflictMarker conflictMarker = (ConflictMarker) obj;
            return this.thingToHash.equals(conflictMarker.thingToHash);
        }
    }
    @Override
    public int hashCode() {
        return thingToHash.hashCode();
    }
}
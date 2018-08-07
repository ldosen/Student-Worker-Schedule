import java.util.*;

/**
 * This class is meant to determine the min and max of certain slots. It can be used to specify that all slots that are at a given time have a certain min and max. For example,
 * creating a new Rule(2,3) and using the applyTimeDayRuleToSlot method on a certain slot that contains a specified day and time will set that slot's min and max according according to the rule's min and max.
 */
public class Rule {
    private int minToSet = 0;
    private int maxToSet = 0;

    public Rule(int min, int max){
        minToSet = min;
        maxToSet = max;
    }

    /**
     *  Attempts to set the min and max of a slot if it is on a certain day and time.
     * @param slot The slot that an update to the min and max will be attempted on.
     * @param day The day that it must be to apply the specified min and max
     * @param time The time that it must also be to apply the specified min and max
     */
    public void applyTimeDayRuleToSlot(Slot slot, String day, String time){
        if (slot.getDate().contains(day)){
            if (slot.getTime().equals(time)){
                slot.setMinimumRequired(minToSet);
                slot.setMax(maxToSet);
            }
        }
    }
}


import java.util.*;
import java.util.Map;

public class PersonMapHash {
    private HashMap<String, Person> people = new HashMap<>(97);

    public void put(final String key, final Person value) {
        people.put(key, value);
    }

    public Set<Map.Entry<String, Person>> entrySet() {
        return people.entrySet();
    }

    public void clear() {
        people.clear();
    }

    public boolean containsKey(final String personID) {
        return people.containsKey(personID);
    }

    public boolean containsValue(final Person person) {
        return people.containsValue(person);
    }

    public Set<String> keySet() {
        final Set<String> setOfPeople = new HashSet<>();
        setOfPeople.addAll(people.keySet());
        return setOfPeople;
    }

    public Person remove(final String key) {
        return people.remove(key);
    }

    public Person get(final String key) {
        return people.get(key);
    }

    public int size() {
        return people.size();
    }

    public boolean updateToNewPersonObj(final String person, final Person human) {
        if (people.containsKey(person)) {
            people.put(person, human);
            return true;
        }
        return false;
    }
}

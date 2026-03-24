import java.util.ArrayList;

public interface Searchable {
    /**
     * Searches for subscriptions that match specified name keyword
     * @param name the keyword used to search for matching subscriptions
     * @return a list of matching Subscription objects
     */

    ArrayList<Subscription> searchByName(String name);
    
    /**
     * Searches for subscriptions that match specified category keyword
     * @param category the keyword used to search for matching subscriptions
     * @return a list of matching Subscription objects
     */

    ArrayList<Subscription> searchByCategory(String category);
}

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

/**
 * @author Ramsha
 * unit tests for Data.java methods
 */
public class DataTest {

    @Test
    void findSubscriptionIndex() {
        Data.subscriptions.clear();
        Data.names.clear();
        ArrayList<String> subscription = new ArrayList<>();
        subscription.add("Netflix");
        subscription.add("18.99");
        subscription.add("Entertainment");
        subscription.add("Monthly");
        subscription.add("March 9");
        subscription.add("April 9");
        subscription.add("ACTIVE");
        Data.subscriptions.add(subscription);

        int index = Data.findingSubscriptionIndex("Netflix");
        assertEquals(0, index, "Netflix should be in index 0");
    }

    @Test
    void findSubscriptionIndex_notFound() {
        Data.subscriptions.clear();
        Data.names.clear();

        int index = Data.findingSubscriptionIndex("Hulu");
        assertEquals(-1, index, "should return -1 when subscription not found");
    }

    @Test
    void findSubscriptionIndex_caseInsensitive() {
        Data.subscriptions.clear();
        Data.names.clear();
        ArrayList<String> subscription = new ArrayList<>();
        subscription.add("Netflix");
        subscription.add("17.99");
        subscription.add("Entertainment");
        subscription.add("Monthly");
        subscription.add("03/01/2025");
        subscription.add("03/01/2026");
        subscription.add("ACTIVE");
        Data.subscriptions.add(subscription);

        int index = Data.findingSubscriptionIndex("netflix");
        assertEquals(0, index, "should find subscription even if lowercase.");
    }

    @Test
    void updateSubscriptionChoice() {
        Data.subscriptions.clear();
        Data.names.clear();
        ArrayList<String> subscription = new ArrayList<>();
        subscription.add("Netflix");
        subscription.add("18.99");
        subscription.add("Entertainment");
        subscription.add("Monthly");
        subscription.add("March 9");
        subscription.add("April 9");
        subscription.add("ACTIVE");
        Data.subscriptions.add(subscription);

        Data.changeSubscriptionChoice(0, Data.INDEX_STATUS, "PAUSED");
        assertEquals("PAUSED", Data.subscriptions.get(0).get(Data.INDEX_STATUS), "should update status to PAUSED");
    }

    @Test
    void changeSubscriptionChoice_updateCost() {
        Data.subscriptions.clear();
        Data.names.clear();
        ArrayList<String> subscription = new ArrayList<>();
        subscription.add("Netflix");
        subscription.add("15.99");
        subscription.add("Entertainment");
        subscription.add("Monthly");
        subscription.add("03/01/2025");
        subscription.add("03/01/2026");
        subscription.add("ACTIVE");
        Data.subscriptions.add(subscription);

        Data.changeSubscriptionChoice(0, Data.INDEX_COST, "19.99");
        assertEquals("19.99", Data.subscriptions.get(0).get(Data.INDEX_COST), "should update cost to 19.99");
    }

    @Test
    void isValidStatus_validInput() {
        assertTrue(Data.isValidStatus("ACTIVE"), "ACTIVE should be valid");
        assertTrue(Data.isValidStatus("PAUSED"), "PAUSED should be valid");
        assertTrue(Data.isValidStatus("CANCELLED"), "CANCELLED should be valid");
    }

    @Test
    void isValidStatus_invalidInput() {
        assertFalse(Data.isValidStatus("active"), "lowercase should be invalid");
        assertFalse(Data.isValidStatus("HELLO"), "random string should be invalid");
        assertFalse(Data.isValidStatus(""), "empty string should be invalid");
    }
}
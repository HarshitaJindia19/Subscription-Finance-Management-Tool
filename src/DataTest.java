import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Ramsha
 * Tests that findSubscriptionIndex returns the right index
 */
public class DataTest {
    @Test
    void findSubscriptionIndex() {
        Data.subscriptions.clear();
        ArrayList<String> subscription = new ArrayList<>();
        subscription.add("Netflix");
        subscription.add("18.99");
        subscription.add("Entertainment");
        subscription.add("Monthly");
        subscription.add("March 9");
        subscription.add("April 9");
        subscription.add("ACTIVE");
        Data.subscriptions.add(subscription);

        int index = Data.findSubscriptionIndex("Netflix");
        assertEquals(0, index, "Netflix should be in index 0");

    }
    @Test
    void updateSubscriptionChoice(){
        Data.suscriptions.clear();
        ArrayList<String> subscription= new ArrayList<>();
        subscription.add("Netflix");
        subscription.add("18.99");
        subscription.add("Entertainment");
        subscription.add("Monthly");
        subscription.add("March 9");
        subscription.add("April 9");
        subscription.add("ACTIVE");
        Data.subscriptions.add(subscription);

        Data.updateSubscriptionChoice(0,6, "PAUSED");
        assertEquals("PAUSED",Data.subscriptions.get(0).get(6), "Should update status to PAUSED");


    }
}
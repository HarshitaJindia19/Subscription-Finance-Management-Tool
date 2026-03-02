import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class DataTest {

    // cleans all stored data before each test
    @BeforeEach
    public void clean() {
        Data.subscriptions.clear();
        Data.categories.clear();
        Data.names.clear();
        Data.categoryBudgets.clear();
    }

    /**
     * @author Ramsha
     * Tests that findSubscriptionIndex returns the right index
     */
    @Test
    void findSubscriptionIndex() {
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
    void updateSubscriptionChoice(){
        ArrayList<String> subscription= new ArrayList<>();
        subscription.add("Netflix");
        subscription.add("18.99");
        subscription.add("Entertainment");
        subscription.add("Monthly");
        subscription.add("March 9");
        subscription.add("April 9");
        subscription.add("ACTIVE");
        Data.subscriptions.add(subscription);

        Data.changeSubscriptionChoice(0,6, "PAUSED");
        assertEquals("PAUSED",Data.subscriptions.get(0).get(6), "Should update status to PAUSED");

    }

    //------------------------------ Monthly Spend Tests -------------------------------//

    @Test
    public void monthlySpend_OnlyChecksActive() {

        Data.storeNewSubscription("Netflix", "15.99", "Entertainment", "Monthly", "01/01/2025", null, "ACTIVE");
        Data.storeNewSubscription("Spotify", "9.99", "Entertainment", "Monthly", "01/01/2025", null, "ACTIVE");
        Data.storeNewSubscription("GoodEarth", "13.99", "Entertainment", "Monthly", "01/01/2025", null, "CANCELLED");
        Data.storeNewSubscription("ArtBox", "110.99", "Entertainment", "Yearly", "01/01/2025", null, "PAUSED");

        assertEquals(25.98, Data.calculateMonthlySpend(), "Monthly cost was not calculated properly!");
    }

    @Test
    public void monthlySpend_YearDividedBy12() {
        Data.storeNewSubscription("Adobe", "120.00", "Work", "Yearly", "01/01/2025", null, "ACTIVE");
        assertEquals(10.00, Data.calculateMonthlySpend(), "Monthly cost was not calculated properly! Yearly costs must be divided by 12!");

        clean();
        Data.storeNewSubscription("Spotify", "34.99", "Entertainment", "Yearly", "01/01/2025", null, "ACTIVE");
        assertEquals(2.9158, Data.calculateMonthlySpend(), "Monthly cost was not calculated properly! Yearly costs must be divided by 12!");

    }

    @Test
    public void monthlySpend_EmptyList() {
        assertEquals(0.0, Data.calculateMonthlySpend(), "Empty list should have no costs!");
    }

    //------------------------------------ Yearly Spend Functions -------------------------------//

    @Test
    public void yearlySpend_EmptyList() {
        assertEquals(0.0, Data.calculateYearlySpend(), "Empty list should have no costs!");
    }

    @Test
    public void yearlySpend_MonthTimes12() {
        Data.storeNewSubscription("Adobe", "10.00", "Work", "Monthly", "01/01/2025", null, "ACTIVE");

        assertEquals(120.00, Data.calculateYearlySpend(), "Yearly cost was not calculated properly! Month should be times by 12.");
    }

    @Test
    public void yearlySpend_OnlyChecksActive() {
        Data.storeNewSubscription("Netflix", "15.99", "Entertainment", "Yearly", "01/01/2025", null, "ACTIVE");
        Data.storeNewSubscription("Spotify", "9.99", "Entertainment", "Yearly", "01/01/2025", null, "ACTIVE");
        Data.storeNewSubscription("GoodEarth", "13.99", "Entertainment", "Monthly", "01/01/2025", null, "CANCELLED");
        Data.storeNewSubscription("ArtBox", "110.99", "Entertainment", "Yearly", "01/01/2025", null, "PAUSED");

        assertEquals(25.98, Data.calculateYearlySpend(), "Yearly cost was not calculated properly!");
    }
}
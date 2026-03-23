import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

/**
 * Unit tests for MonthlySubscription and YearlySubscription
 * @author Ramsha
 * @date March 18 2026
 * @tutorial T01
 */

//--------------------------MonthlySubscription Tests----------------------------------

public class SubscriptionTest {
    @Test
    void monthlySubscription_getMonthlyCost() {
        // monthly cost should equal cost directly
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertEquals(15.99, sub.getMonthlyCost(), "Monthly cost should equal cost directly");
    }

    @Test
    void monthlySubscription_getYearlyCost() {
        // yearly cost should be monthly * 12
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertEquals(15.99 * 12, sub.getYearlyCost(), "Yearly cost should be monthly * 12");
    }

    @Test
    void monthlySubscription_getMonthlyCost_zero() {
        //edge case(cost of 0)
        MonthlySubscription sub = new MonthlySubscription("FreeService", 0.0, "Entertainment", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertEquals(0.0, sub.getMonthlyCost(), "Monthly cost of 0 should return 0");
    }

    @Test
    void monthlySubscription_getRenewal() {
        //renewal should always be Monthly for MonthlySubscription
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertEquals("Monthly", sub.getRenewal(), "Renewal should be Monthly");
    }

    @Test
    void monthlySubscription_isUpcoming_true() {
        //date within next 7 days should return true
        LocalDate upcoming = LocalDate.now().plusDays(3);
        MonthlySubscription sub = new MonthlySubscription("Spotify", 9.99, "Music", LocalDate.of(2025, upcoming.getMonthValue(), upcoming.getDayOfMonth()), null, "ACTIVE");
        assertTrue(sub.isUpcoming(), "Should be upcoming within 7 days");
    }

    @Test
    void monthlySubscription_isUpcoming_false() {
        //date more than 7 days away should return false
        LocalDate notUpcoming = LocalDate.now().plusDays(20);
        MonthlySubscription sub = new MonthlySubscription("Spotify", 9.99, "Music", LocalDate.of(2025, notUpcoming.getMonthValue(), notUpcoming.getDayOfMonth()), null, "ACTIVE");
        assertFalse(sub.isUpcoming(), "Should not be upcoming - more than 7 days away");
    }

    //--------------------------YearlySubscription Tests----------------------
    @Test
    void yearlySubscription_getMonthlyCost() {
        //monthly cost should be yearly/12
        YearlySubscription sub = new YearlySubscription("Adobe", 120.00, "Work", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertEquals(10.00, sub.getMonthlyCost(), "Monthly cost should be yearly / 12");
    }

    @Test
    void yearlySubscription_getMonthlyCost_notDivisible() {
        //edge case - cost not evenly divisible by 12
        YearlySubscription sub = new YearlySubscription("Adobe", 100.00, "Work", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertEquals(100.00 / 12, sub.getMonthlyCost(), "Monthly cost should be 100/12");
    }

    @Test
    void yearlySubscription_getYearlyCost() {
        //yearly cost should equal cost directly
        YearlySubscription sub = new YearlySubscription("Adobe", 120.00, "Work", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertEquals(120.00, sub.getYearlyCost(), "Yearly cost should equal cost directly");
    }

    @Test
    void yearlySubscription_getRenewal() {
        //renewal should always be yearly for YearlySubscription
        YearlySubscription sub = new YearlySubscription("Adobe", 120.00, "Work", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertEquals("Yearly", sub.getRenewal(), "Renewal should be Yearly");
    }

    @Test
    void yearlySubscription_toString() {
        //toString should contain Yearly
        YearlySubscription sub = new YearlySubscription("Adobe", 120.00, "Work", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertTrue(sub.toString().contains("Yearly"), "toString should contain Yearly");
    }

    //--------------------------Extra Inheritance Tests----------------------

    @Test
    void monthlySubscription_isInstanceOfSubscription() {
        //monthlySubscription should be instance of Subscription
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertTrue(sub instanceof Subscription, "MonthlySubscription should be instance of Subscription");
    }

    @Test
    void yearlySubscription_isInstanceOfSubscription() {
        //yearlySubscription should be instance of Subscription
        YearlySubscription sub = new YearlySubscription("Adobe", 120.00, "Work", LocalDate.of(2025, 3, 1), null, "ACTIVE");
        assertTrue(sub instanceof Subscription, "YearlySubscription should be instance of Subscription");
    }
    //--------------------------searchByName Tests----------------------

    @Test
    void searchByName_exactMatch() {
        //Exact name should return 1 result
        SubscriptionManager manager = new SubscriptionManager();

        manager.storeNewSubscription("Netflix", 10, "Entertainment", "Monthly", null, null, Status.ACTIVE);
        ArrayList<Subscription> result = manager.searchByName("Netflix")
        assertEquals(1, result.size(), "Should return exactly 1 match");
        assertEquals("Netflix", result.get(0).getName(), "Name should match Netflix");
    }

    @Test
    void searchByName_partialMatch() {
        //Partial match should still find result
        SubscriptionManager manager = new SubscriptionManager();

        manager.storeNewSubscription("Netflix", 10, "Entertainment", "Monthly", null, null, Status.ACTIVE);
        ArrayList<Subscription> result = manager.searchByName("Net");
        assertEquals(1, result.size(), "Should return 1 partial match");
    }

    @Test
    void searchByName_notFound() {
        //Non existing name should return empty
        SubscriptionManager manager = new SubscriptionManager();

        manager.storeNewSubscription("Netflix", 10, "Entertainment", "Monthly", null, null, Status.ACTIVE);
        ArrayList<Subscription> result = manager.searchByName("Hulu");
        assertTrue(result.isEmpty(), "Result should be empty when not found");
    }

    @Test
    void searchByName_emptyString_returnsAll() {
        //Empty string should return all subscriptions
        SubscriptionManager manager = new SubscriptionManager();

        manager.storeNewSubscription("Netflix", 10, "Entertainment", "Monthly", null, null, Status.ACTIVE);
        manager.storeNewSubscription("Spotify", 10, "Music", "Monthly", null, null, Status.ACTIVE);
        ArrayList<Subscription> result = manager.searchByName("");
        assertEquals(manager.subscriptions.size(), result.size(), "Empty string should return all results");
    }

    @Test
    void searchByName_caseInsensitive() {
        //Search should be case insensitive
        SubscriptionManager manager = new SubscriptionManager();

        manager.storeNewSubscription("Netflix", 10, "Entertainment", "Monthly", null, null, Status.ACTIVE);
        ArrayList<Subscription> result = manager.searchByName("netflix");
        assertEquals(1, result.size(), "Search should ignore case");
        assertEquals("Netflix", result.get(0).getName(), "Name should match original casing");
    }

    @Test
    void searchByName_specialCharacters() {
        // should handle special characters
        SubscriptionManager manager = new SubscriptionManager();

        manager.storeNewSubscription("Net@flix!", 10, "Entertainment", "Monthly", null, null, Status.ACTIVE);
        ArrayList<Subscription> result = manager.searchByName("Net@");
        assertEquals(1, result.size(), "Should match special characters");
    }

    @Test
    void searchByName_emptyList() {
        //Empty list should return empty result
        SubscriptionManager manager = new SubscriptionManager();

        ArrayList<Subscription> result = manager.searchByName("Netflix");
        assertTrue(result.isEmpty(), "Empty list should return empty result");
    }

    //--------------------------searchByCategory Tests----------------------

    @Test
    void searchByCategory_found() {
        //Should find matching category
        SubscriptionManager manager = new SubscriptionManager();

        manager.storeNewSubscription("Netflix", 10, "Entertainment", "Monthly", null, null, Status.ACTIVE);
        ArrayList<Subscription> result = manager.searchByCategory("Entertainment");
        assertEquals(1, result.size(), "Should find 1 category match");
        assertEquals("Netflix", result.get(0).getName(), "Name should match Netflix");

    }
    @Test
    void searchByCategory_notFound() {
        //No matching category should return empty
        SubscriptionManager manager = new SubscriptionManager();

        manager.storeNewSubscription("Netflix", 10, "Entertainment", "Monthly", null, null, Status.ACTIVE);
        ArrayList<Subscription> result = manager.searchByCategory("Fitness");
        assertTrue(result.isEmpty(), "No matching category should return empty");
    }

    @Test
    void searchByCategory_multipleResults() {
        //Multiple matches should be returned
        SubscriptionManager manager = new SubscriptionManager();

        manager.storeNewSubscription("Netflix", 10, "Entertainment", "Monthly", null, null, Status.ACTIVE);
        manager.storeNewSubscription("Spotify", 10, "Entertainment", "Monthly", null, null, Status.ACTIVE);
        ArrayList<Subscription> result = manager.searchByCategory("Entertainment");
        assertEquals(2, result.size(), "Should return multiple results");
    }
}
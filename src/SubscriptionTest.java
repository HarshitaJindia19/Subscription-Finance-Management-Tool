import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

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
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals(15.99, sub.getMonthlyCost(), "Monthly cost should equal cost directly");
    }

    @Test
    void monthlySubscription_getYearlyCost() {
        // yearly cost should be monthly * 12
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals(15.99 * 12, sub.getYearlyCost(), "Yearly cost should be monthly * 12");
    }

    @Test
    void monthlySubscription_getMonthlyCost_zero() {
        //edge case(cost of 0)
        MonthlySubscription sub = new MonthlySubscription("FreeService", 0.0, "Entertainment", LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals(0.0, sub.getMonthlyCost(), "Monthly cost of 0 should return 0");
    }

    @Test
    void monthlySubscription_getRenewal() {
        //renewal should always be Monthly for MonthlySubscription
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1),null,Status.ACTIVE);
        assertEquals("Monthly", sub.getRenewal(), "Renewal should be Monthly");
    }
    @Test
    void monthlySubscription_isUpcoming_true() {
        //date within next 7 days should return true
        LocalDate upcoming = LocalDate.now().plusDays(3);
        MonthlySubscription sub = new MonthlySubscription("Spotify", 9.99, "Music", LocalDate.of(2025, upcoming.getMonthValue(), upcoming.getDayOfMonth()),null, Status.ACTIVE);
        assertTrue(sub.isUpcoming(), "Should be upcoming within 7 days");
    }

    @Test
    void monthlySubscription_isUpcoming_false() {
        //date more than 7 days away should return false
        LocalDate notUpcoming = LocalDate.now().plusDays(20);
        MonthlySubscription sub = new MonthlySubscription("Spotify", 9.99, "Music", LocalDate.of(2025, notUpcoming.getMonthValue(), notUpcoming.getDayOfMonth()),null, Status.ACTIVE);
        assertFalse(sub.isUpcoming(), "Should not be upcoming - more than 7 days away");
    }
    @Test
    void monthlySubscription_isUpcoming_today() {
        // renewal is today - should still be upcoming
        LocalDate today = LocalDate.now();
        MonthlySubscription sub = new MonthlySubscription("Spotify", 9.99, "Music", LocalDate.of(2025, today.getMonthValue(), today.getDayOfMonth()), null, Status.ACTIVE);
        assertTrue(sub.isUpcoming(), "Renewal today should count as upcoming");
    }
//    @Test
//    void monthlySubscription_toString() {
//        // toString should contain Monthly
//        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment",LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
//        assertTrue(sub.toString().contains("Monthly"), "toString should contain Monthly");
//    }


    //--------------------------YearlySubscription Tests----------------------
    @Test
    void yearlySubscription_getMonthlyCost() {
        //monthly cost should be yearly/12
        YearlySubscription sub = new YearlySubscription("Adobe", 120.00, "Work",LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals(10.00, sub.getMonthlyCost(), "Monthly cost should be yearly / 12");
    }
    @Test
    void yearlySubscription_getMonthlyCost_notDivisible() {
        //edge case - cost not evenly divisible by 12
        YearlySubscription sub = new YearlySubscription("Adobe",100.00, "Work",LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals(100.00 / 12, sub.getMonthlyCost(), "Monthly cost should be 100/12");
    }

    @Test
    void yearlySubscription_getYearlyCost() {
        //yearly cost should equal cost directly
        YearlySubscription sub = new YearlySubscription("Adobe",120.00, "Work",LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals(120.00, sub.getYearlyCost(), "Yearly cost should equal cost directly");
    }
    @Test
    void yearlySubscription_getRenewal() {
        //renewal should always be yearly for YearlySubscription
        YearlySubscription sub = new YearlySubscription("Adobe",120.00,"Work",LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertEquals("Yearly", sub.getRenewal(), "Renewal should be Yearly");
    }
//    @Test
//    void yearlySubscription_toString() {
//        //toString should contain Yearly
//        YearlySubscription sub = new YearlySubscription("Adobe",120.00,"Work",LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
//        assertTrue(sub.toString().contains("Yearly"), "toString should contain Yearly");
//    }

    //--------------------------Extra Inheritance Tests----------------------

    @Test
    void monthlySubscription_isInstanceOfSubscription() {
        //monthlySubscription should be instance of Subscription
        MonthlySubscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertTrue(sub instanceof Subscription, "MonthlySubscription should be instance of Subscription");
    }

    @Test
    void yearlySubscription_isInstanceOfSubscription() {
        //yearlySubscription should be instance of Subscription
        YearlySubscription sub = new YearlySubscription("Adobe", 120.00, "Work", LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        assertTrue(sub instanceof Subscription, "YearlySubscription should be instance of Subscription");
    }

    //------------------------------------------- Status Enum Tests ---------------------------------------------------------------//

    @Test
    void status_getStatus_returnsCorrectEnum() {
        assertEquals(Status.ACTIVE, Status.getStatus("ACTIVE"), "getStatus(ACTIVE) should return Status.ACTIVE");
        assertEquals(Status.PAUSED, Status.getStatus("PAUSED"), "getStatus(PAUSED) should return Status.PAUSED");
        assertEquals(Status.CANCELLED, Status.getStatus("CANCELLED"), "getStatus(CANCELLED) should return Status.CANCELLED");
    }

    @Test
    void status_getStatus_caseInsensitive() {
        assertEquals(Status.ACTIVE, Status.getStatus("active"), "Should accept lowercase");
        assertEquals(Status.PAUSED, Status.getStatus("Paused"), "Should accept mixed case");
    }

    @Test
    void status_getStatus_invalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Status.getStatus("EXPIRED"), "Invalid status should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> Status.getStatus(""), "Empty string should throw IllegalArgumentException");
    }

    @Test
    void status_toString_returnsCorrectString() {
        assertEquals("ACTIVE", Status.ACTIVE.toString(), "ACTIVE toString should return 'ACTIVE'");
        assertEquals("PAUSED", Status.PAUSED.toString(), "PAUSED toString should return 'PAUSED'");
        assertEquals("CANCELLED", Status.CANCELLED.toString(), "CANCELLED toString should return 'CANCELLED'");
    }

    //------------------------------------------------ Subscription equals() Test ----------------------------------------------------//

    @Test
    void subscription_equals_sameNameIsEqual() {
        Subscription s1 = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        Subscription s2 = new MonthlySubscription("Netflix", 9.99, "Fitness", LocalDate.of(2025, 1, 1), null, Status.PAUSED);
        assertEquals(s1, s2, "Same name subscriptions should be equal regardless of other fields");
    }

    @Test
    void subscription_equals_differentNameNotEqual() {
        Subscription s1 = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        Subscription s2 = new MonthlySubscription("Spotify", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        assertNotEquals(s1, s2, "Different name subscriptions should not be equal");
    }

    @Test
    void subscription_equals_caseInsensitive() {
        //Tests equals() is case insensitive
        Subscription s1 = new MonthlySubscription("netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        Subscription s2 = new MonthlySubscription("NETFLIX", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        assertEquals(s1, s2, "equals() should be case insensitive");
    }

    // --------------------------------------------- Subscription compareTo() Test ----------------------------------------------------------------------------

    @Test
    void subscription_compareTo_activeSortBeforePaused() {
        //Tests active subscriptions go before paused
        Subscription active = new MonthlySubscription("Spotify", 9.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        Subscription paused = new MonthlySubscription("Adobe", 50.00, "Software", LocalDate.of(2025, 1, 1), null, Status.PAUSED);
        assertTrue(active.compareTo(paused) < 0, "ACTIVE should sort before PAUSED");
    }

    @Test
    void subscription_compareTo_pausedSortBeforeCancelled() {
        //Tests paused subscriptions go before cancelled
        Subscription paused = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.PAUSED);
        Subscription cancelled = new MonthlySubscription("Spotify", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.CANCELLED);
        assertTrue(paused.compareTo(cancelled) < 0, "PAUSED should sort before CANCELLED");
    }

    @Test
    void subscription_compareTo_sameStatusSortAlphabetically() {
        //Tests same status is stored in alphabetical order
        Subscription s1 = new MonthlySubscription("Adobe", 50.00, "Software", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        Subscription s2 = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        assertTrue(s1.compareTo(s2) < 0, "Adobe should sort before Netflix alphabetically within same status");
    }

    //--------------------------------- Subscription toString() Tests ----------------------------------//

    @Test
    void subscription_toString_containsAllFields() {
        Subscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        String result = sub.toString();
        assertTrue(result.contains("Netflix"), "toString should contain name");
        assertTrue(result.contains("15.99"), "toString should contain cost");
        assertTrue(result.contains("Entertainment"), "toString should contain category");
        assertTrue(result.contains("Monthly"), "toString should contain renewal");
        assertTrue(result.contains("01/01/2025"), "toString should contain startDate");
        assertTrue(result.contains("ACTIVE"), "toString should contain status");
    }

    @Test
    void subscription_toString_nullEndDateShowsNA() {
        Subscription sub = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        assertTrue(sub.toString().contains("N/A"), "toString should show N/A when end date is null");
    }

    @Test
    void subscription_toString_showsFormattedEndDate() {
        Subscription s = new MonthlySubscription("Netflix", 15.99, "Entertainment", LocalDate.of(2025, 1, 1), LocalDate.of(2026, 6, 15), Status.ACTIVE);
        assertTrue(s.toString().contains("06/15/2026"), "toString should show formatted end date MM/dd/yyyy");
    }

    // --------------------------- Budget toString() Tests ------------------------------------------
    @Test
    void budget_toString_correctFormat() {
        Budget b = new Budget("Entertainment", 50.00);
        assertEquals("Entertainment: $50.00", b.toString(), "toString should be 'Category: $XX.XX'");
    }

    @Test
    void budget_toString_differentAmounts() {
        Budget b1 = new Budget("Fitness", 100.00);
        Budget b2 = new Budget("Software", 9.99);
        assertEquals("Fitness: $100.00", b1.toString(), "toString should format 100.00 correctly");
        assertEquals("Software: $9.99", b2.toString(), "toString should format 9.99 correctly");
    }

    //--------------------------------- Budget equals() Tests ----------------------------------------------

    @Test
    void budget_equals_sameCategoryIsEqual() {
        Budget b1 = new Budget("Entertainment", 50.00);
        Budget b2 = new Budget("Entertainment", 100.00);
        assertEquals(b1, b2, "Budgets with same category should be equal regardless of limit");
    }

    @Test
    void budget_equals_differentCategoryNotEqual() {
        Budget b1 = new Budget("Entertainment", 50.00);
        Budget b2 = new Budget("Fitness", 50.00);
        assertNotEquals(b1, b2, "Budgets with different categories should not be equal");
    }

    @Test
    void budget_equals_caseInsensitive() {
        Budget b1 = new Budget("entertainment", 50.00);
        Budget b2 = new Budget("Entertainment", 50.00);
        assertEquals(b1, b2, "Budget equals() should be case insensitive");
    }

    @Test
    void budget_equals_usedInSetCategoryBudget() {
        SubscriptionManager manager = new SubscriptionManager();
        manager.setCategoryBudget("Entertainment", 50.00);
        manager.setCategoryBudget("Entertainment", 100.00);
        assertEquals(100.00, manager.getCategoryBudget("Entertainment"), 0.01, "Budget should be overwritten not duplicated - uses equals()");
        assertEquals(1, manager.getAllBudgets().size(), "Should only have one budget for Entertainment");
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

    @Test
    void searchByName_nullInput() {
        //Null input should return empty
        SubscriptionManager manager = new SubscriptionManager();

        Data.storeNewSubscription("Netflix", 10, "Entertainment", "Monthly", null, null, Status.ACTIVE);
        ArrayList<Subscription> result = manager.searchByName(null);
        assertTrue(result.isEmpty(), "Null input should return empty result");
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
    @Test
    void searchByCategory_mixedResults() {
        //Mixed categories should filter correctly
        SubscriptionManager manager = new SubscriptionManager();

        manager.storeNewSubscription("Netflix", 10, "Entertainment", "Monthly", null, null, Status.ACTIVE);
        manager.storeNewSubscription("Gym", 50, "Fitness", "Monthly", null, null, Status.ACTIVE);
        ArrayList<Subscription> result = manager.searchByCategory("Entertainment");
        assertEquals(1, result.size(), "Should only return the matching category");
        assertEquals("Netflix", result.get(0).getName(), "Correct item should be returned");
    }

    @Test
    void searchByCategory_emptyList() {
        //Empty list should return empty result
        SubscriptionManager manager = new SubscriptionManager();

        ArrayList<Subscription> result = manager.searchByCategory("Entertainment");
        assertTrue(result.isEmpty(), "Empty list should return empty result");
    }
}
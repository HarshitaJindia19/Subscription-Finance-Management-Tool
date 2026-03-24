import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class SubscriptionManagerTest {

    private SubscriptionManager manager;

    //creates a new clean SubscriptionManager before each test so tests dont interfere w eachother
    @BeforeEach
    public void clean() {
        manager = new SubscriptionManager();
    }

    //-------------------------------- findingSubscriptionIndex --------------------------------

    @Test
    void findSubscriptionIndex() {
        manager.storeNewSubscription("Netflix", 18.99, "Entertainment", "Monthly", LocalDate.of(2025, 3, 9), null, Status.ACTIVE);
        int index = manager.findingSubscriptionIndex("Netflix");
        assertEquals(0, index,"netflix should be at index 0");
    }

    @Test
    void findSubscriptionIndex_notFound(){
        int index = manager.findingSubscriptionIndex("Hulu");
        assertEquals(-1,index, "should return -1 when subscription not found");
    }

    @Test
    void findSubscriptionIndex_caseInsensitive() {
        manager.storeNewSubscription("Netflix", 17.99, "Entertainment", "Monthly", LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        int index = manager.findingSubscriptionIndex("netflix");
        assertEquals(0, index, "should find subscription even with lowercase input");
    }

    //-------------------------------- changeSubscriptionChoice --------------------------------

    @Test
    void updateSubscriptionChoice_status() {
        manager.storeNewSubscription("Netflix", 18.99, "Entertainment", "Monthly", LocalDate.of(2025, 3, 9), null, Status.ACTIVE);
        manager.changeSubscriptionChoice(0, 6, "PAUSED");
        assertEquals(Status.PAUSED, manager.getAllSubscriptions().get(0).getStatus(), "should update status to PAUSED");
    }

    @Test
    void changeSubscriptionChoice_updateCost() {
        manager.storeNewSubscription("Netflix", 15.99, "Entertainment", "Monthly", LocalDate.of(2025, 3, 1), null, Status.ACTIVE);
        manager.changeSubscriptionChoice(0, 1, "19.99");
        assertEquals(19.99, manager.getAllSubscriptions().get(0).getCost(), "should update cost to 19.99");
    }

    //--------------------------------storeNewSubscription --------------------------------

    @Test
    void storeNewSubscription_duplicateName() {
        manager.storeNewSubscription("Netflix", 15.99, "Entertainment", "Monthly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        assertFalse(manager.storeNewSubscription("Netflix", 15.99, "Entertainment", "Monthly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE),
                "Duplicate name should return false");
        assertFalse(manager.storeNewSubscription("netflix", 9.99, "Education", "Yearly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE),
                "Case-insensitive duplicate should return false");
    }

    @Test
    void storeNewSubscription_createsMonthly() {
        manager.storeNewSubscription("Netflix", 15.99, "Entertainment", "Monthly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        Subscription sub = manager.getAllSubscriptions().get(0);
        assertTrue(sub instanceof MonthlySubscription, "Should create a MonthlySubscription");
        assertEquals("Netflix", sub.getName());
        assertEquals(15.99, sub.getCost());
        assertEquals("Entertainment", sub.getCategory());
    }

    @Test
    void storeNewSubscription_createsYearly() {
        manager.storeNewSubscription("Adobe", 120.00, "Work", "Yearly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        Subscription sub = manager.getAllSubscriptions().get(0);
        assertTrue(sub instanceof YearlySubscription, "Should create a YearlySubscription");
    }

    // --------------------------------Monthly spend------------------------------

    @Test
    void monthlySpend_onlyChecksActive() {
        manager.storeNewSubscription("Netflix", 15.99, "Entertainment", "Monthly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        manager.storeNewSubscription("Spotify", 9.99, "Entertainment", "Monthly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        manager.storeNewSubscription("GoodEarth", 13.99, "Entertainment", "Monthly", LocalDate.of(2025, 1, 1), null, Status.CANCELLED);
        manager.storeNewSubscription("ArtBox", 110.99, "Entertainment", "Yearly", LocalDate.of(2025, 1, 1), null, Status.PAUSED);
        assertEquals(25.98, manager.calculateMonthlySpend(), 0.01, "Should only sum ACTIVE subscriptions");
    }

    @Test
    void monthlySpend_yearlyDividedBy12() {
        manager.storeNewSubscription("Adobe", 120.00, "Work", "Yearly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        assertEquals(10.00, manager.calculateMonthlySpend(), 0.01, "Yearly cost should be divided by 12 for monthly spend");
    }

    @Test
    void monthlySpend_emptyList() {
        assertEquals(0.0, manager.calculateMonthlySpend(), "Empty list should return 0");
    }

    // -------------------------------- Yearly spend -------------------------------

    @Test
    void yearlySpend_emptyList() {
        assertEquals(0.0, manager.calculateYearlySpend(), "Empty list should return 0");
    }

    @Test
    void yearlySpend_monthlyTimes12() {
        manager.storeNewSubscription("Adobe", 10.00, "Work", "Monthly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        assertEquals(120.00, manager.calculateYearlySpend(), 0.01, "Monthly cost should be multiplied by 12 for yearly spend");
    }

    @Test
    void yearlySpend_onlyChecksActive() {
        manager.storeNewSubscription("Netflix", 15.99, "Entertainment", "Yearly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        manager.storeNewSubscription("Spotify", 9.99, "Entertainment", "Yearly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        manager.storeNewSubscription("GoodEarth", 13.99, "Entertainment", "Monthly", LocalDate.of(2025, 1, 1), null, Status.CANCELLED);
        manager.storeNewSubscription("ArtBox", 110.99, "Entertainment", "Yearly", LocalDate.of(2025, 1, 1), null, Status.PAUSED);
        assertEquals(25.98, manager.calculateYearlySpend(), 0.01, "Should only sum ACTIVE subscriptions");
    }

    // -------------------------------- budget-------------------------------

    @Test
    void getSetCategoryBudget_single() {
        manager.setCategoryBudget("Entertainment", 50.00);
        assertEquals(50.00, manager.getCategoryBudget("Entertainment"), "should return the budget that was set");
    }

    @Test
    void setCategoryBudget_caseInsensitive() {
        manager.setCategoryBudget("entertainment", 50.00);
        assertEquals(50.00, manager.getCategoryBudget("Entertainment"), "category should be stored in title case");
    }

    @Test
    void getCategoryBudget_notFound() {
        assertEquals(-1.0, manager.getCategoryBudget("Entertainment"), "no budget set should return -1");
    }

    @Test
    void getAllBudgets_emptyList() {
        assertTrue(manager.getAllBudgets().isEmpty(), "No budgets should be stored initially");
    }

    @Test
    void getAllBudgets_multipleBudgets() {
        manager.setCategoryBudget("Entertainment", 50.00);
        manager.setCategoryBudget("Fitness", 75.00);
        manager.setCategoryBudget("Education", 30.00);
        assertEquals(3, manager.getAllBudgets().size(), "Should have 3 budgets saved");
    }

    //--------------------------------isValidCost --------------------------------

    @Test
    void isValidCost_validAmounts() {
        assertTrue(SubscriptionManager.isValidCost("50.00"), "50.00 should be valid");
        assertTrue(SubscriptionManager.isValidCost("23"), "23 should be valid");
    }
    @Test
    void isValidCost_invalidStrings() {
        assertFalse(SubscriptionManager.isValidCost("abc"), "Letters should be invalid");
        assertFalse(SubscriptionManager.isValidCost("$34.55"), "$ sign should be invalid");
    }

    @Test
    void isValidCost_zero() {
        assertFalse(SubscriptionManager.isValidCost("0"), "0 should be invalid");
    }
    @Test
    void isValidCost_negative() {
        assertFalse(SubscriptionManager.isValidCost("-45.00"), "Negative cost should be invalid");
    }

    //--------------------------------isValidDate -------------------------------
    @Test
    void isValidDate_invalidDates() {
        assertFalse(SubscriptionManager.isValidDate("13/01/2026"), "Month 13 doesn't exist");
        assertFalse(SubscriptionManager.isValidDate("03/32/2026"), "Day 32 doesn't exist");
        assertFalse(SubscriptionManager.isValidDate("2026/01/03"), "Wrong format should be invalid");
        assertFalse(SubscriptionManager.isValidDate(""), "Empty string should be invalid");
    }

    @Test
    void isValidDate_validDate() {
        assertTrue(SubscriptionManager.isValidDate("03/01/2026"), "Valid date should return true");
    }

    //--------------------------------formatTitleCase --------------------------

    @Test
    void formatTitleCase_valid() {
        assertEquals("Education", SubscriptionManager.formatTitleCase("education"));
        assertEquals("Education", SubscriptionManager.formatTitleCase("EDUCATION"));
        assertEquals("Education", SubscriptionManager.formatTitleCase("EDucaTioN"));
    }

    //--------------------------------search --------------------------------

    @Test
    void searchByName_found() {
        manager.storeNewSubscription("Spotify", 7.89, "Music", "Yearly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        ArrayList<Subscription> results = manager.searchByName("Spotify");
        assertEquals(1, results.size(), "Should find 1 subscription named Spotify");
        assertEquals("Spotify", results.get(0).getName());
    }
    @Test
    void searchByName_notFound() {
        ArrayList<Subscription> results = manager.searchByName("Netflix");
        assertTrue(results.isEmpty(), "No subscriptions should match Netflix in empty list");
    }
    @Test
    void searchByName_emptyInput() {
        manager.storeNewSubscription("Netflix", 8.00, "Entertainment", "Monthly", LocalDate.of(2025, 1, 1), null, Status.ACTIVE);
        assertTrue(manager.searchByName("").isEmpty(), "Empty input should return empty list");
        assertTrue(manager.searchByName(null).isEmpty(), "Null input should return empty list");
    }
}
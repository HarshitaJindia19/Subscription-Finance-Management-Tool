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

    //----------------------------------- Add Budget Category Function Tests ---------------------------------//

    @Test
    public void getSetCategoryBudget_SingleBudget() {
        Data.setCategoryBudget("Entertainment", 50.00);
        assertEquals(50.00, Data.getCategoryBudget("Entertainment"), "Entertainment budget is (set) 50.00, should output 50.00 when (get)ing it.");
    }

    @Test
    public void setCategoryBudget_CaseInsensitive() {
        // should store 'entertainment' as 'Entertainment'
        Data.setCategoryBudget("entertainment", 50.00);
        assertEquals(50.00, Data.getCategoryBudget("Entertainment"), "Category was not converted to title case!");
    }

    @Test
    public void getCategoryBudget_notFound() {
        // category with no budget should return -1
        // categories (empty)
        assertEquals(-1.0, Data.getCategoryBudget("Entertainment"), "No budgets in empty categories HashSet!");

        // categories (not empty)
        Data.setCategoryBudget("Education", 50.00);
        Data.categories.add("Health");
        Data.categories.add("Food");
        assertEquals(-1.0, Data.getCategoryBudget("Health"), "No budget set for Health!");
        assertEquals(-1.0, Data.getCategoryBudget("Entertainment"), "No budget set for Entertainment!"); // future category
    }

    @Test
    public void setCategoryBudget_AddToCategories() {
        // setting a budget should also add category to categroies HashSet
        Data.setCategoryBudget("Fitness", 75.00);
        assertTrue(Data.categories.contains("Fitness"), "Budget category was not added to categories!");
    }

    //--------------------------------- isValidCost() Tests ------------------------------//

    @Test
    public void isValidCost_ValidAmount() {
        assertTrue(Data.isValidCost("50.00"),"50.00 can be parsed to double (Valid Cost)!");
        assertTrue(Data.isValidCost("23"),"23 can be parsed to double (Valid Cost)!");
        assertTrue(Data.isValidCost("23.92128164"), "23.92128164 can be parsed to double (Valid Cost)!");
    }

    @Test
    public void isValidBudget_inValidString() {
        assertFalse(Data.isValidCost("abc58fgd"), "String with characters cannot be parsed to double! (Invalid cost)."); // with numbers
        assertFalse(Data.isValidCost("$#@(^@."), "String with characters cannot be parsed to double! (Invalid cost)."); // symbols
        assertFalse(Data.isValidCost("$34.55"),"$ cannot be parsed to double (Invalid cost)"); // $ should be stripped by addCategoryBudget already
    }

    @Test
    public void isValidCost_Zero() {
        assertFalse(Data.isValidCost("0"), "Cost cannot be 0 (Invalid Cost)!");
    }

    @Test
    public void isValidCost_NegativeAmount() {
        assertFalse(Data.isValidCost("-45.00"), "Cost cannot be -ve (Invalid cost)!"); // -ve double
        assertFalse(Data.isValidCost("-54"),"Cost cannot be -ve (Invalid cost)!"); // -ve int
    }
}
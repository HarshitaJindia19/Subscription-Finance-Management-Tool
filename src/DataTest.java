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

    @Test
    void findSubscriptionIndex() {
        //clear any existing data so tests dont interfere
        Data.subscriptions.clear();
        Data.names.clear();
        //create test subscription and add to subscriptions ArrayList
        ArrayList<String> subscription = new ArrayList<>();
        subscription.add("Netflix");
        subscription.add("18.99");
        subscription.add("Entertainment");
        subscription.add("Monthly");
        subscription.add("March 9");
        subscription.add("April 9");
        subscription.add("ACTIVE");
        Data.subscriptions.add(subscription);

        //searching for Netflix which should be at index 0
        int index = Data.findingSubscriptionIndex("Netflix");
        assertEquals(0, index, "Netflix should be in index 0");
    }

    @Test
    void findSubscriptionIndex_notFound() {
        Data.subscriptions.clear();
        Data.names.clear();
        //searching for a subscription that was never added (should return -1)
        int index = Data.findingSubscriptionIndex("Hulu");
        assertEquals(-1, index, "should return -1 when subscription not found");
    }

    @Test
    void findSubscriptionIndex_caseInsensitive() {
        Data.subscriptions.clear();
        Data.names.clear();
        //add subscription with uppercase Netflix
        ArrayList<String> subscription = new ArrayList<>();
        subscription.add("Netflix");
        subscription.add("17.99");
        subscription.add("Entertainment");
        subscription.add("Monthly");
        subscription.add("03/01/2025");
        subscription.add("03/01/2026");
        subscription.add("ACTIVE");
        Data.subscriptions.add(subscription);

        //search using lowercase Netflix
        int index = Data.findingSubscriptionIndex("netflix");
        assertEquals(0, index, "should find subscription even if lowercase.");
    }

    @Test
    void updateSubscriptionChoice(){
        //add test subscription with ACTIVE status
        ArrayList<String> subscription= new ArrayList<>();
        subscription.add("Netflix");
        subscription.add("18.99");
        subscription.add("Entertainment");
        subscription.add("Monthly");
        subscription.add("March 9");
        subscription.add("April 9");
        subscription.add("ACTIVE");
        Data.subscriptions.add(subscription);

        //update status field (INDEX_STATUS=9) to "PAUSED"
        Data.changeSubscriptionChoice(0,6, "PAUSED");
        //verify that status was updated correctly
        assertEquals("PAUSED",Data.subscriptions.get(Data.INDEX_NAME).get(Data.INDEX_STATUS), "Should update status to PAUSED");

    }
    @Test
    void changeSubscriptionChoice_updateCost() {
        Data.subscriptions.clear();
        Data.names.clear();
        //add test subscription with cost 15.99
        ArrayList<String> subscription = new ArrayList<>();
        subscription.add("Netflix");
        subscription.add("15.99");
        subscription.add("Entertainment");
        subscription.add("Monthly");
        subscription.add("03/01/2025");
        subscription.add("03/01/2026");
        subscription.add("ACTIVE");
        Data.subscriptions.add(subscription);
        //update cost field (INDEX_COST =1) tp "19.99"
        Data.changeSubscriptionChoice(0, Data.INDEX_COST, "19.99");
        //verify cost was updated correctly
        assertEquals("19.99", Data.subscriptions.get(Data.INDEX_NAME).get(Data.INDEX_COST), "Should update cost to 19.99");
    }

    @Test
    void isValidStatus_validInput() {
        //all three values in STATUS_OPTIONS should return true
        assertTrue(Data.isValidStatus("ACTIVE"), "ACTIVE should be valid");
        assertTrue(Data.isValidStatus("PAUSED"), "PAUSED should be valid");
        assertTrue(Data.isValidStatus("CANCELLED"), "CANCELLED should be valid");
    }

    @Test
    void isValidStatus_invalidInput() {
        //lowercase active shouldn't match any STATUS_OPTIONS
        assertFalse(Data.isValidStatus("active"), "Lowercase should be invalid");
        //random string HELLO shouldn't match any STATUS_OPTIONS
        assertFalse(Data.isValidStatus("HELLO"), "Random string should be invalid");
        //empty string shouldn't match any STATUS_OPTIONS
        assertFalse(Data.isValidStatus(""), "Empty string should be invalid");
    }

    //------------------------------ Store New Subscription ----------------------------//
    @Test
    void storeNewSubscription_duplicateName() {
        // storing same name twice should return false
        Data.storeNewSubscription("Netflix", "15.99", "Entertainment", "Monthly", "01/01/2025", "N/A", "ACTIVE");
        assertFalse(Data.storeNewSubscription("Netflix", "15.99", "Entertainment", "Monthly", "01/01/2025", "N/A", "ACTIVE"), "The same name subscription was saved twice!");
        assertFalse(Data.storeNewSubscription("netflix", "15.99", "Education", "Yearly", "01/01/2025", "01/01/2025", "ACTIVE"), "The same name subscription was saved twice!"); // diff params
    }

    @Test
    void storeNewSubscription_CorrectDataStored() {
        // verify the data is stored at the right indexes
        Data.storeNewSubscription("Netflix", "15.99", "Entertainment", "Monthly", "01/01/2025", "N/A", "ACTIVE");
        ArrayList<String> sub = Data.subscriptions.getFirst();
        assertEquals("netflix", sub.get(Data.INDEX_NAME), "netflix should be stored. Something else was stored!");
        assertEquals("15.99", sub.get(Data.INDEX_COST), "15.99 should be stored. Something else was stored!");
        assertEquals("Entertainment", sub.get(Data.INDEX_CATEGORY), "Entertainment should be stored. Something else was stored!");
        assertEquals("Monthly", sub.get(Data.INDEX_RENEWAL), "Monthly should be stored. Something else was stored!");
        assertEquals("01/01/2025", sub.get(Data.INDEX_START_DATE), "01/01/2025 should be stored. Something else was stored!");
        assertEquals("N/A", sub.get(Data.INDEX_END_DATE), "N/A should be stored. Something else was stored!");
        assertEquals("ACTIVE", sub.get(Data.INDEX_STATUS), "ACTIVE should be stored. Something else was stored!");

        // Check if it was also stored in categories and names
        assertTrue(Data.names.contains("netflix"));
        assertTrue(Data.categories.contains("Entertainment"));

    }

    //------------------------------ Monthly Spend Tests -------------------------------//

    @Test
    void monthlySpend_OnlyChecksActive() {

        Data.storeNewSubscription("Netflix", "15.99", "Entertainment", "Monthly", "01/01/2025", null, "ACTIVE");
        Data.storeNewSubscription("Spotify", "9.99", "Entertainment", "Monthly", "01/01/2025", null, "ACTIVE");
        Data.storeNewSubscription("GoodEarth", "13.99", "Entertainment", "Monthly", "01/01/2025", null, "CANCELLED");
        Data.storeNewSubscription("ArtBox", "110.99", "Entertainment", "Yearly", "01/01/2025", null, "PAUSED");

        assertEquals(25.98, Data.calculateMonthlySpend(), "Monthly cost was not calculated properly!");
    }

    @Test
    void monthlySpend_YearDividedBy12() {
        Data.storeNewSubscription("Adobe", "120.00", "Work", "Yearly", "01/01/2025", null, "ACTIVE");
        assertEquals(10.00, Data.calculateMonthlySpend(), "Monthly cost was not calculated properly! Yearly costs must be divided by 12!");

        clean();
        Data.storeNewSubscription("Spotify", "34.99", "Entertainment", "Yearly", "01/01/2025", null, "ACTIVE");
        assertEquals(2.9158, Data.calculateMonthlySpend(), "Monthly cost was not calculated properly! Yearly costs must be divided by 12!");

    }

    @Test
    void monthlySpend_EmptyList() {
        assertEquals(0.0, Data.calculateMonthlySpend(), "Empty list should have no costs!");
    }

    //------------------------------------ Yearly Spend Functions -------------------------------//

    @Test
    void yearlySpend_EmptyList() {
        assertEquals(0.0, Data.calculateYearlySpend(), "Empty list should have no costs!");
    }

    @Test
    void yearlySpend_MonthTimes12() {
        Data.storeNewSubscription("Adobe", "10.00", "Work", "Monthly", "01/01/2025", null, "ACTIVE");

        assertEquals(120.00, Data.calculateYearlySpend(), "Yearly cost was not calculated properly! Month should be times by 12.");
    }

    @Test
    void yearlySpend_OnlyChecksActive() {
        Data.storeNewSubscription("Netflix", "15.99", "Entertainment", "Yearly", "01/01/2025", null, "ACTIVE");
        Data.storeNewSubscription("Spotify", "9.99", "Entertainment", "Yearly", "01/01/2025", null, "ACTIVE");
        Data.storeNewSubscription("GoodEarth", "13.99", "Entertainment", "Monthly", "01/01/2025", null, "CANCELLED");
        Data.storeNewSubscription("ArtBox", "110.99", "Entertainment", "Yearly", "01/01/2025", null, "PAUSED");

        assertEquals(25.98, Data.calculateYearlySpend(), "Yearly cost was not calculated properly!");
    }

    //----------------------------------- Add Budget Category Function Tests ---------------------------------//

    @Test
    void getSetCategoryBudget_SingleBudget() {
        Data.setCategoryBudget("Entertainment", 50.00);
        assertEquals(50.00, Data.getCategoryBudget("Entertainment"), "Entertainment budget is (set) 50.00, should output 50.00 when (get)ing it.");
    }

    @Test
    void setCategoryBudget_CaseInsensitive() {
        // should store 'entertainment' as 'Entertainment'
        Data.setCategoryBudget("entertainment", 50.00);
        assertEquals(50.00, Data.getCategoryBudget("Entertainment"), "Category was not converted to title case!");
    }

    @Test
    void getCategoryBudget_notFound() {
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
    void setCategoryBudget_AddToCategories() {
        // setting a budget should also add category to categroies HashSet
        Data.setCategoryBudget("Fitness", 75.00);
        assertTrue(Data.categories.contains("Fitness"), "Budget category was not added to categories!");
    }

    @Test
    void formatTitleCase_CheckValid() {
        assertEquals("Education", Data.formatTitleCase("education"), "Should convert string input to title case!");
        assertEquals("Education", Data.formatTitleCase("EDucaTioN"), "Should convert string input to title case!");
        assertEquals("Education", Data.formatTitleCase("EDUCATION"), "Should convert string input to title case!");

    }

    @Test
    void getAllBudgets_EmptyList() {
        // no budgets set should return empty list
        assertTrue(Data.getAllBudgets().isEmpty(), "No budgets should be stored!");
    }

    @Test
    void getAllBudgets_MultipleBudgets() {
        Data.setCategoryBudget("Entertainment", 50.00);
        Data.setCategoryBudget("Fitness", 75.00);
        Data.setCategoryBudget("Education", 30.00);
        assertEquals(3, Data.getAllBudgets().size(), "There should be 3 budgets saved!");
    }

    //--------------------------------- isValidCost() Tests ------------------------------//

    @Test
    void isValidCost_ValidAmount() {
        assertTrue(Data.isValidCost("50.00"),"50.00 can be parsed to double (Valid Cost)!");
        assertTrue(Data.isValidCost("23"),"23 can be parsed to double (Valid Cost)!");
        assertTrue(Data.isValidCost("23.92128164"), "23.92128164 can be parsed to double (Valid Cost)!");
    }

    @Test
    void isValidBudget_InvalidString() {
        assertFalse(Data.isValidCost("abc58fgd"), "String with characters cannot be parsed to double! (Invalid cost)."); // with numbers
        assertFalse(Data.isValidCost("$#@(^@."), "String with characters cannot be parsed to double! (Invalid cost)."); // symbols
        assertFalse(Data.isValidCost("$34.55"),"$ cannot be parsed to double (Invalid cost)"); // $ should be stripped by addCategoryBudget already
    }

    @Test
    void isValidCost_Zero() {
        assertFalse(Data.isValidCost("0"), "Cost cannot be 0 (Invalid Cost)!");
    }

    @Test
    void isValidCost_NegativeAmount() {
        assertFalse(Data.isValidCost("-45.00"), "Cost cannot be -ve (Invalid cost)!"); // -ve double
        assertFalse(Data.isValidCost("-54"),"Cost cannot be -ve (Invalid cost)!"); // -ve int
    }

    //------------------------------------- is Valid Date --------------------------------------//
    @Test
    void isValidDate_InvalidDates() {
        assertFalse(Data.isValidDate("13/01/2026"), "Month 13 doesn't exist."); // invalid month
        assertFalse(Data.isValidDate("03/32/2026"), "Day 32 doesn't exist."); // invalid date
        assertFalse(Data.isValidDate("03/01/1999"), "Year must be greater than 2000."); // invalid year

        assertFalse(Data.isValidDate("2026/01/03"), "Date must be in MM/DD/YYYY format!");
        assertFalse(Data.isValidDate("ab/cd/efgh"), "Date cannot have non-numbers.");
        assertFalse(Data.isValidDate(""), "Date cannot be empty!");
        assertFalse(Data.isValidDate("03012026"), "Date must have slashes!");

    }

    @Test
    void isValidDate_ValidDates() {
        assertTrue(Data.isValidDate("03/01/2026"), "Date is valid!");
    }

    //------------------------------------- Search Engine --------------------------------------//
    @Test
    public void searchByNameFound() {
        // Clear any existing subs
        Data.subscriptions.clear();
        ArrayList<String> sub = new ArrayList<>();
        sub.add("Spotify");
        sub.add("7.89");
        sub.add("Music");
        sub.add("Yearly");
        sub.add("March 2");
        sub.add(null);
        sub.add("ACTIVE");
        Data.subscriptions.add(sub); //Now program has 1 sub: Spotify

        //Do a search calls function
        ArrayList<ArrayList<String>> results = Data.searchByName("Spotify");
        //Give the result size
        assertEquals(1, results.size(), "Should find 1 subscription named Spotify");
        //Give the name of the first result
        //expects 1 and compares result.size if match pass, if not test failed
        ArrayList<String> numOneResult = results.get(Data.INDEX_NAME);//get the first list index and the name from it
        String name = numOneResult.get(Data.INDEX_NAME);

        assertEquals("Spotify", name);//Confirms it found Spotify, not something else
    }

    @Test
    public void searchByNameNotFound() {
        //Clear subs
        Data.subscriptions.clear();
        //search for a sub that doesn't exist
        ArrayList<ArrayList<String>> results = Data.searchByName("Netflix");
        //Not found
        assertTrue(results.isEmpty(), "No subscriptions should match Netflix");
    }
    @Test
    public void searchByNameEmptyInput() {
        // Clear subscriptions
        Data.subscriptions.clear();
        ArrayList<String> sub = new ArrayList<>();
        sub.add("Netflix");
        sub.add("8.00");
        sub.add("Music");
        sub.add("Yearly");
        sub.add("March 3");
        sub.add(null);
        sub.add("ACTIVE");
        Data.subscriptions.add(sub);

        //Search with empty string
        ArrayList<ArrayList<String>> results = Data.searchByName("");
        assertTrue(results.isEmpty(), "Empty input should have no results");

        //Search with null
        results = Data.searchByName(null);
        assertTrue(results.isEmpty(), "Null input should have no results");
    }
}
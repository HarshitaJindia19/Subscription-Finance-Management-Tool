//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//public class SubscriptionManagerTest {
//
//    // cleans all stored data before each test
//    @BeforeEach
//    public void clean() {
//        SubscriptionManager.subscriptions.clear();
//        SubscriptionManager.categories.clear();
//        SubscriptionManager.names.clear();
//        SubscriptionManager.categoryBudgets.clear();
//    }
//
//    @Test
//    void findSubscriptionIndex() {
//        //clear any existing data so tests dont interfere
//        SubscriptionManager.subscriptions.clear();
//        SubscriptionManager.names.clear();
//        SubscriptionManager.storeNewSubscription("Netflix", 18.99, "Entertainment", "Monthly", LocalDate.of(2025, 3, 9), null, "ACTIVE");
//        //search for Netflix which should be at index 0
//        int index = SubscriptionManager.findingSubscriptionIndex("Netflix");
//        assertEquals(0, index, "Netflix should be in index 0");
//    }
//
//    @Test
//    void findSubscriptionIndex_notFound() {
//        SubscriptionManager.subscriptions.clear();
//        SubscriptionManager.names.clear();
//        //searching for a subscription that was never added (should return -1)
//        int index = SubscriptionManager.findingSubscriptionIndex("Hulu");
//        assertEquals(-1, index, "Should return -1 when subscription not found");}
//
//    @Test
//    void findSubscriptionIndex_caseInsensitive() {
//        SubscriptionManager.subscriptions.clear();
//        SubscriptionManager.names.clear();
//        //add subscription with uppercase Netflix
//        SubscriptionManager.storeNewSubscription("Netflix", 17.99, "Entertainment", "Monthly",LocalDate.of(2025, 3, 1), null, "ACTIVE");
//        //search using lowercase Netflix
//        int index = SubscriptionManager.findingSubscriptionIndex("netflix");
//        assertEquals(0, index, "should find subscription even if lowercase.");
//    }
//
//    @Test
//    void updateSubscriptionChoice(){
//        SubscriptionManager.subscriptions.clear();
//        SubscriptionManager.names.clear();
//        //add test subscription with ACTIVE status
//        SubscriptionManager.storeNewSubscription("Netflix", 18.99,"Entertainment", "Monthly",LocalDate.of(2025, 3, 9), null, "ACTIVE");
//        //update status field to PAUSED
//        SubscriptionManager.changeSubscriptionChoice(0, 6, "PAUSED");
//        //check status was updated properly
//        assertEquals("PAUSED", SubscriptionManager.subscriptions.get(0).getStatus(), "Should update status to PAUSED");
//    }
//
//    @Test
//    void changeSubscriptionChoice_updateCost() {
//        SubscriptionManager.subscriptions.clear();
//        SubscriptionManager.names.clear();
//        // add test subscription with cost 15.99
//        SubscriptionManager.storeNewSubscription("Netflix", 15.99, "Entertainment", "Monthly",LocalDate.of(2025, 3, 1), null, "ACTIVE");
//        SubscriptionManager.changeSubscriptionChoice(0, 1, "19.99");
//        assertEquals(19.99, SubscriptionManager.subscriptions.get(0).getCost(), "Should update cost to 19.99");
//    }
//
//    @Test
//    void isValidStatus_validInput() {
//        //all three values in STATUS_OPTIONS should return true
//        assertTrue(SubscriptionManager.isValidStatus("ACTIVE"), "ACTIVE should be valid");
//        assertTrue(SubscriptionManager.isValidStatus("PAUSED"), "PAUSED should be valid");
//        assertTrue(SubscriptionManager.isValidStatus("CANCELLED"), "CANCELLED should be valid");
//    }
//
//    @Test
//    void isValidStatus_invalidInput() {
//        //lowercase active shouldn't match any STATUS_OPTIONS
//        assertFalse(SubscriptionManager.isValidStatus("active"), "Lowercase should be invalid");
//        //random string HELLO shouldn't match any STATUS_OPTIONS
//        assertFalse(SubscriptionManager.isValidStatus("HELLO"), "Random string should be invalid");
//        //empty string shouldn't match any STATUS_OPTIONS
//        assertFalse(SubscriptionManager.isValidStatus(""), "Empty string should be invalid");
//    }
//
//    //------------------------------ Store New Subscription ----------------------------//
//    @Test
//    void storeNewSubscription_duplicateName() {
//        // storing same name twice should return false
//        SubscriptionManager.storeNewSubscription("Netflix", "15.99", "Entertainment", "Monthly", "01/01/2025", "N/A", "ACTIVE");
//        assertFalse(SubscriptionManager.storeNewSubscription("Netflix", "15.99", "Entertainment", "Monthly", "01/01/2025", "N/A", "ACTIVE"), "The same name subscription was saved twice!");
//        assertFalse(SubscriptionManager.storeNewSubscription("netflix", "15.99", "Education", "Yearly", "01/01/2025", "01/01/2025", "ACTIVE"), "The same name subscription was saved twice!"); // diff params
//    }
//
//    @Test
//    void storeNewSubscription_CorrectDataStored() {
//        // verify the data is stored at the right indexes
//        SubscriptionManager.storeNewSubscription("Netflix", "15.99", "Entertainment", "Monthly", "01/01/2025", "N/A", "ACTIVE");
//        ArrayList<String> sub = SubscriptionManager.subscriptions.getFirst();
//        assertEquals("Netflix", sub.get(SubscriptionManager.INDEX_NAME), "netflix should be stored. Something else was stored!");
//        assertEquals("15.99", sub.get(SubscriptionManager.INDEX_COST), "15.99 should be stored. Something else was stored!");
//        assertEquals("Entertainment", sub.get(SubscriptionManager.INDEX_CATEGORY), "Entertainment should be stored. Something else was stored!");
//        assertEquals("Monthly", sub.get(SubscriptionManager.INDEX_RENEWAL), "Monthly should be stored. Something else was stored!");
//        assertEquals("01/01/2025", sub.get(SubscriptionManager.INDEX_START_DATE), "01/01/2025 should be stored. Something else was stored!");
//        assertEquals("N/A", sub.get(SubscriptionManager.INDEX_END_DATE), "N/A should be stored. Something else was stored!");
//        assertEquals("ACTIVE", sub.get(SubscriptionManager.INDEX_STATUS), "ACTIVE should be stored. Something else was stored!");
//
//        // Check if it was also stored in categories and names
//        assertTrue(SubscriptionManager.names.contains("Netflix"));
//        assertTrue(SubscriptionManager.categories.contains("Entertainment"));
//
//    }
//
//    //------------------------------ Monthly Spend Tests -------------------------------//
//
//    @Test
//    void monthlySpend_OnlyChecksActive() {
//
//        SubscriptionManager.storeNewSubscription("Netflix", "15.99", "Entertainment", "Monthly", "01/01/2025", null, "ACTIVE");
//        SubscriptionManager.storeNewSubscription("Spotify", "9.99", "Entertainment", "Monthly", "01/01/2025", null, "ACTIVE");
//        SubscriptionManager.storeNewSubscription("GoodEarth", "13.99", "Entertainment", "Monthly", "01/01/2025", null, "CANCELLED");
//        SubscriptionManager.storeNewSubscription("ArtBox", "110.99", "Entertainment", "Yearly", "01/01/2025", null, "PAUSED");
//
//        assertEquals(25.98, SubscriptionManager.calculateMonthlySpend(), "Monthly cost was not calculated properly!");
//    }
//
//    @Test
//    void monthlySpend_YearDividedBy12() {
//        SubscriptionManager.storeNewSubscription("Adobe", "120.00", "Work", "Yearly", "01/01/2025", null, "ACTIVE");
//        assertEquals(10.00, SubscriptionManager.calculateMonthlySpend(), "Monthly cost was not calculated properly! Yearly costs must be divided by 12!");
//
//        clean();
//        SubscriptionManager.storeNewSubscription("Spotify", "34.99", "Entertainment", "Yearly", "01/01/2025", null, "ACTIVE");
//        assertEquals(2.9158333333333335, SubscriptionManager.calculateMonthlySpend(), "Monthly cost was not calculated properly! Yearly costs must be divided by 12!");
//
//    }
//
//    @Test
//    void monthlySpend_EmptyList() {
//        assertEquals(0.0, SubscriptionManager.calculateMonthlySpend(), "Empty list should have no costs!");
//    }
//
//    //------------------------------------ Yearly Spend Functions -------------------------------//
//
//    @Test
//    void yearlySpend_EmptyList() {
//        assertEquals(0.0, SubscriptionManager.calculateYearlySpend(), "Empty list should have no costs!");
//    }
//
//    @Test
//    void yearlySpend_MonthTimes12() {
//        SubscriptionManager.storeNewSubscription("Adobe", "10.00", "Work", "Monthly", "01/01/2025", null, "ACTIVE");
//
//        assertEquals(120.00, SubscriptionManager.calculateYearlySpend(), "Yearly cost was not calculated properly! Month should be times by 12.");
//    }
//
//    @Test
//    void yearlySpend_OnlyChecksActive() {
//        SubscriptionManager.storeNewSubscription("Netflix", "15.99", "Entertainment", "Yearly", "01/01/2025", null, "ACTIVE");
//        SubscriptionManager.storeNewSubscription("Spotify", "9.99", "Entertainment", "Yearly", "01/01/2025", null, "ACTIVE");
//        SubscriptionManager.storeNewSubscription("GoodEarth", "13.99", "Entertainment", "Monthly", "01/01/2025", null, "CANCELLED");
//        SubscriptionManager.storeNewSubscription("ArtBox", "110.99", "Entertainment", "Yearly", "01/01/2025", null, "PAUSED");
//
//        assertEquals(25.98, SubscriptionManager.calculateYearlySpend(), "Yearly cost was not calculated properly!");
//    }
//
//    //----------------------------------- Add Budget Category Function Tests ---------------------------------//
//
//    @Test
//    void getSetCategoryBudget_SingleBudget() {
//        SubscriptionManager.setCategoryBudget("Entertainment", 50.00);
//        assertEquals(50.00, SubscriptionManager.getCategoryBudget("Entertainment"), "Entertainment budget is (set) 50.00, should output 50.00 when (get)ing it.");
//    }
//
//    @Test
//    void setCategoryBudget_CaseInsensitive() {
//        // should store 'entertainment' as 'Entertainment'
//        SubscriptionManager.setCategoryBudget("entertainment", 50.00);
//        assertEquals(50.00, SubscriptionManager.getCategoryBudget("Entertainment"), "Category was not converted to title case!");
//    }
//
//    @Test
//    void getCategoryBudget_notFound() {
//        // category with no budget should return -1
//        // categories (empty)
//        assertEquals(-1.0, SubscriptionManager.getCategoryBudget("Entertainment"), "No budgets in empty categories HashSet!");
//
//        // categories (not empty)
//        SubscriptionManager.setCategoryBudget("Education", 50.00);
//        SubscriptionManager.categories.add("Health");
//        SubscriptionManager.categories.add("Food");
//        assertEquals(-1.0, SubscriptionManager.getCategoryBudget("Health"), "No budget set for Health!");
//        assertEquals(-1.0, SubscriptionManager.getCategoryBudget("Entertainment"), "No budget set for Entertainment!"); // future category
//    }
//
//    @Test
//    void setCategoryBudget_AddToCategories() {
//        // setting a budget should also add category to categroies HashSet
//        SubscriptionManager.setCategoryBudget("Fitness", 75.00);
//        assertTrue(SubscriptionManager.categories.contains("Fitness"), "Budget category was not added to categories!");
//    }
//
//    @Test
//    void formatTitleCase_CheckValid() {
//        assertEquals("Education", SubscriptionManager.formatTitleCase("education"), "Should convert string input to title case!");
//        assertEquals("Education", SubscriptionManager.formatTitleCase("EDucaTioN"), "Should convert string input to title case!");
//        assertEquals("Education", SubscriptionManager.formatTitleCase("EDUCATION"), "Should convert string input to title case!");
//
//    }
//
//    @Test
//    void getAllBudgets_EmptyList() {
//        // no budgets set should return empty list
//        assertTrue(SubscriptionManager.getAllBudgets().isEmpty(), "No budgets should be stored!");
//    }
//
//    @Test
//    void getAllBudgets_MultipleBudgets() {
//        SubscriptionManager.setCategoryBudget("Entertainment", 50.00);
//        SubscriptionManager.setCategoryBudget("Fitness", 75.00);
//        SubscriptionManager.setCategoryBudget("Education", 30.00);
//        assertEquals(3, SubscriptionManager.getAllBudgets().size(), "There should be 3 budgets saved!");
//    }
//
//    //--------------------------------- isValidCost() Tests ------------------------------//
//
//    @Test
//    void isValidCost_ValidAmount() {
//        assertTrue(SubscriptionManager.isValidCost("50.00"),"50.00 can be parsed to double (Valid Cost)!");
//        assertTrue(SubscriptionManager.isValidCost("23"),"23 can be parsed to double (Valid Cost)!");
//        assertTrue(SubscriptionManager.isValidCost("23.92128164"), "23.92128164 can be parsed to double (Valid Cost)!");
//    }
//
//    @Test
//    void isValidBudget_InvalidString() {
//        assertFalse(SubscriptionManager.isValidCost("abc58fgd"), "String with characters cannot be parsed to double! (Invalid cost)."); // with numbers
//        assertFalse(SubscriptionManager.isValidCost("$#@(^@."), "String with characters cannot be parsed to double! (Invalid cost)."); // symbols
//        assertFalse(SubscriptionManager.isValidCost("$34.55"),"$ cannot be parsed to double (Invalid cost)"); // $ should be stripped by addCategoryBudget already
//    }
//
//    @Test
//    void isValidCost_Zero() {
//        assertFalse(SubscriptionManager.isValidCost("0"), "Cost cannot be 0 (Invalid Cost)!");
//    }
//
//    @Test
//    void isValidCost_NegativeAmount() {
//        assertFalse(SubscriptionManager.isValidCost("-45.00"), "Cost cannot be -ve (Invalid cost)!"); // -ve double
//        assertFalse(SubscriptionManager.isValidCost("-54"),"Cost cannot be -ve (Invalid cost)!"); // -ve int
//    }
//
//    //------------------------------------- is Valid Date --------------------------------------//
//    @Test
//    void isValidDate_InvalidDates() {
//        assertFalse(SubscriptionManager.isValidDate("13/01/2026"), "Month 13 doesn't exist."); // invalid month
//        assertFalse(SubscriptionManager.isValidDate("03/32/2026"), "Day 32 doesn't exist."); // invalid date
//        assertFalse(SubscriptionManager.isValidDate("03/01/1999"), "Year must be greater than 2000."); // invalid year
//
//        assertFalse(SubscriptionManager.isValidDate("2026/01/03"), "Date must be in MM/DD/YYYY format!");
//        assertFalse(SubscriptionManager.isValidDate("ab/cd/efgh"), "Date cannot have non-numbers.");
//        assertFalse(SubscriptionManager.isValidDate(""), "Date cannot be empty!");
//        assertFalse(SubscriptionManager.isValidDate("03012026"), "Date must have slashes!");
//
//    }
//
//    @Test
//    void isValidDate_ValidDates() {
//        assertTrue(SubscriptionManager.isValidDate("03/01/2026"), "Date is valid!");
//    }
//
//    //------------------------------------- Search Engine --------------------------------------//
//    @Test
//    public void searchByNameFound() {
//        // Clear any existing subs
//        SubscriptionManager.subscriptions.clear();
//        ArrayList<String> sub = new ArrayList<>();
//        sub.add("Spotify");
//        sub.add("7.89");
//        sub.add("Music");
//        sub.add("Yearly");
//        sub.add("March 2");
//        sub.add(null);
//        sub.add("ACTIVE");
//        SubscriptionManager.subscriptions.add(sub); //Now program has 1 sub: Spotify
//
//        //Do a search calls function
//        ArrayList<ArrayList<String>> results = SubscriptionManager.searchByName("Spotify");
//        //Give the result size
//        assertEquals(1, results.size(), "Should find 1 subscription named Spotify");
//        //Give the name of the first result
//        //expects 1 and compares result.size if match pass, if not test failed
//        ArrayList<String> numOneResult = results.get(SubscriptionManager.INDEX_NAME);//get the first list index and the name from it
//        String name = numOneResult.get(SubscriptionManager.INDEX_NAME);
//
//        assertEquals("Spotify", name);//Confirms it found Spotify, not something else
//    }
//
//    @Test
//    public void searchByNameNotFound() {
//        //Clear subs
//        SubscriptionManager.subscriptions.clear();
//        //search for a sub that doesn't exist
//        ArrayList<ArrayList<String>> results = SubscriptionManager.searchByName("Netflix");
//        //Not found
//        assertTrue(results.isEmpty(), "No subscriptions should match Netflix");
//    }
//    @Test
//    public void searchByNameEmptyInput() {
//        // Clear subscriptions
//        SubscriptionManager.subscriptions.clear();
//        ArrayList<String> sub = new ArrayList<>();
//        sub.add("Netflix");
//        sub.add("8.00");
//        sub.add("Music");
//        sub.add("Yearly");
//        sub.add("March 3");
//        sub.add(null);
//        sub.add("ACTIVE");
//        SubscriptionManager.subscriptions.add(sub);
//
//        //Search with empty string
//        ArrayList<ArrayList<String>> results = SubscriptionManager.searchByName("");
//        assertTrue(results.isEmpty(), "Empty input should have no results");
//
//        //Search with null
//        results = SubscriptionManager.searchByName(null);
//        assertTrue(results.isEmpty(), "Null input should have no results");
//    }
//}
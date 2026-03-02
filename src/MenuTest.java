import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/*
class Menu {

    @Test
    void addsTwoNumbers() {
        Calculator calc = new Calculator();
        assertEquals(5, calc.add(2, 3));
    }
}
 */
class Menu {
    @Test
    public void searchByNameFound() {
        Data.subscriptions.clear();

        ArrayList<String> sub = new ArrayList<>();
        sub.add("Spotify");
        sub.add("9.99");
        sub.add("Music");
        sub.add("Monthly");
        sub.add("March 5");
        sub.add(null);
        sub.add("ACTIVE");

        Data.subscriptions.add(sub);

        ArrayList<ArrayList<String>> results = Data.searchByName("Spotify");

        assertEquals(1, results.size(), "Searching for existing subscription name should return exactly 1 result.");
        assertEquals("Spotify", results.get(0).get(0), "Returned subscription name should be Spotify.");
    }
@Test
public void searchByCategoryNotFound() {
    Data.subscriptions.clear();

    ArrayList<String> sub = new ArrayList<>();
    sub.add("Netflix");
    sub.add("15.99");
    sub.add("Entertainment");
    sub.add("Monthly");
    sub.add("March 3");
    sub.add(null);
    sub.add("ACTIVE");

    Data.subscriptions.add(sub);

    ArrayList<ArrayList<String>> results = Data.searchByCategory("Fitness");

    assertTrue(results.isEmpty(), "Searching for non-existent category should return empty list.");
}
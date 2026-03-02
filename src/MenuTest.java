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
    Data.subscriptions.add(sub);

    //Do a search
    ArrayList<ArrayList<String>> results = Data.searchByName("Spotify");
    //Give the result size
    assertEquals(1, results.size(), "Should find 1 subscription named Spotify");
    //Give the name of the first result
    ArrayList<String> numOneResult = results.get(0);
    String name = numOneResult.get(0);

    assertEquals("Spotify", name);
}
@Test
void searchByNameNotFound() {
    // Clear any existing subs
    Data.subscriptions.clear();
    //Do a search
    ArrayList<ArrayList<String>> results = Data.searchByName("Netflix");
    //Show no results found
    assertTrue(results.size() == 0, "No subscriptions should match Netflix");
}


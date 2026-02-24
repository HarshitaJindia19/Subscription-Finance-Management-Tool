import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Data {
    public static ArrayList<ArrayList<String>> subscriptions = new ArrayList<>();
    public static HashMap<String, Double> categoryBudgets = new HashMap<>();
    public static HashSet<String> categories = new HashSet<>();
    public static final String[] STATUS_OPTIONS = {"ACTIVE", "PAUSED", "CANCELLED"};

    // Create function to add new subscription
    public static void addSubscription(String name, String cost, String category, String renewal, String start, String end, String status) {
    /*
    - Create a new array list to store new subscription
    - Add data to the array list
    - Add that list to the subscriptions arraylist
    - Add new category to categories - no repeated categories
     */

        ArrayList<String> newSub = new ArrayList<>();
        newSub.add(name);
        newSub.add(cost);
        newSub.add(category);
        newSub.add(renewal);
        newSub.add(start);
        newSub.add(end);
        newSub.add(status);

        subscriptions.add(newSub);
        categories.add(category); //adds category to set - no duplicates

    }

    //Store budget for each category
    public static void setCategoryBudget(String category, double limit) {
        //Store in HashMap -> Key = Category Name, Value = Budget Limit
    }

    //Calculates Monthly spend of the user
    public static double calculateMonthlySpend() {
        return 0;
    }

    //Calculates yearly spend of the user
    public static double calculateYearlySpend() {
        return 0;
    }
}

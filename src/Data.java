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
        categoryBudgets.put(category.toLowerCase(), limit);
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

/** @author Ramsha
 * finds the index of subcription in outer ArrayList by ensuring the name matches
 * @param name the name of subscription to search for
 * @return index of subscription or -1 if index is invalid
 *
 */
public static int findingSubscriptionIndex(String name){
    for (int i=0;i<subscriptions.size();i++) {
        if (subscriptions.get(i).get(0).equalsIgnoreCase(name)){
            return i;
        }
    }
    return  -1;
}
public static void changeSubscriptionChoice(int subIndex, int choiceIndex, String newChoice) {
    subscriptions.get(subIndex).set(choiceIndex, newChoice);
}
}

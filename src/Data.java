import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Data {
    public static ArrayList<ArrayList<String>> subscriptions = new ArrayList<>();
    //list containing other lists - inner list shows subscription, 1 list for netflic, spotify etc.

    public static HashMap<String, Double> categoryBudgets = new HashMap<>();
    //stores budget, string is categfory name liek entertainment
    public static HashSet<String> categories = new HashSet<>();
    //unique categories
    public static String[] status = {"ACTIVE", "PAUSED", "CANCELLED"};
} //array for 3 options


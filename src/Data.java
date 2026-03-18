import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Data {

    public static final ArrayList<Subscription> subscriptions = new ArrayList<>();
    public static final HashMap<String, Double> categoryBudgets = new HashMap<>();
    public static final HashSet<String> categories = new HashSet<>();
    public static final HashSet<String> names = new HashSet<>();
    public static final String[] STATUS_OPTIONS = {"ACTIVE", "PAUSED", "CANCELLED"};



    /**
     * Checks if subscription is new, and adds it to the subscription ArrayList
     * Adds categories and names to their respective HashSets
     *
     * @author Harshita
     * @param name of subscription - e.g. Netflix
     * @param cost of subscription e.g. 56.99
     * @param category of subscription - e.g. Health
     * @param renewal of subscription - e.g. Monthly
     * @param startDate of subscription - e.g. 05/20/2027
     * @param endDate of subscription - e.g. 05/20/2027 or N/A
     * @param status of subscription - e.g. PAUSED
     * @return boolean - true if subscription was new and was stored into list, false otherwise
     */
    public static boolean storeNewSubscription(String name, double cost, String category, String renewal, LocalDate startDate, LocalDate endDate, String status) {
        String cleanName = formatTitleCase(name);
        String cleanCategory = formatTitleCase(category);
        String cleanRenewal = formatTitleCase(renewal);

        if (!subscriptionExists(cleanName)) {
            Subscription subscription = new Subscription(cleanName, cost, cleanCategory, cleanRenewal, startDate, endDate, status);
            subscriptions.add(subscription);
            names.add(cleanName);
            categories.add(cleanCategory); //no duplicates
            return true;
        } else {
            return false;
        }
    }

    /**
     * Formats category string to title case
     * Stores received budget/limit from Menu.java
     *
     * @author Harshita
     * @param category of subscription chosen by user
     * @param limit of the budget set by the user
     */
    public static void setCategoryBudget(String category, double limit) {
        // format to title case "netflix" -> "Netflix"
        String cleanCategory = formatTitleCase(category);

        categoryBudgets.put(cleanCategory, limit);
        categories.add(cleanCategory);
    }

    /**
     * Formats user input into title case for clean data storage
     * @author Harshita
     * @param input from user
     * @return formatted input
     */
    public static String formatTitleCase(String input) {
        if (input.isEmpty()) return "";
        return input.substring(0, 1).toUpperCase() +
                input.substring(1).toLowerCase();
    }

    /**
     * Validates if a String contains only characters and spaces (no special characters and numbers)
     *
     * @param input collected from Menu.java
     * @return True if input String contains only letters or spaces
     */
    public static boolean isOnlyText(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("^[a-zA-Z\\s]+$"); // https://stackoverflow.com/questions/24191040/checking-to-see-if-a-string-is-letters-spaces-only
    }

    /**
     * Gets the budget for the specified category if it already exists
     * @author Harshita
     * @param category selected by the user for their subscription
     * @return budget for the category, -1 if not found
     */
    public static double getCategoryBudget(String category) {
        return categoryBudgets.getOrDefault(formatTitleCase(category), -1.0);
    }

    /**
     * Lists all categories and budgets in a clean format
     *
     * @return all the budgets currently stored by category
     * @author Harshita
     */
    public static ArrayList<String> getAllBudgets() {
        ArrayList<String> budgetList = new ArrayList<>();
        for (String category : categoryBudgets.keySet()) {
            double limit = categoryBudgets.get(category);
            budgetList.add(category + ": $" + String.format("%.2f", limit));
        }
        return budgetList;
    }


    /**
     * Takes in all active subscriptions and totals their cost for monthly spend
     * @author Harshita
     * @return total monthly spend of user as double
     */
    public static double calculateMonthlySpend() {
        double total = 0.0;
        double numOfMonths = 12;

        for (Subscription sub : subscriptions) {

            if (sub.getStatus().equalsIgnoreCase("ACTIVE")) {
                double cost = sub.getCost();
                String renewal = sub.getRenewal();

                if (renewal.equalsIgnoreCase("MONTHLY")) {
                    total += cost;
                } else if (renewal.equalsIgnoreCase("YEARLY")) {
                    total += cost / numOfMonths; // convert yearly to monthly by dividing costs amongst months
                }
            }
        }

        return total;
    }

    /**
     * Takes in all active subscriptions and totals their cost for yearly spend
     *
     * @return total monthly spend of user as double
     * @author Harshita
     */
    public static double calculateYearlySpend() {

        double total = 0.0;
        double numOfMonths = 12;

        for (Subscription sub : subscriptions) {

            if (sub.getStatus().equalsIgnoreCase("ACTIVE")) {
                double cost = sub.getCost();
                String renewal = sub.getRenewal();

                if (renewal.equalsIgnoreCase("MONTHLY")) {
                    total += cost * numOfMonths; // convert monthly to yearly
                } else if (renewal.equalsIgnoreCase("YEARLY")) {
                    total += cost; // convert yearly to monthly by dividing costs amongst months
                }
            }
        }

        return total;
    }

    /**
     * Finds the index of subscription in outer ArrayList by ensuring the name matches.
     * @author Ramsha
     * @param name the name of subscription to search for
     * @return index of subscription in outer ArrayList or -1 if not found
     */
    public static int findingSubscriptionIndex(String name) {
        //loop through every subscription in list
        for (int i = 0; i < subscriptions.size(); i++) {
            //get(i) - subscription row
            //get(0) - name at index 0
            if (subscriptions.get(i).getName().equalsIgnoreCase(name)) {
                return i;//found - return its position
            }
        }
        return -1;//not found
    }
    /**
     * Finds subscriptions with a start date within the next 7 days.
     * @author Ramsha
     * @return ArrayList of subscriptions renewing within 7 days
     */
    public static ArrayList<Subscription> getUpcomingBills() {
        ArrayList<Subscription> results = new ArrayList<>();

        //get today's date
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);

        for (Subscription sub : subscriptions) {

            LocalDate startDateStr = sub.getStartDate(); //start date at index 4
            String renewal = sub.getRenewal();

            try {
                //parse MM/DD/YYYY format
                LocalDate startDate = LocalDate.parse(startDateStr, formatter);
                LocalDate nextRenewal;

                //check if date is within next 7 days
                if (renewal.equalsIgnoreCase("YEARLY")) {
                    nextRenewal = startDate.withYear(today.getYear());
                    if (nextRenewal.isBefore(today)) {
                        nextRenewal = nextRenewal.plusYears(1);

                    }
                } else if (renewal.equalsIgnoreCase("MONTHLY")) {
                    nextRenewal = startDate.withMonth(today.getMonthValue()).withYear(today.getYear());
                    if (nextRenewal.isBefore(today)) {
                        nextRenewal = nextRenewal.plusMonths(1);
                    }
                } else {
                    continue;
                }

                // Check if this calculated renewal date is within the next 7 days
                if (!nextRenewal.isBefore(today) && !nextRenewal.isAfter(sevenDaysLater)) {
                    results.add(sub);
                }
            } catch (Exception e) {
                //skip if date format is invalid
            }
        }
        return results;
    }

    /**
     * Search Engine to find all the subscriptions under the given name
     * @param name the name of subscription to search for
     * @return ArrayList of ArrayLists, where inner list shows a
     * subscription that matches the name
     */
    public static ArrayList<Subscription> searchByName(String name) {
        ArrayList<Subscription> results = new ArrayList<>(); // Initialize results list

        if (name == null || name.trim().isEmpty()) {// If input is null or isEmpty, return empty list
            return results;
        }

        String keyword = name.trim().toLowerCase(); // Convert search to lowercase

        for (Subscription sub : subscriptions) { // Loop through all subs
            String subName = sub.getName(); //Get the subs name using constant index
            if (subName != null && subName.toLowerCase().contains(keyword)) {// Check if name contains keyword
                results.add(sub);// Add matching subs to results
            }
        }
        return results; // Return list of matching subs
    }

    /**
     * Search Engine to find all the subscriptions under the given category
     *
     * @param category is the category of subscription to search for
     * @return ArrayList of ArrayLists, where each inner list shows a
     *subscription that matches the category
     */

    public static ArrayList<Subscription> searchByCategory(String category) {
        ArrayList<Subscription> results = new ArrayList<>();

        if (category == null || category.trim().isEmpty()) { // Return empty list if input is null
            return results;
        }

        String keyword = category.trim().toLowerCase(); // Convert search to lowercase

        for (Subscription sub : subscriptions) { // Loop through all subs

            if (sub.getCategory().toLowerCase().contains(keyword)) {// Check if category contains keyword
                results.add(sub); // Add matching subs to results
            }

        }
        return results; // Return list of matching subs
    }

    /**
     * Updates a specific field of a subscription using .set(index,newValue).
     *
     * @param subIndex    index of subscription in outer ArrayList
     * @param choiceIndex index of field to update in inner ArrayList
     * @param newChoice   new value to set
     * @author Ramsha
     */

    public static void changeSubscriptionChoice(int subIndex, int choiceIndex, String newChoice) {
        //get(subIndex) - finds correct subscription row
        //.set(choiceIndex, newChoice) - replaces value at field
        subscriptions.get(subIndex).set(choiceIndex, newChoice);
    }


    //--------------------------------Valid Checks----------------------------------------//

    public static boolean subscriptionExists(String name) {
        return names.contains(formatTitleCase(name));
    }

    /**
     * Checks if status string is one of the valid options(ACTIVE, PAUSED, CANCELLED stored in STATUS_OPTIONS)
     * @author Ramsha
     * @param status status string to ensure is right
     * @return true if valid, false if invalid
     */

    public static boolean isValidStatus(String status) {
        for (String validString : STATUS_OPTIONS) {
            if (validString.equals(status)) {
                return true;//matched
            }
        }
        return false;//nothing to match
    }

    /**
     * Checks if date (start/end) are valid
     * @author Harshita
     * @param date (end/start) of the subscription ( user input )
     * @return boolean true if valid, false otherwise
     */
    public static boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu").withResolverStyle(ResolverStyle.STRICT);
            LocalDate parsedDate = LocalDate.parse(date, formatter);

            // Check against today's date
            LocalDate today = LocalDate.now();
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks if date is in the future
     *
     * @param date (end/start) of the subscription ( user input )
     * @return boolean true if Date is in the future, false otherwise
     */
    public static boolean isFutureDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu");
        LocalDate parsedDate = LocalDate.parse(date, formatter);
        return parsedDate.isAfter(LocalDate.now());
    }

    /**
     * Checks if cost is valid
     * @author Harshita
     * @param cost for new subscription
     * @return boolean true if cost is double, false otherwise
     */
    public static boolean isValidCost(String cost) {
        try {
            double value = Double.parseDouble(cost);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

import java.util.Scanner;
import java.util.ArrayList;

/**
 * Menu.java - handles the main menu loop and user interface
 * @author Ramsha Oad
 * @date Feb 24 2026
 * @tutorial T01
 *
 */
public class Menu {
    private static Scanner input = new Scanner(System.in);

    public static final ArrayList<String> options= new ArrayList<>();
    static{
        options.add("Exit");
        options.add("Add subscription");
        options.add("Update subscription");
        options.add("View all subscriptions");
        options.add("View active subscriptions");
        options.add("Total monthly spend");
        options.add("Total annual spend");
        options.add("Upcoming bills");
        options.add("Paused subscriptions");
        options.add("Budget % by category");
        options.add("High-expense bills");
        options.add("Budget alert");
        options.add("Search subscriptions");
        options.add("Add category budget");
    }
    public static void runMenu() {
        boolean active=true;
            while (active) {
                System.out.println("====SUBSCRIPTION MANAGER===");
                for (int i=0;i<options.size();i++) {
                    System.out.println(i + ". " + options.get(i));
                }
                System.out.println("Please select an option: ");
                String choice= input.nextLine().trim();
                switch (choice) {
                case "1":
                    // harshitas work
                    break;
                case "2":
                    updateProcess();
                    break;
                case "3":
                    // mehrins work
                    break;
                case "4":
                    // mehrins work
                    break;
                case "5":
                    // harshitas work
                    break;
                case "6":
                    // harshitas work
                    break;
                case "7":
                    // mehrins work
                    break;
                case "8":
                    // mehrins work
                    break;
                case "9":
                    // mehrins work
                    break;
                case "10":
                    // mehrins work
                    break;
                case "11":
                    // mehrins work
                    break;
                case "12":
                    // mehrins work
                    break;
                case "13":
                    // harshitas work
                    break;
                case "0":
                    active = false;
                    System.out.println("Exited.");
                    break;
                default:
                    System.out.println("Invalid choice, please enter a number from the menu.");
            }
        }
        input.close();
    }

    public static void updateProcess() {
        System.out.println("\nEnter name of subscription to update: ");
        String name = input.nextLine().trim();

        int index = Data.findingSubscriptionIndex(name);
        if (index == -1) {
            System.out.println("ERROR: '" + name + "' subscription not found.");
            return;
        }
        System.out.println("\nWhat do you want to update?");
        System.out.println("1. Name");
        System.out.println("2. Cost");
        System.out.println("3. Category");
        System.out.println("4. Billing cycle");
        System.out.println("5. Start date");
        System.out.println("6. End date");
        System.out.println("7. Status of subscription (ACTIVE/PAUSED/CANCELLED)");
        System.out.print("YOUR DESIRED CHOICE: ");
        String fieldChoice = input.nextLine().trim();

        int choiceIndex = getIndexChoice(fieldChoice);
        if (choiceIndex == -1) {
            System.out.println("ERROR: invalid choice selection.");
            return;
        }
        System.out.println("Please enter new choice: ");
        String newChoice = input.nextLine().trim();

        if (choiceIndex == 6) {
            if (!Data.validStatus(newChoice.toUpperCase())) {
                System.out.println("ERROR: status must be ACTIVE/PAUSED/CANCELLED");
                return;
            }
            newChoice = newChoice.toUpperCase();
        }
        Data.changeSubscriptionChoice(index, choiceIndex, newChoice);
        System.out.println("Update done!");
    }

    /**
     * users desired menu choice --> ArrayList index
     *
     * @param choice users input which is 1-7
     * @return the index 0-6.-1 is invalid
     */

    public static int getIndexChoice(String choice) {

        switch (choice) {
            case "1":
                return 0;
            case "2":
                return 1;
            case "3":
                return 2;
            case "4":
                return 3;
            case "5":
                return 4;
            case "6":
                return 5;
            case "7":
                return 6;

            default:
                return -1;

        }
    }}

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
    //Arraylist stores all menu options - index 0=exit, index 1=add subscription, etc.
    public static final ArrayList<String> options= new ArrayList<>();

    //static runs once when class is first loaded - used here because options is a static variable
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

    /**
     * main loop for program, keeps running until user exits
     * shows menu, reads user input, calls the corresponding method
     */
    public static void runMenu() {
        boolean active=true; //loop keeps going
            while (active) {
                System.out.println("====SUBSCRIPTION MANAGER===");

                //loop through menu options ArrayList and print each option with number in front
                for (int i=0;i<options.size();i++) {
                    System.out.println(i + ". " + options.get(i));
                }
                System.out.println("Please select an option: ");
                String choice= input.nextLine().trim();
                switch (choice) {  //switch to guide user to correct action based on choice
                case "1":
                    // harshitas work
                    break;
                case "2":
                    updateProcess();//update subscription
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
                    active = false;//now active is false to exit while loop
                    System.out.println("Exited.");
                    break;
                default:
                    System.out.println("Invalid choice, please enter a number from the menu.");
            }
        }
        input.close();//close scanner when program ends
    }

    /**
     * handles update subscription - asks user for subscription name--> finds in Data.java-->
     * then asks which field to update and what new value is
     *
     */

    public static void updateProcess() {
        //asking user which subscription to update
        System.out.println("\nEnter name of subscription to update: ");
        String name = input.nextLine().trim();

        //search for subscription by name or return -1 if not found
        int index = Data.findingSubscriptionIndex(name);
        //if returned -1, subscription DNE
        if (index == -1) {
            System.out.println("ERROR: '" + name + "' subscription not found.");
            return;
        }
        System.out.println("\nWhat field do you want to update?");
        System.out.println("1. Name");
        System.out.println("2. Cost");
        System.out.println("3. Category");
        System.out.println("4. Billing cycle");
        System.out.println("5. Start date");
        System.out.println("6. End date");
        System.out.println("7. Status of subscription (ACTIVE/PAUSED/CANCELLED)");
        System.out.print("YOUR DESIRED CHOICE: ");
        String fieldChoice = input.nextLine().trim();

        //convert users choice(1-7) to ArrayList index(0-6)
        int choiceIndex = getIndexChoice(fieldChoice);

        //if -1, that means choice is invalid, stop
        if (choiceIndex == -1) {
            System.out.println("ERROR: invalid choice selection.");
            return;
        }
        System.out.println("Please enter new choice: ");
        String newChoice = input.nextLine().trim();

        if (choiceIndex == 6) {//if (6) updating status, ensure it is one of allowed values(ACTIVE,PAUSED,CANCELLED)
            if (!Data.validStatus(newChoice.toUpperCase())) {
                System.out.println("ERROR: status must be ACTIVE/PAUSED/CANCELLED");
                return;
            }//change to uppercase so it matches STATUS_OPTIONS
            newChoice = newChoice.toUpperCase();
        }
        //call Data to update field using .set(index,newValue)
        Data.changeSubscriptionChoice(index, choiceIndex, newChoice);
        System.out.println("Update complete.");
    }

    /**
     * users desired menu choice(1-7) to ArrayList index(0-6)
     * @param choice users input which is "1"-"7"
     * @return the corresponding ArrayList index 0-6.-1 is invalid
     */

    public static int getIndexChoice(String choice) {
        //each case (e.g. "1") matches to data index
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

import java.util.Scanner;

/**
 * Menu.java - handles the main menu loop and user interface
 * @author Ramsha Oad
 * @date Feb 24 2026
 * @tutorial T01
 *
 */
public class Menu {
    private static Scanner input = new Scanner(System.in);
    public static void runMenu() {
        boolean running = true;
        while (running) {
            System.out.println("====SUBSCRIPTION MANAGER===");
            System.out.println("1. Add Subscription");
            System.out.println("2. Update Subscription");
            System.out.println("3. View all Subscriptions");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");
            String choice= input.nextLine().trim();

            switch (choice) {
                case "1":
                    //harshitas work
                    break;
                case "2":
                    updateProcess();
                    break;
                case "3":
                    //mehrins work
                    break;
                case "4":
                    running=false;
                    System.out.println("Exited.");
                    break;
                default:
                    System.out.println("Invalid choice, please enter numbers 1-4.");

            }
        }
        input.close();
    }
public static void updateProcess() {
    System.out.println("\nEnter name of subscription to update: ");
    String name= input.nextLine().trim();

    int index = Data.subscriptionIndex(name);
    if (index==-1) {
        System.out.println("ERROR: '"+name + "subscription not found.");
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
    System.out.print("SELECT YOUR DESIRED FIELD: ");
}
}
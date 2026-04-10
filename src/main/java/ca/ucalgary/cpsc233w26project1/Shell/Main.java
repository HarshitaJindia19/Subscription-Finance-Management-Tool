package ca.ucalgary.cpsc233w26project1.Shell;

import ca.ucalgary.cpsc233w26project1.FileHandler;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;

import java.io.File;

/**
 * This class initializes the SubscriptionManager and optionally loads
 * data from a CSV file if a filename is provided as a command line argument
 * After the setup it starts the menu interface
 */

public class Main {

/**
* Main method that starts the program
* @param args command-line arguments (optional filename for loading the data)
*/

    public static void main(String[] args) {
        //Create the main SubscriptionManager object to store all data
        SubscriptionManager manager = new SubscriptionManager();

        //Check if user provided a filename as a command-line argument
        if (args.length > 0) {
            //Get the filename from arguments
            String filename = args[0];

            //Create File object to check if file the exists
            File file = new File(filename);
            //If the file exists then load data into manager
            if (file.exists()) {
                System.out.println("Loading data from file: " + filename);
                FileHandler.loadFromCSV(manager, filename);
            } else {
                //If the file does not exist then tell the user
                System.out.println("File not found: " + filename);
            }
        } else {
            //If no file is provided then start program with the empty data
            System.out.println("No file provided. Starting with empty data.");
        }
        //Start the menu system and pass the manager to it
        Menu.runMenu(manager);
    }
}
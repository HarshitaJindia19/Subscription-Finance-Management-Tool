package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import ca.ucalgary.cpsc233w26project1.util.SubReader;
import ca.ucalgary.cpsc233w26project1.util.SubWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ca.ucalgary.cpsc233w26project1.Subscription;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;

/**
 * Controls the main controller of the subscription manager's main window
 */
public class SubscriptionMainController {
    @FXML private Label lblTotalSubscriptions;
    @FXML private Label lblMonthlySpend;
    @FXML private Label lblAnnualSpend;
    @FXML private Label lblUpcomingBills;
    @FXML private Label lblMessage;

    //main table and columns
    @FXML private TableView<Subscription> mainTable;
    @FXML private TableColumn<Subscription, String> nameCol;
    @FXML private TableColumn<Subscription, String> costCol;
    @FXML private TableColumn<Subscription, String> categoryCol;
    @FXML private TableColumn<Subscription, String> renewalCol;
    @FXML private TableColumn<Subscription, String> startDateCol;
    @FXML private TableColumn<Subscription, String> endDateCol;
    @FXML private TableColumn<Subscription, String> statusCol;
    @FXML private TableColumn<Subscription, Void> actionsCol;

    private SubscriptionManager manager;

    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
    }

    /**
     * Initializes program with command line arguments
     * Loads data from file if filename is given
     * @param args command line arguments
     */
    public void initData(List<String> args) {

        if (manager == null) {
            manager = new SubscriptionManager();
        }

            String filename = args.getFirst();
            File file = new File(filename);

            if (file.exists()) {
                SubReader.loadFromFile(manager, file);
                lblMessage.setText("✅ Successfully loaded: " + filename);
            } else {
                lblMessage.setText("Error: File not found: " + filename);
            }

        // Load table with data from command line csv
        loadTable(manager.getAllSubscriptions());

        // Update info to add to status cards
        refreshTotals();
    }

    /**
     * loadTable - helper method to load a list of subscriptions into the main table
     * @param subs list of subscriptions to display
     */
    private void loadTable(java.util.ArrayList<Subscription> subs) {
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        categoryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));

        renewalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRenewal()));

        costCol.setCellValueFactory(data ->new javafx.beans.property.SimpleStringProperty("$" + String.format("%.2f", data.getValue().getCost())));

        startDateCol.setCellValueFactory(data ->new javafx.beans.property.SimpleStringProperty(data.getValue().getStartDate().format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"))));

        endDateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEndDate() != null ? data.getValue().getEndDate().format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy")) : "N/A"));

        statusCol.setCellValueFactory(data ->new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus().toString()));

        mainTable.getItems().clear();
        mainTable.getItems().addAll(subs);

        setupEditColumn();
    }


    /**
     * Sets up the EDIT and DELETE buttons in each row of the main table
     * Clicking EDIT opens the UpdateSubscription popup for that row
     * Clicking DELETE removes the subscription after confirmation
     */
    private void setupEditColumn() {
        Callback<TableColumn<Subscription, Void>, TableCell<Subscription, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Subscription, Void> call(TableColumn<Subscription, Void> param) {
                return new TableCell<>() {
                    // make the EDIT button
                    private final Button editButton = new Button("Edit");
                    // make the DELETE button
                    private final Button deleteButton = new Button("X");
                    private final HBox buttons = new HBox(5, editButton, deleteButton);

                    {
                        // style delete button red
                        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                        // when EDIT is clicked, open UpdateSubscription window
                        editButton.setOnAction(event -> {
                            Subscription sub = getTableView().getItems().get(getIndex());
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateSubscription.fxml"));
                                Stage stage = new Stage();
                                stage.setScene(new Scene(loader.load()));
                                stage.setTitle("Update Subscription");
                                UpdateSubscriptionController controller = loader.getController();
                                controller.setManager(manager);
                                controller.prefillFields(sub);
                                stage.show();

                                // When pop-up closes
                                stage.setOnHidden(e -> {
                                    loadTable(manager.getAllSubscriptions()); // load the table with new information immediately
                                    refreshTotals(); // refreshes M/Y totals
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        // when DELETE is clicked, show confirmation then remove
                        deleteButton.setOnAction(event -> {
                            Subscription sub = getTableView().getItems().get(getIndex());
                            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                            confirm.setTitle("Delete Subscription");
                            confirm.setHeaderText(null);
                            confirm.setContentText("Are you sure you want to delete '" + sub.getName() + "'?");
                            confirm.showAndWait().ifPresent(response ->{
                                if (response == javafx.scene.control.ButtonType.OK) {
                                    int index = manager.findingSubscriptionIndex(sub.getName());
                                    manager.removeSubscription(index);
                                    mainTable.getItems().remove(sub);

                                    refreshTotals(); // updates M/Y totals when sub deleted
                                }
                            });
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {//if cell is empty set graphic to null
                            setGraphic(null);
                        } else {
                            setGraphic(buttons);//if cell has a sub, set graphic to buttons (Hbox that has edit and X button)
                        }
                    }
                };
            }
        };
        actionsCol.setCellFactory(cellFactory);
    }


    //-------------------------------------VIEW METHODS----------------------------------------------------

    /**
     * Loads all current subscriptions into the main table view
     * @param actionEvent the event when button is clicked
     */
    @FXML
    private void showAllSubscriptions(ActionEvent actionEvent) {
        loadTable(manager.getAllSubscriptions());
    }

    /**
     * Loads all Active subscriptions into the main table view
     * @param actionEvent the event when button is clicked
     */
    @FXML
    private void showActiveSubscriptions(ActionEvent actionEvent) {
        loadTable(manager.getActiveSubscription());
    }

    /**
     * Loads all Paused subscriptions into the main table view
     * @param actionEvent the event when button is clicked
     */
    @FXML
    private void showPausedSubscriptions(ActionEvent actionEvent) {
        loadTable(manager.getPausedSubscription());
    }

    /**
     * Loads all Canceled subscriptions into the main table view
     * @param actionEvent the event when button is clicked
     */
    @FXML
    private void showCancelledSubscriptions(ActionEvent actionEvent) {
        loadTable(manager.getCancelledSubscription());
    }

    /**
     * Displays upcoming bills window
     * Initializes the UpcomingBillsController with the current manager and triggeres
     * the loading of bill data.
     * @param actionEvent action event when button is clicked
     */

    @FXML
    private void showUpcomingBills(ActionEvent actionEvent) {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("UpcomingBills.fxml"));
            Stage stage =new Stage();
            stage.setScene(new Scene (loader.load()));
            stage.setTitle("Upcoming Bills");
            UpcomingBillsController controller=loader.getController();
            controller.setManager(manager);
            controller.loadUpcomingBills();
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------FINANCE METHODS---------------------------------------------

    /**
     * Opens the Budget Alerts window in a new stage
     * Loads the FXML layout, initializes the controller with the current
     * SubscriptionManager instance, and displays the window to the user
     * @param actionEvent the event triggered by the user (ex. button click)
     */
    @FXML
    private void showBudgetAlerts(ActionEvent actionEvent) {
        try {
            //Load the FXML file that defines the Budget Alerts UI
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BudgetAlerts.fxml"));
            //Create a new window (Stage) for the budget alerts screen
            Stage stage = new Stage();
            //Set the scene using the loaded FXML layout
            stage.setScene(new Scene(loader.load()));
            //Set the window title
            stage.setTitle("Budget Alerts");
            //Retrieve the controller associated with the FXML file
            BudgetAlertsController controller = loader.getController();
            //Pass the current manager instance so the alerts screen can access data
            controller.setManager(manager);
            //Display the new window
            stage.show();
        } catch (IOException e) {
            //Print error details if FXML loading fails
            e.printStackTrace();
        }
    }
    /**
     * Opens the Budget Percentage by Category window in a new stage
     * Loads the BudgetPercentByCategory.fxml layout, initializes its controller
     * with the current SubscriptionManager instance, and displays the window
     * @param actionEvent the event triggered by the user (ex. button click)
     */
    @FXML
    private void showBudgetByCategory(ActionEvent actionEvent) {
        try {
            //Load the FXML file that defines the Budget By Category UI
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("BudgetPercentByCategory.fxml")
            );
            //Create a new window (Stage)
            Stage stage = new Stage();
            //Set the scene using the loaded FXML layout
            stage.setScene(new Scene(loader.load()));
            //Set the window title
            stage.setTitle("Budget % by Category");
            //Retrieve the controller associated with the FXML file
            BudgetPercentByCategoryController controller = loader.getController();
            //Pass the current manager instance so the screen can access data
            controller.setManager(manager);
            //Display the new window
            stage.show();
        } catch (IOException e) {
            //Print error details if FXML loading fails
            e.printStackTrace();
        }
    }

    /**
     * Opens a high expense window.
     * Initializes the HighExpenseBillsController with current manager instance to
     * filter and show subs that exceed a certain threshold.
     * @param actionEvent action event when button is clicked
     */

    @FXML
    private void showHighExpenseBills(ActionEvent actionEvent) {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("HighExpenseBills.fxml"));
            Stage stage=new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("High-Expense Bills");
            HighExpenseBillsController controller =loader.getController();
            controller.setManager(manager);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------OTHER METHODS------------------------------------

    /**
     * Opens file chooser, for user to select a file to save current data
     * Saves subscriptions and budgets to preferred file and location
     * @param actionEvent the event when button is clicked
     */
    @FXML
    private void saveToFile(ActionEvent actionEvent) {

        if (manager.getAllSubscriptions().isEmpty()) {
            lblMessage.setText("Error: No subscriptions to save! Please add subscription data to save.");
            return;
        }

        // Let user choose location and file to save to
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Subscription Data");
        fileChooser.setInitialFileName("subscriptions.csv");


        // CSV file filter (.txt) *Code was taken from https://gemini.google.com/app
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(csvFilter);

        File file = fileChooser.showSaveDialog(null);

        // Only save if user selected a file
        if (file != null) {
            SubWriter.writeToFile(manager, file);
            lblMessage.setText("✅Saved to : " + file.getName());
        }
    }

    /**
     * Opens file chooser, for the user to select a file to load data from
     * Reads the file and calls loads subscription and budget data onto controller
     * @param actionEvent the event when button is clicked
     */
    @FXML
    private void loadFromFile(ActionEvent actionEvent) {

        // Open file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Subscription Data");

        // CSV filter
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(csvFilter);

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            // confirm before overwriting existing data
            if (!manager.getAllSubscriptions().isEmpty()) {
                //Open Alert CONFIRMATION panel
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Load File");
                confirm.setHeaderText("Loading will replace your current data.");
                confirm.setContentText("Are you sure you want to load from '" + file.getName() + "'?");

                // If user agrees, add the new data and update
                confirm.showAndWait().ifPresent(response -> {
                   if (response == javafx.scene.control.ButtonType.OK) {
                       SubReader.loadFromFile(manager, file);
                       loadTable(manager.getAllSubscriptions());
                       refreshTotals();
                       lblMessage.setText("✅ Loaded from: " + file.getName());
                   }
                });
            } else { // If no subs added so far, load from file without Alert
                SubReader.loadFromFile(manager, file);
                loadTable(manager.getAllSubscriptions());
                refreshTotals();
                lblMessage.setText("✅ Loaded from " + file.getName());
            }
        }
    }

    /**
     * Opens a new stage to add a subscription
     * Displays error messages, and only adds subscription when all inputs are validated
     * @param actionEvent the event when button is clicked
     */
    @FXML
    private void openAddSubscriptionStage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddSubscription.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Add Subscription");
            AddSubscriptionController controller =loader.getController();
            controller.setManager(manager);
            stage.show();

            // When pop-up closes
            stage.setOnHidden(e -> {
                loadTable(manager.getAllSubscriptions()); // adds sub to table immediately
                refreshTotals(); // refreshed M/Y totals
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Opens the Add Budget window in a new stage
     * Loads the AddBudget.fxml layout, initializes its controller with the
     * current SubscriptionManager instance, and displays the window
     * @param actionEvent the event triggered by the user (ex. button click)
     */
    @FXML
    private void openAddBudgetStage(ActionEvent actionEvent) {
        try {
            //Load the FXML file that defines the Add Budget UI
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBudget.fxml"));
            //create a new stage
            Stage stage = new Stage();
            //create a new scene
            stage.setScene(new Scene(loader.load()));
            //set title
            stage.setTitle("Add Budget");
            //Retrieve the controller associated with the FXML file
            AddBudgetController controller = loader.getController();
            //Pass the current manager instance so the the screen can access data
            controller.setManager(manager);
            //Display new window
            stage.show();
        } catch (IOException e) {//Show error message
            e.printStackTrace();
        }
    }

    /**
     * Access program details and authors
     * @param actionEvent the event when button is clicked
     */
    @FXML
    private void about(ActionEvent actionEvent) {
        Alert about = new Alert(Alert.AlertType.INFORMATION); //Set alert to show INFORMATION pop-up

        // Add context in About section
        about.setTitle("About");
        about.setHeaderText("Learn more about our Program:");
        about.setContentText("About: Track, manage, monitor Subscriptions like a pro 😎!\nAuthor: Harshita Jindia, Ramsha Oad, Mehrin Chowdhury\nContact: harshita.jindia1@ucalgary.ca, ramsha.oad@ucalgary.ca, mehrin.chowdhury@ucalgary.ca\nProgram: CPSC233W26 T01 V.1");

        about.showAndWait();
    }

    /**
     * Helper function to immediately refresh the main table view with updated status cards
     * Called when subscription data is changed
     */
    private void refreshTotals() {

        int numOfSubs = manager.getAllSubscriptions().size();
        int numOfUpcoming = manager.getUpcomingBills().size();

        // Calculate the values
        double monthlyTotal = manager.calculateMonthlySpend();
        double yearlyTotal = manager.calculateYearlySpend();

        // Format as currency to 2 decimal places and display
        lblMonthlySpend.setText(String.format("$%.2f", monthlyTotal));
        lblAnnualSpend.setText(String.format("$%.2f", yearlyTotal));

        // Add total subscription value to status card
        lblTotalSubscriptions.setText(String.valueOf(numOfSubs));

        // Add total upcoming bills value to status card
        lblUpcomingBills.setText(String.valueOf(numOfUpcoming));

    }

    /**
     * Opens the Search Subscriptions window in a new stage
     * Loads the Search.fxml layout, initializes its controller with the
     * current SubscriptionManager instance, and displays the window
     * @param actionEvent the event triggered by the user (ex. button click)
     */
    @FXML
    private void openSearchStage(ActionEvent actionEvent) {
        try {
            //Load the FXML file that defines the search UI
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Search.fxml"));
            //Create new stage
            Stage stage = new Stage();
            //Create new scene
            stage.setScene(new Scene(loader.load()));
            //Title window
            stage.setTitle("Search Subscriptions");
            //Retrieve the controller associated with the FXML file
            SearchController controller = loader.getController();
            //Pass the current manager instance so the alerts screen can access data
            controller.setManager(manager);
            //Display new window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Print error message
        }
    }
}

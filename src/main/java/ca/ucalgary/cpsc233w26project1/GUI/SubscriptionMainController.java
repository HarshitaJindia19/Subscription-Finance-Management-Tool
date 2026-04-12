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
import javafx.scene.control.cell.PropertyValueFactory;
import ca.ucalgary.cpsc233w26project1.Subscription;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;

public class SubscriptionMainController {
    @FXML private Button btnAllSubscriptions;
    @FXML private Button btnActive;
    @FXML private Button btnPaused;
    @FXML private Button btnCancelled;
    @FXML private Button btnUpcomingBills;
    @FXML private Button btnMonthlySpend;
    @FXML private Button btnAnnualSpend;
    @FXML private Button btnBudgetAlerts;
    @FXML private Button btnBudgetByCategory;
    @FXML private Button btnHighExpense;
    @FXML private Button btnSearch;
    @FXML private Button btnSaveToFile;
    @FXML private Button btnLoadFromFile;
    @FXML private TextField searchField;
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

    private SubscriptionManager manager = new SubscriptionManager();

    /**
     * Initializes program with command line arguments
     * Loads data from file if filename is given
     * @param args command line arguments
     */
    public void initData(List<String> args) {

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
     * loadTable - Helper method to load a list of subscriptions into the main table
     * @param subs list of subscriptions to display
     */
    private void loadTable(java.util.ArrayList<Subscription> subs) {
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        categoryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));

        renewalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRenewal()));

        costCol.setCellValueFactory(data ->new javafx.beans.property.SimpleStringProperty("$" + String.format("%.2f", data.getValue().getCost())));

        startDateCol.setCellValueFactory(data ->new javafx.beans.property.SimpleStringProperty(data.getValue().getStartDate().format(java.time.format.DateTimeFormatter.ofPattern("MM/DD/YYYY"))));

        endDateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEndDate() != null ? data.getValue().getEndDate().format(java.time.format.DateTimeFormatter.ofPattern("MM/DD/YYYY")) : "N/A"));

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
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttons);
                        }
                    }
                };
            }
        };
        actionsCol.setCellFactory(cellFactory);
    }


    //-------------------------------------VIEW METHODS----------------------------------------------------

    @FXML
    private void showAllSubscriptions(ActionEvent actionEvent) {
        loadTable(manager.getAllSubscriptions());
    }

    @FXML
    private void showActiveSubscriptions(ActionEvent actionEvent) {
        loadTable(manager.getActiveSubscription());
    }

    @FXML
    private void showPausedSubscriptions(ActionEvent actionEvent) {
        loadTable(manager.getPausedSubscription());
    }

    @FXML
    private void showCancelledSubscriptions(ActionEvent actionEvent) {
        loadTable(manager.getCancelledSubscription());
    }

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

    @FXML
    private void showMonthlySpend(ActionEvent actionEvent) {
        manager.calculateMonthlySpend();
    }

    @FXML
    private void showAnnualSpend(ActionEvent actionEvent) {
        manager.calculateYearlySpend();
    }

    @FXML
    private void showBudgetAlerts(ActionEvent actionEvent) {
    }

    @FXML
    private void showBudgetByCategory(ActionEvent actionEvent) {
    }

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

    @FXML
    private void openAddSubscriptionStage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("AddSubscription.fxml"));
            Stage stage=new Stage();
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

    @FXML
    private void openAddBudgetStage(ActionEvent actionEvent) {
    }

    @FXML
    private void about(ActionEvent actionEvent) {
        Alert about = new Alert(Alert.AlertType.INFORMATION); //Set alert to show INFORMATION pop-up

        // Add context in About section
        about.setTitle("About User");
        about.setHeaderText("Welcome to your Profile:");
        about.setContentText("About: Track Subscriptions like a pro 😎!\nAuthor: Harshita Jindia, Ramsha Oad, Mehrin Chowdhury\nTutorial: T01");

        about.showAndWait();
    }

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
}

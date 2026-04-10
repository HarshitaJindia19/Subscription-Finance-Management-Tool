package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.Subscription;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * HighExpenseBillsController.java - shows active subscriptions above a cost threshold
 * @author Ramsha Oad
 * @date April 8 2026
 * @tutorial T01
 */
public class HighExpenseBillsController {

    //connects to FXML elements
    @FXML private TextField thresholdField;
    @FXML private Label statusLabel;
    @FXML private TableView<Subscription> resultsTable;

    //connects to each column
    @FXML private TableColumn<Subscription, String> nameCol;
    @FXML private TableColumn<Subscription, String> costCol;
    @FXML private TableColumn<Subscription, String> categoryCol;
    @FXML private TableColumn<Subscription, String> renewalCol;
    @FXML private TableColumn<Subscription, String> statusCol;

    private SubscriptionManager manager;


    /**
     * Called by main controller to pass in the shared manager
     * @param manager the shared SubscriptionManager
     */

    public void setManager(SubscriptionManager manager) {
        this.manager =manager;
    }

    /**
     * Called when user clicks Search button
     * Validates threshold input then displays matching subscriptions in table
     */
    @FXML
    private void searchHighExpense() {

        //get what user typed in threshold field
        String input = thresholdField.getText().trim();

        //check a valid positive number was entered
        if ( !SubscriptionManager.isValidCost(input)) {
            statusLabel.setText("Please enter a valid amount (e.g. 20.00)");
            statusLabel.setStyle("-fx-text-fill: rgba(255,0,0,0.65);");
            resultsTable.setVisible(false);
            return;
        }

        //convert to double
        double threshold = Double.parseDouble(input);

        //set up columns to pull data from Subscription object
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        renewalCol.setCellValueFactory(new PropertyValueFactory<>("renewal"));

        //cost needs $ formatting
        costCol.setCellValueFactory( data->new javafx.beans.property.SimpleStringProperty("$" + String.format("%.2f",data.getValue().getCost())));

        //status needs to be converted to string
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus().toString()) );

        //get results from manager
        java.util.ArrayList <Subscription> results=manager.getHighExpenseBills(threshold);

        //clear any previous results from table
        resultsTable.getItems().clear();

        //check if any results found
        if (results.isEmpty()){
            statusLabel.setText("No subscriptions found above $" + String.format("%.2f", threshold));
            statusLabel.setStyle("-fx-text-fill: grey;");
            resultsTable.setVisible(false);
        }
        else{
            //add results to table and show it
            resultsTable.getItems().addAll(results);
            statusLabel.setText("Found " +results.size()+" subscription(s) above $" +String.format("%.2f",threshold));
            statusLabel.setStyle("-fx-text-fill: green;");
            resultsTable.setVisible(true);
        }

    }
}
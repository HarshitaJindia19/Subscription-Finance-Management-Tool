package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.Subscription;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * UpcomingBillsController.java - shows subscriptions renewing in next 7 days
 * @author Ramsha Oad
 * @date April 8 2026
 * @tutorial T01
 */
public class UpcomingBillsController {

    //connects to the table in FXML
    @FXML private TableView<Subscription> billsTable;

    //connects to each column in FXML
    @FXML private TableColumn<Subscription, String> nameCol;
    @FXML private TableColumn<Subscription, String> costCol;
    @FXML private TableColumn<Subscription, String> categoryCol;
    @FXML private TableColumn<Subscription, String> renewalCol;
    @FXML private TableColumn<Subscription, String> startDateCol;
    @FXML private TableColumn<Subscription, String> statusCol;

    //shows message when no upcoming bills
    @FXML private Label emptyLabel;

    private SubscriptionManager manager;


    /**
     * Called by main controller to pass in the shared manager
     * @param manager shared SubscriptionManager
     */

    public void setManager(SubscriptionManager manager) {
        this.manager=manager;
    }

    /**
     * Called by main controller after setManager to load and display upcoming bills
     * Gets upcoming bills from manager and puts them in the table
     */
    public void loadUpcomingBills(){

        //set up each column to pull the right data from Subscription object
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        categoryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));

        renewalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRenewal()));

        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus().toString()));

        //cost needs special formatting with $ sign
        costCol.setCellValueFactory (data ->new javafx.beans.property.SimpleStringProperty("$" + String.format("%.2f", data.getValue().getCost())));

        //start date needs formatting to MM/dd/yyyy
        startDateCol.setCellValueFactory(data-> new javafx.beans.property.SimpleStringProperty(data.getValue().getStartDate().format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"))) );

        //get upcoming bills from manager
        java.util.ArrayList<Subscription>upcoming =manager.getUpcomingBills();

        //if no upcoming bills, show the empty message and hide table
        if (upcoming.isEmpty()) {
            emptyLabel.setVisible(true);
            billsTable.setVisible(false );

        }
        else {
            //add all upcoming bills into the table
            billsTable.getItems().addAll(upcoming);
            emptyLabel.setVisible(false);
            billsTable.setVisible(true);
        }
}
}
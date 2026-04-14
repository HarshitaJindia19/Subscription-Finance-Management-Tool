package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.Subscription;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

/**
 * SearchController.java - handles searching subscriptions by name or category
 * Displays matching results in a table with highlighted matches
 * @date April 14 2026
 * @tutorial T01
 */
public class SearchController {

    //search input field
    @FXML
    private TextField searchField;

    //dropdown to select search type (Name or Category)
    @FXML
    private ComboBox<String> searchTypeBox;

    //table to display search results
    @FXML
    private TableView<Subscription> resultsTable;

    //table columns
    @FXML
    private TableColumn<Subscription, String> nameCol;
    @FXML
    private TableColumn<Subscription, String> categoryCol;
    @FXML
    private TableColumn<Subscription, String> costCol;
    @FXML
    private TableColumn<Subscription, String> statusCol;

    //label to show messages (errors, results count, etc.)
    @FXML
    private Label messageLabel;

    private SubscriptionManager manager;

    /**
     * Called by main controller to pass shared manager
     * @param manager shared SubscriptionManager
     */
    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
    }

    /**
     * Automatically runs when FXML loads
     * Initializes dropdown, table, and listeners
     */
    @FXML
    public void initialize() {

        //add options to search type dropdown
        searchTypeBox.getItems().addAll("Name", "Category");
        searchTypeBox.setValue("Name");

        //setup table columns
        setupTable();

        //live search when typing
        searchField
                .textProperty()
                .addListener((obs, oldVal, newVal) -> search());

        //search again when dropdown changes
        searchTypeBox.setOnAction(e -> search());
    }

    /**
     * Sets up table column mappings and formatting
     */
    private void setupTable() {

        //map name column
        nameCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName())
        );

        //map category column
        categoryCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCategory())
        );

        //format cost with $ and 2 decimal places
        costCol.setCellValueFactory(data -> {
            double cost = data.getValue().getCost();
            String formattedCost = "$" + String.format("%.2f", cost);
            return new SimpleStringProperty(formattedCost);
        });

        //convert status enum to string
        statusCol.setCellValueFactory(data -> {
            String status = data.getValue().getStatus().toString();
            return new SimpleStringProperty(status);
        });

        //highlight matching text in name column
        nameCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                String keyword = searchField.getText();

                if (keyword != null && !keyword.isBlank()
                        && item.toLowerCase().contains(keyword.toLowerCase())) {

                    setText(item);
                    setStyle("-fx-background-color: pink;");
                } else {
                    setText(item);
                    setStyle("");
                }
            }
        });

        //highlight matching text in category column
        categoryCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                String keyword = searchField.getText();

                if (keyword != null && !keyword.isBlank()
                        && item.toLowerCase().contains(keyword.toLowerCase())) {

                    setText(item);
                    setStyle("-fx-background-color: pink;");
                } else {
                    setText(item);
                    setStyle("");
                }
            }
        });
    }

    /**
     * Performs search based on user input and selected type
     * Updates table with matching results
     */
    @FXML
    private void search() {

        //check if manager exists
        if (manager == null) {
            showError("Manager not initialized.");
            return;
        }

        String lookup = searchField.getText().trim().toLowerCase();

        //if input is empty, clear results
        if (lookup.isEmpty()) {
            resultsTable.getItems().clear();
            resultsTable.setVisible(false);
            messageLabel.setText("Type to search...");
            return;
        }

        ArrayList<Subscription> results = new ArrayList<>();

        //loop through all subscriptions
        for (Subscription sub : manager.getAllSubscriptions()) {

            if (searchTypeBox.getValue().equals("Name")) {

                String name = sub.getName();

                if (name != null && name.toLowerCase().contains(lookup)) {
                    results.add(sub);
                }

            } else {

                String category = sub.getCategory();

                if (category != null && category.toLowerCase().contains(lookup)) {
                    results.add(sub);
                }
            }
        }

        //clear previous results
        resultsTable.getItems().clear();

        //if no results found
        if (results.isEmpty()) {
            messageLabel.setText("No results found.");
            messageLabel.setStyle("-fx-text-fill: grey;");
            resultsTable.setVisible(false);
            return;
        }

        //display results
        resultsTable.getItems().addAll(results);
        resultsTable.setVisible(true);

        //show number of results
        messageLabel.setText("Found " + results.size() + " result(s).");
        messageLabel.setStyle("-fx-text-fill: green;");
    }

    /**
     * Clears search field and results table
     */
    @FXML
    private void clear() {
        searchField.clear();
        resultsTable.getItems().clear();
        resultsTable.setVisible(false);
        messageLabel.setText("");
    }

    /**
     * Displays an error message in red text
     * @param msg error message
     */
    private void showError(String msg) {
        messageLabel.setText("⛔ " + msg);
        messageLabel.setStyle("-fx-text-fill: red;");
    }
}
package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.Subscription;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class SearchController {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> searchTypeBox;
    @FXML
    private TableView<Subscription> resultsTable;
    @FXML
    private TableColumn<Subscription, String> nameCol;
    @FXML
    private TableColumn<Subscription, String> categoryCol;
    @FXML
    private TableColumn<Subscription, String> costCol;
    @FXML
    private TableColumn<Subscription, String> statusCol;
    @FXML
    private Label messageLabel;

    private SubscriptionManager manager;

    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
    }

    @FXML
    public void initialize() {
        searchTypeBox.getItems().addAll("Name", "Category");
        searchTypeBox.setValue("Name");

        setupTable();
    }

    private void setupTable() {
        nameCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName())
        );
        categoryCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCategory())
        );
        costCol.setCellValueFactory(data -> {
            double cost = data.getValue().getCost();
            String formattedCost = "$" + String.format("%.2f", cost);
            return new SimpleStringProperty(formattedCost);
        });
        statusCol.setCellValueFactory(data -> {
            String status = data.getValue().getStatus().toString();
            return new SimpleStringProperty(status);
        });
    }

    @FXML
    private void search() {

        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            showError("Please enter a search term.");
            return;
        }
        ArrayList<Subscription> results;
        if (searchTypeBox.getValue().equals("Name")) {
            results = manager.searchByName(query);
        } else {
            results = manager.searchByCategory(query);
        }
        resultsTable.getItems().clear();

        if (results.isEmpty()) {
            messageLabel.setText("No results found.");
            messageLabel.setStyle("-fx-text-fill: grey;");
            resultsTable.setVisible(false);
            return;
        }

        resultsTable.getItems().addAll(results);
        resultsTable.setVisible(true);

        messageLabel.setText("Found " + results.size() + " result(s).");
        messageLabel.setStyle("-fx-text-fill: green;");
    }

    @FXML
    private void clear() {
        searchField.clear();
        resultsTable.getItems().clear();
        resultsTable.setVisible(false);
        messageLabel.setText("");
    }

    private void showError(String msg) {
        messageLabel.setText("⛔ " + msg);
        messageLabel.setStyle("-fx-text-fill: red;");
    }
}


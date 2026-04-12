package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.Subscription;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
}


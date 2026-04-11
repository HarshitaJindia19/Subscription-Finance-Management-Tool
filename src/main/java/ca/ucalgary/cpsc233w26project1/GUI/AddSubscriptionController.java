package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.Status;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddSubscriptionController {
    public Label existingCategoriesLabel;
    @FXML private TextField nameField;
    @FXML private TextField costField;
    @FXML private TextField categoryField;
    @FXML private ComboBox<String> renewalComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Label messageLabel;
    @FXML private Button cancelButton;

    private SubscriptionManager manager;

    //--------------------- Set Manager ----------------------------//
    @FXML
    public void setManager(SubscriptionManager manager) {
        this.manager = manager;

        if (!manager.getCategories().isEmpty()) {
            existingCategoriesLabel.setText(
                    "Existing: " + String.join(", ", manager.getCategories()));
        }
    }

    //---------------- Initialize ------------------//
    @FXML
    public void initialize() {
        renewalComboBox.getItems().addAll("Monthly", "Yearly");

        // populate status comboBox from enum
        for (Status status : Status.values()) {
            statusComboBox.getItems().add(status.toString());
        }
        statusComboBox.setValue("ACTIVE"); // set default value
    }

    @FXML
    void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addSubscription(ActionEvent actionEvent) {

        // Get the user inputs and store in variables
        String name = nameField.getText().trim();
        String cost = costField.getText().trim().replace("$", "");
        String category = categoryField.getText().trim();
        String renewal = renewalComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue(); // null if not selected
        String status = statusComboBox.getValue();

        // Validate user inputs and display status message

        // Validate name:
        if (name.isEmpty()) {
            showError("Please enter a name.");
            return;
        }
        if (!SubscriptionManager.isOnlyText(name)) {
            showError("Name nust contain only letters and spaces.");
            return; // keep pop-up window open
        }

        if (manager.subscriptionExists(name)) {
            showError("A subscription named " + SubscriptionManager.formatTitleCase(name) + " already exists.");
            return;
        }

        // Validate Cost:
        if(!SubscriptionManager.isValidCost(cost)) {
            showError("Cost must be a valid positive number (e.g. 15.99");
            return;
        }

        // Validate Category:
        if (!SubscriptionManager.isOnlyText(category)) {
            showError("Category nust contain only letters and spaces.");
            return;
        }

        // Validate Renewal:
        if (renewal == null) {
            showError("Please select a billing cycle.");
            return;
        }

        // Validate startDate:
        if (startDate == null) {
            showError("Please select a start date.");
            return;
        }

        // Validate endDate:
        if (endDate != null && !endDate.isAfter(startDate)) { // Validate only if endDate is after startDate
            showError("End date must be after start date.");
            return;
        }

        // Validate Status:
        if (status == null) {
            showError("Please select a status.");
            return;
        }


        // Store the inputs into the storeNewSubscriptions



    }

    // Error message styling
    private void showError(String message) {
        messageLabel.setText("⛔ " + message);
        messageLabel.setStyle("-fx-text-fill: #c62828; -fx-font-size: 12px;");
    }
}

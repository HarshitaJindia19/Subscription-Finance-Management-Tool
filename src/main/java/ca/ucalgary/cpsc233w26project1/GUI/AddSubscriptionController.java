package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.Status;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Controls the Add Subscription popup window.
 * Handles user input, validates all fields, and stores new subscriptions into the shared SubscriptionManager.
 *
 * @author Harshita
 * @date April 2026
 * @tutorial T01
 */
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
    @FXML private Button closeButton;

    private SubscriptionManager manager;

    //--------------------- Set Manager ----------------------------//

    /**
     * Called by the main controller to inject the shared SubscriptionManager.
     * Also displays existing categories as a hint to the user.
     *
     * @param manager the shared SubscriptionManager instance
     */
    @FXML
    public void setManager(SubscriptionManager manager) {
        this.manager = manager;

        // Display existing categories immediately at open
        if (!manager.getCategories().isEmpty()) {
            existingCategoriesLabel.setText(
                    "Existing: " + String.join(", ", manager.getCategories()));
        }
    }

    //---------------- Initialize ------------------//

    /**
     * Called automatically when the FXML window loads.
     * Loads the billing cycle and status dropdowns with valid options.
     * Status defaults to ACTIVE.
     */
    @FXML
    public void initialize() {
        renewalComboBox.getItems().addAll("Monthly", "Yearly");

        // populate status comboBox from the enum
        for (Status status : Status.values()) {
            statusComboBox.getItems().add(status.toString());
        }
        statusComboBox.setValue("ACTIVE"); // set default value
    }

    //------------------------ Button Events -----------------------//
    /**
     * Closes the Add Subscription popup window without saving.
     * Triggered by the close button.
     *
     * @param actionEvent the even when button is clicked
     */
    @FXML
    void close(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Validates all input fields and stores the new subscription.
     * Displays an error message if any field is invalid.
     * Displays a success message and clears fields if the subscription is added.
     * Triggered by the Add Subscription button.
     *
     * @param actionEvent the event when button is clicked
     */
    @FXML
    private void addSubscription(ActionEvent actionEvent) {

        // Get the user inputs and store in variables
        String name = nameField.getText().trim();
        String costInput = costField.getText().trim().replace("$", "");
        String category = categoryField.getText().trim();
        String renewal = renewalComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue(); // null if not selected
        String statusInput = statusComboBox.getValue();

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
        if(!SubscriptionManager.isValidCost(costInput)) {
            showError("Cost must be a valid positive number (e.g. 15.99");
            return;
        }

        // Validate Category:
        if (category.isEmpty()) {
            showError("Please enter a category.");
            return;
        }
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
        if (statusInput == null) {
            showError("Please select a status.");
            return;
        }

        // Store the inputs into the storeNewSubscriptions

        double cost = Double.parseDouble(costInput);
        Status status = Status.getStatus(statusInput);

        boolean successfulAdd = manager.storeNewSubscription(name, cost, category, renewal, startDate, endDate, status);

        if (successfulAdd) {
            showSuccess("subscription added successfully!");
            clearFields();
        } else {
            showError("Failed to add. Name may already exist");
        }
    }


    //----------------- Helper Functions --------------------//
    /**
     * Clears all input fields after a successful subscription add.
     * Resets status to default ACTIVE and refreshes the category hint.
     */
    private void clearFields() {
        nameField.clear();
        costField.clear();
        categoryField.clear();
        renewalComboBox.setValue(null);
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        statusComboBox.setValue("ACTIVE");

        // refresh category hint
        if (manager != null && !manager.getCategories().isEmpty()) {
            existingCategoriesLabel.setText(
                    "Existing: " + String.join(", ", manager.getCategories()));
        }
    }

    /**
     * Displays a red error message in the message label, for errors
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        messageLabel.setText("⛔ " + message);
        messageLabel.setStyle("-fx-text-fill: #c62828; -fx-font-size: 12px;");
    }

    /**
     * Displays a green error message in the message label, for successes
     *
     * @param message the error message to display
     */
    private void showSuccess(String message) {
        messageLabel.setText("✅" + message);
        messageLabel.setStyle("-fx-text-fill: #2e7d32; -fx-font-size: 12px;");
    }
}

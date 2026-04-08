import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * UpdateSubscriptionController.java - handles the Update Subscription popup
 * @author Ramsha Oad
 * @date April 7 2026
 * @tutorial T01
 */
public class UpdateSubscriptionController {

    @FXML private TextField nameField;
    @FXML private Label statusLabel;
    @FXML private VBox editFields;
    @FXML private TextField newNameField;
    @FXML private TextField newCostField;
    @FXML private TextField newCategoryField;
    @FXML private ComboBox<String> newRenewalField;
    @FXML private TextField newStartDateField;
    @FXML private TextField newEndDateField;
    @FXML private ComboBox<String> newStatusField;

    //the shared data manager which is set from main controller
    private SubscriptionManager manager;

    //stores the index of subscription found
    private int foundIndex = -1;


    /**
     * Called automatically when window opens - sets up ComboBox dropdown options
     */
    @FXML
    public void initialize() {
        //add options to the billing cycle dropdown
        newRenewalField.getItems().addAll("Monthly", "Yearly");

        //add options to the status dropdown (active, paused, cancelled)
        newStatusField.getItems().addAll("ACTIVE", "PAUSED", "CANCELLED");
}


    /**
     * Called by main controller to pass the shared manager
     * @param manager shared SubscriptionManager
     */

    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
}
    /**
     * Called when user clicks "Find Subscription" button
     * Searches for subscription by name and shows edit fields if found
     */
    @FXML
    private void findSubscription() {
        //get what user put in the name field
        String name = nameField.getText().trim();

        //check if name field is empty
        if (name.isEmpty()) {
            statusLabel.setText("Enter a subscription name:");
            statusLabel.setStyle("-fx-text-fill: rgba(255,0,0,0.64);");
            editFields.setVisible(false);
            return;
        }

        //search for subscription in manager
        foundIndex = manager.findingSubscriptionIndex(name);

        //show an error if nothing found
        if (foundIndex == -1) {
            statusLabel.setText("Subscription '" + name + "' not found.");
            statusLabel.setStyle("-fx-text-fill: rgba(255,0,0,0.64);");
            editFields.setVisible(false);
            return;
        }

        //get the subscription object if found
        Subscription sub = manager.getAllSubscriptions().get(foundIndex);

        //pre-fill all the fields with current values
        newNameField.setText(sub.getName());
        newCostField.setText(String.valueOf(sub.getCost()));
        newCategoryField.setText(sub.getCategory());
        newRenewalField.setValue(sub.getRenewal());
        newStartDateField.setText(sub.getStartDate().format(java.time.format.DateTimeFormatter.ofPattern("MM/DD/YYYY")));
        newEndDateField.setText(sub.getEndDate()!= null? sub.getEndDate().format(java.time.format.DateTimeFormatter. ofPattern("MM/DD/YYYY")) : "");
        newStatusField.setValue(sub.getStatus().toString());

        //show the edit fields
        statusLabel.setText("Subscription found! Edit fields below:");
        statusLabel.setStyle("-fx-text-fill: green;");
        editFields.setVisible(true);
    }

    /**
     * Called when user clicks save changes button
     * Validates each field and updates the subscription
     */
    @FXML
    private void saveChanges() {
        //check if a subscription was found first
        if (foundIndex == -1) {
            showAlert("Please find a subscription first.");
            return;
        }
        //get all the values from fields
        String newName =newNameField.getText().trim();
        String newCost=newCostField.getText().trim();
        String newCategory=newCategoryField.getText().trim();
        String newRenewal=newRenewalField.getValue();
        String newStartDate=newStartDateField.getText().trim();
        String newEndDate =newEndDateField.getText().trim();
        String newStatus=newStatusField.getValue();

        //check name (can only contain letters)
        if (!SubscriptionManager. isOnlyText(newName) ) {
            showAlert("Name must contain only letters.");
            return;
        }

        //check cost (can only be a positive number)
        if (!SubscriptionManager.isValidCost(newCost)) {
            showAlert("Cost must be a valid positive number (e.g. 15.99).");
            return;
        }

        //check category (can only contain letters)
        if (!SubscriptionManager.isOnlyText(newCategory)) {
            showAlert("Category must contain only letters.");
            return;
        }

        //check renewal (must be selected)
        if (newRenewal ==null) {
            showAlert("Please select a billing cycle.");
            return;
        }

        //check start date
        if (!SubscriptionManager.isValidDate(newStartDate)) {
            showAlert("Start date must be in MM/DD/YYYY format.");
            return;
        }
        //check end date only if user typed something
        if (!newEndDate.isEmpty() && !SubscriptionManager.isValidDate(newEndDate)) {
            showAlert("End date must be in MM/DD/YYYY format or leave blank.");
            return;

        }

        //check status is chosen
        if (newStatus ==null) {
            showAlert("Please select a status.");
            return;
        }

        //updating each field
        manager.changeSubscriptionChoice(foundIndex, 0,newName);
        manager.changeSubscriptionChoice(foundIndex, 1, newCost);
        manager.changeSubscriptionChoice(foundIndex,2,newCategory);
        manager.changeSubscriptionChoice(foundIndex, 3,newRenewal);
        manager.changeSubscriptionChoice(foundIndex,4, newStartDate);

        //only update end date if user typed something
        if (!newEndDate.isEmpty() ){
            manager.changeSubscriptionChoice(foundIndex, 5,newEndDate);
        }

        manager.changeSubscriptionChoice(foundIndex, 6, newStatus);

        //show success msg and close window
        statusLabel.setText("Updated successfully!");
        statusLabel.setStyle("-fx-text-fill: green;");
        nameField.getScene().getWindow().hide();
    }

    /**
     * Shows a alerting popup with error message
     * @param message error message
     */

    private void showAlert(String message){
        Alert alert= new Alert(Alert. AlertType.ERROR);
        alert.setTitle("Invalid input.");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
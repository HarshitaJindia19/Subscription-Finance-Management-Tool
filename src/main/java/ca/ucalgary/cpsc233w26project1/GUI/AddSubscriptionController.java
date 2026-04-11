package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddSubscriptionController {
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

    @FXML
    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
    }

    @FXML
    void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addSubscription(ActionEvent actionEvent) {
    }
}

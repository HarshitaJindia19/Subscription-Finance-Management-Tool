package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddBudgetController {
    @FXML private TextField categoryField;
    @FXML private TextField limitField;
    @FXML private Label messageLabel;
    @FXML private Button closeButton;

    private SubscriptionManager manager;

    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
    }
}

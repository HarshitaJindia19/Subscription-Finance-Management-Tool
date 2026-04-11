package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.FileHandler;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.List;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ca.ucalgary.cpsc233w26project1.Subscription;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

public class SubscriptionMainController {
    public Button btnAllSubscriptions;
    public Button btnActive;
    public Button btnPaused;
    public Button btnCancelled;
    public Button btnUpcomingBills;
    public Button btnMonthlySpend;
    public Button btnAnnualSpend;
    public Button btnBudgetAlerts;
    public Button btnBudgetByCategory;
    public Button btnHighExpense;
    public Button btnSearch;
    public Button btnSaveToFile;
    public Button btnLoadFromFile;
    public TextField searchField;
    public Label lblTotalSubscriptions;
    public Label lblMonthlySpend;
    public Label lblAnnualSpend;
    public Label lblUpcomingBills;
    public Label lblStatusBar;

    //main table and columns
    @FXML private TableView<Subscription> mainTable;
    @FXML private TableColumn<Subscription, String> nameCol;
    @FXML private TableColumn<Subscription, String> costCol;
    @FXML private TableColumn<Subscription, String> categoryCol;
    @FXML private TableColumn<Subscription, String> renewalCol;
    @FXML private TableColumn<Subscription, String> startDateCol;
    @FXML private TableColumn<Subscription, String> endDateCol;
    @FXML private TableColumn<Subscription, String> statusCol;
    @FXML private TableColumn<Subscription, Void> actionsCol;

    private SubscriptionManager manager = new SubscriptionManager();

    /**
     * Initializes program with command line arguments
     * Loads data from file if filename is given
     * @param args command line arguments
     */
    public void initData(List<String> args) {
        if (!args.isEmpty()) {
            String filename = args.get(0);
            java.io.File file = new java.io.File(filename);
            if (file.exists()) {
                FileHandler.loadFromCSV(manager, filename);
            }
        }
    }

    /**
     * loadTable - Helper method to load a list of subscriptions into the main table
     * @param subs list of subscriptions to display
     */
    private void loadTable(java.util.ArrayList<Subscription> subs) {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        renewalCol.setCellValueFactory(new PropertyValueFactory<>("renewal"));

        costCol.setCellValueFactory(data ->new javafx.beans.property.SimpleStringProperty("$" + String.format("%.2f", data.getValue().getCost())));

        startDateCol.setCellValueFactory(data ->new javafx.beans.property.SimpleStringProperty(data.getValue().getStartDate().format(java.time.format.DateTimeFormatter.ofPattern("MM/DD/YYYY"))));

        endDateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEndDate() != null ? data.getValue().getEndDate().format(java.time.format.DateTimeFormatter.ofPattern("MM/DD/YYYY")) : "N/A"));

        statusCol.setCellValueFactory(data ->new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus().toString()));

        mainTable.getItems().clear();
        mainTable.getItems().addAll(subs);

        setupEditColumn();
    }


    /**
     * Sets up the EDIT button in each row of the main table
     * Clicking EDIT opens the UpdateSubscription popup for that row
     */
    private void setupEditColumn() {Callback<TableColumn<Subscription, Void>, TableCell<Subscription, Void>> cellFactory = new Callback<>() {
        @Override
        public TableCell<Subscription,Void> call(TableColumn<Subscription, Void> param){
            return new TableCell<>() {
                //make the EDIT button
                private final Button editButton = new Button("Edit");{
                //when EDIT is clicked, open UpdateSubscription window
                editButton.setOnAction(event ->{Subscription sub = getTableView().getItems().get(getIndex());
                    try{
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateSubscription.fxml"));
                        Stage stage = new Stage();
                        stage.setScene(new Scene(loader.load()));
                        stage.setTitle("Update Subscription");
                        UpdateSubscriptionController controller = loader.getController();
                        controller.setManager(manager);
//                        controller.prefillFields(sub);
                        stage.show();
                        }
                        catch (IOException e) {
                            e.printStackTrace();}});
             }

                @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        }
                        else {
                            setGraphic(editButton);
                        }
                    }
                };
            }
        };
        actionsCol.setCellFactory(cellFactory);
    }


    //-------------------------------------VIEW METHODS----------------------------------------------------

    public void showAllSubscriptions(ActionEvent actionEvent) {
        loadTable(manager.getAllSubscriptions());
    }

    public void showActiveSubscriptions(ActionEvent actionEvent) {
        loadTable(manager.getActiveSubscription());
    }

    public void showPausedSubscriptions(ActionEvent actionEvent) {
        loadTable(manager.getPausedSubscription());
    }

    public void showCancelledSubscriptions(ActionEvent actionEvent) {
        loadTable(manager.getCancelledSubscription());
    }

    public void showUpcomingBills(ActionEvent actionEvent) {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("UpcomingBills.fxml"));
            Stage stage =new Stage();
            stage.setScene(new Scene (loader.load()));
            stage.setTitle("Upcoming Bills");
            UpcomingBillsController controller=loader.getController();
            controller.setManager(manager);
            controller.loadUpcomingBills();
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------FINANCE METHODS---------------------------------------------

    public void showMonthlySpend(ActionEvent actionEvent) {
    }

    public void showAnnualSpend(ActionEvent actionEvent) {
    }

    public void showBudgetAlerts(ActionEvent actionEvent) {
    }

    public void showBudgetByCategory(ActionEvent actionEvent) {
    }

    public void showHighExpenseBills(ActionEvent actionEvent) {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("HighExpenseBills.fxml"));
            Stage stage=new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("High-Expense Bills");
            HighExpenseBillsController controller =loader.getController();
            controller.setManager(manager);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------TOOL METHODS------------------------------------

    public void saveToFile(ActionEvent actionEvent) {
    }

    public void loadFromFile(ActionEvent actionEvent) {
    }

    public void openAddSubscriptionStage(ActionEvent actionEvent) {
    }

    public void openAddBudgetStage(ActionEvent actionEvent) {
    }

    public void about(ActionEvent actionEvent) {
    }
}

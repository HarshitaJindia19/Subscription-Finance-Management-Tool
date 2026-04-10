package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.FileHandler;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.List;
import java.io.IOException;

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

    public void showAllSubscriptions(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SubscriptionMain.fxml"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showActiveSubscriptions(ActionEvent actionEvent) {
    }

    public void showPausedSubscriptions(ActionEvent actionEvent) {
    }

    public void showCancelledSubscriptions(ActionEvent actionEvent) {
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

    public void saveToFile(ActionEvent actionEvent) {
    }

    public void loadFromFile(ActionEvent actionEvent) {
    }
}

module ca.ucalgary.cpsc233w26project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    exports ca.ucalgary.cpsc233w26project1.GUI;
    opens ca.ucalgary.cpsc233w26project1.GUI to javafx.fxml;
}
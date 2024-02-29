module by.ustsinovich.elementaryciphers {
    requires javafx.controls;
    requires javafx.fxml;


    opens by.ustsinovich.elementaryciphers to javafx.fxml;
    exports by.ustsinovich.elementaryciphers;
    exports by.ustsinovich.elementaryciphers.controller;
    opens by.ustsinovich.elementaryciphers.controller to javafx.fxml;
}
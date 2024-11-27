module com.para {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.para to javafx.fxml;

    exports com.para;
}

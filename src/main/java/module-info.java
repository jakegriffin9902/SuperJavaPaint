module com.example.superjavapaint {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    opens com.example.superjavapaint to javafx.fxml;
    exports com.example.superjavapaint;
    exports com.example.superjavapaint.menutools;
}
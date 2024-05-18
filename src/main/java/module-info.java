module contactsapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    opens contactsapp to javafx.fxml;
    exports contactsapp;
}

module contactsapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens contactsapp to javafx.fxml;
    exports contactsapp;
}

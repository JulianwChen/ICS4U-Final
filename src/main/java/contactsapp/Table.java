package contactsapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Table extends Application {

    // Initialize variables to be used throughout the class
    Person changePerson = new Person("", "", "", "", "");
    int person = 0;
    final TextField addFirstName = new TextField();
    final TextField addLastName = new TextField();
    final TextField addPhoneNumber = new TextField();
    final TextField addEmail = new TextField();
    final TextField addAddress = new TextField();

    // mathis was here
    private TableView<Person> table = new TableView<Person>();
    private final ObservableList<Person> data = FXCollections.observableArrayList();
    final HBox hb = new HBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Contacts App");
        stage.setWidth(1000);
        stage.setHeight(600);

        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));

        table.setEditable(false);

        // First Column = First Name
        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("firstName"));
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setFirstName(t.getNewValue());
                    }
                });

        // Second Column = Last Name
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lastName"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setLastName(t.getNewValue());
                    }
                });

        // Thrid Column = Phone Number
        TableColumn phoneNumCol = new TableColumn("Phone Number");
        phoneNumCol.setMinWidth(150);
        phoneNumCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("phoneNumber"));
        phoneNumCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneNumCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setPhoneNumber(t.getNewValue());
                    }
                });

        // Fourth Column = Email
        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("email"));
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setEmail(t.getNewValue());
                    }
                });

        // Fifth Column = Address
        TableColumn addressCol = new TableColumn("Address");
        addressCol.setMinWidth(200);
        addressCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("address"));
        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setAddress(t.getNewValue());
                    }
                });

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, phoneNumCol, emailCol, addressCol);

        // Text Fields to input Contact info
        addFirstName.setPromptText("First Name");
        addFirstName.setMaxWidth(firstNameCol.getPrefWidth());
        addLastName.setMaxWidth(lastNameCol.getPrefWidth());
        addLastName.setPromptText("Last Name");
        addPhoneNumber.setMaxWidth(100);
        addPhoneNumber.setPromptText("Phone Number");
        addEmail.setMaxWidth(emailCol.getPrefWidth());
        addEmail.setPromptText("Email");
        addAddress.setMaxWidth(addressCol.getPrefWidth());
        addAddress.setPromptText("Address");

        // Add button to add the new contacts
        final Button addButton = new Button("Add");
        // Edit button to enter edit mode
        final Button editButton = new Button("Edit");
        // Button to update contact info
        final Button updateButton = new Button("Update");
        // Button to delete contact
        final Button deleteButton = new Button("Delete");

        // Set up add button
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (addFirstName.getText() != "") {
                    data.add(new Person(
                            addFirstName.getText(),
                            addLastName.getText(),
                            addPhoneNumber.getText(),
                            addEmail.getText(),
                            addAddress.getText()));
                    clearTextFields();
                    writeCSV();
                }
            }
        });

        // Set up edit button
        editButton.setDisable(true);
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                updateButton.setVisible(true);
                addButton.setDisable(true);
                editButton.setDisable(true);
                deleteButton.setVisible(false);
                addFirstName.setText(changePerson.getFirstName());
                addLastName.setText(changePerson.getLastName());
                addPhoneNumber.setText(changePerson.getPhoneNumber());
                addEmail.setText(changePerson.getEmail());
                addAddress.setText(changePerson.getAddress());
            }
        });
        // Set up update button
        updateButton.setVisible(false);
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                addButton.setDisable(false);
                updateButton.setVisible(false);
                editContact(changePerson);
                clearTextFields();
            }
        });

        // Set up delete button
        deleteButton.setVisible(false);
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                deleteContact(changePerson);
                table.getSelectionModel().clearSelection();
                deleteButton.setVisible(false);
                editButton.setDisable(true);
            }
        });

        // Add selection listener to TableView(if the user clicks on a row)
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // Perform actions based on the selected item
            if (newSelection != null) {
                editButton.setDisable(false);
                deleteButton.setVisible(true);
                changePerson = newSelection;
            }
        });

        // Resets buttons if the user clicks away from the table
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (!table.isHover() && !addButton.isHover() && !editButton.isHover() && !deleteButton.isHover()) {
                editButton.setDisable(true);
                deleteButton.setVisible(false);
                table.getSelectionModel().clearSelection();
            }
        });

        // Resets buttons if user clicks on an empty row
        table.setRowFactory(people -> {
            TableRow<Person> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    editButton.setDisable(true);
                    deleteButton.setVisible(false);
                    table.getSelectionModel().clearSelection();
                }
            });
            return row;
        });

        // Add everything to hbox
        hb.getChildren().addAll(addFirstName, addLastName, addPhoneNumber, addEmail, addAddress, addButton, editButton,
                updateButton, deleteButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        // Upload all preexisting contacts
        readCSV();

        stage.setScene(scene);
        stage.show();
    }


    // Read from CSV file and add populate ccontacts app
    public void readCSV() {
        try (Scanner scanner = new Scanner(new File("C:\\Users\\Julia\\OneDrive\\Desktop\\ICS4U Final\\ContactsData.csv"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                data.add(new Person(values[0], values[1], values[2], values[3], values[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update CSV file with changes in contacts
    public void writeCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Julia\\OneDrive\\Desktop\\ICS4U Final\\ContactsData.csv"))) {
            // bw.write("");
            for (Person person : data) {
                bw.write(person.getFirstName() + "," + person.getLastName() + "," + person.getPhoneNumber() + "," + person.getEmail() + "," + person.getAddress());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to edit/update contact info
    public void editContact(Person person) {
        person.setFirstName(addFirstName.getText());
        person.setLastName(addLastName.getText());
        person.setPhoneNumber(addPhoneNumber.getText());
        person.setEmail(addEmail.getText());
        person.setAddress(addAddress.getText());
        table.refresh();
        writeCSV();
    }

    // Method to clear all text fields
    public void clearTextFields() {
        addFirstName.clear();
        addLastName.clear();
        addPhoneNumber.clear();
        addEmail.clear();
        addAddress.clear();
    }

    // Method to delete contact
    public void deleteContact(Person person) {
        data.remove(person);
        writeCSV();
    }

    // Class to represent contacts
    public static class Person {

        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty phoneNumber;
        private final SimpleStringProperty email;
        private final SimpleStringProperty address;

        // Person constructor
        private Person(String fName, String lName, String pNum, String email, String address) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.phoneNumber = new SimpleStringProperty(pNum);
            this.email = new SimpleStringProperty(email);
            this.address = new SimpleStringProperty(address);
        }

        // First Name
        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

        // Last Name
        public String getLastName() {
            return lastName.get();
        }

        public void setLastName(String fName) {
            lastName.set(fName);
        }

        // Phone Number
        public String getPhoneNumber() {
            return phoneNumber.get();
        }

        public void setPhoneNumber(String phoneNum) {
            phoneNumber.set(phoneNum);
        }

        // Email
        public String getEmail() {
            return email.get();
        }

        public void setEmail(String email) {
            this.email.set(email);
        }

        // Address
        public String getAddress() {
            return address.get();
        }

        public void setAddress(String address) {
            this.address.set(address);
        }
    }
}
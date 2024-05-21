package contactsapp;

import java.io.BufferedWriter;
import java.io.File;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Table extends Application {

    // Initialize variables to be used throughout the class
    Person person = new Person("", "", "", "", "", "", "");
    final TextField addFirstName = new TextField();
    final TextField addLastName = new TextField();
    final TextField addPhoneNumber = new TextField();
    final TextField addPhoneNumber2 = new TextField();
    final TextField addEmail = new TextField();
    final TextField addAddress = new TextField();
    final TextField addBday = new TextField();
    final CheckBox secondNumber = new CheckBox("Second Phone Number?");
    int index;

    private TableView<Person> table = new TableView<Person>();
    private final ObservableList<Person> data = FXCollections.observableArrayList();
    final HBox hb = new HBox();
    final HBox hb2 = new HBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Contacts App");
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setResizable(false);

        final Label label = new Label("Contacts");
        label.setFont(new Font("Arial", 20));

        // user cannot directly edit contact info from the table
        table.setEditable(false);

        // First Column = First Name
        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setSortable(false);
        firstNameCol.setReorderable(false);

        // Second Column = Last Name
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setSortable(false);
        lastNameCol.setReorderable(false);

        // Third Column = Phone Number
        TableColumn phoneNumCol = new TableColumn("Phone Number");
        phoneNumCol.setMinWidth(150);
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<Person, String>("phoneNumber"));
        phoneNumCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneNumCol.setSortable(false);
        phoneNumCol.setReorderable(false);

        // Fourth Column = Email
        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setSortable(false);
        emailCol.setReorderable(false);
        // TODO: add multiple emails per eprson??

        // Fifth Column = Address
        TableColumn addressCol = new TableColumn("Address");
        addressCol.setMinWidth(200);
        addressCol.setCellValueFactory(new PropertyValueFactory<Person, String>("address"));
        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressCol.setSortable(false);
        addressCol.setReorderable(false);

        // Sixth Column = birthday
        TableColumn bdayCol = new TableColumn("Birthday");
        bdayCol.setMinWidth(200);
        bdayCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Birthday"));
        bdayCol.setCellFactory(TextFieldTableCell.forTableColumn());
        bdayCol.setSortable(false);
        bdayCol.setReorderable(false);

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, phoneNumCol, emailCol, addressCol, bdayCol);

        // Text Fields to input Contact info
        addFirstName.setPromptText("First Name");
        addFirstName.setMaxWidth(firstNameCol.getPrefWidth());
        addLastName.setPromptText("Last Name");
        addLastName.setMaxWidth(lastNameCol.getPrefWidth());
        addPhoneNumber.setPromptText("Phone Number");
        addPhoneNumber.setMaxWidth(110);
        addPhoneNumber2.setPromptText("Phone Number 2");
        addPhoneNumber2.setMaxWidth(110);
        addPhoneNumber2.setVisible(false);
        addEmail.setPromptText("Email");
        addEmail.setMaxWidth(emailCol.getPrefWidth());
        addAddress.setPromptText("Address");
        addAddress.setMaxWidth(addressCol.getPrefWidth());
        addBday.setPromptText("Birthday");
        addBday.setMaxWidth(bdayCol.getPrefWidth());

        // Second phone number checkbox, set to false
        secondNumber.setSelected(false);
        // Show the second phone number textfield accordingly
        secondNumber.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // If checked, show the texfield for the second phone number
                if (secondNumber.isSelected()) {
                    addPhoneNumber2.setVisible(true);
                } else {
                // If not checked, hide the textfield for the second phone number
                    addPhoneNumber2.setVisible(false);
                    addPhoneNumber2.clear();
                }
            }
        });

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
            public void handle(ActionEvent arg0) {
                // Contact must have first name
                if (!addFirstName.getText().isBlank()) {
                    // Remove all commas from contact info
                    noComma();
                    // Create new Person
                    data.add(new Person(
                            addFirstName.getText(),
                            addLastName.getText(),
                            addPhoneNumber.getText(),
                            addPhoneNumber2.getText(),
                            addEmail.getText(),
                            addAddress.getText(),
                            addBday.getText()));
                    // If there is a second phone number, add it to the next row
                    if (!addPhoneNumber2.getText().isBlank()) {
                        data.add(new Person("", "", addPhoneNumber2.getText(), "", "", "", ""));
                    }
                    // Clear all textfields
                    clearTextFields();
                    // Update the csv file
                    writeCSV();
                    // Reset the second phone number checkbox
                    secondNumber.setSelected(false);
                    addPhoneNumber2.setVisible(false);
                }
            }
        });

        // Set up edit button
        editButton.setDisable(true);
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Enter edit mode
                // Hide all other functions other than update
                // Update textfields with contact info so user can edit
                updateButton.setVisible(true);
                addButton.setDisable(true);
                editButton.setDisable(true);
                deleteButton.setVisible(false);
                secondNumber.setVisible(false);
                addPhoneNumber2.setVisible(true);
                addFirstName.setText(person.getFirstName().trim());
                addLastName.setText(person.getLastName().trim());
                addPhoneNumber.setText(person.getPhoneNumber().trim());
                addPhoneNumber2.setText(person.getPhoneNumber2().trim());
                addEmail.setText(person.getEmail().trim());
                addAddress.setText(person.getAddress().trim());
                addBday.setText(person.getBirthday().trim());
            }
        });

        // Set up update button
        updateButton.setVisible(false);
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Update contact info as long as contact has a first name
                // Reset all buttons
                if (!addFirstName.getText().isBlank()) {
                    addButton.setDisable(false);
                    updateButton.setVisible(false);
                    secondNumber.setVisible(true);
                    secondNumber.setSelected(false);
                    addPhoneNumber2.setVisible(false);
                    editContact(person);
                    clearTextFields();
                }
            }
        });

        // Set up delete button
        deleteButton.setVisible(false);
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Delete contact
                // Reset buttons
                deleteContact(person);
                table.getSelectionModel().clearSelection();
                deleteButton.setVisible(false);
                editButton.setDisable(true);
            }
        });

        // Add selection listener to TableViewto check if the user clicks on a row
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // Gets the index of the clicked row
            if (table.getSelectionModel().getSelectedIndex() >= 0) {
                index = table.getSelectionModel().getSelectedIndex();
            }
            // If not editing:
            if (newSelection != null && !updateButton.isVisible()) {
                person = newSelection;
                // The index of the(row of the) second phone number is the same as the contact
                // ~If the user edits/deletes the second phone number row, edit/delete entire contact instead of just that row
                if (index > 0 && !table.getItems().get(index - 1).getPhoneNumber2().isBlank()) {
                    person = table.getItems().get(--index);
                }
                editButton.setDisable(false);
                deleteButton.setVisible(true);
                // If in editing mode:
            } else if (newSelection != null && updateButton.isVisible()) {
                person = newSelection;
                // The index of the second phone number is the same as the contact
                // Show full conatct info when user clicks on second phone number(instead of only showing that row's info)
                if (index > 0 && !table.getItems().get(index - 1).getPhoneNumber2().isBlank()) {
                    person = table.getItems().get(--index);
                }
                // Update textfield with selected contact's info
                addFirstName.setText(person.getFirstName().trim());
                addLastName.setText(person.getLastName().trim());
                addPhoneNumber.setText(person.getPhoneNumber().trim());
                addPhoneNumber2.setText(person.getPhoneNumber2().trim());
                addEmail.setText(person.getEmail().trim());
                addAddress.setText(person.getAddress().trim());
                addBday.setText(person.getBirthday().trim());
            }
        });

        // Resets buttons + unselect row if the user clicks away from the table
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (!table.isHover() && !addButton.isHover() && !editButton.isHover() && !deleteButton.isHover()) {
                editButton.setDisable(true);
                deleteButton.setVisible(false);
                table.getSelectionModel().clearSelection();
            }
        });

        // Resets buttons + deselect selected row if user clicks on an empty row
        table.setRowFactory(person -> {
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
        hb.getChildren().addAll(addFirstName, addLastName, addPhoneNumber, addEmail, addAddress, addBday, addButton,
                editButton, updateButton, deleteButton, secondNumber);
        hb.setSpacing(3);
        // Empty space to line up phone number text fields
        Region space = new Region();
        space.setMinWidth(167);
        // Add space and second phone number text field to hbox2
        hb2.getChildren().addAll(space, addPhoneNumber2);
        // Set up vbox
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(15, 0, 0, 15));
        vbox.getChildren().addAll(label, table, hb, hb2);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        // Upload all preexisting contacts upon startup
        readCSV();

        stage.setScene(scene);
        stage.show();
    }

    // Read from CSV file and populate contacts app
    public void readCSV() {
        try (Scanner scanner = new Scanner(
                new File("C:\\Users\\Julia\\OneDrive\\Desktop\\ICS4U Final\\ContactsData.csv"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                if (values.length == 7) { // to prevent ArrayIndexOutOfBoundsException
                    // Add all contacts
                    data.add(new Person(values[0], values[1], values[2], values[3], values[4], values[5], values[6]));
                    // Add second phone number to separate row if there is one
                    if (!values[3].isBlank()) {
                        data.add(new Person("", "", values[3], "", "", "", ""));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update CSV file with changes in contacts
    public void writeCSV() {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("C:\\Users\\Julia\\OneDrive\\Desktop\\ICS4U Final\\ContactsData.csv"))) {
            for (Person person : data) {
                // write all contact info, skip all second phone number rows to avoid double counting
                if (!person.getFirstName().isBlank()) {
                    bw.write(person.getFirstName() + " ," + person.getLastName() + " ," + person.getPhoneNumber() + " ,"
                            + person.getPhoneNumber2() + " ," + person.getEmail() + " ," + person.getAddress() + " , "
                            + person.getBirthday());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to edit/update contact info
    public void editContact(Person person) {
        // Remove all commas
        noComma();
        // Update info
        person.setFirstName(addFirstName.getText());
        person.setLastName(addLastName.getText());
        person.setPhoneNumber(addPhoneNumber.getText());
        person.setPhoneNumber2(addPhoneNumber2.getText());
        person.setEmail(addEmail.getText());
        person.setAddress(addAddress.getText());
        person.setBirthday(addBday.getText());
        // Add a new row underneath if there is a second phone number
        if (!table.getItems().get(index).getPhoneNumber2().isBlank()) {
            // If there was a second phone number before, remove that row so it can be updated
            if (index < data.size() - 1 && table.getItems().get(index + 1).getFirstName().isBlank()) {
                data.remove(index + 1);
            }
            // Add new row
            data.add(index + 1, new Person("", "", addPhoneNumber2.getText(), "", "", "", ""));
        }
        // Refresh/update the table
        table.refresh();
        // Write changes on csv file
        writeCSV();
    }

    // Method to clear all text fields
    public void clearTextFields() {
        addFirstName.clear();
        addLastName.clear();
        addPhoneNumber.clear();
        addPhoneNumber2.clear();
        addEmail.clear();
        addAddress.clear();
        addBday.clear();
    }

    // Make sure there are no commas in contact info
    public void noComma() {
        if (addFirstName.getText().contains(",")) {
            addFirstName.setText(addFirstName.getText().replace(",", ""));
        }
        if (addLastName.getText().contains(",")) {
            addLastName.setText(addLastName.getText().replace(",", ""));
        }
        if (addPhoneNumber.getText().contains(",")) {
            addPhoneNumber.setText(addPhoneNumber.getText().replace(",", ""));
        }
        if (addPhoneNumber2.getText().contains(",")) {
            addPhoneNumber2.setText(addPhoneNumber2.getText().replace(",", ""));
        }
        if (addEmail.getText().contains(",")) {
            addEmail.setText(addEmail.getText().replace(",", ""));
        }
        if (addAddress.getText().contains(",")) {
            addAddress.setText(addAddress.getText().replace(",", ""));
        }
        if (addBday.getText().contains(",")) {
            addBday.setText(addBday.getText().replace(",", ""));
        }
    }

    // Method to delete contact
    public void deleteContact(Person person) {
        // If the row under is the second phone number, delete it as well
        if (!table.getItems().get(index).getPhoneNumber2().isBlank()) {
            data.remove(index + 1);
        }
        // Delete selected contact
        data.remove(person);
        // Update csv file
        writeCSV();
    }

    // Class to represent contacts
    public static class Person {
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty phoneNumber;
        private final SimpleStringProperty phoneNumber2;
        private final SimpleStringProperty email;
        private final SimpleStringProperty address;
        private final SimpleStringProperty birthday;

        // Person constructor
        private Person(String fName, String lName, String pNum, String pNum2, String email, String address, String bday) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.phoneNumber = new SimpleStringProperty(pNum);
            this.phoneNumber2 = new SimpleStringProperty(pNum2);
            this.email = new SimpleStringProperty(email);
            this.address = new SimpleStringProperty(address);
            this.birthday = new SimpleStringProperty(bday);
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

        // Phone Number 2
        public String getPhoneNumber2() {
            return phoneNumber2.get();
        }

        public void setPhoneNumber2(String phoneNum2) {
            phoneNumber2.set(phoneNum2);
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

        // Birthday
        public String getBirthday() {
            return birthday.get();
        }

        public void setBirthday(String birthday) {
            this.birthday.set(birthday);
        }
    }
}
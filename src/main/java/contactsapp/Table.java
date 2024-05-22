package contactsapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
    Person person = new Person("", "", "", "", "", "", "",
                                FXCollections.observableArrayList(), FXCollections.observableArrayList());
    final TextField addFirstName = new TextField();
    final TextField addLastName = new TextField();
    final TextField addPhoneNumber = new TextField();
    final TextField addPhoneNumber2 = new TextField();
    final TextField addEmail = new TextField();
    final TextField addAddress = new TextField();
    final TextField addBday = new TextField();
    final TextArea addDevices1 = new TextArea();
    final TextArea addDevices2 = new TextArea();
    final CheckBox secondNumber = new CheckBox("Second Phone Number?");
    int index;

    private TableView<Person> table = new TableView<Person>();
    private final ObservableList<Person> data = FXCollections.observableArrayList();
    final HBox hb = new HBox();
    final HBox hb2 = new HBox();
    final HBox hb3 = new HBox();
    final HBox hbDevices = new HBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Contacts App");
        stage.setWidth(1200);
        stage.setHeight(800);
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

        // Fifth Column = Address
        TableColumn addressCol = new TableColumn("Address");
        addressCol.setMinWidth(200);
        addressCol.setCellValueFactory(new PropertyValueFactory<Person, String>("address"));
        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressCol.setSortable(false);
        addressCol.setReorderable(false);

        // Sixth Column = Birthday
        TableColumn bdayCol = new TableColumn("Birthday");
        bdayCol.setMinWidth(200);
        bdayCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Birthday"));
        bdayCol.setCellFactory(TextFieldTableCell.forTableColumn());
        bdayCol.setSortable(false);
        bdayCol.setReorderable(false);

        // Seventh Column = Devices registered
        TableColumn<Person, ListView<String>> devicesCol = new TableColumn<>("Devices");
        devicesCol.setMinWidth(200);
        devicesCol.setCellValueFactory(deviceData -> {
            Person person = deviceData.getValue();
            ListView<String> listView = new ListView<String>();
            listView.setPrefHeight(70);
            listView.getItems().addAll(person.getDevices1());
            if (!person.getDevices2().isEmpty()) {
                listView.getItems().addAll(person.getDevices2());
            }
            return new SimpleObjectProperty<>(listView);
        });

        // TODO: reordder columns put phone/device together

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, phoneNumCol, emailCol, addressCol, bdayCol, devicesCol);

        // Text Fields to input Contact info
        addFirstName.setPromptText("First Name");
        addFirstName.setMaxWidth(firstNameCol.getPrefWidth());
        addLastName.setPromptText("Last Name");
        addLastName.setMaxWidth(lastNameCol.getPrefWidth());
        addPhoneNumber.setPromptText("Phone Number");
        addPhoneNumber.setMaxWidth(170);
        addPhoneNumber2.setPromptText("Phone Number 2");
        addPhoneNumber2.setMaxWidth(170);
        addPhoneNumber2.setVisible(false);
        addEmail.setPromptText("Email");
        addEmail.setMaxWidth(emailCol.getPrefWidth());
        addAddress.setPromptText("Address");
        addAddress.setMaxWidth(addressCol.getPrefWidth());
        addBday.setPromptText("Birthday");
        addBday.setMaxWidth(bdayCol.getPrefWidth());

        // TextAreas for devices input
        addDevices1.setPromptText("Devices registered for phone number 1\n(one per line)");
        addDevices1.setMaxWidth(150);
        addDevices1.setPrefRowCount(3);
        addDevices2.setPromptText("Devices registeredfor phone number 2\n(one per line)");
        addDevices2.setMaxWidth(150);
        addDevices2.setPrefRowCount(3);
        addDevices2.setVisible(false);
        // TODO: make prompt text have brackets in new line

        // Second phone number checkbox, set to false
        secondNumber.setSelected(false);
        // Show the second phone number textfield accordingly
        secondNumber.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // If checked, show the texfield for the second phone number and devices
                if (secondNumber.isSelected()) {
                    addPhoneNumber2.setVisible(true);
                    addDevices2.setVisible(true);
                } else {
                    // If not checked, hide the textfield for the second phone number and devices
                    addPhoneNumber2.setVisible(false);
                    addDevices2.setVisible(false);
                    addPhoneNumber2.clear();
                    addDevices2.clear();
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
                        addBday.getText(),
                        noBlank(FXCollections.observableArrayList(addDevices1.getText().split("\\n"))),
                        FXCollections.observableArrayList()));
                    // If there is a second phone number, add it to the next row
                    if (!addPhoneNumber2.getText().isBlank()) {
                        data.add(new Person("", "", addPhoneNumber2.getText(), "", "", "", "",
                                            FXCollections.observableArrayList(),
                                            noBlank(FXCollections.observableArrayList(addDevices2.getText().split("\\n")))));
                    }
                    // Clear all textfields
                    clearTextFields();
                    // Update the csv file
                    writeCSV();
                    // Reset the second phone number checkbox
                    secondNumber.setSelected(false);
                    addPhoneNumber2.setVisible(false);
                    addDevices2.setVisible(false);
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
                addDevices2.setVisible(true);
                addFirstName.setText(person.getFirstName().trim());
                addLastName.setText(person.getLastName().trim());
                addPhoneNumber.setText(person.getPhoneNumber().trim());
                addPhoneNumber2.setText(person.getPhoneNumber2().trim());
                addEmail.setText(person.getEmail().trim());
                addAddress.setText(person.getAddress().trim());
                addBday.setText(person.getBirthday().trim());
                addDevices1.setText(String.join("\n", person.getDevices1()));
                addDevices2.setText(String.join("\n", person.getDevices2()));
                // TODO: device 2 does not show anything?
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
                    addDevices2.setVisible(false);
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
                addDevices1.setText(String.join("\n", person.getDevices1()));
                addDevices2.setText(String.join("\n", person.getDevices2()));
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

        // Add Everything
        hb.getChildren().addAll(addButton, editButton, updateButton, deleteButton, secondNumber);
        hb.setSpacing(3);

        hb2.getChildren().addAll(addFirstName, addLastName, addEmail, addAddress, addPhoneNumber, addPhoneNumber2);
        hb2.setSpacing(3);

        // Empty space to line up phone number/devices text fields
        Region space = new Region();
        space.setMinWidth(330);

        hbDevices.getChildren().addAll(space, addDevices1, addDevices2);
        hbDevices.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb, hb2, hb3, hbDevices);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        // Upload all preexisting contacts upon startup
        readCSV();

        stage.setScene(scene);
        stage.show();
    }

    // Read from CSV file and populate contacts app
    public void readCSV() {
        try (Scanner scan = new Scanner(
                new File("C:\\Users\\Julia\\OneDrive\\Desktop\\ICS4U Final\\ContactsData.csv"))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] values = line.split(",");
                if (values.length == 9) { // to prevent ArrayIndexOutOfBoundsException
                    // Add all contacts
                    data.add(new Person(values[0], values[1], values[2], values[3], values[4], values[5], values[6],
                                        FXCollections.observableArrayList(values[7].split("\\|")),
                                        FXCollections.observableArrayList()));
                    // Add second phone number to separate row if there is one
                    if (!values[3].isBlank()) {
                        data.add(new Person("", "", values[3], "", "", "", "",
                                            FXCollections.observableArrayList(),
                                            FXCollections.observableArrayList(values[8].split("\\|"))));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update CSV file with changes in contacts
    public void writeCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(
                "C:\\Users\\Julia\\OneDrive\\DesktopICS4U Final\\ContactsData.csv"))) {
            for (Person person : data) {
                bw.write(person.getFirstName() + "," +
                        person.getLastName() + "," +
                        person.getPhoneNumber() + "," +
                        person.getPhoneNumber2() + "," +
                        person.getEmail() + "," +
                        person.getAddress() + "," +
                        person.getBirthday() + "," +
                        String.join("|", person.getDevices1()) + "," +
                        String.join("|", person.getDevices2()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO: does not write
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
        person.setDevices1(noBlank(FXCollections.observableArrayList(addDevices1.getText().split("\\n"))));
        person.setDevices2(FXCollections.observableArrayList());
        // Add a new row underneath if there is a second phone number
        if (!table.getItems().get(index).getPhoneNumber2().isBlank()) {
            // If there was a second phone number before, remove that row so it can be updated
            if (index < data.size() - 1 && table.getItems().get(index + 1).getFirstName().isBlank()) {
                data.remove(index + 1);
            }
            // Add new row
            data.add(index + 1, new Person("", "", addPhoneNumber2.getText(), "", "", "", "",
                    FXCollections.observableArrayList(),
                    noBlank(FXCollections.observableArrayList(addDevices2.getText().split("\\n")))));
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
        addDevices1.clear();
        addDevices2.clear();
    }

    // Make sure there are no commas in contact info
    public void noComma() {
        addFirstName.setText(addFirstName.getText().replaceAll(",", ""));
        addLastName.setText(addLastName.getText().replaceAll(",", ""));
        addPhoneNumber.setText(addPhoneNumber.getText().replaceAll(",", ""));
        addPhoneNumber2.setText(addPhoneNumber2.getText().replaceAll(",", ""));
        addEmail.setText(addEmail.getText().replaceAll(",", ""));
        addAddress.setText(addAddress.getText().replaceAll(",", ""));
        addBday.setText(addBday.getText().replaceAll(",", ""));
        addDevices1.setText(addDevices1.getText().replaceAll(",", ""));
        addDevices2.setText(addDevices2.getText().replaceAll(",", ""));
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

    // Method to remove all blank lines in the devices TextArea
    public ObservableList<String> noBlank(ObservableList<String> list) {
        ObservableList<String> good = FXCollections.observableArrayList();
        for (String x : list) {
            if (!x.isBlank()) {
                good.add(x);
            }
        }
        return good;
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
        private final SimpleListProperty<String> devices1;
        private final SimpleListProperty<String> devices2;

        private Person(String fName, String lName, String pNum, String pNum2, String email, String address, String bday,
                        ObservableList<String> devices1, ObservableList<String> devices2) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.phoneNumber = new SimpleStringProperty(pNum);
            this.phoneNumber2 = new SimpleStringProperty(pNum2);
            this.email = new SimpleStringProperty(email);
            this.address = new SimpleStringProperty(address);
            this.birthday = new SimpleStringProperty(bday);
            this.devices1 = new SimpleListProperty<>(devices1);
            this.devices2 = new SimpleListProperty<>(devices2);
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

        public void setLastName(String lName) {
            lastName.set(lName);
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

        // Registered Devices for Phone Number 1
        public ObservableList<String> getDevices1() {
            return devices1.get();
        }

        public void setDevices1(ObservableList<String> devices) {
            this.devices1.set(devices);
        }

        // Registered Devices for Phone Number 2
        public ObservableList<String> getDevices2() {
            return devices2.get();
        }

        public void setDevices2(ObservableList<String> devices) {
            this.devices2.set(devices);
        }
    }
}
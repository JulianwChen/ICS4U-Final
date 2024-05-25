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
import javafx.scene.control.TableCell;
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
import javafx.util.Callback;

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
        TableColumn<Person, String> firstNameCol = new TableColumn<Person, String>("First Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        // Change the colour of the contact's first name depending on how long it is
        firstNameCol.setCellFactory(new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
            @Override
            public TableCell<Person, String> call(TableColumn<Person, String> column) {
                return new TableCell<Person, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (item != null && !empty) {
                            setText(item);
                            if (item.length() > 5) {
                                setStyle("-fx-text-fill: darkred;");
                            } else {
                                setStyle("-fx-text-fill: darkgreen;");
                            }
                        } else {
                            setText(null);
                            setStyle("");
                        }
                    }
                };
            }
        });
        firstNameCol.setSortable(false);
        firstNameCol.setReorderable(false);

        // Second Column = Last Name
        TableColumn<Person, String> lastNameCol = new TableColumn<Person, String>("Last Name");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setSortable(false);
        lastNameCol.setReorderable(false);

        // Third Column = Phone Number
        TableColumn<Person, String> phoneNumCol = new TableColumn<Person, String>("Phone Number");
        phoneNumCol.setMinWidth(150);
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<Person, String>("phoneNumber"));
        phoneNumCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneNumCol.setSortable(false);
        phoneNumCol.setReorderable(false);

        // Fourth Column = Email
        TableColumn<Person, String> emailCol = new TableColumn<Person, String>("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setSortable(false);
        emailCol.setReorderable(false);

        // Fifth Column = Address
        TableColumn<Person, String> addressCol = new TableColumn<Person, String>("Address");
        addressCol.setMinWidth(200);
        addressCol.setCellValueFactory(new PropertyValueFactory<Person, String>("address"));
        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressCol.setSortable(false);
        addressCol.setReorderable(false);

        // Sixth Column = Birthday
        TableColumn<Person, String> bdayCol = new TableColumn<Person, String>("Birthday");
        bdayCol.setMinWidth(200);
        bdayCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Birthday"));
        bdayCol.setCellFactory(TextFieldTableCell.forTableColumn());
        bdayCol.setSortable(false);
        bdayCol.setReorderable(false);

        // Seventh Column = Devices registered
        TableColumn<Person, ListView<String>> devicesCol = new TableColumn<Person, ListView<String>>("Devices Registered");
        devicesCol.setMinWidth(200);
        devicesCol.setCellValueFactory(deviceData -> {
            Person person = deviceData.getValue();
            ListView<String> listView = new ListView<String>();
            listView.setPrefHeight(70);
            listView.getItems().addAll(person.getDevices1());
            return new SimpleObjectProperty<>(listView);
        });
        devicesCol.setSortable(false);
        devicesCol.setReorderable(false);

        // Add all the columns to the table
        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol, addressCol, bdayCol, phoneNumCol, devicesCol);

        // TextFields to input Contact info
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

        // TextAreas to input devices registered
        addDevices1.setPromptText("Devices registered for phone number 1 (one per line)");
        addDevices1.setMaxWidth(150);
        addDevices1.setPrefRowCount(3);
        addDevices2.setPromptText("Devices registered for phone number 2 (one per line)");
        addDevices2.setMaxWidth(150);
        addDevices2.setPrefRowCount(3);
        addDevices2.setVisible(false);

        // Second phone number checkbox, set to false
        secondNumber.setSelected(false);
        // Show the second phone number textfield and devices registered accordingly
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
                if (!addFirstName.getText().isBlank() && !addFirstName.getText().contains(",")) {
                    // Remove all commas and pipes from contact info
                    noBad();
                    // Create new Person
                    data.add(new Person(
                        addFirstName.getText().trim(),
                        addLastName.getText().trim(),
                        addEmail.getText().trim(),
                        addAddress.getText().trim(),
                        addBday.getText().trim(),
                        addPhoneNumber.getText().trim(),
                        addPhoneNumber2.getText().trim(),
                        noBlank(FXCollections.observableArrayList(addDevices1.getText().split("\\n"))),
                        noBlank(FXCollections.observableArrayList(addDevices2.getText().split("\\n")))));
                    // If there is a second phone number, add it to the next row
                    if (!addPhoneNumber2.getText().isBlank()) {
                        data.add(new Person("", "", "", "", "", addPhoneNumber2.getText().trim(), "",
                                            noBlank(FXCollections.observableArrayList(addDevices2.getText().split("\\n"))),
                                            FXCollections.observableArrayList()));
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
                // Hide all functions other than update button
                // Update textfields with contact info so user can edit
                updateButton.setVisible(true);
                addButton.setDisable(true);
                editButton.setDisable(true);
                deleteButton.setVisible(false);
                secondNumber.setVisible(false);
                addPhoneNumber2.setVisible(true);
                addDevices2.setVisible(true);
                addFirstName.setText(person.getFirstName());
                addLastName.setText(person.getLastName());
                addEmail.setText(person.getEmail());
                addAddress.setText(person.getAddress());
                addBday.setText(person.getBirthday());
                addPhoneNumber.setText(person.getPhoneNumber());
                addPhoneNumber2.setText(person.getPhoneNumber2());
                addDevices1.setText(String.join("\n", person.getDevices1()));
                addDevices2.setText(String.join("\n", person.getDevices2()));
            }
        });

        // Set up update button
        updateButton.setVisible(false);
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Update contact info as long as contact has a first name
                // Reset all buttons
                if (!addFirstName.getText().isBlank() && !addFirstName.getText().contains(",")) {
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

        // Add selection listener to TableView to check if the user clicks on a row
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
                // Show full contact info when user clicks on second phone number(instead of only showing that row's info)
                if (index > 0 && !table.getItems().get(index - 1).getPhoneNumber2().isBlank()) {
                    person = table.getItems().get(--index);
                }
                // Update textfield with selected contact's info
                addFirstName.setText(person.getFirstName());
                addLastName.setText(person.getLastName());
                addEmail.setText(person.getEmail());
                addAddress.setText(person.getAddress());
                addBday.setText(person.getBirthday());
                addPhoneNumber.setText(person.getPhoneNumber());
                addPhoneNumber2.setText(person.getPhoneNumber2());
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

        // Add Everything to HBoxes
        hb.getChildren().addAll(addButton, editButton, updateButton, deleteButton, secondNumber);
        hb.setSpacing(3);

        hb2.getChildren().addAll(addFirstName, addLastName, addEmail, addAddress, addBday, addPhoneNumber, addPhoneNumber2);
        hb2.setSpacing(3);

        // Empty space to line up phone number/devices text fields
        Region space = new Region();
        space.setMinWidth(412);

        hbDevices.getChildren().addAll(space, addDevices1, addDevices2);
        hbDevices.setSpacing(3);

        // Add everything to VBox
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
                // Reference the CSV file
                new File("C:\\Users\\Julia\\OneDrive\\Desktop\\ICS4U Final\\ContactsData.csv"))) {
            // Add contacts one at a time
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                // Separate all contact information and put them into a String[]
                String[] values = line.split(",");
                if (values.length == 9) { // to prevent ArrayIndexOutOfBoundsException
                    // Add all contact information
                    data.add(new Person(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(),
                                        values[4].trim(), values[5].trim(), values[6].trim(),
                                        FXCollections.observableArrayList(values[7].trim().split("\\|")),
                                        FXCollections.observableArrayList(values[8].trim().split("\\|"))));
                    // Add second phone number to separate row if there is one
                    if (!values[6].isBlank()) {
                        data.add(new Person("", "", "", "", "", values[6].trim(), "",
                                            FXCollections.observableArrayList(values[8].trim().split("\\|")),
                                            FXCollections.observableArrayList()));
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
                // Write to the CSV file
                "C:\\Users\\Julia\\OneDrive\\Desktop\\ICS4U Final\\ContactsData.csv"))) {
            // Write data one contact at a time
            for (Person person : data) {
                // Separate properties with commas
                // Separate devices registered with pipes
                if (!person.getFirstName().isBlank()) {
                    bw.write(person.getFirstName() + " ," +
                            person.getLastName() + " ," +
                            person.getEmail() + " ," +
                            person.getAddress() + " ," +
                            person.getBirthday() + " ," +
                            person.getPhoneNumber() + " ," +
                            person.getPhoneNumber2() + " ," +
                            String.join("|", person.getDevices1()) + " ," +
                            String.join("|", person.getDevices2()) + " \n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to edit/update contact info
    public void editContact(Person person) {
        // Remove all commas and pipes
        noBad();
        // Boolean variables to check if there was ever a second phone number or device
        boolean phone2;
        if (person.getPhoneNumber2().isBlank()) {
            phone2 = false;
        } else {
            phone2 = true;
        }
        // Update info
        person.setFirstName(addFirstName.getText().trim());
        person.setLastName(addLastName.getText().trim());
        person.setEmail(addEmail.getText().trim());
        person.setAddress(addAddress.getText().trim());
        person.setBirthday(addBday.getText().trim());
        person.setPhoneNumber(addPhoneNumber.getText().trim());
        person.setPhoneNumber2(addPhoneNumber2.getText().trim());
        person.setDevices1(noBlank(FXCollections.observableArrayList(addDevices1.getText().split("\\n"))));
        person.setDevices2(noBlank(FXCollections.observableArrayList(addDevices2.getText().split("\\n"))));
        // Add a new row underneath main row if there is a second phone number
        if (!table.getItems().get(index).getPhoneNumber2().isBlank()) {
            // If there was already a second phone number, remove that row first before creating the new updated one
            if (index < data.size() - 1 && table.getItems().get(index + 1).getFirstName().isBlank()) {
                data.remove(index + 1);
            }
            // Create a new row with second phone number and its devices registered
            data.add(index + 1, new Person("", "", "", "", "", addPhoneNumber2.getText(), "",
                    noBlank(FXCollections.observableArrayList(addDevices2.getText().split("\\n"))),
                    FXCollections.observableArrayList()));
        } else if (phone2 && table.getItems().get(index).getPhoneNumber2().isBlank()) {
            // If the user removes the second phone number, delete that row
            data.remove(index + 1);
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
        addEmail.clear();
        addAddress.clear();
        addBday.clear();
        addPhoneNumber.clear();
        addPhoneNumber2.clear();
        addDevices1.clear();
        addDevices2.clear();
    }

    // Method to make sure there are no commas or pipes in contact info
    public void noBad() {
        addFirstName.setText(addFirstName.getText().replaceAll(",", ""));
        addLastName.setText(addLastName.getText().replaceAll(",", ""));
        addEmail.setText(addEmail.getText().replaceAll(",", ""));
        addAddress.setText(addAddress.getText().replaceAll(",", ""));
        addBday.setText(addBday.getText().replaceAll(",", ""));
        addPhoneNumber.setText(addPhoneNumber.getText().replaceAll(",", ""));
        addPhoneNumber2.setText(addPhoneNumber2.getText().replaceAll(",", ""));
        addDevices1.setText(addDevices1.getText().replaceAll(",", ""));
        addDevices2.setText(addDevices2.getText().replaceAll(",", ""));

        addDevices1.setText(addDevices1.getText().replaceAll("\\|", ""));
        addDevices2.setText(addDevices2.getText().replaceAll("\\|", ""));
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

    // Method to remove all blank lines and leading/trailing blank spaces in the devices registered TextArea
    public ObservableList<String> noBlank(ObservableList<String> list) {
        ObservableList<String> good = FXCollections.observableArrayList();
        for (String x : list) {
            if (!x.isBlank()) {
                good.add(x.trim());
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

        private Person(String fName, String lName, String email, String address, String bday, String pNum, String pNum2,
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
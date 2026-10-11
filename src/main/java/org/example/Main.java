package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    private TableView<Customer> customerTable;

    @Override
    public void start(Stage stage) {

        // Customer name field
        TextField nameField = new TextField();
        nameField.setPromptText("Enter customer name");

        // Province dropdown
        ComboBox<String> provinceBox = new ComboBox<>();
        provinceBox.getItems().addAll(
                "Central",
                "Copperbelt",
                "Eastern",
                "Luapula",
                "Lusaka",
                "Muchinga",
                "Northern",
                "North-Western",
                "Southern",
                "Western"
        );
        provinceBox.setPromptText("Select province");

        // Add customer button
        Button addButton = new Button("Add Customer");

        // Delete customer button
        Button deleteButton = new Button("Delete Selected");

        // Event handler button
        addButton.setOnAction(event -> {

            String name = nameField.getText().trim();
            String province = provinceBox.getValue();

            if (name.isEmpty()) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid Input",
                        "Please enter the customer's name."
                );
                return;
            }

            if (province == null) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid Input",
                        "Please select a province."
                );
                return;
            }

            Customer customer = new Customer(name, province);

            customers.add(customer);

            nameField.clear();
            provinceBox.setValue(null);
        });
        deleteButton.setOnAction(event -> {
            Customer selectedCustomer =
                    customerTable.getSelectionModel().getSelectedItem();

            if (selectedCustomer == null) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "No Customer Selected",
                        "Please select a customer to delete."
                );
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Delete Customer");
            confirmation.setContentText(
                    "Are you sure you want to delete "
                            + selectedCustomer.getName() + "?"
            );

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    customers.remove(selectedCustomer);
                }
            });
        });
        // Customer table
        customerTable = new TableView<>();

        TableColumn<Customer, String> nameColumn =
                new TableColumn<>("Name");
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        TableColumn<Customer, String> provinceColumn =
                new TableColumn<>("Province");
        provinceColumn.setCellValueFactory(
                new PropertyValueFactory<>("province")
        );

        customerTable.getColumns().addAll(
                nameColumn,
                provinceColumn
        );

        customerTable.setItems(customers);

        // Form layout
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);

        form.add(new Label("Customer Name:"), 0, 0);
        form.add(nameField, 1, 0);

        form.add(new Label("Province:"), 0, 1);
        form.add(provinceBox, 1, 1);

        HBox buttons = new HBox(10, addButton, deleteButton);

        VBox root = new VBox(
                15,
                new Label("Customer Manager"),
                form,
                buttons,
                customerTable
        );

        root.setPadding(new Insets(20));

        // Allow the table to grow with the window
        VBox.setVgrow(customerTable, Priority.ALWAYS);

        Scene scene = new Scene(root, 600, 450);

        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();
    }

    // Displays messages to te user
    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
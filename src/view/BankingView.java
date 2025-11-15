package view;

import controller.BankingController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Customer;

public class BankingView extends Application {
    private BankingController controller;
    private TextArea outputArea;

    @Override
    public void start(Stage primaryStage) {
        try {
            controller = new BankingController();
            initializeUI(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Failed to initialize banking system: " + e.getMessage());
        }
    }

    private void initializeUI(Stage primaryStage) {
        primaryStage.setTitle("EBM Banking System - Oracle Database");

        // Create tabs
        TabPane tabPane = new TabPane();

        // Customer Management Tab
        Tab customerTab = createCustomerTab();
        // Account Management Tab
        Tab accountTab = createAccountTab();
        // Transactions Tab
        Tab transactionTab = createTransactionTab();

        tabPane.getTabs().addAll(customerTab, accountTab, transactionTab);

        // Output area
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(200);
        outputArea.setText("Banking System Ready - Connected to Oracle Database\n\n");

        VBox root = new VBox(10, tabPane, outputArea);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createCustomerTab() {
        Tab tab = new Tab("Customer Management");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        // Form fields
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField addressField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        ComboBox<String> employmentCombo = new ComboBox<>();
        employmentCombo.getItems().addAll("Employed", "Self-Employed", "Unemployed", "Student");
        TextField companyField = new TextField();
        TextField companyAddressField = new TextField();

        Button registerBtn = new Button("Register Customer");
        Button viewCustomersBtn = new Button("View All Customers");
        Button clearBtn = new Button("Clear Form");

        // labels and fields to grid
        grid.add(new Label("First Name:*"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:*"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Address:"), 0, 2);
        grid.add(addressField, 1, 2);
        grid.add(new Label("Email:*"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Label("Phone:*"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(new Label("Employment Status:*"), 0, 5);
        grid.add(employmentCombo, 1, 5);
        grid.add(new Label("Company Name:"), 0, 6);
        grid.add(companyField, 1, 6);
        grid.add(new Label("Company Address:"), 0, 7);
        grid.add(companyAddressField, 1, 7);

        // Button panel
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.add(registerBtn, 0, 0);
        buttonGrid.add(viewCustomersBtn, 1, 0);
        buttonGrid.add(clearBtn, 2, 0);

        grid.add(buttonGrid, 1, 8);

        // Register Button Action
        registerBtn.setOnAction(e -> {
            try {
                //
                if (firstNameField.getText().trim().isEmpty() ||
                        lastNameField.getText().trim().isEmpty() ||
                        emailField.getText().trim().isEmpty() ||
                        phoneField.getText().trim().isEmpty() ||
                        employmentCombo.getValue() == null) {

                    showErrorDialog("Please fill in all required fields (*)");
                    return;
                }

                // Create customer object
                Customer customer = new Customer();
                customer.setFirstName(firstNameField.getText().trim());
                customer.setLastName(lastNameField.getText().trim());
                customer.setAddress(addressField.getText().trim());
                customer.setEmail(emailField.getText().trim());
                customer.setPhone(phoneField.getText().trim());
                customer.setEmploymentStatus(employmentCombo.getValue());
                customer.setCompanyName(companyField.getText().trim());
                customer.setCompanyAddress(companyAddressField.getText().trim());

                // Register customer
                if (controller.registerCustomer(customer)) {
                    outputArea.appendText("✅ SUCCESS: Customer registered - " +
                            customer.getFirstName() + " " + customer.getLastName() +
                            " (ID: " + customer.getCustomerId() + ")\n");
                    clearCustomerForm(firstNameField, lastNameField, addressField, emailField,
                            phoneField, companyField, companyAddressField, employmentCombo);
                } else {
                    outputArea.appendText("❌ FAILED: Could not register customer\n");
                }
            } catch (Exception ex) {
                outputArea.appendText("❌ ERROR: " + ex.getMessage() + "\n");
                ex.printStackTrace();
            }
        });

        // View Customers Button Action
        viewCustomersBtn.setOnAction(e -> {
            try {
                outputArea.appendText("\n=== ALL CUSTOMERS ===\n");
                java.util.List<Customer> customers = controller.getAllCustomers();
                if (customers.isEmpty()) {
                    outputArea.appendText("No customers found in database.\n");
                } else {
                    for (Customer customer : customers) {
                        outputArea.appendText(String.format("ID: %d | Name: %s %s | Email: %s | Phone: %s\n",
                                customer.getCustomerId(),
                                customer.getFirstName(),
                                customer.getLastName(),
                                customer.getEmail(),
                                customer.getPhone()));
                    }
                    outputArea.appendText("Total customers: " + customers.size() + "\n");
                }
            } catch (Exception ex) {
                outputArea.appendText("❌ ERROR viewing customers: " + ex.getMessage() + "\n");
            }
        });

        // Clear Button Action
        clearBtn.setOnAction(e -> {
            clearCustomerForm(firstNameField, lastNameField, addressField, emailField,
                    phoneField, companyField, companyAddressField, employmentCombo);
            outputArea.appendText("Form cleared.\n");
        });

        VBox tabContent = new VBox(10, grid);
        tabContent.setPadding(new Insets(10));
        tab.setContent(tabContent);

        return tab;
    }

    private Tab createAccountTab() {
        Tab tab = new Tab("Account Management");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        // Form fields
        TextField customerIdField = new TextField();
        ComboBox<String> accountTypeCombo = new ComboBox<>();
        accountTypeCombo.getItems().addAll("Savings", "Investment", "Cheque");
        TextField initialDepositField = new TextField();
        TextField branchField = new TextField();

        Button openAccountBtn = new Button("Open Account");
        Button viewAccountsBtn = new Button("View Customer Accounts");

        grid.add(new Label("Customer ID:*"), 0, 0);
        grid.add(customerIdField, 1, 0);
        grid.add(new Label("Account Type:*"), 0, 1);
        grid.add(accountTypeCombo, 1, 1);
        grid.add(new Label("Initial Deposit:*"), 0, 2);
        grid.add(initialDepositField, 1, 2);
        grid.add(new Label("Branch:*"), 0, 3);
        grid.add(branchField, 1, 3);

        GridPane accButtonGrid = new GridPane();
        accButtonGrid.setHgap(10);
        accButtonGrid.add(openAccountBtn, 0, 0);
        accButtonGrid.add(viewAccountsBtn, 1, 0);
        grid.add(accButtonGrid, 1, 4);

        // Open Account Button Action
        openAccountBtn.setOnAction(e -> {
            try {
                outputArea.appendText("Account opening feature - To be implemented\n");

            } catch (Exception ex) {
                outputArea.appendText("❌ ERROR: " + ex.getMessage() + "\n");
            }
        });

        // View Accounts Button Action
        viewAccountsBtn.setOnAction(e -> {
            try {
                outputArea.appendText("View accounts feature - To be implemented\n");

            } catch (Exception ex) {
                outputArea.appendText("❌ ERROR: " + ex.getMessage() + "\n");
            }
        });

        VBox tabContent = new VBox(10, grid);
        tabContent.setPadding(new Insets(10));
        tab.setContent(tabContent);

        return tab;
    }

    private Tab createTransactionTab() {
        Tab tab = new Tab("Transactions");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        Button applyInterestBtn = new Button("Apply Monthly Interest");
        Button testTransactionBtn = new Button("Test Deposit");

        applyInterestBtn.setOnAction(e -> {
            try {
                controller.applyMonthlyInterest();
                outputArea.appendText("✅ Monthly interest applied to all accounts\n");
            } catch (Exception ex) {
                outputArea.appendText("❌ ERROR applying interest: " + ex.getMessage() + "\n");
            }
        });

        testTransactionBtn.setOnAction(e -> {
            outputArea.appendText("Test transaction feature - To be implemented\n");
        });

        grid.add(applyInterestBtn, 0, 0);
        grid.add(testTransactionBtn, 1, 0);

        VBox tabContent = new VBox(10, grid);
        tabContent.setPadding(new Insets(10));
        tab.setContent(tabContent);

        return tab;
    }

    private void clearCustomerForm(TextField firstName, TextField lastName, TextField address,
                                   TextField email, TextField phone, TextField company,
                                   TextField companyAddress, ComboBox<String> employment) {
        firstName.clear();
        lastName.clear();
        address.clear();
        email.clear();
        phone.clear();
        company.clear();
        companyAddress.clear();
        employment.setValue(null);
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
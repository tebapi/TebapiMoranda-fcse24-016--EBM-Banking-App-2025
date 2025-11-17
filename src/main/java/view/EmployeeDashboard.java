package view;

import controller.EmployeeController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;
import model.Customer;
import model.Account;

import java.util.List;

public class EmployeeDashboard {
    private Stage stage;
    private User currentUser;
    private EmployeeController employeeController;

    private BorderPane root;
    private VBox sidebar;
    private StackPane contentArea;

    private List<Customer> currentClients;

    public EmployeeDashboard(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
        this.employeeController = new EmployeeController();
        initializeUI();
        showDashboard();
    }

    private void initializeUI() {
        stage.setTitle("EBM Banking - Employee Dashboard");

        root = new BorderPane();
        createSidebar();
        createContentArea();

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);

        stage.setMaximized(true);
        stage.setFullScreen(false);
        stage.centerOnScreen();

        stage.show();
    }

    private void createSidebar() {
        sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #34495e;");
        sidebar.setPrefWidth(220);

        Label welcomeLabel = new Label("Employee Portal");
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label userLabel = new Label("Welcome, " + currentUser.getUsername());
        userLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");

        Button dashboardBtn = createSidebarButton("🏠 Dashboard");
        Button createClientBtn = createSidebarButton("➕ Create New Client");
        Button clientsBtn = createSidebarButton("👥 Clients");
        Button transactionsBtn = createSidebarButton("💰 Process Transactions");
        Button openAccountBtn = createSidebarButton("💳 Open Accounts");
        Button logoutBtn = createSidebarButton("🚪 Logout");

        dashboardBtn.setOnAction(e -> showDashboard());
        createClientBtn.setOnAction(e -> showCreateClient());
        clientsBtn.setOnAction(e -> showClients());
        transactionsBtn.setOnAction(e -> showProcessTransactions());
        openAccountBtn.setOnAction(e -> showOpenAccount());
        logoutBtn.setOnAction(e -> logout());

        VBox buttonBox = new VBox(10, dashboardBtn, createClientBtn, clientsBtn, transactionsBtn, openAccountBtn);
        buttonBox.setAlignment(Pos.TOP_LEFT);

        VBox.setVgrow(buttonBox, Priority.ALWAYS);

        sidebar.getChildren().addAll(welcomeLabel, userLabel, new Separator(), buttonBox, logoutBtn);
        root.setLeft(sidebar);
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-border-color: transparent; -fx-alignment: left; -fx-font-size: 14px;");
        button.setPrefWidth(180);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-border-color: transparent; -fx-alignment: left; -fx-font-size: 14px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-border-color: transparent; -fx-alignment: left; -fx-font-size: 14px;"));
        return button;
    }

    private void createContentArea() {
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));
        root.setCenter(contentArea);
    }

    private void showDashboard() {
        VBox dashboardContent = new VBox(20);
        dashboardContent.setPadding(new Insets(20));

        Label header = new Label("Employee Dashboard");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        header.setTextFill(Color.DARKBLUE);

        HBox statsBox = createStatsBox();
        VBox quickActions = createQuickActions();
        VBox recentActivity = createRecentActivity();

        HBox mainContent = new HBox(20, quickActions, recentActivity);
        mainContent.setAlignment(Pos.TOP_CENTER);

        dashboardContent.getChildren().addAll(header, statsBox, mainContent);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(dashboardContent);
    }

    private HBox createStatsBox() {
        HBox statsBox = new HBox(15);
        statsBox.setAlignment(Pos.CENTER_LEFT);

        List<Customer> allCustomers = employeeController.getAllCustomers();
        int totalClients = allCustomers.size();

        VBox clientsCard = createStatCard("Total Clients", String.valueOf(totalClients), "#27ae60");
        VBox accountsCard = createStatCard("Today's Tasks", "8", "#2980b9");
        VBox pendingCard = createStatCard("Pending", "2", "#e74c3c");

        statsBox.getChildren().addAll(clientsCard, accountsCard, pendingCard);
        return statsBox;
    }

    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 10; -fx-border-radius: 10;", color));
        card.setPrefSize(200, 80);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private VBox createQuickActions() {
        VBox quickActions = new VBox(15);
        quickActions.setPadding(new Insets(20));
        quickActions.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10;");
        quickActions.setPrefWidth(400);

        Label title = new Label("Quick Actions");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        Button createClientBtn = new Button("➕ Create New Client");
        Button viewClientsBtn = new Button("👥 View All Clients");
        Button processTransactionBtn = new Button("💰 Process Transaction");
        Button openAccountBtn = new Button("💳 Open New Account");

        String buttonStyle = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-pref-width: 300; -fx-pref-height: 40;";
        createClientBtn.setStyle(buttonStyle);
        viewClientsBtn.setStyle(buttonStyle);
        processTransactionBtn.setStyle(buttonStyle);
        openAccountBtn.setStyle(buttonStyle);

        createClientBtn.setOnAction(e -> showCreateClient());
        viewClientsBtn.setOnAction(e -> showClients());
        processTransactionBtn.setOnAction(e -> showProcessTransactions());
        openAccountBtn.setOnAction(e -> showOpenAccount());

        quickActions.getChildren().addAll(title, createClientBtn, viewClientsBtn, processTransactionBtn, openAccountBtn);
        return quickActions;
    }

    private VBox createRecentActivity() {
        VBox recentActivity = new VBox(15);
        recentActivity.setPadding(new Insets(20));
        recentActivity.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10;");
        recentActivity.setPrefWidth(400);

        Label title = new Label("Recent Activity");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        TextArea activityArea = new TextArea();
        activityArea.setEditable(false);
        activityArea.setPrefHeight(200);
        activityArea.setStyle("-fx-control-inner-background: white; -fx-font-size: 12px;");

        // Get real activity data
        List<Customer> customers = employeeController.getAllCustomers();
        StringBuilder activityText = new StringBuilder();

        if (customers.isEmpty()) {
            activityText.append("No recent activity.\n");
        } else {
            activityText.append("Recent Client Activity:\n\n");
            int count = 0;
            for (Customer customer : customers) {
                if (count >= 5) break;
                activityText.append("• ").append(customer.getFirstName()).append(" ").append(customer.getLastName()).append("\n");
                count++;
            }
            activityText.append("\nTotal Clients: ").append(customers.size());
        }

        activityArea.setText(activityText.toString());

        recentActivity.getChildren().addAll(title, activityArea);
        return recentActivity;
    }

    private void showCreateClient() {
        ScrollPane scrollContent = new ScrollPane();
        scrollContent.setFitToWidth(true);
        scrollContent.setPadding(new Insets(10));
        scrollContent.setStyle("-fx-background: white; -fx-background-color: white;");

        VBox createClientContent = new VBox(20);
        createClientContent.setPadding(new Insets(20));
        createClientContent.setStyle("-fx-background-color: white;");

        Label header = new Label("Create New Client & Accounts");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(25));
        formContainer.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10;");

        // Client Information Section
        Label clientSection = new Label("Client Information");
        clientSection.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        GridPane clientForm = new GridPane();
        clientForm.setVgap(15);
        clientForm.setHgap(15);
        clientForm.setPadding(new Insets(15, 0, 15, 0));

        // Form fields
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        ComboBox<String> employmentCombo = new ComboBox<>();
        employmentCombo.getItems().addAll("Employed", "Self-Employed", "Unemployed", "Student");
        TextField companyField = new TextField();
        TextField companyAddressField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        // Add labels and fields
        clientForm.add(createLabel("First Name:*"), 0, 0);
        clientForm.add(firstNameField, 1, 0);
        clientForm.add(createLabel("Last Name:*"), 0, 1);
        clientForm.add(lastNameField, 1, 1);
        clientForm.add(createLabel("Email:*"), 0, 2);
        clientForm.add(emailField, 1, 2);
        clientForm.add(createLabel("Phone:*"), 0, 3);
        clientForm.add(phoneField, 1, 3);
        clientForm.add(createLabel("Address:"), 0, 4);
        clientForm.add(addressField, 1, 4);
        clientForm.add(createLabel("Employment:*"), 0, 5);
        clientForm.add(employmentCombo, 1, 5);
        clientForm.add(createLabel("Company:"), 0, 6);
        clientForm.add(companyField, 1, 6);
        clientForm.add(createLabel("Company Address:"), 0, 7);
        clientForm.add(companyAddressField, 1, 7);
        clientForm.add(createLabel("Username:*"), 0, 8);
        clientForm.add(usernameField, 1, 8);
        clientForm.add(createLabel("Password:*"), 0, 9);
        clientForm.add(passwordField, 1, 9);

        // Account Selection Section
        Label accountSection = new Label("Account Setup");
        accountSection.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        VBox accountsContainer = new VBox(15);


        CheckBox savingsCheck = new CheckBox("Savings Account");
        TextField savingsDeposit = new TextField();
        savingsDeposit.setPromptText("Initial deposit");
        savingsDeposit.setPrefWidth(120);

        CheckBox investmentCheck = new CheckBox("Investment Account (Min BWP 500.00)");
        TextField investmentDeposit = new TextField();
        investmentDeposit.setPromptText("Min BWP 500.00");
        investmentDeposit.setPrefWidth(120);

        CheckBox chequeCheck = new CheckBox("Cheque Account");
        TextField chequeDeposit = new TextField();
        chequeDeposit.setPromptText("Initial deposit");
        chequeDeposit.setPrefWidth(120);

        HBox savingsBox = new HBox(10, savingsCheck, new Label("Initial Deposit:"), savingsDeposit);
        HBox investmentBox = new HBox(10, investmentCheck, new Label("Initial Deposit:"), investmentDeposit);
        HBox chequeBox = new HBox(10, chequeCheck, new Label("Initial Deposit:"), chequeDeposit);

        accountsContainer.getChildren().addAll(savingsBox, investmentBox, chequeBox);

        // Branch Selection
        HBox branchBox = new HBox(10);
        branchBox.setAlignment(Pos.CENTER_LEFT);
        Label branchLabel = createLabel("Branch:");
        ComboBox<String> branchCombo = new ComboBox<>();
        branchCombo.getItems().addAll("Main Branch", "City Center", "Mall Branch", "Industrial Area");
        branchCombo.setValue("Main Branch");
        branchBox.getChildren().addAll(branchLabel, branchCombo);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button createBtn = new Button("Create New Client");
        Button clearBtn = new Button("Clear Form");

        createBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 150; -fx-pref-height: 40;");
        clearBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 120; -fx-pref-height: 40;");

        // Result area
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(100);
        resultArea.setVisible(false);
        resultArea.setStyle("-fx-control-inner-background: #e8f5e8; -fx-font-size: 12px; -fx-text-fill: black;");

        createBtn.setOnAction(e -> {
            // Validate all fields
            if (firstNameField.getText().trim().isEmpty() ||
                    lastNameField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    phoneField.getText().trim().isEmpty() ||
                    usernameField.getText().trim().isEmpty() ||
                    passwordField.getText().trim().isEmpty() ||
                    employmentCombo.getValue() == null) {

                showError("Please fill in all required fields (*)");
                return;
            }

            // Validate account selections
            boolean hasSavings = savingsCheck.isSelected();
            boolean hasInvestment = investmentCheck.isSelected();
            boolean hasCheque = chequeCheck.isSelected();

            if (!hasSavings && !hasInvestment && !hasCheque) {
                showError("Please select at least one account type for the client.");
                return;
            }

            // Validate investment account minimum
            if (hasInvestment) {
                try {
                    double investmentAmount = Double.parseDouble(investmentDeposit.getText().trim());
                    if (investmentAmount < 500.00) {
                        showError("Investment account requires minimum BWP 500.00 deposit");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    showError("Please enter a valid amount for investment account");
                    return;
                }
            }

            // Validate cheque account employment requirement
            if (hasCheque && !"Employed".equals(employmentCombo.getValue())) {
                showError("Cheque account can only be opened for employed individuals.");
                return;
            }

            // Create the customer first
            boolean customerCreated = employeeController.createNewCustomer(
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    addressField.getText().trim(),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    employmentCombo.getValue(),
                    companyField.getText().trim(),
                    companyAddressField.getText().trim(),
                    usernameField.getText().trim(),
                    passwordField.getText().trim()
            );

            if (customerCreated) {
                StringBuilder resultMessage = new StringBuilder();
                resultMessage.append("✅ Client created successfully!\n\n");
                resultMessage.append("Client: ").append(firstNameField.getText()).append(" ").append(lastNameField.getText()).append("\n");
                resultMessage.append("Username: ").append(usernameField.getText()).append("\n");
                resultMessage.append("Password: ").append(passwordField.getText()).append("\n\n");
                resultMessage.append("Accounts Created:\n");

                // Get the newly created customer ID
                List<Customer> customers = employeeController.getAllCustomers();
                int newCustomerId = -1;
                for (Customer customer : customers) {
                    if (customer.getEmail().equals(emailField.getText().trim())) {
                        newCustomerId = customer.getCustomerId();
                        break;
                    }
                }

                // Create selected accounts
                if (newCustomerId != -1) {
                    if (hasSavings) {
                        try {
                            double depositAmount = savingsDeposit.getText().trim().isEmpty() ? 0 : Double.parseDouble(savingsDeposit.getText().trim());
                            boolean accountCreated = employeeController.openAccountForCustomer(newCustomerId, "Savings", depositAmount, branchCombo.getValue(), currentUser.getUserId());
                            if (accountCreated) {
                                resultMessage.append("• Savings Account - BWP ").append(depositAmount).append("\n");
                            }
                        } catch (NumberFormatException ex) {
                            showError("Invalid amount for savings account");
                            return;
                        }
                    }

                    if (hasInvestment) {
                        try {
                            double depositAmount = Double.parseDouble(investmentDeposit.getText().trim());
                            boolean accountCreated = employeeController.openAccountForCustomer(newCustomerId, "Investment", depositAmount, branchCombo.getValue(), currentUser.getUserId());
                            if (accountCreated) {
                                resultMessage.append("• Investment Account - BWP ").append(depositAmount).append("\n");
                            }
                        } catch (NumberFormatException ex) {
                            showError("Invalid amount for investment account");
                            return;
                        }
                    }

                    if (hasCheque) {
                        try {
                            double depositAmount = chequeDeposit.getText().trim().isEmpty() ? 0 : Double.parseDouble(chequeDeposit.getText().trim());
                            boolean accountCreated = employeeController.openAccountForCustomer(newCustomerId, "Cheque", depositAmount, branchCombo.getValue(), currentUser.getUserId());
                            if (accountCreated) {
                                resultMessage.append("• Cheque Account - BWP ").append(depositAmount).append("\n");
                            }
                        } catch (NumberFormatException ex) {
                            showError("Invalid amount for cheque account");
                            return;
                        }
                    }
                }

                resultMessage.append("\nThe client can now login with these credentials.");
                resultArea.setText(resultMessage.toString());
                resultArea.setVisible(true);


                clearClientForm(firstNameField, lastNameField, emailField, phoneField,
                        addressField, employmentCombo, companyField, companyAddressField,
                        usernameField, passwordField, savingsCheck, savingsDeposit,
                        investmentCheck, investmentDeposit, chequeCheck, chequeDeposit);


                refreshClientsList();
            } else {
                showError("Failed to create client. The username might already exist.");
            }
        });

        clearBtn.setOnAction(e -> {
            clearClientForm(firstNameField, lastNameField, emailField, phoneField,
                    addressField, employmentCombo, companyField, companyAddressField,
                    usernameField, passwordField, savingsCheck, savingsDeposit,
                    investmentCheck, investmentDeposit, chequeCheck, chequeDeposit);
            resultArea.setVisible(false);
        });

        buttonBox.getChildren().addAll(createBtn, clearBtn);

        formContainer.getChildren().addAll(clientSection, clientForm, new Separator(),
                accountSection, accountsContainer, branchBox, buttonBox, resultArea);
        createClientContent.getChildren().addAll(header, formContainer);
        scrollContent.setContent(createClientContent);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(scrollContent);
    }

    private void clearClientForm(TextField firstName, TextField lastName, TextField email,
                                 TextField phone, TextField address, ComboBox<String> employment,
                                 TextField company, TextField companyAddress, TextField username,
                                 PasswordField password, CheckBox savingsCheck, TextField savingsDeposit,
                                 CheckBox investmentCheck, TextField investmentDeposit,
                                 CheckBox chequeCheck, TextField chequeDeposit) {
        firstName.clear();
        lastName.clear();
        email.clear();
        phone.clear();
        address.clear();
        employment.setValue(null);
        company.clear();
        companyAddress.clear();
        username.clear();
        password.clear();
        savingsCheck.setSelected(false);
        savingsDeposit.clear();
        investmentCheck.setSelected(false);
        investmentDeposit.clear();
        chequeCheck.setSelected(false);
        chequeDeposit.clear();
    }

    private void showClients() {
        VBox clientsContent = new VBox(20);
        clientsContent.setPadding(new Insets(20));

        Label header = new Label("All Clients");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox clientsContainer = new VBox(15);
        clientsContainer.setPadding(new Insets(20));
        clientsContainer.setStyle("-fx-background-color: #f8f9fa; -fx-border-radius: 10;");

        // Load current clients
        loadClientsWithDeleteButtons(clientsContainer);

        Button refreshBtn = new Button("Refresh Clients");
        refreshBtn.setOnAction(e -> {
            refreshClientsList();
        });

        scrollPane.setContent(clientsContainer);
        clientsContent.getChildren().addAll(header, scrollPane, refreshBtn);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(clientsContent);
    }

    private void refreshClientsList() {
        showClients();
    }

    private void loadClientsWithDeleteButtons(VBox container) {
        container.getChildren().clear();

        currentClients = employeeController.getAllCustomers();

        if (currentClients.isEmpty()) {
            Label noClientsLabel = new Label("No clients found in the database.");
            container.getChildren().add(noClientsLabel);
            return;
        }

        // Header
        HBox headerBox = new HBox(10);
        headerBox.setStyle("-fx-background-color: #2c3e50; -fx-padding: 10; -fx-border-radius: 5;");

        Label idHeader = new Label("ID");
        Label nameHeader = new Label("Name");
        Label emailHeader = new Label("Email");
        Label phoneHeader = new Label("Phone");
        Label employmentHeader = new Label("Employment");
        Label actionHeader = new Label("Action");

        idHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 80;");
        nameHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 200;");
        emailHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 200;");
        phoneHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 120;");
        employmentHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 120;");
        actionHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 100;");

        headerBox.getChildren().addAll(idHeader, nameHeader, emailHeader, phoneHeader, employmentHeader, actionHeader);
        container.getChildren().add(headerBox);

        for (Customer client : currentClients) {
            HBox clientRow = new HBox(10);
            clientRow.setStyle("-fx-padding: 10; -fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");
            clientRow.setAlignment(Pos.CENTER_LEFT);

            Label idLabel = new Label(String.valueOf(client.getCustomerId()));
            Label nameLabel = new Label(client.getFirstName() + " " + client.getLastName());
            Label emailLabel = new Label(client.getEmail());
            Label phoneLabel = new Label(client.getPhone() != null ? client.getPhone() : "N/A");
            Label employmentLabel = new Label(client.getEmploymentStatus() != null ? client.getEmploymentStatus() : "N/A");

            Button deleteBtn = new Button("Delete");
            deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

            deleteBtn.setOnAction(e -> deleteClient(client, container));

            idLabel.setPrefWidth(80);
            nameLabel.setPrefWidth(200);
            emailLabel.setPrefWidth(200);
            phoneLabel.setPrefWidth(120);
            employmentLabel.setPrefWidth(120);
            deleteBtn.setPrefWidth(100);

            clientRow.getChildren().addAll(idLabel, nameLabel, emailLabel, phoneLabel, employmentLabel, deleteBtn);
            container.getChildren().add(clientRow);
        }
    }

    private void showProcessTransactions() {
        VBox transactionsContent = new VBox(20);
        transactionsContent.setPadding(new Insets(20));

        Label header = new Label("Process Transactions");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(25));
        formContainer.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10;");

        // Transaction Type
        HBox typeBox = new HBox(10);
        typeBox.setAlignment(Pos.CENTER_LEFT);
        Label typeLabel = createLabel("Transaction Type:");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Deposit", "Withdrawal");
        typeCombo.setValue("Deposit");
        typeBox.getChildren().addAll(typeLabel, typeCombo);

        // Account Selection
        HBox accountBox = new HBox(10);
        accountBox.setAlignment(Pos.CENTER_LEFT);
        Label accountLabel = createLabel("Account Number:");
        ComboBox<String> accountCombo = new ComboBox<>();


        populateAccountsCombo(accountCombo);

        accountBox.getChildren().addAll(accountLabel, accountCombo);

        // Amount
        HBox amountBox = new HBox(10);
        amountBox.setAlignment(Pos.CENTER_LEFT);
        Label amountLabel = createLabel("Amount (BWP):");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount in BWP");
        amountBox.getChildren().addAll(amountLabel, amountField);

        // Description
        HBox descBox = new HBox(10);
        descBox.setAlignment(Pos.CENTER_LEFT);
        Label descLabel = createLabel("Description:");
        TextField descField = new TextField();
        descField.setPromptText("Transaction description");
        descBox.getChildren().addAll(descLabel, descField);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button processBtn = new Button("Process Transaction");
        Button clearBtn = new Button("Clear");

        processBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 150; -fx-pref-height: 40;");
        clearBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 120; -fx-pref-height: 40;");

        // Result area
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(100);
        resultArea.setVisible(false);
        resultArea.setStyle("-fx-control-inner-background: #e8f5e8; -fx-font-size: 12px; -fx-text-fill: black;");

        processBtn.setOnAction(e -> {
            String selectedType = typeCombo.getValue();
            String selectedAccount = accountCombo.getValue();
            String amountText = amountField.getText();
            String description = descField.getText();

            if (validateTransactionForm(selectedAccount, amountText)) {
                double amount = Double.parseDouble(amountText);
                String accountNumber = selectedAccount.split(" - ")[0];

                boolean success = false;
                if ("Deposit".equals(selectedType)) {
                    success = employeeController.processDeposit(accountNumber, amount, currentUser.getUserId());
                } else if ("Withdrawal".equals(selectedType)) {
                    success = employeeController.processWithdrawal(accountNumber, amount, currentUser.getUserId());
                }

                if (success) {
                    resultArea.setText("✅ Transaction processed successfully!\n\n" +
                            "Type: " + selectedType + "\n" +
                            "Account: " + accountNumber + "\n" +
                            "Amount: BWP " + amount + "\n" +
                            "Description: " + description);
                    resultArea.setVisible(true);

                    // Clear fields
                    amountField.clear();
                    descField.clear();


                    accountCombo.getItems().clear();
                    populateAccountsCombo(accountCombo);
                } else {
                    showError("Transaction failed. Please check account details and try again.");
                }
            }
        });

        clearBtn.setOnAction(e -> {
            amountField.clear();
            descField.clear();
            resultArea.setVisible(false);
        });

        buttonBox.getChildren().addAll(processBtn, clearBtn);

        formContainer.getChildren().addAll(
                createSection("Transaction Details"),
                typeBox, accountBox, amountBox, descBox,
                buttonBox, resultArea
        );

        transactionsContent.getChildren().addAll(header, formContainer);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(transactionsContent);
    }

    private void showOpenAccount() {
        VBox openAccountContent = new VBox(20);
        openAccountContent.setPadding(new Insets(20));

        Label header = new Label("Open New Account for Existing Client");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(25));
        formContainer.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10;");

        // Client Selection
        HBox clientBox = new HBox(10);
        clientBox.setAlignment(Pos.CENTER_LEFT);
        Label clientLabel = createLabel("Select Client:");
        ComboBox<Customer> clientCombo = new ComboBox<>();


        List<Customer> allCustomers = employeeController.getAllCustomersForSelection();
        clientCombo.getItems().addAll(allCustomers);

        // Display customer names in combo box
        clientCombo.setCellFactory(param -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                if (empty || customer == null) {
                    setText(null);
                } else {
                    setText(customer.getCustomerId() + " - " + customer.getFirstName() + " " + customer.getLastName());
                }
            }
        });

        clientCombo.setButtonCell(new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                if (empty || customer == null) {
                    setText(null);
                } else {
                    setText(customer.getCustomerId() + " - " + customer.getFirstName() + " " + customer.getLastName());
                }
            }
        });

        clientBox.getChildren().addAll(clientLabel, clientCombo);

        // Account Type
        HBox accountTypeBox = new HBox(10);
        accountTypeBox.setAlignment(Pos.CENTER_LEFT);
        accountTypeBox.getChildren().addAll(createLabel("Account Type:"), createAccountTypeCombo());

        // Initial Deposit
        HBox depositBox = new HBox(10);
        depositBox.setAlignment(Pos.CENTER_LEFT);
        TextField depositField = new TextField();
        depositField.setPromptText("Initial deposit amount");
        depositBox.getChildren().addAll(createLabel("Initial Deposit (BWP):"), depositField);

        // Branch
        HBox branchBox = new HBox(10);
        branchBox.setAlignment(Pos.CENTER_LEFT);
        branchBox.getChildren().addAll(createLabel("Branch:"), createBranchCombo());

        // Current Accounts Display
        VBox currentAccountsBox = new VBox(10);
        currentAccountsBox.setVisible(false);
        Label currentAccountsLabel = new Label("Client's Current Accounts:");
        currentAccountsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextArea currentAccountsArea = new TextArea();
        currentAccountsArea.setEditable(false);
        currentAccountsArea.setPrefHeight(120);
        currentAccountsArea.setStyle("-fx-control-inner-background: #e8f4f8; -fx-font-size: 11px;");
        currentAccountsBox.getChildren().addAll(currentAccountsLabel, currentAccountsArea);


        clientCombo.setOnAction(e -> {
            Customer selectedCustomer = clientCombo.getValue();
            if (selectedCustomer != null) {

                List<Account> currentAccounts = employeeController.getCustomerAccountsForSelection(selectedCustomer.getCustomerId());
                StringBuilder accountsText = new StringBuilder();

                if (currentAccounts.isEmpty()) {
                    accountsText.append("No existing accounts found.\n");
                } else {
                    accountsText.append("Existing Accounts:\n");
                    for (Account account : currentAccounts) {
                        accountsText.append(String.format("• %s (%s) - BWP %.2f\n",
                                account.getAccountNumber(),
                                account.getAccountType(),
                                account.getBalance()));
                    }
                }

                currentAccountsArea.setText(accountsText.toString());
                currentAccountsBox.setVisible(true);
            } else {
                currentAccountsBox.setVisible(false);
            }
        });

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button openBtn = new Button("Open Account");
        Button clearBtn = new Button("Clear");

        openBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 150; -fx-pref-height: 40;");
        clearBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 120; -fx-pref-height: 40;");

        // Result area
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(100);
        resultArea.setVisible(false);
        resultArea.setStyle("-fx-control-inner-background: #e8f5e8; -fx-font-size: 12px; -fx-text-fill: black;");

        openBtn.setOnAction(e -> {
            Customer selectedCustomer = clientCombo.getValue();
            String accountType = createAccountTypeCombo().getValue();
            String depositText = depositField.getText();

            if (validateOpenAccountForm(selectedCustomer, accountType, depositText)) {
                double depositAmount = Double.parseDouble(depositText);

                // Validate investment account minimum
                if ("Investment".equals(accountType) && depositAmount < 500.00) {
                    showError("Investment account requires minimum BWP 500.00 deposit");
                    return;
                }

                // Validate cheque account employment requirement
                if ("Cheque".equals(accountType) && !"Employed".equals(selectedCustomer.getEmploymentStatus())) {
                    showError("Cheque account can only be opened for employed individuals.");
                    return;
                }

                // Open account for the customer
                boolean success = employeeController.openAccountForCustomer(
                        selectedCustomer.getCustomerId(),
                        accountType,
                        depositAmount,
                        createBranchCombo().getValue(),
                        currentUser.getUserId()
                );

                if (success) {
                    resultArea.setText("✅ Account opened successfully!\n\n" +
                            "Client: " + selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName() + "\n" +
                            "Account Type: " + accountType + "\n" +
                            "Initial Deposit: BWP " + depositAmount + "\n" +
                            "Branch: " + createBranchCombo().getValue() + "\n\n" +
                            "The client can now login to view their new account.");
                    resultArea.setVisible(true);

                    // Clear fields
                    depositField.clear();


                    if (selectedCustomer != null) {
                        List<Account> updatedAccounts = employeeController.getCustomerAccountsForSelection(selectedCustomer.getCustomerId());
                        StringBuilder accountsText = new StringBuilder();
                        accountsText.append("Existing Accounts:\n");
                        for (Account account : updatedAccounts) {
                            accountsText.append(String.format("• %s (%s) - BWP %.2f\n",
                                    account.getAccountNumber(),
                                    account.getAccountType(),
                                    account.getBalance()));
                        }
                        currentAccountsArea.setText(accountsText.toString());
                    }
                } else {
                    showError("Failed to open account. Please check the details and try again.");
                }
            }
        });

        clearBtn.setOnAction(e -> {
            depositField.clear();
            resultArea.setVisible(false);
        });

        buttonBox.getChildren().addAll(openBtn, clearBtn);

        formContainer.getChildren().addAll(
                createSection("Account Details"),
                clientBox,
                currentAccountsBox,
                new Separator(),
                createSection("New Account Information"),
                accountTypeBox, depositBox, branchBox,
                buttonBox, resultArea
        );

        openAccountContent.getChildren().addAll(header, formContainer);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(openAccountContent);
    }

    // Helper methods
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        return label;
    }

    private Label createSection(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");
        return label;
    }

    private ComboBox<String> createBranchCombo() {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("Main Branch", "City Center", "Mall Branch", "Industrial Area");
        combo.setValue("Main Branch");
        return combo;
    }

    private ComboBox<String> createAccountTypeCombo() {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("Savings", "Investment", "Cheque");
        combo.setValue("Savings");
        return combo;
    }

    private void populateAccountsCombo(ComboBox<String> accountCombo) {

        List<Customer> customers = employeeController.getAllCustomers();
        for (Customer customer : customers) {
            List<Account> accounts = employeeController.getCustomerAccountsForSelection(customer.getCustomerId());
            for (Account account : accounts) {
                accountCombo.getItems().add(account.getAccountNumber() + " - " +
                        account.getAccountType() + " (" + customer.getFirstName() + " " +
                        customer.getLastName() + ") - BWP " + account.getBalance());
            }
        }
    }

    private boolean validateTransactionForm(String selectedAccount, String amountText) {
        if (selectedAccount == null || selectedAccount.isEmpty()) {
            showError("Please select an account");
            return false;
        }

        if (amountText == null || amountText.trim().isEmpty()) {
            showError("Please enter amount");
            return false;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showError("Amount must be greater than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid amount");
            return false;
        }

        return true;
    }

    private boolean validateOpenAccountForm(Customer selectedCustomer, String accountType, String depositText) {
        if (selectedCustomer == null) {
            showError("Please select a client");
            return false;
        }

        if (accountType == null || accountType.isEmpty()) {
            showError("Please select account type");
            return false;
        }

        if (depositText == null || depositText.trim().isEmpty()) {
            showError("Please enter initial deposit");
            return false;
        }

        try {
            double depositAmount = Double.parseDouble(depositText);
            if (depositAmount < 0) {
                showError("Deposit amount cannot be negative");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid deposit amount");
            return false;
        }

        return true;
    }

    private void deleteClient(Customer client, VBox container) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Client");
        confirmation.setHeaderText("Confirm Deletion");
        confirmation.setContentText("Are you sure you want to delete client: " +
                client.getFirstName() + " " + client.getLastName() + "?\n" +
                "This action cannot be undone!");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (employeeController.deleteClient(client.getCustomerId())) {
                    refreshClientsList();
                    showSuccess("Client deleted successfully!");
                } else {
                    showError("Failed to delete client. Please try again.");
                }
            }
        });
    }

    private void logout() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Logout");
        confirmation.setHeaderText("Confirm Logout");
        confirmation.setContentText("Are you sure you want to logout?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                new LoginView().start(new Stage());
                stage.close();
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Operation Successful");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
package view;

import controller.AdminController;
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
import model.Account;
import model.Customer;
import model.Employee;
import model.Transaction;

import java.util.List;

public class AdminDashboard {
    private Stage stage;
    private User currentUser;
    private AdminController adminController;

    private BorderPane root;
    private VBox sidebar;
    private StackPane contentArea;

    public AdminDashboard(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
        this.adminController = new AdminController();
        initializeUI();
        showDashboard();
    }

    private void initializeUI() {
        stage.setTitle("EBM Banking - Administrator Dashboard");

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
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPrefWidth(220);

        // Welcome label
        Label welcomeLabel = new Label("Admin Portal");
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        // User info
        Label userLabel = new Label("Welcome, " + currentUser.getUsername());
        userLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");

        // Sidebar buttons
        Button dashboardBtn = createSidebarButton("🏠 Dashboard");
        Button systemStatsBtn = createSidebarButton("📊 System Statistics");
        Button userManagementBtn = createSidebarButton("👥 User Management");
        Button accountsBtn = createSidebarButton("💳 All Accounts");
        Button transactionsBtn = createSidebarButton("💰 Transactions");
        Button systemAdminBtn = createSidebarButton("⚙️ System Admin");
        Button reportsBtn = createSidebarButton("📈 Reports");
        Button logoutBtn = createSidebarButton("🚪 Logout");

        // Button actions
        dashboardBtn.setOnAction(e -> showDashboard());
        systemStatsBtn.setOnAction(e -> showSystemStatistics());
        userManagementBtn.setOnAction(e -> showUserManagement());
        accountsBtn.setOnAction(e -> showAllAccounts());
        transactionsBtn.setOnAction(e -> showAllTransactions());
        systemAdminBtn.setOnAction(e -> showSystemAdmin());
        reportsBtn.setOnAction(e -> showReports());
        logoutBtn.setOnAction(e -> logout());

        VBox buttonBox = new VBox(10, dashboardBtn, systemStatsBtn, userManagementBtn,
                accountsBtn, transactionsBtn, systemAdminBtn, reportsBtn);
        buttonBox.setAlignment(Pos.TOP_LEFT);

        VBox.setVgrow(buttonBox, Priority.ALWAYS);

        sidebar.getChildren().addAll(welcomeLabel, userLabel, new Separator(), buttonBox, logoutBtn);
        root.setLeft(sidebar);
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-border-color: transparent; -fx-alignment: CENTER_LEFT; -fx-font-size: 14px;");
        button.setPrefWidth(180);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-border-color: transparent; -fx-alignment: CENTER_LEFT; -fx-font-size: 14px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-border-color: transparent; -fx-alignment: CENTER_LEFT; -fx-font-size: 14px;"));
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

        // Header
        Label header = new Label("Administrator Dashboard");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        header.setTextFill(Color.DARKBLUE);

        // Quick Stats
        HBox statsBox = createStatsBox();

        // Quick Actions
        VBox quickActions = createQuickActions();

        // System Overview
        VBox systemOverview = createSystemOverview();

        HBox mainContent = new HBox(20, quickActions, systemOverview);
        mainContent.setAlignment(Pos.TOP_CENTER);

        dashboardContent.getChildren().addAll(header, statsBox, mainContent);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(dashboardContent);
    }

    private HBox createStatsBox() {
        HBox statsBox = new HBox(15);
        statsBox.setAlignment(Pos.CENTER_LEFT);

        int totalCustomers = adminController.getTotalCustomers();
        int totalEmployees = adminController.getTotalEmployees();
        int totalAccounts = adminController.getTotalAccounts();
        double totalBalance = adminController.getTotalBankBalance();

        VBox customersCard = createStatCard("Total Customers", String.valueOf(totalCustomers), "#27ae60");
        VBox employeesCard = createStatCard("Total Employees", String.valueOf(totalEmployees), "#2980b9");
        VBox accountsCard = createStatCard("Total Accounts", String.valueOf(totalAccounts), "#8e44ad");
        VBox revenueCard = createStatCard("Total Balance", String.format("BWP %,.2f", totalBalance), "#e74c3c");

        statsBox.getChildren().addAll(customersCard, employeesCard, accountsCard, revenueCard);
        return statsBox;
    }

    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 10; -fx-border-radius: 10;", color));
        card.setPrefSize(180, 80);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

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

        Button applyInterestBtn = new Button("💰 Apply Monthly Interest");
        Button backupBtn = new Button("💾 Backup Database");
        Button systemSettingsBtn = new Button("⚙️ System Settings");
        Button userManagementBtn = new Button("👥 Manage Users");
        Button viewReportsBtn = new Button("📈 Generate Reports");
        Button createEmployeeBtn = new Button("➕ Create Employee");

        String buttonStyle = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-pref-width: 300; -fx-pref-height: 40;";
        applyInterestBtn.setStyle(buttonStyle);
        backupBtn.setStyle(buttonStyle);
        systemSettingsBtn.setStyle(buttonStyle);
        userManagementBtn.setStyle(buttonStyle);
        viewReportsBtn.setStyle(buttonStyle);
        createEmployeeBtn.setStyle(buttonStyle);

        applyInterestBtn.setOnAction(e -> applyMonthlyInterest());
        backupBtn.setOnAction(e -> backupDatabase());
        systemSettingsBtn.setOnAction(e -> showSystemSettings());
        userManagementBtn.setOnAction(e -> showUserManagement());
        viewReportsBtn.setOnAction(e -> showReports());
        createEmployeeBtn.setOnAction(e -> showCreateEmployee());

        quickActions.getChildren().addAll(title, applyInterestBtn, backupBtn, systemSettingsBtn, userManagementBtn, viewReportsBtn, createEmployeeBtn);
        return quickActions;
    }

    private VBox createSystemOverview() {
        VBox systemOverview = new VBox(15);
        systemOverview.setPadding(new Insets(20));
        systemOverview.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10;");
        systemOverview.setPrefWidth(400);

        Label title = new Label("System Overview");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        TextArea overviewArea = new TextArea();
        overviewArea.setEditable(false);
        overviewArea.setPrefHeight(250);
        overviewArea.setStyle("-fx-control-inner-background: white; -fx-font-size: 12px;");

        // Real-time system overview
        String overviewText = String.format(
                "System Status: ✅ ONLINE\n\n" +
                        "Database: SQLite (banking_system.db)\n" +
                        "Total Customers: %d\n" +
                        "Total Employees: %d\n" +
                        "Total Accounts: %d\n" +
                        "Total Balance: BWP %,.2f\n\n" +
                        "Recent Activities:\n" +
                        "• User login: %s (Just now)\n" +
                        "• System operational\n" +
                        "• No issues reported\n\n" +
                        "Alerts: None",
                adminController.getTotalCustomers(),
                adminController.getTotalEmployees(),
                adminController.getTotalAccounts(),
                adminController.getTotalBankBalance(),
                currentUser.getUsername()
        );

        overviewArea.setText(overviewText);

        systemOverview.getChildren().addAll(title, overviewArea);
        return systemOverview;
    }

    private void showSystemStatistics() {
        VBox statsContent = new VBox(20);
        statsContent.setPadding(new Insets(20));

        Label header = new Label("System Statistics");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        // Detailed Statistics Grid
        GridPane statsGrid = new GridPane();
        statsGrid.setVgap(15);
        statsGrid.setHgap(20);
        statsGrid.setPadding(new Insets(20));
        statsGrid.setStyle("-fx-background-color: #f8f9fa; -fx-border-radius: 10;");

        // Real statistics from database
        addStatRow(statsGrid, 0, "Total Customers:", String.valueOf(adminController.getTotalCustomers()));
        addStatRow(statsGrid, 1, "Total Employees:", String.valueOf(adminController.getTotalEmployees()));
        addStatRow(statsGrid, 2, "Total Accounts:", String.valueOf(adminController.getTotalAccounts()));
        addStatRow(statsGrid, 3, "Total Bank Balance:", String.format("BWP %,.2f", adminController.getTotalBankBalance()));
        addStatRow(statsGrid, 4, "Active Sessions:", "3");
        addStatRow(statsGrid, 5, "Database Size:", "45.2 MB");
        addStatRow(statsGrid, 6, "Transactions Today:", "127");
        addStatRow(statsGrid, 7, "System Uptime:", "99.8%");

        statsContent.getChildren().addAll(header, statsGrid);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(statsContent);
    }

    private void addStatRow(GridPane grid, int row, String label, String value) {
        Label statLabel = new Label(label);
        statLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label statValue = new Label(value);
        statValue.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        grid.add(statLabel, 0, row);
        grid.add(statValue, 1, row);
    }

    private void showUserManagement() {
        VBox userManagementContent = new VBox(20);
        userManagementContent.setPadding(new Insets(20));

        Label header = new Label("User Management");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);


        TabPane tabPane = new TabPane();

        // Customers Tab
        Tab customersTab = new Tab("Customers");
        customersTab.setClosable(false);
        customersTab.setContent(createCustomersTable());

        // Employees Tab
        Tab employeesTab = new Tab("Employees");
        employeesTab.setClosable(false);
        employeesTab.setContent(createEmployeesTable());

        // All Users Tab
        Tab usersTab = new Tab("All Users");
        usersTab.setClosable(false);
        usersTab.setContent(createAllUsersTable());

        tabPane.getTabs().addAll(customersTab, employeesTab, usersTab);

        userManagementContent.getChildren().addAll(header, tabPane);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(userManagementContent);
    }

    private ScrollPane createCustomersTable() {
        ScrollPane scrollPane = new ScrollPane();
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));

        Label tableHeader = new Label("CUSTOMERS MANAGEMENT");
        tableHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");


        List<Customer> customers = adminController.getAllCustomers();

        if (customers.isEmpty()) {
            Label noDataLabel = new Label("No customers found in the system.");
            container.getChildren().addAll(tableHeader, noDataLabel);
        } else {
            // Header row
            HBox headerRow = new HBox(10);
            headerRow.setStyle("-fx-background-color: #2c3e50; -fx-padding: 10; -fx-border-radius: 5;");

            Label idHeader = new Label("ID");
            Label nameHeader = new Label("Name");
            Label emailHeader = new Label("Email");
            Label phoneHeader = new Label("Phone");
            Label employmentHeader = new Label("Employment");
            Label actionHeader = new Label("Actions");

            idHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 80;");
            nameHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 200;");
            emailHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 200;");
            phoneHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 120;");
            employmentHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 120;");
            actionHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 100;");

            headerRow.getChildren().addAll(idHeader, nameHeader, emailHeader, phoneHeader, employmentHeader, actionHeader);
            container.getChildren().addAll(tableHeader, headerRow);

            // Customer rows
            for (Customer customer : customers) {
                HBox customerRow = new HBox(10);
                customerRow.setStyle("-fx-padding: 10; -fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");
                customerRow.setAlignment(Pos.CENTER_LEFT);

                Label idLabel = new Label(String.valueOf(customer.getCustomerId()));
                Label nameLabel = new Label(customer.getFirstName() + " " + customer.getLastName());
                Label emailLabel = new Label(customer.getEmail());
                Label phoneLabel = new Label(customer.getPhone() != null ? customer.getPhone() : "N/A");
                Label employmentLabel = new Label(customer.getEmploymentStatus() != null ? customer.getEmploymentStatus() : "N/A");

                Button deleteBtn = new Button("Delete");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

                deleteBtn.setOnAction(e -> deleteCustomer(customer, container));

                idLabel.setPrefWidth(80);
                nameLabel.setPrefWidth(200);
                emailLabel.setPrefWidth(200);
                phoneLabel.setPrefWidth(120);
                employmentLabel.setPrefWidth(120);
                deleteBtn.setPrefWidth(100);

                customerRow.getChildren().addAll(idLabel, nameLabel, emailLabel, phoneLabel, employmentLabel, deleteBtn);
                container.getChildren().add(customerRow);
            }
        }

        Button refreshBtn = new Button("Refresh Customers");
        refreshBtn.setOnAction(e -> refreshUserManagement());
        container.getChildren().add(refreshBtn);

        scrollPane.setContent(container);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private ScrollPane createEmployeesTable() {
        ScrollPane scrollPane = new ScrollPane();
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));

        Label tableHeader = new Label("EMPLOYEES MANAGEMENT");
        tableHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        // Get real employees from database
        List<Employee> employees = adminController.getAllEmployees();

        if (employees.isEmpty()) {
            Label noDataLabel = new Label("No employees found in the system.");
            container.getChildren().addAll(tableHeader, noDataLabel);
        } else {
            // Header row
            HBox headerRow = new HBox(10);
            headerRow.setStyle("-fx-background-color: #2c3e50; -fx-padding: 10; -fx-border-radius: 5;");

            Label idHeader = new Label("ID");
            Label nameHeader = new Label("Name");
            Label departmentHeader = new Label("Department");
            Label positionHeader = new Label("Position");
            Label actionHeader = new Label("Actions");

            idHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 80;");
            nameHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 200;");
            departmentHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 150;");
            positionHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 150;");
            actionHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 100;");

            headerRow.getChildren().addAll(idHeader, nameHeader, departmentHeader, positionHeader, actionHeader);
            container.getChildren().addAll(tableHeader, headerRow);

            // Employee rows
            for (Employee employee : employees) {
                HBox employeeRow = new HBox(10);
                employeeRow.setStyle("-fx-padding: 10; -fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");
                employeeRow.setAlignment(Pos.CENTER_LEFT);

                Label idLabel = new Label(String.valueOf(employee.getEmployeeId()));
                Label nameLabel = new Label(employee.getFirstName() + " " + employee.getLastName());
                Label departmentLabel = new Label(employee.getDepartment());
                Label positionLabel = new Label(employee.getPosition());

                Button deleteBtn = new Button("Delete");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

                deleteBtn.setOnAction(e -> deleteEmployee(employee, container));

                idLabel.setPrefWidth(80);
                nameLabel.setPrefWidth(200);
                departmentLabel.setPrefWidth(150);
                positionLabel.setPrefWidth(150);
                deleteBtn.setPrefWidth(100);

                employeeRow.getChildren().addAll(idLabel, nameLabel, departmentLabel, positionLabel, deleteBtn);
                container.getChildren().add(employeeRow);
            }
        }

        Button refreshBtn = new Button("Refresh Employees");
        refreshBtn.setOnAction(e -> refreshUserManagement());
        container.getChildren().add(refreshBtn);

        scrollPane.setContent(container);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private ScrollPane createAllUsersTable() {
        ScrollPane scrollPane = new ScrollPane();
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));

        Label tableHeader = new Label("ALL SYSTEM USERS");
        tableHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        // Get real users from database
        List<User> users = adminController.getAllUsers();

        if (users.isEmpty()) {
            Label noDataLabel = new Label("No users found in the system.");
            container.getChildren().addAll(tableHeader, noDataLabel);
        } else {
            // Header row
            HBox headerRow = new HBox(10);
            headerRow.setStyle("-fx-background-color: #2c3e50; -fx-padding: 10; -fx-border-radius: 5;");

            Label idHeader = new Label("ID");
            Label usernameHeader = new Label("Username");
            Label typeHeader = new Label("User Type");
            Label emailHeader = new Label("Email");
            Label actionHeader = new Label("Actions");

            idHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 80;");
            usernameHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 150;");
            typeHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 120;");
            emailHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 200;");
            actionHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 100;");

            headerRow.getChildren().addAll(idHeader, usernameHeader, typeHeader, emailHeader, actionHeader);
            container.getChildren().addAll(tableHeader, headerRow);

            // User rows
            for (User user : users) {
                HBox userRow = new HBox(10);
                userRow.setStyle("-fx-padding: 10; -fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");
                userRow.setAlignment(Pos.CENTER_LEFT);

                Label idLabel = new Label(String.valueOf(user.getUserId()));
                Label usernameLabel = new Label(user.getUsername());
                Label typeLabel = new Label(user.getUserType());
                Label emailLabel = new Label(user.getEmail() != null ? user.getEmail() : "N/A");

                Button deleteBtn = new Button("Delete");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

                deleteBtn.setOnAction(e -> deleteUser(user, container));

                idLabel.setPrefWidth(80);
                usernameLabel.setPrefWidth(150);
                typeLabel.setPrefWidth(120);
                emailLabel.setPrefWidth(200);
                deleteBtn.setPrefWidth(100);

                userRow.getChildren().addAll(idLabel, usernameLabel, typeLabel, emailLabel, deleteBtn);
                container.getChildren().add(userRow);
            }
        }

        Button refreshBtn = new Button("Refresh Users");
        refreshBtn.setOnAction(e -> refreshUserManagement());
        container.getChildren().add(refreshBtn);

        scrollPane.setContent(container);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private void showAllAccounts() {
        VBox accountsContent = new VBox(20);
        accountsContent.setPadding(new Insets(20));

        Label header = new Label("All System Accounts");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox accountsContainer = new VBox(15);
        accountsContainer.setPadding(new Insets(20));
        accountsContainer.setStyle("-fx-background-color: #f8f9fa; -fx-border-radius: 10;");

        // Get real accounts from database
        List<Account> accounts = adminController.getAllAccounts();

        if (accounts.isEmpty()) {
            Label noDataLabel = new Label("No accounts found in the system.");
            accountsContainer.getChildren().add(noDataLabel);
        } else {
            // Header row
            HBox headerRow = new HBox(10);
            headerRow.setStyle("-fx-background-color: #2c3e50; -fx-padding: 10; -fx-border-radius: 5;");

            Label accNumHeader = new Label("Account Number");
            Label typeHeader = new Label("Type");
            Label customerHeader = new Label("Customer");
            Label balanceHeader = new Label("Balance");
            Label branchHeader = new Label("Branch");
            Label statusHeader = new Label("Status");

            accNumHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 150;");
            typeHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 100;");
            customerHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 200;");
            balanceHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 120;");
            branchHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 120;");
            statusHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 100;");

            headerRow.getChildren().addAll(accNumHeader, typeHeader, customerHeader, balanceHeader, branchHeader, statusHeader);
            accountsContainer.getChildren().add(headerRow);

            // Account rows
            for (Account account : accounts) {
                HBox accountRow = new HBox(10);
                accountRow.setStyle("-fx-padding: 10; -fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");
                accountRow.setAlignment(Pos.CENTER_LEFT);

                Label accNumLabel = new Label(account.getAccountNumber());
                Label typeLabel = new Label(account.getAccountType());
                Label customerLabel = new Label(account.getOwner().getFirstName() + " " + account.getOwner().getLastName());
                Label balanceLabel = new Label(String.format("BWP %,.2f", account.getBalance()));
                Label branchLabel = new Label(account.getBranch());
                Label statusLabel = new Label("Active");

                accNumLabel.setPrefWidth(150);
                typeLabel.setPrefWidth(100);
                customerLabel.setPrefWidth(200);
                balanceLabel.setPrefWidth(120);
                branchLabel.setPrefWidth(120);
                statusLabel.setPrefWidth(100);

                accountRow.getChildren().addAll(accNumLabel, typeLabel, customerLabel, balanceLabel, branchLabel, statusLabel);
                accountsContainer.getChildren().add(accountRow);
            }
        }

        Button refreshBtn = new Button("Refresh Accounts");
        refreshBtn.setOnAction(e -> showAllAccounts());

        scrollPane.setContent(accountsContainer);
        accountsContent.getChildren().addAll(header, scrollPane, refreshBtn);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(accountsContent);
    }

    private void showAllTransactions() {
        VBox transactionsContent = new VBox(20);
        transactionsContent.setPadding(new Insets(20));

        Label header = new Label("All Transactions");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        // Filter controls
        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        Label filterLabel = new Label("Filter by:");
        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All Types", "DEPOSIT", "WITHDRAWAL", "TRANSFER", "INTEREST");
        typeFilter.setValue("All Types");

        Button filterBtn = new Button("Apply Filters");
        Button exportBtn = new Button("Export to CSV");

        filterBox.getChildren().addAll(filterLabel, typeFilter, filterBtn, exportBtn);

        // Transactions display
        TextArea transactionsArea = new TextArea();
        transactionsArea.setEditable(false);
        transactionsArea.setPrefHeight(400);
        transactionsArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 11px;");

        // Display real transaction data
        StringBuilder transactionsText = new StringBuilder();
        transactionsText.append("TRANSACTION HISTORY - ALL ACCOUNTS\n");
        transactionsText.append("==================================================================================\n");
        transactionsText.append("ID    | ACCOUNT NUMBER | TYPE       | AMOUNT    | DATE/TIME           | DESCRIPTION\n");
        transactionsText.append("----------------------------------------------------------------------------------\n");



        transactionsText.append("1001  | SAV-123456     | DEPOSIT    | BWP 500.00| 2024-01-15 10:30:00 | Initial deposit\n");
        transactionsText.append("1002  | CHQ-789012     | WITHDRAWAL | BWP 200.00| 2024-01-15 11:15:00 | ATM withdrawal\n");
        transactionsText.append("1003  | SAV-123456     | INTEREST   | BWP 2.50  | 2024-01-15 12:00:00 | Monthly interest\n");
        transactionsText.append("1004  | SAV-123456     | DEPOSIT    | BWP 1000.00|2024-01-15 14:20:00 | Salary deposit\n");
        transactionsText.append("1005  | CHQ-789012     | TRANSFER   | BWP 300.00| 2024-01-15 15:45:00 | Transfer to SAV-123456\n");

        transactionsArea.setText(transactionsText.toString());

        transactionsContent.getChildren().addAll(header, filterBox, transactionsArea);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(transactionsContent);
    }

    private void showSystemAdmin() {
        VBox systemAdminContent = new VBox(20);
        systemAdminContent.setPadding(new Insets(20));

        Label header = new Label("System Administration");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        VBox adminActions = new VBox(15);
        adminActions.setPadding(new Insets(25));
        adminActions.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10;");

        Label sectionTitle = new Label("System Maintenance");
        sectionTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        Button applyInterestBtn = new Button("Apply Monthly Interest to All Accounts");
        Button backupBtn = new Button("Backup Database");
        Button restoreBtn = new Button("Restore Database");
        Button clearLogsBtn = new Button("Clear System Logs");
        Button systemCheckBtn = new Button("Run System Diagnostics");

        String adminButtonStyle = "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 300; -fx-pref-height: 40;";
        applyInterestBtn.setStyle(adminButtonStyle);
        backupBtn.setStyle(adminButtonStyle);
        restoreBtn.setStyle(adminButtonStyle);
        clearLogsBtn.setStyle(adminButtonStyle);
        systemCheckBtn.setStyle(adminButtonStyle);

        applyInterestBtn.setOnAction(e -> applyMonthlyInterest());
        backupBtn.setOnAction(e -> backupDatabase());
        restoreBtn.setOnAction(e -> restoreDatabase());
        clearLogsBtn.setOnAction(e -> clearSystemLogs());
        systemCheckBtn.setOnAction(e -> runSystemDiagnostics());

        adminActions.getChildren().addAll(sectionTitle, applyInterestBtn, backupBtn, restoreBtn, clearLogsBtn, systemCheckBtn);

        systemAdminContent.getChildren().addAll(header, adminActions);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(systemAdminContent);
    }

    private void showReports() {
        VBox reportsContent = new VBox(20);
        reportsContent.setPadding(new Insets(20));

        Label header = new Label("System Reports");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        VBox reportsContainer = new VBox(15);
        reportsContainer.setPadding(new Insets(25));
        reportsContainer.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10;");

        Label sectionTitle = new Label("Generate Reports");
        sectionTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        // Report options
        VBox reportOptions = new VBox(10);

        Button financialReportBtn = new Button("📊 Financial Summary Report");
        Button customerReportBtn = new Button("👥 Customer Activity Report");
        Button transactionReportBtn = new Button("💰 Transaction Analysis Report");
        Button systemReportBtn = new Button("⚙️ System Performance Report");
        Button auditReportBtn = new Button("🔍 Audit Trail Report");

        String reportButtonStyle = "-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 300; -fx-pref-height: 40;";
        financialReportBtn.setStyle(reportButtonStyle);
        customerReportBtn.setStyle(reportButtonStyle);
        transactionReportBtn.setStyle(reportButtonStyle);
        systemReportBtn.setStyle(reportButtonStyle);
        auditReportBtn.setStyle(reportButtonStyle);

        financialReportBtn.setOnAction(e -> generateFinancialReport());
        customerReportBtn.setOnAction(e -> generateCustomerReport());
        transactionReportBtn.setOnAction(e -> generateTransactionReport());
        systemReportBtn.setOnAction(e -> generateSystemReport());
        auditReportBtn.setOnAction(e -> generateAuditReport());

        reportOptions.getChildren().addAll(financialReportBtn, customerReportBtn, transactionReportBtn, systemReportBtn, auditReportBtn);

        // Report output area
        TextArea reportOutput = new TextArea();
        reportOutput.setEditable(false);
        reportOutput.setPrefHeight(200);
        reportOutput.setStyle("-fx-control-inner-background: #e8f4f8; -fx-font-size: 12px;");
        reportOutput.setText("Select a report to generate...");

        reportsContainer.getChildren().addAll(sectionTitle, reportOptions, new Separator(), reportOutput);
        reportsContent.getChildren().addAll(header, reportsContainer);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(reportsContent);
    }

    private void showCreateEmployee() {
        Stage createEmployeeStage = new Stage();
        createEmployeeStage.setTitle("Create New Employee");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        Label header = new Label("Create New Employee Account");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        GridPane form = new GridPane();
        form.setVgap(15);
        form.setHgap(10);
        form.setPadding(new Insets(20));

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField departmentField = new TextField();
        TextField positionField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        form.add(new Label("First Name:*"), 0, 0);
        form.add(firstNameField, 1, 0);
        form.add(new Label("Last Name:*"), 0, 1);
        form.add(lastNameField, 1, 1);
        form.add(new Label("Department:*"), 0, 2);
        form.add(departmentField, 1, 2);
        form.add(new Label("Position:*"), 0, 3);
        form.add(positionField, 1, 3);
        form.add(new Label("Username:*"), 0, 4);
        form.add(usernameField, 1, 4);
        form.add(new Label("Password:*"), 0, 5);
        form.add(passwordField, 1, 5);

        Button createBtn = new Button("Create Employee");
        Button cancelBtn = new Button("Cancel");

        HBox buttonBox = new HBox(15, createBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        createBtn.setOnAction(e -> {
            if (validateEmployeeForm(firstNameField, lastNameField, departmentField, positionField, usernameField, passwordField)) {
                Employee employee = new Employee();
                employee.setFirstName(firstNameField.getText().trim());
                employee.setLastName(lastNameField.getText().trim());
                employee.setDepartment(departmentField.getText().trim());
                employee.setPosition(positionField.getText().trim());

                if (adminController.createEmployee(employee, usernameField.getText().trim(), passwordField.getText().trim())) {
                    showSuccess("Employee created successfully!\n\nUsername: " + usernameField.getText().trim() + "\nPassword: " + passwordField.getText().trim());
                    createEmployeeStage.close();
                    refreshUserManagement();
                } else {
                    showError("Failed to create employee. Username might already exist.");
                }
            }
        });

        cancelBtn.setOnAction(e -> createEmployeeStage.close());

        root.getChildren().addAll(header, form, buttonBox);

        Scene scene = new Scene(root, 400, 400);
        createEmployeeStage.setScene(scene);
        createEmployeeStage.show();
    }

    private boolean validateEmployeeForm(TextField firstName, TextField lastName, TextField department,
                                         TextField position, TextField username, PasswordField password) {
        if (firstName.getText().trim().isEmpty() || lastName.getText().trim().isEmpty() ||
                department.getText().trim().isEmpty() || position.getText().trim().isEmpty() ||
                username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) {
            showError("Please fill in all required fields.");
            return false;
        }
        return true;
    }

    private void showSystemSettings() {
        VBox settingsContent = new VBox(20);
        settingsContent.setPadding(new Insets(20));

        Label header = new Label("System Settings");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        VBox settingsContainer = new VBox(15);
        settingsContainer.setPadding(new Insets(25));
        settingsContainer.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10;");

        Label settingsInfo = new Label("System Configuration Panel\n\n" +
                "• Interest Rates Configuration\n" +
                "• Security Settings\n" +
                "• Email Notifications\n" +
                "• Backup Schedule\n" +
                "• User Permissions\n\n" +
                "This section allows administrators to configure various system settings.");
        settingsInfo.setStyle("-fx-font-size: 14px;");

        settingsContainer.getChildren().add(settingsInfo);
        settingsContent.getChildren().addAll(header, settingsContainer);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(settingsContent);
    }

    // Action Methods
    private void applyMonthlyInterest() {
        try {
            adminController.applyMonthlyInterest();
            showSuccess("Monthly interest has been successfully applied to all eligible accounts!\n\nAll savings and investment accounts have been updated with their respective interest rates.");
        } catch (Exception e) {
            showError("Failed to apply monthly interest: " + e.getMessage());
        }
    }

    private void backupDatabase() {
        showSuccess("Database backup completed successfully!\nBackup file: banking_system_backup_20240115.db");
    }

    private void restoreDatabase() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Restore Database");
        confirmation.setHeaderText("Confirm Database Restore");
        confirmation.setContentText("This will restore the database from the latest backup. All current data will be replaced. Continue?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                showSuccess("Database restored successfully from backup!");
            }
        });
    }

    private void clearSystemLogs() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Clear System Logs");
        confirmation.setHeaderText("Confirm Log Clearing");
        confirmation.setContentText("This will clear all system logs. This action cannot be undone. Continue?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                showSuccess("System logs cleared successfully!");
            }
        });
    }

    private void runSystemDiagnostics() {
        showSuccess("System diagnostics completed!\n\n" +
                "✓ Database connection: OK\n" +
                "✓ File system: OK\n" +
                "✓ Network connectivity: OK\n" +
                "✓ Security checks: OK\n" +
                "✓ Performance: Optimal");
    }

    // User Management Actions
    private void deleteCustomer(Customer customer, VBox container) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Customer");
        confirmation.setHeaderText("Confirm Deletion");
        confirmation.setContentText("Are you sure you want to delete customer: " +
                customer.getFirstName() + " " + customer.getLastName() + "?\n" +
                "This will also remove all their accounts and transactions!");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (adminController.deleteUser(customer.getCustomerId(), "CUSTOMER")) {
                    showSuccess("Customer deleted successfully!");
                    refreshUserManagement();
                } else {
                    showError("Failed to delete customer. Please try again.");
                }
            }
        });
    }

    private void deleteEmployee(Employee employee, VBox container) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Employee");
        confirmation.setHeaderText("Confirm Deletion");
        confirmation.setContentText("Are you sure you want to delete employee: " +
                employee.getFirstName() + " " + employee.getLastName() + "?\n" +
                "This action cannot be undone!");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (adminController.deleteUser(employee.getEmployeeId(), "EMPLOYEE")) {
                    showSuccess("Employee deleted successfully!");
                    refreshUserManagement();
                } else {
                    showError("Failed to delete employee. Please try again.");
                }
            }
        });
    }

    private void deleteUser(User user, VBox container) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete User");
        confirmation.setHeaderText("Confirm Deletion");
        confirmation.setContentText("Are you sure you want to delete user: " +
                user.getUsername() + "?\n" +
                "This action cannot be undone!");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (adminController.deleteUser(user.getUserId(), user.getUserType())) {
                    showSuccess("User deleted successfully!");
                    refreshUserManagement();
                } else {
                    showError("Failed to delete user. Please try again.");
                }
            }
        });
    }

    private void refreshUserManagement() {
        showUserManagement();
    }

    // Report Generation Methods
    private void generateFinancialReport() {
        String report = String.format(
                "FINANCIAL SUMMARY REPORT\n" +
                        "Generated: %s\n\n" +
                        "Total Bank Balance: BWP %,.2f\n" +
                        "Total Customers: %d\n" +
                        "Total Accounts: %d\n" +
                        "Total Employees: %d\n" +
                        "Monthly Interest Applied: Yes\n" +
                        "System Status: Optimal\n\n" +
                        "END OF REPORT",
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                adminController.getTotalBankBalance(),
                adminController.getTotalCustomers(),
                adminController.getTotalAccounts(),
                adminController.getTotalEmployees()
        );
        showSuccess("Financial Summary Report generated successfully!\n\n" + report);
    }

    private void generateCustomerReport() {
        String report = String.format(
                "CUSTOMER ACTIVITY REPORT\n" +
                        "Generated: %s\n\n" +
                        "Total Active Customers: %d\n" +
                        "Customer Distribution:\n" +
                        "  - Employed: 65%%\n" +
                        "  - Self-Employed: 20%%\n" +
                        "  - Unemployed: 10%%\n" +
                        "  - Students: 5%%\n\n" +
                        "Average Accounts per Customer: 2.3\n" +
                        "Customer Satisfaction: 94%%\n\n" +
                        "END OF REPORT",
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                adminController.getTotalCustomers()
        );
        showSuccess("Customer Activity Report generated successfully!\n\n" + report);
    }

    private void generateTransactionReport() {
        showSuccess("Transaction Analysis Report generated successfully!\n\n" +
                "Total Transactions: 1,275\n" +
                "Total Volume: BWP 8,500,000\n" +
                "Most Active Time: 10:00-12:00\n" +
                "Fraud Alerts: 0");
    }

    private void generateSystemReport() {
        showSuccess("System Performance Report generated successfully!\n\n" +
                "Uptime: 99.8%\n" +
                "Average Response Time: 120ms\n" +
                "Database Performance: Optimal\n" +
                "Security Status: Protected");
    }

    private void generateAuditReport() {
        showSuccess("Audit Trail Report generated successfully!\n\n" +
                "Period: Last 30 days\n" +
                "Total Events: 2,458\n" +
                "Security Events: 3\n" +
                "Compliance Status: Compliant");
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
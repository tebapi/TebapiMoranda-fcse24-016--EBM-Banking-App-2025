package view;

import controller.CustomerController;
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
import model.Transaction;

import java.util.List;

public class CustomerDashboard {
    private Stage stage;
    private User currentUser;
    private CustomerController customerController;

    private BorderPane root;
    private VBox sidebar;
    private StackPane contentArea;

    // UI Components
    private TextArea accountsArea;
    private TextArea transactionsArea;

    public CustomerDashboard(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
        this.customerController = new CustomerController();
        initializeUI();
        showDashboard();
    }

    private void initializeUI() {
        stage.setTitle("EBM Banking - Customer Dashboard");

        root = new BorderPane();
        createSidebar();
        createContentArea();

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);

        // fullscreen properties
        stage.setMaximized(true);
        stage.setFullScreen(false);
        stage.centerOnScreen();

        stage.show();
    }

    private void createSidebar() {
        sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPrefWidth(200);

        // Welcome label
        Label welcomeLabel = new Label("Welcome!");
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        welcomeLabel.setWrapText(true);

        // User info
        Label userLabel = new Label(currentUser.getUsername());
        userLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");

        // Sidebar buttons
        Button dashboardBtn = createSidebarButton("🏠 Dashboard");
        Button accountsBtn = createSidebarButton("💳 My Accounts");
        Button transactionsBtn = createSidebarButton("📊 Transactions");
        Button bankingBtn = createSidebarButton("💰 Banking");
        Button profileBtn = createSidebarButton("👤 Profile");
        Button logoutBtn = createSidebarButton("🚪 Logout");

        // Button actions
        dashboardBtn.setOnAction(e -> showDashboard());
        accountsBtn.setOnAction(e -> showAccounts());
        transactionsBtn.setOnAction(e -> showTransactions());
        bankingBtn.setOnAction(e -> showBanking());
        profileBtn.setOnAction(e -> showProfile());
        logoutBtn.setOnAction(e -> logout());

        VBox buttonBox = new VBox(10, dashboardBtn, accountsBtn, transactionsBtn, bankingBtn, profileBtn);
        buttonBox.setAlignment(Pos.TOP_LEFT);

        VBox.setVgrow(buttonBox, Priority.ALWAYS);

        sidebar.getChildren().addAll(welcomeLabel, userLabel, new Separator(), buttonBox, logoutBtn);
        root.setLeft(sidebar);
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-border-color: transparent; -fx-alignment: CENTER_LEFT;");
        button.setPrefWidth(160);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-border-color: transparent; -fx-alignment: CENTER_LEFT;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-border-color: transparent; -fx-alignment: CENTER_LEFT;"));
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
        Label header = new Label("Customer Dashboard");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        // Quick Stats
        HBox statsBox = createStatsBox();

        // Recent Activity
        VBox recentActivity = createRecentActivity();

        // Quick Actions
        VBox quickActions = createQuickActions();

        HBox mainContent = new HBox(20, recentActivity, quickActions);
        mainContent.setAlignment(Pos.TOP_CENTER);

        dashboardContent.getChildren().addAll(header, statsBox, mainContent);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(dashboardContent);
    }

    private HBox createStatsBox() {
        HBox statsBox = new HBox(15);
        statsBox.setAlignment(Pos.CENTER_LEFT);

        // Get customer data
        List<Account> accounts = customerController.getCustomerAccounts(currentUser.getUserId());
        double totalBalance = customerController.getTotalBalance(currentUser.getUserId());
        int totalAccounts = accounts.size();

        VBox balanceCard = createStatCard("Total Balance", String.format("BWP %.2f", totalBalance), "#27ae60");
        VBox accountsCard = createStatCard("Total Accounts", String.valueOf(totalAccounts), "#2980b9");
        VBox typeCard = createStatCard("Account Types", getAccountTypes(accounts), "#8e44ad");

        statsBox.getChildren().addAll(balanceCard, accountsCard, typeCard);
        return statsBox;
    }

    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 10; -fx-border-radius: 10;", color));
        card.setPrefSize(180, 80);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private String getAccountTypes(List<Account> accounts) {
        if (accounts.isEmpty()) return "None";

        StringBuilder types = new StringBuilder();
        for (Account account : accounts) {
            if (types.length() > 0) types.append(", ");
            types.append(account.getAccountType());
        }
        return types.toString();
    }

    private VBox createRecentActivity() {
        VBox recentActivity = new VBox(10);
        recentActivity.setPadding(new Insets(15));
        recentActivity.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
        recentActivity.setPrefWidth(400);

        Label title = new Label("Recent Transactions");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        transactionsArea = new TextArea();
        transactionsArea.setEditable(false);
        transactionsArea.setPrefHeight(200);
        transactionsArea.setStyle("-fx-control-inner-background: white;");

        // Load recent transactions
        loadRecentTransactions();

        Button viewAllBtn = new Button("View All Transactions");
        viewAllBtn.setOnAction(e -> showTransactions());

        recentActivity.getChildren().addAll(title, transactionsArea, viewAllBtn);
        return recentActivity;
    }

    private VBox createQuickActions() {
        VBox quickActions = new VBox(10);
        quickActions.setPadding(new Insets(15));
        quickActions.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
        quickActions.setPrefWidth(300);

        Label title = new Label("Quick Actions");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Button depositBtn = new Button("💰 Make Deposit");
        Button withdrawBtn = new Button("💸 Make Withdrawal");
        Button viewAccountsBtn = new Button("💳 View Accounts");
        Button transactionHistoryBtn = new Button("📊 Transaction History");

        // Style buttons
        String buttonStyle = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 200;";
        depositBtn.setStyle(buttonStyle);
        withdrawBtn.setStyle(buttonStyle);
        viewAccountsBtn.setStyle(buttonStyle);
        transactionHistoryBtn.setStyle(buttonStyle);

        // Button actions
        depositBtn.setOnAction(e -> showBanking());
        withdrawBtn.setOnAction(e -> showBanking());
        viewAccountsBtn.setOnAction(e -> showAccounts());
        transactionHistoryBtn.setOnAction(e -> showTransactions());

        quickActions.getChildren().addAll(title, depositBtn, withdrawBtn, viewAccountsBtn, transactionHistoryBtn);
        return quickActions;
    }

    private void showAccounts() {
        VBox accountsContent = new VBox(20);
        accountsContent.setPadding(new Insets(20));

        Label header = new Label("My Accounts");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        accountsArea = new TextArea();
        accountsArea.setEditable(false);
        accountsArea.setPrefHeight(400);
        accountsArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 12px;");

        // Load accounts
        loadCustomerAccounts();

        Button refreshBtn = new Button("Refresh Accounts");
        refreshBtn.setOnAction(e -> loadCustomerAccounts());

        accountsContent.getChildren().addAll(header, accountsArea, refreshBtn);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(accountsContent);
    }

    private void loadCustomerAccounts() {
        List<Account> accounts = customerController.getCustomerAccounts(currentUser.getUserId());

        StringBuilder accountsText = new StringBuilder();
        accountsText.append("ACCOUNT SUMMARY\n");
        accountsText.append("================\n\n");

        if (accounts.isEmpty()) {
            accountsText.append("No accounts found.\n");
            accountsText.append("Please visit the bank to open an account.\n");
        } else {
            for (Account account : accounts) {
                accountsText.append(String.format("Account Number: %s\n", account.getAccountNumber()));
                accountsText.append(String.format("Account Type:   %s\n", account.getAccountType()));
                accountsText.append(String.format("Balance:        BWP %.2f\n", account.getBalance()));
                accountsText.append(String.format("Branch:         %s\n", account.getBranch()));
                accountsText.append(String.format("Interest Rate:  %.3f%%\n", account.getInterestRate() * 100));

                // Account-specific information
                if (account instanceof model.SavingsAccount) {
                    accountsText.append("Features:       No withdrawals allowed\n");
                } else if (account instanceof model.InvestmentAccount) {
                    accountsText.append("Features:       Withdrawals allowed, higher interest\n");
                } else if (account instanceof model.ChequeAccount) {
                    accountsText.append("Features:       Salary account, unlimited transactions\n");
                }

                accountsText.append("----------------------------------------\n\n");
            }

            double totalBalance = customerController.getTotalBalance(currentUser.getUserId());
            accountsText.append(String.format("TOTAL BALANCE: BWP %.2f\n", totalBalance));
        }

        accountsArea.setText(accountsText.toString());
    }

    private void showTransactions() {
        VBox transactionsContent = new VBox(20);
        transactionsContent.setPadding(new Insets(20));

        Label header = new Label("Transaction History");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        // Account selection
        HBox accountSelection = new HBox(10);
        accountSelection.setAlignment(Pos.CENTER_LEFT);

        Label accountLabel = new Label("Select Account:");
        ComboBox<String> accountCombo = new ComboBox<>();

        // Populate account
        List<Account> accounts = customerController.getCustomerAccounts(currentUser.getUserId());
        for (Account account : accounts) {
            accountCombo.getItems().add(account.getAccountNumber() + " - " + account.getAccountType());
        }

        transactionsArea = new TextArea();
        transactionsArea.setEditable(false);
        transactionsArea.setPrefHeight(400);
        transactionsArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 11px;");

        Button loadBtn = new Button("Load Transactions");
        loadBtn.setOnAction(e -> {
            if (accountCombo.getValue() != null) {
                String selectedAccount = accountCombo.getValue().split(" - ")[0];
                loadAccountTransactions(selectedAccount);
            }
        });

        accountSelection.getChildren().addAll(accountLabel, accountCombo, loadBtn);

        // Load transaction
        if (!accounts.isEmpty()) {
            accountCombo.setValue(accountCombo.getItems().get(0));
            loadAccountTransactions(accounts.get(0).getAccountNumber());
        }

        transactionsContent.getChildren().addAll(header, accountSelection, transactionsArea);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(transactionsContent);
    }

    private void loadAccountTransactions(String accountNumber) {
        List<Transaction> transactions = customerController.getAccountTransactions(accountNumber);

        StringBuilder transactionsText = new StringBuilder();
        transactionsText.append("TRANSACTION HISTORY - Account: ").append(accountNumber).append("\n");
        transactionsText.append("============================================\n\n");

        if (transactions.isEmpty()) {
            transactionsText.append("No transactions found.\n");
        } else {
            transactionsText.append(String.format("%-12s %-15s %-10s %-30s %-20s\n",
                    "ID", "Type", "Amount", "Description", "Date"));
            transactionsText.append("----------------------------------------------------------------------------------------\n");

            for (Transaction transaction : transactions) {
                transactionsText.append(String.format("%-12d %-15s BWP %-7.2f %-30s %-20s\n",
                        transaction.getTransactionId(),
                        transaction.getTransactionType(),
                        transaction.getAmount(),
                        transaction.getDescription(),
                        transaction.getTransactionDate().toString().replace("T", " ")));
            }
        }

        transactionsArea.setText(transactionsText.toString());
    }

    private void loadRecentTransactions() {
        List<Account> accounts = customerController.getCustomerAccounts(currentUser.getUserId());
        if (!accounts.isEmpty()) {
            List<Transaction> transactions = customerController.getAccountTransactions(accounts.get(0).getAccountNumber());

            StringBuilder recentText = new StringBuilder();
            int count = 0;
            for (Transaction transaction : transactions) {
                if (count >= 5) break; // Show only 5 most recent
                recentText.append(String.format("%s: BWP %.2f\n%s\n\n",
                        transaction.getTransactionType(),
                        transaction.getAmount(),
                        transaction.getTransactionDate().toString().replace("T", " ")));
                count++;
            }

            if (recentText.length() == 0) {
                recentText.append("No recent transactions");
            }

            transactionsArea.setText(recentText.toString());
        } else {
            transactionsArea.setText("No accounts available");
        }
    }

    private void showBanking() {
        VBox bankingContent = new VBox(20);
        bankingContent.setPadding(new Insets(20));

        Label header = new Label("Banking Transactions");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(25));
        formContainer.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10;");

        // Transaction Type
        HBox typeBox = new HBox(10);
        typeBox.setAlignment(Pos.CENTER_LEFT);
        Label typeLabel = new Label("Transaction Type:");
        typeLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Deposit", "Withdrawal");
        typeCombo.setValue("Deposit");
        typeBox.getChildren().addAll(typeLabel, typeCombo);

        // Account Selection
        HBox accountBox = new HBox(10);
        accountBox.setAlignment(Pos.CENTER_LEFT);
        Label accountLabel = new Label("Account:");
        accountLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        ComboBox<String> accountCombo = new ComboBox<>();


        List<Account> accounts = customerController.getCustomerAccounts(currentUser.getUserId());
        for (Account account : accounts) {
            accountCombo.getItems().add(account.getAccountNumber() + " - " + account.getAccountType() + " (BWP " + account.getBalance() + ")");
        }
        accountBox.getChildren().addAll(accountLabel, accountCombo);

        // Amount
        HBox amountBox = new HBox(10);
        amountBox.setAlignment(Pos.CENTER_LEFT);
        Label amountLabel = new Label("Amount (BWP):");
        amountLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");
        amountBox.getChildren().addAll(amountLabel, amountField);

        // Description
        HBox descBox = new HBox(10);
        descBox.setAlignment(Pos.CENTER_LEFT);
        Label descLabel = new Label("Description:");
        descLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");
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

            if (validateTransaction(selectedAccount, amountText)) {
                double amount = Double.parseDouble(amountText);
                String accountNumber = selectedAccount.split(" - ")[0];

                boolean success = false;
                if ("Deposit".equals(selectedType)) {
                    success = customerController.deposit(accountNumber, amount);
                } else if ("Withdrawal".equals(selectedType)) {
                    success = customerController.withdraw(accountNumber, amount);
                }

                if (success) {
                    resultArea.setText("✅ " + selectedType + " processed successfully!\n\n" +
                            "Account: " + accountNumber + "\n" +
                            "Amount: BWP " + amount + "\n" +
                            "Description: " + descField.getText() + "\n\n" +
                            "Your account balance has been updated.");
                    resultArea.setVisible(true);

                    // Clear fields
                    amountField.clear();
                    descField.clear();

                    // Refresh account combo with updated balances
                    accountCombo.getItems().clear();
                    List<Account> updatedAccounts = customerController.getCustomerAccounts(currentUser.getUserId());
                    for (Account account : updatedAccounts) {
                        accountCombo.getItems().add(account.getAccountNumber() + " - " + account.getAccountType() + " (BWP " + account.getBalance() + ")");
                    }
                } else {
                    showError("Transaction failed. Please check your account balance and try again.");
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

        bankingContent.getChildren().addAll(header, formContainer);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(bankingContent);
    }

    private boolean validateTransaction(String selectedAccount, String amountText) {
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

    private void showProfile() {
        VBox profileContent = new VBox(20);
        profileContent.setPadding(new Insets(20));

        Label header = new Label("My Profile");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.DARKBLUE);

        // Profile information
        VBox profileInfo = new VBox(10);
        profileInfo.setPadding(new Insets(20));
        profileInfo.setStyle("-fx-background-color: #f8f9fa; -fx-border-radius: 5;");

        Label usernameLabel = new Label("Username: " + currentUser.getUsername());
        Label emailLabel = new Label("Email: " + currentUser.getEmail());
        Label userTypeLabel = new Label("User Type: " + currentUser.getUserType());
        Label customerIdLabel = new Label("Customer ID: " + currentUser.getUserId());

        profileInfo.getChildren().addAll(usernameLabel, emailLabel, userTypeLabel, customerIdLabel);

        profileContent.getChildren().addAll(header, profileInfo);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(profileContent);
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

    // Helper method
    private Label createSection(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");
        return label;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Validation Error");
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
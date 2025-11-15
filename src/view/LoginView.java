package view;

import controller.AuthController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.User;
import model.Customer;

public class LoginView extends Application {
    private AuthController authController;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.authController = new AuthController();
        showLoginScreen();
    }

    private void showLoginScreen() {
        stage.setTitle("EBM Banking System - Login");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(15);
        grid.setHgap(10);

        // Title
        Label titleLabel = new Label("EBM Banking System");
        titleLabel.setFont(Font.font(20));
        grid.add(titleLabel, 0, 0, 2, 1);

        // Username
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        grid.add(userLabel, 0, 1);
        grid.add(userField, 1, 1);

        // Password
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        grid.add(passLabel, 0, 2);
        grid.add(passField, 1, 2);

        // Buttons
        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register as Customer");
        Button employeeRegisterBtn = new Button("Register as Employee");

        grid.add(loginBtn, 0, 3);
        grid.add(registerBtn, 1, 3);
        grid.add(employeeRegisterBtn, 0, 4, 2, 1);

        // Login action
        loginBtn.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter both username and password");
                return;
            }

            User user = authController.login(username, password);
            if (user != null) {
                redirectToDashboard(user);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
            }
        });

        // Register actions
        registerBtn.setOnAction(e -> showCustomerRegistration());
        employeeRegisterBtn.setOnAction(e -> showEmployeeRegistration());

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void redirectToDashboard(User user) {
        if (user.isCustomer()) {
            new CustomerDashboard(stage, user);
        } else if (user.isEmployee()) {
            new EmployeeDashboard(stage, user);
        } else if (user.isAdmin()) {
            new AdminDashboard(stage, user);
        }
    }

    private void showCustomerRegistration() {
        Stage registerStage = new Stage();
        registerStage.setTitle("Customer Registration");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Registration form fields
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        ComboBox<String> employmentCombo = new ComboBox<>();
        employmentCombo.getItems().addAll("Employed", "Self-Employed", "Unemployed", "Student");
        TextField companyField = new TextField();
        TextField companyAddressField = new TextField();

        grid.add(new Label("First Name:*"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:*"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Email:*"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:*"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);
        grid.add(new Label("Employment:*"), 0, 5);
        grid.add(employmentCombo, 1, 5);
        grid.add(new Label("Company Name:"), 0, 6);
        grid.add(companyField, 1, 6);
        grid.add(new Label("Company Address:"), 0, 7);
        grid.add(companyAddressField, 1, 7);
        grid.add(new Label("Username:*"), 0, 8);
        grid.add(usernameField, 1, 8);
        grid.add(new Label("Password:*"), 0, 9);
        grid.add(passwordField, 1, 9);

        Button registerBtn = new Button("Register");
        Button cancelBtn = new Button("Cancel");

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.add(registerBtn, 0, 0);
        buttonGrid.add(cancelBtn, 1, 0);
        grid.add(buttonGrid, 1, 10);

        registerBtn.setOnAction(e -> {
            try {

                if (firstNameField.getText().trim().isEmpty() ||
                        lastNameField.getText().trim().isEmpty() ||
                        emailField.getText().trim().isEmpty() ||
                        phoneField.getText().trim().isEmpty() ||
                        usernameField.getText().trim().isEmpty() ||
                        passwordField.getText().trim().isEmpty() ||
                        employmentCombo.getValue() == null) {

                    showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields (*)");
                    return;
                }


                if (authController.usernameExists(usernameField.getText().trim())) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Username already exists. Please choose another.");
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
                if (authController.registerCustomer(customer, usernameField.getText().trim(), passwordField.getText().trim())) {
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Customer registration completed successfully!\n\n" +
                                    "Your login credentials:\n" +
                                    "Username: " + usernameField.getText().trim() + "\n" +
                                    "Password: " + passwordField.getText().trim() + "\n\n" +
                                    "You can now login with these credentials.");
                    registerStage.close();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Registration failed. Please try again.");
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Registration error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        cancelBtn.setOnAction(e -> registerStage.close());

        Scene scene = new Scene(grid, 500, 500);
        registerStage.setScene(scene);
        registerStage.show();
    }

    private void showEmployeeRegistration() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Employee registration requires administrator approval.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
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
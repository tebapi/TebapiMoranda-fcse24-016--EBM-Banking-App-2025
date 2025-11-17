package controller;

import dao.UserDAO;
import dao.CustomerDAO;
import dao.EmployeeDAO;
import model.User;
import model.Customer;
import model.Employee;

public class AuthController {
    private UserDAO userDAO;
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;

    public AuthController() {
        this.userDAO = new UserDAO();
        this.customerDAO = new CustomerDAO();
        this.employeeDAO = new EmployeeDAO();
    }

    public User login(String username, String password) {
        System.out.println("Attempting login for: " + username);
        User user = userDAO.authenticate(username, password);
        if (user != null) {
            System.out.println("Login successful - User ID: " + user.getUserId() + ", Type: " + user.getUserType());
        } else {
            System.out.println("Login failed for: " + username);
        }
        return user;
    }

    public boolean registerCustomer(Customer customer, String username, String password) {
        try {
            System.out.println("=== STARTING CUSTOMER REGISTRATION ===");
            System.out.println("Customer: " + customer.getFirstName() + " " + customer.getLastName());
            System.out.println("Username: " + username);
            System.out.println("Email: " + customer.getEmail());


            if (userDAO.usernameExists(username)) {
                System.out.println("❌ Username already exists: " + username);
                return false;
            }


            User user = new User(username, password, "CUSTOMER", customer.getEmail());
            System.out.println("Creating user account...");

            if (userDAO.registerUser(user)) {
                System.out.println("✅ User registered successfully with ID: " + user.getUserId());


                customer.setCustomerId(user.getUserId());
                System.out.println("Setting customer ID to: " + customer.getCustomerId());

                // Add customer details
                boolean customerAdded = customerDAO.addCustomer(customer);
                System.out.println("Customer details added: " + customerAdded);

                if (customerAdded) {
                    System.out.println("🎉 CUSTOMER REGISTRATION COMPLETED SUCCESSFULLY!");
                    System.out.println("Customer ID: " + customer.getCustomerId());
                    System.out.println("Username: " + username);
                    return true;
                } else {
                    System.out.println("❌ Failed to add customer details - rolling back user account");

                    userDAO.deleteUser(user.getUserId());
                    return false;
                }
            } else {
                System.out.println("❌ Failed to register user account");
                return false;
            }
        } catch (Exception e) {
            System.err.println("💥 ERROR in registerCustomer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerEmployee(Employee employee, String username, String password) {
        try {
            System.out.println("Starting employee registration for: " + username);

            User user = new User(username, password, "EMPLOYEE", "");

            if (userDAO.registerUser(user)) {
                System.out.println("User registered successfully with ID: " + user.getUserId());

                employee.setEmployeeId(user.getUserId());
                System.out.println("Setting employee ID to: " + employee.getEmployeeId());

                boolean employeeAdded = employeeDAO.addEmployee(employee);
                System.out.println("Employee details added: " + employeeAdded);

                return employeeAdded;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error in registerEmployee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean usernameExists(String username) {
        return userDAO.usernameExists(username);
    }
}
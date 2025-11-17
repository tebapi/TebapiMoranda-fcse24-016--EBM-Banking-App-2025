package model;

public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String department;
    private String position;
    private User userAccount;

    public Employee() {}

    // Getters and setters
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public User getUserAccount() { return userAccount; }
    public void setUserAccount(User userAccount) { this.userAccount = userAccount; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
import view.LoginView;

public class Main {
    public static void main(String[] args) {
        // This will initialize the database with sample data
        // The DBConnection static block runs automatically
        System.out.println("Starting EBM Banking System...");

        // Launch the JavaFX application
        LoginView.main(args);
    }
}
package Controller;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.Scanner;


public class Main {
    public static Connection getDatabase(){
        Connection con = null;
        Statement  stmt = null;
        try {

            Class.forName("org.postgresql.Driver");
            con  = DriverManager
                    .getConnection("jdbc:postgresql://pgserver.mau.se:5432/onlinestore_vot",
                            "an7201", "ynvrxbxm");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return con;
    }


    public static void main(String[] args) {

        boolean completed = false; //Boolean value is set to false

        while  (completed == false){ //If the program is not completed (completed = false) then continue
            showMainMenu();
            Scanner scanner = new Scanner(System.in); //Scanner for  choice in menu
            Scanner scanner2 = new Scanner(System.in); //Scanner for userID
            System.out.print("Select the option: ");
            int choice = scanner.nextInt();
            switch (choice){
                case 1: //Choice = Admin
                    System.out.println("Type your admin ID: ");
                    int userID = scanner2.nextInt();

                    if (userID == 123){ //If user ID is correct
                        System.out.println("You choose Admin ");
                        selectAdminMenu();
                    }
                    else{//If not
                        System.out.println("Wrong ID");
                    }
                    break;
                case 2: //Choice = Customer
                    System.out.println("You choose Customer");
                    selectCustomerMenu();
                    break;
                case 3://Choice = end program
                    System.out.println("End program");
                    completed = true;
                    break;
            }

        }


    }

    public static void showMainMenu(){
        System.out.println("1. Admin");
        System.out.println("2. Customer");
        System.out.println("3. End program");

    }
    public static void  selectMainMenu(){
        //TO  BE CONTINUED
    }
    public static void showAdminMenu(){
        System.out.println("Admin Menu");
        System.out.println("1. Manage Supplier");
        System.out.println("2. Manage Product");
        System.out.println("3. Manage Discounts");
        System.out.println("4. View Product list");
        System.out.println("5. Go back to main menu");
    }

    public static void selectAdminMenu(){

        boolean completed = false;

        while (completed == false){
            showAdminMenu();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Select the option: ");
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    completed = true;
                    break;
                //TBC
            }
        }

    }

    /*
     System.out.println("Enter name: ");
    String name = Utilities.getString();
                    System.out.println("Enter phonenumber:");
    String phone = Utilities.getString();
                    System.out.println("Enter city, country, adress: ");
    String adress = Utilities.getString();
    addSupplier(name,phone,adress);
*/

    public static void manageSupplierMenu(){
        System.out.println("Manage Suppliers:");
        System.out.println("1. Add New Supplier");
        System.out.println("2. Edit Supplier Information");
        System.out.println("3. Delete Supplier");
        System.out.println("4. View All Suppliers");
        System.out.println("5. Return to Main Menu");
    }


    public static void manageProductMenu(){
        System.out.println("Manage Products:");
        System.out.println("1. Add New Product");
        System.out.println("2. Edit Product Information");
        System.out.println("3. Delete Product");
        System.out.println("4. Adjust Product Quantity");
        System.out.println("5. Return to Main Menu");
    }
    public static void manageDiscountMenu(){
        System.out.println("Manage Discounts:");
        System.out.println("1. Add New Discount Category");
        System.out.println("2. Assign Discount to Product");
        System.out.println("3. View Discount History");
        System.out.println("4. Return to Main Menu");
    }

    public static void viewProductList(){
        System.out.println("View Products List:");
        System.out.println("1. Search Products by Code");
        System.out.println("2. Search Products by Name");
        System.out.println("3. Search Products by Supplier");
        System.out.println("4. View All Products");
        System.out.println("5. Return to Main Menu");

    }

    public static void viewAdminMenu(){
        System.out.println("View:");
        System.out.println("1. Suppliers");
        System.out.println("2. Products");
        //NEEDS MORE
    }

    public static void showCustomerMenu(){
        System.out.println("Customer Menu");
        System.out.println("1. Signup");
        System.out.println("2. Login");
        System.out.println("3. Go back main menu");

    }
    public static void selectCustomerMenu(){
        boolean completed = false;
        while (completed == false){
            showCustomerMenu();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Select the option: ");
            int choice = scanner.nextInt();
            switch (choice){
                case 1:
                    System.out.println("Enter Firstname: ");
                    String firstname = Utilities.getString();
                    System.out.println("Enter Lastname: ");
                    String lastname = Utilities.getString();
                    System.out.println("Enter City: ");
                    String city = Utilities.getString();
                    System.out.println("Enter Address: ");
                    String address = Utilities.getString();
                    System.out.println("Enter phonenumber:");
                    String phoneNumber = Utilities.getString();
                    System.out.println("Enter email:");
                    String email = Utilities.getString();
                    System.out.println("Enter country: ");
                    String country = Utilities.getString();
                    System.out.println("Enter username: ");
                    String username = Utilities.getString();
                    System.out.println("Enter password: ");
                    String password = Utilities.getString();
                    signUp(firstname,lastname,city,address,phoneNumber,email,country,username,password);
                    break;
                case 2:
                    loginCustomer("oliver123","123");
                    break;
                case 3:
                    completed = true;
                    break;
                    //TBC
            }
        }

    }

    public static void loginCustomer(String username, String password) {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = getDatabase();
            con.setAutoCommit(false);
            System.out.println("Opened database successfully");

            String sql = ("SELECT c_username,c_password FROM CUSTOMER WHERE c_username = ? AND c_password = ?");
            stmt = con.prepareStatement(sql);
            stmt.setString(1,username);
            stmt.setString(2,password);

            ResultSet result = stmt.executeQuery();
            if (result.next()){
                System.out.println("You have logged in");
                System.out.printf("Username: %s", username);
                System.out.println("");
            }
            else {
                System.out.println("Wrong username or password");
            }
            stmt.close();
            con.commit();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void signUp(String firstname, String lastname, String city, String address, String phoneNumber, String email, String country, String username, String password) {

        try (Connection con = getDatabase()) {
            // Check if email or username already exists in database
            String sql = "SELECT * FROM CUSTOMER WHERE c_email = ? OR c_username = ?";
            PreparedStatement checkStmt = con.prepareStatement(sql);
            checkStmt.setString(1, email);
            checkStmt.setString(2, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                // Email or username already exists, display error message and return
                System.out.println("Email or username already in use");
                return;
            }

            // Email and username are unique, insert new record
            String insertSql = "INSERT INTO CUSTOMER (firstname, lastname, c_city, c_address, c_phone_number, c_email, c_country, c_username, c_password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = con.prepareStatement(insertSql);
            insertStmt.setString(1, firstname);
            insertStmt.setString(2, lastname);
            insertStmt.setString(3, city);
            insertStmt.setString(4, address);
            insertStmt.setString(5, phoneNumber);
            insertStmt.setString(6, email);
            insertStmt.setString(7, country);
            insertStmt.setString(8, username);
            insertStmt.setString(9, password);
            insertStmt.executeUpdate();

            System.out.println("Record inserted successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void addSupplier(String name, String phone, String address) {
        try (Connection con = getDatabase();
             PreparedStatement stmt = con.prepareStatement("INSERT INTO SUPPLIER(s_name, s_phone, s_address) VALUES (?, ?, ?)")) {

            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, address);
            stmt.executeUpdate();

            System.out.println("Supplier added successfully");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


   
    public static void addProduct(String pName, String pID, int pQuantity, int pBasePrice, String pSuplier){
        
    }


}


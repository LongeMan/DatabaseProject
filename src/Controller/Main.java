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
        System.out.println("1. Add");
        System.out.println("2. View");
        System.out.println("3. Remove");
        System.out.println("4. Go back to main menu");
    }
    public static void addAdminMenu(){
        int choice = 0;
        System.out.println("Add a:");
        System.out.println("1. Supplier ");
        System.out.println("2. Product ");
        System.out.println("");
        //NEEDS MORE
    }
    public static void viewAdminMenu(){
        System.out.println("View:");
        System.out.println("1. Suppliers");
        System.out.println("2. Products");
        //NEEDS MORE
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
                    System.out.println("Enter name: ");
                    String name = Utilities.getString();
                    System.out.println("Enter phonenumber:");
                    String phone = Utilities.getString();
                    System.out.println("Enter city, country, adress: ");
                    String adress = Utilities.getString();
                    addSupplier(name,phone,adress);
                case 4:
                    completed = true;
                    break;
                    //TBC
            }
        }

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
                    signIn(firstname,lastname,city,address,phoneNumber,email,country,username,password);
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

            Class.forName("org.postgresql.Driver");
            con = DriverManager
                    .getConnection("jdbc:postgresql://pgserver.mau.se:5432/onlinestore_vot",
                            "an7201", "ynvrxbxm");
            con.setAutoCommit(false);
            System.out.println("Opened database successfully");

            String sql = ("SELECT c_username,c_password FROM CUSTOMER WHERE c_username = ? AND c_password = ?");
            stmt = con.prepareStatement(sql);
            stmt.setString(1,username);
            stmt.setString(2,password);

            ResultSet result = stmt.executeQuery();
            //String username1 = result.getString("c_username");
            if (result.next()){
                System.out.println("You have logged in");
                //MENU FOR CUSTOMER METHOD
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
    public static void signIn(String firstname, String lastname, String city, String address, String phoneNumber, String email, String country, String username, String password){

        Connection con = null;
        PreparedStatement stmt = null;
        try {

            Class.forName("org.postgresql.Driver");
            con  = DriverManager
                    .getConnection("jdbc:postgresql://pgserver.mau.se:5432/onlinestore_vot",
                            "an7201", "ynvrxbxm");
            con.setAutoCommit(false);
            System.out.println("Opened database successfully");


            String sql  = ("INSERT INTO CUSTOMER(firstname,lastname,c_city,c_address,c_phone_number,c_email,c_country,c_username,c_password)"+
                    "VALUES(?,?,?,?,?,?,?,?,?);");
            stmt = con.prepareStatement(sql);

            stmt.setString(1,firstname);
            stmt.setString(2,lastname);
            stmt.setString(3,city);
            stmt.setString(4,address);
            stmt.setString(5,phoneNumber);
            stmt.setString(6,email);
            stmt.setString(7,country);
            stmt.setString(8,username);
            stmt.setString(9,password);
            stmt.executeUpdate();
            stmt.close();
            con.commit();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }


    }
    public static void addSupplier(String name, String phone,String adress){
        Connection con = null;
        PreparedStatement stmt = null;
        try {

            Class.forName("org.postgresql.Driver");
            con  = DriverManager
                    .getConnection("jdbc:postgresql://pgserver.mau.se:5432/onlinestore_vot",
                            "an7201", "ynvrxbxm");
            con.setAutoCommit(false);
            System.out.println("Opened database successfully");


            String sql  = ("INSERT INTO SUPPLIER(s_name,s_phone,s_adress)"+
                    "VALUES(?,?,?);");
            stmt = con.prepareStatement(sql);

            stmt.setString(1,name);
            stmt.setString(2,phone);
            stmt.setString(3,adress);

            stmt.executeUpdate();
            stmt.close();
            con.commit();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

    }
    public static void addProduct(String pName, String pID, int pQuantity, int pBasePrice, String pSuplier){
        
    }

}


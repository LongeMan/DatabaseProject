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


    public static void main(String[] args) throws SQLException {

        boolean completed = false; //Boolean value is set to false

        while  (completed == false){ //If the program is not completed (completed = false) then continue
            Menu.showMainMenu();
            Scanner scanner = new Scanner(System.in); //Scanner for  choice in menu
            Scanner scanner2 = new Scanner(System.in); //Scanner for userID
            int choice = 0;
            boolean isChoiceValid = false;
            while (!isChoiceValid) {
                System.out.println("Enter choice: ");
                String input = Utilities.getString();
                try {
                    choice = Integer.parseInt(input);
                    isChoiceValid = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter an integer.");
                }
            }
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
                    System.out.println("Viewing Products");
                    selectViewProductsMenu();
                    break;
                case 4://Choice = end program
                    System.out.println("Ended program");
                    completed = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }

    public static void selectAdminMenu() throws SQLException {
        boolean completed = false;

        while (completed == false){
            Menu.showAdminMenu();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Select the option: ");
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    selectManageSupplierMenu();
                    break;
                case 2:
                    selectManageProductMenu();

                    break;
                case 3:
                    selectManageDiscountMenu();

                    break;
                case 4:
                    selectViewProductsMenu();

                    break;
                case 5:
                    selectManageConfirmationMenu();

                    break;
                case 6:
                    completed = true;
                    break;
                default:
                    System.out.println("Invalid choice, please select a valid option.");
                    break;
            }
        }
    }

    public static void selectManageSupplierMenu(){
        boolean completed = false;
        while(completed==false){
            Menu.manageSupplierMenu();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Select the option: ");
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    System.out.println("Adding Supplier now:");
                    System.out.println("Enter name: ");
                    String name = Utilities.getString();
                    System.out.println("Enter phonenumber:");
                    String phone = Utilities.getString();
                    System.out.println("Enter adress: ");
                    String adress = Utilities.getString();
                    Supplier.addSupplier(name,phone,adress);

                    break;
                case 2:


                    break;
                case 3:


                    break;
                case 4:
                    completed = true;
                    break;
                default:
                    System.out.println("Invalid choice, please select a valid option.");
                    break;
            }
        }
    }

    public static void selectManageProductMenu() throws SQLException {
        boolean completed = false;
        while (!completed) {
            Menu.manageProductMenu();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Select the option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Adding product now:");
                    System.out.println("Enter product name: ");
                    String productName = Utilities.getString();

                    int productQuantity = 0;
                    boolean isProductQuantityValid = false;
                    while (!isProductQuantityValid) {
                        System.out.println("Enter Quantity: ");
                        String input = Utilities.getString();
                        try {
                            productQuantity = Integer.parseInt(input);
                            isProductQuantityValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }

                    int productBasePrice = 0;
                    boolean isProductBasePriceValid = false;
                    while (!isProductBasePriceValid) {
                        System.out.println("Enter BasePrice: ");
                        String input = Utilities.getString();
                        try {
                            productBasePrice = Integer.parseInt(input);
                            isProductBasePriceValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }
                    int supplier = 0;
                    boolean isSupplierValid = false;
                    while (!isSupplierValid) {
                        Supplier.printAllSuppliers();
                        System.out.println("Enter supplier id: ");
                        String input = Utilities.getString();
                        try {
                            supplier = Integer.parseInt(input);
                            isSupplierValid = true;
                        }catch(NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }
                    Product.addProduct(productName, productQuantity, productBasePrice, supplier);
                    break;
                case 2:
                    // code for managing products
                    break;
                case 3:
                    Product.printAllProductsAndDiscounts();

                    int productId = 0;
                    boolean isProductIdValid = false;
                    while (!isProductIdValid) {
                        System.out.println("Enter Product Id: ");
                        String input = Utilities.getString();
                        try {
                            productId = Integer.parseInt(input);
                            isProductIdValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }
                    Product.removeProduct(productId);
                    break;
                case 4:
                    Product.printAllProductsAndDiscounts();

                    int chosenProductId = 0;
                    boolean isChosenProductIdValid = false;
                    while (!isChosenProductIdValid) {
                        System.out.println("Enter Product Id: ");
                        String input = Utilities.getString();
                        try {
                            chosenProductId = Integer.parseInt(input);
                            isChosenProductIdValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }


                    int quantityChange = 0;
                    boolean isQuantityChangeValid = false;
                    while (!isQuantityChangeValid) {
                        System.out.println("Enter quantity change: ");
                        String input = Utilities.getString();
                        try {
                            quantityChange = Integer.parseInt(input);
                            isQuantityChangeValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }

                    Product.EditQuantity(chosenProductId, quantityChange);


                case 5:
                    completed = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }
    public static void selectManageDiscountMenu(){
        boolean completed = false;
        while(completed==false){
            Menu.manageDiscountMenu();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Select the option: ");
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    System.out.println("Adding discount now:");
                    Product.printAllProductsAndDiscounts();

                    int productId = 0;
                    boolean isProductIdValid = false;
                    while (!isProductIdValid) {
                        System.out.println("Enter Product Id: ");
                        String input = Utilities.getString();
                        try {
                            productId = Integer.parseInt(input);
                            isProductIdValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }

                    double discount = 0.0;
                    boolean isDiscountValueValid = false;
                    while (!isDiscountValueValid) {
                        System.out.println("Enter discount in decimals: ");
                        String input = Utilities.getString();
                        try {
                            discount = Double.parseDouble(input);
                            isDiscountValueValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a double.");
                        }
                    }

                    int period = 0;
                    boolean isPeriodValid = false;
                    while (!isPeriodValid) {
                        System.out.println("Enter period in days: ");
                        String input = Utilities.getString();
                        try {
                            period = Integer.parseInt(input);
                            isPeriodValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }
                    Discount.printAllReasons();

                    int reasonid = 0;
                    boolean isReasonIdValid = false;
                    while (!isReasonIdValid) {
                        System.out.println("Enter reason id for discount ");
                        String input = Utilities.getString();
                        try {
                            reasonid = Integer.parseInt(input);
                            isReasonIdValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }


                    Discount.addDiscount(productId, discount, period,reasonid);

                    break;
                case 2:
                    Discount.printActiveDiscounts();


                    break;
                case 3:
                    Discount.printAllDiscountsHistory();

                    break;
                case 4:
                    completed = true;
                    break;
                default:
                    System.out.println("Invalid choice, please select a valid option.");
                    break;
            }
        }
    }

    public static void selectViewProductsMenu(){
        boolean completed = false;
        while(!completed){

            Menu.viewProductList();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Select the option: ");
            int choice = scanner.nextInt();
            switch (choice){
                case 1:
                    int productId = 0;
                    boolean isProductIdValid = false;
                    while (!isProductIdValid) {
                        System.out.println("Enter Product Id: ");
                        String input = Utilities.getString();
                        try {
                            productId = Integer.parseInt(input);
                            isProductIdValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }

                    Product.getProductById(productId);
                    break;
                case 2:
                    System.out.println("Enter product name: ");
                    String productName = Utilities.getString();
                    Product.getProductsByName(productName);
                    break;
                case 3:
                    System.out.println("Enter supplier name: ");
                    String supplierId = Utilities.getString();
                    Product.getProductsBySupplierName(supplierId);
                    break;
                case 4:
                    Product.printAllProductsAndDiscounts();
                    break;
                case 5:
                    completed = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }


    }

    public static void selectCustomerMenu(){
        boolean completed = false;
        boolean loggedIn = false;
        int customerId = -1; // add this variable
        while (completed == false){
            if (loggedIn) {
                selectCustomerViewMenu(customerId); // pass the customerId to the view menu
            } else {
                Menu.showCustomerMenu();
            }

            int choice = 0;
            boolean isChoiceValid = false;
            while (!isChoiceValid) {
                System.out.println("Enter choice: ");
                String input = Utilities.getString();
                try {
                    choice = Integer.parseInt(input);
                    isChoiceValid = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter an integer.");
                }
            }
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
                    System.out.println("Enter Username: ");
                    String Lusername = Utilities.getString();
                    System.out.println("Enter password: ");
                    String Lpassword = Utilities.getString();
                    customerId = loginCustomer(Lusername,Lpassword); // assign the customerId returned by loginCustomer()
                    if (customerId != -1) {
                        loggedIn = true;
                    }
                    break;
                case 3:
                    completed = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }
    public static void selectCustomerViewMenu(int customerId){
        boolean completed = false;


        while (completed == false){
            Menu.showCustomerViewMenu();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Select the option: ");
            int choice = scanner.nextInt();
            switch (choice){
                case 1:

                    selectViewProductsMenu();

                    //Product.printAllProductsAndDiscounts();
                    break;
                case 2:
                    Product.printProductsInStock();


                    int productId = 0;
                    boolean isProductIdValid = false;
                    while (!isProductIdValid) {
                        System.out.println("Enter Product Id: ");
                        String input = Utilities.getString();
                        try {
                            productId = Integer.parseInt(input);
                            isProductIdValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }
                    int productQuantity = 0;
                    boolean isProductQuantityValid = false;
                    while (!isProductQuantityValid) {
                        System.out.println("Enter Quantity: ");
                        String input = Utilities.getString();
                        try {
                            productQuantity = Integer.parseInt(input);
                            isProductQuantityValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }

                    Order.addProductToOrder(customerId,productId,productQuantity ); // pass the customerId to the addProducttoOrder() method

                    break;
                case 3:
                    Order.ShowAllOrders(customerId);

                    break;
                case 4:
                    Order.ShowCurrentOrder(customerId);
                    break;
                case 5:
                    Discount.printActiveDiscounts();
                    break;
                case 6:
                    Order.removeCurrentOrderItems(customerId);
                    break;
                case 7:
                    Product.printMostSoldProducts();
                    break;
                case 8:

                    completed = true;
                    Menu.showCustomerMenu();
                    break;

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }
    public static void selectManageConfirmationMenu() throws SQLException {
        boolean completed = false;
        while(completed==false){
            Menu.manageConfirmation();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Select the option: ");
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    Order.printUnconfirmedOrders();

                    int chosenOrder = 0;
                    boolean isChosenOrderValid = false;
                    while (!isChosenOrderValid) {
                        System.out.println("Enter the order id you want to confrim: ");
                        String input = Utilities.getString();
                        try {
                           chosenOrder = Integer.parseInt(input);
                            isChosenOrderValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }
                    Order.confirmOrder(chosenOrder);
                    break;

                case 2:
                    Order.printUnconfirmedOrders();

                    int chosenOrderToRemove = 0;
                    boolean isChosenOrderToRemoveValid = false;
                    while (!isChosenOrderToRemoveValid) {
                        System.out.println("Enter the order id you want to cancel: ");
                        String input = Utilities.getString();
                        try {
                            chosenOrderToRemove = Integer.parseInt(input);
                            isChosenOrderToRemoveValid = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter an integer.");
                        }
                    }
                    Order.removeOrder(chosenOrderToRemove);
                    break;
                case 3:
                    completed = true;
                    break;
                default:
                    System.out.println("Invalid choice, please select a valid option.");
                    break;
            }
        }

    }

    public static int loginCustomer(String username, String password) {
        Connection con = null;
        PreparedStatement stmt = null;
        int customerId = -1;
        try {
            con = getDatabase();
            con.setAutoCommit(false);
            System.out.println("Opened database successfully");

            String sql = ("SELECT user_id FROM CUSTOMER WHERE c_username = ? AND c_password = ?");
            stmt = con.prepareStatement(sql);
            stmt.setString(1,username);
            stmt.setString(2,password);

            ResultSet result = stmt.executeQuery();
            if (result.next()){
                customerId = result.getInt("user_id");
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
        return customerId;
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
}


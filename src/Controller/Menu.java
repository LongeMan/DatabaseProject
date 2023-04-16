package Controller;public class Menu {

    public static void showMainMenu(){
        System.out.println("1. Admin");
        System.out.println("2. Customer");
        System.out.println("3. End program");
    }

    public static void showAdminMenu(){
        System.out.println("Admin Menu");
        System.out.println("1. Manage Supplier");
        System.out.println("2. Manage Product");
        System.out.println("3. Manage Discounts");
        System.out.println("4. View Product list");
        System.out.println("5. Go back to main menu");
    }

    public static void showCustomerMenu(){
        System.out.println("Customer Menu");
        System.out.println("1. Signup");
        System.out.println("2. Login");
        System.out.println("3. Go back main menu");

    }

    public static void manageSupplierMenu(){
        System.out.println("Manage Suppliers:");
        System.out.println("1. Add New Supplier");
        System.out.println("2. Delete Supplier");//
        System.out.println("3. View All Suppliers");//
        System.out.println("4. Return to Main Menu");
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

}

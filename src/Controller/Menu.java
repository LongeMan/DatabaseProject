package Controller;public class Menu {

    public static void showMainMenu(){
        System.out.println("1. Admin");
        System.out.println("2. Customer");
        System.out.println("3. View Products");
        System.out.println("4. End program");
        System.out.println("-----------------------------");
    }

    public static void showAdminMenu(){
        System.out.println("Admin Menu");
        System.out.println("1. Manage Supplier");
        System.out.println("2. Manage Product");
        System.out.println("3. Manage Discounts");
        System.out.println("4. View Product list");
        System.out.println("5. Manage Confirmation");
        System.out.println("6. Return");
        System.out.println("-----------------------------");
    }

    public static void showCustomerMenu(){
        System.out.println("Customer Menu");
        System.out.println("1. Signup");
        System.out.println("2. Login");
        System.out.println("3. Return");
        System.out.println("-----------------------------");
    }

    public static void showCustomerViewMenu(){
        System.out.println("Customer Menu");
        System.out.println("1. List of products");
        System.out.println("2. Add products to order");
        System.out.println("3. View previous orders");
        System.out.println("4. View current order");
        System.out.println("5. View Available Discounts");
        System.out.println("6. Delete current order");
        System.out.println("7. View most sold items");
        System.out.println("8. Return");
        System.out.println("-----------------------------");
    }

    public static void manageSupplierMenu(){
        System.out.println("Manage Suppliers:");
        System.out.println("1. Add New Supplier");
        System.out.println("2. Delete Supplier");//
        System.out.println("3. View All Suppliers");//
        System.out.println("4. Return");
        System.out.println("-----------------------------");
    }

    public static void manageProductMenu(){
        System.out.println("Manage Products:");
        System.out.println("1. Add New Product");
        System.out.println("2. Edit Product Information not implemented");
        System.out.println("3. Delete Product");
        System.out.println("4. Adjust Product Quantity");
        System.out.println("5. Return");
        System.out.println("-----------------------------");
    }
    public static void manageDiscountMenu(){
        System.out.println("Manage Discounts:");
        System.out.println("1. Assign Discount to Product");
        System.out.println("2. View Active Discounts");
        System.out.println("3. View Discount History");
        System.out.println("4. Return");
        System.out.println("-----------------------------");
    }

    public static void viewProductList(){
        System.out.println("View Products List:");
        System.out.println("1. Search Products by Code");
        System.out.println("2. Search Products by Name");
        System.out.println("3. Search Products by Supplier");
        System.out.println("4. View All Products");
        System.out.println("5. Return");
        System.out.println("-----------------------------");
    }
    public static void manageConfirmation(){
        System.out.println("Manage Confrimation");
        System.out.println("1. Confirm Orders");
        System.out.println("2. Cancel Orders");
        System.out.println("3. Return");
        System.out.println("-----------------------------");
    }

}

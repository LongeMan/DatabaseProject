package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {

    public static void addProduct(String pName, int pQuantity, int pBasePrice, int pSupplier){
        try (Connection con = Main.getDatabase()){
            // Check if the pSupplier value exists in the SUPPLIER table
            String checkSql = "SELECT supplier_id FROM SUPPLIER WHERE supplier_id = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setInt(1, pSupplier);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                throw new IllegalArgumentException("Invalid supplier ID");
            }

            // If the pSupplier value is valid, insert a new record into the PRODUCT table
            String sql = "INSERT INTO PRODUCT (p_name, p_quantity, p_baseprice, p_supplier) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, pName);
            stmt.setInt(2, pQuantity);
            stmt.setInt(3, pBasePrice);
            stmt.setInt(4, pSupplier);

            stmt.executeUpdate();
            System.out.println("Record inserted successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static void printProductsInStock() {
        try (Connection con = Main.getDatabase()) {
            String sql = "SELECT * FROM PRODUCT WHERE p_quantity > 0";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String pID = rs.getString("p_id");
                String pName = rs.getString("p_name");
                int pQuantity = rs.getInt("p_quantity");
                int pBasePrice = rs.getInt("p_baseprice");
                int pSupplier = rs.getInt("p_supplier");

                System.out.println("Product ID: " + pID);
                System.out.println("Product Name: " + pName);
                System.out.println("Product Quantity: " + pQuantity);
                System.out.println("Product Base Price: " + pBasePrice);
                System.out.println("Product Supplier: " + pSupplier);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void printAllProducts() {
        try (Connection con = Main.getDatabase()) {
            String sql = "SELECT * FROM PRODUCT";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String pID = rs.getString("p_id");
                String pName = rs.getString("p_name");
                int pQuantity = rs.getInt("p_quantity");
                int pBasePrice = rs.getInt("p_baseprice");
                int pSupplier = rs.getInt("p_supplier");

                System.out.println("Product ID: " + pID);
                System.out.println("Product Name: " + pName);
                System.out.println("Product Quantity: " + pQuantity);
                System.out.println("Product Base Price: " + pBasePrice);
                System.out.println("Product Supplier: " + pSupplier);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void printAllProductsAndDiscounts() {
        try (Connection con = Main.getDatabase()) {
            String sql = "SELECT p.p_id, p.p_name, p.p_quantity, p.p_baseprice, p.p_supplier, d.discount, d.period, dr.reason " +
                    "FROM product p " +
                    "LEFT JOIN discounts d ON p.p_id = d.p_id " +
                    "LEFT JOIN discountreason dr ON d.discountreason_id = dr.discountreason_id";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String pID = rs.getString("p_id");
                String pName = rs.getString("p_name");
                int pQuantity = rs.getInt("p_quantity");
                int pBasePrice = rs.getInt("p_baseprice");
                int pSupplier = rs.getInt("p_supplier");
                double discount = rs.getDouble("discount");
                int period = rs.getInt("period");
                String reason = rs.getString("reason");

                System.out.println("Product ID: " + pID);
                System.out.println("Product Name: " + pName);
                System.out.println("Product Quantity: " + pQuantity);
                System.out.println("Product Base Price: " + pBasePrice);
                System.out.println("Product Supplier: " + pSupplier);

                if (discount != 0) {
                    System.out.println("Discount percentage: " + (100*discount) + "%");
                    System.out.println("Discount period: " + period + " days");
                    if (reason != null) {
                        System.out.println("Reason for discount: " + reason);
                    }
                } else {
                    System.out.println("This product is not currently on discount.");
                }
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }




    public static void getProductsBySupplierName(String supplierName) {
        try (Connection con = Main.getDatabase()) {
            String sql = "SELECT * FROM Product p JOIN Supplier s ON p.p_supplier = s.supplier_id WHERE s.s_name = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, supplierName);

            ResultSet rs = stmt.executeQuery();

            // Loop through the result set and print the data
            while (rs.next()) {
                String name = rs.getString("p_name");
                int quantity = rs.getInt("p_quantity");
                int basePrice = rs.getInt("p_baseprice");

                System.out.println("Product name: " + name);
                System.out.println("Quantity: " + quantity);
                System.out.println("Base price: " + basePrice);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static void getProductById(int productId){
        try (Connection con = Main.getDatabase()) {
            String sql = "SELECT * FROM PRODUCT WHERE p_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, productId);

            ResultSet rs = stmt.executeQuery();

            // Loop through the result set and print the data
            while (rs.next()) {
                String name = rs.getString("p_name");
                int quantity = rs.getInt("p_quantity");
                int basePrice = rs.getInt("p_baseprice");

                System.out.println("Product name: " + name);
                System.out.println("Quantity: " + quantity);
                System.out.println("Base price: " + basePrice);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void getProductsByName(String productName) {
        try (Connection con = Main.getDatabase()) {
            String sql = "SELECT * FROM PRODUCT WHERE p_name LIKE ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + productName + "%");

            ResultSet rs = stmt.executeQuery();

            // Loop through the result set and print the data
            while (rs.next()) {
                String name = rs.getString("p_name");
                int quantity = rs.getInt("p_quantity");
                int basePrice = rs.getInt("p_baseprice");

                System.out.println("Product name: " + name);
                System.out.println("Quantity: " + quantity);
                System.out.println("Base price: " + basePrice);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    /**
     * Removes a product from the database
     * @param productId product ID which is designated
     */
    public static void removeProduct(int productId) {
        try (Connection conn = Main.getDatabase()) {
            // Check if there are any order items associated with the product
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM orderitems WHERE p_id = ?");
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int orderItemCount = rs.getInt(1);

            if (orderItemCount == 0) {
                // No order items associated with the product, so delete the product
                stmt = conn.prepareStatement("DELETE FROM Product WHERE p_id = ?");
                stmt.setInt(1, productId);
                stmt.executeUpdate();
            } else {
                // There are order items associated with the product, so do not delete it
                System.out.println("Cannot delete product with id " + productId + " because it has " + orderItemCount + " associated order items.");
            }

            // Close the database connection
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        public static void EditQuantity(int productID, int quantityChange) throws SQLException {
        try(Connection conn = Main.getDatabase()){
            // Get the current product quantity
            String getCurrentQty = "SELECT p_quantity FROM product WHERE p_id = ?";
            PreparedStatement psGetCurrentQty = conn.prepareStatement(getCurrentQty);
            psGetCurrentQty.setInt(1, productID);
            ResultSet rsCurrentQty = psGetCurrentQty.executeQuery();

            // If the product exists, update its quantity
            if (rsCurrentQty.next()) {
                int currentQty = rsCurrentQty.getInt("p_quantity");
                int newQty = currentQty + quantityChange;

                // Update the product quantity
                String updateQty = "UPDATE product SET p_quantity = ? WHERE p_id = ?";
                PreparedStatement psUpdateQty = conn.prepareStatement(updateQty);
                psUpdateQty.setInt(1, newQty);
                psUpdateQty.setInt(2, productID);
                psUpdateQty.executeUpdate();

                System.out.println("Product quantity updated successfully.");

            }
        }
    }
}
package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {

    private String pName;
    private String pID;
    private int pQuantity;
    private int pBasePrice;
    private String pSupplier;

    /**
     * Decreases the amount of products for a specific product
     * @param pID Product ID
     * @param decreasingQuantity amount to decrease for the product
     */
    public void decreaseQuantity(String pID, int decreasingQuantity){
        pQuantity = pQuantity - decreasingQuantity;
    }

    /**
     * Increases the amount of products for a specific product
     * @param pID Product ID
     * @param increaseQuantity amount to increase the quantity for the product
     */
    public void increaseQuantity(String pID, int increaseQuantity){
        pQuantity = pQuantity + increaseQuantity;
    }

    /**
     * Adds a product to the SQL database
     * @param pName Product name
     * @param pQuantity Product quantity
     * @param pBasePrice Products baseprice
     * @param pSupplier Products supplier
     */


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



    /*public void getProductsBySupplierId(int supplierID) {
        try (Connection con = Main.getDatabase()) {
            String sql = "SELECT * FROM PRODUCT WHERE p_supplier = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, supplierID);

            ResultSet rs = stmt.executeQuery();

            // Loop through the result set and print the data
            while (rs.next()) {
                String name = rs.getString("p_name");
                int quantity = rs.getInt("p_quantity");
                int basePrice = rs.getInt("p_baseprice");

                System.out.println("Product name: " + name);
                System.out.println("Quantity: " + quantity);
                System.out.println("Base price: " + basePrice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
*/

    /**
     * Removes a product from the database
     * @param pID product ID which is designated
     */
    public void removeProduct(String pID){
        try(Connection con = Main.getDatabase()){
            String sql = (" DELETE FROM PRODUCT WHERE p_id = ?");
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,pID);
            stmt.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

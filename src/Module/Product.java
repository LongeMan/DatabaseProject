package Module;
import Controller.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
     * @param pID Product ID
     * @param pQuantity Product quantity
     * @param pBasePrice Products baseprice
     * @param pSupplier Products supplier
     */

    public void addProduct(String pName, String pID, int pQuantity, int pBasePrice, String pSupplier){
        try (Connection con = Main.getDatabase()){
            String sql = "INSERT INTO PRODUCT (p_name, p_id, p_quantity, p_baseprice, p_supplier) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, pName);
            stmt.setString(2, pID);
            stmt.setInt(3, pQuantity);
            stmt.setInt(4, pBasePrice);
            stmt.setString(5, pSupplier);

            stmt.executeUpdate();
            System.out.println("Record inserted successfully");


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

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

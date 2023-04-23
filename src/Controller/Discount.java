package Controller;
import Controller.*;

import java.sql.*;

public class Discount {
    private int pID;
    private double discount;
    private int period;
    private String dCode;

    public static void addDiscount(int pID, double discount, int period, String dCode){
        try (Connection con = Main.getDatabase()){
            // Check if the p_id value exists in the PRODUCTS table
            String checkSql = "SELECT P_ID FROM PRODUCT WHERE P_ID = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setInt(1, pID);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                throw new IllegalArgumentException("Invalid product ID");
            }


            String sql = "INSERT INTO DISCOUNTS(p_id,discount,period,d_code) VALUES(?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, pID);
            stmt.setDouble(2,discount);
            stmt.setInt(3,period);
            stmt.setString(4,dCode);

            stmt.executeUpdate();
            System.out.println("Record inserted successfully");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void checkDiscount(String dCode, int productId){
        try (Connection con = Main.getDatabase()){
            String sql = "SELECT * FROM DISCOUNTS WHERE (d_code = ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,dCode);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                System.out.println("Record was successfully found");
                
            }
            else {
                System.out.println("Record was not found");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void printAllActiveDiscounts() {
        try (Connection con = Main.getDatabase()) {
            String sql = "SELECT Discounts.p_id, p_name, discount, period, d_code " +
                    "FROM Discounts JOIN Product ON Discounts.p_id = Product.p_id " +
                    "WHERE NOW() <= DATE_ADD(date_created, INTERVAL period DAY)";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int pID = rs.getInt("p_id");
                String pName = rs.getString("p_name");
                double discount = rs.getDouble("discount");
                int period = rs.getInt("period");
                String d_code = rs.getString("d_code");

                System.out.println("Product Name: " + pName);
                System.out.println("Discount percentage: " + (100*discount));
                System.out.println("Discount period (days): " + period);
                System.out.println("Discount code: " + d_code);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }




    public static void printAllDiscounts() {
        try (Connection con = Main.getDatabase()) {
            String sql = "SELECT Discounts.p_id, p_name, discount, period, d_code " +
                    "FROM Discounts JOIN Product ON Discounts.p_id = Product.p_id";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int pID = rs.getInt("p_id");
                String pName = rs.getString("p_name");
                double discount = rs.getDouble("discount");
                int period = rs.getInt("period");
                String d_code = rs.getString("d_code");

                System.out.println("Product Name: " + pName);
                System.out.println("Discount percentage: " + (100*discount)+"%");
                System.out.println("Discount period: " + period);
                System.out.println("Discount code: " + d_code);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    public void setNewPrice(int productId, double discount){
        try(Connection con = Main.getDatabase()){
            String sql = "SELECT p_baseprice FROM PRODUCT WHERE (p_id = ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1,productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

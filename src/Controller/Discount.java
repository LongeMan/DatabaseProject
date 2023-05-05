package Controller;
import Controller.*;

import java.sql.*;
import java.time.LocalDate;

public class Discount {
    private int pID;
    private double discount;
    private int period;


    public static void addDiscount(int pID, double discount, int period, int discountReasonId) {
        try (Connection con = Main.getDatabase()) {
            // Check if the p_id value exists in the Product table
            String checkSql = "SELECT p_id FROM Product WHERE p_id = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setInt(1, pID);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                throw new IllegalArgumentException("Invalid product ID");
            }

            String sql = "INSERT INTO Discounts( p_id, discount, period, discountreason_id, start_date) VALUES (?, ?, ?, ?, CURRENT_DATE)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, pID);
            stmt.setDouble(2, discount);
            stmt.setInt(3, period);
            stmt.setInt(4, discountReasonId);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New discount added successfully");
            } else {
                System.out.println("Failed to add discount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    public static void printActiveDiscounts() {
        String sql = "SELECT p.p_name, dis.discount, dis.period, dis.start_date, " +
                "(p.p_baseprice - (p.p_baseprice * dis.discount)) AS final_price, " +
                "dr.reason " +
                "FROM discounts dis " +
                "JOIN product p ON dis.p_id = p.p_id " +
                "JOIN discountReason dr ON dis.discountreason_id = dr.discountreason_id " +
                "WHERE dis.start_date <= CURRENT_DATE " +
                "AND (dis.start_date + dis.period * INTERVAL '1 DAY') >= CURRENT_DATE";

        try (Connection con = Main.getDatabase()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String pName = rs.getString("p_name");
                double discount = rs.getDouble("discount");
                int period = rs.getInt("period");
                Date startDate = rs.getDate("start_date");
                double finalPrice = rs.getDouble("final_price");
                String reason = rs.getString("reason");

                System.out.println("Discount for on product: " + pName);
                System.out.println("Discount percentage: " + (100*discount) + "%");
                System.out.println("Discount period: " + period + " days");
                System.out.println("Start date of discount: " + startDate);
                System.out.println("Discount reason: " + reason);
                System.out.println("Final price after discount: " + finalPrice);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static void printAllDiscountsHistory() {
        try (Connection con = Main.getDatabase()) {
            String sql = "SELECT p.p_name, d.discount, d.period, d.start_date, dr.reason, (p.p_baseprice - (p.p_baseprice * d.discount)) AS final_price " +
                    "FROM discounts d " +
                    "JOIN product p ON d.p_id = p.p_id " +
                    "LEFT JOIN discountReason dr ON d.discountReason_id = dr.discountreason_id";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String pName = rs.getString("p_name");
                double discount = rs.getDouble("discount");
                int period = rs.getInt("period");

                Date startDate = rs.getDate("start_date");
                String reason = rs.getString("reason");
                double finalPrice = rs.getDouble("final_price");

                System.out.println("Product Name: " + pName);
                System.out.println("Discount percentage: " + (100*discount) + "%");
                System.out.println("Discount period: " + period + " days");
                System.out.println("Start date of discount: " + startDate);
                if (reason != null) {
                    System.out.println("Reason for discount: " + reason);
                }
                System.out.println("Final price after discount: " + finalPrice);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    public static void printAllReasons() {
        try (Connection con = Main.getDatabase()) {
            String sql = "SELECT * FROM discountReason";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String drID = rs.getString("discountreason_id");
                String reason = rs.getString("reason");


                System.out.println("Reason ID: " + drID);
                System.out.println("Reason: " + reason);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }







}

package Controller;
import Controller.*;

import java.sql.*;
import java.time.LocalDate;

public class Discount {
    private int pID;
    private double discount;
    private int period;
    private String dCode;

    public static void addDiscount(int pID, double discount, int period, String dCode, String reason) {
        try (Connection con = Main.getDatabase()) {
            // Check if the p_id value exists in the Product table
            String checkSql = "SELECT p_id FROM Product WHERE p_id = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setInt(1, pID);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                throw new IllegalArgumentException("Invalid product ID");
            }

            String sql = "INSERT INTO Discounts(d_code, p_id, discount, period, reason, start_date) VALUES (?, ?, ?, ?, ?, CURRENT_DATE)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, dCode);
            stmt.setInt(2, pID);
            stmt.setDouble(3, discount);
            stmt.setInt(4, period);
            stmt.setString(5, reason);

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
    public static void insertDiscount(int customerId, String d_code) {
        try (Connection conn = Main.getDatabase();
             Statement stmt = conn.createStatement()) {

            // Check if the discount code is still active
            String checkPeriodSql = "SELECT * FROM discounts " +
                    "WHERE d_code = '" + d_code + "' " +
                    "AND start_date <= CURRENT_DATE " +
                    "AND (start_date + (period * INTERVAL '1 DAY')) >= CURRENT_DATE";
            ResultSet periodResult = stmt.executeQuery(checkPeriodSql);

            if (!periodResult.next()) {
                System.out.println("Discount code " + d_code + " is not currently active.");
                return;
            }

            // Check if customer has an order that is not confirmed yet
            String checkOrderSql = "SELECT * FROM orders WHERE customer_id = " + customerId +
                    " AND order_id NOT IN (SELECT order_id FROM confirmation)";
            ResultSet orderResult = stmt.executeQuery(checkOrderSql);

            int orderId = -1; // initialize orderId to an invalid value
            if (orderResult.next()) {
                orderId = orderResult.getInt("order_id"); // get the order_id from the result set
            } else {
                System.out.println("Customer has no orders that are not confirmed yet.");
                return;
            }

            // Get the discount amount from the discounts table
            String discountAmountSql = "SELECT discount FROM discounts WHERE d_code = '" + d_code + "'";
            ResultSet discountAmountResult = stmt.executeQuery(discountAmountSql);
            double discount = 0.0; // initialize discount to 0.0 in case there are no results
            if (discountAmountResult.next()) {
                discount = discountAmountResult.getDouble("discount");
            }

            // Get the order items for the given order ID
            String getOrderItemsSql = "SELECT * FROM orderitems WHERE order_id = " + orderId;
            ResultSet orderItemsResult = stmt.executeQuery(getOrderItemsSql);

            // Loop through the order items and update the price for each item
            while (orderItemsResult.next()) {
                int productId = orderItemsResult.getInt("p_id");
                double basePrice = orderItemsResult.getDouble("price");
                double newPrice = basePrice * (1 - discount);

                // Update the price for the current order item
                String updateOrderItemSql = "UPDATE orderitems SET price = " + newPrice + " WHERE order_id = " + orderId + " AND p_id = " + productId;
                stmt.executeUpdate(updateOrderItemSql);
            }

            System.out.println("Prices updated with discount code " + d_code);

        } catch (SQLException e) {
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

    public static void printActiveDiscounts() {
        String sql = "SELECT p.p_name, dis.discount, dis.period, dis.d_code, (p.p_baseprice - (p.p_baseprice * dis.discount)) AS final_price " +
                "FROM discounts dis " +
                "JOIN product p ON dis.p_id = p.p_id " +
                "WHERE dis.start_date <= CURRENT_DATE " +
                "AND (dis.start_date + dis.period * INTERVAL '1 DAY') >= CURRENT_DATE";

        try (Connection con = Main.getDatabase()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String pName = rs.getString("p_name");
                double discount = rs.getDouble("discount");
                int period = rs.getInt("period");
                String dCode = rs.getString("d_code");
                double finalPrice = rs.getDouble("final_price");

                System.out.println("Product Name: " + pName);
                System.out.println("Discount percentage: " + (100*discount) + "%");
                System.out.println("Discount period: " + period + " days");
                System.out.println("Discount code: " + dCode);
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
            String sql = "SELECT p.p_name, d.discount, d.period, d.d_code, d.start_date, (p.p_baseprice - (p.p_baseprice * d.discount)) AS final_price " +
                    "FROM discounts d " +
                    "JOIN product p ON d.p_id = p.p_id";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String pName = rs.getString("p_name");
                double discount = rs.getDouble("discount");
                int period = rs.getInt("period");
                String dCode = rs.getString("d_code");
                Date startDate = rs.getDate("start_date");
                double finalPrice = rs.getDouble("final_price");

                System.out.println("Product Name: " + pName);
                System.out.println("Discount percentage: " + (100*discount) + "%");
                System.out.println("Discount period: " + period + " days");
                System.out.println("Discount code: " + dCode);
                System.out.println("Start date of discount: " + startDate);
                System.out.println("Final price after discount: " + finalPrice);
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

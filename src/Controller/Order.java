package Controller;

import java.sql.*;
import java.util.Calendar;

public class Order {
    public static void addProductToOrder(int customerId, int productId, int quantity) {
        // Check if customer has an unconfirmed order
        boolean hasUnconfirmedOrder = false;
        int orderId = -1;
        try (Connection con = Main.getDatabase()) {
            PreparedStatement stmt = con.prepareStatement("SELECT order_id FROM orders WHERE customer_id = ? AND NOT EXISTS (SELECT 1 FROM confirmation WHERE confirmation.order_id = orders.order_id)");
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                hasUnconfirmedOrder = true;
                orderId = rs.getInt("order_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // If customer has no unconfirmed orders, create a new order
        if (!hasUnconfirmedOrder) {
            try (Connection conn = Main.getDatabase()) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO orders (customer_id, date) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, customerId);
                stmt.setDate(2, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Add the product to the order and decrease the product quantity
        try (Connection conn = Main.getDatabase()) {
            // Check if the product exists and has enough quantity
            PreparedStatement checkStmt = conn.prepareStatement("SELECT p_baseprice, p_supplier, p_quantity FROM product WHERE p_id = ? AND p_quantity >= ?");
            checkStmt.setInt(1, productId);
            checkStmt.setInt(2, quantity);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                throw new IllegalArgumentException("Invalid product ID or not enough quantity");
            }


            double pPrice = rs.getDouble("p_baseprice");
            int pSupplier = rs.getInt("p_supplier");
            int pQuantity = rs.getInt("p_quantity") - quantity;

            // Update the product quantity
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE product SET p_quantity = ? WHERE p_id = ?");
            updateStmt.setInt(1, pQuantity);
            updateStmt.setInt(2, productId);
            updateStmt.executeUpdate();

            // Add the product to the order
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO orderitems (order_id, p_id, p_name, quantity, base_price, supplier_id) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, pPrice);
            stmt.setInt(5, pSupplier);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Product added to order.");
    }


}

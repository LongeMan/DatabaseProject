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

            double pBasePrice = rs.getDouble("p_baseprice");
            double totalPrice = pBasePrice * quantity;
            int pSupplier = rs.getInt("p_supplier");
            int pQuantity = rs.getInt("p_quantity") - quantity;

            // Update the product quantity
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE product SET p_quantity = ? WHERE p_id = ?");
            updateStmt.setInt(1, pQuantity);
            updateStmt.setInt(2, productId);
            updateStmt.executeUpdate();

            // Add the product to the order
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO orderitems (order_id, p_id, quantity, price, supplier_id) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, totalPrice);
            stmt.setInt(5, pSupplier);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        System.out.println("Product added to order.");
    }


    public static void ShowcurrentOrder(int customerId) {
        try (Connection conn = Main.getDatabase()) {
            // Check if there is an unconfirmed order for the given customer
            PreparedStatement stmt = conn.prepareStatement("SELECT order_id FROM orders WHERE customer_id = ? AND NOT EXISTS (SELECT 1 FROM confirmation WHERE confirmation.order_id = orders.order_id)");
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("There is no unconfirmed order for the customer.");
                return;
            }

            // Get the order details and order items
            int orderId = rs.getInt("order_id");
            stmt = conn.prepareStatement("SELECT p_name, quantity, price FROM orderitems JOIN product ON orderitems.p_id = product.p_id WHERE order_id = ?");
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            // Print the order details
            System.out.println("Order Details:");
            while (rs.next()) {
                String pName = rs.getString("p_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                System.out.println("- " + pName + " x " + quantity + " = $" + price);
            }

            // Get the total price of all the order items
            stmt = conn.prepareStatement("SELECT SUM(price) AS total_price FROM orderitems WHERE order_id = ?");
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                double totalPrice = rs.getDouble("total_price");
                System.out.println("Total Price: $" + totalPrice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ShowAllOrders(int customerId) {
        try (Connection conn = Main.getDatabase()) {
            // Retrieve all orders for the given customer
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders WHERE customer_id = ?");
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            // Print out each order
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                Date orderDate = rs.getDate("date");
                System.out.printf("Order ID: %d, Date: %s%n", orderId, orderDate);

                // Retrieve the order items for the current order
                PreparedStatement itemsStmt = conn.prepareStatement("SELECT * FROM orderitems WHERE order_id = ?");
                itemsStmt.setInt(1, orderId);
                ResultSet itemsRs = itemsStmt.executeQuery();

                // Print out each order item
                while (itemsRs.next()) {
                    int productId = itemsRs.getInt("p_id");
                    int quantity = itemsRs.getInt("quantity");
                    double price = itemsRs.getDouble("price");
                    int supplierId = itemsRs.getInt("supplier_id");
                    System.out.printf("\tProduct ID: %d, Quantity: %d, Price: %.2f, Supplier ID: %d%n", productId, quantity, price, supplierId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

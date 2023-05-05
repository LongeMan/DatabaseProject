package Controller;

import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class Order {

    public static void addProductToOrder(int customerId, int productId, int quantity) {
        try (Connection conn = Main.getDatabase()) {
            // Check if the product exists and has enough quantity
            PreparedStatement checkStmt = conn.prepareStatement("SELECT p_baseprice, p_supplier, p_quantity FROM product WHERE p_id = ?");
            checkStmt.setInt(1, productId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                throw new IllegalArgumentException("Invalid product ID");
            }

            double pBasePrice = rs.getDouble("p_baseprice");
            int pSupplier = rs.getInt("p_supplier");
            int pQuantity = rs.getInt("p_quantity");

            // Check if there is enough stock for the requested quantity
            if (quantity > pQuantity) {
                System.out.println("There is not enough stock for your requested quantity");
                return;
            }

            // Check if customer has an unconfirmed order
            boolean hasUnconfirmedOrder = false;
            int cartQuantity = 0;
            double totalPrice = 0;

            int orderId = -1;
            PreparedStatement selectStmt = conn.prepareStatement("SELECT order_id FROM orders WHERE customer_id = ? AND NOT EXISTS (SELECT 1 FROM confirmation WHERE confirmation.order_id = orders.order_id)");
            selectStmt.setInt(1, customerId);
            ResultSet selectRs = selectStmt.executeQuery();

            if (selectRs.next()) {
                hasUnconfirmedOrder = true;
                orderId = selectRs.getInt("order_id");
            }

            // If customer has no unconfirmed orders, create a new order
            if (!hasUnconfirmedOrder) {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO orders (customer_id, date) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setInt(1, customerId);
                insertStmt.setDate(2, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                insertStmt.executeUpdate();

                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                }
            }

            // Check if the product is already in the order and update the quantity if so
            boolean productExistsInOrder = false;
            PreparedStatement orderItemStmt = conn.prepareStatement("SELECT quantity, price FROM orderitems WHERE order_id = ? AND p_id = ?");
            orderItemStmt.setInt(1, orderId);
            orderItemStmt.setInt(2, productId);
            ResultSet orderItemRs = orderItemStmt.executeQuery();

            if (orderItemRs.next()) {
                productExistsInOrder = true;
                cartQuantity = orderItemRs.getInt("quantity");
                cartQuantity += quantity;
                totalPrice = orderItemRs.getDouble("price") + pBasePrice * quantity;
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE orderitems SET quantity = ?, price = ? WHERE order_id = ? AND p_id = ?");
                updateStmt.setInt(1, cartQuantity);
                updateStmt.setDouble(2, totalPrice);
                updateStmt.setInt(3, orderId);
                updateStmt.setInt(4, productId);
                updateStmt.executeUpdate();
            } else {
                // Calculate the total price based on the new quantity
                totalPrice = pBasePrice * quantity;
            }

            // Update the product quantity
            int newQuantity = pQuantity - quantity;
            PreparedStatement updateProductStmt = conn.prepareStatement("UPDATE product SET p_quantity = ? WHERE p_id = ?");
            updateProductStmt.setInt(1, newQuantity);
            updateProductStmt.setInt(2, productId);
            updateProductStmt.executeUpdate();


            // Add the product to the order
            if (!productExistsInOrder) { // Add the product to order only if it doesn't exist already
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO orderitems (order_id, p_id, quantity, price, supplier_id) VALUES (?, ?, ?, ?, ?)");
                stmt.setInt(1, orderId);
                stmt.setInt(2, productId);
                stmt.setInt(3, quantity);
                stmt.setDouble(4, totalPrice);
                stmt.setInt(5, pSupplier);
                stmt.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void ShowCurrentOrder(int customerId) {
        try (Connection conn = Main.getDatabase()) {
            // Check if there is an unconfirmed order for the given customer
            PreparedStatement stmt = conn.prepareStatement("SELECT order_id, date FROM orders WHERE customer_id = ? AND NOT EXISTS (SELECT 1 FROM confirmation WHERE confirmation.order_id = orders.order_id)");
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("There is no unconfirmed order for the customer.");
                return;
            }

            // Get the order details and order items
            int orderId = rs.getInt("order_id");
            Date orderDate = rs.getDate("date");

            stmt = conn.prepareStatement("SELECT oi.p_id, p.p_name, oi.quantity, oi.price, d.discount, dr.reason as discount_reason FROM orderitems oi JOIN product p ON oi.p_id = p.p_id LEFT JOIN discounts d ON d.p_id = oi.p_id AND d.start_date <= ? AND (d.start_date + (d.period * INTERVAL '1 DAY')) >= ? LEFT JOIN discountreason dr ON d.discountreason_id = dr.discountreason_id WHERE oi.order_id = ?");
            stmt.setDate(1, orderDate);
            stmt.setDate(2, orderDate);
            stmt.setInt(3, orderId);
            rs = stmt.executeQuery();

            // Print the order details
            System.out.println("Order Details (Order ID: " + orderId + ", Date: " + orderDate + "):");
            while (rs.next()) {
                String pName = rs.getString("p_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double discount = rs.getDouble("discount");
                String discountReason = rs.getString("discount_reason");

                // Calculate discounted price if discount exists
                double discountedPrice = discount > 0 ? price * (1 - discount) : price;
                String discountText = discount > 0 ? " (discount applied - " + discountReason + ")" : "";

                System.out.println("- " + pName + " x " + quantity + " = $" + discountedPrice + discountText);
            }

            // Get the total price of all the order items
            stmt = conn.prepareStatement("SELECT SUM(price) AS total_price FROM orderitems WHERE order_id = ?");
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                double totalPrice = rs.getDouble("total_price");
                System.out.println("Total Price: $" + totalPrice);

                // Prompt for payment confirmation
                System.out.print("Enter 'Y' to confirm payment for the order: ");
                Scanner scanner = new Scanner(System.in);
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("Y")) {
                    // Insert a new record into the confirmation table with confirmed set to false
                    stmt = conn.prepareStatement("INSERT INTO public.confirmation (price, date, order_id, confirmed) VALUES (?, ?, ?, false)");
                    stmt.setDouble(1, totalPrice);
                    stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    stmt.setInt(3, orderId);
                    stmt.executeUpdate();
                    System.out.println("Payment confirmed.");
                } else {
                    System.out.println("Payment not confirmed.");
                }

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
                PreparedStatement itemsStmt = conn.prepareStatement(
                        "SELECT orderitems.*, product.p_name FROM orderitems " +
                                "JOIN product ON orderitems.p_id = product.p_id " +
                                "WHERE order_id = ?");
                itemsStmt.setInt(1, orderId);
                ResultSet itemsRs = itemsStmt.executeQuery();

                // Print out each order item
                while (itemsRs.next()) {
                    String productName = itemsRs.getString("p_name");
                    int quantity = itemsRs.getInt("quantity");
                    double price = itemsRs.getDouble("price");
                    int supplierId = itemsRs.getInt("supplier_id");
                    System.out.printf("\tProduct: %s, Quantity: %d, Price: %.2f, Supplier ID: %d%n", productName, quantity, price, supplierId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public static void removeOrder(int orderID) {
        try (Connection conn = Main.getDatabase()) {

            // Get all the items in the order
            String getOrderItems = "SELECT * FROM orderitems WHERE order_id = ?";
            PreparedStatement psGetOrderItems = conn.prepareStatement(getOrderItems);
            psGetOrderItems.setInt(1, orderID);
            ResultSet rsOrderItems = psGetOrderItems.executeQuery();

            // For each item, update the product quantity in the product table
            String updateProductQty = "UPDATE product SET p_quantity = p_quantity + ? WHERE p_id = ?";
            PreparedStatement psUpdateProductQty = conn.prepareStatement(updateProductQty);
            while (rsOrderItems.next()) {
                int productID = rsOrderItems.getInt("p_id");
                int quantity = rsOrderItems.getInt("quantity");
                psUpdateProductQty.setInt(1, quantity);
                psUpdateProductQty.setInt(2, productID);
                psUpdateProductQty.executeUpdate();
            }

            // Delete all order items with the given order ID
            String deleteOrderItems = "DELETE FROM orderitems WHERE order_id = ?";
            PreparedStatement psDeleteItems = conn.prepareStatement(deleteOrderItems);
            psDeleteItems.setInt(1, orderID);
            psDeleteItems.executeUpdate();

            // Delete the order with the given order ID
            String deleteOrder = "DELETE FROM orders WHERE order_id = ?";
            PreparedStatement psDeleteOrder = conn.prepareStatement(deleteOrder);
            psDeleteOrder.setInt(1, orderID);
            psDeleteOrder.executeUpdate();

            // Delete the confirmation post with the given order ID
            String deleteConfirmation = "DELETE FROM confirmation WHERE order_id = ?";
            PreparedStatement psDeleteConfirmation = conn.prepareStatement(deleteConfirmation);
            psDeleteConfirmation.setInt(1, orderID);
            psDeleteConfirmation.executeUpdate();



        } catch (SQLException e) {
            System.out.println("An error occurred while removing the order: " + e.getMessage());
        }
    }

    public static void removeCurrentOrderItems(int customerID) {
        try (Connection conn = Main.getDatabase()) {
            // Get the latest order for the customer that doesn't have a confirmation post
            String getOrder = "SELECT * FROM orders WHERE customer_id = ? AND order_id NOT IN (SELECT order_id FROM confirmation)";
            PreparedStatement psGetOrder = conn.prepareStatement(getOrder);
            psGetOrder.setInt(1, customerID);
            ResultSet rsOrder = psGetOrder.executeQuery();

            if (rsOrder.next()) {
                int orderID = rsOrder.getInt("order_id");

                // Get all the items in the order
                String getOrderItems = "SELECT * FROM orderitems WHERE order_id = ?";
                PreparedStatement psGetOrderItems = conn.prepareStatement(getOrderItems);
                psGetOrderItems.setInt(1, orderID);
                ResultSet rsOrderItems = psGetOrderItems.executeQuery();

                // For each item, update the product quantity in the product table
                String updateProductQty = "UPDATE product SET p_quantity = p_quantity + ? WHERE p_id = ?";
                PreparedStatement psUpdateProductQty = conn.prepareStatement(updateProductQty);
                while (rsOrderItems.next()) {
                    int productID = rsOrderItems.getInt("p_id");
                    int quantity = rsOrderItems.getInt("quantity");
                    psUpdateProductQty.setInt(1, quantity);
                    psUpdateProductQty.setInt(2, productID);
                    psUpdateProductQty.executeUpdate();
                }

                // Delete all order items with the given order ID
                String deleteOrderItems = "DELETE FROM orderitems WHERE order_id = ?";
                PreparedStatement psDeleteItems = conn.prepareStatement(deleteOrderItems);
                psDeleteItems.setInt(1, orderID);
                psDeleteItems.executeUpdate();

                // Delete the order with the given order ID
                String deleteOrder = "DELETE FROM orders WHERE order_id = ?";
                PreparedStatement psDeleteOrder = conn.prepareStatement(deleteOrder);
                psDeleteOrder.setInt(1, orderID);
                psDeleteOrder.executeUpdate();
                System.out.println("Removed current order");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while removing the order: " + e.getMessage());
        }
    }

    public static void confirmOrder(int orderId) throws SQLException {
        try (Connection conn = Main.getDatabase()) {
            try (PreparedStatement sumStatement = conn.prepareStatement("SELECT SUM(price * quantity) as total_price FROM orderitems WHERE order_id = ?")) {
                sumStatement.setInt(1, orderId);
                ResultSet resultSet = sumStatement.executeQuery();
                if (resultSet.next()) {
                    double totalPrice = resultSet.getDouble("total_price");

                    try (PreparedStatement confirmationStatement = conn.prepareStatement("UPDATE public.confirmation SET confirmed = true WHERE order_id = ?")) {
                        confirmationStatement.setInt(1, orderId);
                        confirmationStatement.executeUpdate();
                        System.out.println("Order with ID " + orderId + " has been confirmed.");
                    }
                } else {
                    // Handle case where order has no items
                    System.out.println("This order has no items and cannot be confirmed");
                }
            }
        }
    }

    public static void printUnconfirmedOrders() {
        try (Connection conn = Main.getDatabase()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders WHERE NOT EXISTS (SELECT 1 FROM confirmation WHERE confirmation.order_id = orders.order_id AND confirmed = true)");

            // Execute the statement and get the result set
            ResultSet rs = stmt.executeQuery();

            // Loop through the result set and print each unconfirmed order
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int customerId = rs.getInt("customer_id");
                Date date = rs.getDate("date");
                System.out.println("Order ID: " + orderId);
                System.out.println("Customer ID: " + customerId);
                System.out.println("Order Date: " + date);
                System.out.println();
            }

            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Supplier {



    public static void addSupplier(String name, String phone, String address) {
        try (Connection con = Main.getDatabase();
             PreparedStatement stmt = con.prepareStatement("INSERT INTO Supplier(s_name, s_phone, s_address) VALUES (?, ?, ?)")) {

            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, address);
            stmt.executeUpdate();

            System.out.println("Supplier added successfully");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


    public static void printAllSuppliers() {
        try (Connection con = Main.getDatabase()) {
            String sql = "SELECT * FROM SUPPLIER";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String supplierId = rs.getString("supplier_id");
                String pName = rs.getString("s_name");


                System.out.println("Product ID: " + supplierId);
                System.out.println("Product Name: " + pName);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }




}

package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;

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

}

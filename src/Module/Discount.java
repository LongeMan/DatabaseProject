package Module;
import Controller.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Discount {
    private int pID;
    private double discount;
    private int period;
    private String dCode;

    public void addDiscount(int pID, double discount, int period, String dCode){
        try(Connection con = Main.getDatabase()){
            String sql = "INSERT INTO DISCOUNT(p_id,discount,period,d_code) VALUES(?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1,pID);
            stmt.setDouble(2,discount);
            stmt.setInt(3,period);
            stmt.setString(4,dCode);

            stmt.executeUpdate();
            System.out.println("Record inserted successfully");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void checkDiscount(String dCode){
        try (Connection con = Main.getDatabase()){
            String sql = "SELECT FROM DISCOUNT WHERE (d_code = ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,dCode);
            stmt.executeUpdate();
            System.out.println("Record inserted successfully");



        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

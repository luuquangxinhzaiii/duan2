/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class JdbcHelper {
    BCrypt bcrypt;
    private static String driver = "org.postgresql.Driver";
    private static String dburl = "jdbc:postgresql://localhost:5432/fpt";
    private static String username = "postgres";
    private static String password = "tuan123";   
    
//    private static String driver = "org.postgresql.Driver";
//    private static String dburl = "jdbc:postgresql://35.213.128.4:5432/datn";
//    private static String username = "huyenntn";
//    private static String password = "huyenntnph08193";

    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

//     test ket noi
    public static void main(String[] args) throws SQLException {

        try {
            Connection connection = DriverManager.getConnection(dburl, username, password);
            String s = BCrypt.hashpw("123", BCrypt.gensalt(12));
            boolean saa = BCrypt.checkpw("123", "$2a$12$8Qi8sI6aqNKLS1Vmw3FVOedjb.dhXznGIaplOdWFsP/2ubhm2DxIe");
            System.out.println("ok");
        } catch (Exception e) {
            System.out.println("thatbai");
            e.printStackTrace();
        }

    }

    public static PreparedStatement prepareStatement(String sql, Object... args) throws SQLException {
        Connection connection = DriverManager.getConnection(dburl, username, password);
        PreparedStatement pstmt = null;
        if (sql.trim().startsWith("{")) {
            pstmt = connection.prepareCall(sql);
        } else {
            pstmt = connection.prepareStatement(sql);
        }
        for (int i = 0; i < args.length; i++) {
            pstmt.setObject(i + 1, args[i]);
        }
        return pstmt;
    }

    public static void executeUpdate(String sql, Object... args) {
        try {

            PreparedStatement stmt = prepareStatement(sql, args);
            try {
                stmt.executeUpdate();
            } finally {
                stmt.getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet executeQuery(String sql, Object... args) {
        try {
            PreparedStatement stmt = prepareStatement(sql, args);
            return stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

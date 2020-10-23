/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.Encryption;
import da.helper.JdbcHelper;
import da.model.TaiKhoan;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BNC
 */
public class TaiKhoanDAO {

    JdbcHelper Jdbc = new JdbcHelper();
    Encryption encrypt = new Encryption();

//    public void insert(TaiKhoan model) {
//        String passWord = encrypt.encrypt(model.getPassWord(), "DMM");
//        String sql = "insert into account values(?,?,?,?)";
//        Jdbc.executeUpdate(sql, model.getUserName(), passWord, model.getRole(), model.getID());
//    }
    public void delete(String MaNV) {
        String sql = "DELETE FROM account WHERE username=?";
        Jdbc.executeUpdate(sql, MaNV);
    }

    public List<TaiKhoan> select() {
        String sql = "SELECT * FROM account";
        return select(sql);
    }

    public ResultSet select2() {
        String sql = "select *  from account";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet select3(String stt) {
        String sql = "select *  from account where stt=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, stt);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public TaiKhoan findById(String username) {
        String sql = "SELECT * FROM account where email=?";
        List<TaiKhoan> list = select(sql, username);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<TaiKhoan> select(String sql, Object... args) {
        List<TaiKhoan> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = Jdbc.executeQuery(sql, args);
                while (rs.next()) {
                    TaiKhoan model = readFromResultSet(rs);
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private TaiKhoan readFromResultSet(ResultSet rs) throws SQLException {
        TaiKhoan model = new TaiKhoan();
        model.setStt(rs.getInt("stt"));
        model.setMaGiaoVien(rs.getString("magiaovien"));
        model.setMaHocSinh(rs.getString("mahocsinh"));
        model.setPassWord(rs.getString("pass"));
        model.setRole(rs.getString("roles"));

        model.setEmail(rs.getString("email"));
        return model;
    }

    public void update2(String tk, String mk) {
        String sql = "UPDATE account SET  pass=? WHERE  email =?";
        JdbcHelper.executeUpdate(sql, mk, tk);
    }

    public void update(TaiKhoan model) {

        String sql = "update account set email=?,pass=?,roles=?,magiaovien=?,mahocsinh=? where stt=?";
        JdbcHelper.executeUpdate(sql, model.getEmail(), model.getPassWord(), model.getRole(), model.getMaGiaoVien(), model.getMaHocSinh(), model.getStt());
    }

    public void insertTKGV(TaiKhoan model) {

        String sql = "insert into account (email,pass,roles,magiaovien) values(?,?,?,?)";

        JdbcHelper.executeUpdate(sql, model.getEmail(), model.getPassWord(), model.getRole(), model.getMaGiaoVien());
    }

    public void insertTKHS(TaiKhoan model) {

        String sql = "insert into account (email,pass,roles,mahocsinh) values(?,?,?,?)";

        JdbcHelper.executeUpdate(sql, model.getEmail(), model.getPassWord(), model.getRole(), model.getMaHocSinh());
    }

    public void update3(TaiKhoan model) {

        String sql = "update account set email=?,pass=?,roles=? where stt=?";
        JdbcHelper.executeUpdate(sql, model.getEmail(), model.getPassWord(), model.getRole(), model.getStt());
    }

    public void delete(int stt) {
        String sql = "delete from account where stt=?";
        JdbcHelper.executeUpdate(sql, stt);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.JdbcHelper;
import da.model.Mon;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MonDAO {

    JdbcHelper Jdbc = new JdbcHelper();

    private Mon readFromResultSet(ResultSet rs) throws SQLException {
        Mon model = new Mon();
        model.setMaMon(rs.getString("mamon"));
        model.setTenMon(rs.getString("tenmon"));
        model.setHinhThucDG(rs.getBoolean("hinhthucdanhgia"));
        model.setMaKhoi(rs.getString("makhoi"));
        return model;
    }
    
    
    public ResultSet loadDataNotExits(String maKhoi){
        String sql = "SELECT * from mon where mon.makhoi =? and not EXISTS(SELECT * from phancong WHERE phancong.mamon = mon.mamon)";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, maKhoi);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void update(Mon model){
        String sql = "update mon set tenmon=?, hinhthucdanhgia=?, makhoi=? where mamon =?";
        Jdbc.executeUpdate(sql, model.getTenMon(), model.getHinhThucDG(), model.getMaKhoi(), model.getMaMon());
    }
    public void insert(Mon model){
        String sql = "insert into mon values(?,?,?,?)";
        Jdbc.executeUpdate(sql, model.getMaMon(), model.getTenMon(), model.getHinhThucDG(), model.getMaKhoi());
    }

    public List<Mon> selectSubject() {
        String sql = "select * from mon";
        return select(sql);
    }
    private List<Mon> select(String sql, Object... args) {
        List<Mon> list = new ArrayList<>();
        try {
            ResultSet rs = null;

            rs = JdbcHelper.executeQuery(sql, args);
            while (rs.next()) {
                Mon model = readFromResultSet(rs);
                list.add(model);

            }
            rs.getStatement().getConnection().close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;

    }
    
    public ResultSet selectByKhoi(String maKhoi) {
        String sql = "select * from mon where makhoi=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(0, maKhoi);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet select() {
        String sql = "select *  from mon";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }
    
       public ResultSet select2(String mamon) {
        String sql = "select tenmon from mon where mamon=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, mamon);
            
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }
         public ResultSet select3(String tenmon) {
        String sql = "select hinhthucdanhgia from mon where tenmon=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenmon);
            
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

}

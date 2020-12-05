/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.JdbcHelper;
import da.model.DiemDanh;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author BNC
 */
public class DiemDanhDAO {
    JdbcHelper Jdbc = new JdbcHelper();

    public List<DiemDanh> select() {
        String sql = "SELECT * danhgia";
        return select(sql);
    }
    
    public int selectNghiCoPhep(String maHS){
        String sql = "select count(hocsinh_id) as solannghihoc from diemdanh join hocsinh on diemdanh.hocsinh_id = hocsinh.id where hocsinh.mahocsinh = ? and diemdanh.trangthai = true ";
        int sobuoinghicophep = 0;
        try{
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, maHS);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                sobuoinghicophep = rs.getInt("solannghihoc");
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return sobuoinghicophep;
    }
    
    public int selectNghiKoCoPhep(String maHS){
        String sql = "select count(hocsinh_id) as solannghihoc from diemdanh join hocsinh on diemdanh.hocsinh_id = hocsinh.id where hocsinh.mahocsinh = ? and diemdanh.trangthai = false ";
        int sobuoinghikophep = 0;
        try{
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, maHS);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                sobuoinghikophep = rs.getInt("solannghihoc");
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return sobuoinghikophep;
    }
    
    public List<DiemDanh> selectByDG() {
        String sql = "SELECT * FROM mon where hinhthucdanhgia ='1' ";
        return select(sql);
    }
   
    private List<DiemDanh> select(String sql, Object... args) {
        List<DiemDanh> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = Jdbc.executeQuery(sql, args);
                while (rs.next()) {
                    DiemDanh model = readFromResultSet(rs);
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

    private DiemDanh readFromResultSet(ResultSet rs) throws SQLException {
        DiemDanh model = new DiemDanh();
        model.setId(UUID.fromString(rs.getString("id")));
        model.setNgay(rs.getDate("ngay"));
        model.setMaGv(UUID.fromString(rs.getString("giaovien_magiaovien")));
        model.setMaHs(UUID.fromString(rs.getString("hocsinh_id")));
        model.setTrangThai(rs.getBoolean("trangthai"));       
        return model;
    }
}

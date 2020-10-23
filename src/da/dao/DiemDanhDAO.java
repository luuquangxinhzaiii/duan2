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
        String sql = "select count(mahocsinh) as solannghihoc from diemdanh where mahocsinh = ? and trangthai = 0 ";
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
        String sql = "select count(mahocsinh) as solannghihoc from diemdanh where mahocsinh = ? and trangthai = 1 ";
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
        model.setNgay(rs.getString("ngay"));
        model.setMaGv(rs.getString("magiaovien"));
        model.setMaHs(rs.getString("mahocsinh"));
        model.setTrangThai(rs.getBoolean("trangthai"));       
        return model;
    }
}

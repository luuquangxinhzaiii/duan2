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
        String sql = "SELECT * diemdanh";
        return select(sql);
    }

    public int selectNghiCoPhep(String maHS) {
        String sql = "select count(hocsinh_id) as solannghihoc from diemdanh join hocsinh on diemdanh.hocsinh_id = hocsinh.id where hocsinh.mahocsinh = ? and diemdanh.trangthai = true ";
        int sobuoinghicophep = 0;
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, maHS);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sobuoinghicophep = rs.getInt("solannghihoc");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return sobuoinghicophep;
    }

    public int selectNghiKoCoPhep(String maHS) {
        String sql = "select count(hocsinh_id) as solannghihoc from diemdanh join hocsinh on diemdanh.hocsinh_id = hocsinh.id where hocsinh.mahocsinh = ? and diemdanh.trangthai = false ";
        int sobuoinghikophep = 0;
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, maHS);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sobuoinghikophep = rs.getInt("solannghihoc");
            }
        } catch (SQLException ex) {
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

    public int selectNghi_ki(String maHS, boolean ki, boolean status) {
        String sql = "select count(hocsinh_id) as solannghihoc from diemdanh join hocsinh on diemdanh.hocsinh_id = hocsinh.id where hocsinh.mahocsinh = ? and diemdanh.hocki = ? and diemdanh.trangthai = ? ";
        int sobuoinghi = 0;
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, maHS);
            ps.setBoolean(2, ki);
            ps.setBoolean(3, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sobuoinghi = rs.getInt("solannghihoc");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return sobuoinghi;
    }

    private DiemDanh readFromResultSet(ResultSet rs) throws SQLException {
        DiemDanh model = new DiemDanh();
        model.setId(UUID.fromString(rs.getString("id")));
        model.setHocki(rs.getBoolean("hocki"));
        model.setNgay(rs.getDate("ngay"));
        model.setMaGv(UUID.fromString(rs.getString("giaovien_id")));
        model.setMaHs(UUID.fromString(rs.getString("hocsinh_id")));
        model.setTrangThai(rs.getBoolean("trangthai"));   
        model.setNamhoc(UUID.fromString(rs.getString("namhoc_manamhoc")));
        return model;
    }

    public ResultSet loadData(String tenlop) {
        String sql = "select dd.ngay, hs,hoten, dd.trangthai, dd.hocki, nh.nienhoc from diemdanh"
                + " dd join hocsinh hs on dd.hocsinh_id = hs.id join namhoc nh on dd.namhoc_manamhoc = nh.manamhoc "
                + "join lophoc lh on hs.lop_id= lh.id where lh.tenlop=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenlop);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }
    
    public void insert(DiemDanh model) {
        String sql = "insert into diemdanh(id,hocki,ngay,trangthai,giaovien_id,hocsinh_id,namhoc_manamhoc) values(?,?,?,?,?,?,?)";
        JdbcHelper.executeUpdate(sql, UUID.randomUUID(), model.getHocki(), model.getNgay(), model.getTrangThai(), model.getMaGv(), model.getMaHs(), model.getNamhoc());
    }
}

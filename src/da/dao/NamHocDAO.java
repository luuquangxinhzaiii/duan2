/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.DateHelper;
import da.helper.JdbcHelper;
import da.model.NamHoc;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Administrator
 */
public class NamHocDAO {

    JdbcHelper Jdbc = new JdbcHelper();

    private NamHoc readFromResultSet(ResultSet rs) throws SQLException {
        NamHoc model = new NamHoc();
        
        model.setMaNamHoc(UUID.fromString(rs.getString("manamhoc")));
        model.setNienHoc(rs.getString("nienhoc"));
        model.setNgayBD(rs.getDate("ngaybatdau"));
        model.setNgayKT(rs.getDate("ngayketthuc"));
        model.setTrangThai(rs.getBoolean("trangthai"));
        return model;
    }

    private List<NamHoc> select(String sql, Object... args) {
        List<NamHoc> list = new ArrayList<>();
        try {
            ResultSet rs = null;

            rs = JdbcHelper.executeQuery(sql, args);
            while (rs.next()) {
                NamHoc model = readFromResultSet(rs);
                list.add(model);

            }
            rs.getStatement().getConnection().close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;

    }
    
    public List<NamHoc> checkTrangThai(){
        String sql = "SELECT * from namhoc where trangthai = true";
        return select(sql);
    }

    public List<NamHoc> select() {
        String sql = "select * from namhoc where trangthai = true";
        return select(sql);
    }
    
    public NamHoc findByNienHoc(String nienhoc) {
        String sql = "select * from namhoc where trangthai = true and nienhoc = ?";
        List<NamHoc> list = select(sql, nienhoc);
        return list.size() >0 ? list.get(0) : null;
    }

    public ResultSet select2(String nienhoc) {
        String sql = "select manamhoc from namhoc where nienhoc=? and trangthai=true";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, nienhoc);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public void insert(NamHoc model) {
        Date ngaybd = Date.valueOf(DateHelper.toString(model.getNgayBD()));
        Date ngaykt = Date.valueOf(DateHelper.toString(model.getNgayKT()));
        String sql = "insert into namhoc(manamhoc,nienhoc,ngaybatdau,ngayketthuc,trangthai) values(?,?,?,?,true)";
        JdbcHelper.executeUpdate(sql, UUID.randomUUID(), model.getNienHoc(), ngaybd, ngaykt);

    }

    public ResultSet selectid() {
        String sql = "select max(manamhoc)  from namhoc";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public void ketthuc(UUID manamhoc) {
        String sql
                = "UPDATE namhoc SET trangthai=false WHERE manamhoc =  ?";
        JdbcHelper.executeUpdate(sql,
                manamhoc);
    }

    
}

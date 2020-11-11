/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.JdbcHelper;
import da.model.NamHoc;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class NamHocDAO {

    JdbcHelper Jdbc = new JdbcHelper();

    private NamHoc readFromResultSet(ResultSet rs) throws SQLException {
        NamHoc model = new NamHoc();
        
        model.setMaNamHoc(rs.getString("manamhoc"));
        model.setNienHoc(rs.getString("nienhoc"));
        model.setNgayBD(rs.getDate("ngaybatdau"));
        model.setNgayKT(rs.getDate("ngayketthuc"));
        model.setTrangThai(rs.getString("trangthai"));
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

    public List<NamHoc> select() {
        String sql = "select * from namhoc";
        return select(sql);
    }

    public ResultSet select2(String nienhoc) {
        String sql = "select manamhoc from namhoc where nienhoc=?";
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
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "insert into namhoc(manamhoc,nienhoc,ngaybatdau,ngayketthuc,trangthai,xoa) values(?,?,?,?,'Đang học','1')";

        JdbcHelper.executeUpdate(sql, model.getMaNamHoc(), model.getNienHoc(), sfd.format(model.getNgayBD()), sfd.format(model.getNgayKT()));

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

    public void ketthuc(NamHoc model) {
        String sql
                = "UPDATE namhoc SET trangthai='Kết thúc' WHERE  manamhoc =  ?";
        JdbcHelper.executeUpdate(sql,
                model.getMaNamHoc());
    }

    public void xoanamhoc(NamHoc model) {
        String sql
                = "UPDATE namhoc SET xoa='0' WHERE  manamhoc =  ?";
        JdbcHelper.executeUpdate(sql,
                model.getMaNamHoc());
    }
}

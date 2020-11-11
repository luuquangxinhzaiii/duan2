/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.JdbcHelper;
import da.model.LopHoc;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class LopHocDAO {

    JdbcHelper Jdbc = new JdbcHelper();

    private LopHoc readFromResultSet(ResultSet rs) throws SQLException {
        LopHoc model = new LopHoc();
        model.setiD(rs.getInt("id"));
        model.setMaLop(rs.getString("malop"));
        model.setTenLop(rs.getString("tenlop"));

        model.setMaNH(rs.getString("manamhoc"));
        model.setMaKhoi(rs.getString("makhoi"));
        return model;

    }

    private List<LopHoc> select(String sql, Object... args) {
        List<LopHoc> list = new ArrayList<>();
        try {
            ResultSet rs = null;

            rs = JdbcHelper.executeQuery(sql, args);
            while (rs.next()) {
                LopHoc model = readFromResultSet(rs);
                list.add(model);

            }
            rs.getStatement().getConnection().close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;

    }

    public List<LopHoc> select() {
        String sql = "select * from lophoc";
        return select(sql);
    }

    public ResultSet selectWithTenKhoi(String tenkhoi) {
        String sql = "select lophoc.tenlop from lophoc join khoi on lophoc.makhoi=khoi.makhoi and khoi.tenkhoi=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenkhoi);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet selectID() {
        String sql = "select max(id)  from lophoc";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet selectLopByNienhoc(String nienhoc) {
        String sql = "select lh.malop,lh.tenlop,nh.nienhoc from lophoc as lh join namhoc as nh on lh.manamhoc=nh.manamhoc and nh.nienhoc=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, nienhoc);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet selectWithMalop(String malop) {
        String sql = "select * from  lophoc where malop=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, malop);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet countWithMalop(String malop) {
        String sql = "select count(*) as ss from hocsinh where lop=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, malop);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }
    
    public LopHoc findByTenLop(String tenLop){
        String sql = "SELECT * FROM lophoc WHERE tenlop=?";
        List<LopHoc> list = select(sql, tenLop);
        return list.size() > 0 ? list.get(0) : null;
    }
    
    public List<LopHoc> findClass(String maLop) {
        String sql = "SELECT * FROM lophoc WHERE malop=?";
        List<LopHoc> list = select(sql, maLop);
        return list.size() > 0 ? list : null;
    }

    public void insert(LopHoc model) {

        String sql = "insert into lophoc(malop,tenlop,manamhoc,makhoi) values(?,?,?,?)";

        JdbcHelper.executeUpdate(sql, model.getMaLop(), model.getTenLop(), model.getMaNH(), model.getMaKhoi());
    }

    public ResultSet selectWithTenlop(String tenlop) {
        String sql = " select malop from lophoc where tenlop=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenlop);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public void update(LopHoc model) {

        String sql = "update lophoc set tenlop=?,manamhoc=?,makhoi=? where malop=?";
        JdbcHelper.executeUpdate(sql, model.getTenLop(), model.getMaNH(), model.getMaKhoi(), model.getMaLop());
    }
}

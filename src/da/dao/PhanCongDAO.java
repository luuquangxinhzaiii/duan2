/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.JdbcHelper;

import da.model.PhanCong;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Administrator
 */
public class PhanCongDAO {

    JdbcHelper Jdbc = new JdbcHelper();

    private PhanCong readFromResultSet(ResultSet rs) throws SQLException {
        PhanCong model = new PhanCong();
        model.setMaPC(UUID.fromString(rs.getString("mapc")));
        model.setMaLop(UUID.fromString(rs.getString("lop_id")));
        model.setMaGV(UUID.fromString(rs.getString("giaovien_id")));
        model.setVaiTro(rs.getBoolean("vaitro"));
        model.setMaMon(UUID.fromString(rs.getString("mon_mamon")));
        model.setHocKi(rs.getBoolean("hocki"));
        model.setMaNamHoc(UUID.fromString(rs.getString("namhoc_manamhoc")));
        return model;
    }

    private List<PhanCong> select(String sql, Object... args) {
        List<PhanCong> list = new ArrayList<>();
        try {
            ResultSet rs = null;

            rs = JdbcHelper.executeQuery(sql, args);
            while (rs.next()) {
                PhanCong model = readFromResultSet(rs);
                list.add(model);

            }
            rs.getStatement().getConnection().close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;

    }

    public List<PhanCong> select() {
        String sql = "select * from phancong";
        return select(sql);
    }

    public ResultSet select2() {
        String sql = "select max(id)  from phancong";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet select3(String nienhoc, Boolean ki) {
        String sql = "select pc.mapc,pc.malop,gv.hoten,m.tenmon,pc.vaitro,pc.mamon from phancong as pc join mon as m on pc.mamon=m.mamon join giaovien as gv on pc.magiaovien=gv.magiaovien join namhoc as nh on pc.manamhoc=nh.manamhoc and nh.nienhoc=? and pc.hocki=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, nienhoc);
            ps.setBoolean(2, ki);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet select4(String mapc) {
        String sql = "select pc.mapc,pc.malop,gv.hoten,m.tenmon,pc.vaitro,pc.magiaovien from phancong as pc join mon as m on pc.mamon=m.mamon join giaovien as gv on pc.magiaovien=gv.magiaovien join namhoc as nh on pc.manamhoc=nh.manamhoc and pc.mapc=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, mapc);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }
    
    public PhanCong selectPc(String maGv,String tenLop, String tenMon, boolean hocKi){
        String sql = "SELECT * FROM `phancong` inner join mon on phancong.mamon = mon.mamon INNER JOIN lophoc on phancong.malop = lophoc.malop where phancong.magiaovien = ? and lophoc.tenlop = ? and mon.tenmon= ? and hocki= ?";
        List<PhanCong> list = select(sql, maGv,tenLop, tenMon, hocKi);
        return list.size()>0 ? list.get(0) : null;
    }
    
    public ResultSet select5(String magv, boolean hocKi) {
        String sql = "select pc.maphancong,pc.lop_id,gv.hoten,m.ten_mon,m.hinhthucdanhgia,pc.vaitro,pc.giaovien_id from phancong as pc join mon as m on pc.mon_mamon=m.mamon join giaovien as gv on pc.giaovien_id=gv.id join namhoc as nh on pc.namhoc_manamhoc=nh.manamhoc and pc.giaovien_id=? and pc.hocki=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, magv);
            ps.setBoolean(2, hocKi);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }
    
    
    
    public List<PhanCong> selectLopCN(String maGv) {
        String sql = "select * from phancong where magiaovien = ? and vaitro = 0";
        List<PhanCong> list = select(sql, maGv);
        return list;
    }
    
    public List<PhanCong> select6(String maGv, boolean hocKi) {
        String sql = "select * from phancong where magiaovien = ? and hocki=?";
        List<PhanCong> list = select(sql, maGv, hocKi);
        return list;
    }

    public void insert(PhanCong model) {

        String sql = "insert into phancong(mapc,malop,magiaovien,vaitro,mamon,hocki,manamhoc) values(?,?,?,?,?,?,?)";

        JdbcHelper.executeUpdate(sql, model.getMaPC(), model.getMaLop(), model.getMaGV(), model.getVaiTro(), model.getMaMon(), model.getVaiTro(), model.getMaNamHoc());
    }

    public void update(PhanCong model) {

        String sql = "update phancong set malop=?,magiaovien=?,vaitro=?,mamon=?,hocki=?,manamhoc=? where mapc=?";
        JdbcHelper.executeUpdate(sql, model.getMaLop(), model.getMaGV(), model.getVaiTro(), model.getMaMon(), model.getHocKi(), model.getMaNamHoc(), model.getMaPC());
    }

    public void delete(String mapc) {
        String sql = "delete from phancong where mapc=?";
        JdbcHelper.executeUpdate(sql, mapc);
    }

}

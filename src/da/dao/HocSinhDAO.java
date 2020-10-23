/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.JdbcHelper;
import da.model.HocSinh;
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
public class HocSinhDAO {

    JdbcHelper Jdbc = new JdbcHelper();

    private HocSinh readFromResultSet(ResultSet rs) throws SQLException {
        HocSinh model = new HocSinh();
        model.setiD(rs.getInt("id"));
        model.setMaHS(rs.getString("mahocsinh"));
        model.setHoTen(rs.getString("hoten"));
        model.setGioiTinh(rs.getBoolean("gioitinh"));
        model.setNgaySinh(rs.getDate("ngaysinh"));
        model.setDiaChi(rs.getString("diachi"));
        model.setDienThoai(rs.getString("dienthoai"));
        model.setDanToc(rs.getString("dantoc"));
        model.setTonGiao(rs.getString("tongiao"));
        model.setNgayVD(rs.getDate("ngayvaodoan"));
        model.setNoiSinh(rs.getString("noisinh"));
        model.setCmND(rs.getString("cmnd"));
        model.setLop(rs.getString("lop"));
        model.setHotenBo(rs.getString("hotenBo"));
        model.setHotenMe(rs.getString("hotenMe"));
        model.setDienThoaiBo(rs.getString("dienthoaiBo"));
        model.setDienThoaiMe(rs.getString("dienthoaiMe"));
        model.setDvctBo(rs.getString("dvCongTacBo"));
        model.setDvctMe(rs.getString("dvCongTacMe"));
        model.setNguoiDamHo(rs.getString("nguoidamho"));
        model.setTrangThai(rs.getBoolean("trangthai"));
        model.setAnh(rs.getString("anh"));

        return model;

    }

    private List<HocSinh> select(String sql, Object... args) {
        List<HocSinh> list = new ArrayList<>();
        try {
            ResultSet rs = null;

            rs = JdbcHelper.executeQuery(sql, args);
            while (rs.next()) {
                HocSinh model = readFromResultSet(rs);
                list.add(model);

            }
            rs.getStatement().getConnection().close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;

    }

    public ResultSet select() {
        String sql = "select max(id)  from hocsinh";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet select2() {
        String sql = "select mahocsinh from hocsinh";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet loadWith2(String tenlop, boolean ki, String nienhoc) {
        String sql = "select hs.mahocsinh,hs.hoten,hs.gioitinh,hs.ngaysinh from hocsinh as hs  join lophoc as lh on hs.lop=lh.malop  join phancong as pc on lh.manamhoc=pc.manamhoc  join namhoc as nh on pc.manamhoc=nh.manamhoc   and lh.tenlop=? and pc.hocki=? and nh.nienhoc=? group by hs.mahocsinh,hs.hoten,hs.gioitinh,hs.ngaysinh ";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenlop);
            ps.setBoolean(2, ki);
            ps.setString(3, nienhoc);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet selectWithMaHS(String mahocsinh) {
        String sql = "select hs.mahocsinh,hs.hoten,hs.gioitinh,hs.ngaysinh,hs.diachi,hs.dienthoai,hs.dantoc,hs.tongiao,hs.ngayvaodoan,hs.noisinh,hs.cmnd,hs.hotenBo,hs.hotenMe,hs.dienthoaiBo,hs.dienthoaiMe,hs.dvCongTacBo,hs.dvCongTacMe,hs.nguoidamho,hs.trangthai,hs.anh,lh.tenlop from hocsinh as hs join lophoc as lh  on hs.lop=lh.malop and hs.mahocsinh=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, mahocsinh);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet selectWithMaGV(String magiaovien) {
        String sql = "select gv.hoten,lh.tenlop from giaovien as gv  join phancong as pc on gv.magiaovien=pc.magiaovien join lophoc as lh on pc.malop=lh.malop and gv.magiaovien=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, magiaovien);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public HocSinh findByName(String tl) {
        String sql = " select * from hocsinh  join lophoc on hocsinh.lop=lophoc.malop and lophoc.tenlop= ?";
        List<HocSinh> list = select(sql, tl);
        return list.size() > 0 ? list.get(0) : null;
    }

    public void insert(HocSinh model) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "insert into hocsinh(mahocsinh,hoten,gioitinh,ngaysinh,diachi,dienthoai,dantoc,tongiao,ngayvaodoan,noisinh,cmnd,lop,hotenBo,hotenMe,dienthoaiBo,dienthoaiMe,dvCongTacBo,dvCongTacMe,nguoidamho,trangthai,anh) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        JdbcHelper.executeUpdate(sql, model.getMaHS(), model.getHoTen(), model.getGioiTinh(), sfd.format(model.getNgaySinh()), model.getDiaChi(), model.getDienThoai(), model.getDanToc(), model.getTonGiao(), sfd.format(model.getNgayVD()), model.getNoiSinh(), model.getCmND(), model.getLop(), model.getHotenBo(), model.getHotenMe(), model.getDienThoaiBo(), model.getDienThoaiMe(), model.getDvctBo(), model.getDvctMe(), model.getNguoiDamHo(), model.isTrangThai(), model.getAnh());
    }

    public void update(HocSinh model) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "update hocsinh set hoten=?,gioitinh=?,ngaysinh=?,diachi=?,dienthoai=?,dantoc=?,tongiao=?,ngayvaodoan=?,noisinh=?,cmnd=?,lop=?,hotenBo=?,hotenMe=?,dienthoaiBo=?,dienthoaiMe=?,dvCongTacBo=?,dvCongTacMe=?,nguoidamho=?,trangthai=?,anh=? where mahocsinh=?";
        JdbcHelper.executeUpdate(sql, model.getHoTen(), model.getGioiTinh(), sfd.format(model.getNgaySinh()), model.getDiaChi(), model.getDienThoai(), model.getDanToc(), model.getTonGiao(), sfd.format(model.getNgayVD()), model.getNoiSinh(), model.getCmND(), model.getLop(), model.getHotenBo(), model.getHotenMe(), model.getDienThoaiBo(), model.getDienThoaiMe(), model.getDvctBo(), model.getDvctMe(), model.getNguoiDamHo(), model.isTrangThai(), model.getAnh(), model.getMaHS());
    }

    public ResultSet selectSiSoTong(String tenLop, boolean ki, String nienhoc) {
        String sql = "select count(*) as ss from hocsinh join lophoc as lh on hocsinh.lop=lh.malop join phancong as pc on lh.malop=pc.malop join namhoc as nh on pc.manamhoc=nh.manamhoc and lh.tenlop=? and hocsinh.trangthai=1 and pc.hocki=? and nh.nienhoc=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ps.setString(1, tenLop);
            ps.setBoolean(2, ki);
            ps.setString(3, nienhoc);
            ResultSet rs = ps.executeQuery();

            return rs;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResultSet selectSiSoNam(String tenLop, boolean ki, String nienhoc) {
        String sql = "select count(*) as ss from hocsinh join lophoc as lh on hocsinh.lop=lh.malop join phancong as pc on lh.malop=pc.malop join namhoc as nh on pc.manamhoc=nh.manamhoc and lh.tenlop=? and hocsinh.trangthai=1 and pc.hocki=? and nh.nienhoc=? and hocsinh.gioitinh=1";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ps.setString(1, tenLop);
            ps.setBoolean(2, ki);
            ps.setString(3, nienhoc);
            ResultSet rs = ps.executeQuery();

            return rs;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}

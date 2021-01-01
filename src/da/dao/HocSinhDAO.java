/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.DateHelper;
import da.helper.JdbcHelper;
import da.model.HocSinh;
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
public class HocSinhDAO {

    JdbcHelper Jdbc = new JdbcHelper();

    private HocSinh readFromResultSet(ResultSet rs) throws SQLException {
        HocSinh model = new HocSinh();
        model.setiD(UUID.fromString(rs.getString("id")));
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
        model.setLop_id(UUID.fromString(rs.getString("lop_id")));
        model.setHotenBo(rs.getString("hoten_bo"));
        model.setHotenMe(rs.getString("hoten_me"));
        model.setDienThoaiBo(rs.getString("dienthoai_bo"));
        model.setDienThoaiMe(rs.getString("dienthoai_me"));
        model.setDvctBo(rs.getString("dv_cong_tac_bo"));
        model.setDvctMe(rs.getString("dv_cong_tac_me"));
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
    
    public ResultSet loadWithCSV(String tenlop, String nienhoc) {
        String sql = "select * from hocsinh as hs  join lophoc as lh on hs.lop_id=lh.id join namhoc as nh on lh.namhoc_manamhoc=nh.manamhoc  and lh.tenlop=?  and nh.nienhoc=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenlop);
            ps.setString(2, nienhoc);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet select() {
        String sql = "select max(substring(mahocsinh,3,5)) as max from hocsinh";
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

//    public ResultSet loadWithCSV(String tenlop, String nienhoc) {
//        String sql = "select hs.mahocsinh,hs.hoten,hs.gioitinh,hs.ngaysinh,hs.diachi,hs.dienthoai,hs.dantoc,hs.tongiao,hs.ngayvaodoan,hs.noisinh,hs.cmnd from hocsinh as hs  join lophoc as lh on hs.lop_id=lh.id join namhoc as nh on lh.namhoc_manamhoc=nh.manamhoc  and lh.tenlop=?  and nh.nienhoc=?";
//        try {
//            PreparedStatement ps = Jdbc.prepareStatement(sql);
//            ps.setString(1, tenlop);
//            ps.setString(2, nienhoc);
//            ResultSet rs = ps.executeQuery();
//            return rs;
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//
//        }
//    }

    public HocSinh select3(String mahocsinh) {
        String sql = "select * from hocsinh where mahocsinh=?";
        List<HocSinh> list = select(sql, mahocsinh);
        return list.size() > 0 ? list.get(0) : null;
    }

    public ResultSet loadWith2(String tenlop, String nienhoc) {
        String sql = "select hs.mahocsinh,hs.hoten,hs.gioitinh,hs.ngaysinh from hocsinh as hs  join lophoc as lh on hs.lop_id=lh.id join namhoc as nh on lh.namhoc_manamhoc=nh.manamhoc  and lh.tenlop=?  and nh.nienhoc=? and hs.trangthai = true group by hs.mahocsinh,hs.hoten,hs.gioitinh,hs.ngaysinh";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenlop);
            ps.setString(2, nienhoc);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResultSet selectWithMaHS(String mahocsinh) {
        String sql = "select * from hocsinh as hs join lophoc as lh on hs.lop_id=lh.id and hs.mahocsinh=?";
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
        String sql = "select gv.hoten,lh.tenlop from giaovien as gv  join phancong as pc on gv.id=pc.giaovien_id join lophoc as lh on pc.lop_id=lh.id and gv.magiaovien=?";
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
        String sql = " select * from hocsinh  join lophoc on hocsinh.lop_id=lophoc.malop and lophoc.tenlop= ?";
        List<HocSinh> list = select(sql, tl);
        return list.size() > 0 ? list.get(0) : null;
    }

    public void insert(HocSinh model) {
        Date ngaysinh = Date.valueOf(DateHelper.toString(model.getNgaySinh()));
        Date ngayvd = Date.valueOf(DateHelper.toString(model.getNgayVD()));
        String sql = "insert into hocsinh(id,mahocsinh,hoten,gioitinh,ngaysinh,diachi,dienthoai,dantoc,tongiao,ngayvaodoan,noisinh,cmnd,lop_id,hoten_bo,hoten_me,dienthoai_bo,dienthoai_me,dv_cong_tac_bo,dv_cong_tac_me,nguoidamho,trangthai,anh) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        JdbcHelper.executeUpdate(sql, UUID.randomUUID(), model.getMaHS(), model.getHoTen(), model.getGioiTinh(), ngaysinh, model.getDiaChi(), model.getDienThoai(), model.getDanToc(), model.getTonGiao(), ngayvd, model.getNoiSinh(), model.getCmND(), model.getLop_id(), model.getHotenBo(), model.getHotenMe(), model.getDienThoaiBo(), model.getDienThoaiMe(), model.getDvctBo(), model.getDvctMe(), model.getNguoiDamHo(), model.getTrangThai(), model.getAnh());
    }

    public void update(HocSinh model) {
        Date ngaysinh = Date.valueOf(DateHelper.toString(model.getNgaySinh()));
        Date ngayvd = Date.valueOf(DateHelper.toString(model.getNgayVD()));
        String sql = "update hocsinh set hoten=?,gioitinh=?,ngaysinh=?,diachi=?,dienthoai=?,dantoc=?,tongiao=?,ngayvaodoan=?,noisinh=?,cmnd=?,lop_id=?,hoten_bo=?,hoten_me=?,dienthoai_bo=?,dienthoai_me=?,dv_cong_tac_bo=?,dv_cong_tac_me=?,nguoidamho=?,trangthai=?,anh=? where mahocsinh=?";
        JdbcHelper.executeUpdate(sql, model.getHoTen(), model.getGioiTinh(), ngaysinh, model.getDiaChi(), model.getDienThoai(), model.getDanToc(), model.getTonGiao(), ngayvd, model.getNoiSinh(), model.getCmND(), model.getLop_id(), model.getHotenBo(), model.getHotenMe(), model.getDienThoaiBo(), model.getDienThoaiMe(), model.getDvctBo(), model.getDvctMe(), model.getNguoiDamHo(), model.getTrangThai(), model.getAnh(), model.getMaHS());
    }

    public ResultSet selectSiSoTong(String tenLop, String nienhoc) {
        String sql = "select count(*) as ss from hocsinh join lophoc as lh on hocsinh.lop_id=lh.id join namhoc as nh on lh.namhoc_manamhoc=nh.manamhoc and lh.tenlop=? and hocsinh.trangthai=true and nh.nienhoc=?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenLop);
            ps.setString(2, nienhoc);
            ResultSet rs = ps.executeQuery();

            return rs;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResultSet selectWithMaHSandNH(String mahocsinh, String nienhoc) {
        String sql = "select hs.mahocsinh,hs.hoten,hs.gioitinh,hs.ngaysinh,hs.diachi,hs.dienthoai,hs.dantoc,hs.tongiao,hs.ngayvaodoan,hs.noisinh,hs.cmnd,hs.hoten_bo,hs.hoten_me,hs.nguoidamho,hs.anh,lh.tenlop from hocsinh as hs join lophoc as lh  on  hs.lop_id=lh.id join namhoc as nh on lh.namhoc_manamhoc=nh.manamhoc and hs.mahocsinh=? and nh.nienhoc= ?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, mahocsinh);
            ps.setString(2, nienhoc);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    public ResultSet selectSiSoNam(String tenLop, String nienhoc) {
        String sql = "select count(*) as ss from hocsinh join lophoc as lh on hocsinh.lop_id=lh.id join namhoc as nh on lh.namhoc_manamhoc=nh.manamhoc and lh.tenlop=? and hocsinh.trangthai=true and nh.nienhoc=? and hocsinh.gioitinh=true";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);

            ps.setString(1, tenLop);
            ps.setString(2, nienhoc);
            ResultSet rs = ps.executeQuery();

            return rs;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<HocSinh> findAll() {
        String sql = "select * from hocsinh";
        return select(sql);
    }

    public List<HocSinh> findByTenLop(String tenlop) {
        String sql = "select * from hocsinh join lophoc on hocsinh.lop_id = lophoc.id where lophoc.tenlop =?";
        return select(sql, tenlop);
    }
    
    public List<HocSinh> findByTenLopandNienhoc(String tenlop, String nienhoc) {
        String sql = "select * from hocsinh join lophoc on hocsinh.lop_id = lophoc.id join namhoc on lophoc.namhoc_manamhoc = namhoc.manamhoc where lophoc.tenlop =? and namhoc.nienhoc=? order by hocsinh.mahocsinh asc";
        return select(sql, tenlop, nienhoc);
    }
    
    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.JdbcHelper;
import da.model.Diem;
import da.model.PhanCong;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class CsvDao {

    JdbcHelper Jdbc = new JdbcHelper();

    public ResultSet findByClass(String maLop) {
        String sqll = "select mahocsinh, hoten, ngaysinh  from hocsinh inner join lophoc on hocsinh.lop = lophoc.malop where lophoc.tenlop = ?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sqll);
            ps.setString(1, maLop);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResultSet findByEve(String tenMon, String tenLop, boolean ki) {
               String sql = "SELECT hocsinh.mahocsinh, hocsinh.hoten, hocsinh.gioitinh,hocsinh.ngaysinh, diemMieng1, diemMieng2, diemMieng3, diem15phut1, diem15phut2, diem15phut3, diem1Tiet1, diem1Tiet2, diemthi, diemTBM " +
" FROM diem inner join hocsinh on diem.mahocsinh = hocsinh.mahocsinh inner join phancong on diem.mapc = phancong.mapc inner join mon on phancong.mamon = mon.mamon INNER JOIN lophoc on phancong.malop = lophoc.malop" +
" where lophoc.tenlop = ? and mon.tenmon = ? and phancong.hocki = ?";
        try{
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenLop);
            ps.setString(2, tenMon);
            ps.setBoolean(3, ki);
            ResultSet rs = ps.executeQuery();
            return rs;
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public void update(Diem model) {
        String sql = "UPDATE diem SET diemMieng1=?, diemMieng2=?, diemMieng3=?, diem15phut1=?, diem15phut2=?, diem15phut3=?, diem1Tiet1=?, diem1Tiet2=?, diemthi=?, diemTBM=?  WHERE  mahocsinh=? and mapc = ?";
        Jdbc.executeUpdate(sql, model.getDiemMieng1(), model.getDiemMieng2(), model.getDiemMieng3(), model.getDiem15p1(), model.getDiem15p2(), model.getDiem15p3(),
                model.getDiem1Tiet1(), model.getDiem1Tiet2(), model.getDiemThi(), model.getDiemTBM(), model.getMaHocSinh(), model.getMapc());
        System.out.println("update222");
    }
    public ResultSet kqHk1(String tenLop){
        String sql = "SELECT hocsinh.mahocsinh, hocsinh.hoten, hocsinh.ngaysinh, CONVERT(AVG(diem.diemTBM), FLOAT) AS TBhocKi1 FROM hocsinh LEFT JOIN diem ON diem.mahocsinh = hocsinh.mahocsinh LEFT JOIN lophoc ON hocsinh.lop = lophoc.malop LEFT JOIN phancong ON diem.mapc = phancong.mapc WHERE lophoc.tenlop = ? AND phancong.hocki = TRUE GROUP BY hocsinh.hoten, hocsinh.mahocsinh, hocsinh.ngaysinh";
        try{
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenLop);
            ResultSet rs = ps.executeQuery();
            System.out.println(tenLop+"hk1");
            return rs;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
     public ResultSet kqHk2(String tenLop){
        String sql = "SELECT hocsinh.mahocsinh, hocsinh.hoten, hocsinh.ngaysinh, CONVERT(AVG(diem.diemTBM), FLOAT) AS TBhocKi2 FROM hocsinh LEFT JOIN diem ON diem.mahocsinh = hocsinh.mahocsinh LEFT JOIN lophoc ON hocsinh.lop = lophoc.malop LEFT JOIN phancong ON diem.mapc = phancong.mapc WHERE lophoc.tenlop = ? AND phancong.hocki = false GROUP BY hocsinh.hoten, hocsinh.mahocsinh, hocsinh.ngaysinh";
        try{
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenLop);
            ResultSet rs = ps.executeQuery();
            System.out.println(tenLop+"hk2");
            return rs;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
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
      ////////////////////Hoc Sinh/////////////////////////
      
      public ResultSet selectHS(String tenlop, boolean ki, String nienhoc) {
        String sql = "SELECT hs.mahocsinh, hs.hoten, hs.gioitinh, hs.ngaysinh, hs.diachi, hs.dienthoai, hs.dantoc, hs.tongiao, hs.ngayvaodoan, hs.noisinh, hs.cmnd, hs.lop, hs.hotenBo, hs.hotenMe, hs.dienthoaiBo, hs.dienthoaiMe, hs.dvCongTacBo, hs.dvCongTacMe, hs.nguoidamho, hs.trangthai FROM hocsinh AS hs JOIN lophoc AS lh ON hs.lop = lh.malop JOIN phancong AS pc ON lh.manamhoc = pc.manamhoc JOIN namhoc AS nh ON pc.manamhoc = nh.manamhoc AND lh.tenlop = ? AND pc.hocki = ? AND nh.nienhoc = ?";
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
      public String selectWithMalop(String malop) {
          String tenlop = null;
        String sql = "select lophoc.tenlop from lophoc where malop = ?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, malop);

            ResultSet rs = ps.executeQuery();
                        while(rs.next()){
                tenlop = rs.getString("tenlop");
            }
            return tenlop;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }
 public ResultSet selectLop(String khoi) {
        String sql = "SELECT `lophoc`.`malop`, `lophoc`.`tenlop`, `giaovien`.`hoten` FROM `lophoc` LEFT JOIN `khoi` ON `lophoc`.`makhoi` = `khoi`.`makhoi`,`giaovien` WHERE `khoi`.`tenkhoi` = ?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, khoi);

            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

//    private PhanCong readFromResultSet(ResultSet rs) throws SQLException {
//        PhanCong model = new PhanCong();
//        model.setMaPC(rs.getString("mapc"));
//        model.setMaLop(rs.getString("malop"));
//        model.setMaGV(rs.getString("magiaovien"));
//        model.setVaiTro(rs.getBoolean("vaitro"));
//        model.setMaMon(rs.getString("mamon"));
//        model.setHocKi(rs.getBoolean("hocki"));
//        model.setMaNamHoc(rs.getString("manamhoc"));
//        return model;
//    }
//public PhanCong findMaPC(String tenMon, String tenLop, boolean ki, String nienhoc) {
//        String sql = "SELECT * FROM phancong LEFT JOIN lophoc ON phancong.malop = lophoc.malop LEFT JOIN namhoc ON lophoc.manamhoc = namhoc.manamhoc LEFT JOIN mon ON phancong.mamon = mon.mamon WHERE lophoc.tenlop = ? AND namhoc.nienhoc = ? AND mon.tenmon = ? AND phancong.hocki = ?";
//        List<PhanCong> list = select(sql, tenLop, nienhoc, tenMon, ki);
//        return list.size() > 0 ? list.get(0) : null;
//    }
//    private List<PhanCong> select(String sql, Object... args) {
//        List<PhanCong> list = new ArrayList<>();
//        try {
//            ResultSet rs = null;
//
//            rs = JdbcHelper.executeQuery(sql, args);
//            while (rs.next()) {
//                PhanCong model = readFromResultSet(rs);
//                list.add(model);
//
//            }
//            rs.getStatement().getConnection().close();
//
//        } catch (SQLException ex) {
//            throw new RuntimeException(ex);
//        }
//        return list;
//
//    }

//    public List<PhanCong> select() {
//        String sql = "select * from phancong";
//        return select(sql);
//    }
}

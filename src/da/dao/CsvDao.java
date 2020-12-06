/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.Encryption;
import da.helper.JdbcHelper;
import da.model.Diem;
import da.model.HocSinh;
import da.model.PhanCong;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author admin
 */
public class CsvDao {

    JdbcHelper Jdbc = new JdbcHelper();
    Encryption encrypt = new Encryption();

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


    ////////////////////Hoc Sinh/////////////////////////
    public ResultSet selectHS(String tenlop, boolean ki, String nienhoc) {
        String sql = "SELECT * FROM hocsinh AS hs JOIN lophoc AS lh ON hs.lop = lh.malop JOIN phancong AS pc ON lh.manamhoc = pc.manamhoc JOIN namhoc AS nh ON pc.manamhoc = nh.manamhoc AND lh.tenlop = ? AND pc.hocki = ? AND nh.nienhoc = ?";
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
            while (rs.next()) {
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
        public void update(HocSinh model) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "update hocsinh set id =?hoten=?,gioitinh=?,ngaysinh=?,diachi=?,dienthoai=?,dantoc=?,tongiao=?,ngayvaodoan=?,noisinh=?,cmnd=?,lop_id=?,hoten_bo=?,hoten_me=?,dienthoai_bo=?,dienthoai_me=?,dv_cong_tac_bo=?,dv_cong_tac_me=?,nguoidamho=?,trangthai=? where mahocsinh=?";
        JdbcHelper.executeUpdate(sql,UUID.randomUUID(), model.getHoTen(), model.getGioiTinh(), sfd.format(model.getNgaySinh()), model.getDiaChi(), model.getDienThoai(), model.getDanToc(), model.getTonGiao(), sfd.format(model.getNgayVD()), model.getNoiSinh(), model.getCmND(), model.getLop(), model.getHotenBo(), model.getHotenMe(), model.getDienThoaiBo(), model.getDienThoaiMe(), model.getDvctBo(), model.getDvctMe(), model.getNguoiDamHo(), model.isTrangThai(), model.getMaHS());
    }
}

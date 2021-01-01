/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.JdbcHelper;
import da.model.Diem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author BNC
 */
public class diemDAO {

    JdbcHelper Jdbc = new JdbcHelper();

    public void insert(Diem model) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "insert into diem(id,ngay,hocsinh_id,diemmieng1,diemmieng2,diemmieng3,diem15phut1,diem15phut2,diem15phut3,diem1tiet1,diem1tiet2,diemthi,diemtbm,phancong_maphancong) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Jdbc.executeUpdate(sql, UUID.randomUUID(), model.getNgay(), model.getMaHocSinh(), model.getDiemMieng1(), model.getDiemMieng2(),
                 model.getDiemMieng3(), model.getDiem15p1(), model.getDiem15p2(), model.getDiem15p3(), model.getDiem1Tiet1(), model.getDiem1Tiet2(), model.getDiemThi(),
                 model.getDiemTBM(), model.getMapc());
    }

    public ResultSet LoadNewData(String tenLop, UUID maPc) {
        ResultSet rs = null;
        try {
            String sql = "select hocsinh.id,mahocsinh, hoten, ngaysinh from hocsinh inner join lophoc on hocsinh.lop_id = lophoc.id where lophoc.tenlop= ? and not exists (select * from diem where hocsinh.id = diem.hocsinh_id and diem.phancong_maphancong = ?) ";
            rs = Jdbc.executeQuery(sql, tenLop, maPc);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void update(Diem model) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "UPDATE diem SET ngay=?, diemmieng1=?, diemmieng2=?, diemmieng3=?, diem15phut1=?, diem15phut2=?, diem15phut3=?, diem1tiet1=?, diem1tiet2=?, diemthi=?, diemtbm=?  WHERE hocsinh_id=? and phancong_maphancong=?";
        Jdbc.executeUpdate(sql, model.getNgay(), model.getDiemMieng1(), model.getDiemMieng2(), model.getDiemMieng3(), model.getDiem15p1(), model.getDiem15p2(), model.getDiem15p3(),
                model.getDiem1Tiet1(), model.getDiem1Tiet2(), model.getDiemThi(), model.getDiemTBM(), model.getMaHocSinh(), model.getMapc());
    }

//    public void updateDG(diemDG model) {
//        String sql = "UPDATE diem SET diemTX1=?, diemTX2=?, diemTX3=?, diemDK1=?, diemDK2=?, diemHK=?, diemTBMdanhgia=?  WHERE mahocsinh=? and mamon = ? and hocki = ?";
//        Jdbc.executeUpdate(sql, model.isDiemTX1(), model.isDiemTX2(), model.isDiemTX3(), model.isDiemDK1(), model.isDiemDK2(), model.isDiemHK(), model.isDiemTBMDanhGia(), model.getMaHocSinh(), model.getMaMon(), model.isHocKi()); 
//    }
    public List<Diem> select() {
        String sql = "SELECT * FROM diem";
        return select(sql);
    }

    public Diem findByHSId(String maHocSinh, String maMon, boolean ki) {
        String sql = "SELECT * FROM diem WHERE mahocsinh=? and mamon=? and hocki = ?";
        List<Diem> list = select(sql, maHocSinh, maMon, ki);
        return list.size() > 0 ? list.get(0) : null;
    }

    public Diem findByClId(String maLop) {
        String sql = "SELECT * FROM diem WHERE malop=?";
        List<Diem> list = select(sql, maLop);
        return list.size() > 0 ? list.get(0) : null;
    }

    public Diem find(String maLop, String maMon) {
        String sql = "SELECT mahocsinh, hocsinh.hoten, hocsinh.ngaysinh, diemMieng1, diemMieng2, diemMieng3, diem15phut1, diem15phut2, diem15phut3, diem1Tiet1, diem1Tiet2, diemthi FROM diem inner join hocsinh on hocsinh.mahocsinh = diem.mahocsinh WHERE hocsinh.malop=? and mamon=?";
        List<Diem> list = select(sql, maLop, maMon);
        return list.size() > 0 ? list.get(0) : null;
    }

    public ResultSet LoadDataGrade(String maLop, String maMon, Boolean ki) {
        String sql = "SELECT hocsinh.mahocsinh, hocsinh.hoten, hocsinh.gioitinh, diemmieng1, diemmieng2, diemmieng3, diem15phut1, diem15phut2, diem15phut3, diem1tiet1, diem1tiet2, diemthi, diemtbm "
                + " FROM diem inner join hocsinh on diem.hocsinh_id = hocsinh.id inner join phancong on diem.phancong_maphancong = phancong.maphancong inner join mon on phancong.mon_mamon = mon.mamon INNER JOIN lophoc on phancong.lop_id = lophoc.id"
                + " where lophoc.tenlop = ? and mon.ten_mon = ? and phancong.hocki = ? order by hocsinh.mahocsinh asc";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, maLop);
            ps.setString(2, maMon);
            ps.setBoolean(3, ki);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResultSet kqHk1_mon(String mahocsinh, String nienhoc) {
        String sql = "SELECT MON.TEN_MON,\n"
                + "	AVG(DIEMTBM) AS TBHOCKI1\n"
                + "FROM DIEM\n"
                + "INNER JOIN HOCSINH ON DIEM.HOCSINH_ID = HOCSINH.ID\n"
                + "INNER JOIN PHANCONG ON DIEM.PHANCONG_MAPHANCONG = PHANCONG.MAPHANCONG\n"
                + "JOIN NAMHOC AS NH ON PHANCONG.NAMHOC_MANAMHOC = NH.MANAMHOC\n"
                + "INNER JOIN MON ON PHANCONG.MON_MAMON = MON.MAMON\n"
                + "WHERE HOCSINH.MAHOCSINH = ?\n"
                + "				AND NH.NIENHOC = ?\n"
                + "				AND MON.HINHTHUCDANHGIA = TRUE\n"
                + "				AND HOCKI = TRUE\n"
                + "GROUP BY MON.TEN_MON";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, mahocsinh);
            ps.setString(2, nienhoc);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet kqHk2_mon(String mahocsinh, String nienhoc) {
        String sql = "SELECT MON.TEN_MON,\n"
                + "	AVG(DIEMTBM) AS TBHOCKI2\n"
                + "FROM DIEM\n"
                + "INNER JOIN HOCSINH ON DIEM.HOCSINH_ID = HOCSINH.ID\n"
                + "INNER JOIN PHANCONG ON DIEM.PHANCONG_MAPHANCONG = PHANCONG.MAPHANCONG\n"
                + "JOIN NAMHOC AS NH ON PHANCONG.NAMHOC_MANAMHOC = NH.MANAMHOC\n"
                + "INNER JOIN MON ON PHANCONG.MON_MAMON = MON.MAMON\n"
                + "WHERE HOCSINH.MAHOCSINH = ?\n"
                + "				AND NH.NIENHOC = ?\n"
                + "				AND MON.HINHTHUCDANHGIA = TRUE\n"
                + "				AND HOCKI = FALSE\n"
                + "GROUP BY MON.TEN_MON";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, mahocsinh);
            ps.setString(2, nienhoc);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet findDG(String maLop, String maMon, boolean ki) {
        String sql = "SELECT hocsinh.mahocsinh, hocsinh.hoten, hocsinh.gioitinh, diemTX1, diemTX2, diemTX3, diemDK1, diemDK2, diemHK, diemTBMdanhgia \n"
                + "FROM diem inner join hocsinh on diem.mahocsinh = hocsinh.mahocsinh inner join lophoc on hocsinh.lop = lophoc.malop inner join mon on diem.mamon = mon.mamon \n"
                + "where lophoc.tenlop = ? and mon.tenmon = ? and diem.hocki = ?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, maLop);
            ps.setString(2, maMon);
            ps.setBoolean(3, ki);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResultSet findByClass(String tenLop) {
        String sqll = "select mahocsinh, hoten, gioitinh  from hocsinh inner join lophoc on hocsinh.lop_id = lophoc.id where lophoc.tenlop = ?";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sqll);
            ps.setString(1, tenLop);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResultSet kqHk1(String tenLop) {
        String sql = "select hocsinh.mahocsinh, hocsinh.hoten, hocsinh.ngaysinh, avg(diemtbm) as TBhocKi1 FROM diem inner join hocsinh on \n"
                + "diem.hocsinh_id = hocsinh.id inner join phancong on diem.phancong_maphancong = phancong.maphancong inner join lophoc on phancong.lop_id = lophoc.id inner join mon on phancong.mon_mamon = mon.mamon where lophoc.tenlop = ? and mon.hinhthucdanhgia = true and hocki = true group by hocsinh.hoten, hocsinh.mahocsinh, hocsinh.ngaysinh";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenLop);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet kqHk2(String tenLop) {
        String sql = "select hocsinh.mahocsinh, hocsinh.hoten, hocsinh.ngaysinh, avg(diemtbm) as TBhocKi2 FROM diem inner join hocsinh on \n"
                + "diem.hocsinh_id = hocsinh.id inner join phancong on diem.phancong_maphancong = phancong.maphancong inner join lophoc on phancong.lop_id = lophoc.id inner join mon on phancong.mon_mamon = mon.mamon where lophoc.tenlop = ? and mon.hinhthucdanhgia = true and hocki = false group by hocsinh.hoten, hocsinh.mahocsinh, hocsinh.ngaysinh";
        try {
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenLop);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Diem> select(String sql, Object... args) {
        List<Diem> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = Jdbc.executeQuery(sql, args);
                while (rs.next()) {
                    Diem model = readFromResultSet(rs);
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

    private Diem readFromResultSet(ResultSet rs) throws SQLException {
        Diem model = new Diem();
        model.setStt(UUID.fromString(rs.getString("id")));
        model.setNgay(rs.getDate("ngay"));
        model.setMaHocSinh(UUID.fromString(rs.getString("hocsinh_id")));
        model.setDiemMieng1(rs.getInt("diemmieng1"));
        model.setDiemMieng2(rs.getInt("diemmieng2"));
        model.setDiemMieng3(rs.getInt("diemmieng3"));
        model.setDiem15p1(rs.getInt("diem15phut1"));
        model.setDiem15p2(rs.getInt("diem15phut2"));
        model.setDiem15p3(rs.getInt("diem15phut3"));
        model.setDiem1Tiet1(rs.getFloat("diem1tiet1"));
        model.setDiem1Tiet2(rs.getFloat("diem1tiet2"));
        model.setDiemThi(rs.getFloat("diemthi"));
        model.setDiemTBM(rs.getFloat("diemybm"));
        model.setMapc(UUID.fromString(rs.getString("phancong_maphancong")));
        return model;
    }
}

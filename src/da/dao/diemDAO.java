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

/**
 *
 * @author BNC
 */
public class diemDAO {
    JdbcHelper Jdbc = new JdbcHelper();
    public void insert(Diem model) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "insert into diem(ngay,mahocsinh,diemMieng1,diemMieng2,diemMieng3,diem15phut1,diem15phut2,diem15phut3,diem1Tiet1,diem1Tiet2,diemthi,diemTBM,mapc) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Jdbc.executeUpdate(sql, sfd.format(model.getNgay()), model.getMaHocSinh(), model.getDiemMieng1(), model.getDiemMieng2()
        , model.getDiemMieng3(), model.getDiem15p1(), model.getDiem15p2(), model.getDiem15p3(), model.getDiem1Tiet1(), model.getDiem1Tiet2(), model.getDiemThi()
        , model.getDiemTBM(), model.getMapc());
    }
    
    public ResultSet LoadNewData(String tenLop, String maPc){
        String sql = "select mahocsinh, hoten, ngaysinh from hocsinh inner join lophoc on hocsinh.lop = lophoc.malop where lophoc.tenlop= ? and not exists (select * from diem where hocsinh.mahocsinh = diem.mahocsinh and diem.mapc = ?) ";
        try{
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenLop);
            ps.setString(2, maPc);
            ResultSet rs = ps.executeQuery();
            return rs;
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    
//    public void insertDG(diemDG model) {
//        String sql = "insert into diem(ngay,magiaovien,mahocsinh,mamon,diemTX1,diemTX2,diemTX3,diemDK1,diemDK2,diemHK,diemTBMdanhgia,hocki) values(?,?,?,?,?,?,?,?,?,?,?,?)";
//        Jdbc.executeUpdate(sql, model.getNgay(), model.getMaGiaoVien(), model.getMaHocSinh(), model.getMaMon(), model.isDiemTX1(), model.isDiemTX2(), model.isDiemTX3(), model.isDiemDK1(), model.isDiemDK2(), model.isDiemHK(), model.isDiemTBMDanhGia(), model.isHocKi());
//    }
    
    
    public void update(Diem model) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "UPDATE diem SET ngay=?, diemMieng1=?, diemMieng2=?, diemMieng3=?, diem15phut1=?, diem15phut2=?, diem15phut3=?, diem1Tiet1=?, diem1Tiet2=?, diemthi=?, diemTBM=?  WHERE mahocsinh=? and mapc=?";
        Jdbc.executeUpdate(sql,sfd.format(model.getNgay()),model.getDiemMieng1(), model.getDiemMieng2(), model.getDiemMieng3(), model.getDiem15p1(), model.getDiem15p2(), model.getDiem15p3(),
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
    
    
    public ResultSet LoadDataGrade(String maLop, String maMon, boolean ki){        
        String sql = "SELECT hocsinh.mahocsinh, hocsinh.hoten, hocsinh.gioitinh, diemMieng1, diemMieng2, diemMieng3, diem15phut1, diem15phut2, diem15phut3, diem1Tiet1, diem1Tiet2, diemthi, diemTBM " +
" FROM diem inner join hocsinh on diem.mahocsinh = hocsinh.mahocsinh inner join phancong on diem.mapc = phancong.mapc inner join mon on phancong.mamon = mon.mamon INNER JOIN lophoc on phancong.malop = lophoc.malop" +
" where lophoc.tenlop = ? and mon.tenmon = ? and phancong.hocki = ?";
        try{
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, maLop);
            ps.setString(2, maMon);
            ps.setBoolean(3, ki);
            ResultSet rs = ps.executeQuery();
            return rs;
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    
    
    
    public ResultSet findDG(String maLop, String maMon, boolean ki){
        String sql = "SELECT hocsinh.mahocsinh, hocsinh.hoten, hocsinh.gioitinh, diemTX1, diemTX2, diemTX3, diemDK1, diemDK2, diemHK, diemTBMdanhgia \n" +
"FROM diem inner join hocsinh on diem.mahocsinh = hocsinh.mahocsinh inner join lophoc on hocsinh.lop = lophoc.malop inner join mon on diem.mamon = mon.mamon \n" +
"where lophoc.tenlop = ? and mon.tenmon = ? and diem.hocki = ?";
        try{
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, maLop);
            ps.setString(2, maMon);
            ps.setBoolean(3, ki);
            ResultSet rs = ps.executeQuery();
            return rs;
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    
    public ResultSet findByClass(String tenLop){
        String sqll = "select mahocsinh, hoten, gioitinh  from hocsinh inner join lophoc on hocsinh.lop = lophoc.malop where lophoc.tenlop = ?";
        try{
            PreparedStatement ps = Jdbc.prepareStatement(sqll);
            ps.setString(1, tenLop);
            ResultSet rs = ps.executeQuery();
            return rs;
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    
    public ResultSet kqHk1(String tenLop){
        String sql = "select hocsinh.mahocsinh, hocsinh.hoten, hocsinh.ngaysinh, convert(avg(diemTBM),float) as TBhocKi1 FROM diem inner join hocsinh on \n" +
"diem.mahocsinh = hocsinh.mahocsinh inner join phancong on diem.mapc = phancong.mapc inner join lophoc on phancong.malop = lophoc.malop inner join mon on phancong.mamon = mon.mamon where lophoc.tenlop = ? and mon.hinhthucdanhgia = 1 and hocki = 1 group by hocsinh.hoten, hocsinh.mahocsinh, hocsinh.ngaysinh";
        try{
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenLop);
            ResultSet rs = ps.executeQuery();
            return rs;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    public ResultSet kqHk2(String tenLop){
        String sql = "select hocsinh.mahocsinh, hocsinh.hoten, hocsinh.ngaysinh, convert(avg(diemTBM),float) as TBhocKi2 FROM diem inner join hocsinh on \n" +
"diem.mahocsinh = hocsinh.mahocsinh inner join phancong on diem.mapc = phancong.mapc inner join lophoc on phancong.malop = lophoc.malop inner join mon on phancong.mamon = mon.mamon where lophoc.tenlop = ? and mon.hinhthucdanhgia = 1 and hocki = 0 group by hocsinh.hoten, hocsinh.mahocsinh, hocsinh.ngaysinh";
        try{
            PreparedStatement ps = Jdbc.prepareStatement(sql);
            ps.setString(1, tenLop);
            ResultSet rs = ps.executeQuery();
            return rs;
        }catch(SQLException e){
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
        model.setStt(rs.getInt("stt"));
        model.setNgay(rs.getDate("ngay"));
        model.setMaHocSinh(rs.getString("mahocsinh"));
        model.setDiemMieng1(rs.getInt("diemMieng1")); 
        model.setDiemMieng2(rs.getInt("diemMieng2")); 
        model.setDiemMieng3(rs.getInt("diemMieng3")); 
	model.setDiem15p1(rs.getInt("diem15phut1"));
	model.setDiem15p2(rs.getInt("diem15phut2"));
	model.setDiem15p3(rs.getInt("diem15phut3"));
	model.setDiem1Tiet1(rs.getFloat("diem1Tiet1"));
	model.setDiem1Tiet2(rs.getFloat("diem1Tiet2"));
	model.setDiemThi(rs.getFloat("diemthi"));
        model.setDiemTBM(rs.getFloat("diemTBM"));
        model.setMapc(rs.getString("mapc"));
        return model;
    }
}

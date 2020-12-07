/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.GV;

import view.BDH.*;
import AppPackage.AnimationClass;
import da.dao.DiemDanhDAO;
import da.dao.GiaoVienDAO;
import da.dao.HocSinhDAO;
import da.dao.LopHocDAO;
import da.dao.MonDAO;
import da.dao.NamHocDAO;
import da.dao.PhanCongDAO;
import da.dao.diemDAO;
import da.helper.CsvFile;
import da.helper.DialogHelper;
import da.helper.ShareHelper;
import da.model.Diem;
import da.model.DiemDanh;
import da.model.GiaoVien;
import da.model.HocSinh;
import da.model.LopHoc;
import da.model.Mon;
import da.model.NamHoc;
import da.model.PhanCong;
import java.awt.Color;
import java.awt.Frame;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Rahmans
 */
public class TrangChuGV extends javax.swing.JFrame {

    static boolean maximized = true;
    int xMouse;
    int yMouse;
    NamHocDAO nhDAO = new NamHocDAO();
    HocSinhDAO hsDAO = new HocSinhDAO();
    PhanCongDAO pcDAO = new PhanCongDAO();
    GiaoVienDAO gvDAO = new GiaoVienDAO();
    diemDAO dDAO = new diemDAO();
    MonDAO mhDAO = new MonDAO();
    LopHocDAO lhDAO = new LopHocDAO();
    DiemDanhDAO ddDAO = new DiemDanhDAO();
    String head[] = {"Mã học sinh", "Họ tên", "Ngày sinh", "Giới tính"};
    DefaultTableModel model = new DefaultTableModel(head, 0);
    String headTblGrade[] = {"Mã học sinh", "Họ tên", "Giới tính", "Miệng 1", "Miệng 2", "Miệng 3", "15p 1", "15p 2", "15p 3", "45p 1", "45p 2", "Điểm HK", "TBM"};
    String headTblGradeFinal[] = {"Mã học sinh", "Họ tên", "Ngày Sinh", "Điểm học kỳ 1", "Điểm học kỳ 2", "Tổng kết", "Học lực", "Vắng CP", "Vắng KP", "Hạnh Kiểm", "Danh hiệu", "Ghi chú"};
    String headTblDiemdanh[] = {"Ngày", "Họ và tên", "Trạng thái", "Học kì", "Năm học"};
    DefaultTableModel modelTblGrade = new DefaultTableModel(headTblGrade, 0);
    DefaultTableModel modelTblGradeFinal = new DefaultTableModel(headTblGradeFinal, 0);
    DefaultTableModel modelTblDiemdanh = new DefaultTableModel(headTblDiemdanh, 0);

    CsvFile fv = new CsvFile();
    JFileChooser jFileChooser1 = new JFileChooser();

    public TrangChuGV() {
        initComponents();
        lbl_magiaovien.setText(ShareHelper.TaiKhoan.getHoten());
        this.loadtoCBBNamHoc();
        this.LoadTenLop();
        this.loadCBB();
        this.sisoTong();
        this.sisoTongNam();
        this.sisoNu();
        this.LoadLopChuNhiem();
    }
    
    private void themdiemdanh() {
        try{
            DiemDanh diemdanh = new DiemDanh();
            diemdanh.setHocki(cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false);
            diemdanh.setNgay(Date.valueOf(lbl_ngaydiemdanh.getText()));
            diemdanh.setMaGv(ShareHelper.TaiKhoan.getGiaovien_id());
            diemdanh.setMaHs(hsDAO.select3(cbo_mahsdiemdanh.getSelectedItem().toString().substring(0, 6)).getiD());
            System.out.println(cbo_mahsdiemdanh.getSelectedItem().toString().substring(0, 6));
            diemdanh.setTrangThai(cbo_trangthaidiemdanh.getSelectedItem().toString().equals("Có phép")?true:false);
            diemdanh.setNamhoc(nhDAO.findByNienHoc(cbbNamHoc.getSelectedItem().toString()).getMaNamHoc());
            ddDAO.insert(diemdanh);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadCboHocsinh() {
        String tenlop = lbl_tenLopCN.getText();
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbo_mahsdiemdanh.getModel();
        try {
            if(model.getSize() != 0){
                model.removeAllElements();
            }
            List<HocSinh> list = hsDAO.findByTenLop(tenlop);
            for (HocSinh hs : list) {
                model.addElement(hs.getMaHS() +"-"+ hs.getHoTen());
            }
            cbo_mahsdiemdanh.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadDataDiemdanh() {
        modelTblDiemdanh.setRowCount(0);
        String tenlop = lbl_tenLopCN.getText();
        try {
            ResultSet rs = ddDAO.loadData(tenlop);
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getDate("ngay"));
                row.add(rs.getString("hoten"));
                row.add(rs.getBoolean("trangthai") == true ? "Có phép" : "Không phép");
                row.add(rs.getBoolean("hocki") == true ? "Học kỳ 1" : "Học kỳ 2");
                row.add(rs.getString("nienhoc"));
                modelTblDiemdanh.addRow(row);
            }
            tbl_diemdanh.setModel(modelTblDiemdanh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadNewDataDiem(String tenLop, UUID maPc) {
        try {
            ResultSet rs = dDAO.LoadNewData(tenLop, maPc);
            while (rs.next()) {
                Diem themdiem = new Diem();
                themdiem.setNgay(Date.valueOf(LocalDate.now()));
                themdiem.setMaHocSinh(UUID.fromString(rs.getString("id")));
                themdiem.setDiemMieng1(0);
                themdiem.setDiemMieng2(0);
                themdiem.setDiemMieng3(0);
                themdiem.setDiem15p1(0);
                themdiem.setDiem15p2(0);
                themdiem.setDiem15p3(0);
                themdiem.setDiem1Tiet1(0);
                themdiem.setDiem1Tiet2(0);
                themdiem.setDiemThi(0);
                themdiem.setDiemTBM(0);
                themdiem.setMapc(maPc);
                dDAO.insert(themdiem);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi load data moi lớp nhập điểm");
            e.printStackTrace();
        }
    }

    public void LoadKetQua(String tenLop) {
        modelTblGradeFinal.setRowCount(0);
        try {
            ResultSet rs = dDAO.kqHk1(tenLop);
            ResultSet rs2 = dDAO.kqHk2(tenLop);
            while (rs.next() && rs2.next()) {
                Vector row = new Vector();
                row.add(rs.getString("mahocsinh"));
                row.add(rs.getString("hoten"));
                row.add(rs.getDate("ngaysinh"));
                row.add(rs.getFloat("TBhocKi1"));
                row.add(rs2.getFloat("TBhocKi2"));
                Float rate = (rs.getFloat("TBhocKi1") + rs2.getFloat("TBhocKi2")) / 2;
                row.add((double) Math.round(rate * 10) / 10);
                if (rate >= 8) {
                    row.add("Giỏi");
                } else if (rate >= 7 && rate < 8) {
                    row.add("Khá");
                } else if (rate >= 5 && rate < 7) {
                    row.add("Trung Bình");
                } else if (rate < 5) {
                    row.add("Yếu");
                }
                int sobuoinghicp = ddDAO.selectNghiCoPhep(rs.getString("mahocsinh"));
                row.add(sobuoinghicp);
                int sobuoinghikp = ddDAO.selectNghiKoCoPhep(rs.getString("mahocsinh"));
                row.add(sobuoinghikp);
                int tongNghi = sobuoinghicp + sobuoinghikp;
                if (tongNghi >= 30) {
                    row.add("Yếu");
                } else if (tongNghi >= 10 && tongNghi < 30) {
                    row.add("Trung Bình");
                } else if (tongNghi >= 5 && tongNghi < 10) {
                    row.add("Khá");
                } else if (tongNghi < 5) {
                    row.add("Tốt");
                }
                if (rate >= 8 && tongNghi < 5) {
                    row.add("Học sinh giỏi");
                } else if (rate >= 7 && rate < 8 && tongNghi < 10) {
                    row.add("Học sinh tiên tiến");
                } else {
                    row.add("Không có");
                }
                if (tongNghi > 20 || rate < 5 && rate >= 3) {
                    row.add("rèn luyện lại");
                } else if (tongNghi > 20 || rate < 3) {
                    row.add("Lưu Ban");
                } else {
                    row.add("");
                }
                modelTblGradeFinal.addRow(row);
            }
            tblGridView_KetQuaCuoiNam.setModel(modelTblGradeFinal);
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi load data tổng kết");
            e.printStackTrace();
        }
    }

    public void UpdateGradeDG() {
        try {
            HocSinh hocSinh = hsDAO.select3(lbl_mhs3.getText());
            Diem model = new Diem();
            model.setNgay(Date.valueOf(LocalDate.now()));
            model.setDiemMieng1(cbo_diemTX1.getSelectedItem().toString() == "Đạt" ? 1 : 0);
            model.setDiemMieng2(cbo_diemTX2.getSelectedItem().toString() == "Đạt" ? 1 : 0);
            model.setDiemMieng3(cbo_diemTX3.getSelectedItem().toString() == "Đạt" ? 1 : 0);
            model.setDiem15p1(cbo_diemTX4.getSelectedItem().toString() == "Đạt" ? 1 : 0);
            model.setDiem15p2(cbo_diemTX5.getSelectedItem().toString() == "Đạt" ? 1 : 0);
            model.setDiem15p3(cbo_diemTX6.getSelectedItem().toString() == "Đạt" ? 1 : 0);
            model.setDiem1Tiet1(cbo_diemTX7.getSelectedItem().toString() == "Đạt" ? 1 : 0);
            model.setDiem1Tiet2(cbo_diemTX8.getSelectedItem().toString() == "Đạt" ? 1 : 0);
            model.setDiemThi(cbo_diemTX9.getSelectedItem().toString() == "Đạt" ? 1 : 0);
            model.setDiemTBM(lbl_diemTBMDG.getText() == "Đạt" ? 1 : 0);
            model.setMaHocSinh(hocSinh.getiD());
            model.setMapc(pcDAO.selectPc(ShareHelper.TaiKhoan.getGiaovien_id(), cbo_Lop_Diem.getSelectedItem().toString(), cbo_Mon_Diem.getSelectedItem().toString(), cbo_hocKi.getSelectedItem().toString() == "Học kỳ 1" ? true : false).getMaPC());
            dDAO.update(model);
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi update diem DG");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void UpdateGrade() {
        try {
            HocSinh hocSinh = hsDAO.select3(lbl_mhs3.getText());
            Diem model = new Diem();
            model.setNgay(Date.valueOf(LocalDate.now()));
            model.setDiemMieng1(Integer.parseInt(txt_diemMieng1.getText()));
            model.setDiemMieng2(Integer.parseInt(txt_diemMieng2.getText()));
            model.setDiemMieng3(Integer.parseInt(txt_diemMieng3.getText()));
            model.setDiem15p1(Integer.parseInt(txt_15p1.getText()));
            model.setDiem15p2(Integer.parseInt(txt_15p2.getText()));
            model.setDiem15p3(Integer.parseInt(txt_15p3.getText()));
            model.setDiem1Tiet1(Float.parseFloat(txt_45p1.getText()));
            model.setDiem1Tiet2(Float.parseFloat(txt_45p2.getText()));
            model.setDiemThi(Float.parseFloat(txt_hk.getText()));
            model.setDiemTBM(Float.parseFloat(lbl_diemTBM.getText()));
            model.setMaHocSinh(hocSinh.getiD());
            model.setMapc(pcDAO.selectPc(ShareHelper.TaiKhoan.getGiaovien_id(), cbo_Lop_Diem.getSelectedItem().toString(), cbo_Mon_Diem.getSelectedItem().toString(), cbo_hocKi.getSelectedItem().toString() == "Học kỳ 1" ? true : false).getMaPC());
            dDAO.update(model);
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi update diem");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void InsertDataFirst() {
        try {
            for (int i = 0; i < tblGridView_Diem.getRowCount(); i++) {
                HocSinh hocSinh = hsDAO.select3(tblGridView_Diem.getValueAt(i, 0).toString());
                if (cbo_cacchamdiem.getSelectedItem().toString().equals("Chấm điểm") == true) {
                    Diem themdiem = new Diem();
                    themdiem.setNgay(Date.valueOf(LocalDate.now()));
                    themdiem.setMaHocSinh(hocSinh.getiD());
                    themdiem.setDiemMieng1(Integer.parseInt(tblGridView_Diem.getValueAt(i, 3).toString()));
                    themdiem.setDiemMieng2(Integer.parseInt(tblGridView_Diem.getValueAt(i, 4).toString()));
                    themdiem.setDiemMieng3(Integer.parseInt(tblGridView_Diem.getValueAt(i, 5).toString()));
                    themdiem.setDiem15p1(Integer.parseInt(tblGridView_Diem.getValueAt(i, 6).toString()));
                    themdiem.setDiem15p2(Integer.parseInt(tblGridView_Diem.getValueAt(i, 7).toString()));
                    themdiem.setDiem15p3(Integer.parseInt(tblGridView_Diem.getValueAt(i, 8).toString()));
                    themdiem.setDiem1Tiet1(Float.parseFloat(tblGridView_Diem.getValueAt(i, 9).toString()));
                    themdiem.setDiem1Tiet2(Float.parseFloat(tblGridView_Diem.getValueAt(i, 10).toString()));
                    themdiem.setDiemThi(Float.parseFloat(tblGridView_Diem.getValueAt(i, 11).toString()));
                    themdiem.setDiemTBM(Float.parseFloat(tblGridView_Diem.getValueAt(i, 12).toString()));
                    themdiem.setMapc(pcDAO.selectPc(ShareHelper.TaiKhoan.getGiaovien_id(), cbo_Lop_Diem.getSelectedItem().toString(), cbo_Mon_Diem.getSelectedItem().toString(), cbo_hocKi.getSelectedItem().toString() == "Học kỳ 1" ? true : false).getMaPC());
                    dDAO.insert(themdiem);
                } else if (cbo_cacchamdiem.getSelectedItem().toString().equals("Đánh giá") == true) {
                    Diem themdiem = new Diem();
                    themdiem.setNgay(Date.valueOf(LocalDate.now()));
                    themdiem.setMaHocSinh(hocSinh.getiD());
                    themdiem.setDiemMieng1(tblGridView_Diem.getValueAt(i, 3).toString() == "Đạt" ? 1 : 0);
                    themdiem.setDiemMieng2(tblGridView_Diem.getValueAt(i, 4).toString() == "Đạt" ? 1 : 0);
                    themdiem.setDiemMieng3(tblGridView_Diem.getValueAt(i, 5).toString() == "Đạt" ? 1 : 0);
                    themdiem.setDiem15p1(tblGridView_Diem.getValueAt(i, 6).toString() == "Đạt" ? 1 : 0);
                    themdiem.setDiem15p2(tblGridView_Diem.getValueAt(i, 7).toString() == "Đạt" ? 1 : 0);
                    themdiem.setDiem15p3(tblGridView_Diem.getValueAt(i, 8).toString() == "Đạt" ? 1 : 0);
                    themdiem.setDiem1Tiet1(tblGridView_Diem.getValueAt(i, 9).toString() == "Đạt" ? 1 : 0);
                    themdiem.setDiem1Tiet2(tblGridView_Diem.getValueAt(i, 10).toString() == "Đạt" ? 1 : 0);
                    themdiem.setDiemThi(tblGridView_Diem.getValueAt(i, 11).toString() == "Đạt" ? 1 : 0);
                    themdiem.setDiemTBM(tblGridView_Diem.getValueAt(i, 12).toString() == "Đạt" ? 1 : 0);
                    themdiem.setMapc(pcDAO.selectPc(ShareHelper.TaiKhoan.getGiaovien_id(), cbo_Lop_Diem.getSelectedItem().toString(), cbo_Mon_Diem.getSelectedItem().toString(), cbo_hocKi.getSelectedItem().toString() == "Học kỳ 1" ? true : false).getMaPC());
                    dDAO.insert(themdiem);
                }
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi 5");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void LoadNewDataToTableGrade(String tenLop) {
        modelTblGrade.setRowCount(0);
        try {
            ResultSet rs = dDAO.findByClass(tenLop);
            while (rs.next()) {
                Vector row = new Vector();
                if (cbo_cacchamdiem.getSelectedItem().toString().equals("Chấm điểm") == true) {
                    row.add(rs.getString("mahocsinh"));
                    row.add(rs.getString("hoten"));
                    row.add(rs.getBoolean("gioitinh") == false ? "Nam" : "Nữ");
                    row.add(0);
                    row.add(0);
                    row.add(0);
                    row.add(0);
                    row.add(0);
                    row.add(0);
                    row.add(0);
                    row.add(0);
                    row.add(0);
                    row.add(0);
                    modelTblGrade.addRow(row);
                } else if (cbo_cacchamdiem.getSelectedItem().toString().equals("Đánh giá") == true) {
                    row.add(rs.getString("mahocsinh"));
                    row.add(rs.getString("hoten"));
                    row.add("Chưa Đạt");
                    row.add("Chưa Đạt");
                    row.add("Chưa Đạt");
                    row.add("Chưa Đạt");
                    row.add("Chưa Đạt");
                    row.add("Chưa Đạt");
                    row.add("Chưa Đạt");
                    row.add("Chưa Đạt");
                    row.add("Chưa Đạt");
                    row.add("Chưa Đạt");
                    row.add("Chưa Đạt");
                    modelTblGrade.addRow(row);

                }
            }
            tblGridView_Diem.setModel(modelTblGrade);
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi load lớp nhập điểm");
            e.printStackTrace();
        }
    }

    public void LoadDataToTableGrade(String tenLop, String maMon, boolean ki) {
        modelTblGrade.setRowCount(0);
        try {
            ResultSet rs = dDAO.LoadDataGrade(tenLop, maMon, ki);
            while (rs.next()) {
                Vector row = new Vector();
                if (cbo_cacchamdiem.getSelectedItem().toString().equals("Chấm điểm") == true) {
                    row.add(rs.getString("mahocsinh"));
                    row.add(rs.getString("hoten"));
                    row.add(rs.getBoolean("gioitinh") == false ? "Nam" : "Nữ");
                    row.add(rs.getInt("diemMieng1"));
                    row.add(rs.getInt("diemMieng2"));
                    row.add(rs.getInt("diemMieng3"));
                    row.add(rs.getInt("diem15phut1"));
                    row.add(rs.getInt("diem15phut2"));
                    row.add(rs.getInt("diem15phut3"));
                    row.add(rs.getFloat("diem1Tiet1"));
                    row.add(rs.getFloat("diem1Tiet2"));
                    row.add(rs.getFloat("diemthi"));
                    row.add(rs.getFloat("diemTBM"));
                    modelTblGrade.addRow(row);
                } else if (cbo_cacchamdiem.getSelectedItem().toString().equals("Đánh giá") == true) {
                    row.add(rs.getString("mahocsinh"));
                    row.add(rs.getString("hoten"));
                    row.add(rs.getBoolean("gioitinh") == false ? "Nam" : "Nữ");
                    row.add(rs.getInt("diemMieng1") == 1 ? "Đạt" : "Chưa Đạt");
                    row.add(rs.getInt("diemMieng2") == 1 ? "Đạt" : "Chưa Đạt");
                    row.add(rs.getInt("diemMieng3") == 1 ? "Đạt" : "Chưa Đạt");
                    row.add(rs.getInt("diem15phut1") == 1 ? "Đạt" : "Chưa Đạt");
                    row.add(rs.getInt("diem15phut2") == 1 ? "Đạt" : "Chưa Đạt");
                    row.add(rs.getInt("diem15phut3") == 1 ? "Đạt" : "Chưa Đạt");
                    row.add(rs.getFloat("diem1Tiet1") == 1 ? "Đạt" : "Chưa Đạt");
                    row.add(rs.getFloat("diem1Tiet2") == 1 ? "Đạt" : "Chưa Đạt");
                    row.add(rs.getFloat("diemthi") == 1 ? "Đạt" : "Chưa Đạt");
                    row.add(rs.getFloat("diemTBM") == 1 ? "Đạt" : "Chưa Đạt");
                    modelTblGrade.addRow(row);
                }

            }
            tblGridView_Diem.setModel(modelTblGrade);
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi load data nhập điểm");
            e.printStackTrace();
        }
    }

    public int TongMonCoDiem() {
        try {
            int i = 0;
            List<Mon> mon = mhDAO.loadDataNotExits(lhDAO.findByTenLop(cbo_LoCN_KQCN.getSelectedItem().toString()).getMaKhoi());
            i = mon.size();
            return i;
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi checkDATA");
            e.printStackTrace();
        }
        return 0;
    }

    public int countSubject() {
        try {
            int i = 0;
            ResultSet rs = mhDAO.selectByKhoi(lhDAO.findByTenLop(cbo_LoCN_KQCN.getSelectedItem().toString()).getMaKhoi().toString());
            while (rs.next()) {
                i++;
            }
            return i;
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi checkDATA");
            e.printStackTrace();
        }
        return 0;
    }

    public int countClass(String tenLop) {
        try {
            int i = 0;
            ResultSet rs = dDAO.findByClass(tenLop);
            while (rs.next()) {
                i++;
            }
            return i;
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi checkDATA");
            e.printStackTrace();
        }
        return 0;
    }

    public void LoadMonDay() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbo_Mon_Diem.getModel();
        if (model != null) {
            model.removeAllElements();
        }
        GiaoVien giaoVien = gvDAO.findByUUID(ShareHelper.TaiKhoan.getGiaovien_id());
        try {
            boolean t = cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false;
            ResultSet rs = pcDAO.select5(giaoVien.getMaGV(), t);
            while (rs.next()) {
                String tenMon = rs.getString("ten_mon");
                model.addElement(tenMon);
                cbo_Mon_Diem.setModel(model);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi 1");
            e.printStackTrace();
        }
    }

    public void LoadLopChuNhiem() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbo_LoCN_KQCN.getModel();
        GiaoVien giaoVien = gvDAO.findByUUID(ShareHelper.TaiKhoan.getGiaovien_id());
        try {
            if (model != null) {
                model.removeAllElements();
            }
            ResultSet rs = pcDAO.selectLopCN(giaoVien.getMaGV());
            while (rs.next()) {
                model.addElement(rs.getString("tenlop"));
            }
            cbo_LoCN_KQCN.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadLopDay() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbo_Lop_Diem.getModel();
        GiaoVien giaoVien = gvDAO.findByUUID(ShareHelper.TaiKhoan.getGiaovien_id());
        try {
            if (model != null) {
                model.removeAllElements();
            }
            List<PhanCong> list = pcDAO.select6(giaoVien.getMaGV(), cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false, cbo_Mon_Diem.getSelectedItem().toString());
            for (PhanCong pc : list) {
                List<LopHoc> listLh = lhDAO.findClass(pc.getMaLop());
                for (LopHoc lh : listLh) {
                    model.addElement(lh.getTenLop());
                }
            }
            cbo_Lop_Diem.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadTenLop() {
        GiaoVien giaoVien = gvDAO.findByUUID(ShareHelper.TaiKhoan.getGiaovien_id());
        ResultSet rs = pcDAO.selectLopCN(giaoVien.getMaGV());
        try {
            while (rs.next()) {
                lbl_Lop1.setText(rs.getString("tenlop"));
                lbl_tenLopCN.setText(rs.getString("tenlop"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadtoCBBNamHoc() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbbNamHoc.getModel();
        if (model != null) {
            model.removeAllElements();
        }
        try {
            List<NamHoc> list = nhDAO.select();
            for (NamHoc nh : list) {
                model.addElement(nh.getNienHoc());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi truy van du lieu CBB");
            e.printStackTrace();
        }
    }

    public void timkiemlan1() {
        String tenLop = (String) cbo_Lop_Diem.getSelectedItem();
        String tenMon = (String) cbo_Mon_Diem.getSelectedItem();
        boolean ki = cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false;
        if (DialogHelper.confirm(this, "bạn có muốn tạo mới danh sách") == true) {
            this.LoadNewDataToTableGrade(tenLop);
            this.InsertDataFirst();
            DialogHelper.alert(this, "Thành Công");
        } else {
            DialogHelper.alert(this, "vui lòng kiểm tra lại");
        }
    }

    public void resetlan1() {
        if (DialogHelper.confirm(this, "bạn có muốn tạo lại danh sách") == true) {
            this.LoadNewDataDiem((String) cbo_Lop_Diem.getSelectedItem(), pcDAO.selectPc(ShareHelper.TaiKhoan.getGiaovien_id(), cbo_Lop_Diem.getSelectedItem().toString(), cbo_Mon_Diem.getSelectedItem().toString(), cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false).getMaPC());
            this.LoadDataToTableGrade((String) cbo_Lop_Diem.getSelectedItem(), (String) cbo_Mon_Diem.getSelectedItem(), cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false);
            DialogHelper.alert(this, "Update dữ liệu thành công");
        } else {
            DialogHelper.alert(this, "vui lòng kiểm tra lại");
        }
    }

    public void timkiem() {
        String tenLop = (String) cbo_Lop_Diem.getSelectedItem();
        String tenMon = (String) cbo_Mon_Diem.getSelectedItem();
        boolean ki = cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false;
        this.LoadDataToTableGrade(tenLop, tenMon, ki);
        if (tblGridView_Diem.getRowCount() == 0) {
            DialogHelper.alert(this, "Không có dữ liệu điểm, vui lòng ấn tìm kiếm");
        } else {

        }
    }

    public void loadCBB() {
        model.setRowCount(0);
        try {
            String nienHoc = (String) cbbNamHoc.getSelectedItem();
            String tenLop = lbl_Lop1.getText();
            ResultSet rs = hsDAO.loadWith2(tenLop, nienHoc);
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getString("mahocsinh"));
                row.add(rs.getString("hoten"));
                row.add(rs.getDate("ngaysinh"));
                row.add(rs.getBoolean("gioitinh") ? "Nam" : "Nữ");
                model.addRow(row);
            }
            tblGridView1.setModel(model);
        } catch (Exception e) {
        }
    }

    public void sisoTong() {
        String nienHoc = (String) cbbNamHoc.getSelectedItem();

        boolean Ki = cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false;
        String tl = lbl_Lop1.getText();

        ResultSet rs = hsDAO.selectSiSoTong(tl, Ki, nienHoc);
        try {
            if (rs.next()) {
                String kq = rs.getString("ss");
                txtSS.setText(kq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sisoTongNam() {
        String nienHoc = (String) cbbNamHoc.getSelectedItem();

        boolean Ki = cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false;
        String tl = lbl_Lop1.getText();

        ResultSet rs = hsDAO.selectSiSoNam(tl, Ki, nienHoc);
        try {
            if (rs.next()) {
                String kq = rs.getString("ss");
                txtSSNam.setText(kq);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        if (tblGridView_Diem.getRowCount() != this.countClass((String) cbo_Lop_Diem.getSelectedItem())) {
            DialogHelper.alert(this, "Danh sách lớp có dữ liệu mới, vui lòng update");
        } else {

        }
    }

    public void sisoNu() {
        int tong = Integer.parseInt(txtSS.getText());
        int nam = Integer.parseInt(txtSSNam.getText());
        int nu = tong - nam;
        String m = Integer.toString(nu);

        txtssNu.setText(m);
    }

    public void loadkqcuoinam() {
        String tenLop = cbo_LoCN_KQCN.getSelectedItem().toString();
        if (tenLop.equals("")) {
            DialogHelper.alert(this, "vui lòng chọn lớp bạn chủ nhiệm");
        } else if (this.TongMonCoDiem() != 0) {
            DialogHelper.alert(this, "vui lòng nhập đầy đủ điểm của từng môn theo dang sách môn");
            DialogHelper.alert(this, "Dưới đây là tổng hợp điểm của những môn đã có điểm");
            this.LoadKetQua(tenLop);
            lbl_SiSo.setText(String.valueOf(tblGridView_KetQuaCuoiNam.getRowCount()));
        } else {
            this.LoadKetQua(tenLop);
            lbl_SiSo.setText(String.valueOf(tblGridView_KetQuaCuoiNam.getRowCount()));
        }
    }

    public void resetketquacuoinam() {
        String tenLop = cbo_LoCN_KQCN.getSelectedItem().toString();
        if (tblGridView_KetQuaCuoiNam.getRowCount() != this.countClass(tenLop)) {
            DialogHelper.alert(this, "Danh sách lớp có dữ liệu mới, vui lòng update");
            if (DialogHelper.confirm(this, "bạn có muốn tạo lại danh sách") == true) {
                this.LoadKetQua(tenLop);
                DialogHelper.alert(this, "Update dữ liệu thành công");
                lbl_SiSo.setText(String.valueOf(tblGridView_KetQuaCuoiNam.getRowCount()));
            } else {
                DialogHelper.alert(this, "Vui lòng nhập đầy đủ thông tin điểm cho từng học sinh");
            }
        } else {
            DialogHelper.alert(this, "danh sách đã được làm mới");
        }
    }

    AnimationClass ac = new AnimationClass();

    public void checktab(String tieude) {
        if (lbltab1.getText().equals(tieude)) {
            clicktab(tieude);
        } else if (lbltab2.getText().equals(tieude)) {
            clicktab(tieude);
        } else if (lbltab3.getText().equals(tieude)) {
            clicktab(tieude);
        } else if (lbltab4.getText().equals(tieude)) {
            clicktab(tieude);
        } else if (lbltab5.getText().equals(tieude)) {
            clicktab(tieude);
        } else {
            thanhmenu(tieude);
        }
    }

    public void thanhmenu(String tieude) {
        try {

            if (lbltab1.getText().equals("")) {
                tanktab.setBackground(new Color(240, 240, 240));
                lbltab1.setText(tieude);

                tab2.setBackground(new Color(240, 240, 240));
                lbltab2.setBackground(new Color(240, 240, 240));
                btntab2.setBackground(new Color(240, 240, 240));

                tab3.setBackground(new Color(240, 240, 240));
                lbltab3.setBackground(new Color(240, 240, 240));
                btntab3.setBackground(new Color(240, 240, 240));

                tab4.setBackground(new Color(240, 240, 240));
                lbltab4.setBackground(new Color(240, 240, 240));
                btntab4.setBackground(new Color(240, 240, 240));

                tab5.setBackground(new Color(240, 240, 240));
                lbltab5.setBackground(new Color(240, 240, 240));
                btntab5.setBackground(new Color(240, 240, 240));

                Image img = ImageIO.read(getClass().getResource("/img/icons8-macos-close-32.png"));
                btntab1.setIcon(new ImageIcon(img));
                System.out.println("ok");
            } else if (lbltab2.getText().equals("")) {
                lbltab2.setText(tieude);
                tab2.setBackground(new Color(251, 251, 251));
                lbltab2.setBackground(new Color(251, 251, 251));
                btntab2.setBackground(new Color(251, 251, 251));
                Image img = ImageIO.read(getClass().getResource("/img/icons8-macos-close-32.png"));
                btntab2.setIcon(new ImageIcon(img));
            } else if (lbltab3.getText().equals("")) {
                lbltab3.setText(tieude);
                tab3.setBackground(new Color(251, 251, 251));
                lbltab3.setBackground(new Color(251, 251, 251));
                btntab3.setBackground(new Color(251, 251, 251));
                Image img = ImageIO.read(getClass().getResource("/img/icons8-macos-close-32.png"));
                btntab3.setIcon(new ImageIcon(img));
            } else if (lbltab4.getText().equals("")) {
                lbltab4.setText(tieude);
                tab4.setBackground(new Color(251, 251, 251));
                lbltab4.setBackground(new Color(251, 251, 251));
                btntab4.setBackground(new Color(251, 251, 251));
                Image img = ImageIO.read(getClass().getResource("/img/icons8-macos-close-32.png"));
                btntab4.setIcon(new ImageIcon(img));
            } else if (lbltab5.getText().equals("")) {
                lbltab5.setText(tieude);
                tab5.setBackground(new Color(251, 251, 251));
                lbltab5.setBackground(new Color(251, 251, 251));
                btntab5.setBackground(new Color(251, 251, 251));
                Image img = ImageIO.read(getClass().getResource("/img/icons8-macos-close-32.png"));
                btntab5.setIcon(new ImageIcon(img));
            }

        } catch (IOException ex) {
            Logger.getLogger(TrangChuGV.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void clicktab(String tentab) {
        if (tentab.equals("Gửi phản hồi")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(qlyguiphanhoi);
            body.repaint();
            body.revalidate();
        } else if (tentab.equals("Danh sách học sinh")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(qlyhocsinh);
            body.repaint();
            body.revalidate();
        } else if (tentab.equals("Học bạ học sinh")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(qlyhocba);
            body.repaint();
            body.revalidate();
        } else if (tentab.equals("Điểm danh")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(qlydiemdanh);
            body.repaint();
            body.revalidate();
        } else if (tentab.equals("Điểm môn học")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(qlydiem);
            body.repaint();
            body.revalidate();
        } else if (tentab.equals("Trang chủ")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(maintrangchu);
            body.repaint();
            body.revalidate();
        } else if (tentab.equals("Kết quả cuối năm")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(kquacuoinam);
            body.repaint();
            body.revalidate();
        } else {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(trangchinh);
            body.repaint();
            body.revalidate();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnMinimize = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnMinimize4 = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        cbbNamHoc = new javax.swing.JComboBox<>();
        jLabel43 = new javax.swing.JLabel();
        cbo_hocKi = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        trangchu = new javax.swing.JPanel();
        lane1 = new javax.swing.JLabel();
        lbltrangchu = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        diemmonhoc = new javax.swing.JPanel();
        lane2 = new javax.swing.JLabel();
        lbldiemmonhoc = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        diemdanh = new javax.swing.JPanel();
        lane3 = new javax.swing.JLabel();
        lbldiemdanh = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        guiphanhoi = new javax.swing.JPanel();
        lane6 = new javax.swing.JLabel();
        lblguiphanhoi = new javax.swing.JButton();
        qlnamhoc = new javax.swing.JLabel();
        hocbahocsinh = new javax.swing.JPanel();
        lane4 = new javax.swing.JLabel();
        lblhocba = new javax.swing.JButton();
        qlnamhoc1 = new javax.swing.JLabel();
        dshocsinh = new javax.swing.JPanel();
        lane5 = new javax.swing.JLabel();
        lbldshocsinh = new javax.swing.JButton();
        qlnamhoc2 = new javax.swing.JLabel();
        ketquacuoinam = new javax.swing.JPanel();
        lane7 = new javax.swing.JLabel();
        lblketquacuoinam = new javax.swing.JButton();
        jLabel68 = new javax.swing.JLabel();
        lbl_giaovien = new javax.swing.JLabel();
        lbl_magiaovien = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        tanktab = new javax.swing.JPanel();
        tab1 = new javax.swing.JPanel();
        lbltab1 = new javax.swing.JLabel();
        btntab1 = new javax.swing.JButton();
        tab2 = new javax.swing.JPanel();
        lbltab2 = new javax.swing.JLabel();
        btntab2 = new javax.swing.JButton();
        tab3 = new javax.swing.JPanel();
        lbltab3 = new javax.swing.JLabel();
        btntab3 = new javax.swing.JButton();
        tab4 = new javax.swing.JPanel();
        lbltab4 = new javax.swing.JLabel();
        btntab4 = new javax.swing.JButton();
        tab5 = new javax.swing.JPanel();
        lbltab5 = new javax.swing.JLabel();
        btntab5 = new javax.swing.JButton();
        body = new javax.swing.JPanel();
        trangchinh = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        qlydiemdanh = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_diemdanh = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cbo_trangthaidiemdanh = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        lbl_tenLopCN = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lbl_ngaydiemdanh = new javax.swing.JLabel();
        cbo_mahsdiemdanh = new javax.swing.JComboBox<>();
        qlyguiphanhoi = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblGridView2 = new javax.swing.JTable();
        jLabel37 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel41 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jButton6 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        qlydiem = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        bodydiem = new javax.swing.JPanel();
        bangdiem = new javax.swing.JScrollPane();
        tblGridView_Diem = new javax.swing.JTable();
        monchamdiem = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txt_diemMieng1 = new javax.swing.JTextField();
        txt_diemMieng2 = new javax.swing.JTextField();
        txt_diemMieng3 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txt_15p1 = new javax.swing.JTextField();
        txt_15p2 = new javax.swing.JTextField();
        txt_15p3 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        txt_45p1 = new javax.swing.JTextField();
        txt_45p2 = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txt_hk = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        lbl_diemTBM = new javax.swing.JLabel();
        btn_capNhat = new javax.swing.JButton();
        btn_capNhat1 = new javax.swing.JButton();
        mondanhgia = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        cbo_diemTX1 = new javax.swing.JComboBox<>();
        cbo_diemTX2 = new javax.swing.JComboBox<>();
        cbo_diemTX3 = new javax.swing.JComboBox<>();
        cbo_diemTX4 = new javax.swing.JComboBox<>();
        cbo_diemTX5 = new javax.swing.JComboBox<>();
        cbo_diemTX6 = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        cbo_diemTX7 = new javax.swing.JComboBox<>();
        cbo_diemTX8 = new javax.swing.JComboBox<>();
        jPanel28 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        cbo_diemTX9 = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        lbl_diemTBMDG = new javax.swing.JLabel();
        btn_capNhat2 = new javax.swing.JButton();
        btn_capNhat3 = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lbl_hvt3 = new javax.swing.JLabel();
        lbl_gt3 = new javax.swing.JLabel();
        lbl_mhs3 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txt_searchdiem = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        cbo_Lop_Diem = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        cbo_cacchamdiem = new javax.swing.JComboBox<>();
        btnchinhsua = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        cbo_Mon_Diem = new javax.swing.JComboBox<>();
        Xuat_Diem = new javax.swing.JButton();
        Nhap_Diem = new javax.swing.JButton();
        Tai_Form_Diem = new javax.swing.JButton();
        btn_timkiem = new javax.swing.JButton();
        btn_lammoi = new javax.swing.JButton();
        maintrangchu = new javax.swing.JPanel();
        qlyhocba = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        lbl_hvt4 = new javax.swing.JLabel();
        lbl_gt4 = new javax.swing.JLabel();
        lbl_mhs4 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        lbl_mhs5 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        lbl_hvt5 = new javax.swing.JLabel();
        btn_capNhat9 = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        txt_searchdiem1 = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        cbo_Mon_Diem1 = new javax.swing.JComboBox<>();
        jLabel56 = new javax.swing.JLabel();
        cbo_Mon_Diem2 = new javax.swing.JComboBox<>();
        jLabel62 = new javax.swing.JLabel();
        cbo_Mon_Diem3 = new javax.swing.JComboBox<>();
        btn_capNhat10 = new javax.swing.JButton();
        banglop = new javax.swing.JScrollPane();
        tblGridView3 = new javax.swing.JTable();
        qlyhocsinh = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGridView1 = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        image = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        lbl_mahs = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        lbl_HoTen = new javax.swing.JLabel();
        lbl_GioiTinh = new javax.swing.JLabel();
        lbl_Ngaysinh = new javax.swing.JLabel();
        lbl_DanToc = new javax.swing.JLabel();
        lbl_TonGiao = new javax.swing.JLabel();
        lbl_NoiSinh = new javax.swing.JLabel();
        lbl_CMND = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lbl_DiaCHi = new javax.swing.JTextArea();
        lbl_Lop2 = new javax.swing.JLabel();
        lbl_NgayVaoDoan = new javax.swing.JLabel();
        lbl_SDT = new javax.swing.JLabel();
        lbl_path = new javax.swing.JLabel();
        lbl_TrangThai = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        lbl_DVCTBo = new javax.swing.JTextArea();
        jLabel79 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        lbl_DVCTMe = new javax.swing.JTextArea();
        jLabel80 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        lbl_NguoiDamHo = new javax.swing.JTextArea();
        lbl_HoTenBo = new javax.swing.JLabel();
        lbl_SDTBo = new javax.swing.JLabel();
        lbl_HotenMe = new javax.swing.JLabel();
        lbl_SDTMe = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        lbl_Lop1 = new javax.swing.JLabel();
        txtSS = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        txtSSNam = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        txtssNu = new javax.swing.JLabel();
        kquacuoinam = new javax.swing.JPanel();
        jLabel84 = new javax.swing.JLabel();
        txt_timkiemkqcuoinam = new javax.swing.JTextField();
        jLabel85 = new javax.swing.JLabel();
        jLabel124 = new javax.swing.JLabel();
        lbl_SiSo = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel101 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        lbl_hvt = new javax.swing.JLabel();
        lbl_ns = new javax.swing.JLabel();
        lbl_mhs = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel105 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        lbl_KQTB1 = new javax.swing.JLabel();
        lbl_KQTB2 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        lbl_HanhKiemFull = new javax.swing.JLabel();
        lbl_HanhKiem1 = new javax.swing.JLabel();
        lbl_HanhKiem2 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        lbl_HocLuc1 = new javax.swing.JLabel();
        lbl_HocLuc2 = new javax.swing.JLabel();
        lbl_HocLuc3 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        lbl_DanhHieu4 = new javax.swing.JLabel();
        lbl_DanhHieu5 = new javax.swing.JLabel();
        lbl_DanhHieu6 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel92 = new javax.swing.JLabel();
        lbl_VangCP = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel93 = new javax.swing.JLabel();
        lbl_VangCP1 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tblGridView_KetQuaCuoiNam = new javax.swing.JTable();
        Xuat_KQCN = new javax.swing.JButton();
        cbo_LoCN_KQCN = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Giáo Viên");
        setBackground(new java.awt.Color(244, 248, 251));
        setUndecorated(true);
        setSize(new java.awt.Dimension(1000, 600));
        getContentPane().setLayout(null);

        pnlHeader.setBackground(new java.awt.Color(255, 255, 255));
        pnlHeader.setForeground(new java.awt.Color(255, 255, 255));
        pnlHeader.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlHeaderMouseDragged(evt);
            }
        });
        pnlHeader.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlHeaderMousePressed(evt);
            }
        });

        btnExit.setBackground(new java.awt.Color(255, 255, 255));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Exit.png"))); // NOI18N
        btnExit.setContentAreaFilled(false);
        btnExit.setFocusable(false);
        btnExit.setOpaque(true);
        btnExit.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Exit (2).png"))); // NOI18N
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExitMouseExited(evt);
            }
        });
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnMinimize.setBackground(new java.awt.Color(255, 255, 255));
        btnMinimize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Minimize.png"))); // NOI18N
        btnMinimize.setContentAreaFilled(false);
        btnMinimize.setFocusable(false);
        btnMinimize.setOpaque(true);
        btnMinimize.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Minimize (2).png"))); // NOI18N
        btnMinimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMinimizeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMinimizeMouseExited(evt);
            }
        });
        btnMinimize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinimizeActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Quản lý học bạ cấp 2");

        btnMinimize4.setBackground(new java.awt.Color(255, 255, 255));
        btnMinimize4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-education-32.png"))); // NOI18N
        btnMinimize4.setContentAreaFilled(false);
        btnMinimize4.setFocusable(false);
        btnMinimize4.setOpaque(true);
        btnMinimize4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMinimize4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMinimize4MouseExited(evt);
            }
        });
        btnMinimize4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinimize4ActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel42.setText("Niên học :");

        cbbNamHoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbNamHoc.setBorder(null);
        cbbNamHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbNamHocActionPerformed(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel43.setText("Học kì :");

        cbo_hocKi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Học kỳ 1", "Học kỳ 2" }));
        cbo_hocKi.setBorder(null);
        cbo_hocKi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_hocKiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHeaderLayout.createSequentialGroup()
                .addComponent(btnMinimize4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(383, 383, 383)
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbbNamHoc, 0, 159, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_hocKi, 0, 159, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnMinimize4)
                        .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbbNamHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnExit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnMinimize, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbo_hocKi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(pnlHeader);
        pnlHeader.setBounds(0, 0, 1360, 40);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        trangchu.setBackground(new java.awt.Color(255, 255, 255));
        trangchu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trangchuMouseClicked(evt);
            }
        });

        lane1.setBackground(new java.awt.Color(255, 255, 255));
        lane1.setOpaque(true);

        lbltrangchu.setBackground(new java.awt.Color(255, 255, 255));
        lbltrangchu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-home-32.png"))); // NOI18N
        lbltrangchu.setContentAreaFilled(false);
        lbltrangchu.setFocusable(false);
        lbltrangchu.setOpaque(true);
        lbltrangchu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbltrangchuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbltrangchuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbltrangchuMouseExited(evt);
            }
        });
        lbltrangchu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbltrangchuActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setText("Trang chủ");

        javax.swing.GroupLayout trangchuLayout = new javax.swing.GroupLayout(trangchu);
        trangchu.setLayout(trangchuLayout);
        trangchuLayout.setHorizontalGroup(
            trangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(trangchuLayout.createSequentialGroup()
                .addComponent(lane1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbltrangchu, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        trangchuLayout.setVerticalGroup(
            trangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbltrangchu, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        diemmonhoc.setBackground(new java.awt.Color(255, 255, 255));
        diemmonhoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                diemmonhocMouseClicked(evt);
            }
        });

        lane2.setBackground(new java.awt.Color(255, 255, 255));
        lane2.setOpaque(true);

        lbldiemmonhoc.setBackground(new java.awt.Color(255, 255, 255));
        lbldiemmonhoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-clipboard-list-32.png"))); // NOI18N
        lbldiemmonhoc.setContentAreaFilled(false);
        lbldiemmonhoc.setFocusable(false);
        lbldiemmonhoc.setOpaque(true);
        lbldiemmonhoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbldiemmonhocMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbldiemmonhocMouseExited(evt);
            }
        });
        lbldiemmonhoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbldiemmonhocActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Điểm môn học");

        javax.swing.GroupLayout diemmonhocLayout = new javax.swing.GroupLayout(diemmonhoc);
        diemmonhoc.setLayout(diemmonhocLayout);
        diemmonhocLayout.setHorizontalGroup(
            diemmonhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(diemmonhocLayout.createSequentialGroup()
                .addComponent(lane2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbldiemmonhoc, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        diemmonhocLayout.setVerticalGroup(
            diemmonhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbldiemmonhoc, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/tải xuống.png"))); // NOI18N

        diemdanh.setBackground(new java.awt.Color(255, 255, 255));
        diemdanh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                diemdanhMouseClicked(evt);
            }
        });

        lane3.setBackground(new java.awt.Color(255, 255, 255));
        lane3.setOpaque(true);

        lbldiemdanh.setBackground(new java.awt.Color(255, 255, 255));
        lbldiemdanh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-user-account-32.png"))); // NOI18N
        lbldiemdanh.setContentAreaFilled(false);
        lbldiemdanh.setFocusable(false);
        lbldiemdanh.setOpaque(true);
        lbldiemdanh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbldiemdanhMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbldiemdanhMouseExited(evt);
            }
        });
        lbldiemdanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbldiemdanhActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText("Điểm danh");

        javax.swing.GroupLayout diemdanhLayout = new javax.swing.GroupLayout(diemdanh);
        diemdanh.setLayout(diemdanhLayout);
        diemdanhLayout.setHorizontalGroup(
            diemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(diemdanhLayout.createSequentialGroup()
                .addComponent(lane3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbldiemdanh, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        diemdanhLayout.setVerticalGroup(
            diemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbldiemdanh, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        guiphanhoi.setBackground(new java.awt.Color(255, 255, 255));
        guiphanhoi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                guiphanhoiMouseClicked(evt);
            }
        });

        lane6.setBackground(new java.awt.Color(255, 255, 255));
        lane6.setOpaque(true);

        lblguiphanhoi.setBackground(new java.awt.Color(255, 255, 255));
        lblguiphanhoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-send-email-32.png"))); // NOI18N
        lblguiphanhoi.setContentAreaFilled(false);
        lblguiphanhoi.setFocusable(false);
        lblguiphanhoi.setOpaque(true);
        lblguiphanhoi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblguiphanhoiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblguiphanhoiMouseExited(evt);
            }
        });
        lblguiphanhoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblguiphanhoiActionPerformed(evt);
            }
        });

        qlnamhoc.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        qlnamhoc.setText("Gửi phản hồi");

        javax.swing.GroupLayout guiphanhoiLayout = new javax.swing.GroupLayout(guiphanhoi);
        guiphanhoi.setLayout(guiphanhoiLayout);
        guiphanhoiLayout.setHorizontalGroup(
            guiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(guiphanhoiLayout.createSequentialGroup()
                .addComponent(lane6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblguiphanhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(qlnamhoc, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                .addContainerGap())
        );
        guiphanhoiLayout.setVerticalGroup(
            guiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblguiphanhoi, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(qlnamhoc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        hocbahocsinh.setBackground(new java.awt.Color(255, 255, 255));
        hocbahocsinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hocbahocsinhMouseClicked(evt);
            }
        });

        lane4.setBackground(new java.awt.Color(255, 255, 255));
        lane4.setOpaque(true);

        lblhocba.setBackground(new java.awt.Color(255, 255, 255));
        lblhocba.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-syllabus-32.png"))); // NOI18N
        lblhocba.setContentAreaFilled(false);
        lblhocba.setFocusable(false);
        lblhocba.setOpaque(true);
        lblhocba.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblhocbaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblhocbaMouseExited(evt);
            }
        });
        lblhocba.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblhocbaActionPerformed(evt);
            }
        });

        qlnamhoc1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        qlnamhoc1.setText("Học bạ học sinh");

        javax.swing.GroupLayout hocbahocsinhLayout = new javax.swing.GroupLayout(hocbahocsinh);
        hocbahocsinh.setLayout(hocbahocsinhLayout);
        hocbahocsinhLayout.setHorizontalGroup(
            hocbahocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hocbahocsinhLayout.createSequentialGroup()
                .addComponent(lane4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblhocba, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(qlnamhoc1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        hocbahocsinhLayout.setVerticalGroup(
            hocbahocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblhocba, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(qlnamhoc1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        dshocsinh.setBackground(new java.awt.Color(255, 255, 255));
        dshocsinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dshocsinhMouseClicked(evt);
            }
        });

        lane5.setBackground(new java.awt.Color(255, 255, 255));
        lane5.setOpaque(true);

        lbldshocsinh.setBackground(new java.awt.Color(255, 255, 255));
        lbldshocsinh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-save-tabs-list-32.png"))); // NOI18N
        lbldshocsinh.setContentAreaFilled(false);
        lbldshocsinh.setFocusable(false);
        lbldshocsinh.setOpaque(true);
        lbldshocsinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbldshocsinhMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbldshocsinhMouseExited(evt);
            }
        });
        lbldshocsinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbldshocsinhActionPerformed(evt);
            }
        });

        qlnamhoc2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        qlnamhoc2.setText("Danh sách học sinh");

        javax.swing.GroupLayout dshocsinhLayout = new javax.swing.GroupLayout(dshocsinh);
        dshocsinh.setLayout(dshocsinhLayout);
        dshocsinhLayout.setHorizontalGroup(
            dshocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dshocsinhLayout.createSequentialGroup()
                .addComponent(lane5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbldshocsinh, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(qlnamhoc2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dshocsinhLayout.setVerticalGroup(
            dshocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbldshocsinh, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(qlnamhoc2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        ketquacuoinam.setBackground(new java.awt.Color(255, 255, 255));
        ketquacuoinam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ketquacuoinamMouseClicked(evt);
            }
        });

        lane7.setBackground(new java.awt.Color(255, 255, 255));
        lane7.setOpaque(true);

        lblketquacuoinam.setBackground(new java.awt.Color(255, 255, 255));
        lblketquacuoinam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-education-32.png"))); // NOI18N
        lblketquacuoinam.setContentAreaFilled(false);
        lblketquacuoinam.setFocusable(false);
        lblketquacuoinam.setOpaque(true);
        lblketquacuoinam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblketquacuoinamMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblketquacuoinamMouseExited(evt);
            }
        });
        lblketquacuoinam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblketquacuoinamActionPerformed(evt);
            }
        });

        jLabel68.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel68.setText("Kết quả cuối năm");

        javax.swing.GroupLayout ketquacuoinamLayout = new javax.swing.GroupLayout(ketquacuoinam);
        ketquacuoinam.setLayout(ketquacuoinamLayout);
        ketquacuoinamLayout.setHorizontalGroup(
            ketquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ketquacuoinamLayout.createSequentialGroup()
                .addComponent(lane7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblketquacuoinam, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel68, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ketquacuoinamLayout.setVerticalGroup(
            ketquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblketquacuoinam, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(jLabel68, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        lbl_giaovien.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_giaovien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_giaovien.setText("Tên giáo viên");

        lbl_magiaovien.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_magiaovien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_magiaovien.setText("Mã GV");

        jLabel102.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(52, 152, 219));
        jLabel102.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel102.setText("Đăng xuất ?");
        jLabel102.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hocbahocsinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(dshocsinh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(guiphanhoi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(trangchu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(diemdanh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(diemmonhoc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ketquacuoinam, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_giaovien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_magiaovien, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(jLabel102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_giaovien, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_magiaovien, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(trangchu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(diemmonhoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ketquacuoinam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(diemdanh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hocbahocsinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dshocsinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(guiphanhoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 40, 220, 680);

        tanktab.setBackground(new java.awt.Color(255, 255, 255));

        tab1.setBackground(new java.awt.Color(255, 255, 255));
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab1MouseClicked(evt);
            }
        });

        lbltab1.setBackground(new java.awt.Color(255, 255, 255));
        lbltab1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        btntab1.setBackground(new java.awt.Color(255, 255, 255));
        btntab1.setContentAreaFilled(false);
        btntab1.setFocusable(false);
        btntab1.setOpaque(true);
        btntab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntab1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab1Layout = new javax.swing.GroupLayout(tab1);
        tab1.setLayout(tab1Layout);
        tab1Layout.setHorizontalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbltab1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btntab1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        tab1Layout.setVerticalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1Layout.createSequentialGroup()
                .addComponent(lbltab1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(btntab1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tab2.setBackground(new java.awt.Color(255, 255, 255));
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab2MouseClicked(evt);
            }
        });

        lbltab2.setBackground(new java.awt.Color(255, 255, 255));
        lbltab2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        btntab2.setBackground(new java.awt.Color(255, 255, 255));
        btntab2.setContentAreaFilled(false);
        btntab2.setFocusable(false);
        btntab2.setOpaque(true);
        btntab2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntab2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab2Layout = new javax.swing.GroupLayout(tab2);
        tab2.setLayout(tab2Layout);
        tab2Layout.setHorizontalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbltab2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btntab2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        tab2Layout.setVerticalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab2Layout.createSequentialGroup()
                .addComponent(lbltab2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(btntab2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tab3.setBackground(new java.awt.Color(255, 255, 255));
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab3MouseClicked(evt);
            }
        });

        lbltab3.setBackground(new java.awt.Color(255, 255, 255));
        lbltab3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        btntab3.setBackground(new java.awt.Color(255, 255, 255));
        btntab3.setContentAreaFilled(false);
        btntab3.setFocusable(false);
        btntab3.setOpaque(true);
        btntab3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntab3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab3Layout = new javax.swing.GroupLayout(tab3);
        tab3.setLayout(tab3Layout);
        tab3Layout.setHorizontalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbltab3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btntab3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        tab3Layout.setVerticalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab3Layout.createSequentialGroup()
                .addComponent(lbltab3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(btntab3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tab4.setBackground(new java.awt.Color(255, 255, 255));
        tab4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab4MouseClicked(evt);
            }
        });

        lbltab4.setBackground(new java.awt.Color(255, 255, 255));
        lbltab4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        btntab4.setBackground(new java.awt.Color(255, 255, 255));
        btntab4.setContentAreaFilled(false);
        btntab4.setFocusable(false);
        btntab4.setOpaque(true);
        btntab4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntab4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab4Layout = new javax.swing.GroupLayout(tab4);
        tab4.setLayout(tab4Layout);
        tab4Layout.setHorizontalGroup(
            tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbltab4, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btntab4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        tab4Layout.setVerticalGroup(
            tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab4Layout.createSequentialGroup()
                .addComponent(lbltab4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(btntab4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tab5.setBackground(new java.awt.Color(255, 255, 255));
        tab5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab5MouseClicked(evt);
            }
        });

        lbltab5.setBackground(new java.awt.Color(255, 255, 255));
        lbltab5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        btntab5.setBackground(new java.awt.Color(255, 255, 255));
        btntab5.setContentAreaFilled(false);
        btntab5.setFocusable(false);
        btntab5.setOpaque(true);
        btntab5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntab5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab5Layout = new javax.swing.GroupLayout(tab5);
        tab5.setLayout(tab5Layout);
        tab5Layout.setHorizontalGroup(
            tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbltab5, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btntab5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        tab5Layout.setVerticalGroup(
            tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btntab5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbltab5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tanktabLayout = new javax.swing.GroupLayout(tanktab);
        tanktab.setLayout(tanktabLayout);
        tanktabLayout.setHorizontalGroup(
            tanktabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tanktabLayout.createSequentialGroup()
                .addComponent(tab1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tab2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tab3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tab4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tab5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tanktabLayout.setVerticalGroup(
            tanktabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tanktabLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(tanktabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tab5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tanktabLayout.createSequentialGroup()
                        .addGroup(tanktabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tab1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tab2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tab3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tab4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(tanktab);
        tanktab.setBounds(230, 50, 1130, 40);

        body.setBackground(new java.awt.Color(255, 255, 255));
        body.setLayout(new java.awt.CardLayout());

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/—Pngtree—hand drawn online education online_4986515.png"))); // NOI18N
        jLabel6.setText("jLabel6");

        javax.swing.GroupLayout trangchinhLayout = new javax.swing.GroupLayout(trangchinh);
        trangchinh.setLayout(trangchinhLayout);
        trangchinhLayout.setHorizontalGroup(
            trangchinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1141, Short.MAX_VALUE)
        );
        trangchinhLayout.setVerticalGroup(
            trangchinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 668, Short.MAX_VALUE)
        );

        body.add(trangchinh, "card2");

        qlydiemdanh.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 51, 51));
        jLabel10.setText("Điểm danh");

        tbl_diemdanh.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tbl_diemdanh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ngày", "Mã học sinh", "Trạng thái", "Học kì", "Năm học"
            }
        ));
        tbl_diemdanh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbl_diemdanh.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tbl_diemdanh.setRowHeight(25);
        tbl_diemdanh.setSelectionBackground(new java.awt.Color(255, 51, 51));
        tbl_diemdanh.setShowVerticalLines(false);
        tbl_diemdanh.getTableHeader().setReorderingAllowed(false);
        tbl_diemdanh.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tbl_diemdanhMouseDragged(evt);
            }
        });
        tbl_diemdanh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_diemdanhMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbl_diemdanhMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_diemdanh);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel9.setText("Tìm kiếm :");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel12.setText("ID :");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel11.setText("Ngày :");

        cbo_trangthaidiemdanh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Có phép", "Không phép" }));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel13.setText("Mã HS :");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel14.setText("Trạng thái :");

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_user_male_32px.png"))); // NOI18N
        jButton3.setText("Cập nhật");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-cancel-delete-32.png"))); // NOI18N
        jButton2.setText("Xóa");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-available-updates-32.png"))); // NOI18N
        jButton5.setText("Làm mới");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel26.setText("Lớp :");

        lbl_tenLopCN.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        jLabel7.setText("ID tự sinh");

        javax.swing.GroupLayout qlydiemdanhLayout = new javax.swing.GroupLayout(qlydiemdanh);
        qlydiemdanh.setLayout(qlydiemdanhLayout);
        qlydiemdanhLayout.setHorizontalGroup(
            qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, qlydiemdanhLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(qlydiemdanhLayout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addGroup(qlydiemdanhLayout.createSequentialGroup()
                        .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbo_trangthaidiemdanh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbo_mahsdiemdanh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(qlydiemdanhLayout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(qlydiemdanhLayout.createSequentialGroup()
                        .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1)
                            .addGroup(qlydiemdanhLayout.createSequentialGroup()
                                .addComponent(lbl_tenLopCN, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(qlydiemdanhLayout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(lbl_ngaydiemdanh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        qlydiemdanhLayout.setVerticalGroup(
            qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlydiemdanhLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(qlydiemdanhLayout.createSequentialGroup()
                        .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(lbl_tenLopCN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_ngaydiemdanh, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbo_mahsdiemdanh, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbo_trangthaidiemdanh, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(qlydiemdanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31))
        );

        body.add(qlydiemdanh, "card3");

        qlyguiphanhoi.setBackground(new java.awt.Color(255, 255, 255));

        jLabel36.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 51, 255));
        jLabel36.setText("Gửi phản hồi.");

        tblGridView2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tblGridView2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "1", "1", null, null}
            },
            new String [] {
                "Mã HS", "Họ và tên", "Ngày sinh", "Giới tính", "Email"
            }
        ));
        tblGridView2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblGridView2.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblGridView2.setRowHeight(25);
        tblGridView2.setSelectionBackground(new java.awt.Color(51, 102, 255));
        tblGridView2.setShowVerticalLines(false);
        tblGridView2.getTableHeader().setReorderingAllowed(false);
        tblGridView2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tblGridView2MouseDragged(evt);
            }
        });
        tblGridView2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridView2MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblGridView2MouseReleased(evt);
            }
        });
        jScrollPane6.setViewportView(tblGridView2);

        jLabel37.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel37.setText("Tìm kiếm :");

        jTextField5.setText("jTextField1");

        jLabel38.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel38.setText("Người nhận :");

        jLabel39.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel39.setText("Loại gửi :");

        jRadioButton1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jRadioButton1.setText("CC");

        jRadioButton2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jRadioButton2.setText("BCC");

        jLabel40.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel40.setText("Nội dung :");

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jTextArea2.setRows(5);
        jTextArea2.setText("Nội dung\n");
        jScrollPane7.setViewportView(jTextArea2);

        jLabel41.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel41.setText("File đính kèm :");

        jTextArea5.setColumns(20);
        jTextArea5.setRows(5);
        jScrollPane8.setViewportView(jTextArea5);

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_user_male_32px.png"))); // NOI18N
        jButton6.setText("Thêm");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton11.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_upload_to_the_cloud_32px.png"))); // NOI18N
        jButton11.setText("Tải File");
        jButton11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-send-email-32.png"))); // NOI18N
        jButton12.setText("Gửi thư");
        jButton12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout qlyguiphanhoiLayout = new javax.swing.GroupLayout(qlyguiphanhoi);
        qlyguiphanhoi.setLayout(qlyguiphanhoiLayout);
        qlyguiphanhoiLayout.setHorizontalGroup(
            qlyguiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlyguiphanhoiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(qlyguiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(qlyguiphanhoiLayout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(qlyguiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(qlyguiphanhoiLayout.createSequentialGroup()
                                .addGroup(qlyguiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(qlyguiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane8)
                                    .addGroup(qlyguiphanhoiLayout.createSequentialGroup()
                                        .addComponent(jRadioButton1)
                                        .addGap(41, 41, 41)
                                        .addComponent(jRadioButton2)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(qlyguiphanhoiLayout.createSequentialGroup()
                                        .addComponent(jTextField5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton6))))
                            .addComponent(jScrollPane7)
                            .addGroup(qlyguiphanhoiLayout.createSequentialGroup()
                                .addComponent(jLabel41)
                                .addGap(18, 18, 18)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        qlyguiphanhoiLayout.setVerticalGroup(
            qlyguiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlyguiphanhoiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(qlyguiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(qlyguiphanhoiLayout.createSequentialGroup()
                        .addGroup(qlyguiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(qlyguiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(qlyguiphanhoiLayout.createSequentialGroup()
                                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane8))
                        .addGap(18, 18, 18)
                        .addGroup(qlyguiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(qlyguiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(qlyguiphanhoiLayout.createSequentialGroup()
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(qlyguiphanhoiLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(qlyguiphanhoiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        body.add(qlyguiphanhoi, "card4");

        qlydiem.setBackground(new java.awt.Color(255, 255, 255));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 102, 102));
        jLabel19.setText("Sổ điểm.");

        bodydiem.setLayout(new java.awt.CardLayout());

        tblGridView_Diem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã HS", "Họ tên", "Giới tính", "Miệng(1)", "Miệng(2)", "Miệng(3)", "15p(1)", "15p(2)", "15p(3)", "45p(1)", "45p(2)", "Điểm HK", "Điểm TB"
            }
        ));
        tblGridView_Diem.setRowHeight(25);
        tblGridView_Diem.setSelectionBackground(new java.awt.Color(52, 152, 219));
        tblGridView_Diem.getTableHeader().setReorderingAllowed(false);
        tblGridView_Diem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridView_DiemMouseClicked(evt);
            }
        });
        tblGridView_Diem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblGridView_DiemKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblGridView_DiemKeyReleased(evt);
            }
        });
        bangdiem.setViewportView(tblGridView_Diem);

        bodydiem.add(bangdiem, "card3");

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Điểm hệ số 1", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel8.setText("Kiểm tra miệng :");

        txt_diemMieng1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_diemMieng1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_diemMieng1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_diemMieng1MouseClicked(evt);
            }
        });
        txt_diemMieng1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_diemMieng1ActionPerformed(evt);
            }
        });
        txt_diemMieng1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_diemMieng1KeyPressed(evt);
            }
        });

        txt_diemMieng2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_diemMieng2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_diemMieng2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_diemMieng2ActionPerformed(evt);
            }
        });
        txt_diemMieng2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_diemMieng2KeyPressed(evt);
            }
        });

        txt_diemMieng3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_diemMieng3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_diemMieng3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_diemMieng3ActionPerformed(evt);
            }
        });
        txt_diemMieng3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_diemMieng3KeyPressed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel27.setText("Kiểm tra 15 phút :");

        txt_15p1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_15p1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_15p1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_15p1ActionPerformed(evt);
            }
        });
        txt_15p1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_15p1KeyPressed(evt);
            }
        });

        txt_15p2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_15p2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_15p2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_15p2ActionPerformed(evt);
            }
        });
        txt_15p2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_15p2KeyPressed(evt);
            }
        });

        txt_15p3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_15p3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_15p3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_15p3ActionPerformed(evt);
            }
        });
        txt_15p3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_15p3KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addGap(28, 28, 28)
                        .addComponent(txt_15p1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(38, 38, 38)
                        .addComponent(txt_diemMieng1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(txt_diemMieng2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_diemMieng3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(txt_15p2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(txt_15p3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_diemMieng2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_diemMieng3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txt_diemMieng1))
                .addGap(54, 54, 54)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_15p2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_15p3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txt_15p1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Điểm hệ số 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel28.setText("Kiểm tra 45 phút :");

        txt_45p1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_45p1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_45p1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_45p1ActionPerformed(evt);
            }
        });
        txt_45p1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_45p1KeyPressed(evt);
            }
        });

        txt_45p2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_45p2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_45p2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_45p2ActionPerformed(evt);
            }
        });
        txt_45p2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_45p2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_45p2KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addGap(28, 28, 28)
                .addComponent(txt_45p1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_45p2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_45p2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_45p1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Điểm hệ số 3", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel29.setText("Kiểm tra học kì  :");

        txt_hk.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_hk.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_hk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_hkActionPerformed(evt);
            }
        });
        txt_hk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_hkKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29)
                .addGap(28, 28, 28)
                .addComponent(txt_hk, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(113, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_hk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Điểm trung bình ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        lbl_diemTBM.setFont(new java.awt.Font("Tahoma", 1, 21)); // NOI18N
        lbl_diemTBM.setForeground(new java.awt.Color(255, 0, 0));
        lbl_diemTBM.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addComponent(lbl_diemTBM, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(lbl_diemTBM, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE))
        );

        btn_capNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_update_file_32px.png"))); // NOI18N
        btn_capNhat.setText("Cập Nhập");
        btn_capNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhatActionPerformed(evt);
            }
        });

        btn_capNhat1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_back_to_32px_1.png"))); // NOI18N
        btn_capNhat1.setText("Hủy");
        btn_capNhat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout monchamdiemLayout = new javax.swing.GroupLayout(monchamdiem);
        monchamdiem.setLayout(monchamdiemLayout);
        monchamdiemLayout.setHorizontalGroup(
            monchamdiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(monchamdiemLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addGroup(monchamdiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(93, 93, 93)
                .addGroup(monchamdiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_capNhat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_capNhat1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(53, 53, 53))
        );
        monchamdiemLayout.setVerticalGroup(
            monchamdiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(monchamdiemLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(monchamdiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(monchamdiemLayout.createSequentialGroup()
                        .addGroup(monchamdiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(monchamdiemLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(monchamdiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(monchamdiemLayout.createSequentialGroup()
                                .addComponent(btn_capNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_capNhat1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, monchamdiemLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(74, Short.MAX_VALUE))
        );

        bodydiem.add(monchamdiem, "card3");

        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Điểm hệ số 1", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel32.setText("Kiểm tra miệng :");

        jLabel33.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel33.setText("Kiểm tra 15 phút :");

        cbo_diemTX1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đạt", "Chưa Đạt" }));
        cbo_diemTX1.setBorder(null);

        cbo_diemTX2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đạt", "Chưa Đạt" }));
        cbo_diemTX2.setBorder(null);
        cbo_diemTX2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_diemTX2ActionPerformed(evt);
            }
        });

        cbo_diemTX3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đạt", "Chưa Đạt" }));
        cbo_diemTX3.setBorder(null);

        cbo_diemTX4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đạt", "Chưa Đạt" }));
        cbo_diemTX4.setBorder(null);

        cbo_diemTX5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đạt", "Chưa Đạt" }));
        cbo_diemTX5.setBorder(null);
        cbo_diemTX5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_diemTX5ActionPerformed(evt);
            }
        });

        cbo_diemTX6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đạt", "Chưa Đạt" }));
        cbo_diemTX6.setBorder(null);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(cbo_diemTX1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(cbo_diemTX2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(cbo_diemTX3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(jLabel33))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                        .addComponent(cbo_diemTX4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(cbo_diemTX5, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbo_diemTX6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_diemTX1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_diemTX2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_diemTX3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_diemTX4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_diemTX5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_diemTX6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Điểm hệ số 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        jLabel34.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel34.setText("Kiểm tra 45 phút :");

        cbo_diemTX7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đạt", "Chưa Đạt" }));
        cbo_diemTX7.setBorder(null);

        cbo_diemTX8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đạt", "Chưa Đạt" }));
        cbo_diemTX8.setBorder(null);
        cbo_diemTX8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_diemTX8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel34))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(cbo_diemTX7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(cbo_diemTX8, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_diemTX7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_diemTX8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Điểm hệ số 3", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        jLabel35.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel35.setText("Kiểm tra học kì  :");

        cbo_diemTX9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đạt", "Chưa Đạt" }));
        cbo_diemTX9.setBorder(null);

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addGap(32, 32, 32)
                .addComponent(cbo_diemTX9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_diemTX9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Điểm trung bình ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        lbl_diemTBMDG.setFont(new java.awt.Font("Tahoma", 1, 21)); // NOI18N
        lbl_diemTBMDG.setForeground(new java.awt.Color(255, 0, 0));
        lbl_diemTBMDG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(lbl_diemTBMDG, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lbl_diemTBMDG, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_capNhat2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_update_file_32px.png"))); // NOI18N
        btn_capNhat2.setText("Cập Nhật");
        btn_capNhat2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat2ActionPerformed(evt);
            }
        });

        btn_capNhat3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_back_to_32px_1.png"))); // NOI18N
        btn_capNhat3.setText("Hủy");
        btn_capNhat3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mondanhgiaLayout = new javax.swing.GroupLayout(mondanhgia);
        mondanhgia.setLayout(mondanhgiaLayout);
        mondanhgiaLayout.setHorizontalGroup(
            mondanhgiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mondanhgiaLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addGroup(mondanhgiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(93, 93, 93)
                .addGroup(mondanhgiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_capNhat2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_capNhat3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(53, 53, 53))
        );
        mondanhgiaLayout.setVerticalGroup(
            mondanhgiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mondanhgiaLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(mondanhgiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mondanhgiaLayout.createSequentialGroup()
                        .addGroup(mondanhgiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(mondanhgiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mondanhgiaLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btn_capNhat2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_capNhat3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mondanhgiaLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        bodydiem.add(mondanhgia, "card3");

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin học sinh", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel20.setText("Mã học sinh :");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel21.setText("Họ và tên :");

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel22.setText("Giới tính :");

        lbl_hvt3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_hvt3.setText("Nguyễn Xuân Bách");

        lbl_gt3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_gt3.setText("04/06/2000");

        lbl_mhs3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_mhs3.setText("02932823");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                        .addGap(0, 21, Short.MAX_VALUE)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_mhs3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_hvt3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_gt3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_mhs3)
                .addGap(12, 12, 12)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_hvt3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_gt3)
                .addGap(6, 6, 6))
        );

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel23.setText("Tìm kiếm :");

        txt_searchdiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchdiemKeyReleased(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel24.setText("Lớp dạy :");

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel25.setText("Cách chấm điểm :");

        cbo_cacchamdiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chấm điểm", "Đánh giá" }));

        btnchinhsua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-save-tabs-list-32.png"))); // NOI18N
        btnchinhsua.setText("Chỉnh sửa");
        btnchinhsua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnchinhsuaActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel30.setText("Môn dạy :");

        cbo_Mon_Diem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_Mon_DiemItemStateChanged(evt);
            }
        });

        Xuat_Diem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-uninstalling-updates-32.png"))); // NOI18N
        Xuat_Diem.setText("Xuất Excel");
        Xuat_Diem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Xuat_DiemActionPerformed(evt);
            }
        });

        Nhap_Diem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-installing-updates-32.png"))); // NOI18N
        Nhap_Diem.setText("Nhập Excel");
        Nhap_Diem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Nhap_DiemActionPerformed(evt);
            }
        });

        Tai_Form_Diem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-downloads-32.png"))); // NOI18N
        Tai_Form_Diem.setText("Tải Form");
        Tai_Form_Diem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Tai_Form_DiemActionPerformed(evt);
            }
        });

        btn_timkiem.setText("Tìm kiếm");
        btn_timkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_timkiemActionPerformed(evt);
            }
        });

        btn_lammoi.setText("Làm mới");
        btn_lammoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_lammoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout qlydiemLayout = new javax.swing.GroupLayout(qlydiem);
        qlydiem.setLayout(qlydiemLayout);
        qlydiemLayout.setHorizontalGroup(
            qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlydiemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(qlydiemLayout.createSequentialGroup()
                        .addComponent(bodydiem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(qlydiemLayout.createSequentialGroup()
                        .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(qlydiemLayout.createSequentialGroup()
                                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(qlydiemLayout.createSequentialGroup()
                                        .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel25))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cbo_Lop_Diem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cbo_cacchamdiem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(qlydiemLayout.createSequentialGroup()
                                        .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(55, 55, 55)
                                        .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_searchdiem, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbo_Mon_Diem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(78, 78, 78)
                        .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Xuat_Diem, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Nhap_Diem, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tai_Form_Diem, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnchinhsua, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(qlydiemLayout.createSequentialGroup()
                                .addComponent(btn_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_lammoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(34, 34, 34))))
        );
        qlydiemLayout.setVerticalGroup(
            qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlydiemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(qlydiemLayout.createSequentialGroup()
                        .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(qlydiemLayout.createSequentialGroup()
                                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(13, 13, 13))
                            .addGroup(qlydiemLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btn_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_lammoi, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnchinhsua, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Xuat_Diem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Nhap_Diem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Tai_Form_Diem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)))
                        .addComponent(bodydiem, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(qlydiemLayout.createSequentialGroup()
                        .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_searchdiem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbo_Mon_Diem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbo_Lop_Diem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(qlydiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbo_cacchamdiem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        body.add(qlydiem, "card5");

        maintrangchu.setBackground(new java.awt.Color(153, 153, 255));

        javax.swing.GroupLayout maintrangchuLayout = new javax.swing.GroupLayout(maintrangchu);
        maintrangchu.setLayout(maintrangchuLayout);
        maintrangchuLayout.setHorizontalGroup(
            maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1130, Short.MAX_VALUE)
        );
        maintrangchuLayout.setVerticalGroup(
            maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );

        body.add(maintrangchu, "card6");

        qlyhocba.setBackground(new java.awt.Color(255, 255, 255));

        jLabel47.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 102, 102));
        jLabel47.setText("Học bạ học sinh");

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin học sinh", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        jLabel49.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel49.setText("Mã học sinh :");

        jLabel50.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel50.setText("Họ và tên :");

        jLabel51.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel51.setText("Giới tính :");

        lbl_hvt4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_hvt4.setText("Nguyễn Xuân Bách");

        lbl_gt4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_gt4.setText("04/06/2000");

        lbl_mhs4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_mhs4.setText("02932823");

        jLabel52.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel52.setText("Ngày sinh:");

        lbl_mhs5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_mhs5.setText("02932823");

        jLabel53.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel53.setText("CMND:");

        lbl_hvt5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_hvt5.setText("Nguyễn Xuân Bách");

        btn_capNhat9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btn_capNhat9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-save-tabs-list-32.png"))); // NOI18N
        btn_capNhat9.setText("Chi tiết học bạ");
        btn_capNhat9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51)
                            .addComponent(jLabel49)
                            .addComponent(jLabel50)))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_gt4, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_hvt4, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_mhs4, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGap(0, 27, Short.MAX_VALUE)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel52)
                            .addComponent(jLabel53)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl_hvt5, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_mhs5, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(btn_capNhat9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_mhs5)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_hvt5, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                        .addGap(14, 14, 14)
                        .addComponent(btn_capNhat9, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_mhs4)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_hvt4, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel51)
                        .addGap(6, 6, 6)
                        .addComponent(lbl_gt4)))
                .addContainerGap())
        );

        jLabel54.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel54.setText("Tìm kiếm :");

        txt_searchdiem1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchdiem1KeyReleased(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel55.setText("Niên học :");

        cbo_Mon_Diem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_Mon_Diem1ActionPerformed(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel56.setText("Khối :");

        cbo_Mon_Diem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_Mon_Diem2ActionPerformed(evt);
            }
        });

        jLabel62.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel62.setText("Lớp :");

        cbo_Mon_Diem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_Mon_Diem3ActionPerformed(evt);
            }
        });

        btn_capNhat10.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btn_capNhat10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_32px_1.png"))); // NOI18N
        btn_capNhat10.setText("Tìm kiếm");
        btn_capNhat10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat10ActionPerformed(evt);
            }
        });

        tblGridView3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"098992323", "Nguyễn Xuân Bách", "04/06/2020", "9", "9"},
                {"2", "2", "2", "2", "2"},
                {"3", "3", "3", "3", "3"},
                {"5", "4", "4", "4", "4"},
                {"5", "5", "0", "0", "0"},
                {"5", "5", null, null, null},
                {"5", "5", null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã học sinh", "Họ và tên", "Giới tính", "Ngày sinh", "CMND"
            }
        ));
        tblGridView3.setRowHeight(25);
        tblGridView3.setSelectionBackground(new java.awt.Color(52, 152, 219));
        tblGridView3.getTableHeader().setReorderingAllowed(false);
        tblGridView3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridView3MouseClicked(evt);
            }
        });
        tblGridView3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblGridView3KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblGridView3KeyReleased(evt);
            }
        });
        banglop.setViewportView(tblGridView3);

        javax.swing.GroupLayout qlyhocbaLayout = new javax.swing.GroupLayout(qlyhocba);
        qlyhocba.setLayout(qlyhocbaLayout);
        qlyhocbaLayout.setHorizontalGroup(
            qlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, qlyhocbaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(qlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, qlyhocbaLayout.createSequentialGroup()
                        .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addGroup(qlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55)
                        .addGroup(qlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(qlyhocbaLayout.createSequentialGroup()
                                .addComponent(cbo_Mon_Diem3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_capNhat10, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cbo_Mon_Diem2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(qlyhocbaLayout.createSequentialGroup()
                                .addComponent(txt_searchdiem1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cbo_Mon_Diem1, 0, 318, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, qlyhocbaLayout.createSequentialGroup()
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(qlyhocbaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(banglop, javax.swing.GroupLayout.PREFERRED_SIZE, 1069, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37))
        );
        qlyhocbaLayout.setVerticalGroup(
            qlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlyhocbaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(qlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(qlyhocbaLayout.createSequentialGroup()
                        .addGroup(qlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_searchdiem1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(qlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbo_Mon_Diem1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(qlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbo_Mon_Diem2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(qlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbo_Mon_Diem3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_capNhat10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(banglop, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        body.add(qlyhocba, "card7");

        qlyhocsinh.setBackground(new java.awt.Color(255, 255, 255));

        tblGridView1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tblGridView1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "1", "1", null}
            },
            new String [] {
                "Mã HS", "Họ tên", "Ngày sinh", "Giới tính"
            }
        ));
        tblGridView1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblGridView1.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblGridView1.setRowHeight(25);
        tblGridView1.setSelectionBackground(new java.awt.Color(51, 102, 255));
        tblGridView1.setShowVerticalLines(false);
        tblGridView1.getTableHeader().setReorderingAllowed(false);
        tblGridView1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tblGridView1MouseDragged(evt);
            }
        });
        tblGridView1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridView1MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblGridView1MouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblGridView1);

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel17.setText("Tìm kiếm :");

        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 51, 255));
        jLabel18.setText("Danh sách học sinh");

        jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        image.setBackground(new java.awt.Color(0, 51, 204));
        image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/User.png"))); // NOI18N

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel58.setText("Mã học sinh :");

        lbl_mahs.setBackground(new java.awt.Color(255, 255, 255));
        lbl_mahs.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel57.setText("Họ và tên :");

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel59.setText("Giới tính :");

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel60.setText("Ngày sinh :");

        jLabel64.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel64.setText("Dân tộc :");

        jLabel65.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel65.setText("Tôn giáo :");

        jLabel66.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel66.setText("Nơi sinh :");

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel67.setText("CMND :");

        lbl_HoTen.setBackground(new java.awt.Color(255, 255, 255));
        lbl_HoTen.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_GioiTinh.setBackground(new java.awt.Color(255, 255, 255));
        lbl_GioiTinh.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_Ngaysinh.setBackground(new java.awt.Color(255, 255, 255));
        lbl_Ngaysinh.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_DanToc.setBackground(new java.awt.Color(255, 255, 255));
        lbl_DanToc.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_TonGiao.setBackground(new java.awt.Color(255, 255, 255));
        lbl_TonGiao.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_NoiSinh.setBackground(new java.awt.Color(255, 255, 255));
        lbl_NoiSinh.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_CMND.setBackground(new java.awt.Color(255, 255, 255));
        lbl_CMND.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                        .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(image)
                        .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel59)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl_mahs, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(lbl_GioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(118, 118, 118)
                                    .addComponent(lbl_HoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(lbl_DanToc, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                                .addComponent(lbl_TonGiao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_NoiSinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_CMND, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lbl_Ngaysinh, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_mahs, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_HoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_GioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Ngaysinh, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_DanToc, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_TonGiao, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_NoiSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_CMND, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Chi tiết học sinh", jPanel2);

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel61.setText("Lớp :");

        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel75.setText("Ngày vào đoàn :");

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel69.setText("Số điện thoại :");

        jLabel70.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel70.setText("Email :");

        jLabel71.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel71.setText("Trạng thái  :");

        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel72.setText("Địa chỉ :");

        lbl_DiaCHi.setColumns(20);
        lbl_DiaCHi.setRows(5);
        jScrollPane3.setViewportView(lbl_DiaCHi);

        lbl_Lop2.setBackground(new java.awt.Color(255, 255, 255));
        lbl_Lop2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_NgayVaoDoan.setBackground(new java.awt.Color(255, 255, 255));
        lbl_NgayVaoDoan.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_SDT.setBackground(new java.awt.Color(255, 255, 255));
        lbl_SDT.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_path.setBackground(new java.awt.Color(255, 255, 255));
        lbl_path.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_TrangThai.setBackground(new java.awt.Color(255, 255, 255));
        lbl_TrangThai.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(lbl_Lop2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel71, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel75, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 8, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(lbl_NgayVaoDoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lbl_SDT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_path, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_TrangThai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Lop2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_NgayVaoDoan, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_SDT, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_path, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_TrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 62, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Chi tiết  học sinh", jPanel3);

        jLabel73.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel73.setText("Họ tên bố :");

        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel74.setText("Số điện thoại bố :");

        jLabel76.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel76.setText("Đơn vị công tác bố :");

        lbl_DVCTBo.setColumns(20);
        lbl_DVCTBo.setRows(5);
        jScrollPane4.setViewportView(lbl_DVCTBo);

        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel79.setText("Họ tên mẹ :");

        jLabel78.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel78.setText("Số điện thoại mẹ:");

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel77.setText("Đơn vị công tác mẹ:");

        lbl_DVCTMe.setColumns(20);
        lbl_DVCTMe.setRows(5);
        jScrollPane5.setViewportView(lbl_DVCTMe);

        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel80.setText("Người dám hộ :");

        lbl_NguoiDamHo.setColumns(20);
        lbl_NguoiDamHo.setRows(5);
        jScrollPane9.setViewportView(lbl_NguoiDamHo);

        lbl_HoTenBo.setBackground(new java.awt.Color(255, 255, 255));
        lbl_HoTenBo.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_SDTBo.setBackground(new java.awt.Color(255, 255, 255));
        lbl_SDTBo.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_HotenMe.setBackground(new java.awt.Color(255, 255, 255));
        lbl_HotenMe.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        lbl_SDTMe.setBackground(new java.awt.Color(255, 255, 255));
        lbl_SDTMe.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel76)
                                    .addComponent(jLabel74))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(67, 67, 67))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel79, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(68, 68, 68)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                            .addComponent(lbl_HoTenBo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_SDTBo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_HotenMe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel78)
                        .addGap(24, 24, 24)
                        .addComponent(lbl_SDTMe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel77)
                            .addComponent(jLabel80))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane9)
                            .addComponent(jScrollPane5))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_HoTenBo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_SDTBo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_HotenMe, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_SDTMe, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Phụ huynh học sinh", jPanel4);

        jLabel44.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel44.setText("Lớp :");

        lbl_Lop1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_Lop1.setText("Tên Lớp");

        txtSS.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtSS.setText("jLabel15");

        jLabel45.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel45.setText("Sĩ số :");

        jLabel46.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel46.setText("Sĩ số nam :");

        txtSSNam.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtSSNam.setText("jLabel15");

        jLabel48.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel48.setText("Sĩ số nữ :");

        txtssNu.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtssNu.setText("jLabel15");

        javax.swing.GroupLayout qlyhocsinhLayout = new javax.swing.GroupLayout(qlyhocsinh);
        qlyhocsinh.setLayout(qlyhocsinhLayout);
        qlyhocsinhLayout.setHorizontalGroup(
            qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlyhocsinhLayout.createSequentialGroup()
                .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, qlyhocsinhLayout.createSequentialGroup()
                        .addContainerGap(632, Short.MAX_VALUE)
                        .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(qlyhocsinhLayout.createSequentialGroup()
                                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_Lop1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(qlyhocsinhLayout.createSequentialGroup()
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(qlyhocsinhLayout.createSequentialGroup()
                                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(24, 24, 24)
                                    .addComponent(txtSSNam, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 23, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, qlyhocsinhLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSS, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtssNu, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)))
                .addContainerGap())
            .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(qlyhocsinhLayout.createSequentialGroup()
                    .addGap(17, 17, 17)
                    .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(17, 518, Short.MAX_VALUE)))
        );
        qlyhocsinhLayout.setVerticalGroup(
            qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlyhocsinhLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSS, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(qlyhocsinhLayout.createSequentialGroup()
                        .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_Lop1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel46)
                        .addComponent(txtSSNam, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtssNu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(qlyhocsinhLayout.createSequentialGroup()
                    .addGap(17, 17, 17)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(17, Short.MAX_VALUE)))
        );

        body.add(qlyhocsinh, "card8");

        kquacuoinam.setBackground(new java.awt.Color(255, 255, 255));

        jLabel84.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel84.setText("Tìm kiếm :");

        txt_timkiemkqcuoinam.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txt_timkiemkqcuoinam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_timkiemkqcuoinamKeyReleased(evt);
            }
        });

        jLabel85.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(0, 51, 255));
        jLabel85.setText("Kết quả cuối năm.");

        jLabel124.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel124.setText("Lớp :");

        lbl_SiSo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_SiSo.setText("jLabel15");

        jLabel127.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel127.setText("Sĩ số :");

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin học sinh", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        jLabel101.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel101.setText("Mã học sinh :");

        jLabel103.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel103.setText("Họ và tên :");

        jLabel104.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel104.setText("Ngày sinh :");

        lbl_hvt.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_hvt.setText("Nguyễn Xuân Bách");

        lbl_ns.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_ns.setText("04/06/2000");

        lbl_mhs.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_mhs.setText("02932823");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel101)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_mhs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel104)
                        .addGap(32, 32, 32)
                        .addComponent(lbl_ns, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel103)
                        .addGap(36, 36, 36)
                        .addComponent(lbl_hvt, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101)
                    .addComponent(lbl_mhs))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel103)
                    .addComponent(lbl_hvt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel104)
                    .addComponent(lbl_ns))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Điểm trung bình", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        jLabel105.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel105.setText("Học kỳ 1 :");

        jLabel106.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel106.setText("Học kỳ 2 :");

        lbl_KQTB1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_KQTB1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_KQTB1.setText("8");

        lbl_KQTB2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_KQTB2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_KQTB2.setText("8");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel105)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_KQTB1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel106)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_KQTB2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel105, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(lbl_KQTB1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel106, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_KQTB2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hạnh kiểm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        jLabel107.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel107.setText("Học kỳ 1 :");

        jLabel108.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel108.setText("Học kỳ 2 :");

        jLabel109.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel109.setText("Cả năm :");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel109)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_HanhKiemFull, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel108)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_HanhKiem2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel107)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_HanhKiem1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel107, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_HanhKiem1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel108, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_HanhKiem2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel109, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_HanhKiemFull, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Học lực", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        jLabel110.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel110.setText("Học kỳ 1 :");

        jLabel111.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel111.setText("Học kỳ 2 :");

        jLabel112.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel112.setText("Cả năm :");

        lbl_HocLuc1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_HocLuc1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_HocLuc1.setText("Giỏi");

        lbl_HocLuc2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_HocLuc2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_HocLuc2.setText("Khá");

        lbl_HocLuc3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_HocLuc3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_HocLuc3.setText("Trung Bình");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel110)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_HocLuc1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel112)
                        .addGap(14, 14, 14)
                        .addComponent(lbl_HocLuc3, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel111)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_HocLuc2, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel110, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_HocLuc1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_HocLuc2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel112, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_HocLuc3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh hiệu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        jLabel89.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel89.setText("Học kỳ 1 :");

        jLabel90.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel90.setText("Học kỳ 2 :");

        jLabel91.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel91.setText("Cả năm :");

        lbl_DanhHieu4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_DanhHieu4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DanhHieu4.setText("Giỏi");

        lbl_DanhHieu5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_DanhHieu5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DanhHieu5.setText("Tiến Tiến");

        lbl_DanhHieu6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_DanhHieu6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_DanhHieu6.setText("Tiên Tiến");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel89)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_DanhHieu4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel91)
                        .addGap(14, 14, 14)
                        .addComponent(lbl_DanhHieu6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel90)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_DanhHieu5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_DanhHieu4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_DanhHieu5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_DanhHieu6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Vắng có phép", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        jLabel92.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel92.setText("Cả năm :");

        lbl_VangCP.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_VangCP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_VangCP.setText("5");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel92)
                .addGap(14, 14, 14)
                .addComponent(lbl_VangCP, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_VangCP, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Nghỉ có phép", jPanel5);

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Vắng không phép", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        jLabel93.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel93.setText("Cả năm :");

        lbl_VangCP1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_VangCP1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_VangCP1.setText("5");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel93)
                .addGap(14, 14, 14)
                .addComponent(lbl_VangCP1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_VangCP1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Nghỉ không phép", jPanel10);

        tblGridView_KetQuaCuoiNam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"098992323", "Nguyễn Xuân Bách", "04/06/2020", "9", "9", "9", "9", "9", "9", "9", null, null},
                {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2", null, null},
                {"3", "3", "3", "3", "3", "3", "3", "3", "3", "3", null, null},
                {"5", "4", "4", "4", "4", "4", "4", "4", "4", "4", null, null},
                {"5", "5", "0", "0", "0", "0", "0", "0", "0", "0", null, null},
                {"5", "5", null, null, null, null, null, null, null, null, null, null},
                {"5", "5", null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã học sinh", "Họ tên", "Ngày sinh", "Điểm học kỳ 1", "Điểm học kỳ 2", "Điểm cả năm", "Hạnh Kiểm", "Học Lực", "Danh Hiệu", "Vắng có phép", "Vắng không phép", "Ghi chú"
            }
        ));
        tblGridView_KetQuaCuoiNam.setRowHeight(25);
        tblGridView_KetQuaCuoiNam.setSelectionBackground(new java.awt.Color(52, 152, 219));
        tblGridView_KetQuaCuoiNam.getTableHeader().setReorderingAllowed(false);
        tblGridView_KetQuaCuoiNam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridView_KetQuaCuoiNamMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tblGridView_KetQuaCuoiNam);

        Xuat_KQCN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-installing-updates-32.png"))); // NOI18N
        Xuat_KQCN.setText("Xuất Excel");
        Xuat_KQCN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Xuat_KQCNActionPerformed(evt);
            }
        });

        cbo_LoCN_KQCN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_LoCN_KQCNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kquacuoinamLayout = new javax.swing.GroupLayout(kquacuoinam);
        kquacuoinam.setLayout(kquacuoinamLayout);
        kquacuoinamLayout.setHorizontalGroup(
            kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kquacuoinamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kquacuoinamLayout.createSequentialGroup()
                        .addGroup(kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 784, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(kquacuoinamLayout.createSequentialGroup()
                                .addGroup(kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(kquacuoinamLayout.createSequentialGroup()
                                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(kquacuoinamLayout.createSequentialGroup()
                                        .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_timkiemkqcuoinam, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(Xuat_KQCN, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kquacuoinamLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel124)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbo_LoCN_KQCN, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel127, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_SiSo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))))
            .addGroup(kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(kquacuoinamLayout.createSequentialGroup()
                    .addGap(17, 17, 17)
                    .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(870, Short.MAX_VALUE)))
        );
        kquacuoinamLayout.setVerticalGroup(
            kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kquacuoinamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel124, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel127, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_SiSo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbo_LoCN_KQCN, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kquacuoinamLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(kquacuoinamLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(Xuat_KQCN, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25)
                .addGroup(kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kquacuoinamLayout.createSequentialGroup()
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTabbedPane2))
                    .addGroup(kquacuoinamLayout.createSequentialGroup()
                        .addGroup(kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_timkiemkqcuoinam, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(kquacuoinamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(kquacuoinamLayout.createSequentialGroup()
                    .addGap(17, 17, 17)
                    .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(579, Short.MAX_VALUE)))
        );

        body.add(kquacuoinam, "card8");

        getContentPane().add(body);
        body.setBounds(230, 90, 1130, 630);

        setSize(new java.awt.Dimension(1362, 722));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseEntered
        btnExit.setBackground(new Color(232, 17, 35));
    }//GEN-LAST:event_btnExitMouseEntered

    private void btnExitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseExited
        btnExit.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnExitMouseExited

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnMinimizeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimizeMouseEntered
        btnMinimize.setBackground(new Color(229, 229, 229));
    }//GEN-LAST:event_btnMinimizeMouseEntered

    private void btnMinimizeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimizeMouseExited
        btnMinimize.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnMinimizeMouseExited

    private void btnMinimizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinimizeActionPerformed
        this.setState(Frame.ICONIFIED);
    }//GEN-LAST:event_btnMinimizeActionPerformed

    private void pnlHeaderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlHeaderMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_pnlHeaderMousePressed

    private void pnlHeaderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlHeaderMouseDragged
        if (maximized) {
            int x = evt.getXOnScreen();
            int y = evt.getYOnScreen();
            this.setLocation(x - xMouse, y - yMouse);
        }
    }//GEN-LAST:event_pnlHeaderMouseDragged

    private void lbltrangchuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbltrangchuMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lbltrangchuMouseEntered

    private void lbltrangchuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbltrangchuMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lbltrangchuMouseExited

    private void lbltrangchuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbltrangchuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbltrangchuActionPerformed

    private void lbldiemmonhocMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbldiemmonhocMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lbldiemmonhocMouseEntered

    private void lbldiemmonhocMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbldiemmonhocMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lbldiemmonhocMouseExited

    private void lbldiemmonhocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbldiemmonhocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbldiemmonhocActionPerformed

    private void diemmonhocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_diemmonhocMouseClicked
        lane2.setBackground(new Color(119, 84, 248));
        lbldiemmonhoc.setBackground(new Color(237, 234, 242));
        diemmonhoc.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane3.setBackground(new Color(255, 255, 255));
        lbldiemdanh.setBackground(new Color(255, 255, 255));
        diemdanh.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lblhocba.setBackground(new Color(255, 255, 255));
        hocbahocsinh.setBackground(new Color(255, 255, 255));

        lane5.setBackground(new Color(255, 255, 255));
        lbldshocsinh.setBackground(new Color(255, 255, 255));
        dshocsinh.setBackground(new Color(255, 255, 255));

        lane6.setBackground(new Color(255, 255, 255));
        lblguiphanhoi.setBackground(new Color(255, 255, 255));
        guiphanhoi.setBackground(new Color(255, 255, 255));

        lane7.setBackground(new Color(255, 255, 255));
        lblketquacuoinam.setBackground(new Color(255, 255, 255));
        ketquacuoinam.setBackground(new Color(255, 255, 255));

        String tieude2 = "Điểm môn học";
        checktab(tieude2);

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(qlydiem);
        body.repaint();
        body.revalidate();
        this.LoadMonDay();
        this.LoadLopDay();
        this.timkiem();
        this.reset();
    }//GEN-LAST:event_diemmonhocMouseClicked

    private void trangchuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trangchuMouseClicked

        lane1.setBackground(new Color(119, 84, 248));
        lbltrangchu.setBackground(new Color(237, 234, 242));
        trangchu.setBackground(new Color(237, 234, 242));

        lane2.setBackground(new Color(255, 255, 255));
        lbldiemmonhoc.setBackground(new Color(255, 255, 255));
        diemmonhoc.setBackground(new Color(255, 255, 255));

        lane3.setBackground(new Color(255, 255, 255));
        lbldiemdanh.setBackground(new Color(255, 255, 255));
        diemdanh.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lblhocba.setBackground(new Color(255, 255, 255));
        hocbahocsinh.setBackground(new Color(255, 255, 255));

        lane5.setBackground(new Color(255, 255, 255));
        lbldshocsinh.setBackground(new Color(255, 255, 255));
        dshocsinh.setBackground(new Color(255, 255, 255));

        lane6.setBackground(new Color(255, 255, 255));
        lblguiphanhoi.setBackground(new Color(255, 255, 255));
        guiphanhoi.setBackground(new Color(255, 255, 255));

        String tieude2 = "Trang chủ";
        checktab(tieude2);
        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(maintrangchu);
        body.repaint();
        body.revalidate();

    }//GEN-LAST:event_trangchuMouseClicked


    private void btnMinimize4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimize4MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMinimize4MouseEntered

    private void btnMinimize4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimize4MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMinimize4MouseExited

    private void btnMinimize4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinimize4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMinimize4ActionPerformed

    private void btntab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntab1ActionPerformed

        if (lbltab5.getText().length() > 0) {
            clicktab(lbltab2.getText());
            lbltab1.setText(lbltab2.getText());
            lbltab2.setText(lbltab3.getText());
            lbltab3.setText(lbltab4.getText());
            lbltab4.setText(lbltab5.getText());
            lbltab5.setText("");
            btntab5.setIcon(null);
            tab5.setBackground(new Color(240, 240, 240));
            btntab5.setBackground(new Color(240, 240, 240));
        } else if (lbltab4.getText().length() > 0) {
            clicktab(lbltab2.getText());
            lbltab1.setText(lbltab2.getText());
            lbltab2.setText(lbltab3.getText());
            lbltab3.setText(lbltab4.getText());
            lbltab4.setText("");
            btntab4.setIcon(null);
            tab4.setBackground(new Color(240, 240, 240));
            btntab4.setBackground(new Color(240, 240, 240));
        } else if (lbltab3.getText().length() > 0) {
            clicktab(lbltab2.getText());
            lbltab1.setText(lbltab2.getText());
            lbltab2.setText(lbltab3.getText());
            lbltab3.setText("");
            btntab3.setIcon(null);
            tab3.setBackground(new Color(240, 240, 240));
            btntab3.setBackground(new Color(240, 240, 240));
        } else if (lbltab2.getText().length() > 0) {
            clicktab(lbltab2.getText());
            lbltab1.setText(lbltab2.getText());
            lbltab2.setText("");
            btntab2.setIcon(null);
            tab2.setBackground(new Color(240, 240, 240));
            btntab2.setBackground(new Color(240, 240, 240));
        } else if (lbltab1.getText().length() > 0) {
            clicktab(lbltab2.getText());
            lbltab1.setText("");
            btntab1.setIcon(null);
            btntab1.setBackground(new Color(255, 255, 255));
            btntab2.setBackground(new Color(255, 255, 255));
            btntab3.setBackground(new Color(255, 255, 255));
            btntab4.setBackground(new Color(255, 255, 255));
            btntab5.setBackground(new Color(255, 255, 255));
            tanktab.setBackground(new Color(255, 255, 255));
            tab1.setBackground(new Color(255, 255, 255));
            tab2.setBackground(new Color(255, 255, 255));
            tab3.setBackground(new Color(255, 255, 255));
            tab4.setBackground(new Color(255, 255, 255));
            tab5.setBackground(new Color(255, 255, 255));
        }
    }//GEN-LAST:event_btntab1ActionPerformed

    private void btntab2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntab2ActionPerformed
        if (lbltab5.getText().length() > 0) {
            clicktab(lbltab3.getText());
            lbltab2.setText(lbltab3.getText());
            lbltab3.setText(lbltab4.getText());
            lbltab4.setText(lbltab5.getText());
            lbltab5.setText("");
            btntab5.setIcon(null);
            tab5.setBackground(new Color(240, 240, 240));
            btntab5.setBackground(new Color(240, 240, 240));
        } else if (lbltab4.getText().length() > 0) {
            clicktab(lbltab3.getText());
            lbltab2.setText(lbltab3.getText());
            lbltab3.setText(lbltab4.getText());
            lbltab4.setText("");
            btntab4.setIcon(null);
            tab4.setBackground(new Color(240, 240, 240));
            btntab4.setBackground(new Color(240, 240, 240));
        } else if (lbltab3.getText().length() > 0) {
            clicktab(lbltab3.getText());
            lbltab2.setText(lbltab3.getText());
            lbltab3.setText("");
            btntab3.setIcon(null);
            tab3.setBackground(new Color(240, 240, 240));
            btntab3.setBackground(new Color(240, 240, 240));
        } else if (lbltab2.getText().length() > 0) {
            clicktab(lbltab1.getText());
            lbltab2.setText("");
            btntab2.setIcon(null);
            tab2.setBackground(new Color(240, 240, 240));
            btntab2.setBackground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_btntab2ActionPerformed

    private void btntab3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntab3ActionPerformed
        if (lbltab5.getText().length() > 0) {
            clicktab(lbltab4.getText());
            lbltab3.setText(lbltab4.getText());
            lbltab4.setText(lbltab5.getText());
            lbltab5.setText("");
            btntab5.setIcon(null);
            tab5.setBackground(new Color(240, 240, 240));
            btntab5.setBackground(new Color(240, 240, 240));
        } else if (lbltab4.getText().length() > 0) {
            clicktab(lbltab4.getText());
            lbltab3.setText(lbltab4.getText());
            lbltab4.setText("");
            btntab4.setIcon(null);
            tab4.setBackground(new Color(240, 240, 240));
            btntab4.setBackground(new Color(240, 240, 240));
        } else if (lbltab3.getText().length() > 0) {
            clicktab(lbltab2.getText());
            lbltab3.setText("");
            btntab3.setIcon(null);
            tab3.setBackground(new Color(240, 240, 240));
            btntab3.setBackground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_btntab3ActionPerformed

    private void btntab4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntab4ActionPerformed
        if (lbltab5.getText().length() > 0) {
            clicktab(lbltab5.getText());
            lbltab4.setText(lbltab5.getText());
            lbltab5.setText("");
            btntab5.setIcon(null);
            tab5.setBackground(new Color(240, 240, 240));
            btntab5.setBackground(new Color(240, 240, 240));
        } else if (lbltab4.getText().length() > 0) {
            clicktab(lbltab3.getText());
            lbltab4.setText("");
            btntab4.setIcon(null);
            tab4.setBackground(new Color(240, 240, 240));
            btntab4.setBackground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_btntab4ActionPerformed

    private void btntab5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntab5ActionPerformed
        clicktab(lbltab4.getText());
        lbltab5.setText("");
        btntab5.setIcon(null);
        tab5.setBackground(new Color(240, 240, 240));
        btntab5.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_btntab5ActionPerformed

    private void lbldiemdanhMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbldiemdanhMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lbldiemdanhMouseEntered

    private void lbldiemdanhMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbldiemdanhMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lbldiemdanhMouseExited

    private void lbldiemdanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbldiemdanhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbldiemdanhActionPerformed

    private void diemdanhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_diemdanhMouseClicked
        lane3.setBackground(new Color(119, 84, 248));
        lbldiemdanh.setBackground(new Color(237, 234, 242));
        diemdanh.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane2.setBackground(new Color(255, 255, 255));
        lbldiemmonhoc.setBackground(new Color(255, 255, 255));
        diemmonhoc.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lblhocba.setBackground(new Color(255, 255, 255));
        hocbahocsinh.setBackground(new Color(255, 255, 255));

        lane5.setBackground(new Color(255, 255, 255));
        lbldshocsinh.setBackground(new Color(255, 255, 255));
        dshocsinh.setBackground(new Color(255, 255, 255));

        lane6.setBackground(new Color(255, 255, 255));
        lblguiphanhoi.setBackground(new Color(255, 255, 255));
        guiphanhoi.setBackground(new Color(255, 255, 255));

        lane7.setBackground(new Color(255, 255, 255));
        lblketquacuoinam.setBackground(new Color(255, 255, 255));
        ketquacuoinam.setBackground(new Color(255, 255, 255));

        String tieude2 = "Điểm danh";
        checktab(tieude2);

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(qlydiemdanh);
        body.repaint();
        body.revalidate();
        this.loadCboHocsinh();
        this.loadDataDiemdanh();
    }//GEN-LAST:event_diemdanhMouseClicked

    private void lblguiphanhoiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblguiphanhoiMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblguiphanhoiMouseEntered

    private void lblguiphanhoiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblguiphanhoiMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblguiphanhoiMouseExited

    private void lblguiphanhoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblguiphanhoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblguiphanhoiActionPerformed

    private void guiphanhoiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guiphanhoiMouseClicked
        lane6.setBackground(new Color(119, 84, 248));
        lblguiphanhoi.setBackground(new Color(237, 234, 242));
        guiphanhoi.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane2.setBackground(new Color(255, 255, 255));
        lbldiemmonhoc.setBackground(new Color(255, 255, 255));
        diemmonhoc.setBackground(new Color(255, 255, 255));

        lane3.setBackground(new Color(255, 255, 255));
        lbldiemdanh.setBackground(new Color(255, 255, 255));
        diemdanh.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lblhocba.setBackground(new Color(255, 255, 255));
        hocbahocsinh.setBackground(new Color(255, 255, 255));

        lane5.setBackground(new Color(255, 255, 255));
        lbldshocsinh.setBackground(new Color(255, 255, 255));
        dshocsinh.setBackground(new Color(255, 255, 255));

        lane7.setBackground(new Color(255, 255, 255));
        lblketquacuoinam.setBackground(new Color(255, 255, 255));
        ketquacuoinam.setBackground(new Color(255, 255, 255));

        String tieude2 = "Gửi phản hồi";
        checktab(tieude2);

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(qlyguiphanhoi);
        body.repaint();
        body.revalidate();
    }//GEN-LAST:event_guiphanhoiMouseClicked

    private void tab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab1MouseClicked
        String tentab = lbltab1.getText();
        clicktab(tentab);
    }//GEN-LAST:event_tab1MouseClicked

    private void tab2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab2MouseClicked
        String tentab = lbltab2.getText();
        clicktab(tentab);
    }//GEN-LAST:event_tab2MouseClicked

    private void tab3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab3MouseClicked
        String tentab = lbltab3.getText();
        clicktab(tentab);
    }//GEN-LAST:event_tab3MouseClicked

    private void tab4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab4MouseClicked
        String tentab = lbltab4.getText();
        clicktab(tentab);
    }//GEN-LAST:event_tab4MouseClicked

    private void tab5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab5MouseClicked
        String tentab = lbltab5.getText();
        clicktab(tentab);
    }//GEN-LAST:event_tab5MouseClicked

    private void lblhocbaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblhocbaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblhocbaMouseEntered

    private void lblhocbaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblhocbaMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblhocbaMouseExited

    private void lblhocbaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblhocbaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblhocbaActionPerformed

    private void hocbahocsinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hocbahocsinhMouseClicked
        lane4.setBackground(new Color(119, 84, 248));
        lblhocba.setBackground(new Color(237, 234, 242));
        hocbahocsinh.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane2.setBackground(new Color(255, 255, 255));
        lbldiemmonhoc.setBackground(new Color(255, 255, 255));
        diemmonhoc.setBackground(new Color(255, 255, 255));

        lane3.setBackground(new Color(255, 255, 255));
        lbldiemdanh.setBackground(new Color(255, 255, 255));
        diemdanh.setBackground(new Color(255, 255, 255));

        lane5.setBackground(new Color(255, 255, 255));
        lbldshocsinh.setBackground(new Color(255, 255, 255));
        dshocsinh.setBackground(new Color(255, 255, 255));

        lane6.setBackground(new Color(255, 255, 255));
        lblguiphanhoi.setBackground(new Color(255, 255, 255));
        guiphanhoi.setBackground(new Color(255, 255, 255));

        lane7.setBackground(new Color(255, 255, 255));
        lblketquacuoinam.setBackground(new Color(255, 255, 255));
        ketquacuoinam.setBackground(new Color(255, 255, 255));

        String tieude2 = "Học bạ học sinh";
        checktab(tieude2);

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(qlyhocba);
        body.repaint();
        body.revalidate();


    }//GEN-LAST:event_hocbahocsinhMouseClicked

    private void lbldshocsinhMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbldshocsinhMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lbldshocsinhMouseEntered

    private void lbldshocsinhMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbldshocsinhMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lbldshocsinhMouseExited

    private void lbldshocsinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbldshocsinhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbldshocsinhActionPerformed

    private void dshocsinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dshocsinhMouseClicked
        lane5.setBackground(new Color(119, 84, 248));
        lbldshocsinh.setBackground(new Color(237, 234, 242));
        dshocsinh.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane2.setBackground(new Color(255, 255, 255));
        lbldiemmonhoc.setBackground(new Color(255, 255, 255));
        diemmonhoc.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lblhocba.setBackground(new Color(255, 255, 255));
        hocbahocsinh.setBackground(new Color(255, 255, 255));

        lane3.setBackground(new Color(255, 255, 255));
        lbldiemdanh.setBackground(new Color(255, 255, 255));
        diemdanh.setBackground(new Color(255, 255, 255));

        lane6.setBackground(new Color(255, 255, 255));
        lblguiphanhoi.setBackground(new Color(255, 255, 255));
        guiphanhoi.setBackground(new Color(255, 255, 255));

        lane7.setBackground(new Color(255, 255, 255));
        lblketquacuoinam.setBackground(new Color(255, 255, 255));
        ketquacuoinam.setBackground(new Color(255, 255, 255));

        String tieude2 = "Danh sách học sinh";
        checktab(tieude2);

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(qlyhocsinh);
        body.repaint();
        body.revalidate();
    }//GEN-LAST:event_dshocsinhMouseClicked

    private void tbl_diemdanhMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_diemdanhMouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_diemdanhMouseDragged

    private void tbl_diemdanhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_diemdanhMouseClicked

    }//GEN-LAST:event_tbl_diemdanhMouseClicked

    private void tbl_diemdanhMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_diemdanhMouseReleased

    }//GEN-LAST:event_tbl_diemdanhMouseReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.themdiemdanh();
        this.loadDataDiemdanh();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        lbl_ngaydiemdanh.setText(java.time.LocalDate.now().toString());

    }//GEN-LAST:event_jButton5ActionPerformed

    private void tblGridView1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView1MouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridView1MouseDragged

    private void tblGridView1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView1MouseClicked
        DefaultTableModel model = (DefaultTableModel) tblGridView1.getModel();
        int i = tblGridView1.getSelectedRow();
        String m = (tblGridView1.getValueAt(i, 0).toString());
        ResultSet rs = hsDAO.selectWithMaHS(m);
        try {
            if (rs.next()) {
//                lbl_Lop1.setText(rs.getString("lh.tenlop"));
                lbl_mahs.setText(rs.getString("mahocsinh"));
                lbl_HoTen.setText(rs.getString("hoten"));

                if (rs.getString("gioitinh").equals("1")) {
                    lbl_GioiTinh.setText("Nam");
                } else {
                    lbl_GioiTinh.setText("Nữ");
                }
                lbl_Ngaysinh.setText(rs.getString("ngaysinh"));
                lbl_DanToc.setText(rs.getString("dantoc"));
                lbl_TonGiao.setText(rs.getString("tongiao"));
                lbl_NoiSinh.setText(rs.getString("noisinh"));
                lbl_CMND.setText(rs.getString("cmnd"));
//                lbl_Lop2.setText(rs.getString("lh.tenlop"));
                lbl_NgayVaoDoan.setText(rs.getString("ngayvaodoan"));
                lbl_SDT.setText(rs.getString("dienthoai"));

                if (rs.getString("trangthai").equals("1")) {
                    lbl_TrangThai.setText("Đi học");
                } else {
                    lbl_TrangThai.setText("Nghỉ học");
                }
                lbl_DiaCHi.setText(rs.getString("diachi"));
                lbl_HoTenBo.setText(rs.getString("hoten_bo"));
                lbl_SDTBo.setText(rs.getString("dienthoai_bo"));
                lbl_DVCTBo.setText(rs.getString("dv_cong_tac_bo"));
                lbl_HotenMe.setText(rs.getString("hoten_me"));
                lbl_SDTMe.setText(rs.getString("dienthoai_me"));
                lbl_DVCTMe.setText(rs.getString("dv_cong_tacme"));
                lbl_NguoiDamHo.setText(rs.getString("nguoidamho"));
//                lbl_path.setText(rs.getString("hs.anh"));
                if (image != null) {
                    ImageIcon icon = new ImageIcon(rs.getString("hs.anh"));
                    Image img = icon.getImage().getScaledInstance(110, 121, Image.SCALE_SMOOTH);
                    image.setIcon(new ImageIcon(img));

                } else {
                    image.setIcon(null);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jTabbedPane1.setSelectedIndex(0);


    }//GEN-LAST:event_tblGridView1MouseClicked

    private void tblGridView1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView1MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridView1MouseReleased

    private void tblGridView_DiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView_DiemMouseClicked
        btnchinhsua.setEnabled(true);
        int i = tblGridView_Diem.getSelectedRow();
        if (cbo_cacchamdiem.getSelectedItem().toString() == "Chấm điểm") {
            lbl_mhs3.setText(tblGridView_Diem.getValueAt(i, 0).toString());
            lbl_hvt3.setText(tblGridView_Diem.getValueAt(i, 1).toString());
            lbl_gt3.setText(tblGridView_Diem.getValueAt(i, 2).toString());
            txt_diemMieng1.setText(tblGridView_Diem.getValueAt(i, 3).toString());
            txt_diemMieng2.setText(tblGridView_Diem.getValueAt(i, 4).toString());
            txt_diemMieng3.setText(tblGridView_Diem.getValueAt(i, 5).toString());
            txt_15p1.setText(tblGridView_Diem.getValueAt(i, 6).toString());
            txt_15p2.setText(tblGridView_Diem.getValueAt(i, 7).toString());
            txt_15p3.setText(tblGridView_Diem.getValueAt(i, 8).toString());
            txt_45p1.setText(tblGridView_Diem.getValueAt(i, 9).toString());
            txt_45p2.setText(tblGridView_Diem.getValueAt(i, 10).toString());
            txt_hk.setText(tblGridView_Diem.getValueAt(i, 11).toString());
            lbl_diemTBM.setText(tblGridView_Diem.getValueAt(i, 12).toString());
        } else {
            lbl_mhs3.setText(tblGridView_Diem.getValueAt(i, 0).toString());
            lbl_hvt3.setText(tblGridView_Diem.getValueAt(i, 1).toString());
            lbl_gt3.setText(tblGridView_Diem.getValueAt(i, 2).toString());
            cbo_diemTX1.setSelectedItem(tblGridView_Diem.getValueAt(i, 3).toString() == "1" ? "Đạt" : "Chưa Đạt");
            cbo_diemTX2.setSelectedItem(tblGridView_Diem.getValueAt(i, 4).toString() == "1" ? "Đạt" : "Chưa Đạt");
            cbo_diemTX3.setSelectedItem(tblGridView_Diem.getValueAt(i, 5).toString() == "1" ? "Đạt" : "Chưa Đạt");
            cbo_diemTX4.setSelectedItem(tblGridView_Diem.getValueAt(i, 6).toString() == "1" ? "Đạt" : "Chưa Đạt");
            cbo_diemTX5.setSelectedItem(tblGridView_Diem.getValueAt(i, 7).toString() == "1" ? "Đạt" : "Chưa Đạt");
            cbo_diemTX6.setSelectedItem(tblGridView_Diem.getValueAt(i, 8).toString() == "1" ? "Đạt" : "Chưa Đạt");
            cbo_diemTX7.setSelectedItem(tblGridView_Diem.getValueAt(i, 9).toString() == "1" ? "Đạt" : "Chưa Đạt");
            cbo_diemTX8.setSelectedItem(tblGridView_Diem.getValueAt(i, 10).toString() == "1" ? "Đạt" : "Chưa Đạt");
            cbo_diemTX9.setSelectedItem(tblGridView_Diem.getValueAt(i, 11).toString() == "1" ? "Đạt" : "Chưa Đạt");
            lbl_diemTBMDG.setText(tblGridView_Diem.getValueAt(i, 11).toString() == "1" ? "Đạt" : "Chưa Đạt");
        }
    }//GEN-LAST:event_tblGridView_DiemMouseClicked

    private void tblGridView_DiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGridView_DiemKeyPressed

    }//GEN-LAST:event_tblGridView_DiemKeyPressed

    private void tblGridView_DiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGridView_DiemKeyReleased

    }//GEN-LAST:event_tblGridView_DiemKeyReleased

    private void btnchinhsuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnchinhsuaActionPerformed

        String ki = cbo_cacchamdiem.getSelectedItem().toString();
        if (ki.equals("Chấm điểm")) {
            bodydiem.removeAll();
            bodydiem.repaint();
            bodydiem.revalidate();
            bodydiem.add(monchamdiem);
            bodydiem.repaint();
            bodydiem.revalidate();
            btnchinhsua.setVisible(false);
        } else {
            bodydiem.removeAll();
            bodydiem.repaint();
            bodydiem.revalidate();
            bodydiem.add(mondanhgia);
            bodydiem.repaint();
            bodydiem.revalidate();
            btnchinhsua.setVisible(false);
        }


    }//GEN-LAST:event_btnchinhsuaActionPerformed

    private void txt_diemMieng1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_diemMieng1MouseClicked

    }//GEN-LAST:event_txt_diemMieng1MouseClicked

    private void txt_diemMieng1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_diemMieng1ActionPerformed

    }//GEN-LAST:event_txt_diemMieng1ActionPerformed

    private void txt_diemMieng1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_diemMieng1KeyPressed

    }//GEN-LAST:event_txt_diemMieng1KeyPressed

    private void txt_diemMieng2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_diemMieng2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_diemMieng2ActionPerformed

    private void txt_diemMieng2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_diemMieng2KeyPressed

    }//GEN-LAST:event_txt_diemMieng2KeyPressed

    private void txt_diemMieng3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_diemMieng3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_diemMieng3ActionPerformed

    private void txt_diemMieng3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_diemMieng3KeyPressed

    }//GEN-LAST:event_txt_diemMieng3KeyPressed

    private void txt_15p1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_15p1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_15p1ActionPerformed

    private void txt_15p1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_15p1KeyPressed

    }//GEN-LAST:event_txt_15p1KeyPressed

    private void txt_15p2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_15p2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_15p2ActionPerformed

    private void txt_15p2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_15p2KeyPressed

    }//GEN-LAST:event_txt_15p2KeyPressed

    private void txt_15p3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_15p3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_15p3ActionPerformed

    private void txt_15p3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_15p3KeyPressed

    }//GEN-LAST:event_txt_15p3KeyPressed

    private void txt_45p1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_45p1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_45p1ActionPerformed

    private void txt_45p1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_45p1KeyPressed

    }//GEN-LAST:event_txt_45p1KeyPressed

    private void txt_45p2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_45p2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_45p2ActionPerformed

    private void txt_45p2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_45p2KeyPressed

    }//GEN-LAST:event_txt_45p2KeyPressed

    private void txt_45p2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_45p2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_45p2KeyReleased

    private void txt_hkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_hkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_hkActionPerformed

    private void txt_hkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_hkKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_hkKeyPressed

    private void btn_capNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhatActionPerformed
        String tenLop = (String) cbo_Lop_Diem.getSelectedItem();
        String tenMon = (String) cbo_Mon_Diem.getSelectedItem();
        boolean ki = cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false;
        try {
            if (checkDiem() == true) {
                this.tinhDiemTBM();
                this.UpdateGrade();
                this.LoadDataToTableGrade(tenLop, tenMon, ki);
                DialogHelper.alert(this, "thành công");
                bodydiem.removeAll();
                bodydiem.repaint();
                bodydiem.revalidate();
                bodydiem.add(bangdiem);
                bodydiem.repaint();
                bodydiem.revalidate();
                btnchinhsua.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_capNhatActionPerformed

    private void btn_capNhat1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat1ActionPerformed
        bodydiem.removeAll();
        bodydiem.repaint();
        bodydiem.revalidate();
        bodydiem.add(bangdiem);
        bodydiem.repaint();
        bodydiem.revalidate();
        btnchinhsua.setVisible(true);
    }//GEN-LAST:event_btn_capNhat1ActionPerformed

    private void btn_capNhat2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat2ActionPerformed
        String tenLop = (String) cbo_Lop_Diem.getSelectedItem();
        String tenMon = (String) cbo_Mon_Diem.getSelectedItem();
        boolean ki = cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false;
        try {
            this.tinhDiemDanhGia();
            this.UpdateGradeDG();
            this.LoadDataToTableGrade(tenLop, tenMon, ki);
            DialogHelper.alert(this, "thành công");
            bodydiem.removeAll();
            bodydiem.repaint();
            bodydiem.revalidate();
            bodydiem.add(bangdiem);
            bodydiem.repaint();
            bodydiem.revalidate();
            btnchinhsua.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_capNhat2ActionPerformed

    private void btn_capNhat3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat3ActionPerformed
        bodydiem.removeAll();
        bodydiem.repaint();
        bodydiem.revalidate();
        bodydiem.add(bangdiem);
        bodydiem.repaint();
        bodydiem.revalidate();
        btnchinhsua.setVisible(true);
    }//GEN-LAST:event_btn_capNhat3ActionPerformed

    private void cbo_diemTX2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_diemTX2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_diemTX2ActionPerformed

    private void cbo_diemTX5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_diemTX5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_diemTX5ActionPerformed

    private void cbo_diemTX8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_diemTX8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_diemTX8ActionPerformed

    private void tblGridView2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView2MouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridView2MouseDragged

    private void tblGridView2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView2MouseClicked

    }//GEN-LAST:event_tblGridView2MouseClicked

    private void tblGridView2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView2MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridView2MouseReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        String str = JOptionPane.showInputDialog("Nhập mật khẩu email");
        System.out.println(str);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void lblketquacuoinamMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblketquacuoinamMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblketquacuoinamMouseEntered

    private void lblketquacuoinamMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblketquacuoinamMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblketquacuoinamMouseExited

    private void lblketquacuoinamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblketquacuoinamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblketquacuoinamActionPerformed

    private void ketquacuoinamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ketquacuoinamMouseClicked
        lane7.setBackground(new Color(119, 84, 248));
        lblketquacuoinam.setBackground(new Color(237, 234, 242));
        ketquacuoinam.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane2.setBackground(new Color(255, 255, 255));
        lbldiemmonhoc.setBackground(new Color(255, 255, 255));
        diemmonhoc.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lblhocba.setBackground(new Color(255, 255, 255));
        hocbahocsinh.setBackground(new Color(255, 255, 255));

        lane3.setBackground(new Color(255, 255, 255));
        lbldiemdanh.setBackground(new Color(255, 255, 255));
        diemdanh.setBackground(new Color(255, 255, 255));

        lane6.setBackground(new Color(255, 255, 255));
        lblguiphanhoi.setBackground(new Color(255, 255, 255));
        guiphanhoi.setBackground(new Color(255, 255, 255));

        lane5.setBackground(new Color(255, 255, 255));
        lbldshocsinh.setBackground(new Color(255, 255, 255));
        dshocsinh.setBackground(new Color(255, 255, 255));

        String tieude2 = "Kết quả cuối năm";
        checktab(tieude2);

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(kquacuoinam);
        body.repaint();
        body.revalidate();
        this.loadkqcuoinam();
        this.resetketquacuoinam();
    }//GEN-LAST:event_ketquacuoinamMouseClicked

    private void lbltrangchuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbltrangchuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbltrangchuMouseClicked

    private void cbbNamHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbNamHocActionPerformed
        this.loadCBB();
        this.sisoTong();
        this.sisoTongNam();
        this.sisoNu();

    }//GEN-LAST:event_cbbNamHocActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        DefaultTableModel model = (DefaultTableModel) tblGridView1.getModel();
        String ft = txtTimKiem.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
        tblGridView1.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(ft));
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void Xuat_DiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Xuat_DiemActionPerformed
        try {
            jFileChooser1.setSelectedFile(new File("C:\\Bảng điểm Lớp " + cbo_Lop_Diem.getSelectedItem().toString() + cbo_hocKi.getSelectedItem().toString() + " Môn " + cbo_Mon_Diem.getSelectedItem().toString() + ".csv"));
            jFileChooser1.showSaveDialog(null);
            File file = jFileChooser1.getSelectedFile();
            if (file.exists()) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(this, "Bạn muốn ghi đè lên File đã có", "Hệ thống quản lý đào tạo", dialogButton);
                if (dialogResult == 0) {
                    System.out.println("starting write user.csv file: " + file.getPath());
                    fv.writeDiemCsv(file.getPath(), (String) cbo_Mon_Diem.getSelectedItem(), (String) cbo_Lop_Diem.getSelectedItem(), (String) cbo_hocKi.getSelectedItem());
                    DialogHelper.alert(this, "Xuất Excel thành công");
                } else {
                    System.out.println("No Option");
                }
            } else {
                System.out.println("starting write file: " + file.getPath());
                fv.writeDiemCsv(file.getPath(), (String) cbo_Mon_Diem.getSelectedItem(), (String) cbo_Lop_Diem.getSelectedItem(), (String) cbo_hocKi.getSelectedItem());
                DialogHelper.alert(this, "Xuất Excel thành công");
            }
        } catch (Exception ex) {
            DialogHelper.alert(this, "Xuất Excel không thành công");
        }
    }//GEN-LAST:event_Xuat_DiemActionPerformed

    private void Nhap_DiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Nhap_DiemActionPerformed
        try {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("File csv", "csv");
            jFileChooser1.setFileFilter(filter);
            jFileChooser1.showSaveDialog(null);
            File file = jFileChooser1.getSelectedFile();
            if (file.exists()) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(this, "Bạn muốn nhập điểm vào Lớp: " + cbo_Lop_Diem.getSelectedItem().toString() + " || Môn: " + cbo_Mon_Diem.getSelectedItem().toString() + " || Kì: " + (String) cbo_hocKi.getSelectedItem() + " || Năm: " + (String) cbbNamHoc.getSelectedItem(), "Hệ thống quản lý đào tạo", dialogButton);
                if (dialogResult == 0) {
                    System.out.println("starting write user.csv file: " + file.getPath());
                    fv.readDiemCsv(file.getPath(), (String) cbo_Mon_Diem.getSelectedItem(), (String) cbo_Lop_Diem.getSelectedItem(), (String) cbo_hocKi.getSelectedItem());
                    DialogHelper.alert(this, "Nhập Excel thành công");
                } else {
                    System.out.println("No Option");
                }
            } else {
                System.out.println("starting write file: " + file.getPath());
                fv.readDiemCsv(file.getPath(), (String) cbo_Mon_Diem.getSelectedItem(), (String) cbo_Lop_Diem.getSelectedItem(), (String) cbo_hocKi.getSelectedItem());
                DialogHelper.alert(this, "Nhập Excel thành công");
            }
        } catch (Exception ex) {
            DialogHelper.alert(this, "Nhập Excel không thành công");
            ex.printStackTrace();
        }
    }//GEN-LAST:event_Nhap_DiemActionPerformed

    private void Tai_Form_DiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Tai_Form_DiemActionPerformed
        try {
            JFileChooser jFileChooser1 = new JFileChooser();
            jFileChooser1.setSelectedFile(new File("C:\\Form Bảng điểm.csv"));
            jFileChooser1.showSaveDialog(null);
            File file = jFileChooser1.getSelectedFile();
            if (file.exists()) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(this, "Bạn muốn ghi đè lên File đã có", "Hệ thống quản lý đào tạo", dialogButton);
                if (dialogResult == 0) {
                    System.out.println("starting write user.csv file: " + file.getPath());
                    fv.writeFormDMHCsv(file.getPath(), cbo_Lop_Diem.getSelectedItem().toString());
                    DialogHelper.alert(this, "Xuất Excel thành công");
                } else {
                    System.out.println("No Option");
                }
            } else {
                System.out.println("starting write form file: " + file.getPath());
                fv.writeFormDMHCsv(file.getPath(), cbo_Lop_Diem.getSelectedItem().toString());
                DialogHelper.alert(this, "Xuất Excel thành công");
            }
        } catch (Exception ex) {
            DialogHelper.alert(this, "Xuất Excel không thành công");
        }
    }//GEN-LAST:event_Tai_Form_DiemActionPerformed

    private void txt_searchdiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchdiemKeyReleased
        DefaultTableModel model = (DefaultTableModel) tblGridView_Diem.getModel();
        String ft = txt_searchdiem.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
        tblGridView_Diem.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(ft));
    }//GEN-LAST:event_txt_searchdiemKeyReleased

    private void cbo_LoCN_KQCNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_LoCN_KQCNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_LoCN_KQCNActionPerformed

    private void Xuat_KQCNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Xuat_KQCNActionPerformed
        try {
            jFileChooser1.setSelectedFile(new File("C:\\Kết quả cuối năm lớp " + cbo_LoCN_KQCN.getSelectedItem().toString() + ".csv"));
            jFileChooser1.showSaveDialog(null);
            File file = jFileChooser1.getSelectedFile();
            if (file.exists()) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(this, "Bạn muốn ghi đè lên File đã có", "Hệ thống quản lý đào tạo", dialogButton);
                if (dialogResult == 0) {
                    System.out.println("starting write user.csv file: " + file.getPath());
                    fv.writeKQCNCsv(cbo_LoCN_KQCN.getSelectedItem().toString(), file.getPath());
                    DialogHelper.alert(this, "Xuất Excel thành công");
                } else {
                    System.out.println("No Option");
                }
            } else {
                System.out.println("starting write file: " + file.getPath());
                fv.writeKQCNCsv(cbo_LoCN_KQCN.getSelectedItem().toString(), file.getPath());
                DialogHelper.alert(this, "Xuất Excel thành công");
            }
        } catch (Exception ex) {
            DialogHelper.alert(this, "Xuất Excel không thành công");
        }
    }//GEN-LAST:event_Xuat_KQCNActionPerformed

    private void tblGridView_KetQuaCuoiNamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView_KetQuaCuoiNamMouseClicked
        int i = tblGridView_KetQuaCuoiNam.getSelectedRow();
        lbl_mhs.setText(tblGridView_KetQuaCuoiNam.getValueAt(i, 0).toString());
        lbl_hvt.setText(tblGridView_KetQuaCuoiNam.getValueAt(i, 1).toString());
        lbl_ns.setText(tblGridView_KetQuaCuoiNam.getValueAt(i, 2).toString());
        lbl_KQTB1.setText(tblGridView_KetQuaCuoiNam.getValueAt(i, 3).toString());
        lbl_KQTB2.setText(tblGridView_KetQuaCuoiNam.getValueAt(i, 4).toString());
        lbl_HanhKiem1.setText(tblGridView_KetQuaCuoiNam.getValueAt(i, 9).toString());
        lbl_HanhKiem2.setText(tblGridView_KetQuaCuoiNam.getValueAt(i, 9).toString());
        lbl_HanhKiemFull.setText(tblGridView_KetQuaCuoiNam.getValueAt(i, 9).toString());
        lbl_HocLuc3.setText(tblGridView_KetQuaCuoiNam.getValueAt(i, 6).toString());
        if (Float.parseFloat(lbl_KQTB1.getText()) >= 8) {
            lbl_HocLuc1.setText("Giỏi");
        } else if (Float.parseFloat(lbl_KQTB1.getText()) >= 7 && Float.parseFloat(lbl_KQTB1.getText()) < 8) {
            lbl_HocLuc1.setText("Khá");
        } else if (Float.parseFloat(lbl_KQTB1.getText()) >= 5 && Float.parseFloat(lbl_KQTB1.getText()) < 7) {
            lbl_HocLuc1.setText("Trung Bình");
        } else if (Float.parseFloat(lbl_KQTB1.getText()) < 5) {
            lbl_HocLuc1.setText("Yếu");
        }
        if (Float.parseFloat(lbl_KQTB2.getText()) >= 8) {
            lbl_HocLuc2.setText("Giỏi");
        } else if (Float.parseFloat(lbl_KQTB2.getText()) >= 7 && Float.parseFloat(lbl_KQTB2.getText()) < 8) {
            lbl_HocLuc2.setText("Khá");
        } else if (Float.parseFloat(lbl_KQTB2.getText()) >= 5 && Float.parseFloat(lbl_KQTB2.getText()) < 7) {
            lbl_HocLuc2.setText("Trung Bình");
        } else if (Float.parseFloat(lbl_KQTB2.getText()) < 5) {
            lbl_HocLuc2.setText("Yếu");
        }

        if (lbl_HocLuc1.getText() == "Giỏi" && lbl_HanhKiem1.getText() == "Tốt") {
            lbl_DanhHieu4.setText("Học sinh giỏi");
        } else if (lbl_HocLuc1.getText() == "Khá" && lbl_HanhKiem1.getText() == "Tốt") {
            lbl_DanhHieu4.setText("Học sinh tiên tiến");
        } else {
            lbl_DanhHieu4.setText("Không có");
        }
        if (lbl_HocLuc2.getText() == "Giỏi" && lbl_HanhKiem2.getText() == "Tốt") {
            lbl_DanhHieu5.setText("Học sinh giỏi");
        } else if (lbl_HocLuc2.getText() == "Khá" && lbl_HanhKiem2.getText() == "Tốt") {
            lbl_DanhHieu5.setText("Học sinh tiên tiến");
        } else {
            lbl_DanhHieu5.setText("Không có");
        }
        lbl_DanhHieu6.setText(tblGridView_KetQuaCuoiNam.getValueAt(i, 10).toString());
        lbl_VangCP.setText(tblGridView_KetQuaCuoiNam.getValueAt(i, 7).toString());
        lbl_VangCP1.setText(tblGridView_KetQuaCuoiNam.getValueAt(i, 8).toString());

    }//GEN-LAST:event_tblGridView_KetQuaCuoiNamMouseClicked

    private void txt_timkiemkqcuoinamKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_timkiemkqcuoinamKeyReleased
        DefaultTableModel model = (DefaultTableModel) tblGridView_KetQuaCuoiNam.getModel();
        String ft = txt_timkiemkqcuoinam.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
        tblGridView_KetQuaCuoiNam.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(ft));
    }//GEN-LAST:event_txt_timkiemkqcuoinamKeyReleased

    private void btn_timkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timkiemActionPerformed
        this.timkiemlan1();
    }//GEN-LAST:event_btn_timkiemActionPerformed

    private void btn_lammoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lammoiActionPerformed
        this.resetlan1();
    }//GEN-LAST:event_btn_lammoiActionPerformed

    private void btn_capNhat9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat9ActionPerformed

    }//GEN-LAST:event_btn_capNhat9ActionPerformed

    private void txt_searchdiem1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchdiem1KeyReleased

    }//GEN-LAST:event_txt_searchdiem1KeyReleased

    private void cbo_Mon_Diem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_Mon_Diem1ActionPerformed

    }//GEN-LAST:event_cbo_Mon_Diem1ActionPerformed

    private void cbo_Mon_Diem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_Mon_Diem2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_Mon_Diem2ActionPerformed

    private void cbo_Mon_Diem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_Mon_Diem3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_Mon_Diem3ActionPerformed

    private void btn_capNhat10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_capNhat10ActionPerformed

    private void tblGridView3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView3MouseClicked

    }//GEN-LAST:event_tblGridView3MouseClicked

    private void tblGridView3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGridView3KeyPressed

    }//GEN-LAST:event_tblGridView3KeyPressed

    private void tblGridView3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGridView3KeyReleased

    }//GEN-LAST:event_tblGridView3KeyReleased

    private void cbo_Mon_DiemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_Mon_DiemItemStateChanged
//        String tenMon = cbo_Mon_Diem.getSelectedItem().toString();
//        ResultSet rs = mhDAO.select3(tenMon);
//        LoadLopDay();
//        try {
//            while (rs.next()) {
//                boolean DG = rs.getBoolean("hinhthucdanhgia");
//                cbo_cacchamdiem.setSelectedItem(DG == true ? "Chấm điểm" : "Đánh giá");
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(TrangChuGV.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_cbo_Mon_DiemItemStateChanged

    private void cbo_hocKiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_hocKiActionPerformed
        this.loadCBB();
        this.sisoTong();
        this.sisoTongNam();
        this.sisoNu();
        this.LoadLopDay();
        this.LoadMonDay();
        String tenLop = (String) cbo_Lop_Diem.getSelectedItem();
        String tenMon = (String) cbo_Mon_Diem.getSelectedItem();
        boolean ki = cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false;
        this.LoadDataToTableGrade(tenLop, tenMon, ki);
        if (DialogHelper.confirm(this, "bạn có muốn tạo mới danh sách") == true) {
            this.LoadNewDataToTableGrade(tenLop);
            this.InsertDataFirst();
            DialogHelper.alert(this, "Thành Công");
        } else {
            DialogHelper.alert(this, "vui lòng kiểm tra lại");
        }
    }//GEN-LAST:event_cbo_hocKiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TrangChuGV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TrangChuGV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TrangChuGV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrangChuGV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TrangChuGV().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Nhap_Diem;
    private javax.swing.JButton Tai_Form_Diem;
    private javax.swing.JButton Xuat_Diem;
    private javax.swing.JButton Xuat_KQCN;
    private javax.swing.JScrollPane bangdiem;
    private javax.swing.JScrollPane banglop;
    private javax.swing.JPanel body;
    private javax.swing.JPanel bodydiem;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnMinimize;
    private javax.swing.JButton btnMinimize4;
    private javax.swing.JButton btn_capNhat;
    private javax.swing.JButton btn_capNhat1;
    private javax.swing.JButton btn_capNhat10;
    private javax.swing.JButton btn_capNhat2;
    private javax.swing.JButton btn_capNhat3;
    private javax.swing.JButton btn_capNhat9;
    private javax.swing.JButton btn_lammoi;
    private javax.swing.JButton btn_timkiem;
    private javax.swing.JButton btnchinhsua;
    private javax.swing.JButton btntab1;
    private javax.swing.JButton btntab2;
    private javax.swing.JButton btntab3;
    private javax.swing.JButton btntab4;
    private javax.swing.JButton btntab5;
    private javax.swing.JComboBox<String> cbbNamHoc;
    private javax.swing.JComboBox<String> cbo_LoCN_KQCN;
    private javax.swing.JComboBox<String> cbo_Lop_Diem;
    private javax.swing.JComboBox<String> cbo_Mon_Diem;
    private javax.swing.JComboBox<String> cbo_Mon_Diem1;
    private javax.swing.JComboBox<String> cbo_Mon_Diem2;
    private javax.swing.JComboBox<String> cbo_Mon_Diem3;
    private javax.swing.JComboBox<String> cbo_cacchamdiem;
    private javax.swing.JComboBox<String> cbo_diemTX1;
    private javax.swing.JComboBox<String> cbo_diemTX2;
    private javax.swing.JComboBox<String> cbo_diemTX3;
    private javax.swing.JComboBox<String> cbo_diemTX4;
    private javax.swing.JComboBox<String> cbo_diemTX5;
    private javax.swing.JComboBox<String> cbo_diemTX6;
    private javax.swing.JComboBox<String> cbo_diemTX7;
    private javax.swing.JComboBox<String> cbo_diemTX8;
    private javax.swing.JComboBox<String> cbo_diemTX9;
    private javax.swing.JComboBox<String> cbo_hocKi;
    private javax.swing.JComboBox<String> cbo_mahsdiemdanh;
    private javax.swing.JComboBox<String> cbo_trangthaidiemdanh;
    private javax.swing.JPanel diemdanh;
    private javax.swing.JPanel diemmonhoc;
    private javax.swing.JPanel dshocsinh;
    private javax.swing.JPanel guiphanhoi;
    private javax.swing.JPanel hocbahocsinh;
    private javax.swing.JLabel image;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JPanel ketquacuoinam;
    private javax.swing.JPanel kquacuoinam;
    private javax.swing.JLabel lane1;
    private javax.swing.JLabel lane2;
    private javax.swing.JLabel lane3;
    private javax.swing.JLabel lane4;
    private javax.swing.JLabel lane5;
    private javax.swing.JLabel lane6;
    private javax.swing.JLabel lane7;
    private javax.swing.JLabel lbl_CMND;
    private javax.swing.JTextArea lbl_DVCTBo;
    private javax.swing.JTextArea lbl_DVCTMe;
    private javax.swing.JLabel lbl_DanToc;
    private javax.swing.JLabel lbl_DanhHieu4;
    private javax.swing.JLabel lbl_DanhHieu5;
    private javax.swing.JLabel lbl_DanhHieu6;
    private javax.swing.JTextArea lbl_DiaCHi;
    private javax.swing.JLabel lbl_GioiTinh;
    private javax.swing.JLabel lbl_HanhKiem1;
    private javax.swing.JLabel lbl_HanhKiem2;
    private javax.swing.JLabel lbl_HanhKiemFull;
    private javax.swing.JLabel lbl_HoTen;
    private javax.swing.JLabel lbl_HoTenBo;
    private javax.swing.JLabel lbl_HocLuc1;
    private javax.swing.JLabel lbl_HocLuc2;
    private javax.swing.JLabel lbl_HocLuc3;
    private javax.swing.JLabel lbl_HotenMe;
    private javax.swing.JLabel lbl_KQTB1;
    private javax.swing.JLabel lbl_KQTB2;
    private javax.swing.JLabel lbl_Lop1;
    private javax.swing.JLabel lbl_Lop2;
    private javax.swing.JLabel lbl_NgayVaoDoan;
    private javax.swing.JLabel lbl_Ngaysinh;
    private javax.swing.JTextArea lbl_NguoiDamHo;
    private javax.swing.JLabel lbl_NoiSinh;
    private javax.swing.JLabel lbl_SDT;
    private javax.swing.JLabel lbl_SDTBo;
    private javax.swing.JLabel lbl_SDTMe;
    private javax.swing.JLabel lbl_SiSo;
    private javax.swing.JLabel lbl_TonGiao;
    private javax.swing.JLabel lbl_TrangThai;
    private javax.swing.JLabel lbl_VangCP;
    private javax.swing.JLabel lbl_VangCP1;
    private javax.swing.JLabel lbl_diemTBM;
    private javax.swing.JLabel lbl_diemTBMDG;
    private javax.swing.JLabel lbl_giaovien;
    private javax.swing.JLabel lbl_gt3;
    private javax.swing.JLabel lbl_gt4;
    private javax.swing.JLabel lbl_hvt;
    private javax.swing.JLabel lbl_hvt3;
    private javax.swing.JLabel lbl_hvt4;
    private javax.swing.JLabel lbl_hvt5;
    private javax.swing.JLabel lbl_magiaovien;
    private javax.swing.JLabel lbl_mahs;
    private javax.swing.JLabel lbl_mhs;
    private javax.swing.JLabel lbl_mhs3;
    private javax.swing.JLabel lbl_mhs4;
    private javax.swing.JLabel lbl_mhs5;
    private javax.swing.JLabel lbl_ngaydiemdanh;
    private javax.swing.JLabel lbl_ns;
    private javax.swing.JLabel lbl_path;
    private javax.swing.JLabel lbl_tenLopCN;
    private javax.swing.JButton lbldiemdanh;
    private javax.swing.JButton lbldiemmonhoc;
    private javax.swing.JButton lbldshocsinh;
    private javax.swing.JButton lblguiphanhoi;
    private javax.swing.JButton lblhocba;
    private javax.swing.JButton lblketquacuoinam;
    private javax.swing.JLabel lbltab1;
    private javax.swing.JLabel lbltab2;
    private javax.swing.JLabel lbltab3;
    private javax.swing.JLabel lbltab4;
    private javax.swing.JLabel lbltab5;
    private javax.swing.JButton lbltrangchu;
    private javax.swing.JPanel maintrangchu;
    private javax.swing.JPanel monchamdiem;
    private javax.swing.JPanel mondanhgia;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JLabel qlnamhoc;
    private javax.swing.JLabel qlnamhoc1;
    private javax.swing.JLabel qlnamhoc2;
    private javax.swing.JPanel qlydiem;
    private javax.swing.JPanel qlydiemdanh;
    private javax.swing.JPanel qlyguiphanhoi;
    private javax.swing.JPanel qlyhocba;
    private javax.swing.JPanel qlyhocsinh;
    private javax.swing.JPanel tab1;
    private javax.swing.JPanel tab2;
    private javax.swing.JPanel tab3;
    private javax.swing.JPanel tab4;
    private javax.swing.JPanel tab5;
    private javax.swing.JPanel tanktab;
    private javax.swing.JTable tblGridView1;
    private javax.swing.JTable tblGridView2;
    private javax.swing.JTable tblGridView3;
    private javax.swing.JTable tblGridView_Diem;
    private javax.swing.JTable tblGridView_KetQuaCuoiNam;
    private javax.swing.JTable tbl_diemdanh;
    private javax.swing.JPanel trangchinh;
    private javax.swing.JPanel trangchu;
    private javax.swing.JLabel txtSS;
    private javax.swing.JLabel txtSSNam;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txt_15p1;
    private javax.swing.JTextField txt_15p2;
    private javax.swing.JTextField txt_15p3;
    private javax.swing.JTextField txt_45p1;
    private javax.swing.JTextField txt_45p2;
    private javax.swing.JTextField txt_diemMieng1;
    private javax.swing.JTextField txt_diemMieng2;
    private javax.swing.JTextField txt_diemMieng3;
    private javax.swing.JTextField txt_hk;
    private javax.swing.JTextField txt_searchdiem;
    private javax.swing.JTextField txt_searchdiem1;
    private javax.swing.JTextField txt_timkiemkqcuoinam;
    private javax.swing.JLabel txtssNu;
    // End of variables declaration//GEN-END:variables

    private boolean checkDiem() {
        try {
            if (Float.parseFloat(txt_diemMieng1.getText().trim()) < 0 || Float.parseFloat(txt_diemMieng1.getText().trim()) > 10) {
                txt_diemMieng1.requestFocus();
                DialogHelper.alert(this, "điểm không hợp lệ, điểm nên trong khoảng 0 đến 10.");
                return false;
            } else if (Float.parseFloat(txt_diemMieng2.getText().trim()) < 0 || Float.parseFloat(txt_diemMieng2.getText().trim()) > 10) {
                txt_diemMieng2.requestFocus();
                DialogHelper.alert(this, "điểm không hợp lệ, điểm nên trong khoảng 0 đến 10.");
                return false;
            } else if (Float.parseFloat(txt_diemMieng3.getText().trim()) < 0 || Float.parseFloat(txt_diemMieng3.getText().trim()) > 10) {
                txt_diemMieng3.requestFocus();
                DialogHelper.alert(this, "điểm không hợp lệ, điểm nên trong khoảng 0 đến 10.");
                return false;
            } else if (Float.parseFloat(txt_15p1.getText().trim()) < 0 || Float.parseFloat(txt_15p1.getText().trim()) > 10) {
                txt_15p1.requestFocus();
                DialogHelper.alert(this, "điểm không hợp lệ, điểm nên trong khoảng 0 đến 10.");
                return false;
            } else if (Float.parseFloat(txt_15p2.getText().trim()) < 0 || Float.parseFloat(txt_15p2.getText().trim()) > 10) {
                txt_15p2.requestFocus();
                DialogHelper.alert(this, "điểm không hợp lệ, điểm nên trong khoảng 0 đến 10.");
                return false;
            } else if (Float.parseFloat(txt_15p3.getText().trim()) < 0 || Float.parseFloat(txt_15p3.getText().trim()) > 10) {
                txt_15p3.requestFocus();
                DialogHelper.alert(this, "điểm không hợp lệ, điểm nên trong khoảng 0 đến 10.");
                return false;
            } else if (Float.parseFloat(txt_45p1.getText().trim()) < 0 || Float.parseFloat(txt_45p1.getText().trim()) > 10) {
                txt_45p1.requestFocus();
                DialogHelper.alert(this, "điểm không hợp lệ, điểm nên trong khoảng 0 đến 10.");
                return false;
            } else if (Float.parseFloat(txt_45p2.getText().trim()) < 0 || Float.parseFloat(txt_45p2.getText().trim()) > 10) {
                txt_45p2.requestFocus();
                DialogHelper.alert(this, "điểm không hợp lệ, điểm nên trong khoảng 0 đến 10.");
                return false;
            } else if (Float.parseFloat(txt_hk.getText().trim()) < 0 || Float.parseFloat(txt_hk.getText().trim()) > 10) {
                txt_hk.requestFocus();
                DialogHelper.alert(this, "điểm không hợp lệ, điểm nên trong khoảng 0 đến 10.");
                return false;
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "điểm không hợp lệ!");
            return false;
        }
        return true;
    }

    private void tinhDiemTBM() {
        Double m1 = Double.valueOf(txt_diemMieng1.getText());
        Double m2 = Double.valueOf(txt_diemMieng2.getText());
        Double m3 = Double.valueOf(txt_diemMieng3.getText());
        Double diem15pl1 = Double.parseDouble(txt_15p1.getText());
        Double diem15pl2 = Double.parseDouble(txt_15p2.getText());
        Double diem15pl3 = Double.parseDouble(txt_15p3.getText());
        Double diem45pl1 = Double.parseDouble(txt_45p1.getText());
        Double diem45pl2 = Double.parseDouble(txt_45p2.getText());
        Double diemHK = Double.parseDouble(txt_hk.getText());
        Double rate = (double) Math.round((m1 + m2 + m3 + diem15pl1 + diem15pl2 + diem15pl3 + diem45pl1 * 2 + diem45pl2 * 2 + diemHK * 3) / 13);
        Double TBM = (rate * 100) / 100;
        lbl_diemTBM.setText(TBM.toString());
    }

    private void tinhDiemDanhGia() {
        int i1 = cbo_diemTX1.getSelectedItem().toString() == "Đạt" ? 1 : 0;
        int i2 = cbo_diemTX2.getSelectedItem().toString() == "Đạt" ? 1 : 0;
        int i3 = cbo_diemTX3.getSelectedItem().toString() == "Đạt" ? 1 : 0;
        int i4 = cbo_diemTX4.getSelectedItem().toString() == "Đạt" ? 1 : 0;
        int i5 = cbo_diemTX5.getSelectedItem().toString() == "Đạt" ? 1 : 0;
        int i6 = cbo_diemTX6.getSelectedItem().toString() == "Đạt" ? 1 : 0;
        int i7 = cbo_diemTX7.getSelectedItem().toString() == "Đạt" ? 1 : 0;
        int i8 = cbo_diemTX8.getSelectedItem().toString() == "Đạt" ? 1 : 0;
        int i9 = cbo_diemTX9.getSelectedItem().toString() == "Đạt" ? 1 : 0;
        Double TBM = (double) (i1 + i2 + i3 + i4 + i5 + i6 + i7 * 2 + i8 * 2 + i9 * 3) / 13;
        lbl_diemTBMDG.setText(TBM >= 0.5 ? "Đạt" : "Chưa Đạt");
    }

    

}

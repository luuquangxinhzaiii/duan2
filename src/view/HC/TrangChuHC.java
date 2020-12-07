/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.HC;

import view.BDH.*;
import AppPackage.AnimationClass;
import da.dao.GiaoVienDAO;
import da.dao.HocSinhDAO;
import da.dao.KhoiDAO;
import da.dao.LopHocDAO;
import da.dao.MonDAO;
import da.dao.NamHocDAO;
import da.dao.PhanCongDAO;
import da.helper.CsvFile;
import da.helper.DialogHelper;
import da.helper.ShareHelper;
import da.model.GiaoVien;
import da.model.HocSinh;
import da.model.Khoi;
import da.model.LopHoc;
import da.model.Mon;
import da.model.NamHoc;
import da.model.PhanCong;
import java.awt.Color;
import java.awt.Frame;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Rahmans
 */
public class TrangChuHC extends javax.swing.JFrame {

    static boolean maximized = true;
    int xMouse;
    int yMouse;
    NamHocDAO nhDAO = new NamHocDAO();
    LopHocDAO lhDAO = new LopHocDAO();
    HocSinhDAO hsDAO = new HocSinhDAO();
    KhoiDAO kDAO = new KhoiDAO();
    MonDAO mhDAO = new MonDAO();
    String head[] = {"Mã học sinh", "Họ tên", "Ngày sinh", "Giới tính"};
    String headLopHoc[] = {"Mã lớp", "Tên lớp", "Niên học"};
    String headPhanCong[] = {"Mã lớp", "Giáo viên", "Tên môn dạy", "Vai trò", "Học kì"};
    String headMonHoc[] = {"Tên môn", "Hình thức đánh giá", "Khối"};
    DefaultTableModel model = new DefaultTableModel(head, 0);
    DefaultTableModel modelLopHoc = new DefaultTableModel(headLopHoc, 0);
    DefaultTableModel modelPhanCong = new DefaultTableModel(headPhanCong, 0);
    DefaultTableModel modelMonHoc = new DefaultTableModel(headMonHoc, 0);
    PhanCongDAO pcDAO = new PhanCongDAO();
    MonDAO mDAO = new MonDAO();
    GiaoVienDAO gvDAO = new GiaoVienDAO();
    JFileChooser jFileChooser1 = new JFileChooser();
    CsvFile fv = new CsvFile();

    public TrangChuHC() {
        initComponents();
        lbl_Gv.setText(ShareHelper.TaiKhoan.getHoten());
        this.loadtoCBBNamHoc();
        this.loadCBB();
        loadToCbbKhoi();
        btn_Luu.setEnabled(false);
        btn_ChinhSua.setEnabled(false);
        btnTaiAnh.setEnabled(false);
        txtMagiaoVien.setEnabled(false);
        loadTableSubject();
        loadCboKhoi();
    }

    public void clear() {
        lbl_timkiem.setText("");
        lbl_mamon.setText("");
        lbl_tenMon.setText("");
    }

    public void tblSubjectClick() {
        int i = tbl_DanhSachMon.getSelectedRow();
        lbl_mamon.setText(tbl_DanhSachMon.getValueAt(i, 0).toString());
        lbl_tenMon.setText(tbl_DanhSachMon.getValueAt(i, 1).toString());
        cbo_hinhthuc.setSelectedItem(tbl_DanhSachMon.getValueAt(i, 2).toString().equals("Chấm điểm") ? "Chấm điểm" : "Đánh giá");
        cbo_Khoi.setSelectedItem(tbl_DanhSachMon.getValueAt(i, 3).toString());
    }

    public void UpdateSubject() {
        Mon mon = new Mon();
        mon.setMaMon(UUID.fromString(lbl_mamon.getText()));
        mon.setTenMon(lbl_tenMon.getText());
        mon.setHinhThucDG(cbo_hinhthuc.getSelectedItem().toString().equals("Chấm điểm") ? true : false);
        mon.setMaKhoi(kDAO.selectByTenKhoi(cbo_Khoi.getSelectedItem().toString()).getMaKhoi());
        mDAO.update(mon);
        loadTableSubject();
    }

    public void loadCboKhoi() {
        List<Khoi> list = kDAO.select();
        for (Khoi khoi : list) {
            cbo_Khoi.addItem(khoi.getTenKhoi());
        }
    }

    public void addSubject() {
        Mon mon = new Mon();
        mon.setTenMon(lbl_tenMon.getText());
        mon.setHinhThucDG(cbo_hinhthuc.getSelectedItem().toString().equals("Chấm điểm") ? true : false);
        mon.setMaKhoi(kDAO.selectByTenKhoi(cbo_Khoi.getSelectedItem().toString()).getMaKhoi());
        mDAO.insert(mon);
        loadTableSubject();
    }

    public void loadTableSubject() {
        try {
            modelMonHoc.setRowCount(0);
            List<Mon> list = mDAO.selectSubject();
            for (Mon mon : list) {
                Vector row = new Vector();
                row.add(mon.getTenMon());
                row.add(mon.getHinhThucDG() == true ? "Chấm điểm" : "Đánh giá");
                row.add(kDAO.selectByMaKhoi(mon.getMaKhoi()).getTenKhoi());
                modelMonHoc.addRow(row);
            }
            tbl_DanhSachMon.setModel(modelMonHoc);
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

    public void loadCBBMonDay() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboMonDay.getModel();
        if (model != null) {
            model.removeAllElements();
        }
        try {
            ResultSet rs = mDAO.selecttoPC((String) CbbKhoiHoc.getSelectedItem());
            while (rs.next()) {
                model.addElement(rs.getString("ten_mon"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadToCbbKhoi() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) CbbKhoi.getModel();
        if (model != null) {
            model.removeAllElements();
        }
        try {
            List<Khoi> list = kDAO.select();
            for (Khoi k : list) {
                model.addElement(k.getTenKhoi());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi truy van du lieu CBB");
            e.printStackTrace();
        }
    }

    public void loadCBB() {
        model.setRowCount(0);
        try {
            String nienHoc = (String) cbbNamHoc.getSelectedItem();
            String tenLop = (String) cbbLopHoc.getSelectedItem();
            ResultSet rs = hsDAO.loadWith2(tenLop, nienHoc);
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getString("mahocsinh"));
                row.add(rs.getString("hoten"));
                row.add(rs.getDate("ngaysinh"));
                row.add(rs.getBoolean("gioitinh") ? "Nam" : "Nữ");
                model.addRow(row);
            }
            tblGridView.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadToTableLopHoc() {
        modelLopHoc.setRowCount(0);
        try {
            String nienHoc = (String) cbbNamHoc.getSelectedItem();
            ResultSet rs = lhDAO.selectLopByNienhoc(nienHoc);
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getString("malop"));
                row.add(rs.getString("tenlop"));
                row.add(rs.getString("nienhoc"));
                modelLopHoc.addRow(row);
            }
            tblGridView3.setModel(modelLopHoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadToTablePhanCong() {
        modelPhanCong.setRowCount(0);
        try {
            String nienHoc = (String) cbbNamHoc.getSelectedItem();
            boolean Ki = cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false;
            ResultSet rs = pcDAO.select3(nienHoc, Ki);
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getString("tenlop"));
                row.add(rs.getString("hoten"));
                row.add(rs.getString("ten_mon"));
                if (rs.getBoolean("vaitro") == true) {
                    row.add("Chủ nghiệm");
                } else {
                    row.add("Bộ môn");
                }
                row.add(rs.getBoolean("hocki") == true ? "Học kì 1" : "Học kì 2");
                modelPhanCong.addRow(row);
            }
            tblGridView4.setModel(modelPhanCong);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void NewForm() {
        lbl_Mahocsinh.setText("");
        suaHoten.setText("");
        ((JTextField) jDateChooser5.getDateEditor().getUiComponent()).setText("");
        suaDanToc.setText("");
        suaTonGiao.setText("");
        suaNoiSinh.setText("");
        suaCMND.setText("");

        suaSDT.setText("");
        suaAnh.setText("");
        ((JTextField) jDateChooser6.getDateEditor().getUiComponent()).setText("");
        suaDiaChi.setText("");
        suaNguoiDamHo.setText("");
        suaHotenBo.setText("");
        suaSDTBo.setText("");
        suaDVCTBo.setText("");
        suaHotenMe.setText("");
        suaSDTMe.setText("");
        suaDVCTMe.setText("");
        image.setIcon(null);
        cbbsuaGioitinh.setSelectedIndex(0);
        cbbSuaTrangThai.setSelectedIndex(0);

    }

    HocSinh getModel() {
        HocSinh model = new HocSinh();
        try {
            model.setMaHS(lbl_Mahocsinh.getText());
            model.setHoTen(suaHoten.getText());
            model.setGioiTinh(cbbsuaGioitinh.getSelectedIndex() == 0);
            model.setNgaySinh(jDateChooser5.getDate());
            model.setCmND(suaCMND.getText());
            model.setDiaChi(suaDiaChi.getText());
            model.setDienThoai(suaSDT.getText());
            model.setDanToc(suaDanToc.getText());
            model.setTonGiao(suaTonGiao.getText());
            model.setNgayVD(jDateChooser6.getDate());
            model.setNoiSinh(suaNoiSinh.getText());
            String tenLop = (String) cbbLopHoc.getSelectedItem();
            ResultSet rs = lhDAO.selectWithTenlop(tenLop);
            if (rs.next()) {
                model.setLop_id(lhDAO.findByTenLop(tenLop).getiD());
            }
            model.setHotenBo(suaHotenBo.getText());
            model.setHotenMe(suaHotenMe.getText());
            model.setDienThoaiBo(suaSDTBo.getText());
            model.setDienThoaiMe(suaSDTMe.getText());
            model.setDvctBo(suaDVCTBo.getText());
            model.setDvctMe(suaDVCTMe.getText());
            model.setTrangThai(cbbSuaTrangThai.getSelectedIndex() == 0);
            model.setAnh(suaAnh.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    void insert() {
        HocSinh model = getModel();
        try {
            hsDAO.insert(model);
            DialogHelper.alert(this, "Thêm mới thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Học sinh này đã tồn tại vui lòng tạo mới!");
            e.printStackTrace();
        }
    }

    public boolean check() {
        String ngaysinh = ((JTextField) jDateChooser5.getDateEditor().getUiComponent()).getText();
        String ngayvaodoan = ((JTextField) jDateChooser6.getDateEditor().getUiComponent()).getText();
        String mahs = lbl_Mahocsinh.getText();
        ResultSet rs = hsDAO.select();
        if (lbl_Mahocsinh.getText().equals("")) {
            DialogHelper.alert(this, "Vui lòng nhấn nút thêm mới");
            return false;
        } else if ((suaHoten.getText()).equals("")) {
            DialogHelper.alert(this, "Họ tên không được để trống");
            return false;
        } else if (suaHoten.getText().length() > 50) {
            DialogHelper.alert(this, "Họ tên không được lớn hơn 50 ký tự!");
            return false;
        } else if (ngaysinh.equals("")) {
            DialogHelper.alert(this, "Ngày sinh không đươc để trống");
            return false;
        } else if ((suaDanToc.getText()).equals("")) {
            DialogHelper.alert(this, "Dân tộc không được để trống");
            return false;
        } else if (suaDanToc.getText().length() > 20) {
            DialogHelper.alert(this, "Dân tộc không được lớn hơn 20 ký tự!");
            return false;
        } else if ((suaTonGiao.getText()).equals("")) {
            DialogHelper.alert(this, "Tôn giáo không được để trống");
            return false;
        } else if (suaTonGiao.getText().length() > 20) {
            DialogHelper.alert(this, "Tôn giáo không được lớn hơn 20 ký tự!");
            return false;
        } else if ((suaNoiSinh.getText()).equals("")) {
            DialogHelper.alert(this, "Nơi sinh không được để trống");
            return false;
        } else if (suaNoiSinh.getText().length() > 50) {
            DialogHelper.alert(this, "Nơi sinh không được lớn hơn 50 ký tự!");
            return false;
        } else if (ngayvaodoan.equals("")) {
            DialogHelper.alert(this, "Ngày vào đoàn không đươc để trống");
            return false;
        } else if (suaSDT.getText().equals("")) {
            DialogHelper.alert(this, "Số điện thoại không được để trống");
            return false;
        } else if (!suaSDT.getText().matches("[0-9]+")) {
            DialogHelper.alert(this, "Số điện thoại chỉ nhập số");
            return false;
        } else if (suaSDT.getText().length() < 10 || suaSDT.getText().length() > 12) {
            DialogHelper.alert(this, "Số điện thoại phải nhập đủ 10 hoặc 11 số.");
            return false;
        } else if ((suaDiaChi.getText()).equals("")) {
            DialogHelper.alert(this, "Địa chỉ không được để trống");
            return false;
        } else if (suaDiaChi.getText().length() > 50) {
            DialogHelper.alert(this, "Địa chỉ không được lớn hơn 50 ký tự!");
            return false;
        } else {
            return true;
        }

    }

    public void update() {
        HocSinh model = getModel();
        try {
            hsDAO.update(model);

            DialogHelper.alert(this, "Chỉnh sửa thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Chỉnh sửa thất bại!");
            e.printStackTrace();
        }
    }

    public void NewForm2() {
        txtTenLop.setText("");
        txtMagiaoVien.setText("");
        CbbKhoiHoc.setSelectedIndex(0);
        txtSiSo.setText("");
    }

    LopHoc getModel1() {
        LopHoc model = new LopHoc();
        try {
            model.setMaLop(lbl_malop.getText());
            model.setTenLop(txtTenLop.getText());
            if (CbbKhoiHoc.getSelectedIndex() == 0) {
                model.setMaKhoi(kDAO.selectByTenKhoi("Khối 6").getMaKhoi());
            } else if (CbbKhoiHoc.getSelectedIndex() == 1) {
                model.setMaKhoi(kDAO.selectByTenKhoi("Khối 7").getMaKhoi());
            } else if (CbbKhoiHoc.getSelectedIndex() == 2) {
                model.setMaKhoi(kDAO.selectByTenKhoi("Khối 8").getMaKhoi());
            } else {
                model.setMaKhoi(kDAO.selectByTenKhoi("Khối 9").getMaKhoi());
            }
            String nh = (String) cbbNamHoc.getSelectedItem();
            ResultSet rs = nhDAO.select2(nh);
            if (rs.next()) {
                model.setMaNH(UUID.fromString(rs.getString("manamhoc")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    PhanCong getModel2() {
        PhanCong model = new PhanCong();
        try {
            String[] list = cbogiaovien.getSelectedItem().toString().split("-");
            model.setMaLop(lhDAO.findBymalop(lbl_malop2.getText()).getiD());
            model.setMaGV(gvDAO.findBymagiaovien(list[0]).getId());
            if (rdoChuNghiem.isSelected()) {
                model.setVaiTro(true);
                model.setMaMon(null);
            } else {
                model.setVaiTro(false);
                model.setMaMon(mhDAO.findByTenMon((String) cboMonDay.getSelectedItem()).getMaMon());
                model.setHocKi(cbo_hocKi.getSelectedItem().toString().equals("Học kỳ 1") ? true : false);
            }
            model.setMaNamHoc(nhDAO.findByNienHoc((String) cbbNamHoc.getSelectedItem()).getMaNamHoc());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    void insert2() {
        LopHoc model = getModel1();
        try {
            lhDAO.insert(model);
            DialogHelper.alert(this, "Thêm mới thành công!");

        } catch (Exception e) {
            DialogHelper.alert(this, "Có lỗi xảy ra, vui lòng kiểm tra lại");

        }
    }

    void insert3() {
        PhanCong model = getModel2();
        try {
            if (model.getVaiTro() == true) {
                pcDAO.insert(model);
                DialogHelper.alert(this, "Thêm mới chủ nhiệm lớp thành công!");
            } else {
                pcDAO.insert(model);
                DialogHelper.alert(this, "Thêm mới phân công thành công!");
            }

        } catch (Exception e) {
            DialogHelper.alert(this, "Thêm mới phân công thất bại!");
            e.printStackTrace();
        }
    }

    public boolean check2() {
        if (txtTenLop.getText().equals("")) {
            DialogHelper.alert(this, "Vui lòng nhập tên lớp!");
            return false;
        } else if (txtTenLop.getText().length() > 50) {
            DialogHelper.alert(this, "Tên lớp không được dài quá 50 ký tự!");
            return false;
        } else {
            return true;
        }

    }

    public void update2() {
        LopHoc model = getModel1();
        try {
            lhDAO.update(model);

            DialogHelper.alert(this, "Chỉnh sửa thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Chỉnh sửa thất bại!");
            e.printStackTrace();
        }
    }

    public void update3() {
        PhanCong model = getModel2();

        try {
            pcDAO.update(model);

            DialogHelper.alert(this, "Chỉnh sửa thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Chỉnh sửa thất bại!");
            e.printStackTrace();
        }
    }

    public void NewForm3() {
        lbl_malop2.setText("");
        cboMonDay.setSelectedIndex(0);
        buttonGroup1.clearSelection();
    }

    public boolean checkPc() {
        ResultSet rs = gvDAO.select();

        if (lbl_malop2.getText().equals("")) {
            DialogHelper.alert(this, "Vui lòng chọn lớp bên bảng Danh sách lớp!");
            return false;
        } else if (rdoChuNghiem.isSelected() == false && rdoBoMon.isSelected() == false) {
            DialogHelper.alert(this, "Vui lòng chọn vai trò");
            return false;
        } else {
            return true;
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
            Logger.getLogger(TrangChuHC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void clicktab(String tentab) {
        if (tentab.equals("Quản lý học sinh")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(quanlyhocsinh);
            body.repaint();
            body.revalidate();
        } else if (tentab.equals("Quản lý tin tức")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(quanlytintuc);
            body.repaint();
            body.revalidate();
        } else if (tentab.equals("Danh sách lớp")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(danhsachlop);
            body.repaint();
            body.revalidate();
        } else if (tentab.equals("Môn Học")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(maintrangchu);
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

        PopUpMenu = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        pnlHeader = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnMinimize = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnMinimize4 = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        cbbNamHoc = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        cbo_hocKi = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        trangchu = new javax.swing.JPanel();
        lane1 = new javax.swing.JLabel();
        lbltrangchu = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        qlyhocsinh = new javax.swing.JPanel();
        lane2 = new javax.swing.JLabel();
        lblquanlyhocsinh = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        qlytintuc = new javax.swing.JPanel();
        lane3 = new javax.swing.JLabel();
        lblqlytintuc = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        dslop = new javax.swing.JPanel();
        lane4 = new javax.swing.JLabel();
        lbldslop = new javax.swing.JButton();
        qlnamhoc = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        lbl_Gv = new javax.swing.JLabel();
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
        quanlytintuc = new javax.swing.JPanel();
        danhsachlop = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtTenLop = new javax.swing.JTextField();
        txtMagiaoVien = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtSiSo = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        CbbKhoiHoc = new javax.swing.JComboBox<>();
        btn_capNhat9 = new javax.swing.JButton();
        lbl_malop = new javax.swing.JLabel();
        bodylop = new javax.swing.JPanel();
        banglop = new javax.swing.JScrollPane();
        tblGridView3 = new javax.swing.JTable();
        phancong = new javax.swing.JPanel();
        banglop1 = new javax.swing.JScrollPane();
        tblGridView4 = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        lbl_maPc = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        lbl_malop2 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        cboMonDay = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        rdoBoMon = new javax.swing.JRadioButton();
        rdoChuNghiem = new javax.swing.JRadioButton();
        cbogiaovien = new javax.swing.JComboBox<>();
        bodythaotac = new javax.swing.JPanel();
        thaotaclop = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        btn_capNhat2 = new javax.swing.JButton();
        btn_capNhat3 = new javax.swing.JButton();
        btn_capNhat4 = new javax.swing.JButton();
        btn_capNhat5 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        Btn_Xuat_Excel_Lop = new javax.swing.JButton();
        thaotacpc = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        btn_capNhat17 = new javax.swing.JButton();
        btn_capNhat18 = new javax.swing.JButton();
        btn_capNhat19 = new javax.swing.JButton();
        btn_capNhat20 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        btn_capNhat21 = new javax.swing.JButton();
        btn_capNhat22 = new javax.swing.JButton();
        btn_capNhat23 = new javax.swing.JButton();
        quanlyhocsinh = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGridView = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        image = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        lbl_Mahocsinh = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        suaHoten = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        cbbsuaGioitinh = new javax.swing.JComboBox<>();
        jLabel60 = new javax.swing.JLabel();
        btnTaiAnh = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        suaDanToc = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        suaTonGiao = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        suaNoiSinh = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        suaCMND = new javax.swing.JTextField();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        suaSDT = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        suaAnh = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        cbbSuaTrangThai = new javax.swing.JComboBox<>();
        jLabel72 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        suaDiaChi = new javax.swing.JTextArea();
        jLabel80 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        suaNguoiDamHo = new javax.swing.JTextArea();
        jDateChooser6 = new com.toedter.calendar.JDateChooser();
        jPanel4 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        suaHotenBo = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        suaSDTBo = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        suaDVCTBo = new javax.swing.JTextArea();
        jLabel79 = new javax.swing.JLabel();
        suaHotenMe = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        suaSDTMe = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        suaDVCTMe = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        Btn_Xuat_Excel_HS = new javax.swing.JButton();
        Nhap_excel_hs = new javax.swing.JButton();
        btn_Luu = new javax.swing.JButton();
        btnThemMoi = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        CbbKhoi = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        cbbLopHoc = new javax.swing.JComboBox<>();
        btn_ChinhSua = new javax.swing.JButton();
        maintrangchu = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbl_DanhSachMon = new javax.swing.JTable();
        jLabel37 = new javax.swing.JLabel();
        lbl_timkiem = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        lbl_tenMon = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        cbo_Khoi = new javax.swing.JComboBox<>();
        btn_reloadSubject = new javax.swing.JButton();
        btn_addSubject = new javax.swing.JButton();
        btn_updateSubject = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        cbo_hinhthuc = new javax.swing.JComboBox<>();
        lbl_mamon = new javax.swing.JLabel();

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_update_file_32px.png"))); // NOI18N
        jMenuItem1.setText("Cập Nhật");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        PopUpMenu.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-cancel-delete-32.png"))); // NOI18N
        jMenuItem2.setText("Thôi học");
        PopUpMenu.add(jMenuItem2);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_telegram_app_32px.png"))); // NOI18N
        jMenuItem3.setText("Chuyển lớp");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        PopUpMenu.add(jMenuItem3);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hành Chính");
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

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel30.setText("Niên học :");

        cbbNamHoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbNamHoc.setBorder(null);
        cbbNamHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbNamHocActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel31.setText("Học kì :");

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
                .addGap(388, 388, 388)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbbNamHoc, 0, 160, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_hocKi, 0, 160, Short.MAX_VALUE)
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
                        .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnExit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                            .addComponent(btnMinimize, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(btnMinimize4)
                        .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbo_hocKi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbbNamHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
        jLabel2.setText("Môn Học");

        javax.swing.GroupLayout trangchuLayout = new javax.swing.GroupLayout(trangchu);
        trangchu.setLayout(trangchuLayout);
        trangchuLayout.setHorizontalGroup(
            trangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(trangchuLayout.createSequentialGroup()
                .addComponent(lane1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbltrangchu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        trangchuLayout.setVerticalGroup(
            trangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbltrangchu, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        qlyhocsinh.setBackground(new java.awt.Color(255, 255, 255));
        qlyhocsinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qlyhocsinhMouseClicked(evt);
            }
        });

        lane2.setBackground(new java.awt.Color(255, 255, 255));
        lane2.setOpaque(true);

        lblquanlyhocsinh.setBackground(new java.awt.Color(255, 255, 255));
        lblquanlyhocsinh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-clipboard-list-32.png"))); // NOI18N
        lblquanlyhocsinh.setContentAreaFilled(false);
        lblquanlyhocsinh.setFocusable(false);
        lblquanlyhocsinh.setOpaque(true);
        lblquanlyhocsinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblquanlyhocsinhMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblquanlyhocsinhMouseExited(evt);
            }
        });
        lblquanlyhocsinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblquanlyhocsinhActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Quản lý học sinh");

        javax.swing.GroupLayout qlyhocsinhLayout = new javax.swing.GroupLayout(qlyhocsinh);
        qlyhocsinh.setLayout(qlyhocsinhLayout);
        qlyhocsinhLayout.setHorizontalGroup(
            qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlyhocsinhLayout.createSequentialGroup()
                .addComponent(lane2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblquanlyhocsinh, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        qlyhocsinhLayout.setVerticalGroup(
            qlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblquanlyhocsinh, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        qlytintuc.setBackground(new java.awt.Color(255, 255, 255));
        qlytintuc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qlytintucMouseClicked(evt);
            }
        });

        lane3.setBackground(new java.awt.Color(255, 255, 255));
        lane3.setOpaque(true);

        lblqlytintuc.setBackground(new java.awt.Color(255, 255, 255));
        lblqlytintuc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-save-tabs-list-32.png"))); // NOI18N
        lblqlytintuc.setContentAreaFilled(false);
        lblqlytintuc.setFocusable(false);
        lblqlytintuc.setOpaque(true);
        lblqlytintuc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblqlytintucMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblqlytintucMouseExited(evt);
            }
        });
        lblqlytintuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblqlytintucActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText("Quản lý tin tức");

        javax.swing.GroupLayout qlytintucLayout = new javax.swing.GroupLayout(qlytintuc);
        qlytintuc.setLayout(qlytintucLayout);
        qlytintucLayout.setHorizontalGroup(
            qlytintucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlytintucLayout.createSequentialGroup()
                .addComponent(lane3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblqlytintuc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        qlytintucLayout.setVerticalGroup(
            qlytintucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblqlytintuc, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        dslop.setBackground(new java.awt.Color(255, 255, 255));
        dslop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dslopMouseClicked(evt);
            }
        });

        lane4.setBackground(new java.awt.Color(255, 255, 255));
        lane4.setOpaque(true);

        lbldslop.setBackground(new java.awt.Color(255, 255, 255));
        lbldslop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-syllabus-32.png"))); // NOI18N
        lbldslop.setContentAreaFilled(false);
        lbldslop.setFocusable(false);
        lbldslop.setOpaque(true);
        lbldslop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbldslopMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbldslopMouseExited(evt);
            }
        });
        lbldslop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbldslopActionPerformed(evt);
            }
        });

        qlnamhoc.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        qlnamhoc.setText("Danh sách lớp");

        javax.swing.GroupLayout dslopLayout = new javax.swing.GroupLayout(dslop);
        dslop.setLayout(dslopLayout);
        dslopLayout.setHorizontalGroup(
            dslopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dslopLayout.createSequentialGroup()
                .addComponent(lane4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbldslop, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(qlnamhoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        dslopLayout.setVerticalGroup(
            dslopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbldslop, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(qlnamhoc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/tải xuống.png"))); // NOI18N

        jLabel99.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel99.setText("Tên giáo viên");

        lbl_Gv.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_Gv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Gv.setText("Mã GV");

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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(qlytintuc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dslop, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(qlyhocsinh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(trangchu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel99, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_Gv, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel102, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel99, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_Gv, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel102, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(trangchu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(qlyhocsinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(qlytintuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(dslop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(183, Short.MAX_VALUE))
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
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1186, Short.MAX_VALUE)
        );
        trangchinhLayout.setVerticalGroup(
            trangchinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 635, Short.MAX_VALUE)
        );

        body.add(trangchinh, "card2");

        quanlytintuc.setBackground(new java.awt.Color(0, 102, 102));

        javax.swing.GroupLayout quanlytintucLayout = new javax.swing.GroupLayout(quanlytintuc);
        quanlytintuc.setLayout(quanlytintucLayout);
        quanlytintucLayout.setHorizontalGroup(
            quanlytintucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1186, Short.MAX_VALUE)
        );
        quanlytintucLayout.setVerticalGroup(
            quanlytintucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 635, Short.MAX_VALUE)
        );

        body.add(quanlytintuc, "card3");

        danhsachlop.setBackground(new java.awt.Color(255, 255, 255));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 102, 102));
        jLabel19.setText("Danh sách lớp.");

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin lớp học", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel20.setText("Mã lớp :");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel21.setText("Tên lớp :");

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel22.setText("Mã giáo viên :");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel23.setText("Sĩ số :");

        txtSiSo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSiSo.setEnabled(false);

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel24.setText("Khối :");

        CbbKhoiHoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Khối 6", "Khối 7", "Khối 8", "Khối 9" }));
        CbbKhoiHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbbKhoiHocActionPerformed(evt);
            }
        });

        btn_capNhat9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btn_capNhat9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-syllabus-32.png"))); // NOI18N
        btn_capNhat9.setText("Phân công giảng dạy");
        btn_capNhat9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat9ActionPerformed(evt);
            }
        });

        lbl_malop.setText("auto generate ");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMagiaoVien, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTenLop, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                            .addComponent(lbl_malop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(23, 23, 23)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSiSo, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CbbKhoiHoc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(btn_capNhat9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_malop, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSiSo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(CbbKhoiHoc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                        .addComponent(txtTenLop, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_capNhat9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMagiaoVien, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
        );

        bodylop.setLayout(new java.awt.CardLayout());

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
                "Mã lớp", "Tên lớp", "Mã giáo viên", "Sĩ số", "Niên học"
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

        bodylop.add(banglop, "card3");

        tblGridView4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"098992323", "Nguyễn Xuân Bách", "04/06/2020", "9"},
                {"2", "2", "2", "2"},
                {"3", "3", "3", "3"},
                {"5", "4", "4", "4"},
                {"5", "5", "0", "0"},
                {"5", "5", null, null},
                {"5", "5", null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã PC", "Mã lớp", "Mã giáo viên", "Mã môn"
            }
        ));
        tblGridView4.setRowHeight(25);
        tblGridView4.setSelectionBackground(new java.awt.Color(52, 152, 219));
        tblGridView4.getTableHeader().setReorderingAllowed(false);
        tblGridView4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridView4MouseClicked(evt);
            }
        });
        tblGridView4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblGridView4KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblGridView4KeyReleased(evt);
            }
        });
        banglop1.setViewportView(tblGridView4);

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel25.setText("Mã PC:");

        lbl_maPc.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_maPc.setText("auto generate");

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel26.setText("Mã lớp :");

        lbl_malop2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel27.setText("Mã giáo viên :");

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel28.setText("Vai trò:");

        cboMonDay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboMonDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMonDayActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_back_to_32px_1.png"))); // NOI18N
        jButton1.setText("Hủy");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel29.setText("Môn dạy :");

        buttonGroup1.add(rdoBoMon);
        rdoBoMon.setText("Bộ môn");

        buttonGroup1.add(rdoChuNghiem);
        rdoChuNghiem.setText("Chủ nghiệm");

        cbogiaovien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbogiaovien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbogiaovienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout phancongLayout = new javax.swing.GroupLayout(phancong);
        phancong.setLayout(phancongLayout);
        phancongLayout.setHorizontalGroup(
            phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, phancongLayout.createSequentialGroup()
                .addGroup(phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(phancongLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(phancongLayout.createSequentialGroup()
                        .addGap(677, 677, 677)
                        .addGroup(phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(phancongLayout.createSequentialGroup()
                                .addGroup(phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel29))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cboMonDay, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_maPc, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                                .addComponent(lbl_malop2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(phancongLayout.createSequentialGroup()
                                    .addComponent(rdoChuNghiem)
                                    .addGap(18, 18, 18)
                                    .addComponent(rdoBoMon)))
                            .addComponent(cbogiaovien, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, phancongLayout.createSequentialGroup()
                    .addContainerGap(18, Short.MAX_VALUE)
                    .addComponent(banglop1, javax.swing.GroupLayout.PREFERRED_SIZE, 632, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(418, Short.MAX_VALUE)))
        );
        phancongLayout.setVerticalGroup(
            phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(phancongLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_maPc, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_malop2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbogiaovien, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboMonDay, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdoBoMon)
                    .addComponent(rdoChuNghiem))
                .addGap(15, 15, 15)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(phancongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, phancongLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(banglop1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        bodylop.add(phancong, "card3");

        bodythaotac.setLayout(new java.awt.CardLayout());

        thaotaclop.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        btn_capNhat2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-user-account-32.png"))); // NOI18N
        btn_capNhat2.setText("Thêm mới");
        btn_capNhat2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat2ActionPerformed(evt);
            }
        });

        btn_capNhat3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_update_file_32px.png"))); // NOI18N
        btn_capNhat3.setText("Cập Nhật");
        btn_capNhat3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat3ActionPerformed(evt);
            }
        });

        btn_capNhat4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-cancel-delete-32.png"))); // NOI18N
        btn_capNhat4.setText("Xóa lớp");
        btn_capNhat4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat4ActionPerformed(evt);
            }
        });

        btn_capNhat5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-update-32.png"))); // NOI18N
        btn_capNhat5.setText("Làm mới");
        btn_capNhat5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_capNhat5MouseClicked(evt);
            }
        });
        btn_capNhat5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btn_capNhat2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_capNhat3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btn_capNhat4, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_capNhat5, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_capNhat2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_capNhat3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_capNhat4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_capNhat5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        thaotaclop.addTab("Thao tác", jPanel6);

        Btn_Xuat_Excel_Lop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-uninstalling-updates-32.png"))); // NOI18N
        Btn_Xuat_Excel_Lop.setText("Xuất Excel");
        Btn_Xuat_Excel_Lop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Xuat_Excel_LopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(92, Short.MAX_VALUE)
                .addComponent(Btn_Xuat_Excel_Lop, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(Btn_Xuat_Excel_Lop, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        thaotaclop.addTab("Excel", jPanel7);

        bodythaotac.add(thaotaclop, "card2");

        thaotacpc.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        btn_capNhat17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-user-account-32.png"))); // NOI18N
        btn_capNhat17.setText("Thêm mới");
        btn_capNhat17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat17ActionPerformed(evt);
            }
        });

        btn_capNhat18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_update_file_32px.png"))); // NOI18N
        btn_capNhat18.setText("Cập Nhật");
        btn_capNhat18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat18ActionPerformed(evt);
            }
        });

        btn_capNhat19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-cancel-delete-32.png"))); // NOI18N
        btn_capNhat19.setText("Xóa PC");
        btn_capNhat19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat19ActionPerformed(evt);
            }
        });

        btn_capNhat20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-update-32.png"))); // NOI18N
        btn_capNhat20.setText("Làm mới");
        btn_capNhat20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(btn_capNhat17, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_capNhat18, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(btn_capNhat19, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_capNhat20, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_capNhat17, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_capNhat18, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_capNhat19, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_capNhat20, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        thaotacpc.addTab("Thao tác", jPanel10);

        btn_capNhat21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-downloads-32.png"))); // NOI18N
        btn_capNhat21.setText("Tải Form");
        btn_capNhat21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat21ActionPerformed(evt);
            }
        });

        btn_capNhat22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-uninstalling-updates-32.png"))); // NOI18N
        btn_capNhat22.setText("Nhập Excel");
        btn_capNhat22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat22ActionPerformed(evt);
            }
        });

        btn_capNhat23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-installing-updates-32.png"))); // NOI18N
        btn_capNhat23.setText("Xuất Excel");
        btn_capNhat23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat23ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(btn_capNhat21, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_capNhat22, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_capNhat23, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_capNhat21, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_capNhat22, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btn_capNhat23, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        thaotacpc.addTab("Excel", jPanel11);

        bodythaotac.add(thaotacpc, "card2");

        javax.swing.GroupLayout danhsachlopLayout = new javax.swing.GroupLayout(danhsachlop);
        danhsachlop.setLayout(danhsachlopLayout);
        danhsachlopLayout.setHorizontalGroup(
            danhsachlopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danhsachlopLayout.createSequentialGroup()
                .addGroup(danhsachlopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(danhsachlopLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(danhsachlopLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(danhsachlopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bodylop, javax.swing.GroupLayout.PREFERRED_SIZE, 1068, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(danhsachlopLayout.createSequentialGroup()
                                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bodythaotac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        danhsachlopLayout.setVerticalGroup(
            danhsachlopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danhsachlopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(danhsachlopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bodythaotac, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addComponent(bodylop, javax.swing.GroupLayout.PREFERRED_SIZE, 331, Short.MAX_VALUE)
                .addContainerGap())
        );

        body.add(danhsachlop, "card4");

        quanlyhocsinh.setBackground(new java.awt.Color(255, 255, 255));

        tblGridView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "1", "1", null}
            },
            new String [] {
                "Mã HS", "Họ tên", "Ngày sinh", "Giới tính"
            }
        ));
        tblGridView.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblGridView.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblGridView.setRowHeight(25);
        tblGridView.setSelectionBackground(new java.awt.Color(51, 102, 255));
        tblGridView.setShowVerticalLines(false);
        tblGridView.getTableHeader().setReorderingAllowed(false);
        tblGridView.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tblGridViewMouseDragged(evt);
            }
        });
        tblGridView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridViewMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblGridViewMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblGridView);

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel17.setText("Tìm kiếm :");

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 51, 255));
        jLabel18.setText("Quản lý học sinh.");

        jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        image.setBackground(new java.awt.Color(0, 51, 204));
        image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/User.png"))); // NOI18N

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel58.setText("Mã học sinh :");

        lbl_Mahocsinh.setBackground(new java.awt.Color(255, 255, 255));

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel57.setText("Họ và tên :");

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel59.setText("Giới tính :");

        cbbsuaGioitinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel60.setText("Ngày sinh :");

        btnTaiAnh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_upload_to_the_cloud_32px.png"))); // NOI18N
        btnTaiAnh.setText("Tải ảnh");
        btnTaiAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTaiAnhMouseClicked(evt);
            }
        });
        btnTaiAnh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaiAnhActionPerformed(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel64.setText("Dân tộc :");

        jLabel65.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel65.setText("Tôn giáo :");

        suaTonGiao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suaTonGiaoActionPerformed(evt);
            }
        });

        jLabel66.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel66.setText("Nơi sinh :");

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel67.setText("CMND :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(image)
                    .addComponent(btnTaiAnh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel59)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_Mahocsinh, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                            .addComponent(cbbsuaGioitinh, 0, 202, Short.MAX_VALUE)
                            .addComponent(suaHoten)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jDateChooser5, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE))
                        .addComponent(suaCMND)
                        .addComponent(suaNoiSinh, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                        .addComponent(suaTonGiao)
                        .addComponent(suaDanToc)))
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
                            .addComponent(lbl_Mahocsinh, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(suaHoten, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbsuaGioitinh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTaiAnh))
                    .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suaDanToc, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suaTonGiao, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suaNoiSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suaCMND, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Chi tiết học sinh", jPanel2);

        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel75.setText("Ngày vào đoàn :");

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel69.setText("Số điện thoại :");

        jLabel70.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel70.setText("Path:");

        jLabel71.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel71.setText("Trạng thái  :");

        cbbSuaTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đi học", "Nghỉ học" }));

        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel72.setText("Địa chỉ :");

        suaDiaChi.setColumns(20);
        suaDiaChi.setRows(5);
        jScrollPane3.setViewportView(suaDiaChi);

        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel80.setText("Người dám hộ:");

        suaNguoiDamHo.setColumns(20);
        suaNguoiDamHo.setRows(5);
        jScrollPane6.setViewportView(suaNguoiDamHo);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel71, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel75, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel80, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(suaSDT)
                    .addComponent(suaAnh)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbbSuaTrangThai, 0, 274, Short.MAX_VALUE)
                            .addComponent(jScrollPane3)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jDateChooser6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suaSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suaAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbSuaTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 35, Short.MAX_VALUE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Chi tiết  học sinh", jPanel3);

        jLabel73.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel73.setText("Họ tên bố :");

        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel74.setText("Số điện thoại bố :");

        jLabel76.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel76.setText("Đơn vị công tác bố :");

        suaDVCTBo.setColumns(20);
        suaDVCTBo.setRows(5);
        jScrollPane4.setViewportView(suaDVCTBo);

        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel79.setText("Họ tên mẹ :");

        jLabel78.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel78.setText("Số điện thoại mẹ:");

        suaSDTMe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suaSDTMeActionPerformed(evt);
            }
        });

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel77.setText("Đơn vị công tác mẹ:");

        suaDVCTMe.setColumns(20);
        suaDVCTMe.setRows(5);
        jScrollPane5.setViewportView(suaDVCTMe);

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
                            .addComponent(suaHotenMe)
                            .addComponent(suaHotenBo)
                            .addComponent(suaSDTBo)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel78)
                        .addGap(23, 23, 23)
                        .addComponent(suaSDTMe))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel77)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suaHotenBo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suaSDTBo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suaHotenMe, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suaSDTMe, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Phụ huynh học sinh", jPanel4);

        Btn_Xuat_Excel_HS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-uninstalling-updates-32.png"))); // NOI18N
        Btn_Xuat_Excel_HS.setText("Xuất Excel");
        Btn_Xuat_Excel_HS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Xuat_Excel_HSActionPerformed(evt);
            }
        });

        Nhap_excel_hs.setText("Nhập Excel");
        Nhap_excel_hs.setToolTipText("");
        Nhap_excel_hs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Nhap_excel_hsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Nhap_excel_hs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Btn_Xuat_Excel_HS, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addGap(59, 59, 59))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(Nhap_excel_hs, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(Btn_Xuat_Excel_HS, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(185, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Excel", jPanel5);

        btn_Luu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-update-32.png"))); // NOI18N
        btn_Luu.setText("Lưu");
        btn_Luu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btn_Luu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LuuActionPerformed(evt);
            }
        });

        btnThemMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_user_male_32px.png"))); // NOI18N
        btnThemMoi.setText("Thêm mới");
        btnThemMoi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnThemMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMoiActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel32.setText("Khối :");

        CbbKhoi.setBorder(null);
        CbbKhoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbbKhoiActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel33.setText("Lớp :");

        cbbLopHoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbLopHoc.setBorder(null);
        cbbLopHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbLopHocActionPerformed(evt);
            }
        });

        btn_ChinhSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-update-32.png"))); // NOI18N
        btn_ChinhSua.setText("Chỉnh Sửa");
        btn_ChinhSua.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btn_ChinhSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ChinhSuaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout quanlyhocsinhLayout = new javax.swing.GroupLayout(quanlyhocsinh);
        quanlyhocsinh.setLayout(quanlyhocsinhLayout);
        quanlyhocsinhLayout.setHorizontalGroup(
            quanlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, quanlyhocsinhLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(quanlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(quanlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(quanlyhocsinhLayout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(CbbKhoi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel33)
                        .addGap(18, 18, 18)
                        .addComponent(cbbLopHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(quanlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(quanlyhocsinhLayout.createSequentialGroup()
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(quanlyhocsinhLayout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addComponent(btnThemMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(btn_Luu, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(btn_ChinhSua, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(24, 24, 24))
        );
        quanlyhocsinhLayout.setVerticalGroup(
            quanlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quanlyhocsinhLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(quanlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(quanlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(CbbKhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbbLopHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(quanlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(quanlyhocsinhLayout.createSequentialGroup()
                        .addGroup(quanlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(quanlyhocsinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Luu, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnThemMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_ChinhSua, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        body.add(quanlyhocsinh, "card5");

        maintrangchu.setBackground(new java.awt.Color(255, 255, 255));

        jLabel36.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 51, 255));
        jLabel36.setText("Danh sách môn học");

        tbl_DanhSachMon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "1", "1", null}
            },
            new String [] {
                "Mã môn", "Tên môn", "Khối", "Giới tính"
            }
        ));
        tbl_DanhSachMon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbl_DanhSachMon.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tbl_DanhSachMon.setRowHeight(25);
        tbl_DanhSachMon.setSelectionBackground(new java.awt.Color(51, 102, 255));
        tbl_DanhSachMon.setShowVerticalLines(false);
        tbl_DanhSachMon.getTableHeader().setReorderingAllowed(false);
        tbl_DanhSachMon.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tbl_DanhSachMonMouseDragged(evt);
            }
        });
        tbl_DanhSachMon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DanhSachMonMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbl_DanhSachMonMouseReleased(evt);
            }
        });
        jScrollPane8.setViewportView(tbl_DanhSachMon);

        jLabel37.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel37.setText("Tìm kiếm :");

        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel63.setText("Mã môn :");

        jLabel38.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel38.setText("Tên môn :");

        jLabel68.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel68.setText("Khối :");

        cbo_Khoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_KhoiActionPerformed(evt);
            }
        });

        btn_reloadSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-update-32.png"))); // NOI18N
        btn_reloadSubject.setText("Làm mới");
        btn_reloadSubject.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btn_reloadSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reloadSubjectActionPerformed(evt);
            }
        });

        btn_addSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_user_male_32px.png"))); // NOI18N
        btn_addSubject.setText("Thêm mới");
        btn_addSubject.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btn_addSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addSubjectActionPerformed(evt);
            }
        });

        btn_updateSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_update_file_32px.png"))); // NOI18N
        btn_updateSubject.setText("Cập nhật");
        btn_updateSubject.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btn_updateSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateSubjectActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel39.setText("Hình thức đánh giá");

        cbo_hinhthuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chấm điểm", "Đánh giá" }));

        lbl_mamon.setText("auto generate");

        javax.swing.GroupLayout maintrangchuLayout = new javax.swing.GroupLayout(maintrangchu);
        maintrangchu.setLayout(maintrangchuLayout);
        maintrangchuLayout.setHorizontalGroup(
            maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(maintrangchuLayout.createSequentialGroup()
                .addGap(642, 642, 642)
                .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, maintrangchuLayout.createSequentialGroup()
                        .addGap(0, 277, Short.MAX_VALUE)
                        .addComponent(btn_updateSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49))
                    .addGroup(maintrangchuLayout.createSequentialGroup()
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(maintrangchuLayout.createSequentialGroup()
                                .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(48, 48, 48))
                            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_timkiem, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(lbl_tenMon, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(cbo_hinhthuc, 0, 266, Short.MAX_VALUE)
                            .addComponent(cbo_Khoi, 0, 266, Short.MAX_VALUE)
                            .addComponent(lbl_mamon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(40, 40, 40))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, maintrangchuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_reloadSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(178, 178, 178))
            .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(maintrangchuLayout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(572, Short.MAX_VALUE)))
            .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(maintrangchuLayout.createSequentialGroup()
                    .addGap(644, 644, 644)
                    .addComponent(btn_addSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(339, Short.MAX_VALUE)))
        );
        maintrangchuLayout.setVerticalGroup(
            maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(maintrangchuLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_mamon, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_tenMon, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_hinhthuc, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_Khoi, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addComponent(btn_updateSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addComponent(btn_reloadSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
            .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(maintrangchuLayout.createSequentialGroup()
                    .addGap(17, 17, 17)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(22, Short.MAX_VALUE)))
            .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, maintrangchuLayout.createSequentialGroup()
                    .addContainerGap(451, Short.MAX_VALUE)
                    .addComponent(btn_addSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(142, 142, 142)))
        );

        body.add(maintrangchu, "card6");

        getContentPane().add(body);
        body.setBounds(230, 90, 1130, 630);

        setSize(new java.awt.Dimension(1360, 722));
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

    private void lblquanlyhocsinhMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblquanlyhocsinhMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblquanlyhocsinhMouseEntered

    private void lblquanlyhocsinhMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblquanlyhocsinhMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblquanlyhocsinhMouseExited

    private void lblquanlyhocsinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblquanlyhocsinhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblquanlyhocsinhActionPerformed

    private void qlyhocsinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qlyhocsinhMouseClicked
        lane2.setBackground(new Color(119, 84, 248));
        lblquanlyhocsinh.setBackground(new Color(237, 234, 242));
        qlyhocsinh.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane3.setBackground(new Color(255, 255, 255));
        lblqlytintuc.setBackground(new Color(255, 255, 255));
        qlytintuc.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lbldslop.setBackground(new Color(255, 255, 255));
        dslop.setBackground(new Color(255, 255, 255));

        String tieude2 = "Quản lý học sinh";
        checktab(tieude2);

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(quanlyhocsinh);
        body.repaint();
        body.revalidate();


    }//GEN-LAST:event_qlyhocsinhMouseClicked

    private void trangchuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trangchuMouseClicked

        lane1.setBackground(new Color(119, 84, 248));
        lbltrangchu.setBackground(new Color(237, 234, 242));
        trangchu.setBackground(new Color(237, 234, 242));

        lane2.setBackground(new Color(255, 255, 255));
        lblquanlyhocsinh.setBackground(new Color(255, 255, 255));
        qlyhocsinh.setBackground(new Color(255, 255, 255));

        lane3.setBackground(new Color(255, 255, 255));
        lblqlytintuc.setBackground(new Color(255, 255, 255));
        qlytintuc.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lbldslop.setBackground(new Color(255, 255, 255));
        dslop.setBackground(new Color(255, 255, 255));

        String tieude2 = "Môn học";
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

    private void lblqlytintucMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblqlytintucMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblqlytintucMouseEntered

    private void lblqlytintucMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblqlytintucMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblqlytintucMouseExited

    private void lblqlytintucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblqlytintucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblqlytintucActionPerformed

    private void qlytintucMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qlytintucMouseClicked
        lane3.setBackground(new Color(119, 84, 248));
        lblqlytintuc.setBackground(new Color(237, 234, 242));
        qlytintuc.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane2.setBackground(new Color(255, 255, 255));
        lblquanlyhocsinh.setBackground(new Color(255, 255, 255));
        qlyhocsinh.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lbldslop.setBackground(new Color(255, 255, 255));
        dslop.setBackground(new Color(255, 255, 255));

        String tieude2 = "Quản lý tin tức";
        checktab(tieude2);

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(quanlytintuc);
        body.repaint();
        body.revalidate();
    }//GEN-LAST:event_qlytintucMouseClicked

    private void lbldslopMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbldslopMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lbldslopMouseEntered

    private void lbldslopMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbldslopMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lbldslopMouseExited

    private void lbldslopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbldslopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbldslopActionPerformed

    private void dslopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dslopMouseClicked
        lane4.setBackground(new Color(119, 84, 248));
        lbldslop.setBackground(new Color(237, 234, 242));
        dslop.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane2.setBackground(new Color(255, 255, 255));
        lblquanlyhocsinh.setBackground(new Color(255, 255, 255));
        qlyhocsinh.setBackground(new Color(255, 255, 255));

        lane3.setBackground(new Color(255, 255, 255));
        lblqlytintuc.setBackground(new Color(255, 255, 255));
        qlytintuc.setBackground(new Color(255, 255, 255));

        String tieude2 = "Danh sách lớp";
        checktab(tieude2);

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(danhsachlop);
        body.repaint();
        body.revalidate();


    }//GEN-LAST:event_dslopMouseClicked

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

    private void tblGridViewMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridViewMouseDragged

    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        int i = tblGridView.getSelectedRow();
        String m = (tblGridView.getValueAt(i, 0).toString());
        ResultSet rs = hsDAO.selectWithMaHS(m);
        try {
            if (rs.next()) {
                lbl_Mahocsinh.setText(rs.getString("mahocsinh"));
                suaHoten.setText(rs.getString("hoten"));
                if (rs.getString("gioitinh").equals("1")) {
                    cbbsuaGioitinh.setSelectedItem("Nam");
                } else {
                    cbbsuaGioitinh.setSelectedItem("Nữ");
                }
                jDateChooser5.setDate(rs.getDate("ngaysinh"));
                suaDanToc.setText(rs.getString("dantoc"));
                suaTonGiao.setText(rs.getString("tongiao"));
                suaNoiSinh.setText(rs.getString("noisinh"));
                suaCMND.setText(rs.getString("cmnd"));
                jDateChooser6.setDate(rs.getDate("ngayvaodoan"));
                suaSDT.setText(rs.getString("dienthoai"));
                if (rs.getBoolean("trangthai")==true) {
                    cbbSuaTrangThai.setSelectedItem("Đi học");
                } else {
                    cbbSuaTrangThai.setSelectedItem("Nghỉ học");
                }
                suaDiaChi.setText(rs.getString("diachi"));
                suaNguoiDamHo.setText(rs.getString("nguoidamho"));
                suaHotenBo.setText(rs.getString("hoten_bo"));
                suaSDTBo.setText(rs.getString("dienthoai_bo"));
                suaDVCTBo.setText(rs.getString("dv_cong_tac_bo"));
                suaHotenMe.setText(rs.getString("hoten_me"));
                suaSDTMe.setText(rs.getString("dienthoai_me"));
                suaDVCTMe.setText(rs.getString("dv_cong_tac_me"));
                suaAnh.setText(rs.getString("anh"));
                if (image != null) {
                    ImageIcon icon = new ImageIcon(rs.getString("anh"));
                    Image img = icon.getImage().getScaledInstance(110, 121, Image.SCALE_SMOOTH);
                    image.setIcon(new ImageIcon(img));
                } else {
                    image.setIcon(null);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        btn_ChinhSua.setEnabled(true);
        btnTaiAnh.setEnabled(true);
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_tblGridViewMouseClicked

    private void tblGridViewMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3) {
            if (evt.isPopupTrigger() && tblGridView.getSelectedRowCount() != 0) {
                PopUpMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tblGridViewMouseReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Object[] options = {"6A", "6B", "6C", "Option 4"};
        JComboBox optionList = new JComboBox(options);
        JOptionPane.showMessageDialog(null, optionList, "Chuyển lớp",
                JOptionPane.QUESTION_MESSAGE);


    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void btnTaiAnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaiAnhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTaiAnhActionPerformed

    private void btn_LuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LuuActionPerformed
        if (check()) {
            this.insert();
            this.loadCBB();
            this.NewForm();
        }


    }//GEN-LAST:event_btn_LuuActionPerformed

    private void btnThemMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMoiActionPerformed
        this.NewForm();
        ResultSet rs = hsDAO.select();
        List<HocSinh> list = hsDAO.findAll();
        try {
            if (list.isEmpty()) {
                lbl_Mahocsinh.setText(String.valueOf("hs" + 1));
            } else {
                while (rs.next()) {
                    int i = Integer.parseInt(rs.getString("max")) + 1;
                    lbl_Mahocsinh.setText(String.valueOf("hs" + i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        btn_Luu.setEnabled(true);
        btnTaiAnh.setEnabled(true);


    }//GEN-LAST:event_btnThemMoiActionPerformed

    private void tblGridView3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView3MouseClicked
        DefaultTableModel model = (DefaultTableModel) tblGridView3.getModel();
        int i = tblGridView3.getSelectedRow();
        String m = tblGridView3.getValueAt(i, 0).toString();
        ResultSet rs = lhDAO.selectWithMalop(m);
        ResultSet rs1 = lhDAO.countWithMalop(m);
        try {
            if (rs.next() && rs1.next()) {
                lbl_malop.setText(rs.getString("malop"));
                txtTenLop.setText(rs.getString("tenlop"));
                if (kDAO.selectByMaKhoi(UUID.fromString(rs.getString("khoi_makhoi"))).getTenKhoi().equals("Khối 6")) {
                    CbbKhoiHoc.setSelectedIndex(0);
                } else if (kDAO.selectByMaKhoi(UUID.fromString(rs.getString("khoi_makhoi"))).getTenKhoi().equals("Khối 7")) {
                    CbbKhoiHoc.setSelectedIndex(1);
                } else if (kDAO.selectByMaKhoi(UUID.fromString(rs.getString("khoi_makhoi"))).getTenKhoi().equals("Khối 8")) {
                    CbbKhoiHoc.setSelectedIndex(2);
                } else {
                    CbbKhoiHoc.setSelectedIndex(3);
                }
                txtSiSo.setText(rs1.getString("ss"));
                lbl_malop2.setText(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblGridView3MouseClicked

    private void tblGridView3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGridView3KeyPressed

    }//GEN-LAST:event_tblGridView3KeyPressed

    private void tblGridView3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGridView3KeyReleased

    }//GEN-LAST:event_tblGridView3KeyReleased

    private void btn_capNhat2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat2ActionPerformed
        if (check2()) {
            this.insert2();
            this.loadToTableLopHoc();
        }
    }//GEN-LAST:event_btn_capNhat2ActionPerformed

    private void btn_capNhat3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat3ActionPerformed
        if (check2()) {
            this.update2();
            this.loadToTableLopHoc();
        }
    }//GEN-LAST:event_btn_capNhat3ActionPerformed

    private void btn_capNhat4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_capNhat4ActionPerformed

    private void btn_capNhat5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat5ActionPerformed
        this.NewForm2();
        ResultSet rs = lhDAO.selectmax();
        List<LopHoc> list = lhDAO.select();
        try {
            if (list.isEmpty()) {
                lbl_malop.setText(String.valueOf("lop" + 1));
            } else {
                while (rs.next()) {
                    int i = Integer.parseInt(rs.getString("max")) + 1;
                    lbl_malop.setText(String.valueOf("lop" + i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_capNhat5ActionPerformed

    private void btn_capNhat9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat9ActionPerformed
        bodylop.removeAll();
        bodylop.repaint();
        bodylop.revalidate();
        bodylop.add(phancong);
        bodylop.repaint();
        bodylop.revalidate();

        bodythaotac.removeAll();
        bodythaotac.repaint();
        bodythaotac.revalidate();
        bodythaotac.add(thaotacpc);
        bodythaotac.repaint();
        bodythaotac.revalidate();
        this.loadCBBMonDay();
        this.LoadToTablePhanCong();
        this.loadCbogiaovienPC();
    }//GEN-LAST:event_btn_capNhat9ActionPerformed

    private void tblGridView4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView4MouseClicked
        DefaultTableModel model = (DefaultTableModel) tblGridView4.getModel();
        int i = tblGridView4.getSelectedRow();
        String m = (tblGridView4.getValueAt(i, 0).toString());
        String n = (tblGridView4.getValueAt(i, 3).toString());
        ResultSet rs = pcDAO.select4(m);
        ResultSet rs1 = mDAO.select2(n);
        try {
            if (rs.next() && rs1.next()) {
                lbl_maPc.setText(rs.getString("pc.mapc"));
                lbl_malop2.setText(rs.getString("pc.malop"));
                cbogiaovien.setSelectedItem(rs.getString("pc.magiaovien"));
                if (rs.getString("pc.vaitro").equals("1")) {
                    rdoChuNghiem.setSelected(true);
                } else {
                    rdoBoMon.setSelected(true);
                }
                cboMonDay.setSelectedItem(n);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblGridView4MouseClicked

    private void tblGridView4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGridView4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridView4KeyPressed

    private void tblGridView4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGridView4KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridView4KeyReleased

    private void btn_capNhat17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat17ActionPerformed
        if (checkPc()) {
            this.insert3();
            this.LoadToTablePhanCong();
        }
    }//GEN-LAST:event_btn_capNhat17ActionPerformed

    private void btn_capNhat18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat18ActionPerformed
        this.update3();
        this.LoadToTablePhanCong();
    }//GEN-LAST:event_btn_capNhat18ActionPerformed

    private void btn_capNhat19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat19ActionPerformed
        DefaultTableModel model = (DefaultTableModel) tblGridView4.getModel();
        int i = tblGridView4.getSelectedRow();
        String m = (tblGridView4.getValueAt(i, 0).toString());
        try {
            pcDAO.delete(m);
            this.LoadToTablePhanCong();
            this.NewForm3();
            JOptionPane.showMessageDialog(this, "Xoa thanh cong");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xoa that bai!!!");
        }


    }//GEN-LAST:event_btn_capNhat19ActionPerformed

    private void btn_capNhat20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat20ActionPerformed
        this.NewForm3();
    }//GEN-LAST:event_btn_capNhat20ActionPerformed

    private void btn_capNhat21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_capNhat21ActionPerformed

    private void btn_capNhat22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_capNhat22ActionPerformed

    private void btn_capNhat23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_capNhat23ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        bodylop.removeAll();
        bodylop.repaint();
        bodylop.revalidate();
        bodylop.add(banglop);
        bodylop.repaint();
        bodylop.revalidate();

        bodythaotac.removeAll();
        bodythaotac.repaint();
        bodythaotac.revalidate();
        bodythaotac.add(thaotaclop);
        bodythaotac.repaint();
        bodythaotac.revalidate();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbbLopHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbLopHocActionPerformed
        this.loadCBB();
    }//GEN-LAST:event_cbbLopHocActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        String ft = txtTimKiem.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
        tblGridView.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(ft));
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void CbbKhoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbbKhoiActionPerformed

        DefaultComboBoxModel model = (DefaultComboBoxModel) cbbLopHoc.getModel();

        if (model != null) {
            model.removeAllElements();
        }
        try {
            String tenKhoi = (String) CbbKhoi.getSelectedItem();
            ResultSet rs = lhDAO.selectWithTenKhoi(tenKhoi);
            while (rs.next()) {
                model.addElement(rs.getString("tenlop"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi truy van du lieu CBB");
            e.printStackTrace();
        }


    }//GEN-LAST:event_CbbKhoiActionPerformed

    private void cbo_hocKiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_hocKiActionPerformed
        this.loadCBB();
        this.LoadToTablePhanCong();
    }//GEN-LAST:event_cbo_hocKiActionPerformed

    private void cbbNamHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbNamHocActionPerformed
        this.loadCBB();
        this.loadToTableLopHoc();
        this.LoadToTablePhanCong();

    }//GEN-LAST:event_cbbNamHocActionPerformed

    private void suaSDTMeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaSDTMeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_suaSDTMeActionPerformed

    private void suaTonGiaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaTonGiaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_suaTonGiaoActionPerformed

    private void btn_ChinhSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ChinhSuaActionPerformed
        if (check()) {
            this.update();
            this.loadCBB();
            this.NewForm();
        }
    }//GEN-LAST:event_btn_ChinhSuaActionPerformed

    private void btnTaiAnhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTaiAnhMouseClicked

        JFileChooser open = new JFileChooser();
        open.setDialogTitle("Chọn Đường Dẫn Đến Ảnh Học Sinh");
        int file = open.showOpenDialog(null);
        if (file == JFileChooser.APPROVE_OPTION) {
            String[] ary = open.getCurrentDirectory().toString().split("\\\\");
            for(String w: ary){
                System.out.println(w);
            }
            suaAnh.setText(ary[0] + "\\" +ary[1] + "\\" +ary[2] + "\\" + ary[3] + "\\" + open.getSelectedFile().getName());
            ImageIcon icon = new ImageIcon(suaAnh.getText());
            Image img = icon.getImage().getScaledInstance(110, 121, Image.SCALE_SMOOTH);
            System.out.println(img);
            image.setIcon(new ImageIcon(img));
        } else {
//            lblStatus.setText("Chọn đường dẫn tới ảnh của linh kiện!!");
        }


    }//GEN-LAST:event_btnTaiAnhMouseClicked

    private void btn_capNhat5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_capNhat5MouseClicked

    }//GEN-LAST:event_btn_capNhat5MouseClicked

    private void cboMonDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMonDayActionPerformed

    }//GEN-LAST:event_cboMonDayActionPerformed

    private void Btn_Xuat_Excel_LopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_Xuat_Excel_LopActionPerformed
        try {
            jFileChooser1.setSelectedFile(new File("C:\\Danh sách Lớp khối " + CbbKhoiHoc.getSelectedItem().toString() + " kì " + cbo_hocKi.getSelectedItem().toString() + " năm " + cbbNamHoc.getSelectedItem().toString() + ".csv"));
            jFileChooser1.showSaveDialog(null);
            File file = jFileChooser1.getSelectedFile();
            if (file.exists()) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(this, "Bạn muốn ghi đè lên File đã có", "Hệ thống quản lý đào tạo", dialogButton);
                if (dialogResult == 0) {
                    System.out.println("starting write user.csv file: " + file.getPath());
                    fv.writeLopCsv(file.getPath(), (String) CbbKhoiHoc.getSelectedItem(), (String) cbbNamHoc.getSelectedItem(), (String) cbo_hocKi.getSelectedItem());
                    DialogHelper.alert(this, "Xuất Excel thành công");
                } else {
                    System.out.println("No Option");
                }
            } else {
                System.out.println("starting write file: " + file.getPath());
                fv.writeLopCsv(file.getPath(), (String) CbbKhoiHoc.getSelectedItem(), (String) cbbNamHoc.getSelectedItem(), (String) cbo_hocKi.getSelectedItem());
                DialogHelper.alert(this, "Xuất Excel thành công");
            }
        } catch (Exception ex) {
            DialogHelper.alert(this, "Xuất Excel không thành công");
        }
    }//GEN-LAST:event_Btn_Xuat_Excel_LopActionPerformed

    private void Btn_Xuat_Excel_HSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_Xuat_Excel_HSActionPerformed
        try {
            jFileChooser1.setSelectedFile(new File("C:\\Danh sách học sinh Lớp " + cbbLopHoc.getSelectedItem().toString() + " năm " + cbbNamHoc.getSelectedItem().toString() + " kì " + cbo_hocKi.getSelectedItem().toString() + ".csv"));
            jFileChooser1.showSaveDialog(null);
            File file = jFileChooser1.getSelectedFile();
            if (file.exists()) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(this, "Bạn muốn ghi đè lên File đã có", "Hệ thống quản lý đào tạo", dialogButton);
                if (dialogResult == 0) {
                    System.out.println("starting write user.csv file: " + file.getPath());
                    fv.writeHSCsv(file.getPath(), (String) cbbLopHoc.getSelectedItem(), (String) cbbNamHoc.getSelectedItem());
                    DialogHelper.alert(this, "Xuất Excel thành công");
                } else {
                    System.out.println("No Option");
                }
            } else {
                System.out.println("starting write file: " + file.getPath());
                fv.writeHSCsv(file.getPath(), (String) cbbLopHoc.getSelectedItem(), (String) cbbNamHoc.getSelectedItem());
                DialogHelper.alert(this, "Xuất Excel thành công");
            }
        } catch (Exception ex) {
            DialogHelper.alert(this, "Xuất Excel không thành công");
        }
    }//GEN-LAST:event_Btn_Xuat_Excel_HSActionPerformed

    private void btn_updateSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateSubjectActionPerformed
        try {
            this.UpdateSubject();
            DialogHelper.alert(this, "cập nhật thành công");
            this.clear();
        } catch (Exception e) {
            DialogHelper.alert(this, "cập nhật không thành công");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_updateSubjectActionPerformed

    private void btn_addSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addSubjectActionPerformed
        try {
            this.addSubject();
            DialogHelper.alert(this, "thêm môn mới thành công");
            this.clear();
        } catch (Exception e) {
            DialogHelper.alert(this, "thêm môn mới không thành công");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_addSubjectActionPerformed

    private void btn_reloadSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reloadSubjectActionPerformed
        lbl_timkiem.setText("");
        lbl_mamon.setText("");
        lbl_tenMon.setText("");
    }//GEN-LAST:event_btn_reloadSubjectActionPerformed

    private void tbl_DanhSachMonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DanhSachMonMouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3) {
            if (evt.isPopupTrigger() && tbl_DanhSachMon.getSelectedRowCount() != 0) {
                PopUpMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tbl_DanhSachMonMouseReleased

    private void tbl_DanhSachMonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DanhSachMonMouseClicked
        this.tblSubjectClick();
    }//GEN-LAST:event_tbl_DanhSachMonMouseClicked

    private void tbl_DanhSachMonMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DanhSachMonMouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_DanhSachMonMouseDragged

    private void cbo_KhoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_KhoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_KhoiActionPerformed

    private void CbbKhoiHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbbKhoiHocActionPerformed

    }//GEN-LAST:event_CbbKhoiHocActionPerformed

    private void cbogiaovienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbogiaovienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbogiaovienActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void Nhap_excel_hsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Nhap_excel_hsActionPerformed
        try {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("File csv", "csv");
            jFileChooser1.setFileFilter(filter);
            jFileChooser1.showSaveDialog(null);
            File file = jFileChooser1.getSelectedFile();
            if (file.exists()) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(this, "Bạn muốn nhập học sinh" , "Hệ thống quản lý đào tạo", dialogButton);
                if (dialogResult == 0) {
                    System.out.println("starting write user.csv file: " + file.getPath());
                    fv.readHSCsv(file.getPath());
                    DialogHelper.alert(this, "Nhập Excel thành công");
                } else {
                    System.out.println("No Option");
                }
            } else {
                System.out.println("starting write file: " + file.getPath());
                fv.readHSCsv(file.getPath());
                DialogHelper.alert(this, "Nhập Excel thành công");
            }
        } catch (Exception ex) {
            DialogHelper.alert(this, "Nhập Excel không thành công");
        }
    }//GEN-LAST:event_Nhap_excel_hsActionPerformed

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
            java.util.logging.Logger.getLogger(TrangChuHC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TrangChuHC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TrangChuHC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrangChuHC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new TrangChuHC().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_Xuat_Excel_HS;
    private javax.swing.JButton Btn_Xuat_Excel_Lop;
    private javax.swing.JComboBox<String> CbbKhoi;
    private javax.swing.JComboBox<String> CbbKhoiHoc;
    private javax.swing.JButton Nhap_excel_hs;
    private javax.swing.JPopupMenu PopUpMenu;
    private javax.swing.JScrollPane banglop;
    private javax.swing.JScrollPane banglop1;
    private javax.swing.JPanel body;
    private javax.swing.JPanel bodylop;
    private javax.swing.JPanel bodythaotac;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnMinimize;
    private javax.swing.JButton btnMinimize4;
    private javax.swing.JButton btnTaiAnh;
    private javax.swing.JButton btnThemMoi;
    private javax.swing.JButton btn_ChinhSua;
    private javax.swing.JButton btn_Luu;
    private javax.swing.JButton btn_addSubject;
    private javax.swing.JButton btn_capNhat17;
    private javax.swing.JButton btn_capNhat18;
    private javax.swing.JButton btn_capNhat19;
    private javax.swing.JButton btn_capNhat2;
    private javax.swing.JButton btn_capNhat20;
    private javax.swing.JButton btn_capNhat21;
    private javax.swing.JButton btn_capNhat22;
    private javax.swing.JButton btn_capNhat23;
    private javax.swing.JButton btn_capNhat3;
    private javax.swing.JButton btn_capNhat4;
    private javax.swing.JButton btn_capNhat5;
    private javax.swing.JButton btn_capNhat9;
    private javax.swing.JButton btn_reloadSubject;
    private javax.swing.JButton btn_updateSubject;
    private javax.swing.JButton btntab1;
    private javax.swing.JButton btntab2;
    private javax.swing.JButton btntab3;
    private javax.swing.JButton btntab4;
    private javax.swing.JButton btntab5;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbbLopHoc;
    private javax.swing.JComboBox<String> cbbNamHoc;
    private javax.swing.JComboBox<String> cbbSuaTrangThai;
    private javax.swing.JComboBox<String> cbbsuaGioitinh;
    private javax.swing.JComboBox<String> cboMonDay;
    private javax.swing.JComboBox<String> cbo_Khoi;
    private javax.swing.JComboBox<String> cbo_hinhthuc;
    private javax.swing.JComboBox<String> cbo_hocKi;
    private javax.swing.JComboBox<String> cbogiaovien;
    private javax.swing.JPanel danhsachlop;
    private javax.swing.JPanel dslop;
    private javax.swing.JLabel image;
    private javax.swing.JButton jButton1;
    private com.toedter.calendar.JDateChooser jDateChooser5;
    private com.toedter.calendar.JDateChooser jDateChooser6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel102;
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
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
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
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lane1;
    private javax.swing.JLabel lane2;
    private javax.swing.JLabel lane3;
    private javax.swing.JLabel lane4;
    private javax.swing.JLabel lbl_Gv;
    private javax.swing.JLabel lbl_Mahocsinh;
    private javax.swing.JLabel lbl_maPc;
    private javax.swing.JLabel lbl_malop;
    private javax.swing.JLabel lbl_malop2;
    private javax.swing.JLabel lbl_mamon;
    private javax.swing.JTextField lbl_tenMon;
    private javax.swing.JTextField lbl_timkiem;
    private javax.swing.JButton lbldslop;
    private javax.swing.JButton lblqlytintuc;
    private javax.swing.JButton lblquanlyhocsinh;
    private javax.swing.JLabel lbltab1;
    private javax.swing.JLabel lbltab2;
    private javax.swing.JLabel lbltab3;
    private javax.swing.JLabel lbltab4;
    private javax.swing.JLabel lbltab5;
    private javax.swing.JButton lbltrangchu;
    private javax.swing.JPanel maintrangchu;
    private javax.swing.JPanel phancong;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JLabel qlnamhoc;
    private javax.swing.JPanel qlyhocsinh;
    private javax.swing.JPanel qlytintuc;
    private javax.swing.JPanel quanlyhocsinh;
    private javax.swing.JPanel quanlytintuc;
    private javax.swing.JRadioButton rdoBoMon;
    private javax.swing.JRadioButton rdoChuNghiem;
    private javax.swing.JTextField suaAnh;
    private javax.swing.JTextField suaCMND;
    private javax.swing.JTextArea suaDVCTBo;
    private javax.swing.JTextArea suaDVCTMe;
    private javax.swing.JTextField suaDanToc;
    private javax.swing.JTextArea suaDiaChi;
    private javax.swing.JTextField suaHoten;
    private javax.swing.JTextField suaHotenBo;
    private javax.swing.JTextField suaHotenMe;
    private javax.swing.JTextArea suaNguoiDamHo;
    private javax.swing.JTextField suaNoiSinh;
    private javax.swing.JTextField suaSDT;
    private javax.swing.JTextField suaSDTBo;
    private javax.swing.JTextField suaSDTMe;
    private javax.swing.JTextField suaTonGiao;
    private javax.swing.JPanel tab1;
    private javax.swing.JPanel tab2;
    private javax.swing.JPanel tab3;
    private javax.swing.JPanel tab4;
    private javax.swing.JPanel tab5;
    private javax.swing.JPanel tanktab;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTable tblGridView3;
    private javax.swing.JTable tblGridView4;
    private javax.swing.JTable tbl_DanhSachMon;
    private javax.swing.JTabbedPane thaotaclop;
    private javax.swing.JTabbedPane thaotacpc;
    private javax.swing.JPanel trangchinh;
    private javax.swing.JPanel trangchu;
    private javax.swing.JTextField txtMagiaoVien;
    private javax.swing.JTextField txtSiSo;
    private javax.swing.JTextField txtTenLop;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables

    private void loadCbogiaovienPC() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbogiaovien.getModel();
        if (model != null) {
            model.removeAllElements();
        }
        try {
            List<GiaoVien> list = gvDAO.select2();
            for (GiaoVien giaoVien : list) {
                model.addElement(giaoVien.getMaGV() + "-" + giaoVien.getHoTen());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.BDH;

import AppPackage.AnimationClass;
import da.dao.GiaoVienDAO;
import da.dao.HocSinhDAO;
import da.dao.KhoiDAO;
import da.dao.LopHocDAO;
import da.dao.NamHocDAO;
import da.dao.TaiKhoanDAO;
import da.helper.BCrypt;
import da.helper.DialogHelper;
import da.helper.ShareHelper;
import da.helper.write_PDF;
import da.model.GiaoVien;
import da.model.HocSinh;
import da.model.Khoi;
import da.model.NamHoc;
import da.model.TaiKhoan;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Frame;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import view.GV.TrangChuGV;
import view.dangnhap;

/**
 *
 * @author Rahmans
 */
public class TrangChuBDH extends javax.swing.JFrame {

    static boolean maximized = true;
    int xMouse;
    int yMouse;
    String head[] = {"Email", "Chức Vụ", "Mã Giáo Viên"};
    String head2[] = {"Email", "Chức Vụ", "Mã Học Sinh"};
    String head1[] = {"Mã giáo viên", "Họ tên", "Ngày Sinh", "Giới tính", "CMND", "Địa chỉ"};
    String headtblhocba[] = {"Mã học sinh", "Họ tên", "Ngày Sinh", "Giới tính", "CMND"};
    DefaultTableModel model = new DefaultTableModel(head, 0);
    DefaultTableModel model1 = new DefaultTableModel(head1, 0);
    DefaultTableModel model2 = new DefaultTableModel(head2, 0);
    DefaultTableModel modelhocba = new DefaultTableModel(headtblhocba, 0);
    TaiKhoanDAO tkDAO = new TaiKhoanDAO();
    NamHocDAO nhDAO = new NamHocDAO();
    GiaoVienDAO gvdao = new GiaoVienDAO();
    HocSinhDAO hsDAO = new HocSinhDAO();
    KhoiDAO kDAO = new KhoiDAO();
    LopHocDAO lhDAO = new LopHocDAO();
    write_PDF pdf = new write_PDF();

    public TrangChuBDH() {
        initComponents();
        jLabel100.setText(ShareHelper.TaiKhoan.getHoten());
        this.LoadToTableTKGV();
        this.LoadToTableTKHS();
        txtHocSinh.setEnabled(false);
        this.loadtoCBBNamHoc();
        this.loadToCbbKhoi();
        this.loadToGV();
        btnCapNhat.setEnabled(false);
        btnLuu.setEnabled(false);
    }
    
    public int checkNamHoc(){
        try {
            int i = 0;
            List<NamHoc> namHoc = nhDAO.checkTrangThai();
            i = namHoc.size();
            return i;
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi checkDATA");
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean compareDate(){
        java.util.Date date1 = txt_ketthuc.getDate();
        System.out.println(date1);
        java.sql.Date date2 = java.sql.Date.valueOf(LocalDate.now());
        System.out.println(date2);
        String relation;
        if (date1.equals(date2)){
          relation = "Hai ngày trùng nhau";
          return true;
        }else if (date1.before(date2)){ // Hoặc  else if (date1.after(date2)== false)
          relation = " Trước";
          return false;
        }else
          relation = " Sau";
        System.out.println(date1 + relation + ' ' + date2);
        return false;

    }
    
    private static PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Học sinh giỏi", new Double(200));
        dataset.setValue("Học sinh khá", new Double(560));
        dataset.setValue("Học sinh trung bình", new Double(640));
        dataset.setValue("Các loại khác", new Double(30));
        return dataset;
    }

    private static JFreeChart createPieChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Thành tích học tập năm học 2018-2019".toUpperCase(),
                dataset, true, true, true);
        return chart;
    }

    public void chart() {
        JFreeChart pieChart = createPieChart(createDataset());
        ChartPanel chartPanel = new ChartPanel(pieChart);
        JFrame frame = new JFrame();
        frame.add(chartPanel);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    public Boolean checkemail(String email){
        List<GiaoVien> giaoViens = gvdao.getall();
        for(GiaoVien gv:giaoViens){
            if(email.equals(gv.getEmail()) ){
                DialogHelper.alert(null,"không được trùng email!");
                return false;
            }
        }
        return true;
    }

    //load hoc sinh hoc ba
    public void loadhocsinhhocba() {
        DefaultTableModel model = (DefaultTableModel) tbl_hocba.getModel();
        model.setRowCount(0);
        try {
            String nienHoc = (String) cbo_hocba_nienhoc.getSelectedItem();
            String lop = (String) cbo_hocba_lop.getSelectedItem();
            List<HocSinh> list = hsDAO.findByTenLopandNienhoc(lop, nienHoc);
            for (HocSinh nh : list) {
                Object[] row = {
                    nh.getMaHS(),
                    nh.getHoTen(),
                    nh.getGioiTinh() == true ? "Nam" : "Nữ",
                    nh.getNgaySinh(),
                    nh.getCmND()};
                model.addRow(row);
            }
            tbl_hocba.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //load lop hoc hocba
    public void loadToTableLopHoc() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbo_hocba_lop.getModel();
        if (model != null) {
            model.removeAllElements();
        }
        try {
            String nienHoc = (String) cbo_hocba_nienhoc.getSelectedItem();
            String khoi = (String) cbo_hocba_khoi.getSelectedItem();
            ResultSet rs = lhDAO.selectLopByNienhocandtenKhoi(nienHoc, khoi);
            while (rs.next()) {
                model.addElement(rs.getString("tenlop"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadToCbbKhoi() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbo_hocba_khoi.getModel();
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

    private void loadtablenamhoc() {
        DefaultTableModel model = (DefaultTableModel) tblnamhoc.getModel();
        model.setRowCount(0);
        try {

            List<NamHoc> list = nhDAO.select();
            for (NamHoc nh : list) {
                Object[] row = {
                    nh.getNienHoc(),
                    nh.getNgayBD(),
                    nh.getNgayKT(),
                    nh.getTrangThai() == true ? "Đang học" : "Kết thúc",};
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Load bảng giáo viên
    public void loadToGV() {
        DefaultTableModel model = (DefaultTableModel) tblGridView1.getModel();
        model1.setRowCount(0);
        try {
            ResultSet rs = gvdao.select();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getString("magiaovien"));
                row.add(rs.getString("hoten"));
                row.add(rs.getString("ngaysinh"));

                if (rs.getBoolean("gioitinh") == true) {
                    row.add("Nam");
                } else {
                    row.add("Nữ");
                }

                row.add(rs.getString("cmnd"));
                row.add(rs.getString("diachi"));
                model1.addRow(row);
            }
            tblGridView1.setModel(model1);
        } catch (Exception e) {
        }

    }

//Xóa form danh sách giáo viên
    public void NewForGV() {
        txtHotenGV.setText("");
        txtHotenGV.setText("");
        ((JTextField) jDateChooser5.getDateEditor().getUiComponent()).setText("");
        txtEmail.setText("");
        txtDienThoaiGV.setText("");
        txtCMNDGV.setText("");
        txtDiaChiGV.setText("");
    }

//Thêm mới học sinh
    GiaoVien getModel1() {
        GiaoVien model = new GiaoVien();
        model.setMaGV(lblMagv.getText());
        model.setHoTen(txtHotenGV.getText());
        model.setGioiTinh(cbbsuaGioitinh.getSelectedIndex() == 0);
        model.setEmail(txtEMailGV.getText());
        model.setNgaySinh(jDateChooser5.getDate());
        model.setDienThoai(txtDienThoaiGV.getText());
        model.setCmnd(txtCMNDGV.getText());
        model.setDiaChi(txtDiaChiGV.getText());
        return model;
    }

//    gen 1 account
    TaiKhoan genaccount() {
        TaiKhoan model = new TaiKhoan();
        model.setStt(UUID.randomUUID());
        model.setEmail(txtEMailGV.getText());
        model.setPassWord(BCrypt.hashpw("123", BCrypt.gensalt(12)));
        model.setRole("GV");
        model.setGiaovien_id(gvdao.findBymagiaovien(lblMagv.getText()).getId());
        return model;
    }

    //Check giáo viên 
    public boolean checkGV() {
        String ngaysinh = ((JTextField) jDateChooser5.getDateEditor().getUiComponent()).getText();
        if (txtHotenGV.getText().equals("")) {
            DialogHelper.alert(this, "Họ tên không được bỏ trống!");
            return false;
        } else if (txtHotenGV.getText().length() > 50) {
            DialogHelper.alert(this, "Họ tên không được lớn hơn 50 ký tự!");
            return false;
        } else if (ngaysinh.equals("")) {
            DialogHelper.alert(this, "Ngày sinh không đươc để trống");
            return false;
        } else if (txtDienThoaiGV.getText().equals("")) {
            DialogHelper.alert(this, "Số điện thoại không được để trống");
            return false;
        } else if (!txtDienThoaiGV.getText().matches("[0-9]+")) {
            DialogHelper.alert(this, "Số điện thoại chỉ nhập số");
            return false;
        } else if (txtDienThoaiGV.getText().length() < 10 || txtDienThoaiGV.getText().length() > 12) {
            DialogHelper.alert(this, "Số điện thoại phải nhập đủ 10 hoặc 11 số.");
            return false;
        } else if (txtCMNDGV.getText().equals("")) {
            DialogHelper.alert(this, "CMND không được để trống");
            return false;
        } else if (!txtCMNDGV.getText().matches("[0-9]+")) {
            DialogHelper.alert(this, "CMND chỉ nhập số");
            return false;
        } else if (txtCMNDGV.getText().length() > 12) {
            DialogHelper.alert(this, "CMND không được lớn hơn 12 ký tự!");
            return false;
        } else if (txtDiaChiGV.getText().equals("")) {
            DialogHelper.alert(this, "Địa chỉ không được để trống");
            return false;
        } else if (txtDiaChiGV.getText().length() > 50) {
            DialogHelper.alert(this, "Địa chỉ không được lớn hơn 50 ký tự!");
            return false;
        }

        return true;
    }

    ///update giao vien
    public void updateGV() {
        GiaoVien model = getModel1();
        try {
            gvdao.update(model);

            DialogHelper.alert(this, "Chỉnh sửa thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Chỉnh sửa thất bại!");
            e.printStackTrace();
        }
    }
    ////

    void insert() {
        GiaoVien model = getModel1();
        try {
            
                gvdao.insert(model);
                DialogHelper.alert(this, "Thêm mới giáo viên thành công!");
               
        } catch (Exception e) {
            DialogHelper.alert(this, "Thêm mới giáo viên thất bại!");
            e.printStackTrace();
        }
    }

    private void taomoinamhoc() {
        NamHoc model = getModel();
        try {
            nhDAO.insert(model);
            DialogHelper.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật thất bại!");
            e.printStackTrace();
        }
    }

    private NamHoc getModel() {
        NamHoc model = new NamHoc();
        model.setNienHoc(txt_nienhoc.getText());
        model.setNgayBD(txt_ngaybatdau.getDate());
        model.setNgayKT(txt_ketthuc.getDate());
        return model;
    }

    public TaiKhoan getModelGV() {
        TaiKhoan model = new TaiKhoan();
        model.setEmail(txtEmail.getText());
        if (cboChucVu.getSelectedIndex() == 0) {
            model.setRole("BGH");
        } else if (cboChucVu.getSelectedIndex() == 1) {
            model.setRole("DT");
        } else if (cboChucVu.getSelectedIndex() == 2) {
            model.setRole("GV");
        } else {
            model.setRole("HS");
        }
        model.setPassWord(BCrypt.hashpw("123", BCrypt.gensalt(12)));
        if (model.getRole().equals("HS")) {
            HocSinh hocSinh = hsDAO.select3(txtHocSinh.getText());
            model.setHocsinh_id(hocSinh.getiD());
            model.setGiaovien_id(null);
        } else {
            GiaoVien giaovien = gvdao.findBymagiaovien(txtMaGv.getText());
            model.setGiaovien_id(giaovien.getId());
            model.setHocsinh_id(null);
        }
        return model;
    }

    public TaiKhoan getModel2() {
        TaiKhoan model = tkDAO.findById(tblGridView.getValueAt(tblGridView.getSelectedRow(), 0).toString());
        model.setEmail(txtEmail.getText());
        if (cboChucVu.getSelectedIndex() == 0) {
            model.setRole("BGH");
        } else if (cboChucVu.getSelectedIndex() == 1) {
            model.setRole("DT");
        } else if (cboChucVu.getSelectedIndex() == 2) {
            model.setRole("GV");
        } else {
            model.setRole("HS");
        }
        return model;
    }

    public TaiKhoan getModel3() {
        cboChucVu.setSelectedItem("Học Sinh");
        cboChucVu.enable(false);
        TaiKhoan model = tkDAO.findById(tblGridView2.getValueAt(tblGridView2.getSelectedRow(), 0).toString());
        model.setRole("HS");
        model.setEmail(txtEmail.getText());
        return model;
    }

    public void insertGV() {
        TaiKhoan model = getModelGV();
        try {
            tkDAO.insertTKGV(model);
            DialogHelper.alert(this, "Thêm mới thành công!");

        } catch (Exception e) {
            DialogHelper.alert(this, "Thêm mới thất bại");
            e.printStackTrace();

        }
    }

    public void insertHS() {
        TaiKhoan model = getModelGV();
        try {
            tkDAO.insertTKHS(model);
            DialogHelper.alert(this, "Thêm mới thành công!");

        } catch (Exception e) {
            DialogHelper.alert(this, "Thêm mới thất bại");
            e.printStackTrace();

        }
    }

    public void update() {
        TaiKhoan model = getModel2();
        try {
            tkDAO.update3(model);
            DialogHelper.alert(this, "Chỉnh sửa thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Chỉnh sửa thất bại!");
            e.printStackTrace();
        }
    }

    public void updateHS() {
        TaiKhoan model = getModel3();
        try {
            tkDAO.update3(model);
            DialogHelper.alert(this, "Chỉnh sửa thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Chỉnh sửa thất bại!");
            e.printStackTrace();
        }
    }

    public void deleteTK() {
        int i = tblGridView.getSelectedRow();
        String m = (tblGridView.getValueAt(i, 0).toString());
        try {
            tkDAO.delete(m);
            this.LoadToTableTKGV();
            this.NewForm();
            JOptionPane.showMessageDialog(this, "Xoa thanh cong");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xoa that bai!!!");
        }
    }

    public void deleteTKHS() {
        int i = tblGridView2.getSelectedRow();
        String m = (tblGridView2.getValueAt(i, 0).toString());
        try {
            tkDAO.delete(m);
            this.LoadToTableTKHS();
            this.NewForm();
            JOptionPane.showMessageDialog(this, "Xoa tai khoan hoc sinh thanh cong");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xoa that bai!!!");
        }
    }

    void resetform() {
        txtTimKiem.setText(null);
        ((JTextField) txt_ketthuc.getDateEditor().getUiComponent()).setText(null);
        ((JTextField) txt_ngaybatdau.getDateEditor().getUiComponent()).setText(null);
        txt_nienhoc.setText(null);
    }

    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean verifyEmail(String email) {
        if (email == null) {
            return false;
        }
        return email.matches(EMAIL_PATTERN);
    }

    public boolean checktk() {
//        List<GiaoVien> list = gvdao.select2();
//        for (GiaoVien ch : list) {
//            if (txtEmail.getText().matches(ch.get())) {
//                JOptionPane.showMessageDialog(this, "Tài khoản đã tồn tại");
//                return false;
//            }
//        }
        if (txtEmail.getText().equals("")) {
            DialogHelper.alert(this, "Email không được để trống");
            return false;
        } else if (txtMaGv.getText().equals("")) {
            DialogHelper.alert(this, "Mã giáo viên không được để trống");
            return false;
        } else if (verifyEmail(txtEmail.getText()) == false) {
            DialogHelper.alert(this, "Định dạng email bạn nhập không chính xác");
            return false;
        } else if (txtMaGv.getText().length() > 10) {
            DialogHelper.alert(this, "Mã giáo viên không được quá 10 kí tự");
            return false;
        } else {
            return true;
        }

    }

    public boolean checktkHS() {
//        List<GiaoVien> list = gvdao.select2();
//        for (GiaoVien ch : list) {
//            if (txtEmail.getText().matches(ch.get())) {
//                JOptionPane.showMessageDialog(this, "Tài khoản đã tồn tại");
//                return false;
//            }
//        }
        if (txtEmail.getText().equals("")) {
            DialogHelper.alert(this, "Email không được để trống");
            return false;
        } else if (txtHocSinh.getText().equals("")) {
            DialogHelper.alert(this, "Mã học sinh không được để trống");
            return false;
        } else if (verifyEmail(txtEmail.getText()) == false) {
            DialogHelper.alert(this, "Định dạng email bạn nhập không chính xác");
            return false;
        } else if (txtHocSinh.getText().length() > 10) {
            DialogHelper.alert(this, "Mã giáo viên không được quá 10 kí tự");
            return false;
        } else {
            return true;
        }

    }

    public void LoadToTableTKGV() {
        model.setRowCount(0);
        try {
            ResultSet rs = tkDAO.select2();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getString("email"));
                if (rs.getString("roles").equals("BGH")) {
                    row.add("Ban Giám Hiệu");
                } else if (rs.getString("roles").equals("DT")) {
                    row.add("Đào Tạo");
                } else {
                    row.add("Giáo Viên");
                }
                row.add(rs.getString("magiaovien"));
                model.addRow(row);

            }
            tblGridView.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadToTableTKHS() {
        model2.setRowCount(0);
        try {
            ResultSet rs = tkDAO.select4();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getString("email"));
                if (rs.getString("roles").equals("HS")) {
                    row.add("Học Sinh");
                }
                row.add(rs.getString("mahocsinh"));
                model2.addRow(row);
            }
            tblGridView2.setModel(model2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadtoCBBNamHoc() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbbNamHoc.getModel();
        DefaultComboBoxModel model_hocba = (DefaultComboBoxModel) cbo_hocba_nienhoc.getModel();
        if (model != null) {
            model.removeAllElements();
        }
        if (model_hocba != null) {
            model_hocba.removeAllElements();
        }
        try {
            List<NamHoc> list = nhDAO.select();
            for (NamHoc nh : list) {
                model.addElement(nh.getNienHoc());
                model_hocba.addElement(nh.getNienHoc());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi truy van du lieu CBB");
            e.printStackTrace();
        }
    }

    public void NewForm() {
        txtEmail.setText("");
        txtMaGv.setText("");
        txtHocSinh.setText("");
        cboChucVu.setSelectedIndex(0);

    }

//    TaiKhoan getModel() {
//        TaiKhoan model = new TaiKhoan();
//
//        return model;
//    }
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

    private void ktnamhoc() {
        NamHoc model = getModel();
        nhDAO.ketthuc(nhDAO.findByNienHoc(model.getNienHoc()).getMaNamHoc());
        this.loadtablenamhoc();
        this.resetform();
        JOptionPane.showMessageDialog(this, "Kết thúc thành công");
    }

    public boolean checkformnh() {
        String ngaybd = ((JTextField) txt_ngaybatdau.getDateEditor().getUiComponent()).getText();
        String ngaykt = ((JTextField) txt_ketthuc.getDateEditor().getUiComponent()).getText();
        if (txt_nienhoc.getText().equals("")) {
            DialogHelper.alert(this, "Vui lòng kiểm tra lại!");
            return false;
        } else if (ngaybd.equals("")) {
            DialogHelper.alert(this, "Vui lòng kiểm tra lại!");
            return false;
        } else if (ngaykt.equals("")) {
            DialogHelper.alert(this, "Vui lòng kiểm tra lại!");
            return false;
        } else {
            return true;
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
            Logger.getLogger(TrangChuBDH.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void clicktab(String tentab) {
        if (tentab.equals("Quản lý tài khoản")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(quanlytaikhoan);
            body.repaint();
            body.revalidate();
        } else if (tentab.equals("Thông tin năm học")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(quanlynamhoc);
            body.repaint();
            this.loadtablenamhoc();
            body.revalidate();
        } else if (tentab.equals("Thông tin học bạ")) {
            body.removeAll();
            body.repaint();
            body.revalidate();
            body.add(quanlyhocba);
            body.repaint();
            body.revalidate();
        } else if (tentab.equals("Danh sách giáo viên")) {
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
        pnlHeader = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnMinimize = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnMinimize4 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        cbbNamHoc = new javax.swing.JComboBox<>();
        jComboBox7 = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        trangchu = new javax.swing.JPanel();
        lane1 = new javax.swing.JLabel();
        lbltrangchu = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        qlyhocba = new javax.swing.JPanel();
        lane2 = new javax.swing.JLabel();
        lblquanlyhocsinh = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        qlytaikhoan = new javax.swing.JPanel();
        lane3 = new javax.swing.JLabel();
        lblquanlytaikhoan = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        qlynamhoc = new javax.swing.JPanel();
        lane4 = new javax.swing.JLabel();
        lblquanlynamhoc = new javax.swing.JButton();
        qlnamhoc = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
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
        quanlytaikhoan = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtHocSinh = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        cboChucVu = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGridView = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGridView2 = new javax.swing.JTable();
        txtMaGv = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        quanlynamhoc = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblnamhoc = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txt_nienhoc = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        txt_ngaybatdau = new com.toedter.calendar.JDateChooser();
        txt_ketthuc = new com.toedter.calendar.JDateChooser();
        quanlyhocba = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        lbl_hocba_hoten = new javax.swing.JLabel();
        lbl_hocba_gioitinh = new javax.swing.JLabel();
        lbl_hocba_mahs = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        lbl_hocba_ngaysinh = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        lbl_hocba_cmnd = new javax.swing.JLabel();
        btn_capNhat9 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txt_searchdiem = new javax.swing.JTextField();
        cbo_hocba_nienhoc = new javax.swing.JComboBox<>();
        cbo_hocba_khoi = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        cbo_hocba_lop = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        banglop = new javax.swing.JScrollPane();
        tbl_hocba = new javax.swing.JTable();
        btn_capNhat10 = new javax.swing.JButton();
        maintrangchu = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblGridView1 = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        txtTimKiemGV = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        lblMagv = new javax.swing.JLabel();
        txtHotenGV = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        cbbsuaGioitinh = new javax.swing.JComboBox<>();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        jLabel60 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtDienThoaiGV = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtCMNDGV = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtDiaChiGV = new javax.swing.JTextArea();
        jLabel72 = new javax.swing.JLabel();
        btnThemMOi = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        lblEMail = new javax.swing.JLabel();
        txtEMailGV = new javax.swing.JTextField();

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-cancel-delete-32.png"))); // NOI18N
        jMenuItem1.setText("Thôi việc");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        PopUpMenu.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-male-user-32_1.png"))); // NOI18N
        jMenuItem2.setText("Thêm tài khoản");
        jMenuItem2.setActionCommand("Quản lý tài khoản");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        PopUpMenu.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ban Giám Hiệu");
        setBackground(new java.awt.Color(244, 248, 251));
        setUndecorated(true);
        setSize(new java.awt.Dimension(1000, 600));

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

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel26.setText("Niên học :");

        cbbNamHoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbNamHoc.setBorder(null);

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Học kỳ 1", "Học kỳ 2" }));
        jComboBox7.setBorder(null);

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel27.setText("Học kì :");

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHeaderLayout.createSequentialGroup()
                .addComponent(btnMinimize4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(376, 376, 376)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbbNamHoc, 0, 160, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox7, 0, 160, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbbNamHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnExit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(btnMinimize, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.Alignment.LEADING))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
        lbltrangchu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-male-user-32.png"))); // NOI18N
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
        jLabel2.setText("Danh sách giáo viên");

        javax.swing.GroupLayout trangchuLayout = new javax.swing.GroupLayout(trangchu);
        trangchu.setLayout(trangchuLayout);
        trangchuLayout.setHorizontalGroup(
            trangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(trangchuLayout.createSequentialGroup()
                .addComponent(lane1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbltrangchu, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        trangchuLayout.setVerticalGroup(
            trangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbltrangchu, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        qlyhocba.setBackground(new java.awt.Color(255, 255, 255));
        qlyhocba.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qlyhocbaMouseClicked(evt);
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
        jLabel3.setText("Thông tin học bạ");

        javax.swing.GroupLayout qlyhocbaLayout = new javax.swing.GroupLayout(qlyhocba);
        qlyhocba.setLayout(qlyhocbaLayout);
        qlyhocbaLayout.setHorizontalGroup(
            qlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlyhocbaLayout.createSequentialGroup()
                .addComponent(lane2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblquanlyhocsinh, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        qlyhocbaLayout.setVerticalGroup(
            qlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblquanlyhocsinh, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        qlytaikhoan.setBackground(new java.awt.Color(255, 255, 255));
        qlytaikhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qlytaikhoanMouseClicked(evt);
            }
        });

        lane3.setBackground(new java.awt.Color(255, 255, 255));
        lane3.setOpaque(true);

        lblquanlytaikhoan.setBackground(new java.awt.Color(255, 255, 255));
        lblquanlytaikhoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-user-account-32.png"))); // NOI18N
        lblquanlytaikhoan.setContentAreaFilled(false);
        lblquanlytaikhoan.setFocusable(false);
        lblquanlytaikhoan.setOpaque(true);
        lblquanlytaikhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblquanlytaikhoanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblquanlytaikhoanMouseExited(evt);
            }
        });
        lblquanlytaikhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblquanlytaikhoanActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText("Quản lý tài khoản");

        javax.swing.GroupLayout qlytaikhoanLayout = new javax.swing.GroupLayout(qlytaikhoan);
        qlytaikhoan.setLayout(qlytaikhoanLayout);
        qlytaikhoanLayout.setHorizontalGroup(
            qlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlytaikhoanLayout.createSequentialGroup()
                .addComponent(lane3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblquanlytaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
        );
        qlytaikhoanLayout.setVerticalGroup(
            qlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblquanlytaikhoan, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        qlynamhoc.setBackground(new java.awt.Color(255, 255, 255));
        qlynamhoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qlynamhocMouseClicked(evt);
            }
        });

        lane4.setBackground(new java.awt.Color(255, 255, 255));
        lane4.setOpaque(true);

        lblquanlynamhoc.setBackground(new java.awt.Color(255, 255, 255));
        lblquanlynamhoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-syllabus-32.png"))); // NOI18N
        lblquanlynamhoc.setContentAreaFilled(false);
        lblquanlynamhoc.setFocusable(false);
        lblquanlynamhoc.setOpaque(true);
        lblquanlynamhoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblquanlynamhocMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblquanlynamhocMouseExited(evt);
            }
        });
        lblquanlynamhoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblquanlynamhocActionPerformed(evt);
            }
        });

        qlnamhoc.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        qlnamhoc.setText("Thông tin năm học");

        javax.swing.GroupLayout qlynamhocLayout = new javax.swing.GroupLayout(qlynamhoc);
        qlynamhoc.setLayout(qlynamhocLayout);
        qlynamhocLayout.setHorizontalGroup(
            qlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qlynamhocLayout.createSequentialGroup()
                .addComponent(lane4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblquanlynamhoc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(qlnamhoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        qlynamhocLayout.setVerticalGroup(
            qlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lane4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblquanlynamhoc, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addComponent(qlnamhoc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/tải xuống.png"))); // NOI18N

        jLabel99.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel99.setText("BAN GIÁM HIỆU");

        jLabel100.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel100.setText("Mã GV");

        jLabel102.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(52, 152, 219));
        jLabel102.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel102.setText("Đăng xuất ?");
        jLabel102.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel102.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel102MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel99, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel100, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel102, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(qlynamhoc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(qlytaikhoan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(qlyhocba, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(trangchu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(jLabel100, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel102, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(trangchu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(qlyhocba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(qlytaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(qlynamhoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(183, Short.MAX_VALUE))
        );

        tanktab.setBackground(new java.awt.Color(255, 255, 255));

        tab1.setBackground(new java.awt.Color(255, 255, 255));
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab1MouseClicked(evt);
            }
        });

        lbltab1.setBackground(new java.awt.Color(255, 255, 255));
        lbltab1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbltab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbltab1MouseClicked(evt);
            }
        });

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

        body.setBackground(new java.awt.Color(255, 255, 255));
        body.setLayout(new java.awt.CardLayout());

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/—Pngtree—hand drawn online education online_4986515.png"))); // NOI18N
        jLabel6.setText("jLabel6");

        javax.swing.GroupLayout trangchinhLayout = new javax.swing.GroupLayout(trangchinh);
        trangchinh.setLayout(trangchinhLayout);
        trangchinhLayout.setHorizontalGroup(
            trangchinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1130, Short.MAX_VALUE)
        );
        trangchinhLayout.setVerticalGroup(
            trangchinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 653, Short.MAX_VALUE)
        );

        body.add(trangchinh, "card2");

        quanlytaikhoan.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel9.setText("Tìm kiếm :");

        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 51, 51));
        jLabel10.setText("Quản lý tài khoản.");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel11.setText("Chức vụ :");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel12.setText("Email :");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel13.setText("Mã giáo viên :");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel14.setText("Mã học sinh :");

        cboChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ban Giám Hiệu", "Đào Tạo", "Giáo Viên", "Học Sinh" }));
        cboChucVu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboChucVuItemStateChanged(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-cancel-delete-32.png"))); // NOI18N
        jButton2.setText("Xóa");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-available-updates-32.png"))); // NOI18N
        jButton4.setText("Làm mới");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });

        tblGridView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "1", null}
            },
            new String [] {
                "Email", "Chức vụ", "Mã GV"
            }
        ));
        tblGridView.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblGridView.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblGridView.setRowHeight(25);
        tblGridView.setSelectionBackground(new java.awt.Color(255, 51, 51));
        tblGridView.setShowVerticalLines(false);
        tblGridView.getTableHeader().setReorderingAllowed(false);
        tblGridView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridViewMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGridView);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 509, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Tài khoản giáo viên", jPanel2);

        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
        });

        tblGridView2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "1", null}
            },
            new String [] {
                "Email", "Chức vụ", "Mã HS"
            }
        ));
        tblGridView2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblGridView2.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblGridView2.setRowHeight(25);
        tblGridView2.setSelectionBackground(new java.awt.Color(255, 51, 51));
        tblGridView2.setShowVerticalLines(false);
        tblGridView2.getTableHeader().setReorderingAllowed(false);
        tblGridView2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridView2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblGridView2);
        if (tblGridView2.getColumnModel().getColumnCount() > 0) {
            tblGridView2.getColumnModel().getColumn(1).setHeaderValue("Chức vụ");
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 509, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Tài khoản học sinh", jPanel3);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_user_male_32px.png"))); // NOI18N
        jButton5.setText("Cập nhật");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout quanlytaikhoanLayout = new javax.swing.GroupLayout(quanlytaikhoan);
        quanlytaikhoan.setLayout(quanlytaikhoanLayout);
        quanlytaikhoanLayout.setHorizontalGroup(
            quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, quanlytaikhoanLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(quanlytaikhoanLayout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 593, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(quanlytaikhoanLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22))
                            .addGroup(quanlytaikhoanLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(quanlytaikhoanLayout.createSequentialGroup()
                                        .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cboChucVu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtMaGv)
                                            .addComponent(txtHocSinh)
                                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.TRAILING)))
                                    .addGroup(quanlytaikhoanLayout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addComponent(txtTimKiem)))
                                .addContainerGap())
                            .addGroup(quanlytaikhoanLayout.createSequentialGroup()
                                .addGap(163, 163, 163)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(quanlytaikhoanLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        quanlytaikhoanLayout.setVerticalGroup(
            quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quanlytaikhoanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(quanlytaikhoanLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaGv, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHocSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        body.add(quanlytaikhoan, "card3");

        quanlynamhoc.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 51, 255));
        jLabel16.setText("Thông tin năm học.");

        tblnamhoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "1", null, null}
            },
            new String [] {
                "Niên học", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"
            }
        ));
        tblnamhoc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblnamhoc.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblnamhoc.setRowHeight(25);
        tblnamhoc.setSelectionBackground(new java.awt.Color(51, 102, 255));
        tblnamhoc.setShowVerticalLines(false);
        tblnamhoc.getTableHeader().setReorderingAllowed(false);
        tblnamhoc.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tblnamhocMouseDragged(evt);
            }
        });
        tblnamhoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblnamhocMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblnamhocMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblnamhoc);

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel17.setText("Tìm kiếm :");

        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel19.setText("Niên học :");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel20.setText("Ngày bắt đầu :");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel21.setText("Ngày kết thúc :");

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-clipboard-list-32.png"))); // NOI18N
        jButton6.setText("Cập nhật");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-available-updates-32.png"))); // NOI18N
        jButton7.setText("Làm mới");
        jButton7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-education-32.png"))); // NOI18N
        jButton8.setText("Kết thúc năm học");
        jButton8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout quanlynamhocLayout = new javax.swing.GroupLayout(quanlynamhoc);
        quanlynamhoc.setLayout(quanlynamhocLayout);
        quanlynamhocLayout.setHorizontalGroup(
            quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quanlynamhocLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(quanlynamhocLayout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(quanlynamhocLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(quanlynamhocLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(quanlynamhocLayout.createSequentialGroup()
                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10))
                                    .addGroup(quanlynamhocLayout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txt_ketthuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(quanlynamhocLayout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_ngaybatdau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(quanlynamhocLayout.createSequentialGroup()
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                                        .addComponent(txt_nienhoc, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, quanlynamhocLayout.createSequentialGroup()
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, quanlynamhocLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(151, 151, 151)))))
                .addContainerGap())
        );
        quanlynamhocLayout.setVerticalGroup(
            quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quanlynamhocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(quanlynamhocLayout.createSequentialGroup()
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_nienhoc, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_ngaybatdau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_ketthuc, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(61, 61, 61)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        body.add(quanlynamhoc, "card4");

        quanlyhocba.setBackground(new java.awt.Color(255, 255, 255));

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 102, 102));
        jLabel29.setText("Học bạ học sinh");

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin học sinh", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel30.setText("Mã học sinh :");

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel31.setText("Họ và tên :");

        jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel32.setText("Giới tính :");

        lbl_hocba_hoten.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_hocba_hoten.setText("Nguyễn Xuân Bách");

        lbl_hocba_gioitinh.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_hocba_gioitinh.setText("04/06/2000");

        lbl_hocba_mahs.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_hocba_mahs.setText("HS0010");

        jLabel37.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel37.setText("Ngày sinh:");

        lbl_hocba_ngaysinh.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_hocba_ngaysinh.setText("02932823");

        jLabel38.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        jLabel38.setText("CMND:");

        lbl_hocba_cmnd.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_hocba_cmnd.setText("Nguyễn Xuân Bách");

        btn_capNhat9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btn_capNhat9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-save-tabs-list-32.png"))); // NOI18N
        btn_capNhat9.setText("Chi tiết học bạ");
        btn_capNhat9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31)))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_hocba_gioitinh, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_hocba_hoten, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_hocba_mahs, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGap(0, 27, Short.MAX_VALUE)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37)
                            .addComponent(jLabel38)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl_hocba_cmnd, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_hocba_ngaysinh, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(btn_capNhat9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_hocba_ngaysinh)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_hocba_cmnd, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                        .addGap(14, 14, 14)
                        .addComponent(btn_capNhat9, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_hocba_mahs)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_hocba_hoten, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)
                        .addGap(6, 6, 6)
                        .addComponent(lbl_hocba_gioitinh)))
                .addContainerGap())
        );

        jLabel33.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel33.setText("Tìm kiếm :");

        jLabel34.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel34.setText("Niên học :");

        txt_searchdiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchdiemKeyReleased(evt);
            }
        });

        cbo_hocba_nienhoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_hocba_nienhocActionPerformed(evt);
            }
        });

        cbo_hocba_khoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_hocba_khoiActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel35.setText("Khối :");

        cbo_hocba_lop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_hocba_lopActionPerformed(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel36.setText("Lớp :");

        tbl_hocba.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã học sinh", "Họ và tên", "Giới tính", "Ngày sinh", "CMND"
            }
        ));
        tbl_hocba.setRowHeight(25);
        tbl_hocba.setSelectionBackground(new java.awt.Color(52, 152, 219));
        tbl_hocba.getTableHeader().setReorderingAllowed(false);
        tbl_hocba.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_hocbaMouseClicked(evt);
            }
        });
        tbl_hocba.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_hocbaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbl_hocbaKeyReleased(evt);
            }
        });
        banglop.setViewportView(tbl_hocba);

        btn_capNhat10.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btn_capNhat10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_32px_1.png"))); // NOI18N
        btn_capNhat10.setText("Tìm kiếm");
        btn_capNhat10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhat10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout quanlyhocbaLayout = new javax.swing.GroupLayout(quanlyhocba);
        quanlyhocba.setLayout(quanlyhocbaLayout);
        quanlyhocbaLayout.setHorizontalGroup(
            quanlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, quanlyhocbaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(quanlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, quanlyhocbaLayout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addGroup(quanlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55)
                        .addGroup(quanlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(quanlyhocbaLayout.createSequentialGroup()
                                .addComponent(cbo_hocba_lop, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_capNhat10, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cbo_hocba_khoi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(quanlyhocbaLayout.createSequentialGroup()
                                .addComponent(txt_searchdiem, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cbo_hocba_nienhoc, 0, 318, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, quanlyhocbaLayout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(quanlyhocbaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(banglop, javax.swing.GroupLayout.PREFERRED_SIZE, 1069, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37))
        );
        quanlyhocbaLayout.setVerticalGroup(
            quanlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(quanlyhocbaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(quanlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(quanlyhocbaLayout.createSequentialGroup()
                        .addGroup(quanlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_searchdiem, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(quanlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbo_hocba_nienhoc, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(quanlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbo_hocba_khoi, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(quanlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbo_hocba_lop, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_capNhat10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(banglop, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        body.add(quanlyhocba, "card5");

        maintrangchu.setBackground(new java.awt.Color(255, 255, 255));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 51, 255));
        jLabel22.setText("Danh sách giáo viên");

        tblGridView1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã giáo viên", "Họ tên", "Ngày sinh", "Giới tính", "Điện thoại", "CMND", "Địa chỉ"
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
        jScrollPane4.setViewportView(tblGridView1);

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel23.setText("Tìm kiếm :");

        txtTimKiemGV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemGVKeyReleased(evt);
            }
        });

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel58.setText("Mã giáo viên :");

        lblMagv.setBackground(new java.awt.Color(255, 255, 255));
        lblMagv.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        txtHotenGV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHotenGVActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel24.setText("Họ tên :");

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel59.setText("Giới tính :");

        cbbsuaGioitinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel60.setText("Ngày sinh :");

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel25.setText("Điện thoại :");

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel28.setText("CMND/TCC:");

        txtDiaChiGV.setColumns(20);
        txtDiaChiGV.setRows(5);
        jScrollPane5.setViewportView(txtDiaChiGV);

        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel72.setText("Địa chỉ :");

        btnThemMOi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_user_male_32px.png"))); // NOI18N
        btnThemMOi.setText("Thêm mới");
        btnThemMOi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnThemMOi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMOiActionPerformed(evt);
            }
        });

        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_update_file_32px.png"))); // NOI18N
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-update-32.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        lblEMail.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        lblEMail.setText("Email:");

        javax.swing.GroupLayout maintrangchuLayout = new javax.swing.GroupLayout(maintrangchu);
        maintrangchu.setLayout(maintrangchuLayout);
        maintrangchuLayout.setHorizontalGroup(
            maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(maintrangchuLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(maintrangchuLayout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(136, 136, 136))
                    .addGroup(maintrangchuLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(maintrangchuLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel59)
                                    .addComponent(jLabel60, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblEMail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                                .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jDateChooser5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbbsuaGioitinh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtHotenGV)
                                    .addComponent(txtTimKiemGV, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                                    .addComponent(lblMagv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtDienThoaiGV)
                                    .addComponent(txtCMNDGV)
                                    .addComponent(jScrollPane5)
                                    .addComponent(txtEMailGV)))
                            .addGroup(maintrangchuLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnThemMOi, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        maintrangchuLayout.setVerticalGroup(
            maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(maintrangchuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(maintrangchuLayout.createSequentialGroup()
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(maintrangchuLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(txtTimKiemGV, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMagv, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(maintrangchuLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(txtHotenGV)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbbsuaGioitinh, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(txtDienThoaiGV))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEMail, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEMailGV, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(txtCMNDGV))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnThemMOi, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        body.add(maintrangchu, "card6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tanktab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(body, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(tanktab, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(body, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

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

    private void lblquanlyhocsinhMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblquanlyhocsinhMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblquanlyhocsinhMouseEntered

    private void lblquanlyhocsinhMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblquanlyhocsinhMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblquanlyhocsinhMouseExited

    private void lblquanlyhocsinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblquanlyhocsinhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblquanlyhocsinhActionPerformed

    private void qlyhocbaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qlyhocbaMouseClicked
        lane2.setBackground(new Color(119, 84, 248));
        lblquanlyhocsinh.setBackground(new Color(237, 234, 242));
        qlyhocba.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane3.setBackground(new Color(255, 255, 255));
        lblquanlytaikhoan.setBackground(new Color(255, 255, 255));
        qlytaikhoan.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lblquanlynamhoc.setBackground(new Color(255, 255, 255));
        qlynamhoc.setBackground(new Color(255, 255, 255));

        String tieude2 = "Thông tin học bạ";
        checktab(tieude2);

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(quanlyhocba);
        body.repaint();
        body.revalidate();
    }//GEN-LAST:event_qlyhocbaMouseClicked

    private void trangchuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trangchuMouseClicked

        lane1.setBackground(new Color(119, 84, 248));
        lbltrangchu.setBackground(new Color(237, 234, 242));
        trangchu.setBackground(new Color(237, 234, 242));

        lane2.setBackground(new Color(255, 255, 255));
        lblquanlyhocsinh.setBackground(new Color(255, 255, 255));
        qlyhocba.setBackground(new Color(255, 255, 255));

        lane3.setBackground(new Color(255, 255, 255));
        lblquanlytaikhoan.setBackground(new Color(255, 255, 255));
        qlytaikhoan.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lblquanlynamhoc.setBackground(new Color(255, 255, 255));
        qlynamhoc.setBackground(new Color(255, 255, 255));

        String tieude2 = "Danh sách giáo viên";
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

    private void lblquanlytaikhoanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblquanlytaikhoanMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblquanlytaikhoanMouseEntered

    private void lblquanlytaikhoanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblquanlytaikhoanMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblquanlytaikhoanMouseExited

    private void lblquanlytaikhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblquanlytaikhoanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblquanlytaikhoanActionPerformed

    private void qlytaikhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qlytaikhoanMouseClicked
        lane3.setBackground(new Color(119, 84, 248));
        lblquanlytaikhoan.setBackground(new Color(237, 234, 242));
        qlytaikhoan.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane2.setBackground(new Color(255, 255, 255));
        lblquanlyhocsinh.setBackground(new Color(255, 255, 255));
        qlyhocba.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lblquanlynamhoc.setBackground(new Color(255, 255, 255));
        qlynamhoc.setBackground(new Color(255, 255, 255));

        String tieude2 = "Quản lý tài khoản";
        checktab(tieude2);

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(quanlytaikhoan);
        body.repaint();
        body.revalidate();
    }//GEN-LAST:event_qlytaikhoanMouseClicked

    private void lblquanlynamhocMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblquanlynamhocMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblquanlynamhocMouseEntered

    private void lblquanlynamhocMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblquanlynamhocMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblquanlynamhocMouseExited

    private void lblquanlynamhocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblquanlynamhocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblquanlynamhocActionPerformed

    private void qlynamhocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qlynamhocMouseClicked
        lane4.setBackground(new Color(119, 84, 248));
        lblquanlynamhoc.setBackground(new Color(237, 234, 242));
        qlynamhoc.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane2.setBackground(new Color(255, 255, 255));
        lblquanlyhocsinh.setBackground(new Color(255, 255, 255));
        qlyhocba.setBackground(new Color(255, 255, 255));

        lane3.setBackground(new Color(255, 255, 255));
        lblquanlytaikhoan.setBackground(new Color(255, 255, 255));
        qlytaikhoan.setBackground(new Color(255, 255, 255));

        String tieude2 = "Thông tin năm học";
        checktab(tieude2);

        this.loadtablenamhoc();

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(quanlynamhoc);
        body.repaint();
        body.revalidate();
    }//GEN-LAST:event_qlynamhocMouseClicked

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (cboChucVu.getSelectedItem().toString().equals("Học Sinh")) {
            this.deleteTKHS();
        } else {
            this.deleteTK();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.NewForm();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tblnamhocMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblnamhocMouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblnamhocMouseDragged

    private void tblnamhocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblnamhocMouseClicked
        int row = tblnamhoc.getSelectedRow();
        txt_nienhoc.setText(tblnamhoc.getValueAt(row, 0).toString());
        txt_ngaybatdau.setDate((Date) tblnamhoc.getValueAt(row, 1));
        txt_ketthuc.setDate((Date) tblnamhoc.getValueAt(row, 2));
        if (tblnamhoc.getValueAt(row, 3).toString().equals("Kết thúc")) {
            jButton8.setVisible(false);
        } else {
            jButton8.setVisible(true);
        }
    }//GEN-LAST:event_tblnamhocMouseClicked

    private void tblnamhocMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblnamhocMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblnamhocMouseReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        boolean i = DialogHelper.confirm(null, "bạn muốn tạo năm học mới !");
        if (i == true) {
            if (this.checkNamHoc() !=0) {
                DialogHelper.alert(null, "vui lòng kết thúc năm học cũ trước khi tạo năm học mới!");
                this.resetform();
            } else {
                if (checkformnh()) {
                    this.taomoinamhoc();
                    this.loadtablenamhoc();
                    this.loadtoCBBNamHoc();
                }
            }
        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        this.resetform();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        boolean i = DialogHelper.confirm(null, "bạn muốn kết thúc năm học !");
        if (i == true && compareDate() == true) {
            this.ktnamhoc();
            this.loadtoCBBNamHoc();
            boolean a = DialogHelper.confirm(null, "Thống kê học lực học sinh toàn trường");
            if(a==true){
                chart();
            }
        }

    }//GEN-LAST:event_jButton8ActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        String ft = txtTimKiem.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
        tblGridView.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(ft));
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
//        txtHocSinh.setEnabled(true);
//        txtMaGv.setEnabled(false);
//        cboChucVu.setSelectedItem("Học Sinh");
//        cboChucVu.setEnabled(false);
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        int i = tblGridView.getSelectedRow();
        txtEmail.setText(tblGridView.getValueAt(i, 0).toString());
        cboChucVu.setSelectedItem(tblGridView.getValueAt(i, 1));
        txtMaGv.setText(tblGridView.getValueAt(i, 2).toString());
    }//GEN-LAST:event_tblGridViewMouseClicked

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        DefaultTableModel model = (DefaultTableModel) tblnamhoc.getModel();
        String ft = txtTimKiem.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
        tblnamhoc.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(ft));
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (cboChucVu.getSelectedItem().toString().equals("Học Sinh")) {
            if (checktkHS()) {
                this.updateHS();
                this.LoadToTableTKHS();
                this.NewForm();
            }
        } else {
            if (checktk()) {
                this.update();
                this.LoadToTableTKGV();
                this.NewForm();
            }
        }


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked

    }//GEN-LAST:event_jPanel2MouseClicked

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked

    }//GEN-LAST:event_jPanel3MouseClicked

    private void tblGridView1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView1MouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridView1MouseDragged

    private void tblGridView1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView1MouseClicked
        DefaultTableModel model = (DefaultTableModel) tblGridView1.getModel();
        int i = tblGridView1.getSelectedRow();
        String m = (tblGridView1.getValueAt(i, 0).toString());

        ResultSet rs = gvdao.select3(m);
        try {
            if (rs.next()) {
                lblMagv.setText(rs.getString("magiaovien"));
                txtHotenGV.setText(rs.getString("hoten"));
                if (rs.getString("gioitinh").equals("1")) {
                    cbbsuaGioitinh.setSelectedItem("Nam");
                } else {
                    cbbsuaGioitinh.setSelectedItem("Nữ");
                }
                jDateChooser5.setDate(rs.getDate("ngaysinh"));
                txtDienThoaiGV.setText(rs.getString("dienthoai"));
                txtEMailGV.setText(rs.getString("email"));
                txtCMNDGV.setText(rs.getString("cmnd"));
                txtDiaChiGV.setText(rs.getString("diachi"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnCapNhat.setEnabled(true);
    }//GEN-LAST:event_tblGridView1MouseClicked

    private void tblGridView1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView1MouseReleased
        if (evt.getButton() == MouseEvent.BUTTON3) {
            if (evt.isPopupTrigger() && tblGridView1.getSelectedRowCount() != 0) {
                PopUpMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tblGridView1MouseReleased

    private void btnThemMOiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMOiActionPerformed
        this.NewForGV();
        ResultSet rs = gvdao.select4();
        try {
            if (rs.next()) {
                int max = Integer.parseInt(rs.getString("max"));
                int i = max + 1;
                if (i < 10) {
                    lblMagv.setText(String.valueOf("GV00" + i));
                } else if (10 <= i && i < 100) {
                    lblMagv.setText(String.valueOf("GV0" + i));
                } else {
                    lblMagv.setText(String.valueOf("GV" + i));
                }
            }
        } catch (Exception e) {
        }
        btnLuu.setEnabled(true);
    }//GEN-LAST:event_btnThemMOiActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        if (checkGV()) {
            this.updateGV();
        }
        this.loadToGV();
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        if (checkGV() && this.checkemail(txtEMailGV.getText())) {
            this.insert();
            tkDAO.insertTKGV(genaccount());
        }
        this.loadToGV();
    }//GEN-LAST:event_btnLuuActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void txtTimKiemGVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemGVKeyReleased
        DefaultTableModel model = (DefaultTableModel) tblGridView1.getModel();
        String ft = txtTimKiemGV.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
        tblGridView1.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(ft));
    }//GEN-LAST:event_txtTimKiemGVKeyReleased

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        lane3.setBackground(new Color(119, 84, 248));
        lblquanlytaikhoan.setBackground(new Color(237, 234, 242));
        qlytaikhoan.setBackground(new Color(237, 234, 242));

        lane1.setBackground(new Color(255, 255, 255));
        lbltrangchu.setBackground(new Color(255, 255, 255));
        trangchu.setBackground(new Color(255, 255, 255));

        lane2.setBackground(new Color(255, 255, 255));
        lblquanlyhocsinh.setBackground(new Color(255, 255, 255));
        qlyhocba.setBackground(new Color(255, 255, 255));

        lane4.setBackground(new Color(255, 255, 255));
        lblquanlynamhoc.setBackground(new Color(255, 255, 255));
        qlynamhoc.setBackground(new Color(255, 255, 255));

        String tieude2 = "Quản lý tài khoản";
        checktab(tieude2);

        body.removeAll();
        body.repaint();
        body.revalidate();
        body.add(quanlytaikhoan);
        body.repaint();
        body.revalidate();

        DefaultTableModel model = (DefaultTableModel) tblGridView1.getModel();
        int i = tblGridView1.getSelectedRow();
        String m = (tblGridView1.getValueAt(i, 0).toString());
        txtMaGv.setText(m);

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void tblGridView2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView2MouseClicked
        int i = tblGridView2.getSelectedRow();
        txtEmail.setText(tblGridView2.getValueAt(i, 0).toString());
        cboChucVu.setSelectedItem(tblGridView2.getValueAt(i, 1));
        txtHocSinh.setText(tblGridView2.getValueAt(i, 2).toString());
    }//GEN-LAST:event_tblGridView2MouseClicked

    private void cboChucVuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboChucVuItemStateChanged
        if (cboChucVu.getSelectedItem().equals("Học Sinh")) {
            txtHocSinh.setEnabled(true);
            txtMaGv.setEnabled(false);
        } else {
            txtHocSinh.setEnabled(false);
            txtMaGv.setEnabled(true);
        }
    }//GEN-LAST:event_cboChucVuItemStateChanged

    private void txtHotenGVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHotenGVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHotenGVActionPerformed

    private void txt_searchdiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchdiemKeyReleased
        DefaultTableModel model = (DefaultTableModel) tbl_hocba.getModel();
        String ft = txt_searchdiem.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
        tbl_hocba.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(ft));
    }//GEN-LAST:event_txt_searchdiemKeyReleased

    private void cbo_hocba_nienhocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_hocba_nienhocActionPerformed

    }//GEN-LAST:event_cbo_hocba_nienhocActionPerformed

    private void cbo_hocba_khoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_hocba_khoiActionPerformed
        this.loadToTableLopHoc();
    }//GEN-LAST:event_cbo_hocba_khoiActionPerformed

    private void cbo_hocba_lopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_hocba_lopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_hocba_lopActionPerformed

    private void btn_capNhat9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat9ActionPerformed

        File file = new File(System.getProperty("user.dir") + "học Bạ.pdf");
        try {
            pdf.write_PDF(file.getPath(), lbl_hocba_mahs.getText(), (String) cbo_hocba_nienhoc.getSelectedItem());
        } catch (IOException ex) {
            Logger.getLogger(TrangChuGV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TrangChuGV.class.getName()).log(Level.SEVERE, null, ex);
        }

        Desktop desktop = Desktop.getDesktop();
        if (file.exists()) {
            try {
                desktop.open(file);
            } catch (IOException ex) {
                Logger.getLogger(TrangChuGV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //let's try to open PDF file
        if (file.exists()) {
            try {
                desktop.open(file);
            } catch (IOException ex) {
                Logger.getLogger(TrangChuGV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btn_capNhat9ActionPerformed

    private void tbl_hocbaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_hocbaMouseClicked
        int i = tbl_hocba.getSelectedRow();
        lbl_hocba_mahs.setText(tbl_hocba.getValueAt(i, 0).toString());
        lbl_hocba_hoten.setText(tbl_hocba.getValueAt(i, 1).toString());
        lbl_hocba_gioitinh.setText(tbl_hocba.getValueAt(i, 2).toString());
        lbl_hocba_ngaysinh.setText(tbl_hocba.getValueAt(i, 3).toString());
        lbl_hocba_cmnd.setText(tbl_hocba.getValueAt(i, 4).toString());
    }//GEN-LAST:event_tbl_hocbaMouseClicked

    private void tbl_hocbaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_hocbaKeyPressed

    }//GEN-LAST:event_tbl_hocbaKeyPressed

    private void tbl_hocbaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_hocbaKeyReleased

    }//GEN-LAST:event_tbl_hocbaKeyReleased

    private void btn_capNhat10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhat10ActionPerformed
        this.loadhocsinhhocba();
    }//GEN-LAST:event_btn_capNhat10ActionPerformed

    private void lbltab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbltab1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbltab1MouseClicked

    private void jLabel102MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel102MouseClicked
        boolean i = DialogHelper.confirm(null, "bạn có muốn đăng xuất");
        if (i == true) {
            ShareHelper.logoff();
            dangnhap dn = new dangnhap();
            dn.setVisible(maximized);
            this.dispose();
        }
    }//GEN-LAST:event_jLabel102MouseClicked

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
            java.util.logging.Logger.getLogger(TrangChuBDH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TrangChuBDH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TrangChuBDH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrangChuBDH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TrangChuBDH().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu PopUpMenu;
    private javax.swing.JScrollPane banglop;
    private javax.swing.JPanel body;
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnMinimize;
    private javax.swing.JButton btnMinimize4;
    private javax.swing.JButton btnThemMOi;
    private javax.swing.JButton btn_capNhat10;
    private javax.swing.JButton btn_capNhat9;
    private javax.swing.JButton btntab1;
    private javax.swing.JButton btntab2;
    private javax.swing.JButton btntab3;
    private javax.swing.JButton btntab4;
    private javax.swing.JButton btntab5;
    private javax.swing.JComboBox<String> cbbNamHoc;
    private javax.swing.JComboBox<String> cbbsuaGioitinh;
    private javax.swing.JComboBox<String> cboChucVu;
    private javax.swing.JComboBox<String> cbo_hocba_khoi;
    private javax.swing.JComboBox<String> cbo_hocba_lop;
    private javax.swing.JComboBox<String> cbo_hocba_nienhoc;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox7;
    private com.toedter.calendar.JDateChooser jDateChooser5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JLabel lane1;
    private javax.swing.JLabel lane2;
    private javax.swing.JLabel lane3;
    private javax.swing.JLabel lane4;
    private javax.swing.JLabel lblEMail;
    private javax.swing.JLabel lblMagv;
    private javax.swing.JLabel lbl_hocba_cmnd;
    private javax.swing.JLabel lbl_hocba_gioitinh;
    private javax.swing.JLabel lbl_hocba_hoten;
    private javax.swing.JLabel lbl_hocba_mahs;
    private javax.swing.JLabel lbl_hocba_ngaysinh;
    private javax.swing.JButton lblquanlyhocsinh;
    private javax.swing.JButton lblquanlynamhoc;
    private javax.swing.JButton lblquanlytaikhoan;
    private javax.swing.JLabel lbltab1;
    private javax.swing.JLabel lbltab2;
    private javax.swing.JLabel lbltab3;
    private javax.swing.JLabel lbltab4;
    private javax.swing.JLabel lbltab5;
    private javax.swing.JButton lbltrangchu;
    private javax.swing.JPanel maintrangchu;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JLabel qlnamhoc;
    private javax.swing.JPanel qlyhocba;
    private javax.swing.JPanel qlynamhoc;
    private javax.swing.JPanel qlytaikhoan;
    private javax.swing.JPanel quanlyhocba;
    private javax.swing.JPanel quanlynamhoc;
    private javax.swing.JPanel quanlytaikhoan;
    private javax.swing.JPanel tab1;
    private javax.swing.JPanel tab2;
    private javax.swing.JPanel tab3;
    private javax.swing.JPanel tab4;
    private javax.swing.JPanel tab5;
    private javax.swing.JPanel tanktab;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTable tblGridView1;
    private javax.swing.JTable tblGridView2;
    private javax.swing.JTable tbl_hocba;
    private javax.swing.JTable tblnamhoc;
    private javax.swing.JPanel trangchinh;
    private javax.swing.JPanel trangchu;
    private javax.swing.JTextField txtCMNDGV;
    private javax.swing.JTextArea txtDiaChiGV;
    private javax.swing.JTextField txtDienThoaiGV;
    private javax.swing.JTextField txtEMailGV;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHocSinh;
    private javax.swing.JTextField txtHotenGV;
    private javax.swing.JTextField txtMaGv;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTimKiemGV;
    private com.toedter.calendar.JDateChooser txt_ketthuc;
    private com.toedter.calendar.JDateChooser txt_ngaybatdau;
    private javax.swing.JTextField txt_nienhoc;
    private javax.swing.JTextField txt_searchdiem;
    // End of variables declaration//GEN-END:variables

}

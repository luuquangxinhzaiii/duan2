/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.BDH;

import AppPackage.AnimationClass;
import da.dao.GiaoVienDAO;
import da.dao.NamHocDAO;
import da.dao.TaiKhoanDAO;
import da.helper.DialogHelper;
import da.model.GiaoVien;
import da.model.NamHoc;
import da.model.TaiKhoan;
import java.awt.Color;
import java.awt.Frame;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Rahmans
 */
public class TrangChuBDH extends javax.swing.JFrame {

    static boolean maximized = true;
    int xMouse;
    int yMouse;
    String head[] = {"Id", "Email", "Chức Vụ", "Mã Giáo Viên"};
    String head1[] = {"Mã giáo viên", "Họ tên", "Ngày Sinh", "Giới tính", "CMND", "Địa chỉ"};
    DefaultTableModel model = new DefaultTableModel(head, 0);
    DefaultTableModel model1 = new DefaultTableModel(head1, 0);
    TaiKhoanDAO tkDAO = new TaiKhoanDAO();
    NamHocDAO nhDAO = new NamHocDAO();
    GiaoVienDAO gvdao = new GiaoVienDAO();

    public TrangChuBDH() {
        initComponents();
        this.LoadToTableTKGV();
        txtHocSinh.setEnabled(false);
        this.loadtoCBBNamHoc();
        this.loadToGV();
        btnCapNhat.setEnabled(false);
        btnLuu.setEnabled(false);
    }

    private void loadtablenamhoc() {
        DefaultTableModel model = (DefaultTableModel) tblnamhoc.getModel();
        model.setRowCount(0);
        try {

            List<NamHoc> list = nhDAO.select();
            for (NamHoc nh : list) {
                Object[] row = {
                    nh.getMaNamHoc(),
                    nh.getNienHoc(),
                    nh.getNgayBD(),
                    nh.getNgayKT(),
                    nh.getTrangThai(),};
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

                if (rs.getString("gioitinh").equals("1")) {
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
        model.setNgaySinh(jDateChooser5.getDate());
        model.setDienThoai(txtDienThoaiGV.getText());
        model.setCmnd(txtCMNDGV.getText());
        model.setDiaChi(txtDiaChiGV.getText());
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

    private void xoanamhoc() {
        NamHoc model = getModel();
        try {
            nhDAO.xoanamhoc(model);
            DialogHelper.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật thất bại!");
            e.printStackTrace();
        }
    }

    private void updatenamhoc() {
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
        model.setMaNamHoc(lbl_id.getText());
        model.setNienHoc(txt_nienhoc.getText());
        model.setNgayBD(txt_ngaybatdau.getDate());
        model.setNgayKT(txt_ketthuc.getDate());
        return model;

    }

    public TaiKhoan getModelGV() {
        TaiKhoan model = new TaiKhoan();

        model.setEmail(txtEmail.getText());
        if (cbbChucVu.getSelectedIndex() == 0) {
            model.setRole("BGH");
        } else if (cbbChucVu.getSelectedIndex() == 1) {
            model.setRole("HC");
        } else if (cbbChucVu.getSelectedIndex() == 2) {
            model.setRole("GV");
        } else {
            model.setRole("HS");
        }
        model.setPassWord("123");
        model.setMaGiaoVien(txtMaGv.getText());

        return model;
    }

    public TaiKhoan getModel2() {
        TaiKhoan model = new TaiKhoan();

        int i = tblGridView.getSelectedRow();
        String m = (tblGridView.getValueAt(i, 0).toString());
        model.setStt(Integer.parseInt(m));
        model.setEmail(txtEmail.getText());
        if (cbbChucVu.getSelectedIndex() == 0) {
            model.setRole("BGH");
        } else if (cbbChucVu.getSelectedIndex() == 1) {
            model.setRole("HC");
        } else if (cbbChucVu.getSelectedIndex() == 2) {
            model.setRole("GV");
        } else {
            model.setRole("HS");
        }
        model.setPassWord("123");

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

    public void deleteTK() {
        int i = tblGridView.getSelectedRow();
        String m = (tblGridView.getValueAt(i, 0).toString());
        try {
            tkDAO.delete(Integer.parseInt(m));
            this.LoadToTableTKGV();
            this.NewForm();
            JOptionPane.showMessageDialog(this, "Xoa thanh cong");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xoa that bai!!!");
        }
    }

    void resetform() {
        txtTimKiem.setText(null);
        ((JTextField) txt_ketthuc.getDateEditor().getUiComponent()).setText(null);
        ((JTextField) txt_ngaybatdau.getDateEditor().getUiComponent()).setText(null);
        ResultSet rs = nhDAO.selectid();
        try {
            if (rs.next()) {

                int i = Integer.parseInt(rs.getString("max(manamhoc)")) + 1;

                lbl_id.setText(String.valueOf(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        ResultSet rs = gvdao.select();

        List<GiaoVien> list = gvdao.select2();
        for (GiaoVien ch : list) {
            if (txtMaGv.getText().matches(ch.getMaGV())) {
                JOptionPane.showMessageDialog(this, "Tài khoản đã tồn tại");
                return false;
            }
        }

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

    public void LoadToTableTKGV() {
        model.setRowCount(0);
        try {
            ResultSet rs = tkDAO.select2();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("stt"));
                row.add(rs.getString("email"));
                if (rs.getString("roles").equals("BGH")) {
                    row.add("Ban Giám Hiệu");
                } else if (rs.getString("roles").equals("HC")) {
                    row.add("Hành Chính");
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

    public void NewForm() {
        txtEmail.setText("");
        txtMaGv.setText("");
        txtHocSinh.setText("");
        cbbChucVu.setSelectedIndex(0);

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
        nhDAO.ketthuc(model);
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
        cbbChucVu = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        btnThemMoi = new javax.swing.JButton();
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
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txt_nienhoc = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        txt_ngaybatdau = new com.toedter.calendar.JDateChooser();
        txt_ketthuc = new com.toedter.calendar.JDateChooser();
        lbl_id = new javax.swing.JLabel();
        quanlyhocba = new javax.swing.JPanel();
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
                            .addComponent(btnExit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnMinimize, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))))
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
        jLabel99.setText("Tên giáo viên");

        jLabel100.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel100.setText("Mã GV");

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
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1130, Short.MAX_VALUE)
        );
        trangchinhLayout.setVerticalGroup(
            trangchinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 630, Short.MAX_VALUE)
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

        cbbChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ban Giám Hiệu", "Hành Chính", "Giáo Viên", "Học Sinh" }));

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-cancel-delete-32.png"))); // NOI18N
        jButton2.setText("Xóa");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnThemMoi.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnThemMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_user_male_32px.png"))); // NOI18N
        btnThemMoi.setText("Thêm mới");
        btnThemMoi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnThemMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMoiActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
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

        tblGridView.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tblGridView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "1", "1", null}
            },
            new String [] {
                "ID", "Email", "Chức vụ", "Mã GV"
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
        if (tblGridView.getColumnModel().getColumnCount() > 0) {
            tblGridView.getColumnModel().getColumn(3).setHeaderValue("Mã GV");
        }

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

        tblGridView2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tblGridView2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "1", "1", null}
            },
            new String [] {
                "ID", "Email", "Chức vụ", "Mã HS"
            }
        ));
        tblGridView2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblGridView2.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblGridView2.setRowHeight(25);
        tblGridView2.setSelectionBackground(new java.awt.Color(255, 51, 51));
        tblGridView2.setShowVerticalLines(false);
        tblGridView2.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblGridView2);

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

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
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
                                .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                                .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnThemMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                            .addComponent(cbbChucVu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtMaGv)
                                            .addComponent(txtHocSinh)
                                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.TRAILING)))
                                    .addGroup(quanlytaikhoanLayout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addComponent(txtTimKiem)))
                                .addContainerGap())))
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
                .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                            .addComponent(cbbChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGap(34, 34, 34)
                        .addGroup(quanlytaikhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnThemMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14))
                    .addComponent(jTabbedPane1))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        body.add(quanlytaikhoan, "card3");

        quanlynamhoc.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 51, 255));
        jLabel16.setText("Thông tin năm học.");

        tblnamhoc.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tblnamhoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "1", "1", null, null}
            },
            new String [] {
                "ID", "Niên học", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"
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

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel18.setText("ID :");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel19.setText("Niên học :");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel20.setText("Ngày bắt đầu :");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel21.setText("Ngày kết thúc :");

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-clipboard-list-32.png"))); // NOI18N
        jButton6.setText("Cập nhật");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-available-updates-32.png"))); // NOI18N
        jButton7.setText("Làm mới");
        jButton7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-education-32.png"))); // NOI18N
        jButton8.setText("Kết thúc năm học");
        jButton8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-cancel-delete-32.png"))); // NOI18N
        jButton9.setText("Xóa");
        jButton9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        lbl_id.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_id.setText("jLabel7");

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
                        .addGap(30, 30, 30)
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(quanlynamhocLayout.createSequentialGroup()
                                .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                                .addComponent(txt_nienhoc, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, quanlynamhocLayout.createSequentialGroup()
                                .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                                    .addComponent(lbl_id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
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
                        .addGap(31, 31, 31)
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(lbl_id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(28, 28, 28)
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_nienhoc, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_ngaybatdau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_ketthuc, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(quanlynamhocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        body.add(quanlynamhoc, "card4");

        quanlyhocba.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout quanlyhocbaLayout = new javax.swing.GroupLayout(quanlyhocba);
        quanlyhocba.setLayout(quanlyhocbaLayout);
        quanlyhocbaLayout.setHorizontalGroup(
            quanlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1130, Short.MAX_VALUE)
        );
        quanlyhocbaLayout.setVerticalGroup(
            quanlyhocbaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );

        body.add(quanlyhocba, "card5");

        maintrangchu.setBackground(new java.awt.Color(255, 255, 255));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 51, 255));
        jLabel22.setText("Danh sách giáo viên");

        tblGridView1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
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
        lblMagv.setText("jLabel1");

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel24.setText("Họ tên :");

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel59.setText("Giới tính :");

        cbbsuaGioitinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel60.setText("Ngày sinh :");

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel25.setText("Điện thoại :");

        txtDienThoaiGV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDienThoaiGVActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel28.setText("CMND/TCC:");

        txtDiaChiGV.setColumns(20);
        txtDiaChiGV.setRows(5);
        jScrollPane5.setViewportView(txtDiaChiGV);

        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel72.setText("Địa chỉ :");

        btnThemMOi.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnThemMOi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_user_male_32px.png"))); // NOI18N
        btnThemMOi.setText("Thêm mới");
        btnThemMOi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnThemMOi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMOiActionPerformed(evt);
            }
        });

        btnCapNhat.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_update_file_32px.png"))); // NOI18N
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnLuu.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-update-32.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

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
                                    .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jDateChooser5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbbsuaGioitinh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtHotenGV)
                                    .addComponent(txtTimKiemGV, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                                    .addComponent(lblMagv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtDienThoaiGV)
                                    .addComponent(txtCMNDGV)
                                    .addComponent(jScrollPane5)))
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
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimKiemGV, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMagv, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHotenGV, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbbsuaGioitinh, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDienThoaiGV, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCMNDGV, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(maintrangchuLayout.createSequentialGroup()
                                .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(maintrangchuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnThemMOi, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        body.add(maintrangchu, "card6");

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
        this.deleteTK();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnThemMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMoiActionPerformed

        if (checktk()) {
            this.insertGV();
            this.LoadToTableTKGV();
        }


    }//GEN-LAST:event_btnThemMoiActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.NewForm();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tblnamhocMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblnamhocMouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_tblnamhocMouseDragged

    private void tblnamhocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblnamhocMouseClicked
        int row = tblnamhoc.getSelectedRow();
        lbl_id.setText(tblnamhoc.getValueAt(row, 0).toString());
        txt_nienhoc.setText(tblnamhoc.getValueAt(row, 1).toString());
        txt_ngaybatdau.setDate((Date) tblnamhoc.getValueAt(row, 2));
        txt_ketthuc.setDate((Date) tblnamhoc.getValueAt(row, 3));

        if (tblnamhoc.getValueAt(row, 4).toString().equals("Kết thúc")) {
            jButton8.setVisible(false);
        } else {
            jButton8.setVisible(true);
        }
    }//GEN-LAST:event_tblnamhocMouseClicked

    private void tblnamhocMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblnamhocMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblnamhocMouseReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        if (checkformnh()) {
            this.updatenamhoc();
            this.loadtablenamhoc();

        }


    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        this.resetform();
        this.resetform();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        this.ktnamhoc();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (checkformnh()) {
            this.xoanamhoc();
            this.loadtablenamhoc();
            this.resetform();

        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        String ft = txtTimKiem.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
        tblGridView.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(ft));
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked

    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        int i = tblGridView.getSelectedRow();
        String m = (tblGridView.getValueAt(i, 0).toString());

        ResultSet rs = tkDAO.select3(m);
        try {
            if (rs.next()) {
                txtEmail.setText(rs.getString("email"));
                if (rs.getString("roles").equals("BGH")) {
                    cbbChucVu.setSelectedIndex(0);
                } else if (rs.getString("roles").equals("HC")) {
                    cbbChucVu.setSelectedIndex(1);
                } else {
                    cbbChucVu.setSelectedIndex(2);
                }

                txtMaGv.setText(rs.getString("magiaovien"));
                txtHocSinh.setText(rs.getString("mahocsinh"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblGridViewMouseClicked

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        DefaultTableModel model = (DefaultTableModel) tblnamhoc.getModel();
        String ft = txtTimKiem.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
        tblnamhoc.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(ft));
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        if (checktk()) {
            this.update();
            this.LoadToTableTKGV();
            this.NewForm();
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
                txtCMNDGV.setText(rs.getString("cmnd"));
                txtDiaChiGV.setText(rs.getString("diachi"));
            }
        } catch (Exception e) {
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

    private void txtDienThoaiGVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDienThoaiGVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDienThoaiGVActionPerformed

    private void btnThemMOiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMOiActionPerformed
        this.NewForGV();
        ResultSet rs = gvdao.select4();
        try {
            if (rs.next()) {
                int i = Integer.parseInt(rs.getString("max(id)")) + 1;
                lblMagv.setText(String.valueOf("GV0000" + i));
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
        if (checkGV()) {
            this.insert();

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
    private javax.swing.JPanel body;
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnMinimize;
    private javax.swing.JButton btnMinimize4;
    private javax.swing.JButton btnThemMOi;
    private javax.swing.JButton btnThemMoi;
    private javax.swing.JButton btntab1;
    private javax.swing.JButton btntab2;
    private javax.swing.JButton btntab3;
    private javax.swing.JButton btntab4;
    private javax.swing.JButton btntab5;
    private javax.swing.JComboBox<String> cbbChucVu;
    private javax.swing.JComboBox<String> cbbNamHoc;
    private javax.swing.JComboBox<String> cbbsuaGioitinh;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
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
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JLabel lblMagv;
    private javax.swing.JLabel lbl_id;
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
    private javax.swing.JTable tblnamhoc;
    private javax.swing.JPanel trangchinh;
    private javax.swing.JPanel trangchu;
    private javax.swing.JTextField txtCMNDGV;
    private javax.swing.JTextArea txtDiaChiGV;
    private javax.swing.JTextField txtDienThoaiGV;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHocSinh;
    private javax.swing.JTextField txtHotenGV;
    private javax.swing.JTextField txtMaGv;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTimKiemGV;
    private com.toedter.calendar.JDateChooser txt_ketthuc;
    private com.toedter.calendar.JDateChooser txt_ngaybatdau;
    private javax.swing.JTextField txt_nienhoc;
    // End of variables declaration//GEN-END:variables

}

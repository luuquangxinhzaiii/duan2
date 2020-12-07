/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import AppPackage.AnimationClass;
import da.dao.GiaoVienDAO;
import da.dao.RanDom;
import da.dao.TaiKhoanDAO;
import da.helper.BCrypt;
import da.helper.DialogHelper;
import da.helper.ShareHelper;
import da.model.GiaoVien;
import da.model.TaiKhoan;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import view.BDH.TrangChuBDH;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import view.GV.TrangChuGV;
import view.HC.TrangChuHC;

/**
 *
 * @author Rahmans
 */
public class dangnhap extends javax.swing.JFrame {

    static int interval;
    static Timer timer;
    static boolean maximized = true;
    int xMouse;
    int yMouse;
    AnimationClass ac = new AnimationClass();
    TaiKhoanDAO uDAO = new TaiKhoanDAO();
    int numberOfCharactor = 8;
    GiaoVienDAO gvDAO = new GiaoVienDAO();

    public dangnhap() {
        initComponents();
        slidershow();
    }

    private void sendmail() {
        RanDom rand = new RanDom();
        String code = rand.randomAlphaNumeric(numberOfCharactor);
        this.sendmail(txt_email.getText(), code);
        macode.setText(code);
        System.out.println(code);
    }

    public void updatepass() {
        String email = txt_tentk.getText();
        String mk1 = new String(txt_mk1.getPassword());
        String mk2 = new String(txt_mk2.getPassword());
        try {
            TaiKhoan nhanVien = uDAO.findById(email);
            if (nhanVien != null) {
                if (mk1.equals(mk2)) {
                    uDAO.update2(email, BCrypt.hashpw(mk2, BCrypt.gensalt(12)));
                    pnlBody.removeAll();
                    pnlBody.repaint();
                    pnlBody.revalidate();
                    pnlBody.add(pnlNews4);
                    pnlBody.repaint();
                    pnlBody.revalidate();
                } else {
                    lbl_tbdmk.setText("Vui lòng kiểm tra lại thông tin");
                }
            } else {
                lbl_tbdmk.setText("Vui lòng kiểm tra lại thông tin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lbl_tbdmk.setText("Vui lòng kiểm tra lại thông tin");
        }

    }

    public void Login() {
        String email = txt_UserName.getText();
        String matKhau = new String(pas_PassWord.getPassword());
        try {
            TaiKhoan taikhoan = uDAO.findById(email);
            if (taikhoan != null) {
                String matKhau2 = taikhoan.getPassWord();
                String roles = taikhoan.getRole();
                GiaoVien giaovien = gvDAO.findByUUID(taikhoan.getGiaovien_id());
                taikhoan.setHoten(giaovien.getHoTen());
                ShareHelper.TaiKhoan = taikhoan;
                if (BCrypt.checkpw(matKhau, matKhau2)) {
                    if (roles.equals("BGH")) {
                        TrangChuBDH bdh = new TrangChuBDH();
                        bdh.setVisible(maximized);
                        this.dispose();
                    } else if (roles.equals("GV")) {
                        TrangChuGV bdh = new TrangChuGV();
                        bdh.setVisible(maximized);
                        this.dispose();
                    } else if (roles.equals("DT")) {
                        TrangChuHC bdh = new TrangChuHC();
                        bdh.setVisible(maximized);
                        this.dispose();
                    }
                } else {
                    txt_tbdn.setText("Sai mật khẩu");
                }
            } else {
                txt_tbdn.setText("Sai tên đăng nhập");
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    public void slidershow() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int nb = 0;
                try {
                    while (true) {
                        switch (nb) {
                            case 0:
                                Thread.sleep(3000);
                                ac.jLabelXLeft(0, -500, 12, 10, img1);
                                ac.jLabelXLeft(500, 0, 12, 10, img2);
                                ac.jLabelXLeft(1000, 500, 12, 10, img3);
                                nb++;

                            case 1:
                                Thread.sleep(3000);
                                ac.jLabelXLeft(-500, -1000, 12, 10, img1);
                                ac.jLabelXLeft(0, -500, 12, 10, img2);
                                ac.jLabelXLeft(500, 0, 12, 10, img3);
                                nb++;

                            case 2:
                                Thread.sleep(3000);
                                ac.jLabelXRight(-1000, -500, 12, 10, img1);
                                ac.jLabelXRight(-500, 0, 12, 10, img2);
                                ac.jLabelXRight(0, 500, 12, 10, img3);
                                nb++;
                            case 3:
                                Thread.sleep(3000);
                                ac.jLabelXRight(-500, 0, 12, 10, img1);
                                ac.jLabelXRight(0, 500, 12, 10, img2);
                                ac.jLabelXRight(500, 1000, 12, 10, img3);
                                nb = 0;

                        }
                    }
                } catch (Exception e) {

                }
            }
        }).start();
    }

    private static final int setInterval() {
        if (interval == 1) {
            timer.cancel();

        }

        return --interval;
    }

    public void checkuser() {
        String email = txt_email.getText();
        try {
            TaiKhoan nhanVien = uDAO.findById(email);
            if (nhanVien != null) {
                this.sendmail();
                pnlBody.removeAll();
                pnlBody.repaint();
                pnlBody.revalidate();
                pnlBody.add(pnlNews2);
                pnlBody.repaint();
                pnlBody.revalidate();
                String secs = "100";
                int delay = 1000;
                int period = 1000;
                timer = new Timer();
                interval = Integer.parseInt(secs);
                timer.scheduleAtFixedRate(new TimerTask() {

                    public void run() {
                        System.out.println(setInterval());
                        String s = String.valueOf(interval);
                        txtdemso.setText(s);
                        if (interval == 0) {
                            txtthongbao.setText("Lấy lại mã xác nhận");
                            txtdemso.setText(null);
                            macode.setText(null);
                        }
                    }
                }, delay, period);

            } else {
                lbl_tbuser.setText("Sai tên Email");
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    public void checkcode(String code) {
        String code2 = txt_code.getText();
        if (code2.equals(code)) {
            pnlBody.removeAll();
            pnlBody.repaint();
            pnlBody.revalidate();
            pnlBody.add(pnlNews3);
            pnlBody.repaint();
            pnlBody.revalidate();
        } else {
            txtthongbao.setText("Vui lòng nhập đúng mã xác nhận !");
        }
    }

    public void sendmail(String email, String code) {
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true");
            Authenticator auth;
            auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("quangllph08786@fpt.edu.vn", "1123581321345589");
                }
            };
            Session session = Session.getInstance(props, auth);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("quangllph08786@fpt.edu.vn"));
            msg.setSubject("Mã Xác Nhận Tài Khoản");
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            msg.setContent(" Mã xác nhận tài khoản của bạn : " + code, "text/html");
            Transport.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.print(ex.toString());
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
        jPanel2 = new javax.swing.JPanel();
        left = new javax.swing.JPanel();
        img2 = new javax.swing.JLabel();
        img3 = new javax.swing.JLabel();
        img1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlBody = new javax.swing.JPanel();
        pnlNews = new javax.swing.JPanel();
        pnlTimeline1 = new javax.swing.JPanel();
        lbl_login = new javax.swing.JLabel();
        lbl_user = new javax.swing.JLabel();
        txt_UserName = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        lbl_pass = new javax.swing.JLabel();
        pas_PassWord = new javax.swing.JPasswordField();
        jSeparator2 = new javax.swing.JSeparator();
        btn_login = new javax.swing.JButton();
        lbl_quenmk = new javax.swing.JLabel();
        lbl_hotro = new javax.swing.JLabel();
        txt_tbdn = new javax.swing.JLabel();
        pnlNews1 = new javax.swing.JPanel();
        pnlTimeline2 = new javax.swing.JPanel();
        lbl_login1 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        lbl_pass1 = new javax.swing.JLabel();
        btn_login1 = new javax.swing.JButton();
        lbl_tbuser = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        pnlNews2 = new javax.swing.JPanel();
        pnlTimeline3 = new javax.swing.JPanel();
        lbl_login2 = new javax.swing.JLabel();
        lbl_user2 = new javax.swing.JLabel();
        txt_code = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        lbl_pass2 = new javax.swing.JLabel();
        btn_login2 = new javax.swing.JButton();
        txtthongbao = new javax.swing.JLabel();
        txtdemso = new javax.swing.JLabel();
        macode = new javax.swing.JLabel();
        pnlNews3 = new javax.swing.JPanel();
        pnlTimeline5 = new javax.swing.JPanel();
        lbl_login4 = new javax.swing.JLabel();
        lbl_user4 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        lbl_pass4 = new javax.swing.JLabel();
        txt_mk2 = new javax.swing.JPasswordField();
        jSeparator8 = new javax.swing.JSeparator();
        btn_login4 = new javax.swing.JButton();
        lbl_tbdmk = new javax.swing.JLabel();
        txt_mk1 = new javax.swing.JPasswordField();
        lbl_user7 = new javax.swing.JLabel();
        txt_tentk = new javax.swing.JTextField();
        jSeparator9 = new javax.swing.JSeparator();
        pnlNews4 = new javax.swing.JPanel();
        pnlTimeline6 = new javax.swing.JPanel();
        lbl_login5 = new javax.swing.JLabel();
        lbl_user5 = new javax.swing.JLabel();
        btn_login5 = new javax.swing.JButton();
        lbl_user6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Đăng Nhập");
        setUndecorated(true);
        setSize(new java.awt.Dimension(1000, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHeaderLayout.createSequentialGroup()
                .addGap(0, 815, Short.MAX_VALUE)
                .addComponent(btnMinimize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnExit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMinimize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        getContentPane().add(pnlHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 920, 30));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        left.setBackground(new java.awt.Color(255, 255, 255));
        left.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        img2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/2.png"))); // NOI18N
        left.add(img2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 0, 470, 670));

        img3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/3.png"))); // NOI18N
        left.add(img3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 0, 470, 670));

        img1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/grap.png"))); // NOI18N
        left.add(img1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 670));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(left, javax.swing.GroupLayout.PREFERRED_SIZE, 470, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(left, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 31, 470, 680));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/tải xuống.png"))); // NOI18N

        pnlBody.setLayout(new java.awt.CardLayout());

        pnlNews.setBackground(new java.awt.Color(255, 255, 255));

        pnlTimeline1.setBackground(new java.awt.Color(255, 255, 255));

        lbl_login.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lbl_login.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_login.setText("Đăng nhập");

        lbl_user.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbl_user.setText("Email :");

        txt_UserName.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txt_UserName.setText("gv01@gmail.com");
        txt_UserName.setToolTipText("");
        txt_UserName.setBorder(null);
        txt_UserName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_UserNameActionPerformed(evt);
            }
        });
        txt_UserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_UserNameKeyPressed(evt);
            }
        });

        lbl_pass.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbl_pass.setText("Mật khẩu :");

        pas_PassWord.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        pas_PassWord.setBorder(null);
        pas_PassWord.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pas_PassWordKeyPressed(evt);
            }
        });

        btn_login.setBackground(new java.awt.Color(255, 255, 255));
        btn_login.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        btn_login.setText("Đăng Nhập");
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });

        lbl_quenmk.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbl_quenmk.setText("Quên mật khẩu?");
        lbl_quenmk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_quenmkMouseClicked(evt);
            }
        });

        lbl_hotro.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbl_hotro.setText("Trợ giúp.");

        txt_tbdn.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        txt_tbdn.setForeground(new java.awt.Color(255, 0, 0));
        txt_tbdn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlTimeline1Layout = new javax.swing.GroupLayout(pnlTimeline1);
        pnlTimeline1.setLayout(pnlTimeline1Layout);
        pnlTimeline1Layout.setHorizontalGroup(
            pnlTimeline1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimeline1Layout.createSequentialGroup()
                .addGroup(pnlTimeline1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTimeline1Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(pnlTimeline1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_pass, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2)
                            .addComponent(lbl_user, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_UserName, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1)
                            .addComponent(pas_PassWord)
                            .addComponent(txt_tbdn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(pnlTimeline1Layout.createSequentialGroup()
                                .addComponent(lbl_quenmk)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_hotro))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTimeline1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))))
                    .addGroup(pnlTimeline1Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(lbl_login, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        pnlTimeline1Layout.setVerticalGroup(
            pnlTimeline1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimeline1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(lbl_login)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(lbl_user)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_UserName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(lbl_pass)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pas_PassWord, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(txt_tbdn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(pnlTimeline1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_quenmk, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_hotro, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout pnlNewsLayout = new javax.swing.GroupLayout(pnlNews);
        pnlNews.setLayout(pnlNewsLayout);
        pnlNewsLayout.setHorizontalGroup(
            pnlNewsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
            .addGroup(pnlNewsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlNewsLayout.createSequentialGroup()
                    .addComponent(pnlTimeline1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        pnlNewsLayout.setVerticalGroup(
            pnlNewsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 537, Short.MAX_VALUE)
            .addGroup(pnlNewsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlTimeline1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBody.add(pnlNews, "card4");

        pnlNews1.setBackground(new java.awt.Color(245, 245, 245));

        pnlTimeline2.setBackground(new java.awt.Color(255, 255, 255));

        lbl_login1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lbl_login1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_login1.setText("Quên mật khẩu");

        lbl_pass1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbl_pass1.setText("Email");

        btn_login1.setBackground(new java.awt.Color(0, 0, 0));
        btn_login1.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        btn_login1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_next_page_32px_1.png"))); // NOI18N
        btn_login1.setText("Tiếp Theo");
        btn_login1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_login1ActionPerformed(evt);
            }
        });

        lbl_tbuser.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        lbl_tbuser.setForeground(new java.awt.Color(255, 51, 51));
        lbl_tbuser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        txt_email.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txt_email.setToolTipText("");
        txt_email.setBorder(null);
        txt_email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_emailKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlTimeline2Layout = new javax.swing.GroupLayout(pnlTimeline2);
        pnlTimeline2.setLayout(pnlTimeline2Layout);
        pnlTimeline2Layout.setHorizontalGroup(
            pnlTimeline2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimeline2Layout.createSequentialGroup()
                .addGroup(pnlTimeline2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTimeline2Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(pnlTimeline2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbl_pass1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlTimeline2Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(btn_login1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_tbuser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txt_email)))
                    .addGroup(pnlTimeline2Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(lbl_login1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        pnlTimeline2Layout.setVerticalGroup(
            pnlTimeline2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimeline2Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(lbl_login1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addComponent(lbl_pass1)
                .addGap(8, 8, 8)
                .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlTimeline2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTimeline2Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(lbl_tbuser, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlTimeline2Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(btn_login1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(150, 150, 150))
        );

        javax.swing.GroupLayout pnlNews1Layout = new javax.swing.GroupLayout(pnlNews1);
        pnlNews1.setLayout(pnlNews1Layout);
        pnlNews1Layout.setHorizontalGroup(
            pnlNews1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
            .addGroup(pnlNews1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlNews1Layout.createSequentialGroup()
                    .addComponent(pnlTimeline2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        pnlNews1Layout.setVerticalGroup(
            pnlNews1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 537, Short.MAX_VALUE)
            .addGroup(pnlNews1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlTimeline2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBody.add(pnlNews1, "card4");

        pnlNews2.setBackground(new java.awt.Color(245, 245, 245));

        pnlTimeline3.setBackground(new java.awt.Color(255, 255, 255));

        lbl_login2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lbl_login2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_login2.setText("Quên mật khẩu");

        lbl_user2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbl_user2.setText("Chúng tôi đã gửi một mã xác nhận đến Email của bạn.");

        txt_code.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txt_code.setToolTipText("");
        txt_code.setBorder(null);
        txt_code.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_codeKeyPressed(evt);
            }
        });

        lbl_pass2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbl_pass2.setForeground(new java.awt.Color(255, 51, 51));
        lbl_pass2.setText("Nhập mã xác nhận");

        btn_login2.setBackground(new java.awt.Color(0, 0, 0));
        btn_login2.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        btn_login2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_next_page_32px_1.png"))); // NOI18N
        btn_login2.setText("Tiếp Theo");
        btn_login2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_login2ActionPerformed(evt);
            }
        });

        txtthongbao.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        txtthongbao.setForeground(new java.awt.Color(255, 51, 51));
        txtthongbao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtthongbaoMouseClicked(evt);
            }
        });

        txtdemso.setBackground(new java.awt.Color(255, 102, 102));
        txtdemso.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        macode.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlTimeline3Layout = new javax.swing.GroupLayout(pnlTimeline3);
        pnlTimeline3.setLayout(pnlTimeline3Layout);
        pnlTimeline3Layout.setHorizontalGroup(
            pnlTimeline3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimeline3Layout.createSequentialGroup()
                .addGroup(pnlTimeline3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTimeline3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlTimeline3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlTimeline3Layout.createSequentialGroup()
                                .addComponent(lbl_pass2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlTimeline3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlTimeline3Layout.createSequentialGroup()
                                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(pnlTimeline3Layout.createSequentialGroup()
                                        .addComponent(txt_code)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtdemso, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTimeline3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(pnlTimeline3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn_login2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtthongbao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(35, 35, 35))))
                    .addGroup(pnlTimeline3Layout.createSequentialGroup()
                        .addGroup(pnlTimeline3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlTimeline3Layout.createSequentialGroup()
                                .addGap(112, 112, 112)
                                .addComponent(lbl_login2, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlTimeline3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbl_user2))
                            .addComponent(macode, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlTimeline3Layout.setVerticalGroup(
            pnlTimeline3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimeline3Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(lbl_login2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addGroup(pnlTimeline3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTimeline3Layout.createSequentialGroup()
                        .addComponent(lbl_user2)
                        .addGap(55, 55, 55)
                        .addGroup(pnlTimeline3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_pass2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_code, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)))
                    .addComponent(txtdemso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtthongbao, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(btn_login2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(117, 117, 117)
                .addComponent(macode, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlNews2Layout = new javax.swing.GroupLayout(pnlNews2);
        pnlNews2.setLayout(pnlNews2Layout);
        pnlNews2Layout.setHorizontalGroup(
            pnlNews2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
            .addGroup(pnlNews2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlNews2Layout.createSequentialGroup()
                    .addComponent(pnlTimeline3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        pnlNews2Layout.setVerticalGroup(
            pnlNews2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 537, Short.MAX_VALUE)
            .addGroup(pnlNews2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlTimeline3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBody.add(pnlNews2, "card4");

        pnlNews3.setBackground(new java.awt.Color(245, 245, 245));

        pnlTimeline5.setBackground(new java.awt.Color(255, 255, 255));

        lbl_login4.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lbl_login4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_login4.setText("Quên mật khẩu");

        lbl_user4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbl_user4.setText("Mật khẩu mới :");

        lbl_pass4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbl_pass4.setText("Nhập lại mật khẩu :");

        txt_mk2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txt_mk2.setBorder(null);
        txt_mk2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_mk2KeyPressed(evt);
            }
        });

        btn_login4.setBackground(new java.awt.Color(0, 0, 0));
        btn_login4.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        btn_login4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_checkmark_32px.png"))); // NOI18N
        btn_login4.setText("Đồng Ý");
        btn_login4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_login4ActionPerformed(evt);
            }
        });

        lbl_tbdmk.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        lbl_tbdmk.setForeground(new java.awt.Color(255, 51, 51));

        txt_mk1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txt_mk1.setBorder(null);
        txt_mk1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_mk1KeyPressed(evt);
            }
        });

        lbl_user7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbl_user7.setText("Tên Email :");

        txt_tentk.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txt_tentk.setToolTipText("");
        txt_tentk.setBorder(null);
        txt_tentk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_tentkKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlTimeline5Layout = new javax.swing.GroupLayout(pnlTimeline5);
        pnlTimeline5.setLayout(pnlTimeline5Layout);
        pnlTimeline5Layout.setHorizontalGroup(
            pnlTimeline5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTimeline5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlTimeline5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator8)
                    .addComponent(jSeparator7)
                    .addComponent(txt_mk2)
                    .addComponent(lbl_tbdmk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_pass4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_user4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_mk1, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addComponent(lbl_user7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_tentk)
                    .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(77, 77, 77))
            .addGroup(pnlTimeline5Layout.createSequentialGroup()
                .addGroup(pnlTimeline5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTimeline5Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(lbl_login4, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlTimeline5Layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(btn_login4, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(106, Short.MAX_VALUE))
        );
        pnlTimeline5Layout.setVerticalGroup(
            pnlTimeline5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimeline5Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(lbl_login4)
                .addGap(37, 37, 37)
                .addComponent(lbl_user7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_tentk, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(lbl_user4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_mk1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(lbl_pass4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_mk2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_tbdmk, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_login4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        javax.swing.GroupLayout pnlNews3Layout = new javax.swing.GroupLayout(pnlNews3);
        pnlNews3.setLayout(pnlNews3Layout);
        pnlNews3Layout.setHorizontalGroup(
            pnlNews3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
            .addGroup(pnlNews3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlNews3Layout.createSequentialGroup()
                    .addComponent(pnlTimeline5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        pnlNews3Layout.setVerticalGroup(
            pnlNews3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 537, Short.MAX_VALUE)
            .addGroup(pnlNews3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlTimeline5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBody.add(pnlNews3, "card4");

        pnlNews4.setBackground(new java.awt.Color(245, 245, 245));

        pnlTimeline6.setBackground(new java.awt.Color(255, 255, 255));

        lbl_login5.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lbl_login5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_login5.setText("Quên mật khẩu");

        lbl_user5.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        lbl_user5.setText("Bạn đã thay đổi mật khẩu thành công !");

        btn_login5.setBackground(new java.awt.Color(0, 0, 0));
        btn_login5.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        btn_login5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_login_32px.png"))); // NOI18N
        btn_login5.setText("Đăng Nhập");
        btn_login5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_login5ActionPerformed(evt);
            }
        });

        lbl_user6.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        lbl_user6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_ok_48px.png"))); // NOI18N

        javax.swing.GroupLayout pnlTimeline6Layout = new javax.swing.GroupLayout(pnlTimeline6);
        pnlTimeline6.setLayout(pnlTimeline6Layout);
        pnlTimeline6Layout.setHorizontalGroup(
            pnlTimeline6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimeline6Layout.createSequentialGroup()
                .addGroup(pnlTimeline6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTimeline6Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(lbl_login5, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlTimeline6Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(lbl_user6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_user5, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTimeline6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_login5, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
        );
        pnlTimeline6Layout.setVerticalGroup(
            pnlTimeline6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimeline6Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(lbl_login5)
                .addGroup(pnlTimeline6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTimeline6Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(lbl_user6))
                    .addGroup(pnlTimeline6Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(lbl_user5)))
                .addGap(74, 74, 74)
                .addComponent(btn_login5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(184, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlNews4Layout = new javax.swing.GroupLayout(pnlNews4);
        pnlNews4.setLayout(pnlNews4Layout);
        pnlNews4Layout.setHorizontalGroup(
            pnlNews4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
            .addGroup(pnlNews4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlNews4Layout.createSequentialGroup()
                    .addComponent(pnlTimeline6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        pnlNews4Layout.setVerticalGroup(
            pnlNews4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 537, Short.MAX_VALUE)
            .addGroup(pnlNews4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlTimeline6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBody.add(pnlNews4, "card4");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(115, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(98, 98, 98))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addComponent(pnlBody, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(580, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(102, 102, 102)
                    .addComponent(pnlBody, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(41, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 450, 680));

        setSize(new java.awt.Dimension(923, 710));
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

    private void txt_UserNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_UserNameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pas_PassWord.requestFocus();
        }
    }//GEN-LAST:event_txt_UserNameKeyPressed

    private void pas_PassWordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pas_PassWordKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.Login();
        }
    }//GEN-LAST:event_pas_PassWordKeyPressed

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed
        this.Login();
    }//GEN-LAST:event_btn_loginActionPerformed

    private void lbl_quenmkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_quenmkMouseClicked
        pnlBody.removeAll();
        pnlBody.repaint();
        pnlBody.revalidate();
        pnlBody.add(pnlNews1);
        pnlBody.repaint();
        pnlBody.revalidate();
    }//GEN-LAST:event_lbl_quenmkMouseClicked

    private void btn_login1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_login1ActionPerformed
        this.checkuser();
    }//GEN-LAST:event_btn_login1ActionPerformed

    private void btn_login2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_login2ActionPerformed

        String code = macode.getText();
        System.out.println(code);
        String ma = txt_code.getText();
        System.out.println(ma);
        if (ma.equals(code)) {
            pnlBody.removeAll();
            pnlBody.repaint();
            pnlBody.revalidate();
            pnlBody.add(pnlNews3);
            pnlBody.repaint();
            pnlBody.revalidate();
        } else {
            txtthongbao.setText("Vui lòng kiểm tra lại.");
        }

    }//GEN-LAST:event_btn_login2ActionPerformed

    private void txt_codeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_codeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_codeKeyPressed

    private void txt_mk2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_mk2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_mk2KeyPressed

    private void btn_login4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_login4ActionPerformed

        this.updatepass();
    }//GEN-LAST:event_btn_login4ActionPerformed

    private void txt_mk1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_mk1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_mk1KeyPressed

    private void btn_login5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_login5ActionPerformed
        pnlBody.removeAll();
        pnlBody.repaint();
        pnlBody.revalidate();
        pnlBody.add(pnlNews);
        pnlBody.repaint();
        pnlBody.revalidate();
    }//GEN-LAST:event_btn_login5ActionPerformed

    private void txt_emailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_emailKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_emailKeyPressed

    private void txt_tentkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_tentkKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tentkKeyPressed

    private void txtthongbaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtthongbaoMouseClicked
        this.checkuser();
        txtthongbao.setText(null);
    }//GEN-LAST:event_txtthongbaoMouseClicked

    private void txt_UserNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_UserNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_UserNameActionPerformed

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
            java.util.logging.Logger.getLogger(dangnhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dangnhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dangnhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dangnhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dangnhap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnMinimize;
    private javax.swing.JButton btn_login;
    private javax.swing.JButton btn_login1;
    private javax.swing.JButton btn_login2;
    private javax.swing.JButton btn_login4;
    private javax.swing.JButton btn_login5;
    private javax.swing.JLabel img1;
    private javax.swing.JLabel img2;
    private javax.swing.JLabel img3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lbl_hotro;
    private javax.swing.JLabel lbl_login;
    private javax.swing.JLabel lbl_login1;
    private javax.swing.JLabel lbl_login2;
    private javax.swing.JLabel lbl_login4;
    private javax.swing.JLabel lbl_login5;
    private javax.swing.JLabel lbl_pass;
    private javax.swing.JLabel lbl_pass1;
    private javax.swing.JLabel lbl_pass2;
    private javax.swing.JLabel lbl_pass4;
    private javax.swing.JLabel lbl_quenmk;
    private javax.swing.JLabel lbl_tbdmk;
    private javax.swing.JLabel lbl_tbuser;
    private javax.swing.JLabel lbl_user;
    private javax.swing.JLabel lbl_user2;
    private javax.swing.JLabel lbl_user4;
    private javax.swing.JLabel lbl_user5;
    private javax.swing.JLabel lbl_user6;
    private javax.swing.JLabel lbl_user7;
    private javax.swing.JPanel left;
    private javax.swing.JLabel macode;
    private javax.swing.JPasswordField pas_PassWord;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlNews;
    private javax.swing.JPanel pnlNews1;
    private javax.swing.JPanel pnlNews2;
    private javax.swing.JPanel pnlNews3;
    private javax.swing.JPanel pnlNews4;
    private javax.swing.JPanel pnlTimeline1;
    private javax.swing.JPanel pnlTimeline2;
    private javax.swing.JPanel pnlTimeline3;
    private javax.swing.JPanel pnlTimeline5;
    private javax.swing.JPanel pnlTimeline6;
    private javax.swing.JTextField txt_UserName;
    private javax.swing.JTextField txt_code;
    private javax.swing.JTextField txt_email;
    private javax.swing.JPasswordField txt_mk1;
    private javax.swing.JPasswordField txt_mk2;
    private javax.swing.JLabel txt_tbdn;
    private javax.swing.JTextField txt_tentk;
    private javax.swing.JLabel txtdemso;
    private javax.swing.JLabel txtthongbao;
    // End of variables declaration//GEN-END:variables

}

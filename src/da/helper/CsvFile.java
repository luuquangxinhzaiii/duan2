/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.helper;

import da.dao.DiemDanhDAO;
import da.dao.HocSinhDAO;
import da.dao.LopHocDAO;
import da.dao.PhanCongDAO;
import da.dao.diemDAO;
import da.model.Diem;
import da.model.HocSinh;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.Vector;

/**
 *
 * @author admin
 */
public class CsvFile {

    PrintWriter fileWriter1 = null;
    LopHocDAO LHDao = new LopHocDAO();
    DiemDanhDAO DDDao = new DiemDanhDAO();
    PhanCongDAO PCDao = new PhanCongDAO();
    diemDAO dDAO = new diemDAO();
    LopHocDAO lopDao = new LopHocDAO();
    HocSinhDAO hsDao = new HocSinhDAO();

    public void writeFormDMHCsv(String filePath, String lop) {
        ResultSet rs = dDAO.findByClass(lop);
        PrintWriter fileWriter1 = null;
        try {
            FileOutputStream os = new FileOutputStream(filePath);
            os.write(239);
            os.write(187);
            os.write(191);

            fileWriter1 = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            fileWriter1.println(" Mã học sinh, Họ tên, Giới tính, Điểm miệng 1, Điểm miệng 2, Điểm miệng 3, Điểm 15p 1, Điểm 15p 2, Điểm 15p 3, Điểm 1 tiết 1, Điểm 1 tiết 2, Điểm HK");
            while (rs.next()) {
                fileWriter1.append(rs.getString("mahocsinh"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hoten"));
                fileWriter1.append(",");
                fileWriter1.append(setGT(rs.getString("gioitinh")));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(""));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(""));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(""));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(""));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(""));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(""));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(""));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(""));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(""));
                fileWriter1.append("\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileWriter1.flush();
                fileWriter1.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void writeFormHSCsv(String filePath) {
        try {
            FileOutputStream os = new FileOutputStream(filePath);
            os.write(239);
            os.write(187);
            os.write(191);
            fileWriter1 = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            fileWriter1.println(" Mã học sinh, Họ tên, Giới tính, Ngày sinh, Địa chỉ, Số điện thoại, Dân tộc, Tôn giáo, Ngày vào đoàn, Nơi sinh, Cmnd, Lớp, Họ tên bố, Số điện thoại bố,Nơi công tác bố,Họ tên mẹ, Số điện thoại mẹ,Nơi công tác mẹ,Người giám hộ,Trạng thái");
            System.out.println("đã chạy y1");
            System.out.println("Bắt đầu viết");
            
            
            System.out.println("Viết thành công");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileWriter1.flush();
                fileWriter1.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void writeKQCNCsv(String tenLop, String filePath) {
        try {
            ResultSet rs = dDAO.kqHk1(tenLop);
            ResultSet rs2 = dDAO.kqHk2(tenLop);

            FileOutputStream os = new FileOutputStream(filePath);
            os.write(239);
            os.write(187);
            os.write(191);

            fileWriter1 = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            fileWriter1.println("Mã học sinh, Họ tên, Ngày sinh, Điểm học kì 1, Điểm học kì 2, Tổng kết, Học lực, Danh hiệu, Vắng có phép, Vắng không phép, Hạnh kiểm, Ghi chú");
            while (rs.next() && rs2.next()) {
                System.out.println("đã chạy 2");
                fileWriter1.append(rs.getString("mahocsinh"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hoten"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("ngaysinh"));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(rs.getFloat("TBhocKi1")));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(rs2.getFloat("TBhocKi2")));
                fileWriter1.append(",");
                Float rate = (rs.getFloat("TBhocKi1") + rs2.getFloat("TBhocKi2")) / 2;
                fileWriter1.append(String.valueOf((double) Math.round(rate * 10) / 10));
                fileWriter1.append(",");
                if (rate >= 8) {
                    fileWriter1.append("Giỏi");
                    fileWriter1.append(",");
                    fileWriter1.append("Học Sinh Giỏi");
                } else if (rate >= 7 && rate < 8) {
                    fileWriter1.append("Khá");
                    fileWriter1.append(",");
                    fileWriter1.append("Học Sinh Khá");
                } else if (rate >= 5 && rate < 7) {
                    fileWriter1.append("Trung Bình");
                    fileWriter1.append(",");
                    fileWriter1.append("Không Có");
                } else if (rate < 5) {
                    fileWriter1.append("Yếu");
                    fileWriter1.append(",");
                    fileWriter1.append("Không Có");
                }
                fileWriter1.append(",");
                int sobuoinghicp = DDDao.selectNghiCoPhep(rs.getString("mahocsinh"));
                fileWriter1.append(String.valueOf(sobuoinghicp));
                fileWriter1.append(",");
                int sobuoinghikp = DDDao.selectNghiKoCoPhep(rs.getString("mahocsinh"));
                fileWriter1.append(String.valueOf(sobuoinghikp));
                fileWriter1.append(",");
                int tongNghi = sobuoinghicp + sobuoinghikp;
                if (tongNghi >= 30) {
                    fileWriter1.append("Yếu");
                } else if (tongNghi >= 10 && tongNghi < 30) {
                    fileWriter1.append("Trung Bình");
                } else if (tongNghi >= 5 && tongNghi < 10) {
                    fileWriter1.append("Khá");
                } else if (tongNghi < 5) {
                    fileWriter1.append("Tốt");
                }
                fileWriter1.append(",");
                if (tongNghi > 20 || rate < 5 && rate >= 3) {
                    fileWriter1.append("rèn luyện lại");
                } else if (tongNghi > 20 || rate < 3) {
                    fileWriter1.append("Lưu Ban");
                } else {
                    fileWriter1.append("");
                }
                fileWriter1.append("\n");
            }
            System.out.println("đã chạy 3");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileWriter1.flush();
                fileWriter1.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void writeDiemCsv(String filePath, String tenMon, String tenLop, String ki) {
        try {
            System.out.println(tenMon + "---" + tenLop + "----" + sethocki(ki) + "---");
            ResultSet rs = dDAO.LoadDataGrade(tenLop, tenMon, sethocki(ki));
            FileOutputStream os = new FileOutputStream(filePath);
            os.write(239);
            os.write(187);
            os.write(191);
            fileWriter1 = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            fileWriter1.println(" Mã học sinh, Họ tên, Điểm miệng 1, Điểm miệng 2, Điểm miệng 3, Điểm 15p 1, Điểm 15p 2, Điểm 15p 3, Điểm 1 tiết 1, Điểm 1 tiết 2, Điểm Thi, Điểm TB");
            System.out.println(rs);
            int i = 0;
            while (rs.next()) {
                i++;
                System.out.println("đã chạy y");
                fileWriter1.append(rs.getString("mahocsinh"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hoten"));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(rs.getInt("diemMieng1")));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(rs.getInt("diemMieng2")));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(rs.getInt("diemMieng3")));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(rs.getInt("diem15phut1")));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(rs.getInt("diem15phut2")));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(rs.getInt("diem15phut3")));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(rs.getFloat("diem1Tiet1")));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(rs.getFloat("diem1Tiet2")));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(rs.getFloat("diemthi")));
                fileWriter1.append(",");
                fileWriter1.append(String.valueOf(rs.getFloat("diemTBM")));
                fileWriter1.append("\n");
            }
            System.out.println(i);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileWriter1.flush();
                fileWriter1.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void writeHSCsv(String filePath, String tenlop, String nienhoc) {
        try {
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println(tenlop + "---" + nienhoc);
            ResultSet rs = hsDao.loadWithCSV(tenlop, nienhoc);
            FileOutputStream os = new FileOutputStream(filePath);
            os.write(239);
            os.write(187);
            os.write(191);
            fileWriter1 = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            fileWriter1.println(" Mã học sinh, Họ tên, Giới tính, Ngày sinh, Địa chỉ, Số điện thoại, Dân tộc, Tôn giáo, Ngày vào đoàn, Nơi sinh, Cmnd, Lớp, Họ tên bố, Số điện thoại bố,Nơi công tác bố,Họ tên mẹ, Số điện thoại mẹ,Nơi công tác mẹ,Người giám hộ,Trạng thái");
            System.out.println("đã chạy y1");
            System.out.println("Bắt đầu viết");
            while (rs.next()) {
                fileWriter1.append(rs.getString("mahocsinh"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hoten"));
                fileWriter1.append(",");
                fileWriter1.append(setGT(rs.getString("gioitinh")));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("ngaysinh"));
                fileWriter1.append(",");
                fileWriter1.append('"');
                fileWriter1.append(rs.getString("diachi"));
                fileWriter1.append('"');
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("dienthoai"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("dantoc"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("tongiao"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("ngayvaodoan"));
                fileWriter1.append(",");
                fileWriter1.append('"');
                fileWriter1.append(rs.getString("noisinh"));
                fileWriter1.append('"');
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("cmnd"));
                fileWriter1.append(",");
                fileWriter1.append(tenlop);
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hoten_bo"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("dienthoai_bo"));
                fileWriter1.append(",");
                fileWriter1.append('"');
                fileWriter1.append(rs.getString("dv_cong_tac_bo"));
                fileWriter1.append('"');
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hoten_me"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("dienthoai_me"));
                fileWriter1.append(",");
                fileWriter1.append('"');
                fileWriter1.append(rs.getString("dv_cong_tac_me"));
                fileWriter1.append('"');
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("nguoidamho"));
                fileWriter1.append(",");
                fileWriter1.append(setTT(rs.getString("trangthai")));
                fileWriter1.append("\n");
            }
            System.out.println("Viết thành công");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileWriter1.flush();
                fileWriter1.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void writeLopCsv(String filePath, String khoi, String nienhoc, String ki) {
        try {
            System.out.println(khoi + "---" + nienhoc + "----" + sethocki(ki) + "---");
            ResultSet rs = LHDao.selectLopByNienhoc(nienhoc);
            FileOutputStream os = new FileOutputStream(filePath);
            os.write(239);
            os.write(187);
            os.write(191);
            fileWriter1 = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            fileWriter1.println(" Mã lớp, Tên lớp, Niên học");
            System.out.println("bắt đầu ghi");
            int i = 0;
            while (rs.next()) {
                i++;
                System.out.println("đã chạy y");
                fileWriter1.append(rs.getString("malop"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("tenlop"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("nienhoc"));
                fileWriter1.append("\n");

            }
            System.out.println("Ghi thành công");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileWriter1.flush();
                fileWriter1.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void readDiemCsv(String filePath, String tenMon, String tenLop, String ki) {
        BufferedReader reader = null;
        try {
            Diem model = new Diem();
            String line = "";
            reader = new BufferedReader(new FileReader(filePath));
            reader.readLine();
            System.out.println("Bắt đầu nhập");
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 0) {
                    if(!fields[0].equals("")){
                    model.setNgay(java.sql.Date.valueOf(LocalDate.now()));
                    model.setDiemMieng1(Integer.parseInt(fields[2]));
                    model.setDiemMieng2(Integer.parseInt(fields[3]));
                    model.setDiemMieng3(Integer.parseInt(fields[4]));
                    model.setDiem15p1(Integer.parseInt(fields[5]));
                    model.setDiem15p2(Integer.parseInt(fields[6]));
                    model.setDiem15p3(Integer.parseInt(fields[7]));
                    model.setDiem1Tiet1(Float.parseFloat(fields[8]));
                    model.setDiem1Tiet2(Float.parseFloat(fields[9]));
                    model.setDiemThi(Float.parseFloat(fields[10]));
                    model.setDiemTBM(TinhDiemTB(model.getDiemMieng1(), model.getDiemMieng2(), model.getDiemMieng3(), model.getDiem15p1(), model.getDiem15p2(), model.getDiem15p3(), model.getDiem1Tiet1(), model.getDiem1Tiet2(), model.getDiemThi()));
                    model.setMapc(PCDao.selectPc(ShareHelper.TaiKhoan.getGiaovien_id(), tenLop, tenMon, sethocki(ki)).getMaPC());
                    if (hsDao.select3(fields[0]) == null) {
                        dDAO.insert(model);
                        System.out.println("Insert Thành công");
                    } else {
                        HocSinh hocSinh = hsDao.select3(fields[0]);
                        model.setMaHocSinh(hocSinh.getiD());
                        dDAO.update(model);
                        System.out.println("Update Thành công");
                    }}

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void readHSCsv(String filePath) {
        SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/YYYY");
        BufferedReader reader = null;
        try {
            HocSinh model = new HocSinh();
            String line = "";
            reader = new BufferedReader(new FileReader(filePath));
            reader.readLine();
            System.out.println("đã chạy 1");
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (fields.length > 0) {
                    if(!fields[0].equals("")){
                    model.setMaHS(fields[0]);
                    model.setHoTen(fields[1]);
                    model.setGioiTinh(getGT(fields[2]));
                    model.setNgaySinh(fromUser.parse(fields[3]));
                    model.setDiaChi(fields[4].replace("\"", ""));
                    model.setDienThoai(fields[5]);
                    model.setDanToc(fields[6]);
                    model.setTonGiao(fields[7]);
                    model.setNgayVD(fromUser.parse(fields[8]));
                    model.setNoiSinh(fields[9].replace("\"", ""));
                    model.setCmND(fields[10]);
                    model.setLop_id(lopDao.findByTenLop(fields[11]).getiD());
                    model.setHotenBo(fields[12]);
                    model.setDienThoaiBo(fields[13]);
                    model.setDvctBo(fields[14].replace("\"", ""));
                    model.setHotenBo(fields[15]);
                    model.setDienThoaiBo(fields[16]);
                    model.setDvctBo(fields[17].replace("\"", ""));
                    model.setNguoiDamHo(fields[18]);
                    model.setTrangThai(getTT(fields[19]));
                    System.out.println(fields[19]);
                    if (hsDao.select3(fields[0]) == null) {
                        hsDao.insert(model);
                        System.out.println("Insert Thành công");
                    } else {
                        hsDao.update(model);
                        System.out.println("Update Thành công");
                    }}
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private boolean sethocki(String hocki) {
        if (hocki.equals("Học kỳ 1")) {
            return true;
        } else {
            return false;
        }
    }

    private String setdat(boolean diem) {
        if (diem == true) {
            return "Đạt";
        } else {
            return "Chưa Đạt";
        }
    }

    private String setGT(String GT) {
        if (GT.equals("t")) {
            return "Nam";
        } else {
            return "Nữ";
        }
    }

    private boolean getGT(String GT) {
        if (GT.equals("Nam") || GT.equals("nam")) {
            return true;
        } else {
            return false;
        }
    }

    private String setTT(String TT) {
        if (TT.equals("t")) {
            return "Đi học";
        } else  {
            return "Nghỉ học";
        }
    }

    private boolean getTT(String TT) {
        if (TT.equals("Đi học") || TT.equals("đi học") || TT.equals("Học Đi") || TT.equals("học đi")) {
            return true;
        } else {
            return false;
        }
    }

    private String setLop(String malop) throws SQLException {
        String lop = String.valueOf(LHDao.selectWithMalop(malop));
        return lop;
    }

    private boolean getdat(String diem) {
        if (diem.equals("Đạt")) {
            return true;
        } else {
            return false;
        }
    }

    private float TinhDiemTB(int m1, int m2, int m3, int diem15pl1, int diem15pl2, int diem15pl3, float diem45pl1, float diem45pl2, float diemHK) {
        float rate = Math.round((m1 + m2 + m3 + diem15pl1 + diem15pl2 + diem15pl3 + diem45pl1 * 2 + diem45pl2 * 2 + diemHK * 3) / 13);
        float TBM = (rate * 100) / 100;
        return TBM;

    }
}

//Xuân Hải: 0988136358

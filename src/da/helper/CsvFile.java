/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.helper;

import da.dao.CsvDao;
import da.dao.PhanCongDAO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 *
 * @author admin
 */
public class CsvFile {

    PrintWriter fileWriter1 = null;
    CsvDao Dao = new CsvDao();

    public void writeFormDMHCsv(String filePath, String lop) {
        ResultSet rs = Dao.findByClass(lop);
        PrintWriter fileWriter1 = null;
        try {
            FileOutputStream os = new FileOutputStream(filePath);
            os.write(239);
            os.write(187);
            os.write(191);

            fileWriter1 = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            fileWriter1.println(" Mã học sinh, Họ tên, Ngày sinh, Điểm miệng 1, Điểm miệng 2, Điểm miệng 3, Điểm 15p 1, Điểm 15p 2, Điểm 15p 3, Điểm 1 tiết 1, Điểm 1 tiết 2, Điểm HK");

            while (rs.next()) {
                fileWriter1.append(rs.getString("mahocsinh"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hoten"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("ngaysinh"));
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

    public void writeFormCsv(String filePath, String lop) {
        ResultSet rs = Dao.findByClass(lop);
        PrintWriter fileWriter1 = null;
        try {
            FileOutputStream os = new FileOutputStream(filePath);
            os.write(239);
            os.write(187);
            os.write(191);

            fileWriter1 = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            fileWriter1.println(" Mã học sinh, Họ tên, Ngày sinh, Điểm KT thường xuyên 1, Điểm KT thường xuyên 2, Điểm KT thường xuyên 3, Điểm KT định kì 1, Điểm KT định kì 2, Điểm KT học kì");

            while (rs.next()) {
                fileWriter1.append(rs.getString("mahocsinh"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hoten"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("ngaysinh"));
                fileWriter1.append(",");
                fileWriter1.append("");
                fileWriter1.append(",");
                fileWriter1.append("");
                fileWriter1.append(",");
                fileWriter1.append("");
                fileWriter1.append(",");
                fileWriter1.append("");
                fileWriter1.append(",");
                fileWriter1.append("");
                fileWriter1.append(",");
                fileWriter1.append("");
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

//    public void writeCsv(String maLop, String maMon, String filePath, String ki) {
//        try {
//            ResultSet rs = dDao.findDG(maLop, maMon, sethocki(ki));
//
//            FileOutputStream os = new FileOutputStream(filePath);
//            os.write(239);
//            os.write(187);
//            os.write(191);
//
//            fileWriter1 = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
//            fileWriter1.println(" Mã học sinh, Họ tên, Ngày sinh, Điểm KT thường xuyên 1, Điểm KT thường xuyên 2, Điểm KT thường xuyên 3, Điểm KT định kì 1, Điểm KT định kì 2, Điểm KT học kì");
//            while (rs.next()) {
//                fileWriter1.append(rs.getString("mahocsinh"));
//                fileWriter1.append(",");
//                fileWriter1.append(rs.getString("hoten"));
//                fileWriter1.append(",");
//                fileWriter1.append(rs.getString("ngaysinh"));
//                fileWriter1.append(",");
//                fileWriter1.append(String.valueOf(setdat(rs.getBoolean("diemTX1"))));
//                fileWriter1.append(",");
//                fileWriter1.append(String.valueOf(setdat(rs.getBoolean("diemTX2"))));
//                fileWriter1.append(",");
//                fileWriter1.append(String.valueOf(setdat(rs.getBoolean("diemTX3"))));
//                fileWriter1.append(",");
//                fileWriter1.append(String.valueOf(setdat(rs.getBoolean("diemDK1"))));
//                fileWriter1.append(",");
//                fileWriter1.append(String.valueOf(setdat(rs.getBoolean("diemDK2"))));
//                fileWriter1.append(",");
//                fileWriter1.append(String.valueOf(setdat(rs.getBoolean("diemHK"))));
//                fileWriter1.append("\n");
//            }System.out.println("đã chạy 3");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//                fileWriter1.flush();
//                fileWriter1.close();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
    public void writeKQCNCsv(String tenLop, String filePath) {
        try {
            ResultSet rs = Dao.kqHk1(tenLop);
            ResultSet rs2 = Dao.kqHk2(tenLop);

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
                int sobuoinghicp = Dao.selectNghiCoPhep(rs.getString("mahocsinh"));
                fileWriter1.append(String.valueOf(sobuoinghicp));
                fileWriter1.append(",");
                int sobuoinghikp = Dao.selectNghiKoCoPhep(rs.getString("mahocsinh"));
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
            ResultSet rs = Dao.findByEve(tenMon, tenLop, sethocki(ki));
            FileOutputStream os = new FileOutputStream(filePath);
            os.write(239);
            os.write(187);
            os.write(191);
            fileWriter1 = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            fileWriter1.println(" Mã học sinh, Họ tên, Ngày sinh, Điểm miệng 1, Điểm miệng 2, Điểm miệng 3, Điểm 15p 1, Điểm 15p 2, Điểm 15p 3, Điểm 1 tiết 1, Điểm 1 tiết 2, Điểm Thi, Điểm TB");
            System.out.println("đã chạy y1");
            int i = 0;
            while (rs.next()) {
                i++;
                System.out.println("đã chạy y");
                fileWriter1.append(rs.getString("mahocsinh"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hoten"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("ngaysinh"));
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

    public void writeHSCsv(String filePath, String tenlop, String nienhoc, String ki) {
        try {
            System.out.println(tenlop + "---" + nienhoc + "----" + sethocki(ki) + "---");
            ResultSet rs = Dao.selectHS(tenlop, sethocki(ki), nienhoc);
            FileOutputStream os = new FileOutputStream(filePath);
            os.write(239);
            os.write(187);
            os.write(191);
            fileWriter1 = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            fileWriter1.println(" Mã học sinh, Họ tên, Giới tính, Ngày sinh, Địa chỉ, Số điện thoại, Dân tộc, Tôn giáo, Ngày vào đoàn, Nơi sinh, Cmnd, Lớp, Họ tên bố, Họ tên mẹ, Số điện thoại bố, Số điện thoại mẹ, Nơi công tác của bố, Nơi công tác của mẹ, Người dám hộ, Trạng thái");
            System.out.println("đã chạy y1");
            int i = 0;
            while (rs.next()) {
                i++;
                System.out.println("đã chạy y");
                fileWriter1.append(rs.getString("mahocsinh"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hoten"));
                fileWriter1.append(",");
                fileWriter1.append(setGT(rs.getString("gioitinh")));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("ngaysinh"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("diachi"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("dienthoai"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("dantoc"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("tongiao"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("ngayvaodoan"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("noisinh"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("cmnd"));
                fileWriter1.append(",");
                fileWriter1.append(setLop(rs.getString("lop")));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hotenBo"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hotenMe"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("dienthoaiBo"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("dienthoaiMe"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("dvCongTacBo"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("dvCongTacMe"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("nguoidamho"));
                fileWriter1.append(",");
                fileWriter1.append(setTT(rs.getString("trangthai")));
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

    public void writeLopCsv(String filePath, String khoi, String nienhoc, String ki) {
        try {
            System.out.println(khoi + "---" + nienhoc + "----" + sethocki(ki) + "---");
            ResultSet rs = Dao.selectLop(khoi);
            FileOutputStream os = new FileOutputStream(filePath);
            os.write(239);
            os.write(187);
            os.write(191);
            fileWriter1 = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            fileWriter1.println(" Mã lớp, Tên lớp, Giáo viên");
            System.out.println("đã chạy y1");
            int i = 0;
            while (rs.next()) {
                i++;
                System.out.println("đã chạy y");
                fileWriter1.append(rs.getString("malop"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("tenlop"));
                fileWriter1.append(",");
                fileWriter1.append(rs.getString("hoten"));
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

    public void readDiemCsv(String filePath, String tenMon, String tenLop, String ki, String nienhoc) {

        BufferedReader reader = null;
        try {
            Diem model = new Diem();
            String line = "";
            reader = new BufferedReader(new FileReader(filePath));
            reader.readLine();
            System.out.println("đã chạy 1");
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 0) {
                    model.setDiemMieng1(Integer.parseInt(fields[3]));
                    model.setDiemMieng2(Integer.parseInt(fields[4]));
                    model.setDiemMieng3(Integer.parseInt(fields[5]));
                    model.setDiem15p1(Integer.parseInt(fields[6]));
                    model.setDiem15p2(Integer.parseInt(fields[7]));
                    model.setDiem15p3(Integer.parseInt(fields[8]));
                    model.setDiem1Tiet1(Float.parseFloat(fields[9]));
                    model.setDiem1Tiet2(Float.parseFloat(fields[10]));
                    model.setDiemThi(Float.parseFloat(fields[11]));
                    model.setDiemTBM(TinhDiemTB(model.getDiemMieng1(), model.getDiemMieng2(), model.getDiemMieng3(), model.getDiem15p1(), model.getDiem15p2(), model.getDiem15p3(), model.getDiem1Tiet1(), model.getDiem1Tiet2(), model.getDiemThi()));
                    model.setMaHocSinh(fields[0]);
                    model.setMapc(Dao.findMaPC(tenMon, tenLop, sethocki(ki), nienhoc).getMaPC());
                    System.out.println(TinhDiemTB(model.getDiemMieng1(), model.getDiemMieng2(), model.getDiemMieng3(), model.getDiem15p1(), model.getDiem15p2(), model.getDiem15p3(), model.getDiem1Tiet1(), model.getDiem1Tiet2(), model.getDiemThi()));
                    Dao.update(model);

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
// public void readHSCsv(String filePath, String tenMon, String tenLop, String ki, String nienhoc) {
//
//        BufferedReader reader = null;
//        try {
//            HocSinh model = new HocSinh();
//            String line = "";
//            reader = new BufferedReader(new FileReader(filePath));
//            reader.readLine();
//            System.out.println("đã chạy 1");
//            while ((line = reader.readLine()) != null) {
//                String[] fields = line.split(",");
//                if (fields.length > 0) {
//                    model.setDiemMieng1(Integer.parseInt(fields[3]));
//                    model.setDiemMieng2(Integer.parseInt(fields[4]));
//                    model.setDiemMieng3(Integer.parseInt(fields[5]));
//                    model.setDiem15p1(Integer.parseInt(fields[6]));
//                    model.setDiem15p2(Integer.parseInt(fields[7]));
//                    model.setDiem15p3(Integer.parseInt(fields[8]));
//                    model.setDiem1Tiet1(Float.parseFloat(fields[9]));
//                    model.setDiem1Tiet2(Float.parseFloat(fields[10]));
//                    model.setDiemThi(Float.parseFloat(fields[11]));
//                    model.setDiemTBM(TinhDiemTB(model.getDiemMieng1(), model.getDiemMieng2(), model.getDiemMieng3(), model.getDiem15p1(), model.getDiem15p2(), model.getDiem15p3(), model.getDiem1Tiet1(), model.getDiem1Tiet2(), model.getDiemThi()));
//                    model.setMaHocSinh(fields[0]);
//                    model.setMapc(pcDao.findMaPC(tenMon, tenLop, sethocki(ki), nienhoc).getMaPC());
//                    System.out.println(TinhDiemTB(model.getDiemMieng1(), model.getDiemMieng2(), model.getDiemMieng3(), model.getDiem15p1(), model.getDiem15p2(), model.getDiem15p3(), model.getDiem1Tiet1(), model.getDiem1Tiet2(), model.getDiemThi()));
//                    Dao.update(model);
//
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//                reader.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

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
        if (GT.equals("1")) {
            return "Nam";
        } else {
            return "Nữ";
        }
    }

    private String setTT(String TT) {
        if (TT.equals("1")) {
            return "Đi học";
        } else {
            return "Nghỉ học";
        }
    }

    private String setLop(String malop) throws SQLException {
        String lop = String.valueOf(Dao.selectWithMalop(malop));
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

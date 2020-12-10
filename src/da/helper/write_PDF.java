/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.helper;

import com.pdfcrowd.Pdfcrowd;
import da.dao.DiemDanhDAO;
import da.dao.HocSinhDAO;
import da.dao.diemDAO;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author PC
 */
public class write_PDF {

    public void write_PDF(String filepath, String Mahocsinh, String nienhoc) throws IOException, SQLException {
        try {
            // create the API client instance
            System.out.println(filepath);
            System.out.println(Mahocsinh);
            System.out.println(nienhoc);
            System.out.println("start 1");
            String html = "";
            Pdfcrowd.HtmlToPdfClient client
                    = new Pdfcrowd.HtmlToPdfClient("kien5620", "c94142e867c91420c25d9c7d52438b35");
            HocSinhDAO hsDAO = new HocSinhDAO();
            ResultSet rs = hsDAO.selectWithMaHSandNH(Mahocsinh, nienhoc);
            if (rs.next()) {
                String gioitinh = rs.getBoolean("gioitinh") ? "Nam" : "Nữ";
                System.out.println(gioitinh);
                System.out.println("sr 1");
                html = html + "<html>\n"
                        + "    <head>\n"
                        + "        <title>Học bạ</title>\n"
                        + "        <meta charset=\"UTF-8\">\n"
                        + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                        + "    </head>\n"
                        + "    <body style=\"border-style: double;\">\n"
                        + "        <div style=\"border-style: solid; float: left; text-align: center;width: 100px;height: 100px;\">Ảnh</div>\n"
                        + "        <div style=\"text-align: center;\">\n"
                        + "            <span >CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</span> <br>\n"
                        + "            <span>Độc lập - Tự do - Hạnh phúc</span> \n"
                        + "            <hr style=\" width: 250px; align-content: center;\">\n"
                        + "        </div>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <div style=\"margin-left: 30px;\">\n"
                        + "            <h3 style=\"text-align: center;\">HỌC BẠ</h3> \n"
                        + "            <p style=\"text-align: justify;\">Họ và tên học sinh: " + rs.getString("hoten") + "          Giới tính: " + gioitinh + "</p>\n"
                        + "            <p style=\"text-align: justify;\">Ngày, tháng, năm sinh: " + rs.getDate("ngaysinh") + "          Dân tộc: " + check_null(rs.getString("dantoc")) + "         Quốc tịch: Việt Nam </p> \n"
                        + "            <p style=\"text-align: justify;\">Nơi sinh: " + check_null(rs.getString("noisinh")) + "</p> \n"
                        + "            <p style=\"text-align: justify;\">Nơi ở hiện nay: " + check_null(rs.getString("diachi")) + "</p> \n"
                        + "            <p style=\"text-align: justify;\">Họ và tên bố: " + check_null(rs.getString("hoten_bo")) + "</p>\n"
                        + "            <p style=\"text-align: justify;\">Họ và tên mẹ: " + check_null(rs.getString("hoten_me")) + "</p> \n"
                        + "            <p style=\"text-align: justify;\">Người giám hộ (nếu có): " + check_null(rs.getString("nguoidamho")) + "</p> \n"
                        + "        </div>\n"
                        + "        <table style=\"width: 100%;\" border=\"0\"> \n"
                        + "            <tbody> \n"
                        + "                <tr> \n"
                        + "                    <td> </td> \n"
                        + "                    <td style=\"text-align: right;\"><em>.........ngày....tháng...năm......           </em></td> \n"
                        + "                </tr> \n"
                        + "                <tr> <td> </td> \n"
                        + "                    <td style=\"text-align: center;\"><strong>HIỆU TRƯỞNG</strong><br><em>(Ký, ghi rõ họ tên và đóng dấu)</em></td> \n"
                        + "                </tr> \n"
                        + "            </tbody> \n"
                        + "        </table> \n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <h3 style=\"text-align: center;\">QUÁ TRÌNH HỌC TẬP</h3> \n"
                        + "        <table class=\"table-striped\" style=\"width: 100%; border: 1px solid #000000; border-color: #300808;\" border=\"1\" cellspacing=\"0\" cellpadding=\"5\"> \n"
                        + "            <tbody> \n"
                        + "                <tr> \n"
                        + "                    <td style=\"text-align: center;\"><strong>Năm học</strong></td> \n"
                        + "                    <td style=\"text-align: center;\"><strong>Lớp</strong></td> \n"
                        + "                    <td style=\"text-align: center;\"><strong>Tên trường</strong></td> \n"
                        + "                    <td style=\"text-align: center;\"><strong>Ngày nhập học/chuyển đến</strong></td> \n"
                        + "                </tr> \n"
                        + "                <tr> \n"
                        + "                    <td>" + nienhoc + " </td> \n"
                        + "                    <td> " + rs.getString("tenlop") + "</td> \n"
                        + "                    <td> Trung học cơ sở Minh Khôi</td> \n"
                        + "                    <td> </td> \n"
                        + "                </tr> \n"
                        + "            </tbody> \n"
                        + "        </table> \n"
                        + "        <p>Họ và tên học sinh: " + rs.getString("hoten") + "         Lớp: " + rs.getString("tenlop") + "Năm học " + nienhoc + "</p> \n";
            }
            html = html + "        <table class=\"table-striped\" style=\"width: 100%; border-color: #541f1f; border-width: 1px; ; width: 100%;\" border=\"1\" cellspacing=\"0\" cellpadding=\"5\">\n"
                    + "            <tbody>\n"
                    + "                <tr> \n"
                    + "                    <th rowspan=\"2\" style=\"text-align: center;\"><strong>Môn học và hoạt động giáo dục</strong></th>\n"
                    + "                    <th colspan=\"3\" style=\"text-align: center;\"><strong>Điểm trung bình hoặc xếp loại các môn</strong></th> \n"
                    + "                    <th rowspan=\"2\" style=\"text-align: center;\"><strong>Điểm hoặc xếp loại sau KT lại (nếu có)</strong></th> \n"
                    + "                    <th rowspan=\"2\" style=\"text-align: center;\"><strong>Giáo viên bộ môn ký xác nhận</strong></th> \n"
                    + "                </tr> \n"
                    + " <tr>"
                    + " <th>HKỳ 1</th>"
                    + " <th>HKỳ</th>"
                    + " <th>CN</th>"
                    + " </tr>";
            diemDAO dDAO = new diemDAO();
            DiemDanhDAO ddDAO = new DiemDanhDAO();
            ResultSet rs_hk1 = dDAO.kqHk1_mon(Mahocsinh, nienhoc);
            ResultSet rs_hk2 = dDAO.kqHk2_mon(Mahocsinh, nienhoc);
            while (rs_hk1.next() && rs_hk2.next()) {
                int CP1 = ddDAO.selectNghi_ki(rs.getString("mahocsinh"), true, true);
                int KP1 = ddDAO.selectNghi_ki(rs.getString("mahocsinh"), true, false);
                int CP2 = ddDAO.selectNghi_ki(rs.getString("mahocsinh"), false, true);
                int KP2 = ddDAO.selectNghi_ki(rs.getString("mahocsinh"), false, false);
                int Total_1 = CP1 + KP1;
                int Total_2 = CP2 + KP2;
                int Total = CP1 + KP1 + CP2 + KP2;
                Float rate = (rs_hk1.getFloat("TBhocKi1") + rs_hk2.getFloat("TBhocKi2")) / 2;
                html = html + "                <tr> \n"
                        + "                    <td>" + rs_hk1.getString("TEN_MON") + "</td> \n"
                        + "                    <td>" + rs_hk1.getFloat("TBhocKi1") + " </td> \n"
                        + "                    <td>" + rs_hk2.getFloat("TBhocKi2") + "</td> \n"
                        + "                    <td>" + (double) Math.round(rate * 10) / 10 + " </td> \n"
                        + "                    <td> </td> \n"
                        + "                    <td> </td> \n"
                        + "                </tr> \n"
                        + "            </tbody> \n"
                        + "        </table> \n"
                        + "        <p>Trong bảng này sửa ở : .................... chỗ, Thuộc các môn học: ..........................</p>"
                        + "        <table style=\"width: 100%;\" border=\"0\"> \n"
                        + "            <tbody> \n"
                        + "                <tr> \n"
                        + "                    <td> </td> \n"
                        + "                    <td style=\"text-align: right;\"><em>.........ngày....tháng...năm......           </em></td> \n"
                        + "                </tr> \n"
                        + "                <tr> "
                        + "                   <td style=\"text-align: left;\"><strong>Xác nhận của giáo viên chủ nghiệm</strong><br><em style=\"text-align: center;\">(Ký và ghi rõ họ tên)</em></td> \n"
                        + "                    <td style=\"text-align: right;\"><strong>Xác nhận của Hiệu trưởng</strong><br><em>(Ký, ghi rõ họ tên và đóng dấu)</em></td> \n"
                        + "                </tr> \n"
                        + "            </tbody> \n"
                        + "        </table> \n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <br>\n"
                        + "        <p style=\"text-align: justify;\">Trường:.................................Huyện:.........................Tỉnh:...........................................</p> \n"
                        + "        <table class=\"table-striped\" style=\"border-color: #401818; border-width: 1px; width: 100%; height: 92px;\" border=\"1\" cellspacing=\"0\" cellpadding=\"5\"> \n"
                        + "            <tbody> \n"
                        + "                <tr style=\"height: 23px;\"> \n"
                        + "                    <th rowspan=\"2\" style=\"text-align: center; height: 23px;\"><strong>Học kì</strong></th>\n"
                        + "                    <th colspan=\"2\" style=\"text-align: center; height: 23px;\"><strong>Kiết quả xếp loại</strong></th> \n"
                        + "                    <th rowspan=\"2\" style=\"text-align: center; height: 23px;\"><strong>Số ngày nghỉ học cả năm</strong></th>\n"
                        + "                    <th colspan=\"2\" style=\"text-align: center; height: 23px;\"><strong>Xếp loại sau khi KT lại các môn hoặc rèn luyện thêm về hạnh kiểm</strong></th>\n"
                        + "                    <th rowspan=\"2\" ><p> -Được lên lớp thẳng: <br>.............................................</p></th>\n"
                        + "                </tr> \n"
                        + "                <tr style=\"height: 23px;\"> \n"
                        + "                    <th style=\"text-align: center; height: 23px;\"><strong>HL</strong></th>\n"
                        + "                    <th style=\"text-align: center; height: 23px;\"><strong>HK</strong></th> \n"
                        + "                    <th style=\"text-align: center; height: 23px;\"><strong>HL</strong></th>\n"
                        + "                    <th style=\"text-align: center; height: 23px;\"><strong>HK</strong></th>\n"
                        + "                </tr> \n"
                        + "                <tr style=\"height: 23px;\"> <td style=\"height: 23px;\">Học kỳ 1</td> \n"
                        + "                    <td style=\"height: 23px;\">" + hocluc(rs_hk1.getFloat("TBhocKi1")) + " </td> \n"
                        + "                    <td style=\"height: 23px;\">" + hanh_kiem(Total_1) + " </td> \n"
                        + "                    <td style=\"height: 23px;\" rowspan=\"3\"> " + Total + "</td> \n"
                        + "                    <td style=\"height: 23px;\"> </td> \n"
                        + "                    <td style=\"height: 23px;\"> </td> \n"
                        + "                    <td style=\"height: 69px;\" rowspan=\"1\"> - Được lên lớp sau khi KT lại các môn hoặc rèn luyện thêm về hạnh kiểm: <br>...........................................</td> \n"
                        + "                </tr>"
                        + "                <tr style=\"height: 23px;\"> <td style=\"height: 23px;\">Học kỳ 2</td> \n"
                        + "                    <td style=\"height: 23px;\"> " + hocluc(rs_hk1.getFloat("TBhocKi1")) + "</td> \n"
                        + "                    <td style=\"height: 23px;\"> " + hanh_kiem(Total_2) + "</td> \n"
                        + "                    <td style=\"height: 23px;\"> </td> \n"
                        + "                    <td style=\"height: 23px;\"> </td> \n"
                        + "                    <td style=\"height: 69px;\" rowspan=\"2\">-Không được lên lớp: <br>................................................... </td> \n"
                        + "                </tr>"
                        + "                <tr style=\"height: 23px;\"> <td style=\"height: 23px;\">Cả năm</td> \n"
                        + "                    <td style=\"height: 23px;\">" + hocluc(rate) + " </td> \n"
                        + "                    <td style=\"height: 23px;\">" + hanh_kiem(Total) + " </td> \n"
                        + "                    <td style=\"height: 23px;\"> </td> \n"
                        + "                    <td style=\"height: 23px;\"> </td> \n"
                        + "                </tr>"
                        + "            </tbody> \n"
                        + "        </table> \n";
            }
            html = html + "        <p style=\"text-align: justify;\"> -Có chứng chỉ nghề phổ thông: ..................................................................... Loại: ....................................</p> \n"
                    + "        <p> -Được giải thưởng của các kì thi từ cấp huyện trở lên: ...............................................................................</p>\n"
                    + "        <p> -Khen thưởng đặc biệt khác: .....................................................................................................................</p>\n"
                    + " <div style=\"text-align: center;\"> <strong>Nhận xét của giáo viên chủ nghiệm</strong><br><em>(Ký, ghi rõ họ tên và đóng dấu)</em></div>"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + " <div style=\"text-align: center;\"> <strong>Phê duyệt của giáo viên hiệu trưởng</strong><br></div>"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <table style=\"width: 100%;\" border=\"0\"> \n"
                    + "            <tbody> \n"
                    + "                <tr> \n"
                    + "                    <td> </td> \n"
                    + "                    <td style=\"text-align: right;\"><em>......................, ngày.............tháng...........năm........</em></td> \n"
                    + "                </tr> \n"
                    + "                <tr> \n"
                    + "                    <td style=\"text-align: right;\"><strong>Xác nhận của Hiệu trưởng</strong><br><em>(Ký, ghi rõ họ tên và đóng dấu)</em></td> \n"
                    + "                </tr> \n"
                    + "            </tbody> \n"
                    + "        </table>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "        <br>\n"
                    + "    </body>\n"
                    + "</html>";
            System.out.println("start 2");
            // run the conversion and write the result to a file
            client.convertStringToFile(html, filepath);
        } catch (Pdfcrowd.Error why) {
            // report the error
            System.err.println("Pdfcrowd Error: " + why);

            // rethrow or handle the exception
            throw why;
        }
        // report the error
        // rethrow or handle the exception
        System.out.println("start 3");
    }

    private String hocluc(Float rate) {
        String kq = null;
        if (rate >= 8) {
            kq = "Giỏi";
        } else if (rate >= 7 && rate < 8) {
            kq = "Khá";
        } else if (rate >= 5 && rate < 7) {
            kq = "Trung Bình";
        } else if (rate < 5) {
            kq = "Yếu";
        }
        return kq;
    }

    private String hanh_kiem(int tongNghi) {
        String kq = null;
        if (tongNghi >= 30) {
            kq = "Yếu";
        } else if (tongNghi >= 10 && tongNghi < 30) {
            kq = "Trung Bình";
        } else if (tongNghi >= 5 && tongNghi < 10) {
            kq = "Khá";
        } else if (tongNghi < 5) {
            kq = "Tốt";
        }
        return kq;
    }

    private String check_null(String line) {

        if (line == null) {
            line = "";

        }
        return line;
    }
}

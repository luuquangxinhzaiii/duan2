/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.model;

/**
 *
 * @author Administrator
 */
public class LopHoc {

    private int iD;
    private String maLop;
    private String tenLop;
    private String maNH;
    private String maKhoi;

    public LopHoc() {
    }

    public LopHoc(int iD, String maLop, String tenLop, String maNH, String maKhoi) {
        this.iD = iD;
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maNH = maNH;
        this.maKhoi = maKhoi;
    }

    public int getiD() {
        return iD;
    }

    public void setiD(int iD) {
        this.iD = iD;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getMaNH() {
        return maNH;
    }

    public void setMaNH(String maNH) {
        this.maNH = maNH;
    }

    public String getMaKhoi() {
        return maKhoi;
    }

    public void setMaKhoi(String maKhoi) {
        this.maKhoi = maKhoi;
    }
   
   

}

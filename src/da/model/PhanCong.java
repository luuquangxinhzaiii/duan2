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
public class PhanCong {
    private int iD;
    private String maPC;
    private String maLop;
    private String maGV;
    private boolean vaiTro;
    private String maMon;
    private boolean hocKi;
    private String maNamHoc;

    public PhanCong() {
    }

    public PhanCong(int iD, String maPC, String maLop, String maGV, boolean vaiTro, String maMon, boolean hocKi, String maNamHoc) {
        this.iD = iD;
        this.maPC = maPC;
        this.maLop = maLop;
        this.maGV = maGV;
        this.vaiTro = vaiTro;
        this.maMon = maMon;
        this.hocKi = hocKi;
        this.maNamHoc = maNamHoc;
    }

    public int getiD() {
        return iD;
    }

    public void setiD(int iD) {
        this.iD = iD;
    }

    public String getMaPC() {
        return maPC;
    }

    public void setMaPC(String maPC) {
        this.maPC = maPC;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public boolean getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(boolean vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public boolean getHocKi() {
        return hocKi;
    }

    public void setHocKi(boolean hocKi) {
        this.hocKi = hocKi;
    }

    public String getMaNamHoc() {
        return maNamHoc;
    }

    public void setMaNamHoc(String maNamHoc) {
        this.maNamHoc = maNamHoc;
    }

  
  
    
    
}

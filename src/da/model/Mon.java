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
public class Mon {
    private String maMon;
    private String tenMon;
    private Boolean hinhThucDG;
    private String maKhoi;

    public Mon() {
    }

    public Mon(String maMon, String tenMon, Boolean hinhThucDG, String maKhoi) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.hinhThucDG = hinhThucDG;
        this.maKhoi = maKhoi;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public Boolean getHinhThucDG() {
        return hinhThucDG;
    }

    public void setHinhThucDG(Boolean hinhThucDG) {
        this.hinhThucDG = hinhThucDG;
    }

    public String getMaKhoi() {
        return maKhoi;
    }

    public void setMaKhoi(String maKhoi) {
        this.maKhoi = maKhoi;
    }
    
    
}

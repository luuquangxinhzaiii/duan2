/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.model;

/**
 *
 * @author BNC
 */
public class DiemDanh {
    private String Ngay;
    private String maGv;
    private String maHs;
    private boolean trangThai;
    
    public DiemDanh(){}
    public DiemDanh(String Ngay, String maGv, String maHs, boolean trangThai){
        super();
        this.Ngay = Ngay;
        this.maGv = maGv;
        this.maHs = maHs;
        this.trangThai = trangThai;
    }

    public String getNgay() {
        return Ngay;
    }

    public void setNgay(String Ngay) {
        this.Ngay = Ngay;
    }

    public String getMaGv() {
        return maGv;
    }

    public void setMaGv(String maGv) {
        this.maGv = maGv;
    }

    public String getMaHs() {
        return maHs;
    }

    public void setMaHs(String maHs) {
        this.maHs = maHs;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}

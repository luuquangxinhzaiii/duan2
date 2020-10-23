/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.model;

import java.util.Date;

/**
 *
 * @author Administrator
 */
public class NamHoc {
   
    private String maNamHoc;
    private String nienHoc;
    private Date   ngayBD;
    private Date   ngayKT;
    private String trangThai;
    private int xoa;
    public NamHoc() {
    }

    public NamHoc(String maNamHoc, String nienHoc, Date ngayBD, Date ngayKT,String trangThai, int xoa) {
        this.maNamHoc = maNamHoc;
        this.nienHoc = nienHoc;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.trangThai=trangThai;
        this.xoa=xoa;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getXoa() {
        return xoa;
    }

    public void setXoa(int xoa) {
        this.xoa = xoa;
    }

    public String getMaNamHoc() {
        return maNamHoc;
    }

    public void setMaNamHoc(String maNamHoc) {
        this.maNamHoc = maNamHoc;
    }

    public String getNienHoc() {
        return nienHoc;
    }

    public void setNienHoc(String nienHoc) {
        this.nienHoc = nienHoc;
    }

    public Date getNgayBD() {
        return ngayBD;
    }

    public void setNgayBD(Date ngayBD) {
        this.ngayBD = ngayBD;
    }

    public Date getNgayKT() {
        return ngayKT;
    }

    public void setNgayKT(Date ngayKT) {
        this.ngayKT = ngayKT;
    }
    
    
    
}

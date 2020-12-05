/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.model;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Administrator
 */
public class NamHoc {
   
    private UUID maNamHoc;
    private String nienHoc;
    private Date   ngayBD;
    private Date   ngayKT;
    private Boolean trangThai;
    public NamHoc() {
    }

    public NamHoc(UUID maNamHoc, String nienHoc, Date ngayBD, Date ngayKT,Boolean trangThai) {
        this.maNamHoc = maNamHoc;
        this.nienHoc = nienHoc;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.trangThai=trangThai;
    }

    public UUID getMaNamHoc() {
        return maNamHoc;
    }

    public void setMaNamHoc(UUID maNamHoc) {
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

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

   
    
    
}

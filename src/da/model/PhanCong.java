/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.model;

import java.util.UUID;

/**
 *
 * @author Administrator
 */
public class PhanCong {
    private UUID maPC;
    private UUID maLop;
    private UUID maGV;
    private Boolean vaiTro;
    private UUID maMon;
    private Boolean hocKi;
    private UUID maNamHoc;

    public PhanCong() {
    }

    public PhanCong(UUID maPC, UUID maLop, UUID maGV, Boolean vaiTro, UUID maMon, Boolean hocKi, UUID maNamHoc) {
        this.maPC = maPC;
        this.maLop = maLop;
        this.maGV = maGV;
        this.vaiTro = vaiTro;
        this.maMon = maMon;
        this.hocKi = hocKi;
        this.maNamHoc = maNamHoc;
    }

    public UUID getMaPC() {
        return maPC;
    }

    public void setMaPC(UUID maPC) {
        this.maPC = maPC;
    }

    public UUID getMaLop() {
        return maLop;
    }

    public void setMaLop(UUID maLop) {
        this.maLop = maLop;
    }

    public UUID getMaGV() {
        return maGV;
    }

    public void setMaGV(UUID maGV) {
        this.maGV = maGV;
    }

    public Boolean getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(Boolean vaiTro) {
        this.vaiTro = vaiTro;
    }

    public UUID getMaMon() {
        return maMon;
    }

    public void setMaMon(UUID maMon) {
        this.maMon = maMon;
    }

    public Boolean getHocKi() {
        return hocKi;
    }

    public void setHocKi(Boolean hocKi) {
        this.hocKi = hocKi;
    }

    public UUID getMaNamHoc() {
        return maNamHoc;
    }

    public void setMaNamHoc(UUID maNamHoc) {
        this.maNamHoc = maNamHoc;
    }

   
  
  
    
    
}

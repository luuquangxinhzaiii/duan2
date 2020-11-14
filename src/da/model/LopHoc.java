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
public class LopHoc {

    private UUID iD;
    private String maLop;
    private String tenLop;
    private UUID maNH;
    private UUID maKhoi;

    public LopHoc() {
    }

    public LopHoc(UUID iD, String maLop, String tenLop, UUID maNH, UUID maKhoi) {
        this.iD = iD;
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maNH = maNH;
        this.maKhoi = maKhoi;
    }

    public UUID getiD() {
        return iD;
    }

    public void setiD(UUID iD) {
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

    public UUID getMaNH() {
        return maNH;
    }

    public void setMaNH(UUID maNH) {
        this.maNH = maNH;
    }

    public UUID getMaKhoi() {
        return maKhoi;
    }

    public void setMaKhoi(UUID maKhoi) {
        this.maKhoi = maKhoi;
    }
   
   

}

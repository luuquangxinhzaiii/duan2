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
public class Mon {
    private UUID maMon;
    private String tenMon;
    private Boolean hinhThucDG;
    private UUID maKhoi;

    public Mon() {
    }

    public Mon(UUID maMon, String tenMon, Boolean hinhThucDG, UUID maKhoi) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.hinhThucDG = hinhThucDG;
        this.maKhoi = maKhoi;
    }

    public UUID getMaMon() {
        return maMon;
    }

    public void setMaMon(UUID maMon) {
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

    public UUID getMaKhoi() {
        return maKhoi;
    }

    public void setMaKhoi(UUID maKhoi) {
        this.maKhoi = maKhoi;
    }
    
    
}

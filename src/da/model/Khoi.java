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
public class Khoi {
    private String maKhoi;
    private String tenKhoi;

    public Khoi() {
    }

    public Khoi(String maKhoi, String tenKhoi) {
        this.maKhoi = maKhoi;
        this.tenKhoi = tenKhoi;
    }

    public String getMaKhoi() {
        return maKhoi;
    }

    public void setMaKhoi(String maKhoi) {
        this.maKhoi = maKhoi;
    }

    public String getTenKhoi() {
        return tenKhoi;
    }

    public void setTenKhoi(String tenKhoi) {
        this.tenKhoi = tenKhoi;
    }
    
    
}

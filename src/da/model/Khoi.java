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
public class Khoi {
    private UUID maKhoi;
    private String tenKhoi;

    public Khoi() {
    }

    public Khoi(UUID maKhoi, String tenKhoi) {
        this.maKhoi = maKhoi;
        this.tenKhoi = tenKhoi;
    }

    public UUID getMaKhoi() {
        return maKhoi;
    }

    public void setMaKhoi(UUID maKhoi) {
        this.maKhoi = maKhoi;
    }

    public String getTenKhoi() {
        return tenKhoi;
    }

    public void setTenKhoi(String tenKhoi) {
        this.tenKhoi = tenKhoi;
    }
    
    
}

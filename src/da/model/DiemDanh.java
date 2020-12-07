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
 * @author BNC
 */
public class DiemDanh {
    private UUID id;
    private Boolean hocki;
    private Date Ngay;
    private UUID maGv;
    private UUID maHs;
    private Boolean trangThai;
    private UUID namhoc;
    
    public DiemDanh(){}
    public DiemDanh(UUID id,Boolean hocki, Date Ngay, UUID maGv, UUID maHs, Boolean trangThai,UUID namhoc){
        super();
        this.id = id;
        this.hocki = hocki;
        this.Ngay = Ngay;
        this.maGv = maGv;
        this.maHs = maHs;
        this.trangThai = trangThai;
        this.namhoc = namhoc;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getNgay() {
        return Ngay;
    }

    public void setNgay(Date Ngay) {
        this.Ngay = Ngay;
    }

    public UUID getMaGv() {
        return maGv;
    }

    public void setMaGv(UUID maGv) {
        this.maGv = maGv;
    }

    public UUID getMaHs() {
        return maHs;
    }

    public void setMaHs(UUID maHs) {
        this.maHs = maHs;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public UUID getNamhoc() {
        return namhoc;
    }

    public void setNamhoc(UUID namhoc) {
        this.namhoc = namhoc;
    }

    public Boolean getHocki() {
        return hocki;
    }

    public void setHocki(Boolean hocki) {
        this.hocki = hocki;
    }

    
}

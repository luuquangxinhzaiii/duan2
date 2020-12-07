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
public class HocSinh {
    private UUID iD;
    private String maHS;
    private String hoTen;
    private Boolean gioiTinh;
    private Date ngaySinh;
    private String diaChi;
    private String dienThoai;
    private String danToc;
    private String tonGiao;
    private Date ngayVD;
    private String noiSinh;
    private String cmND;
    private UUID lop_id;
    private String hotenBo;
    private String hotenMe;
    private String dienThoaiBo;
    private String dienThoaiMe;
    private String dvctBo;
    private String dvctMe;
    private String nguoiDamHo;
    private Boolean trangThai;
    private String anh;

    public HocSinh() {
    }

    public HocSinh(UUID iD, String maHS, String hoTen, Boolean gioiTinh, Date ngaySinh, String diaChi, String dienThoai, String danToc, String tonGiao, Date ngayVD, String noiSinh, String cmND, UUID lop_id, String hotenBo, String hotenMe, String dienThoaiBo, String dienThoaiMe, String dvctBo, String dvctMe, String nguoiDamHo, Boolean trangThai, String anh) {
        this.iD = iD;
        this.maHS = maHS;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.dienThoai = dienThoai;
        this.danToc = danToc;
        this.tonGiao = tonGiao;
        this.ngayVD = ngayVD;
        this.noiSinh = noiSinh;
        this.cmND = cmND;
        this.lop_id = lop_id;
        this.hotenBo = hotenBo;
        this.hotenMe = hotenMe;
        this.dienThoaiBo = dienThoaiBo;
        this.dienThoaiMe = dienThoaiMe;
        this.dvctBo = dvctBo;
        this.dvctMe = dvctMe;
        this.nguoiDamHo = nguoiDamHo;
        this.trangThai = trangThai;
        this.anh = anh;
    }

    public UUID getiD() {
        return iD;
    }

    public void setiD(UUID iD) {
        this.iD = iD;
    }

    public String getMaHS() {
        return maHS;
    }

    public void setMaHS(String maHS) {
        this.maHS = maHS;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public Boolean getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(Boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public String getDanToc() {
        return danToc;
    }

    public void setDanToc(String danToc) {
        this.danToc = danToc;
    }

    public String getTonGiao() {
        return tonGiao;
    }

    public void setTonGiao(String tonGiao) {
        this.tonGiao = tonGiao;
    }

    public Date getNgayVD() {
        return ngayVD;
    }

    public void setNgayVD(Date ngayVD) {
        this.ngayVD = ngayVD;
    }

    public String getNoiSinh() {
        return noiSinh;
    }

    public void setNoiSinh(String noiSinh) {
        this.noiSinh = noiSinh;
    }

    public String getCmND() {
        return cmND;
    }

    public void setCmND(String cmND) {
        this.cmND = cmND;
    }

    public UUID getLop_id() {
        return lop_id;
    }

    public void setLop_id(UUID lop_id) {
        this.lop_id = lop_id;
    }

    public String getHotenBo() {
        return hotenBo;
    }

    public void setHotenBo(String hotenBo) {
        this.hotenBo = hotenBo;
    }

    public String getHotenMe() {
        return hotenMe;
    }

    public void setHotenMe(String hotenMe) {
        this.hotenMe = hotenMe;
    }

    public String getDienThoaiBo() {
        return dienThoaiBo;
    }

    public void setDienThoaiBo(String dienThoaiBo) {
        this.dienThoaiBo = dienThoaiBo;
    }

    public String getDienThoaiMe() {
        return dienThoaiMe;
    }

    public void setDienThoaiMe(String dienThoaiMe) {
        this.dienThoaiMe = dienThoaiMe;
    }

    public String getDvctBo() {
        return dvctBo;
    }

    public void setDvctBo(String dvctBo) {
        this.dvctBo = dvctBo;
    }

    public String getDvctMe() {
        return dvctMe;
    }

    public void setDvctMe(String dvctMe) {
        this.dvctMe = dvctMe;
    }

    public String getNguoiDamHo() {
        return nguoiDamHo;
    }

    public void setNguoiDamHo(String nguoiDamHo) {
        this.nguoiDamHo = nguoiDamHo;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    

   
    
    
    
    
}

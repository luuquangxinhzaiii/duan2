/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.model;

import java.util.Date;

/**
 *
 * @author admin
 */
public class Diem {

    private int stt;
    private Date ngay;
    private String maHocSinh;
    private int diemMieng1;
    private int diemMieng2;
    private int diemMieng3;
    private int diem15p1;
    private int diem15p2;
    private int diem15p3;
    private float diem1Tiet1;
    private float diem1Tiet2;
    private float diemThi;
    private float diemTBM;
    private String mapc;

    public Diem() {}

    public Diem(int stt, Date ngay, String maHocSinh, int diemMieng1, int diemMieng2, int diemMieng3, int diem15p1, int diem15p2, int diem15p3, float diem1Tiet1, float diem1Tiet2, float diemThi, float diemTBM, String mapc) {
        this.stt = stt;
        this.ngay = ngay;
        this.maHocSinh = maHocSinh;
        this.diemMieng1 = diemMieng1;
        this.diemMieng2 = diemMieng2;
        this.diemMieng3 = diemMieng3;
        this.diem15p1 = diem15p1;
        this.diem15p2 = diem15p2;
        this.diem15p3 = diem15p3;
        this.diem1Tiet1 = diem1Tiet1;
        this.diem1Tiet2 = diem1Tiet2;
        this.diemThi = diemThi;
        this.diemTBM = diemTBM;
        this.mapc = mapc;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    public void setMaHocSinh(String maHocSinh) {
        this.maHocSinh = maHocSinh;
    }

    public void setDiemMieng1(int diemMieng1) {
        this.diemMieng1 = diemMieng1;
    }

    public void setDiemMieng2(int diemMieng2) {
        this.diemMieng2 = diemMieng2;
    }

    public void setDiemMieng3(int diemMieng3) {
        this.diemMieng3 = diemMieng3;
    }

    public void setDiem15p1(int diem15p1) {
        this.diem15p1 = diem15p1;
    }

    public void setDiem15p2(int diem15p2) {
        this.diem15p2 = diem15p2;
    }

    public void setDiem15p3(int diem15p3) {
        this.diem15p3 = diem15p3;
    }

    public void setDiem1Tiet1(float diem1Tiet1) {
        this.diem1Tiet1 = diem1Tiet1;
    }

    public void setDiem1Tiet2(float diem1Tiet2) {
        this.diem1Tiet2 = diem1Tiet2;
    }

    public void setDiemThi(float diemThi) {
        this.diemThi = diemThi;
    }

    public void setDiemTBM(float diemTBM) {
        this.diemTBM = diemTBM;
    }

    public void setMapc(String mapc) {
        this.mapc = mapc;
    }

    public int getStt() {
        return stt;
    }

    public Date getNgay() {
        return ngay;
    }

    public String getMaHocSinh() {
        return maHocSinh;
    }

    public int getDiemMieng1() {
        return diemMieng1;
    }

    public int getDiemMieng2() {
        return diemMieng2;
    }

    public int getDiemMieng3() {
        return diemMieng3;
    }

    public int getDiem15p1() {
        return diem15p1;
    }

    public int getDiem15p2() {
        return diem15p2;
    }

    public int getDiem15p3() {
        return diem15p3;
    }

    public float getDiem1Tiet1() {
        return diem1Tiet1;
    }

    public float getDiem1Tiet2() {
        return diem1Tiet2;
    }

    public float getDiemThi() {
        return diemThi;
    }

    public float getDiemTBM() {
        return diemTBM;
    }

    public String getMapc() {
        return mapc;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.model;

/**
 *
 * @author BNC
 */
public class TaiKhoan {

    private int stt;

    private String passWord;
    private String role;
   
    private String email;
    private String maGiaoVien;
    private String maHocSinh;

    public TaiKhoan() {
    }

    public TaiKhoan(int stt, String passWord, String role,  String email, String maGiaoVien, String maHocSinh) {
        this.stt = stt;
        this.maGiaoVien = maGiaoVien;
        this.maHocSinh = maHocSinh;
        this.passWord = passWord;
        this.role = role;
       
        this.email = email;
    }

    public String getMaGiaoVien() {
        return maGiaoVien;
    }

    public void setMaGiaoVien(String maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
    }

    public String getMaHocSinh() {
        return maHocSinh;
    }

    public void setMaHocSinh(String maHocSinh) {
        this.maHocSinh = maHocSinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

   

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

   

    @Override
    public String toString() {
        return "TaiKhoan{" + "stt=" + stt + ", email=" + email + ", passWord=" + passWord + ", role=" + role + '}';
    }

   

}

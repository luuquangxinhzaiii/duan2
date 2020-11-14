/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.model;

import java.util.UUID;

/**
 *
 * @author BNC
 */
public class TaiKhoan {

    private UUID stt;
    private String passWord;
    private String role;
    private String email;
    private UUID giaovien_id;
    private String hoten;
    private UUID hocsinh_id;

    public TaiKhoan() {
    }

    public TaiKhoan(UUID stt, String passWord, String role,  String email, UUID giaovien_id, UUID hocsinh_id, String hoten) {
        this.stt = stt;
        this.giaovien_id = giaovien_id;
        this.hocsinh_id = hocsinh_id;
        this.passWord = passWord;
        this.role = role;
        this.hoten = hoten;
        this.email = email;
    }

    public UUID getStt() {
        return stt;
    }

    public void setStt(UUID stt) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getGiaovien_id() {
        return giaovien_id;
    }

    public void setGiaovien_id(UUID giaovien_id) {
        this.giaovien_id = giaovien_id;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public UUID getHocsinh_id() {
        return hocsinh_id;
    }

    public void setHocsinh_id(UUID hocsinh_id) {
        this.hocsinh_id = hocsinh_id;
    }

    

    

   

    @Override
    public String toString() {
        return "TaiKhoan{" + "stt=" + stt + ", email=" + email + ", passWord=" + passWord + ", role=" + role + '}';
    }

   

}

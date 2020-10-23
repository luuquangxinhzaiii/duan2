/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.helper;

import da.model.TaiKhoan;



/**
 *
 * @author BNC
 */
public class ShareHelper {
    public static TaiKhoan TaiKhoan = null;

    public static void logoff() {
        ShareHelper.TaiKhoan = null;
    }

    public static boolean checklog() {
        return ShareHelper.TaiKhoan != null;
    }
}

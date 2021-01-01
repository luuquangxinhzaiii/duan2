/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.helper;

import java.sql.Date;

/**
 *
 * @author Admin
 */
public class date {
    public static void compareDate(){

        // Định nghĩa 2 mốc thời gian ban đầu
        Date date1 = Date.valueOf("2011-07-30");
        Date date2 = Date.valueOf("2011-07-30");
        
        String relation;
        if (date1.equals(date2))
          relation = "Hai ngày trùng nhau";
        else if (date1.before(date2)) // Hoặc  else if (date1.after(date2)== false)
          relation = " Trước";
        else
          relation = " Sau";
        System.out.println(date1 + relation + ' ' + date2);

    }
    public static void main(String[] args) {
        compareDate();
    }
    
}

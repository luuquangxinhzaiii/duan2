/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

import da.helper.JdbcHelper;
import da.model.Khoi;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Administrator
 */
public class KhoiDAO {

    private Khoi readFromResultSet(ResultSet rs) throws SQLException {
        Khoi model = new Khoi();
        model.setMaKhoi(UUID.fromString(rs.getString("makhoi")));
        model.setTenKhoi(rs.getString("tenkhoi"));

        return model;

    }

    private List<Khoi> select(String sql, Object... args) {
        List<Khoi> list = new ArrayList<>();
        try {
            ResultSet rs = null;

            rs = JdbcHelper.executeQuery(sql, args);
            while (rs.next()) {
                Khoi model = readFromResultSet(rs);
                list.add(model);

            }
            rs.getStatement().getConnection().close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;

    }
    
    
     public List<Khoi> select(){
        String sql="select * from khoi";
        return select(sql);
    }
     
    public Khoi selectByMaKhoi(UUID maKhoi){
        String sql="select * from khoi where makhoi=?";
        List<Khoi> list = select(sql, maKhoi);
        return list.size() > 0 ? list.get(0) : null;
    }
    
    public Khoi selectByTenKhoi(String tenKhoi){
        String sql="select * from khoi where tenKhoi=?";
        List<Khoi> list = select(sql, tenKhoi);
        return list.size() > 0 ? list.get(0) : null;
    }
    
    
}

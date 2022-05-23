package db.tables;

import db.manager.DbManager;
import db.manager.Result;
import db.manager.Setter;
import java.util.ArrayList;
import java.util.List;
import util.TimeManager;

public class SimpleDb extends DbManager{

    private String table;
    private String insertQuery;
    private String allQuery;
    
    public SimpleDb(String table){
        this.table = table;
        insertQuery = "INSERT INTO "+table+"(name,date_added) values(?,?)";
        allQuery = "SELECT * FROM "+table;
    }
    
    public void add(String name)throws Exception{
        Setter setter = stmt ->{
            stmt.setString(1,name);
            stmt.setString(2,TimeManager.getTimestamp());
            stmt.executeUpdate();
        };
        
        update(insertQuery, setter);
    }
    
    public List<String> getList() throws Exception {
        Result<List<String>> result = rs -> {
            List<String> list = new ArrayList<>();

            while (rs.next()) {
                list.add(rs.getString("name"));
            }
            return list;
        };

        return retrieve(allQuery, result).get();
    }
 
    public void addBatch(List<String> list) throws Exception {
        Setter setter = stmt -> {
            for (var name : list) {
                stmt.setString(1, name);
                stmt.setString(2, TimeManager.getTimestamp());
                stmt.addBatch();
            }

            stmt.executeBatch();
        };

        update(insertQuery, setter);
    }
   
}

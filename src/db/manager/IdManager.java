
package db.manager;

public class IdManager extends DbManager{
    
    private IdManager() {
    }
    
    public static IdManager getInstance() {
        return IdManagerHolder.INSTANCE;
    }
    
    private static class IdManagerHolder {
        private static final IdManager INSTANCE = new IdManager();
    }
    
    public int getNextId(String header)throws Exception{
        String query = "SELECT ID FROM ID_MANAGER WHERE HEADER = ?";
        
        Setter setter = stmt -> {
            stmt.setString(1, header);
            stmt.executeQuery();
        };
        
        Result<Integer>result = resultSet ->{
            resultSet.next();
            return resultSet.getInt("ID");
        };
        
        int id =  retrieve(query,setter,result).get() + 1;        
        
        update(header);
        
        return id;
    }
    
    private void update(String header)throws Exception{
      
        String query = "UPDATE ID_MANAGER SET ID = ID + 1 WHERE HEADER = ?";
        
        Setter setter = stmt -> {
            stmt.setString(1,header);
            stmt.executeUpdate();
                    };
        
        super.update(query, setter);
      
    }
}

package db.manager;

import db.connection.ConnectionManager;
import java.sql.PreparedStatement;
import java.util.Optional;

public class DbManager {


    public void update(String query, Setter setter) throws Exception {
        ConnectionManager conn = new ConnectionManager();
        conn.openConnection();
        try {
            PreparedStatement preparedStmt = conn.getConnection().prepareStatement(query);
            setter.accept(preparedStmt);
            conn.closeConnection();
            return;
        } catch (Exception e) {
            e.printStackTrace();
              conn.closeConnection();
            throw new Exception(e);
            
        } 
    }

    public <T> Optional<T> retrieve(String query, Result<T> result) throws Exception {

        ConnectionManager conn = new ConnectionManager();
        conn.openConnection();

        Optional<T> value = Optional.empty();

        try {
            PreparedStatement preparedStmt = conn.getConnection().prepareStatement(query);
            value = Optional.ofNullable(result.apply(preparedStmt.executeQuery()));
            conn.closeConnection();
            return value;
        } catch (Exception e) {
            conn.closeConnection();
            e.printStackTrace();
            throw new Exception(e);
        }
    }
    
       public <T> Optional<T> retrieve(String query, Setter setter,Result<T> result) throws Exception {

        ConnectionManager conn = new ConnectionManager();
        conn.openConnection();

        Optional<T> value = Optional.empty();

        try {
            PreparedStatement preparedStmt = conn.getConnection().prepareStatement(query);
            setter.accept(preparedStmt);
            value = Optional.ofNullable(result.apply(preparedStmt.executeQuery()));
            conn.closeConnection();
            return value;
        } catch (Exception e) {
            conn.closeConnection();
            e.printStackTrace();
            throw new Exception(e);
        }
    }
   
}

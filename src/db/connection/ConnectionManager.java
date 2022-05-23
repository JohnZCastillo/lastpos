package db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private Connection conn;

    public void openConnection() {
        try {

            Class.forName(DbConfig.DRIVER.getValue());

            conn = DriverManager.getConnection(
                    DbConfig.PATH.getValue(),
                    DbConfig.USERNAME.getValue(),
                    DbConfig.PASSWORD.getValue()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if(!conn.isClosed()){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws Exception {
        if (conn == null) {
            throw new Exception("Connection Null");
        }
        return conn;
    }

}

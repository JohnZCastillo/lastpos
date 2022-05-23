
package db.manager;

import java.sql.ResultSet;

@FunctionalInterface
public interface Result<T>{

   T apply(ResultSet result) throws Exception;
   
}

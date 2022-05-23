
package db.manager;

import java.sql.PreparedStatement;

@FunctionalInterface
public interface Setter {

    void accept(PreparedStatement stmt)throws Exception;
    
}

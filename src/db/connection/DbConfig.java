
package db.connection;


public enum DbConfig {
   
    USERNAME("admin"),
    PASSWORD("superadmin01..."),
    PATH("jdbc:h2:./db/db"),
    DRIVER("org.h2.Driver");
    
    private final String value;
     
    private DbConfig(String value){
        this.value = value;
    }
        
    public String getValue(){
        return this.value;
    }
    
}
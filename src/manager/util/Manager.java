package manager.util;

import java.util.ArrayList;
import java.util.List;

public abstract class Manager{

    protected List<String> list;
    
    public Manager(){
        list = new ArrayList<>();
    }
    
    public List<String> getList(){
        return list;
    }
    
    public boolean inList(String t){
       return list.contains(t);
    }
      
    public abstract void addToList(String t)throws Exception;
    public abstract void update()throws Exception;
    
    public abstract boolean removeFromList(String t);
    
}
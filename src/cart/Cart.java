package cart;

import java.util.Map;
import item.Item;

public interface Cart {
    
    public boolean add(Item item,int quantity);
    
    public boolean remove(Item item,int quantity);
    
    public boolean clear();
    
    public boolean inCart(Item item);
    
    public Map<Item,Integer> getItems();
    
    public int valueInCart(Item item);
    
    public boolean isEmpty();
   
}

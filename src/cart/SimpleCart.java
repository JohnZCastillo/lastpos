package cart;

import java.util.HashMap;
import java.util.Map;
import item.Item;

public abstract class SimpleCart implements Cart{

    final private Map<Item,Integer> cart;
    
    public SimpleCart(){
        cart = new HashMap<>();
    }
    
    private int getQuantity(Item item){
        return cart.get(item);
    }
    
    
    @Override
    public boolean add(Item item, int quantity) {
        
        if(quantity <= 0) return false;
        
        if(!inventoryControl(item,quantity)) return false;
                
        if(cart.containsKey(item)){
            cart.put(item,quantity + getQuantity(item));
            calculate(getItems());
            return true;
        }
        
        cart.put(item,quantity);
        calculate(getItems());
        
        if(getQuantity(item) == 0) cart.remove(item);
        
        return true;
    }

    @Override
    public boolean remove(Item item, int quantity) {
        
        if(quantity <= 0) return false;
        
        if(!inCart(item)) return false;
        
        if(quantity > getQuantity(item)) return false;
        
        cart.put(item, getQuantity(item) - quantity);
        calculate(getItems());
        return true;
    }

    @Override
    public boolean clear() {
        cart.clear();
        return cart.isEmpty();
    }

    @Override
    public Map<Item, Integer> getItems() {
        return cart;
    }

    @Override
    public boolean inCart(Item item) {
        return cart.containsKey(item);
    }

    @Override
    public int valueInCart(Item item){
        if(inCart(item)){
         return cart.get(item);
        }
        return 0;
    }
    
    @Override
    public boolean isEmpty(){
        return cart.isEmpty();
    }
    
    abstract void calculate(Map<Item,Integer>cart);
    
    abstract boolean inventoryControl(Item item,int quantity);
    
}
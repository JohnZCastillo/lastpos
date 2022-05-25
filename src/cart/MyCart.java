package cart;

import item.Item;
import java.util.Map;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import item.CartItem;
import java.util.ArrayList;
import java.util.List;

public class MyCart extends SimpleCart{

    final private SimpleDoubleProperty total = new SimpleDoubleProperty();
    private ObservableList<CartItem> items = FXCollections.observableArrayList();
    
    @Override
    void calculate(Map<Item, Integer> cart) {
        total.set(0);
        cart.forEach((item,quantity)->total.set(item.getSellingPrice() * quantity));
        System.out.println("TOTAL: "+total.get());
    }

    @Override
    boolean inventoryControl(Item item, int quantity) {
        return true;
    }
    
    @Override
    public boolean clear(){
        
        boolean status = super.clear();
        
        if(status){
            total.set(0);
        }
        
        return status;
    }

    public SimpleDoubleProperty getTotal() {
        return total;
    }

    public ObservableList<CartItem> getList(){
        return items;
    }
    
    @Override
    public boolean add(Item item, int quantity){
        boolean status = super.add(item,quantity);
        items.add(new CartItem(item,quantity));
        return status;
    }
    
    @Override
    public boolean remove(Item item, int quantity){
        
        boolean status = super.remove(item,quantity);
        
                    System.out.println("Remvoing");

        if(status){
           this.remove(new CartItem(item,quantity));
            System.out.println("Remvoing");
        }
        return status;
    }
    
    ///remove items quantity in cart 
    //the purpose of this is for gui only
    //not the internal removing beacuase it is done
    //by the parents
    public void remove(CartItem cartItem){
        
        for(CartItem item: getList()){
            if(item.item().getBarcode().equals(cartItem.item().getBarcode())){
                
                if(item.getQuantity() <= 0){
                    continue;
                }
                
                if(item.getQuantity() >= cartItem.getQuantity()){
                    item.setQuantity(item.getQuantity() - cartItem.getQuantity());
                    break;
                }
                
                if (item.getQuantity() < cartItem.getQuantity()) {
                    cartItem.setQuantity(cartItem.getQuantity() - item.getQuantity());
                    item.setQuantity(0);
                }
               
            }
        }

       //remove products that has zero quantity
       
        List<CartItem> toRemove = new ArrayList<>();
        
        for (int i = 0; i < getList().size(); i++) {
            if(getList().get(i).getQuantity() == 0){
                toRemove.add(getList().get(i));
            }
        }
       
        getList().removeAll(toRemove);
        
    }
    
}

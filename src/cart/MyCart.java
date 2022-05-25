package cart;

import item.Item;
import java.util.Map;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import item.CartItem;
import java.util.ArrayList;
import java.util.List;
import util.CurrencyFormat;

        
public class MyCart extends SimpleCart{

    final private SimpleDoubleProperty total = new SimpleDoubleProperty();
    final private SimpleDoubleProperty cash = new SimpleDoubleProperty();
    final private SimpleDoubleProperty change = new SimpleDoubleProperty();
    final private SimpleDoubleProperty discount = new SimpleDoubleProperty();
    
    private ObservableList<CartItem> items = FXCollections.observableArrayList();
    
    public MyCart(){
        cash.addListener(arg->{
            if(cash.get() > 0){
                change.set(CurrencyFormat.roundOff(cash.get() - total.get()));
                return;
            }
           change.set(0);
        });
        
        total.addListener(args -> {
            if (cash.get() > 0) {
                change.set(CurrencyFormat.roundOff(cash.get() - total.get()));
                return;
            }
            change.set(0);
        });
        
    }
    
    @Override
    void calculate(Map<Item, Integer> cart) {
        
        total.set(0);
        cart.forEach((item,quantity)->{
            total.set(CurrencyFormat.roundOff(total.get() + (item.getSellingPrice() * quantity)));
        });
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
            cash.set(0);
            change.set(0);
            items.clear();
        }
        
        return status;
    }

    public SimpleDoubleProperty getTotal() {
        return total;
    }

    public SimpleDoubleProperty getChange() {
        return change;
    }
    public SimpleDoubleProperty getCash() {
        return cash;
    }
    public SimpleDoubleProperty getDisocunt() {
        return discount;
    }
    
    public ObservableList<CartItem> getList(){
        return items;
    }
    
    public boolean setCash(double cash){
        if(cash >= total.get()){
            this.cash.set(cash);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean add(Item item, int quantity){
        boolean status = super.add(item,quantity);
        
        if(status){
            items.add(new CartItem(item,quantity));
        }
        
        return status;
    }
    
    @Override
    public boolean remove(Item item, int quantity){
        
        boolean status = super.remove(item,quantity);

        if(status){
           this.remove(new CartItem(item,quantity));
        }
        
        return status;
    }
    
    ///remove items quantity in cart 
    //the purpose of this is for gui only
    //not the internal removing beacuase it is done
    //by the parent
    public void remove(CartItem cartItem){
        
        for(CartItem item: getList()){
            if(item.item().getBarcode().equals(cartItem.item().getBarcode())){
                
                if( cartItem.getQuantity() <= 0){
                    break;
                }
                    
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

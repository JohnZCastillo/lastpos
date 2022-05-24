
package manager;

import item.Item;
import java.util.ArrayList;
import java.util.List;

import db.item.ProductDb;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {
    
    private List<Item> items;
    private ProductDb db;
    
    private Map<String,String>name;
    private Map<String,String>barcode;
    
    private ItemManager() {
        items = new ArrayList<>();
        name = new HashMap<>();
        barcode = new HashMap<>();
        db = new ProductDb();
        update();
    }
    
    private void update() {
        try {
            items.addAll(db.getProducts());
            items.stream().forEach(item -> {
                name.put(item.getName(), item.getName());
                barcode.put(item.getBarcode(), item.getBarcode());
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Item> getItems(){
        return items;
    }
    
    public static ItemManager getInstance() {
        return ItemManagerHolder.INSTANCE;
    }
    
    private static class ItemManagerHolder {

        private static final ItemManager INSTANCE = new ItemManager();
    }
    
    public boolean updateItem(Item item){
        try{
            db.updateProduct(item);
            name.putIfAbsent(item.getName(), item.getName());
            barcode.putIfAbsent(item.getBarcode(), item.getBarcode());
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public void addItem(Item item)throws Exception{
            db.addProduct(item);
            getItems().add(item);
            name.putIfAbsent(item.getName(), item.getName());
            barcode.putIfAbsent(item.getBarcode(), item.getBarcode());
    }
    
    public boolean isBarcodePresent(String barcode){
        return this.barcode.containsKey(barcode);
    }
    
    public boolean isNamePresent(String name){
        return this.name.containsKey(name);
    }
    
    public void addAll(List<Item> items) throws Exception {
        db.addBatchProduct(items);
        getItems().addAll(items);
        items.stream().forEach(item -> {
            name.put(item.getName(), item.getName());
            barcode.put(item.getBarcode(), item.getBarcode());
        });
    }
    
    public void removeItem(Item item)throws Exception{
        db.deleteProduct(item);
        getItems().remove(item);
    }
}

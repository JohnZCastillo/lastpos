package db.transaction;

import cart.Cart;
import cart.MyCart;
import db.manager.DbManager;
import db.manager.Setter;
import item.Item;
import util.TimeManager;
import db.manager.IdManager;


public class TransactionDb extends DbManager{

    public void save(MyCart cart,int cartId)throws Exception{
       
     String query = "INSERT INTO TRANSACTION(total,subtotal,discount,cash,change,cashier,cart_id,`date`,`time`) VALUES(?,?,?,?,?,?,?,?,?)";

       Setter setter = stmt ->{
          
           stmt.setDouble(1,cart.getTotal().get());
           stmt.setDouble(2,cart.getTotal().get());
           stmt.setDouble(3,cart.getDisocunt().get());
           stmt.setDouble(4,cart.getCash().get());
           stmt.setDouble(5,cart.getChange().get());
           stmt.setString(6,"ADMIN");
           stmt.setInt(7,cartId);
           stmt.setString(8,TimeManager.today().toString());
           stmt.setString(9,TimeManager.todayTime().toString());
           
           stmt.executeUpdate();
           
       };
       
       
       super.update(query, setter);
    }
    
    public int saveCart(Cart cart) throws Exception {

        String query = "INSERT INTO CART(NAME,SELLING_PRICE,PURCHASE_COST,DESCRIPTION,BARCODE,SKU,QUANTITY,DATE,CART_ID) VALUES(?,?,?,?,?,?,?,?,?)";
        
        final int id = IdManager.getInstance().getNextId("CART");
        
        Setter setter = stmt -> {

            for (Item item : cart.getItems().keySet()) {

                stmt.setString(1, item.getName());
                stmt.setDouble(2, item.getSellingPrice());
                stmt.setDouble(3, item.getPurchaseCost());
                stmt.setString(4, item.getDescription());
                stmt.setString(5, item.getBarcode());
                stmt.setString(6, item.getSku());
                stmt.setInt(7, cart.getItems().get(item));
                stmt.setString(8, TimeManager.getTimestamp());
                stmt.setInt(9, id);

                stmt.addBatch();
            }

            stmt.executeBatch();
        };

        super.update(query, setter);
        return id;
    }
    
}

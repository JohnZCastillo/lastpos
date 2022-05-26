package db.transaction;

import item.CartItem;
import item.Item;
import item.Product;
import db.manager.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import transaction.Transaction;
import db.manager.DbManager;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionHistory extends DbManager{
    
    
    public List<Transaction> getHistory() throws Exception {

        String query = "SELECT TRANSACTION.CHANGE,TRANSACTION.CASH,TRANSACTION.TOTAL,TRANSACTION.SUBTOTAL,TRANSACTION.CART_ID,TRANSACTION.CASHIER,TRANSACTION.`DATE`,TRANSACTION.`TIME` "
                + ",CART.NAME,CART.DESCRIPTION,CART.BARCODE,CART.SELLING_PRICE,CART.PURCHASE_COST,CART.QUANTITY"
                + " FROM TRANSACTION JOIN CART ON TRANSACTION.CART_ID = CART.CART_ID";

        Result<List<Transaction>> result = resultSet -> {

            Map<Integer, Transaction> tr = new HashMap<>();

            Map<Integer, List<CartItem>> map = new HashMap<>();

            while (resultSet.next()) {

                Item item = new Product();

                item.setName(resultSet.getString("NAME"));
                item.setBarcode(resultSet.getString("BARCODE"));
                item.setDescription(resultSet.getString("DESCRIPTION"));
                item.setPurchaseCost(resultSet.getDouble("PURCHASE_COST"));
                item.setSellingPrice(resultSet.getDouble("SELLING_PRICE"));

                int id = resultSet.getInt("CART_ID");

                CartItem cartItem = new CartItem(item, resultSet.getInt("QUANTITY"));

                if (map.containsKey(id)) {
                    map.get(id).add(cartItem);
                } else {
                    List<CartItem> temp = new ArrayList<>();
                    temp.add(cartItem);
                    map.put(id, temp);

                    Transaction trn = new Transaction();
                    trn.setId(id);

                    trn.setTotal(resultSet.getDouble("TOTAL"));
                    trn.setTime(LocalTime.parse(resultSet.getString("TIME")));
                    trn.setDate(LocalDate.parse(resultSet.getString("DATE")));
                    trn.setCashier(resultSet.getString("CASHIER"));
                    trn.setChange(resultSet.getDouble("CHANGE"));
                    trn.setCash(resultSet.getDouble("CASH"));
                    trn.setSubtotal(resultSet.getDouble("SUBTOTAL"));
                    tr.put(id, trn);
                }

            }

            for (var cartId : map.keySet()) {
                tr.get(cartId).setItems(map.get(cartId));
            }

            return new ArrayList<Transaction>(tr.values());
        };

        return retrieve(query, result).get();
    }
}

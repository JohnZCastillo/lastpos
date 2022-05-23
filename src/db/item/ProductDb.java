package db.item;

import db.manager.DbManager;
import db.manager.Result;
import db.manager.Setter;
import item.Item;
import item.Product;
import java.util.ArrayList;
import java.util.List;
import util.TimeManager;



public class ProductDb extends DbManager{

 //   id
//    name
//    description
//    price
//    cost
//    brand
//    category 
//    sku
//    barcode 
//    active
//    dateCreated
//    lastModified
            
    
    public void addProduct(Item item) throws Exception {

        String query = "INSERT INTO PRODUCT(name,description,selling_price,purchase_cost,brand,category,sku,barcode,active,created_date,last_modified) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        Setter setter = stmt -> {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getSellingPrice());
            stmt.setDouble(4, item.getPurchaseCost());
            stmt.setString(5, item.getBrand());
            stmt.setString(6, item.getCategory());
            stmt.setString(7, item.getSku());
            stmt.setString(8, item.getBarcode());
            stmt.setBoolean(9, item.isActive());
            stmt.setString(10, TimeManager.getTimestamp());
            stmt.setString(11, TimeManager.getTimestamp());
            stmt.executeUpdate();
        };

        update(query,setter);
    }
 
    public void deleteProduct(Item item) throws Exception {

        String query = "DELETE FROM PRODUCT WHERE BARCODE = ?";

        Setter setter = stmt -> {
            stmt.setString(1, item.getBarcode());
            stmt.executeUpdate();
        };

        update(query,setter);
    }
 
    public void addBatchProduct(List<Item> products) throws Exception {

        String query = "INSERT INTO PRODUCT(name,description,selling_price,purchase_cost,brand,category,sku,barcode,active,created_date,last_modified) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        Setter setter = stmt -> {

            for (Item item : products) {
                stmt.setString(1, item.getName());
                stmt.setString(2, item.getDescription());
                stmt.setDouble(3, item.getSellingPrice());
                stmt.setDouble(4, item.getPurchaseCost());
                stmt.setString(5, item.getBrand());
                stmt.setString(6, item.getCategory());
                stmt.setString(7, item.getSku());
                stmt.setString(8, item.getBarcode());
                stmt.setBoolean(9, item.isActive());
                stmt.setString(10, TimeManager.getTimestamp());
                stmt.setString(11, TimeManager.getTimestamp());
                stmt.addBatch();
            }

            stmt.executeBatch();
        };

        update(query, setter);
    }

        
     public void updateProduct(Item item) throws Exception {

        String query = "UPDATE PRODUCT SET "
                + "name = ?,"
                + "description = ?,"
                + "selling_price = ?,"
                + "purchase_cost = ?,"
                + "brand = ?,"
                + "category = ?,"
                + "sku = ?,"
                + "last_modified = ?"
                + " WHERE BARCODE = ?";

        Setter setter = stmt -> {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getSellingPrice());
            stmt.setDouble(4, item.getPurchaseCost());
            stmt.setString(5, item.getBrand());
            stmt.setString(6, item.getCategory());
            stmt.setString(7, item.getSku());
            stmt.setString(8, TimeManager.getTimestamp());
            stmt.setString(9, item.getBarcode());
            stmt.executeUpdate();
        };

        update(query,setter);
    }
       
     public List<Item> getProducts() throws Exception {

        String query = "SELECT * FROM PRODUCT";

        Result<List<Item>> result = resultSet -> {

            List<Item> products = new ArrayList<>();

            while (resultSet.next()) {

                Item item = new Product();

                item.setName(resultSet.getString("name"));
                item.setDescription(resultSet.getString("description"));
                item.setSellingPrice(resultSet.getDouble("selling_Price"));
                item.setPurchaseCost(resultSet.getDouble("purchase_Cost"));
                item.setBrand(resultSet.getString("brand"));
                item.setCategory(resultSet.getString("category"));
                item.setSku(resultSet.getString("sku"));
                item.setBarcode(resultSet.getString("barcode"));
                item.setActive(resultSet.getBoolean("active"));

                products.add(item);
            }
            return products;
        };

        return retrieve(query,result).get();
    }
    
     public List<Item> getMyItems() throws Exception {

        String query = "SELECT PRODUCT.NAME,PRODUCT.DESCRIPTION,PRODUCT.SELLING_PRICE,PRODUCT.PURCHASE_COST,PRODUCT.BRAND,PRODUCT.CATEGORY,PRODUCT.SKU,PRODUCT.BARCODE,PRODUCT.ACTIVE,INVENTORY.STOCK ,INVENTORY.STOCK * PRODUCT.SELLING_PRICE AS SALE,INVENTORY.STOCK * PRODUCT.PURCHASE_COST AS COST FROM PRODUCT JOIN INVENTORY ON PRODUCT.SKU = INVENTORY.SKU";

        Result<List<Item>> result = resultSet -> {

            List<Item> products = new ArrayList<>();

            while (resultSet.next()) {

               Item item =new Product();

                item.setName(resultSet.getString("name"));
                item.setDescription(resultSet.getString("description"));
                item.setSellingPrice(resultSet.getDouble("selling_Price"));
                item.setPurchaseCost(resultSet.getDouble("purchase_Cost"));
                item.setBrand(resultSet.getString("brand"));
                item.setCategory(resultSet.getString("category"));
                item.setSku(resultSet.getString("sku"));
                item.setBarcode(resultSet.getString("barcode"));
                item.setActive(resultSet.getBoolean("active"));
                item.setStock(resultSet.getInt("STOCK"));
                products.add(item);
            }
            return products;
        };

        return retrieve(query,result).get();
    }
     
    
}

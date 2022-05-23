package manager.util;

import db.tables.SimpleDb;
import db.tables.BrandDb;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


class Test extends SimpleStringProperty{
    
    private final String text;
    
    public Test(String test){
        super(test);
        text = test;
    }

    @Override
    public String toString() {
        return text;
    }
    
    
}

public class BrandManager extends Manager{

    private SimpleDb db;
    
    public BrandManager() {
        db = getDb();
        try {
            update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public SimpleDb getDb(){
        return new BrandDb();
    }
    
    @Override
    public void addToList(String brand) throws Exception {
        if(inList(brand)) return;
        db.add(brand);
        getList().add(brand);
    }

    @Override
    public void update() throws Exception {
        list = db.getList();
    }


    public void addBatch(List<String>list)throws Exception{
        db.addBatch(list);
        getList().addAll(list);
    }
    
    @Override
    public boolean inList(String value){
        return getList().contains(value);
    }

    @Override
    public boolean removeFromList(String t){
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

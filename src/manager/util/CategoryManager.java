package manager.util;

import db.tables.SimpleDb;
import db.tables.CategoryDb;

public class CategoryManager extends BrandManager{

    private SimpleDb db;
    
    public CategoryManager(){
        db = new CategoryDb();
    }
    
    @Override
    public final SimpleDb getDb(){
        return new CategoryDb();
    }
    
     @Override
    public void addToList(String brand) throws Exception {
        if(inList(brand)) return;
        db.add(brand);
        getList().add(brand);
    }
}

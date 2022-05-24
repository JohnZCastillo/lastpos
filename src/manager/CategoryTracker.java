
package manager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import db.tables.SimpleDb;
import db.tables.CategoryDb;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CategoryTracker {
    
    private List<SimpleStringProperty> categories;
    private SimpleDb db;
    
    private CategoryTracker() {
        db = new CategoryDb();
        update();
    }
    
     private final void update() {
        
        try {
            
            categories = FXCollections.observableArrayList(
                    db.getList()
                            .stream()
                            .map(car -> new Category(car))
                            .collect(Collectors.toList()));
//            
//            categories = FXCollections.o(
                   
//            
//            );
//            
//            categories = FXCollections.observableList(
//                    DataManager.getInstance()
//                    .getCategoryManager()
//                    .getList()
//                    .stream()
//                    .map(category -> new Category(category.toString()))
//                    .collect(Collectors.toList())
//            );
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static CategoryTracker getInstance() {
        return CategoryManagerHolder.INSTANCE;
    }
    
    private static class CategoryManagerHolder {

        private static final CategoryTracker INSTANCE = new CategoryTracker();
    }
    
    public  List<SimpleStringProperty> getCategories(){
        return categories;
    }
    
    public boolean inList(String name){
        return categories.contains(new Category(name));
    }
    
    public void add(String name){
        getCategories().add(new Category(name));
    }
    
    public void add(List<String> list) {
        categories.addAll(list
                .stream()
                .map(car -> new Category(car))
                .collect(Collectors.toList()));
    }
}

class Category extends SimpleStringProperty{

    private String category;
    
    public Category(String category){
        super(category);
        this.category = category;
    }

    @Override
    public String toString() {
        return category;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.category);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Category other = (Category) obj;
        return Objects.equals(this.category, other.category);
    }
    
    
}
package item;

import java.util.Objects;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Product implements Item{
    
    final private SimpleStringProperty  name = new SimpleStringProperty("");
    final private SimpleStringProperty  description = new SimpleStringProperty("");
    final private SimpleStringProperty  brand = new SimpleStringProperty("");
    final private SimpleStringProperty  category = new SimpleStringProperty("");
    final private SimpleStringProperty  barcode = new SimpleStringProperty("");
    final private SimpleStringProperty  sku = new SimpleStringProperty("");
    final private SimpleStringProperty  dateCreated = new SimpleStringProperty("");
    final private SimpleStringProperty  dateModified = new SimpleStringProperty("");
    final private SimpleDoubleProperty  sellingPrice = new SimpleDoubleProperty();
    final private SimpleDoubleProperty  purchaseCost = new SimpleDoubleProperty();
    final private SimpleBooleanProperty  vatable = new SimpleBooleanProperty();
    final private SimpleBooleanProperty  active = new SimpleBooleanProperty();
    final private SimpleIntegerProperty  stock = new SimpleIntegerProperty();


    
    
    
    @Override
    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public String getName() {
        return this.name.get();
    }
    
    @Override
    public String getBrand() {
        return this.brand.get();
    }

    @Override
    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    @Override
    public String getCategory() {
        return this.category.get();
    }

    @Override
    public void setCategory(String category) {
        this.category.set(category);
    }

    @Override
    public String getBarcode() {
        return this.barcode.get();
    }

    @Override
    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    @Override
    public String getSku() {
        return this.sku.get();
    }

    @Override
    public void setSku(String sku) {
        this.sku.set(sku);
    }

    @Override
    public String getDateCreated() {
        return this.dateCreated.get();
    }
    
    @Override
    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    @Override
    public String getDateModified() {
        return this.dateModified.get();
    }

    @Override
    public void setDateModified(String dateModified) {
        this.dateModified.set(dateModified);
    }

    @Override
    public double getSellingPrice() {
        return this.sellingPrice.get();
    }

    @Override
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice.set(sellingPrice);
    }

    @Override
    public double getPurchaseCost() {
       return this.purchaseCost.get();
    }

    @Override
    public void setPurchaseCost(double purchaseCost) {
        this.purchaseCost.set(purchaseCost);
    }

    @Override
    public boolean isVatable() {
        return this.vatable.get();
    }

    @Override
    public void setVatable(boolean vatable) {
        this.vatable.set(vatable);
    }

    @Override
    public boolean isActive() {
        return this.active.get();
    }

    @Override
    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Override
    public int getStock() {
        return this.stock.get();
    }

    @Override
    public void setStock(int stock) {
        this.stock.set(stock);
    }

    @Override
    public String getDescription() {
        return this.description.get();
    }

    @Override
    public void setDescription(String description) {
        this.description.set(description);
    }

    public SimpleStringProperty name() {
        return name;
    }

    public SimpleStringProperty description() {
        return description;
    }

    public SimpleStringProperty brand() {
        return brand;
    }

    public SimpleStringProperty category() {
        return category;
    }

    public SimpleStringProperty barcode() {
        return barcode;
    }

    public SimpleStringProperty sku() {
        return sku;
    }

    public SimpleStringProperty dateCreated() {
        return dateCreated;
    }

    public SimpleStringProperty dateModified() {
        return dateModified;
    }

    public SimpleDoubleProperty sellingPrice() {
        return sellingPrice;
    }

    public SimpleDoubleProperty purchaseCost() {
        return purchaseCost;
    }

    public SimpleBooleanProperty vatable() {
        return vatable;
    }

    public SimpleBooleanProperty active() {
        return active;
    }

    public SimpleIntegerProperty stock() {
        return stock;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.name);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.barcode, other.barcode)) {
            return false;
        }
        return Objects.equals(this.sku, other.sku);
    }
 
}

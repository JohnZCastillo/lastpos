package item;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import util.CurrencyFormat;

public class CartItem {

    final private Item item;

    final private SimpleIntegerProperty quantity = new SimpleIntegerProperty();
    final private SimpleDoubleProperty total = new SimpleDoubleProperty();

    public CartItem(Item item, int quantity) {
        
        this.item = item;
        
        setQuantity(quantity);
        setTotal(item.getSellingPrice() * quantity);

        ((Product) item).sellingPrice().addListener(arg -> {
            total.set(CurrencyFormat.roundOff(item.getSellingPrice() * getQuantity()));
        });

        this.quantity.addListener(args -> {
            total.set(CurrencyFormat.roundOff(item.getSellingPrice() * getQuantity()));
        });

    }

    public Item item() {
        return item;
    }

    public SimpleIntegerProperty quantity() {
        return quantity;
    }

    public SimpleDoubleProperty total() {
        return total;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public double getTotal() {
        return this.total.get();
    }

    public void updateQuantity(int item) {
        quantity().set(quantity().get() + item);
    }
}

package transaction;

import cart.MyCart;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import util.TimeManager;
import item.CartItem;

public class Transaction {

    private int id;
    private double total;
    private double subtotal;
    private double cash;
    private double change;
    private String cashier;
    private LocalDate date;
    private LocalTime time;

    private List<CartItem> items;

    public Transaction(MyCart cart, int id) {
        this.id = id;
        items = new ArrayList(cart.getList());
        total = cart.getTotal().get();
        subtotal = cart.getTotal().get();
        cashier = "ADMIN";
        cash = cart.getCash().get();
        date = TimeManager.today();
        time = TimeManager.todayTime();
        change = cart.getChange().get();
    }

    public Transaction(int id, double total, double subtotal, double cash, double change, String cashier, LocalDate date, LocalTime time, List<CartItem> items) {
        this.id = id;
        this.total = total;
        this.subtotal = subtotal;
        this.cash = cash;
        this.change = change;
        this.cashier = cashier;
        this.date = date;
        this.time = time;
        this.items = items;
    }

    public Transaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Transaction other = (Transaction) obj;
        return this.id == other.id;
    }

}

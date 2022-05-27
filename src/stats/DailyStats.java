package stats;

import java.time.LocalDate;
import java.util.Objects;
import javafx.beans.property.SimpleDoubleProperty;
import util.TimeManager;
import util.CurrencyFormat;

public class DailyStats {

    final private SimpleDoubleProperty sale = new SimpleDoubleProperty(0);
    final private SimpleDoubleProperty cost = new SimpleDoubleProperty(0);
    final private SimpleDoubleProperty profit = new SimpleDoubleProperty(0);
    private LocalDate date;

    
    public DailyStats(){
        date = TimeManager.today();
        
        cost.addListener(args -> {
            profit.set(CurrencyFormat.roundOff(sale.get() - cost.get()));
        });

        sale.addListener(args -> {
            profit.set(CurrencyFormat.roundOff(sale.get() - cost.get()));
        });

    }
    
    public SimpleDoubleProperty cost() {
        return cost;
    }

    public SimpleDoubleProperty sale() {
        return sale;
    }

    public SimpleDoubleProperty profit() {
        return profit;
    }
      
    public double getSale() {
        return sale.get();
    }

    public void setSale(double sale) {
        this.sale.set(sale);
    }

    public double getCost() {
        return cost.get();
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    public double getProfit() {
        return profit.get();
    }

    public void setProfit(double profit) {
        this.profit.set(profit);
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date){
        this.date = date;
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
        final DailyStats other = (DailyStats) obj;
        if (!Objects.equals(this.sale, other.sale)) {
            return false;
        }
        if (!Objects.equals(this.cost, other.cost)) {
            return false;
        }
        if (!Objects.equals(this.profit, other.profit)) {
            return false;
        }
        return Objects.equals(this.date, other.date);
    }
    
}

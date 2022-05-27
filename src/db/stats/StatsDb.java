package db.stats;

import db.manager.DbManager;
import stats.DailyStats;
import db.manager.Result;
import java.time.Month;
import util.TimeManager;
import db.manager.Setter;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class StatsDb extends DbManager{

    
    public DailyStats getStatsToday()throws Exception{
       
        final String query = "SELECT SUM(SELLING_PRICE * QUANTITY)  AS SALE, SUM(PURCHASE_COST * QUANTITY)  AS COST, FROM CART WHERE  CAST(`DATE` AS DATE) = ?";
       
        Setter setter = stmt ->{
            stmt.setString(1,TimeManager.today().toString());
            stmt.executeQuery();
        };
        
       Result<DailyStats>result = rs ->{
           
           rs.next();
       
           DailyStats stats = new DailyStats();
           stats.setSale(rs.getDouble("SALE"));
           stats.setCost(rs.getDouble("COST"));
           
           return stats;
       };
       
       return retrieve(query,setter,result).get();
    }
    
    public DailyStats getMonthStats(LocalDate date) throws Exception {

        final String query = "SELECT SUM(SELLING_PRICE * QUANTITY)  AS SALE, SUM(PURCHASE_COST * QUANTITY)  AS COST, FROM CART WHERE  CAST(`DATE` AS DATE) BETWEEN ? AND ?";

        
        final LocalDate start = date.withDayOfMonth(1);
        final LocalDate end = date.with(lastDayOfMonth());
        
        Setter setter = stmt->{
            stmt.setString(1,start.toString());
            stmt.setString(2,end.toString());
            stmt.executeQuery();
        };
        
        
        Result<DailyStats> result = rs -> {

            rs.next();

            DailyStats stats = new DailyStats();
            stats.setSale(rs.getDouble("SALE"));
            stats.setCost(rs.getDouble("COST"));
            stats.setDate(date);
            
            return stats;
        };

        return retrieve(query,setter,result).get();
    }
    
    public static void main(String[] args) throws Exception{
        
        StatsDb db = new StatsDb();
        
        System.out.println("Sale: "+db.getMonthStats(LocalDate.now()).getSale());
        System.out.println("Sale: "+db.getMonthStats(LocalDate.now()).getCost());
        System.out.println("Sale: "+db.getMonthStats(LocalDate.now()).getProfit());
        
        
    }
    
}

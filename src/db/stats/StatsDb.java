package db.stats;

import db.manager.DbManager;
import stats.DailyStats;
import db.manager.Result;

public class StatsDb extends DbManager{

    
    public DailyStats getStatsToday()throws Exception{
       
        final String query = "SELECT SUM(SELLING_PRICE * QUANTITY)  AS SALE, SUM(PURCHASE_COST * QUANTITY)  AS COST, FROM CART WHERE  CAST(`DATE` AS DATE) = current_date()";
       
       Result<DailyStats>result = rs ->{
           
           rs.next();
       
           DailyStats stats = new DailyStats();
           stats.setSale(rs.getDouble("SALE"));
           stats.setCost(rs.getDouble("COST"));
           
           return stats;
       };
       
       return retrieve(query,result).get();
    }
}


package manager;

import db.stats.StatsDb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import stats.DailyStats;

public class StatsManager {
    
    private StatsDb db;
    
    final private DailyStats stats = new DailyStats();
    
    private StatsManager() {
        db = new StatsDb();
        update();
    }

    
    public void update() {
        try {
            DailyStats temp = db.getStatsToday();
           stats.setCost(temp.getCost());
            stats.setSale(temp.getSale());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public DailyStats getValue(){
        return stats;
    }
    
    public static StatsManager getInstance() {
        return StatsManagerHolder.INSTANCE;
    }
    
    private static class StatsManagerHolder {
        private static final StatsManager INSTANCE = new StatsManager();
    }
}

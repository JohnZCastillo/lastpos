
package manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transaction.Transaction;
import db.transaction.TransactionHistory;

public class TransactionManager {
    
    private ObservableList<Transaction>list;
    private TransactionHistory db = new TransactionHistory();

    
    private TransactionManager() {
        list = FXCollections.observableArrayList();
        update();
    }
    
    private void update(){
        try{
            getList().addAll(db.getHistory());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static TransactionManager getInstance() {
        return TransactionManagerHolder.INSTANCE;
    }
    
    private static class TransactionManagerHolder {
        private static final TransactionManager INSTANCE = new TransactionManager();
    }
    
    public ObservableList getList(){
        return list;
    }
    
    public void add(Transaction tr){
        getList().add(tr);
    }
}

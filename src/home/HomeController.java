
package home;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class HomeController implements Initializable {

  
    private Node product;
    private Node transaction;
    private Node history;
    private Node overview;
    
    @FXML private BorderPane mother;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try{
            
            product = FXMLLoader.load(getClass().getResource("/productView/Product.fxml"));
            transaction = FXMLLoader.load(getClass().getResource("/transactionView/Transaction.fxml"));
            history = FXMLLoader.load(getClass().getResource("/historyView/History.fxml"));
            overview = FXMLLoader.load(getClass().getResource("/overviewView/Overview.fxml"));
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    public void viewProduct(ActionEvent e){
        mother.setCenter(product);
    }
    
    public void viewTransaction(ActionEvent e){
        mother.setCenter(transaction);
    }
    
    public void viewHistory(ActionEvent e){
         mother.setCenter(history);
    }
    
     public void viewOverview(ActionEvent e){
         mother.setCenter(overview);
    }
    
}

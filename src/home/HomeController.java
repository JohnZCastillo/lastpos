
package home;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import manager.ItemManager;
import manager.CategoryTracker;
import manager.util.DataManager;

public class HomeController implements Initializable {

  
    private Node product;
    
    @FXML private BorderPane mother;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try{
            product = FXMLLoader.load(getClass().getResource("/productView/Product.fxml"));
            
        
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    public void viewProduct(ActionEvent e){
        mother.setCenter(product);
    }
    
    
    
}

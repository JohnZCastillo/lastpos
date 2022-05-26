
package home;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class HomeController implements Initializable {

    @FXML private Button b1,b2,b3,b4;
    @FXML private BorderPane mother;
  
    private Node product;
    private Node transaction;
    private Node history;
    private Node overview;
    
    private Button selected = new Button();
    
    final private String pressStyle = "-fx-background-color:#6B7887; -fx-padding:10px; -fx-alignment:center-left";
    final private String unPressStyle = "-fx-background-color:transparent; -fx-padding:10px; -fx-alignment:center-left";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try{
            
            product = FXMLLoader.load(getClass().getResource("/productView/Product.fxml"));
            transaction = FXMLLoader.load(getClass().getResource("/transactionView/Transaction.fxml"));
            history = FXMLLoader.load(getClass().getResource("/historyView/History.fxml"));
            overview = FXMLLoader.load(getClass().getResource("/overviewView/Overview.fxml"));
            
            selected = b1;
            
            mother.setCenter(overview);
            press(b1);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    public void viewOverview(ActionEvent e) {
        mother.setCenter(overview);
        press(b1);
    }

    public void viewTransaction(ActionEvent e) {
        mother.setCenter(transaction);
        press(b2);
    }

    public void viewHistory(ActionEvent e) {
        mother.setCenter(history);
        press(b3);
    }

    public void viewProduct(ActionEvent e) {
        mother.setCenter(product);
        press(b4);
    }

    private void press(Button btn){
        selected.setStyle(unPressStyle);
        selected = btn;
        selected.setStyle(pressStyle);
    }
    
}

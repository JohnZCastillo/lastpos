
package transactionView.util;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import item.Item;
import manager.ItemManager;
import util.Format;

public class InquireController implements Initializable {

    @FXML TextField searchBar;
    @FXML TextField name;
    @FXML TextField description;
    @FXML TextField price;
    @FXML Label errorLabel;
    @FXML DialogPane dialogPane;
    
    private Dialog dialog;
    
    public InquireController(){
        dialog = new Dialog();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dialog.setDialogPane(dialogPane);
    }    
    
    public void show(){
        defualtValue();
        dialog.show();
    }
    
    public void searchAction(ActionEvent e){
        
        errorLabel.setText("");
        name.setText("");
        price.setText("");
        description.setText("");
        
        if(searchBar.getText().isEmpty()){
            errorLabel.setText("Empty Search Query");
            return;
        }
        
        Optional <Item> product = ItemManager.getInstance().getItem(searchBar.getText());
        
        if(product.isEmpty()){
            errorLabel.setText("Item not found!");
            return;  
        }
        
        Item retrieveItem  = product.get();
        
        name.setText(retrieveItem.getName());
        description.setText(retrieveItem.getDescription());
        price.setText(Format.formatWithComma(retrieveItem.getSellingPrice()));
        
    }
    
    private void defualtValue(){
        errorLabel.setText("");
        name.setText("");
        price.setText("");
        description.setText("");
        searchBar.setText("");
    }
}

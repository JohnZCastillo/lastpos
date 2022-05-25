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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import item.Item;
import manager.ItemManager;
import cart.Cart;
import javafx.scene.control.ButtonType;

public class VoidController implements Initializable {

    @FXML TextField searchBar;
    @FXML TextField nameField;
    @FXML Spinner quantitySpinner;
    @FXML DialogPane dialogPane;
    @FXML Label errorLabel;

    private Dialog dialog;
    private Cart cart;
    
    private Optional<Item> product;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dialog = new Dialog();
        dialog.setDialogPane(dialogPane);
        quantitySpinner.setValueFactory(new IntegerSpinnerValueFactory​(0, 0, 0));
        dialog.getDialogPane().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });
         
        dialogPane.lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, ae -> {
            if (invalid()) {
                ae.consume(); //not valid
            }
        });
    }

    public void searchProduct(ActionEvent e) {

        nameField.setText("");
        errorLabel.setText("");
        quantitySpinner.getValueFactory().setValue(0);
        
        product = ItemManager.getInstance().getItem(searchBar.getText());
        
        if(product.isEmpty()){
            errorLabel.setText("Product Not Found!");
            return;
        }

        if (cart.inCart(product.get())) {
            nameField.setText(product.get().getName());
            quantitySpinner.setValueFactory(new IntegerSpinnerValueFactory​(1, cart.valueInCart(product.get()), 1));
            return;
        }
        
        errorLabel.setText("Product Not In Cart!");

    }

    //item, quantiy
    public void show(Cart cart) {
        defaultValue();
        this.cart = cart;
        
        if(dialog.showAndWait().get() == ButtonType.OK){
            if(!invalid()){
                cart.remove(product.get(), (int)quantitySpinner.getValue());
            }
        }
        
    }

    private void defaultValue() {
        searchBar.setText("");
        nameField.setText("");
        errorLabel.setText("");
        quantitySpinner.getValueFactory().setValue(0);
    }
    
    private boolean invalid(){
        return  product.isEmpty();
    }
}
package transactionView.util;

import cart.MyCart;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.NumberStringConverter;
import popup.Popup;
import util.Format;

public class PaymentController implements Initializable {

    @FXML private TextField paymentField;
    @FXML private DialogPane paymentDialogPane;
    
    private Dialog dialog;
    
    private double total;
    
    public PaymentController() {
        dialog = new Dialog();
    }
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        dialog.setOnShown(arg -> {
            Platform.runLater(() -> paymentField.requestFocus());
        });
   
        dialog.getDialogPane().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });
        
//        Format.lockToDouble(paymentField);
        
        Format.bindToEnlishFormat(paymentField);
        
        dialog.setDialogPane(paymentDialogPane);
        
        //lock
        paymentDialogPane.lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, ae -> {
            if (invalid()) {
                ae.consume(); //not valid
            }
        });
        
       
    }    
    
    private double getPaymentInput() throws Exception {

        if (paymentField.getText().isBlank()) throw new Exception("Blank Input!");
            
        try {
            return Double.parseDouble(paymentField.getText().replaceAll(",",""));
        } catch (Exception e) {
            throw new Exception("Incorrect Format!");
        }
    }
    
    public Optional<Double> showDialog(double total){
        
        this.total = total;
        
        paymentField.setText("");
        
        Optional<ButtonType> option = dialog.showAndWait();
        
        try {
            if (option.get() == ButtonType.OK) {
                  return Optional.of(getPaymentInput());
            }
        } catch (Exception e) {
            Popup.error(e.getMessage());
        }
        return Optional.empty();
    }
    
    private boolean invalid() {
        try {
            return getPaymentInput() < total
                    || paymentField.getText().isBlank();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    
    public static void main(String[] args) {
        
    }
}


package historyView.util;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import item.CartItem;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

public class CartData implements Initializable {

      
    @FXML private TableView <CartItem>tableView;
    @FXML TableColumn<CartItem, String> barcode;
    @FXML TableColumn<CartItem, String> name;
    @FXML TableColumn<CartItem, String> description;
    @FXML TableColumn<CartItem, Double> price;
    @FXML TableColumn<CartItem, Double> cost;
    @FXML TableColumn<CartItem, Double> quantity;
    
    private Consumer consumer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        barcode.setCellValueFactory(c ->  new ReadOnlyObjectWrapper(c.getValue().item().getBarcode()));
        name.setCellValueFactory(c ->  new ReadOnlyObjectWrapper(c.getValue().item().getName()));
        description.setCellValueFactory(c ->  new ReadOnlyObjectWrapper(c.getValue().item().getDescription()));
        price.setCellValueFactory(c ->  new ReadOnlyObjectWrapper(c.getValue().item().getSellingPrice()));
        cost.setCellValueFactory(c ->  new ReadOnlyObjectWrapper(c.getValue().item().getPurchaseCost()));
        quantity.setCellValueFactory(c ->  new ReadOnlyObjectWrapper(c.getValue().getQuantity()));
        
    }
    
    public void setGoBackAction(Consumer consumer){
        this.consumer = consumer;
    }
    
    private void clearLabels(){
     
    }
    
//    private void setLabels(){
//        idLabel.setText(data.getId()+"");
//        totalLabel.setText(data.getParseTotal());
//        cashierLabel.setText(data.getCashier());
//    }
//    
    public void show(List<CartItem>items) {
        System.out.println(items.size());
        tableView.setItems(FXCollections.observableList(items));
//        clearLabels();
//        setLabels(data);
    }
    
    public void goBackBtn(ActionEvent e){
        consumer.accept(e);
    }
 
}

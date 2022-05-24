package productView.util;

import java.util.Optional;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;

import item.Item;
import item.Product;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import util.Format;
import manager.util.DataManager;
import manager.ItemManager;
import manager.CategoryTracker;
import popup.Popup;

public class CreateProductController implements Initializable {

    @FXML DialogPane dialogPane;
    @FXML Label errorMsg;
    @FXML TextField name,description,barcode,sku,price,cost;
    @FXML ComboBox brand,category;
    
    private Dialog dialog;
    
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dialog = new Dialog();
        dialog.setDialogPane(dialogPane);
        Format.lockToDouble(price);
        Format.lockToDouble(cost);
        
        barcode.setOnKeyReleased(args -> showErrorMessage(barcodeIsUsed(barcode.getText()),"Barcode Is Already Used!"));
        sku.setOnKeyReleased(args -> showErrorMessage(skuIsUsed(sku.getText()),"SKU Is Already Used!"));
        name.setOnKeyReleased(args -> showErrorMessage(nameIsUsed(name.getText()),"Name Is Already Used!"));
        
      
        brand.getProperties().put("comboBoxRowsToMeasureWidth", 10);
        category.getProperties().put("comboBoxRowsToMeasureWidth", 10);

        brand.setOnKeyReleased(args -> {
            ListView list = (ListView) ((ComboBoxListViewSkin) brand.getSkin()).getPopupContent();
            list.scrollTo(Math.max(0, brand.getItems().indexOf(brand.getEditor().getText())));

        });
        
         category.setOnKeyReleased(args -> {
            ListView list = (ListView) ((ComboBoxListViewSkin) category.getSkin()).getPopupContent();
            list.scrollTo(Math.max(0, category.getItems().indexOf(category.getEditor().getText())));
        });
     
     
             
        dialogPane.lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, ae -> {
            if (notValid()) {
                ae.consume(); //not valid
            }
        });

    }    
    
    
    public Optional<Item> show(){
       
        showErrorMessage(false,"");
        
        clear();
        
        setComboBox();
        
        if(showForm()){
            
            Item item = new Product();

            item.setName(name.getText());
            item.setDescription(description.getText());
            item.setSellingPrice(Format.convert(price));
            item.setPurchaseCost(Format.convert(cost));
            item.setBrand(getItem(brand));
            item.setCategory(getItem(category));
            item.setSku(sku.getText());
            item.setBarcode(barcode.getText());
            item.setActive(true);
         
            saveItem(item);
            
            return Optional.ofNullable(item);
        }
        return Optional.empty();
    }
    
    private void saveItem(Item product){
        Task<Item>task = new Task(){
            @Override
            protected Item call() throws Exception {
                   
                DataManager.getInstance().getBrandManager().addToList(product.getBrand());
                DataManager.getInstance().getCategoryManager().addToList(product.getCategory());
                DataManager.getInstance().getSkuManager().addToList(product.getSku());
                
                ItemManager.getInstance().addItem(product);
                
                CategoryTracker.getInstance().add(product.getCategory());
                        
                return product;
            }
        
        };
        
        task.setOnFailed(args -> Popup.error("Failed Adding Item!"));
        task.setOnSucceeded(args -> Popup.message("Item Added!"));
        new Thread(task).start();
    }
    
    private boolean empty(TextField textField){
        return textField.getText().isBlank();
    }
   
  
    private String getItem(ComboBox box){
        return box.getSelectionModel().getSelectedItem().toString();
    }
    
    private boolean showForm(){
        return dialog.showAndWait().get() == ButtonType.OK;
    }
    
    private void clear(){
        Format.clear(name);
        Format.clear(description);
        Format.clear(barcode);
        Format.clear(price);
        Format.clear(cost);
        Format.clear(sku);
        Format.clear(brand);
        Format.clear(category);
    }
  
    private boolean notValid() {
        
        if(showErrorMessage(empty(name), "name is empty!")) return true;
        if(showErrorMessage(empty(description), "description is empty!")) return true;
        if(showErrorMessage(empty(price), "price is empty!")) return true;
        if(showErrorMessage(empty(cost), "purchase cost is empty!")) return true;
        if(showErrorMessage(empty(barcode), "barcode is empty!")) return true;
        if(showErrorMessage(empty(sku), "sku is empty!")) return true;
        if(showErrorMessage(empty(brand.getEditor()), "brand is empty!")) return true;
        if(showErrorMessage(empty(category.getEditor()), "category is empty!")) return true;      
        if(showErrorMessage(barcodeIsUsed(barcode.getText()), "barcode is already in used!")) return true;      
        if(showErrorMessage(skuIsUsed(sku.getText()), "sku is already in use!!")) return true;      
        if(showErrorMessage(nameIsUsed(name.getText()), "name is already in use!")) return true;      

        return false;
    }
    
    private void setComboBox(){
        brand.setItems(FXCollections.observableArrayList(DataManager.getInstance().getBrandManager().getList()));
        category.setItems(CategoryTracker.getInstance().getCategories());
    }
    
    public boolean barcodeIsUsed(String barcode){
       return ItemManager.getInstance().isBarcodePresent(barcode);
    }
    
    public boolean nameIsUsed(String name){
        return ItemManager.getInstance().isNamePresent(name);
    }
    
    public boolean skuIsUsed(String text){
      return DataManager.getInstance().getSkuManager().inList(text);
    }
    
    private boolean showErrorMessage(boolean status, String message){
        errorMsg.setText((status ? message : ""));
        return status;
    }
}

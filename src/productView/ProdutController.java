package productView;

import item.Item;
import item.Product;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import manager.CategoryTracker;
import manager.ItemManager;
import popup.Popup;
import util.Matcher;
import productView.util.CreateProductController;

public class ProdutController implements Initializable {

    @FXML private TableView<Item>table;
    @FXML private TableColumn<Item, String> name,description,brand,category,sku,barcode;
    @FXML private TableColumn<Item, Number> price,cost,no;
    @FXML private ComboBox categoryBox;
    @FXML private TextField search;
    @FXML private Button viewAll;
    
    private CreateProductController createProduct;
    
    final private ItemManager itemManager = ItemManager.getInstance();
    final private CategoryTracker categoryManager = CategoryTracker.getInstance();
    final private FilteredList<Item> filter = new FilteredList<>(FXCollections.observableList(itemManager.getItems()));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //numbering
        no.setSortable(false);
        no.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(column.getValue()) + 1));
        
        //values
        name.setCellValueFactory(column-> ((Product)column.getValue()).name());
        description.setCellValueFactory(column-> ((Product)column.getValue()).description());
        brand.setCellValueFactory(column-> ((Product)column.getValue()).brand());
        category.setCellValueFactory(column-> ((Product)column.getValue()).category());
        sku.setCellValueFactory(column-> ((Product)column.getValue()).sku());
        barcode.setCellValueFactory(column-> ((Product)column.getValue()).barcode());
        price.setCellValueFactory(column-> ((Product)column.getValue()).sellingPrice());
        cost.setCellValueFactory(column-> ((Product)column.getValue()).purchaseCost());
        
        //make columns editable
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(args->{

            String value = args.getOldValue();
            
            Item item = getSelectedItem();
            item.setName(args.getNewValue());
            
            if(!itemManager.updateItem(item)){
                item.setName(value);
                 Popup.error("Error Updating Name!");
                 return;
            }
            
            Popup.message("Name Updated!");
            
        });

        
        search.setOnAction(args->{
             filter.setPredicate(item -> Matcher.checkRelevance(item, search.getText()));
             table.setItems(filter);
        });
        
        //setup table;
        table.setItems(FXCollections.observableList(itemManager.getItems()));
        
        //setup categories
        categoryBox.setItems(FXCollections.observableList(categoryManager.getCategories()));
        
        //setup category actions
        categoryBox.setOnAction(args->{
            if(categoryBox.getSelectionModel().getSelectedItem() != null){
                viewByCategory(categoryBox.getSelectionModel().getSelectedItem().toString());
            }
        });
        
        //setup search
//        search.setOnAction(args -> search());
        
        //prevent from lagging
        categoryBox.getProperties().put("comboBoxRowsToMeasureWidth", 10);
        
        table.getProperties().put("TableViewToMeasureWidth", 10);
        
        //make description wraps text
        description.setCellFactory(tc -> {
            TableCell<Item, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
//            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        
        //make view All actione
        viewAll.setOnAction(args -> viewAll());
        
        try{
            FXMLLoader createProductLoader = new FXMLLoader(getClass().getResource("/productView/util/CreateProduct.fxml"));
            createProductLoader.load();
            createProduct = createProductLoader.getController();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }    
    
    //return items base on a category
    private void viewByCategory(String category){
//        Task<List<Item>>task = new Task(){
//            @Override
//            protected List<Item> call() throws Exception {
//                return itemManager.getItems()
//                        .stream()
//                        .filter(item -> item.getCategory().equals(category))
//                        .collect(Collectors.toList());
//            }
//        };
//        
//        task.setOnSucceeded(args -> {
//            
//            if(task.getValue().isEmpty()){
//                Popup.warning("Category Is Empty!");
//                return;
//            }
//            table.scrollTo(0);
//            Platform.runLater(()-> table.setItems(FXCollections.observableList(task.getValue())));
//        });
//        
//        task.setOnFailed(args -> Popup.error("Error Viewing Category"));
//        new Thread(task).start();
                
        filter.setPredicate(item -> item.getCategory().equals(category));
        table.setItems(filter);
        
    }
   
    private Item getSelectedItem(){
        return table.getSelectionModel().getSelectedItem();
    }
    
    private void search(){
        
        Task<List<Item>>task = new Task(){
            @Override
            protected List<Item> call() throws Exception {
                return itemManager.getItems()
                            .stream()
                        .filter(item -> Matcher.checkRelevance(item,search.getText()))
                        .collect(Collectors.toList());
            }
        };
        
        task.setOnSucceeded(args->{
            
            search.setText("");
        
            if(task.getValue().isEmpty()){
                Popup.warning("Empty Search Result!");
                return;
            }
            
            Platform.runLater(()->{
                table.scrollTo(0);
                table.setItems(FXCollections.observableList(task.getValue()));
            });
            
        });
        
        task.setOnFailed(args->{
            Popup.error("Something Went Wrong While Searching!");
        });
        
        new Thread(task).start();
        
    }
    
    private void viewAll(){
        Task<List<Item>> task = new Task(){
            @Override
            protected List<Item> call() throws Exception {
                    return itemManager.getItems();
            }
        };
        
        task.setOnSucceeded(args->{
            table.scrollTo(0);
            table.setItems(FXCollections.observableList(task.getValue()));
                });
        
        task.setOnFailed(args-> Popup.error("Something Wen't Wrong While Getting Your Products!"));
        
        new Thread(task).start();
    }
    
    public void addProduct(ActionEvent e){
        createProduct.show();
    }
}

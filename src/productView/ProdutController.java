package productView;

import file.FileManager;
import item.Item;
import item.Product;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.Control;
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
import productView.util.FilterController;
import excel.ExcelReader;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Row;
import manager.util.DataManager;
import manager.util.BrandManager;
import manager.util.CategoryManager;
import manager.util.SkuManager;

public class ProdutController implements Initializable {

    @FXML private TableView<Item>table;
    @FXML private TableColumn<Item, String> name,description,brand,category,sku,barcode;
    @FXML private TableColumn<Item, Number> price,cost,no;
    @FXML private ComboBox categoryBox;
    @FXML private TextField search;
    @FXML private Button viewAll;
    
    private CreateProductController createProduct;
    private FilterController filterController;
    
    final private ItemManager itemManager = ItemManager.getInstance();
    final private CategoryTracker categoryManager = CategoryTracker.getInstance();
    final private FilteredList<Item> filter = new FilteredList<>(FXCollections.observableList(itemManager.getItems()));

    final private FileManager fileManager = new FileManager();
    
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
            String query = search.getText();
             table.scrollTo(0);
             filter.setPredicate(item -> Matcher.checkRelevance(item,query));            
             table.setItems(filter);
        });
        
        //setup table;
        table.setItems(FXCollections.observableList(itemManager.getItems()));


        categoryBox.setItems(categoryManager.getCategories());

        //setup category actions
        categoryBox.setOnAction(args->{
            if(categoryBox.getSelectionModel().getSelectedItem() != null){
                viewByCategory(categoryBox.getSelectionModel().getSelectedItem().toString());
            }
        });
        
        
        //prevent from lagging
        categoryBox.getProperties().put("comboBoxRowsToMeasureWidth", 10);
        
        
        //make description wraps text
        description.setCellFactory(tc -> {
            TableCell<Item, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        
        //make view All actione
        viewAll.setOnAction(args -> viewAll());
        
        try{
            FXMLLoader createProductLoader = new FXMLLoader(getClass().getResource("/productView/util/CreateProduct.fxml"));
            FXMLLoader filterControllerLoader = new FXMLLoader(getClass().getResource("/productView/util/Filter.fxml"));
            
            createProductLoader.load();
            filterControllerLoader.load();
            
            createProduct = createProductLoader.getController();
            filterController = filterControllerLoader.getController();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }    
    
    //return items base on a category
    private void viewByCategory(String category) {
        table.scrollTo(0);
        filter.setPredicate(item -> item.getCategory().equals(category));
        table.setItems(filter);
    }

    private Item getSelectedItem() {
        return table.getSelectionModel().getSelectedItem();
    }

    private void viewAll() {
        Task<List<Item>> task = new Task() {
            @Override
            protected List<Item> call() throws Exception {
                return itemManager.getItems();
            }
        };

        task.setOnSucceeded(args -> {
            table.scrollTo(0);
            table.setItems(FXCollections.observableList(task.getValue()));
        });

        task.setOnFailed(args -> Popup.error("Something Wen't Wrong While Getting Your Products!"));

        new Thread(task).start();
    }

    public void addProduct(ActionEvent e) {
        createProduct.show();
    }

    public void filter(ActionEvent e) {
       table.scrollTo(0);
       filterController.show(table);
    }
    
    
    public void importFile(ActionEvent e){
       
        Optional<File> file = fileManager.chooseFile();
        
        if(file.isEmpty()) return;
        
        Task<Void> task = new Task(){
            @Override
            protected Object call() throws Exception {
                
                List<Item> items = new ArrayList<>();
                
                new ExcelReader(sheet ->{
                   
                    for(Row row: sheet){
                        //exclude header
                        if(row.getRowNum() == 0) continue;
                        
                        Item item = new Product();    
                        
                        item.setName(row.getCell(0).getStringCellValue());
                        item.setDescription(row.getCell(1).getStringCellValue());
                        item.setSellingPrice(row.getCell(2).getNumericCellValue());
                        item.setPurchaseCost(row.getCell(3).getNumericCellValue());
                        item.setBrand(row.getCell(4).getStringCellValue());
                        item.setCategory(row.getCell(5).getStringCellValue());
                        item.setSku(row.getCell(6).getStringCellValue());
                        item.setBarcode(row.getCell(7).getStringCellValue());
                        
                        items.add(item);
                   }
               
                }).read(file.get());
                
                final List<String> newSku = new ArrayList<>();
                final List<String> newCategory = new ArrayList<>();
                final List<String> newBrand = new ArrayList<>();

                items.forEach(item -> {

                    if (!DataManager.getInstance().getCategoryManager().inList(item.getCategory())) {
                        if(!newCategory.contains(item.getCategory())){
                            newCategory.add(item.getCategory());
                        }
                    }

                    if (!DataManager.getInstance().getBrandManager().inList(item.getBrand())) {
                        if(!newBrand.contains(item.getBrand())){
                             newBrand.add(item.getBrand());
                        }
                    }

                    if (!DataManager.getInstance().getSkuManager().inList(item.getSku())) {
                         if(!newSku.contains(item.getSku())){
                              newSku.add(item.getSku());
                        }
                    }

                });
                   
                //save new brand / categories / sku to db
                ((BrandManager)DataManager.getInstance().getBrandManager()).addBatch(newBrand);
                ((CategoryManager)DataManager.getInstance().getCategoryManager()).addBatch(newCategory);
                ((SkuManager)DataManager.getInstance().getSkuManager()).addBatch(newSku);
                    
                //track categories
                manager.CategoryTracker.getInstance().add(newCategory);
                
                //save products
                itemManager.addAll(items);
                return null;
            }
        };
        
        task.setOnSucceeded(args -> Popup.message("File Succesfuly Imported!"));
        task.setOnFailed(args -> Popup.error("File Can't Be Read!"));
        
        new Thread(task).start();
        
    }
    
}
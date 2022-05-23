package productView.util;

import gui.popup.Popup;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import static java.util.stream.Collectors.toCollection;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;
import product.Product;
import work.Employer;


public class FilterController implements Initializable {

   @FXML private RadioButton select,unselect;
   @FXML private TextField min,max,search;

   @FXML private TableView<Brand> table;
   @FXML private TableColumn<Brand,Boolean>selected;
   
   @FXML private Label error;

   //for conrolling duplicate brnds
   private List<String> brandControl;
   
   @FXML private DialogPane dialogPane;
   
   private Dialog dialog;
   private ToggleGroup group;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dialog = new Dialog();
        dialog.setDialogPane(dialogPane);
        group = new ToggleGroup();
        select.setOnAction(arg-> selections(true));
        unselect.setOnAction(arg-> selections(false));
        select.setToggleGroup(group);
        unselect.setToggleGroup(group);
        
        
        selected.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isSelect()));
        
        selected.setEditable( true );

        selected.setCellFactory(new Callback<TableColumn<Brand, Boolean>, TableCell<Brand, Boolean>>() {

            @Override
            public TableCell<Brand, Boolean> call(TableColumn<Brand, Boolean> col) {
                return new Select();
            }
        });

        
        
//  selected.setCellFactory(CheckBoxTableCell.forTableColumn(selected));
         
//        selected.setOnEditStart(args -> {
//            Brand brand = table.getSelectionModel().getSelectedItem();
//            brand.setSelect(!brand.isSelect());
//            System.out.println("Hello i am cocli!");
//            table.refresh();
//        });

        
//selected.setOnEditStart(args -> System.out.println("Sgtart"));
//selected.setOnEditCommit(args -> System.out.println("commit"));
//selected.setOnEditCancel(args -> System.out.println("cnedl"));
         
//        selected.setCellFactory(new Callback<TableColumn<Brand, Boolean>, TableCell<Brand, Boolean>>() {
//            @Override
//            public TableCell<Brand, Boolean> call(TableColumn<Brand, Boolean> param) {
//                return new CheckBoxTableCell<Brand, Boolean>() {
//                    {
//                        setAlignment(Pos.CENTER);
//                    }
//
//                    @Override
//                    public void updateItem(Boolean item, boolean empty) {
//                        if (!empty) {
//                            TableRow row = getTableRow();
//
//                            if (row != null) {
//                                int rowNo = row.getIndex();
//                                TableViewSelectionModel sm = getTableView().getSelectionModel();
//
//                                if (item) {
//                                    sm.select(rowNo);
//                                } else {
//                                    sm.clearSelection(rowNo);
//                                }
//                            }
//                        }
//
//                        super.updateItem(item, empty);
//                    }
//                };
//            }
//        });
        


      
        
        table.setEditable(true);
        
        search.setOnAction(args->{
            
           
            for(var brand: table.getItems()){
                if(brand.getName().equalsIgnoreCase(search.getText())){
                    
                    
                    table.getSelectionModel().clearSelection();
                    table.scrollTo(brand);
                    table.getSelectionModel().select(brand);
//                    ensureVisible(table,brand);
                    search.setText("");
                    showErroMessage(false,"Brand not found!");
                    return;
                }
            }
           showErroMessage(true,"Brand not found!");
        });
        
        brandControl = new ArrayList<>();
    }    
    
    public void show(TableView<Product>tableView){
        clearField();
        setBrands(tableView);
        if(dialog.showAndWait().get() == ButtonType.OK){
           startFiltering(tableView);
        }
    }

    private void startFiltering(TableView<Product> tableView) {
        
        Task<List<Product>> task = new Task() {
            @Override
            protected Object call() throws Exception {
                updateMessage("Filtering Items");
                List<Product> list = tableView.getItems()
                        .stream()
                        .filter(product -> filterBrand(product))
                        .filter(product -> filterPrice(product))
                        .collect(toCollection(ArrayList::new));
             updateMessage("Filtering Done..");
             return list;
            }
        };
        
        task.setOnSucceeded(arg -> {
            Platform.runLater(() -> {
                tableView.getItems().clear();
                tableView.getItems().addAll(task.getValue());
                Popup.message("Filtered!");
            });

        });

        task.setOnFailed(arg -> Popup.error("Error In Filtering Products"));
        Employer.getInstance().addWork(task);
        new Thread(task).start();
    }

    private boolean filterPrice(Product product) {
        try {
            double maximum = max.getText().isBlank() ? Double.MAX_VALUE : Double.parseDouble(max.getText());
            double minimum = min.getText().isBlank() ? 0 : Double.parseDouble(min.getText());

            return product.getSellingPrice() >= minimum && product.getSellingPrice() <= maximum;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    private boolean filterBrand(Product product) {
        for (var brand : table.getItems()) {
            if (brand.isSelect()) {
                if (brand.getName().equals(product.getBrand())) {
                    return false;
                }
            }

        }

        return true;
    }

//    private void select(boolean status) {
//        for (var brand : brands) {
//            brand.setSelected(status);
//        }
//    }
    
    private void selections(boolean status){
        table.getItems().forEach(brand -> brand.setSelect(status));
        table.refresh();
    }
    
    private void setBrands(TableView<Product> tableView) {
        
       
        table.getItems().clear();
        error.setStyle("-fx-text-fill: black");
        error.setText("Processing Brands Please Wait....");
        
        List<Product> products = tableView.getItems();
        
        Task<List<Brand>>task = new Task(){
            @Override
            protected Object call() throws Exception {
               
               List<Brand> brand = new ArrayList<>();
             
               for(var product: products){
                   Brand tempBrand = new Brand(product.getBrand(),false);
                   if(!brand.contains(tempBrand)){
                      brand.add(tempBrand);
                   }
               }
               
//                products.forEach(product -> {
//                    if (!brandControl.contains(product.getBrand())) {
//                        brands.add(new CheckBox(product.getBrand()));
//                        brandControl.add(product.getBrand());
//                    }
//                });
//    
//                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
return brand;
            }
        
        };
        
        task.setOnSucceeded(args ->{
            table.getItems().addAll(task.getValue());
            error.setText("");
             error.setStyle("-fx-text-fill: red");
        });
        
        new Thread(task).start();
        
//        tableView.getItems().forEach(product -> {
//            if (!brandControl.contains(product.getBrand())) {
//                brands.add(new CheckBox(product.getBrand()));
//                brandControl.add(product.getBrand());
//            }
//        });
//        
//        brands.forEach(brand -> content.getChildren().add(brand));
    }
    
    private void ensureVisible(ScrollPane scrollPane, Node node) {
        
        double heightViewPort = scrollPane.getViewportBounds().getHeight();
                double heightScrollPane = scrollPane.getContent().getBoundsInLocal().getHeight();
                double y = node.getBoundsInParent().getMaxY();
                if (y<(heightViewPort/2)){
                    scrollPane.setVvalue(0);
                    // below 0 of scrollpane

                }else if ((y>=(heightViewPort/2))&(y<=(heightScrollPane-heightViewPort/2))){
                   // between 0 and 1 of scrollpane
                    scrollPane.setVvalue((y-(heightViewPort/2))/(heightScrollPane-heightViewPort));
                }
                else if(y>= (heightScrollPane-(heightViewPort/2))){
                    // above 1 of scrollpane
                    scrollPane.setVvalue(1);

                }
    }
    
    private void showErroMessage(boolean status,String message){
        error.setText( status ? message : "");
    }
    
    private void clearField(){
        min.setText("");
        max.setText("");
        search.setText("");
        error.setText("");
    }
    
    static class Brand {

        private boolean select;
        private String name;

        public Brand(String name, boolean select) {
            this.name = name;
            this.select = select;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Brand other = (Brand) obj;
            if (this.select != other.select) {
                return false;
            }
            return Objects.equals(this.name, other.name);
        }

    }

    class Select extends TableCell<Brand, Boolean> {

        private final CheckBox box = new CheckBox();

        public Select() {
            box.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {
                  getTableView().getItems().get(getIndex()).setSelect(box.isSelected());
                }
            });
        }

        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty) {
                box.setSelected(item);
                setText(getTableView().getItems().get(getIndex()).getName());
                setGraphic(box);
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }
    
}

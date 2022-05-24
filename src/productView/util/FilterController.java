package productView.util;

import popup.Popup;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import item.Item;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;



public class FilterController implements Initializable {

   @FXML private RadioButton select,unselect;
   @FXML private TextField min,max,search;

   @FXML ListView <Brand>view;
           
   @FXML private Label error;

   @FXML private DialogPane dialogPane;
   
   private FilteredList<Item> filter;
           
   private Dialog dialog;
   private ToggleGroup group;
   
   final private Map<String,Boolean> duplicate = new HashMap<>();
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dialog = new Dialog();
        dialog.setDialogPane(dialogPane);
        group = new ToggleGroup();
        select.setOnAction(arg-> selections(true));
        unselect.setOnAction(arg-> selections(false));
        select.setToggleGroup(group);
        unselect.setToggleGroup(group);
        
        
       
        view.setCellFactory(CheckBoxListCell.forListView(brand -> {
            return brand.getSelect();
        }));
        
      
        
        
//        
//        selected.setEditable( true );
//
//        selected.setCellFactory(new Callback<TableColumn<Brand, Boolean>, TableCell<Brand, Boolean>>() {
//
//            @Override
//            public TableCell<Brand, Boolean> call(TableColumn<Brand, Boolean> col) {
//                return new Select();
//            }
//        });

        
        
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
        


      
//        
//        table.setEditable(true);
//        
//        search.setOnAction(args->{
//            
//           
//            for(var brand: table.getItems()){
//                if(brand.getName().equalsIgnoreCase(search.getText())){
//                    
//                    
//                    table.getSelectionModel().clearSelection();
//                    table.scrollTo(brand);
//                    table.getSelectionModel().select(brand);
////                    ensureVisible(table,brand);
//                    search.setText("");
//                    showErroMessage(false,"Brand not found!");
//                    return;
//                }
//            }
//           showErroMessage(true,"Brand not found!");
//        });
//        
//        brandControl = new ArrayList<>();
    }    
    
    public void show(TableView<Item>tableView){
        clearField();
        duplicate.clear();
        
        filter = new FilteredList<>(tableView.getItems());
        
        setBrands(tableView);

                
        if(dialog.showAndWait().get() == ButtonType.OK){
           startFiltering(tableView);
        }
    }

    private void startFiltering(TableView<Item> tableView) {

        Task<Void> task = new Task() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Filtering Items");
                setup();
                filter.setPredicate(item -> filterBrand(item) && filterPrice(item));
                updateMessage("Filtering Done..");
                return null;
            }
        };

        task.setOnSucceeded(arg -> {
            Platform.runLater(() -> {
                tableView.setItems(filter);
                Popup.message("Filtered!");
            });

        });

        task.setOnFailed(arg -> Popup.error("Error In Filtering Products"));
        new Thread(task).start();
    }

    private boolean filterPrice(Item item) {
        try {
            double maximum = max.getText().isBlank() ? Double.MAX_VALUE : Double.parseDouble(max.getText());
            double minimum = min.getText().isBlank() ? 0 : Double.parseDouble(min.getText());

            return item.getSellingPrice() >= minimum && item.getSellingPrice() <= maximum;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    private boolean filterBrand(Item item) {
        return duplicate.get(item.getBrand());
    }
    
    private void selections(boolean status){
        view.getItems().forEach(brand -> brand.setSelect(status));
        view.refresh();
    }
    
    private void setBrands(TableView<Item> tableView) {

        view.setItems(null);
        error.setStyle("-fx-text-fill: black");
        error.setText("Processing Brands Please Wait....");
        
        Task<List<Brand>>task = new Task(){
            @Override
            protected Object call() throws Exception {
                return tableView.getItems()
                        .stream()
                        .filter(item -> {
                            if (!duplicate.containsKey(item.getBrand())) {
                                duplicate.put(item.getBrand(),false);
                                return true;
                            }
                            return false;
                        })
                        .map(item -> new Brand(item.getBrand(), false))
                        .collect(Collectors.toList());
            }
        };
        
        task.setOnSucceeded(args ->{
            view.setItems(FXCollections.observableArrayList(task.getValue()));
             error.setText("");
             error.setStyle("-fx-text-fill: red");
        });
        
        new Thread(task).start();
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
    
    private void setup(){
        view.getItems().forEach(brand -> {
            if(duplicate.containsKey(brand.getName())){
                    duplicate.put(brand.getName(),brand.isSelect());
            }
        });
        
    }
        
    class Brand {

        final private SimpleBooleanProperty select = new SimpleBooleanProperty();
        private String name;

        public Brand(String name, boolean select) {
            this.name = name;
            this.select.setValue(select);
        }
        
        public boolean isSelect() {
            return this.select.get();
        }

        public SimpleBooleanProperty getSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select.set(select);
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

        @Override
        public String toString() {
            return name;
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

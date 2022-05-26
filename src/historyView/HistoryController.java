package historyView;

import historyView.util.CartData;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import transaction.Transaction;
import manager.TransactionManager;
import util.TimeManager;

public class HistoryController implements Initializable {

    @FXML BorderPane mother;
    @FXML Node top;
    
    //table
    @FXML private TableView<Transaction> tableView;
    @FXML private TableColumn<Transaction, String> date;
    @FXML private TableColumn<Transaction, String> cashier;
    @FXML private TableColumn<Transaction, String> total;
    @FXML private TableColumn<Transaction, Integer> id;
    @FXML private TableColumn<Transaction, String> change;
    @FXML private TableColumn<Transaction, String> cash;

    @FXML TextField searchBar;
    
    @FXML private DatePicker startDate,endDate;

    private FilteredList<Transaction>filter;
    
    private Node cartData;
    private CartData cart;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        cashier.setCellValueFactory(new PropertyValueFactory<>("cashier"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        change.setCellValueFactory(new PropertyValueFactory<>("change"));
        cash.setCellValueFactory(new PropertyValueFactory<>("cash"));
         
        util.Format.lockToInteger(searchBar);
        
        setDate();
        
        tableView.setItems(TransactionManager.getInstance().getList());
        
        filter = new FilteredList(TransactionManager.getInstance().getList());
        
        startDate.setOnAction(args -> filter());
        endDate.setOnAction(args -> filter());
        
        
        
        loadTransactinHistoryView();
         
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                cart.show(tableView.getSelectionModel().getSelectedItem().getItems());
                mother.setTop(null);
                mother.setCenter(cartData);
            }
        });
       
         
    }

    private void setDate() {
        startDate.setValue(TimeManager.today());
        endDate.setValue(TimeManager.today());
    }
    
    private void filter(){
        filter.setPredicate(tr -> 
                ( tr.getDate().isAfter(startDate.getValue())  || tr.getDate().isEqual(startDate.getValue())) 
                        && 
               ( tr.getDate().isBefore(endDate.getValue())  || tr.getDate().isEqual(endDate.getValue())));
        tableView.setItems(filter);
    }
    
//    public void setMenuItems() {
//
//        Runnable run = () -> {
//
//            List<String> cashiers = usersDb.getCashiers().get();
//
//            MenuItem defaultItem = new MenuItem("ALL");
//
//            defaultItem.setOnAction(event -> {
//                menuButton.setText("All");
//                setTable();
//                
//            });
//
//            Platform.runLater(() -> menuButton.getItems().add(defaultItem));
//
//            for (var cashier : cashiers) {
//
//                MenuItem item = new MenuItem(cashier);
//
//                item.setOnAction(event -> {
//                    menuButton.setText(cashier);
//                    setTable(cashier);
//                });
//
//                Platform.runLater(() -> menuButton.getItems().add(item));
//            }
//
//        };
//
//        new Thread(run).start();
//    }
//        
//    private String getStartDate() {
//        return startDate.getValue().toString();
//    }
//
//    private String getEndDate() {
//        return endDate.getValue().toString();
//    }
//
//    private void todayRange() {
//        startDate.setValue(LocalDate.now());
//        endDate.setValue(LocalDate.now().plusDays(1));
//    }

    private void loadTransactinHistoryView() {
        try {
            //this for loading transaction histroy view 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/historyView/util/CartDataView.fxml"));
            loader.load();

            cartData = loader.getRoot();
           cart = loader.getController();

            cart.setGoBackAction(consumer -> {
                mother.setCenter(tableView);
                mother.setTop(top);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //set table all no cahsier filter
//    private void setTable() {

//        tableView.getItems().clear();
//
//        //cashier filter name is fow now disble if you want to filter cahsier
//        //then setup db first becaise it is disabled in db
//        Runnable run = () -> {
//            Optional<List<TransactionListData>> data = transactionDb
//                    .getTransactionHistory(getStartDate(), getEndDate(), message -> Popup.message("Retrieving Transaction Failed!", Alert.AlertType.ERROR));
//
//            if (data.isPresent()) {
//                Platform.runLater(() -> tableView.getItems().addAll(data.get()));
//            }
//        };
//
//        new Thread(run).start();
//    }

    //cahsier filter
//    private void setTable(String cashier) {

//        tableView.getItems().clear();
//
//        //cashier filter name is fow now disble if you want to filter cahsier
//        //then setup db first becaise it is disabled in db
//        Runnable run = () -> {
//            Optional<List<TransactionListData>> data = transactionDb
//                    .getTransactionHistory(getStartDate(), getEndDate(), cashier, message -> Popup.message("Retrieving Transaction Failed!", Alert.AlertType.ERROR));
//
//            if (data.isPresent()) {
//                Platform.runLater(() -> tableView.getItems().addAll(data.get()));
//            }
//        };
//
//        new Thread(run).start();
//    }

    public void today(ActionEvent e) {
        setDate();
        filter();
    }
//    
    public void search(ActionEvent e){
        filter.setPredicate(tr -> tr.getId() == Integer.parseInt(searchBar.getText()));
        tableView.setItems(filter);
    }
 
    
}

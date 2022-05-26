package transactionView;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import item.Product;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import popup.Popup;
import cart.MyCart;
import cart.Cart;
import item.Item;
import manager.ItemManager;
import item.CartItem;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import transactionView.util.PaymentController;
import transactionView.util.VoidController;
import transactionView.util.InquireController;
import db.transaction.TransactionDb;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import manager.StatsManager;
import manager.TransactionManager;
import printer.ReceiptPrinter;
import transaction.Transaction;

public class TransactionController implements Initializable {
    
    @FXML private TextField searchBar;
    @FXML private Spinner spinner;
    @FXML private TableView <CartItem> tableView;
    
    @FXML private TableColumn<CartItem, String> nameColumn,descriptionColumn;
    @FXML private TableColumn<CartItem, Double> priceColumn,totalColumn;
    @FXML private TableColumn<CartItem, Integer> quantityColumn;
    @FXML private Label amountDueLabel,cashLabel,changeLabel;

    @FXML private BorderPane mother;
    
    private PaymentController payment;
    private VoidController voider;
    private InquireController inquire;
    
    final private Cart cart = new MyCart();
    
    final private ReceiptPrinter printer = new ReceiptPrinter();
    final private TransactionDb transDb = new TransactionDb();
    
    final private List<Cart> list = new ArrayList<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
            
        mother.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            final KeyCombination find = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
            final KeyCombination pay = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
            final KeyCombination voidK = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
            final KeyCombination resumeTransaction = new KeyCodeCombination(KeyCode.SPACE, KeyCombination.CONTROL_DOWN);
            final KeyCombination newTransaction = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);

            public void handle(KeyEvent ke) {
                if (find.match(ke)) {
                    inquireBtnClick(new ActionEvent());
                    ke.consume(); // <-- stops passing the event to next node
                }

                if (pay.match(ke)) {
                    paymentBtnClick(new ActionEvent());
                    ke.consume(); // <-- stops passing the event to next node
                }

                if (voidK.match(ke)) {
                    voidBtnClick(new ActionEvent());
                    ke.consume(); // <-- stops passing the event to next node
                }

//                if (newTransaction.match(ke)) {
//                    newTr();
//                    ke.consume(); // <-- stops passing the event to next node
//                }
//
//                if (resumeTransaction.match(ke)) {
//                    resumeTr();
//                    ke.consume(); // <-- stops passing the event to next node
//                }

            }
        });


    
    
        
        spinner.setValueFactory(new IntegerSpinnerValueFactory(1,100));
        
        nameColumn.setCellValueFactory(c -> ((Product)c.getValue().item()).name());
        descriptionColumn.setCellValueFactory(c -> ((Product)c.getValue().item()).description());
        priceColumn.setCellValueFactory(c -> ((Product)c.getValue().item()).sellingPrice().asObject());
        
        quantityColumn.setCellValueFactory(column -> column.getValue().quantity().asObject());
        totalColumn.setCellValueFactory(column -> column.getValue().total().asObject());
        
        tableView.setItems(((MyCart)cart).getList());
        
//        amountDueLabel.textProperty().bind(Bindings.convert(((MyCart)cart).getTotal()));
//        cashLabel.textProperty().bind(Bindings.convert(((MyCart)cart).getCash()));
//        changeLabel.textProperty().bind(Bindings.convert(((MyCart)cart).getChange()));
        
        
      Bindings.bindBidirectional( amountDueLabel.textProperty(), ((MyCart)cart).getTotal(),  NumberFormat.getNumberInstance(Locale.US));
      Bindings.bindBidirectional( cashLabel.textProperty(), ((MyCart)cart).getCash(),  NumberFormat.getNumberInstance(Locale.US));
      Bindings.bindBidirectional( changeLabel.textProperty(), ((MyCart)cart).getChange(),  NumberFormat.getNumberInstance(Locale.US));
        
        try {
            FXMLLoader paymentLoader = new FXMLLoader(getClass().getResource("/transactionView/util/PaymentPopup.fxml"));
            paymentLoader.load();
            payment = paymentLoader.getController();
            
            FXMLLoader voidLoader = new FXMLLoader(getClass().getResource("/transactionView/util/VoidPopup.fxml"));
            voidLoader.load();
            voider = voidLoader.getController();
            
            FXMLLoader inquireLoader = new FXMLLoader(getClass().getResource("/transactionView/util/InquirePopup.fxml"));
            inquireLoader.load();
            inquire = inquireLoader.getController();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    @FXML private void barcodeInput(ActionEvent event) {

        Optional<Item> item = ItemManager.getInstance().getItem(searchBar.getText());

        if (item.isEmpty()) {
            Popup.warning("Product Not Found!");
        } else {
            
          if(getQuantity() > 1){
              if(!Popup.ask("Are You Sure You Want To Input "+getQuantity()+" Products?")){
                resetQuantity();
              }
          }
            
          if(!cart.add(item.get(), getQuantity())){
              Popup.error("Unable To Add Product!");
          }
          
          
        }

        searchBar.setText("");
        resetQuantity();
    }
  
    @FXML private void paymentBtnClick(ActionEvent event) {
        
        if(checkCart()) return;
        
        Optional<Double> amount = payment.showDialog(((MyCart)cart).getTotal().get());
        
        if(amount.isEmpty())return;
        
        if(!((MyCart)cart).setCash(amount.get())){
            Popup.error("Error Setting Up Payment!");
        }
       
        saveTransaction();
        
    }
    
    @FXML private void voidBtnClick(ActionEvent event) {
         
        if(checkCart()) return;

        voider.show(cart);
    }
    
    @FXML private void inquireBtnClick(ActionEvent event) {
        inquire.show();
    }
    
    private void resetQuantity(){
        spinner.getValueFactory().setValue(1);
    }
    
    private int getQuantity(){
        return (int)spinner.getValueFactory().getValue();
    }
    
    private boolean checkCart(){
        if(cart.isEmpty()){
            Popup.warning("Cart Is Empty!");
            return true;
        }
        
        return false;
    }
    
    private void saveTransaction() {
        
        mother.setCursor(Cursor.WAIT);
        
        Task<Void> task = new Task() {
            @Override
            protected Void call() throws Exception {
                int id = transDb.saveCart(cart);
                TransactionManager.getInstance().add(new Transaction((MyCart)cart,id));
                transDb.save((MyCart) cart, id);
                StatsManager.getInstance().update();
                return null;
            }
        };

        task.setOnSucceeded(args -> {
            mother.setCursor(Cursor.DEFAULT);
            Popup.message("Transaction Completed!");
            cart.clear();
        });

        task.setOnFailed(args -> {
            mother.setCursor(Cursor.DEFAULT);
            Popup.error("Erro Saving Transaction!");
            System.out.println(task.getException());
                });
        
        new Thread(task).start();
    }
   
}
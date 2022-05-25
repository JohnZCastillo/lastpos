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
import javafx.fxml.FXMLLoader;
import transactionView.util.PaymentController;
import transactionView.util.VoidController;


public class TransactionController implements Initializable {
    
    @FXML private TextField searchBar;
    @FXML private Spinner spinner;
    @FXML private TableView <CartItem> tableView;
    
    @FXML private TableColumn<CartItem, String> nameColumn,descriptionColumn;
    @FXML private TableColumn<CartItem, Double> priceColumn,totalColumn;
    @FXML private TableColumn<CartItem, Integer> quantityColumn;
    @FXML private Label amountDueLabel,cashLabel,changeLabel;

    
    private PaymentController payment;
    private VoidController voider;
    
    final private Cart cart = new MyCart();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        spinner.setValueFactory(new IntegerSpinnerValueFactory(1,100));
        
        nameColumn.setCellValueFactory(c -> ((Product)c.getValue().item()).name());
        descriptionColumn.setCellValueFactory(c -> ((Product)c.getValue().item()).description());
        priceColumn.setCellValueFactory(c -> ((Product)c.getValue().item()).sellingPrice().asObject());
        
        quantityColumn.setCellValueFactory(column -> column.getValue().quantity().asObject());
        totalColumn.setCellValueFactory(column -> column.getValue().total().asObject());
        
        tableView.setItems(((MyCart)cart).getList());
        
        amountDueLabel.textProperty().bind(Bindings.convert(((MyCart)cart).getTotal()));
        cashLabel.textProperty().bind(Bindings.convert(((MyCart)cart).getCash()));
        changeLabel.textProperty().bind(Bindings.convert(((MyCart)cart).getChange()));
        
        try {
            FXMLLoader paymentLoader = new FXMLLoader(getClass().getResource("/transactionView/util/PaymentPopup.fxml"));
            paymentLoader.load();
            payment = paymentLoader.getController();
            
            FXMLLoader voidLoader = new FXMLLoader(getClass().getResource("/transactionView/util/VoidPopup.fxml"));
            voidLoader.load();
            voider = voidLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    @FXML
    private void barcodeInput(ActionEvent event) {

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

    @FXML
    private void paymentBtnClick(ActionEvent event) {
        
        Optional<Double> amount = payment.showDialog(((MyCart)cart).getTotal().get());
        
        if(amount.isEmpty())return;
        
        if(!((MyCart)cart).setCash(amount.get())){
            Popup.error("Error Setting Up Payment!");
        }
    }

    @FXML
    private void voidBtnClick(ActionEvent event) {
        voider.show(cart);
    }

    @FXML
    private void inquireBtnClick(ActionEvent event) {
    }
    
    
    private void resetQuantity(){
        spinner.getValueFactory().setValue(1);
    }
    
    private int getQuantity(){
        return (int)spinner.getValueFactory().getValue();
    }
    
    
//    class CartItem {
//    
//        final private Item item;
//        
//        final private SimpleIntegerProperty quantity = new SimpleIntegerProperty();
//        final private SimpleDoubleProperty total = new SimpleDoubleProperty();
//        
//        public CartItem(Item item,int quantity){
//            this.item = item;
//            setQuantity(quantity);
//            setTotal(item.getSellingPrice() * quantity);
//            
//            ((Product)item).sellingPrice().addListener(arg->{
//                total.set(item.getSellingPrice() * getQuantity());
//            });
//            
//            this.quantity.addListener(args->{
//                total.set(item.getSellingPrice() * getQuantity());
//            });
//            
//        }
//        
//        
//        public Item item(){
//            return item;
//        }
//        
//        public SimpleIntegerProperty quantity(){
//            return quantity;
//        }
//        
//        public SimpleDoubleProperty total(){
//            return total;
//        }
//        
//        public int getQuantity(){
//            return quantity.get();
//        }
//        
//        public void setQuantity(int quantity){
//            this.quantity.set(quantity);
//        }
//        
//        public void setTotal(double total){
//                this.total.set(total);
//        }
//        
//        public double getTotal(){
//            return this.total.get();
//        }
//        
//        public void updateQuantity(int item){
//            quantity().set(quantity().get() + item);
//        }
//    }
//    
//    class Cart{
//    
//        final private ObservableList<CartItem> cart;
//        
//        final private SimpleDoubleProperty total = new SimpleDoubleProperty();
//        final private SimpleDoubleProperty subtotal = new SimpleDoubleProperty();
//        final private SimpleDoubleProperty change = new SimpleDoubleProperty();
//        final private SimpleDoubleProperty cash = new SimpleDoubleProperty();
//        
//        public Cart(){
//            cart = FXCollections.observableArrayList();
//            
//            cart.addListener(new ListChangeListener<CartItem>() {
//                @Override
//                public void onChanged(Change<? extends CartItem> arg) {
//                        while(arg.next()){
//                            total.set(0);
//                            getCart().forEach(item->{
//                                total.setValue(total.get() + (item.item.getSellingPrice() * item.getQuantity()));
//                            });
//                        }
//                }
//            });
//
//        }
//        
//        public void addToCart(CartItem item){
//            getCart().add(item);
//        }
//        
//        public void removeFromCart(CartItem item){
//            getCart().remove(item); 
//        }
//        
//        public ObservableList<CartItem> getCart(){
//            return cart;
//        }
//
//        public SimpleDoubleProperty getTotal() {
//            return total;
//        }
//
//        public SimpleDoubleProperty getSubtotal() {
//            return subtotal;
//        }
//
//        public SimpleDoubleProperty getChange() {
//            return change;
//        }
//
//        public SimpleDoubleProperty getCash() {
//            return cash;
//        }
//        
//    }
    
}
package printer;

//import com.sun.javafx.print.PrintHelper;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.Units;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.transform.Scale;
import util.TimeManager;
import util.CurrencyFormat;
import javafx.print.Printer;


import cart.MyCart;
import item.Item;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.text.Font;
import popup.Popup;
        
//import com.sun.javafx.print.Units;
//import java.util.Map;
//import javafx.print.PageLayout;
//import javafx.print.PageOrientation;
//import javafx.print.Paper;
//import javafx.print.Printer;
//import javafx.print.PrinterJob;
//import javafx.scene.Node;
//import javafx.scene.control.Label;
//import javafx.scene.text.Font;
//import javafx.scene.transform.Scale;
//import logic.cart.Cart;
//import logic.cart.Computation;
//import logic.cart.MyCart;
//import logic.product.Product;
//import main.util.TimeManager;

public class ReceiptPrinter {

    //45
    
    
    private void appendLine(StringBuilder builder,int space,String what){
     
        System.out.println("recieve space: "+space);
        System.out.println("recieve what: "+what.length());
        
        StringBuilder form = new StringBuilder("%-");
        form.append(space);
        form.append("s");
        
        builder.append(String.format(form.toString(),what));
        
        for(int i = 0; i <space; i++){
            builder.append(what);
        }
    }
    
    public void appendCenter(StringBuilder builder,int gap,String text){
        int entrance = gap - text.length();
        
        for(int i = 0; i <gap; i++){
            
            if(i != (entrance/2)){
                builder.append(" ");
            }else{
                builder.append(text);
                i += text.length();
            }
        }
    }
    
    private void appendLast(String firstLine,String lastLine,StringBuilder builder){
        int first = firstLine.length();
        int last = lastLine.length();
        
        int gap = 45 - (first+last);
        
        builder.append(firstLine);
        appendLine(builder,gap," ");
        builder.append(lastLine);
    }
    
    private String body(MyCart cart){
        
        StringBuilder stringBuilder = new StringBuilder();
        
        
        cart.getItems().forEach((product,quantity)->{
            
            stringBuilder.append(product.getName());
            stringBuilder.append(System.lineSeparator());
            
            String temp = quantity + " X " + product.getSellingPrice();
            
            String last = CurrencyFormat.roundOff(quantity * product.getSellingPrice()) + "";
            
            appendLast(temp,last,stringBuilder);
            stringBuilder.append(System.lineSeparator());
        });
        
        return stringBuilder.toString();
        
    }
    
    public void print(MyCart cart) {

        Task<Node> task = new Task() {
            @Override
            protected Node call() throws Exception {
                return getPrintableText(cart);
            }
        };

        task.setOnSucceeded(args -> {
            printNode(task.getValue());
            Platform.runLater(() -> Popup.message("Printing Done!"));
                });
        task.setOnFailed(args -> {
            
            System.out.println(task.getException());
            Platform.runLater(() -> Popup.error("Printing Error!"));
                    
                });

        new Thread(task).start();
    }
    
    
    public Node getPrintableText(MyCart cart) {
         
       Label label = new Label();

        Map <Item,Integer> map = cart.getItems();
        
        StringBuilder stringBuilder = new StringBuilder();

        String currencySymbol = "$";
                
        stringBuilder.append("*********************************************" + "\n");
        stringBuilder.append("             " + "John Castillo" + "            \n");
        stringBuilder.append("*********************************************" + "\n");

        stringBuilder.append("Location: " + "masbate City" + "\n");
        stringBuilder.append("Contact Us: " + "09979116656" + "\n");
        stringBuilder.append("TIN: " + "123456" + "\n");
        stringBuilder.append("Receipt No: " + "123" + "\n");
        stringBuilder.append("Date: " + TimeManager.getTimestamp() + "\n");
        stringBuilder.append("---------------------------------------------" + "\n");

        
        stringBuilder.append(body(cart));
        stringBuilder.append(System.lineSeparator());

        appendLine(stringBuilder, 45, "-");
        stringBuilder.append(System.lineSeparator());

        appendLast("Subtotal:",cart.getTotal().get()+"", stringBuilder);
        stringBuilder.append(System.lineSeparator());

//        appendLast("Discount:", Double.toString(((MyCart) cart).getTotalDiscount()), stringBuilder);
//        stringBuilder.append(System.lineSeparator());
//
//        appendLast("Total:", Double.toString(((MyCart) cart).getTotal()), stringBuilder);
//        stringBuilder.append(System.lineSeparator());
//
//        appendLast("Cash:", Double.toString(((MyCart) cart).getPayment()), stringBuilder);
//        stringBuilder.append(System.lineSeparator());
//
//        appendLast(" ", "--------", stringBuilder);
//        stringBuilder.append(System.lineSeparator());
//
//        appendLast("Change:", Double.toString(((MyCart) cart).getChange()), stringBuilder);
//        stringBuilder.append(System.lineSeparator());
//        appendLine(stringBuilder, 45, "-");
//                stringBuilder.append(System.lineSeparator());
//        appendLast("Total Vat:", Double.toString(((MyCart) cart).getTotalVat()), stringBuilder);
//        stringBuilder.append(System.lineSeparator());
//
//        appendLast("Vat Sale:", Double.toString(((MyCart) cart).getVatSale()), stringBuilder);
//        stringBuilder.append(System.lineSeparator());
//
//        appendLast("Non Vat Sale:", Double.toString(((MyCart) cart).getNonVatSale()), stringBuilder);
//        stringBuilder.append(System.lineSeparator());

        appendLine(stringBuilder, 45, "-");
        stringBuilder.append(System.lineSeparator());
        appendCenter(stringBuilder,45,"Thank You");
       stringBuilder.append(System.lineSeparator());
        appendCenter(stringBuilder,45,"Come Again");

        //label.setFont(arg0);
        label.setText(stringBuilder.toString());
        label.setFont(new Font("Consolas", 10));
        label.setStyle("-fx-line-spacing: 0px");
        return label;
    }
     
    
    public void printNode(Node node) {

//        try {
//            FileWriter myWriter = new FileWriter("reciept.txt");
//            myWriter.write(((Label)node).getText());
//            myWriter.close();
//            System.out.println("Successfully wrote to the file.");
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
                
        Printer printer = Printer.getDefaultPrinter();

        PrinterJob printerJob = PrinterJob.createPrinterJob(printer);

        Paper paper = PrintHelper.createPaper("Roll80", 80, 590, Units.MM);

        
        PageLayout pageLayout = printerJob.getPrinter().createPageLayout(paper, PageOrientation.PORTRAIT, 0, 0, 0, 0);

        double height = node.getLayoutBounds().getHeight();
        double scale = 0.791;

        node.getTransforms().add(new Scale(scale, scale));
        
        
        if (printerJob != null) {
            
            boolean success = printerJob.printPage(pageLayout, node);
            
            
            if (success) {
                printerJob.endJob();
                System.out.println("print success");
            }
        }

    }
}
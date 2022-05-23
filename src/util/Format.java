package util;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;


public class Format {

    public static double convert(String data){
        return Double.parseDouble(data);
    }
    
    public static String formatWithComma(double number){
       return NumberFormat.getNumberInstance(Locale.US).format(number);
    }
        
    public static double convert(TextField textField){
        return Double.parseDouble(textField.getText());
    }
    
    public static Pattern getDoublePattern(){
        return Pattern.compile("\\d*|\\d+\\.\\d*");
    }
     
    public static Pattern getIntegerPattern(){
         return Pattern.compile("\\d*|\\d*");
    }
    
    public static TextFormatter getFormatter() {
        return new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return getDoublePattern().matcher(change.getControlNewText()).matches() ? change : null;
        });
    }
    
    public static TextFormatter getIntegerFormatter() {
        return new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return getIntegerPattern().matcher(change.getControlNewText()).matches() ? change : null;
        });
    }
    
    public static void lockToDouble(TextField textField){
        textField.setTextFormatter(getFormatter());
    }
    
     public static void lockToInteger(TextField textField){
        textField.setTextFormatter(getIntegerFormatter());
    }
      
    public static void clear(TextField textField){
        textField.setText("");
    }
    
    public static void clear(ComboBox box){
        box.valueProperty().set(null);
    }
}

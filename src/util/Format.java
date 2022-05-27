package util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;


public class Format {

    public static double convert(String data){
        return Double.parseDouble(data);
    }
    
    public static String formatWithComma(double number){
       return NumberFormat.getNumberInstance(Locale.US).format(number);
    }
  
     public static String formatWithComma(String number){
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
    
    public static void bindToEnlishFormat(TextField textField) {
        
        
        final char seperatorChar = ',';
        
        final Pattern p =  Pattern.compile("[0-9" + seperatorChar+"]*");
        
        textField.setTextFormatter(new TextFormatter<>(c -> {
           
            if (!c.isContentChange()) {
                return c; // no need for modification, if only the selection changes
            }
            
            String newText = c.getControlNewText();
            
            if (newText.isEmpty()) {
                return c;
            }
            
            
            if (!p.matcher(newText).matches()) {
                return null; // invalid change
            }
            
            // invert everything before the range
            int suffixCount = c.getControlText().length() - c.getRangeEnd();
            int digits = suffixCount - suffixCount / 4;
            StringBuilder sb = new StringBuilder();

            // insert seperator just before caret, if necessary
            if (digits % 3 == 0 && digits > 0 && suffixCount % 4 != 0) {
                sb.append(seperatorChar);
            }
            
            // add the rest of the digits in reversed order
            for (int i = c.getRangeStart() + c.getText().length()-1; i >= 0; i--) {
                char letter = newText.charAt(i);
                if (Character.isDigit(letter)) {
                    
                    sb.append(letter);
                    
                    digits++;
                    
                    if(letter == '.'){
                        System.out.println("Added Dot!");
                        sb.append(".");
                        digits--;
                        continue;
                    }
                    
                    if (digits % 3 == 0) {
                        sb.append(seperatorChar);
                    }
                    
                    
                    
                }

            }

            // remove seperator char, if added as last char
            if (digits % 3 == 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            
            sb.reverse();
            int length = sb.length();

            // replace with modified text
            c.setRange(0, c.getRangeEnd());
            c.setText(sb.toString());
            c.setCaretPosition(length);
            c.setAnchor(length);

            return c;
        }));
    }
   
    
}

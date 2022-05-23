package util;

import java.text.DecimalFormat;


public class CurrencyFormat {

    private final static DecimalFormat format = new DecimalFormat("#.##");
    
    public static double roundOff(double x){
        return toDouble(format.format(x));
    }
    
    public static double toDouble(String target){
        return Double.parseDouble(target);
    }
    
    public static String toString(double x){
        return ((Double)x).toString();
    }
}

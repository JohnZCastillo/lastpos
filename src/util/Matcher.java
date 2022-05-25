package util;

import item.Item;


public class Matcher {

    public static boolean same(String a,String b){
       return a.matches("(?i).*" +b+ ".*");
    }
    
     public static boolean sameReverse(String a,String b){
       return a.matches("(?i).*" + b+ ".*");
    }
     
    public static boolean clean(String a,String b){
        return a.matches("(?!).*"+b+".*");
    }
    
    public static String splice(String a,String b){
        a = a.trim();
        return b.substring(a.length()).trim();
    }
    
    public static SearchQuery searchQuery(String key, String value) {
        return new SearchQuery(
                splice(key, value),
                same(key, value)
        );
    }
    
   public static boolean checkRelevance(Item item,String text){
        return same(item.getName(),text)|| 
                same(item.getDescription(),text) ||
                same(item.getBrand(),text) ||
                same(item.getCategory(),text) ||
                same(item.getBarcode(),text) ||
                same(item.getSku(),text);
    }
    
     public static boolean checkIntegrity(Item item,String text){
        return item.getName().equalsIgnoreCase(text) ||
                item.getDescription().equalsIgnoreCase(text) ||
                item.getBrand().equalsIgnoreCase(text) ||
                item.getCategory().equalsIgnoreCase(text) ||
                item.getBarcode().equalsIgnoreCase(text) ||
                item.getSku().equalsIgnoreCase(text);
    }
     
   
}

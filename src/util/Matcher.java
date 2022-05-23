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
    
    public static void main(String[] args) {
        System.out.println("1: "+Matcher.same("daD", "bulad"));
        System.out.println("2: "+Matcher.same("AD", "bulad"));
        System.out.println("3: "+Matcher.same("test1", "bulad"));
        System.out.println("4: "+Matcher.same("castillo", "bulad"));
        System.out.println("5: "+Matcher.same("123", "bulad"));
        System.out.println("6: "+Matcher.same("13", "bulad"));
    }
    
}

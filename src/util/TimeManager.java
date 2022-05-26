package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class TimeManager {

    public static String getTimestamp(){
        return LocalDateTime.now().toString();
    }
    
    public static LocalDate today(){
        return LocalDate.now();
    }
    
    public static LocalTime todayTime(){
        return LocalTime.now();
    }
    
    public static void main(String[] args) {
        System.out.println(LocalDate.now());
    }
   
}

package util;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class TimeManager {

    public static String getTimestamp(){
        return LocalDateTime.now().toString();
    }
    
    public static LocalDate today(){
        return LocalDate.now();
    }
}

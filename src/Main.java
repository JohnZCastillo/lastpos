

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import manager.CategoryTracker;
import manager.ItemManager;
import manager.StatsManager;
import manager.TransactionManager;
import manager.util.DataManager;


public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {

        try {
            
            //preload data in another thread
            new Thread(()-> ItemManager.getInstance()).start();
            new Thread(()-> DataManager.getInstance()).start();
            new Thread(()-> CategoryTracker.getInstance()).start();
            new Thread(()-> TransactionManager.getInstance()).start();
            new Thread(()-> StatsManager.getInstance()).start();
            
            Parent root =  FXMLLoader.load(getClass().getResource("/home/Home.fxml"));
            Scene scene = new Scene(root,300,250);
            
            scene.getStylesheets().add(getClass().getResource("/css/theme.css").toExternalForm());
            
            primaryStage.setTitle("Castle POS");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }

   
    public static void main(String[] args) {
        launch(args);
    }
    
}

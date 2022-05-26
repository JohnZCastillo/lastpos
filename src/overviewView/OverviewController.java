package overviewView;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import manager.StatsManager;
import stats.DailyStats;

public class OverviewController implements Initializable{

    //labels
    @FXML Label monthlyLabel;
    @FXML Label todaySales;
    @FXML Label todaySalesCost;
    @FXML Label todayGrossIncome;
    
    //chart
    @FXML BarChart salesAndSalesCostChart;
    @FXML BarChart grossIncomeChart;
    
    @FXML CategoryAxis salesCategoryAxis;
    @FXML CategoryAxis grossIncomeCategoryAxis;
    @FXML NumberAxis salesNumberAxis;
    @FXML NumberAxis grossIncomeNumberAxis;
    
    private XYChart.Series<String,Number> salesData ;
    private XYChart.Series<String,Number> costData ;
    private XYChart.Series<String,Number> grossIncomeData ;
    
    final private DailyStats stats;
    
    private final String[] months = {
        "JANUARY",
        "FEBRUARY",
        "MARCH",
        "APRIL",
        "MAY",
        "JUNE",
        "JULY",
        "AUGUST",
        "SEPTEMBER",
        "OCTOBER",
        "NOVEMBER",
        "DECEMBER"
    };

    public OverviewController() {
        salesData = new XYChart.Series<>();
        costData = new XYChart.Series<>();
        grossIncomeData = new XYChart.Series<>();

        salesData.setName("Sales");
        costData.setName("Sales Cost");
        grossIncomeData.setName("Gross Income");
        
       stats = StatsManager.getInstance().getValue();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        grossIncomeCategoryAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(months)));
        salesCategoryAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(months)));
        
         Bindings.bindBidirectional(todaySales.textProperty(), stats.sale(),  NumberFormat.getNumberInstance(Locale.US));
         Bindings.bindBidirectional(todaySalesCost.textProperty(), stats.cost(),  NumberFormat.getNumberInstance(Locale.US));
         Bindings.bindBidirectional(todayGrossIncome.textProperty(), stats.profit(),  NumberFormat.getNumberInstance(Locale.US));
        
//        
//        salesAndSalesCostChart.getData().add(salesData);
//        salesAndSalesCostChart.getData().add(costData);
//        grossIncomeChart.getData().add(grossIncomeData);
        
    }
    
 
  
}

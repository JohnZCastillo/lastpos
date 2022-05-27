package overviewView;

import db.stats.StatsDb;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
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
    
    private Text text = new Text("hi");
    
    final private StatsDb db = new StatsDb();
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
        
          setMonthlyData();
          
          
//        salesAndSalesCostChart.getData().add(salesData);
//        salesAndSalesCostChart.getData().add(costData);
//        grossIncomeChart.getData().add(grossIncomeData);
        
        
    }
    
    private void setMonthlyData(){
        
        Task<List<DailyStats>>task = new Task(){
            @Override
            protected List<DailyStats> call() throws Exception {
                
                
                List<DailyStats> stats = new ArrayList<>();
                
                int year = LocalDate.now().getYear();
                
                for(Month month: Month.values()){
                    stats.add(db.getMonthStats(LocalDate.of(year, month, 1)));
                }
                
                return stats;
            }
        };
        
        task.setOnSucceeded(arg ->{
      
            List<XYChart.Data<String, Number>> sale = new ArrayList<>();
            List<XYChart.Data<String, Number>> cost = new ArrayList<>();
            List<XYChart.Data<String, Number>> profit = new ArrayList<>();

            for (var stats : task.getValue()) {
                sale.add(new XYChart.Data<>(stats.getDate().getMonth().toString(), stats.getSale()));
                cost.add(new XYChart.Data<>(stats.getDate().getMonth().toString(), stats.getCost()));
                profit.add(new XYChart.Data<>(stats.getDate().getMonth().toString(), stats.getProfit()));
            }

            
            salesData.getData().addAll(sale);
            costData.getData().addAll(cost);
            grossIncomeData.getData().addAll(profit);

            Platform.runLater(() -> {
                   
                for (var data : sale) {
                    data.nodeProperty().addListener(new ChangeListener<Node>() {
                        @Override
                        public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                            if (node != null) {
                                displayLabelForData(data);
                            }
                        }
                    });
                }
                
                  for (var data : cost) {
                    data.nodeProperty().addListener(new ChangeListener<Node>() {
                        @Override
                        public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                            if (node != null) {
                                displayLabelForData(data);
                            }
                        }
                    });
                }
                  
                      for (var data : profit) {
                    data.nodeProperty().addListener(new ChangeListener<Node>() {
                        @Override
                        public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                            if (node != null) {
                                displayLabelForData(data);
                            }
                        }
                    });
                }
              
                
                salesAndSalesCostChart.getData().add(salesData);
                salesAndSalesCostChart.getData().add(costData);
                grossIncomeChart.getData().add(grossIncomeData);
                
                System.out.println("Size: "+sale.size());
       

            });
        });
        
        task.setOnFailed(arg->{
            System.out.println(task.getException());
        });
        
        new Thread(task).start();
    }
    /** places a text label with a bar's value above a bar node for a given XYChart.Data */
    private void displayLabelForData(XYChart.Data<String, Number> data) {

      if((Double)data.getYValue() == 0)  return;

      final Node node = data.getNode();

      final Text dataText = new Text(util.Format.formatWithComma((Double)data.getYValue()));

      node.parentProperty().addListener(new ChangeListener<Parent>() {
        @Override public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
          Group parentGroup = (Group) parent;
          parentGroup.getChildren().add(dataText);
        }
      });

      node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
        @Override public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
          dataText.setLayoutX(
            Math.round(
              bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
            )
          );
          dataText.setLayoutY(
            Math.round(
              bounds.getMinY() - dataText.prefHeight(-1) * 0.5
            )
          );
        }
      });
    }

}

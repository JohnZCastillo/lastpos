package excel;


import java.io.File;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

    private RowReader sheetRead;
    private Workbook workbook;
    
    public ExcelReader(RowReader reader){
        this.sheetRead = reader;
    }
   
    private void closeWorkbook(){
        try{
            workbook.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Sheet getSheet(Workbook workbook){
        return workbook.getSheetAt(0);
    }
    
    public void read(File file) {
        try {
            workbook = new XSSFWorkbook(file);
            sheetRead.accept(getSheet(workbook));
            closeWorkbook();
        } catch (Exception e) {
            closeWorkbook();
        }
    }
 
    public Workbook export(){
      return workbook;
    }
    
}
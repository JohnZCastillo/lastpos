package excel;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelWriter{

    private final RowReader row;
    
    public ExcelWriter(RowReader rowReader){
        this.row = rowReader;
    }
    
    public File write() throws Exception{
        
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Products");
        
        row.accept(sheet);

        File outputFile = File.createTempFile("products", ".xlsx");
        FileOutputStream fos = new FileOutputStream(outputFile);
        workbook.write(fos);
        workbook.close();

        return outputFile;
    }
    
}
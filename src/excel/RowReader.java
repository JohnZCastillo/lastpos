package excel;


import org.apache.poi.ss.usermodel.Sheet;

@FunctionalInterface
public interface RowReader {
    public void accept(Sheet sheet) throws Exception;
}

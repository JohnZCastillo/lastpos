package file;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class FileManager {

    private Stage stage;
    private FileChooser fileChooser;
    
    public FileManager() {
        stage = new Stage();
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("Excel (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
    }
    
    public Optional<File> chooseFile() {
        return Optional.ofNullable(fileChooser.showOpenDialog(stage));
    }

    public Optional<File> saveFile() {
        return Optional.ofNullable(fileChooser.showSaveDialog(stage));
    }
    
    public void export(File exportFile, File selectedFile)throws Exception{
        Files.copy(exportFile.toPath(), selectedFile.toPath());    
    }
    
}

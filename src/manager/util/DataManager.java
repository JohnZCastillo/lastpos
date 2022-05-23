
package manager.util;

public class DataManager {

    private final Manager brandManager;
    private final Manager categoryManager;
    private final Manager skuManager;
    
    private DataManager() {
        brandManager = new BrandManager();
        categoryManager = new CategoryManager();
        skuManager = new SkuManager();
    }
    
    public static DataManager getInstance() {
        return DataManagerHolder.INSTANCE;
    }
    
    private static class DataManagerHolder {
        private static final DataManager INSTANCE = new DataManager();
    }
    
    public Manager getBrandManager(){
        return brandManager;
    }
    
    public Manager getCategoryManager(){
        return categoryManager;
    }
    
    public Manager getSkuManager(){
        return skuManager;
    }
    
}

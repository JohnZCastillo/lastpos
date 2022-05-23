package manager.util;

import db.tables.SimpleDb;
import db.tables.SkuDb;


public class SkuManager extends BrandManager {

    @Override
    public final SimpleDb getDb() {
        return new SkuDb();
    }
    
}

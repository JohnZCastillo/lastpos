package item;



public interface Item {
    
    public String getName();

    public void setName(String name);

    public String getBrand();

    public void setBrand(String brand);

    public String getCategory();

    public void setCategory(String category) ;

    public String getBarcode();

    public void setBarcode(String barcode);

    public String getSku();

    public void setSku(String sku);

    public String getDateCreated();
    
    public void setDateCreated(String dateCreated);
    
    public String getDateModified();

    public void setDateModified(String dateModified);
    
    public double getSellingPrice();
    
    public void setSellingPrice(double sellingPrice);
    
    public double getPurchaseCost();
    
    public void setPurchaseCost(double purchaseCost);
    
    public boolean isVatable();

    public void setVatable(boolean vatable);

    public boolean isActive();

    public void setActive(boolean active);

    public int getStock();

    public void setStock(int stock);

    public String getDescription();
    
    public void setDescription(String description);
}

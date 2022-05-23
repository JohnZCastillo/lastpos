package util;

import java.util.Objects;


public class SearchQuery {

    private String value;
    private boolean status;

    public SearchQuery(String value, boolean status) {
        this.value = value;
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    

}

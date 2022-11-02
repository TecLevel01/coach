package models;

import java.util.Date;

public class initTrip {
    Date timestamp;
    String item, nPlate;
public initTrip(){}

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getnPlate() {
        return nPlate;
    }

    public void setnPlate(String nPlate) {
        this.nPlate = nPlate;
    }
}

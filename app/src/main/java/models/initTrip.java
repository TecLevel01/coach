package models;

import java.util.Date;

public class initTrip {
    Date timestamp;
    String destination;
public initTrip(){}

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDestination() {
        return destination;
    }

    public void settDestination(String Destination) {
        this.destination = Destination;
    }
}

package models;

public class LocationReader {
    Double lat;
    Double log;
    String uid;



    public LocationReader(){

    }

    public Double getLat() {
        return lat;
    }

    public Double getLog() {
        return log;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

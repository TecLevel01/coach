package models;

import java.io.Serializable;

public class Driver implements Serializable {
    String uname;
    String phone;
    String uid;

    public Driver() {
    }

    public String getUname() {
        return uname;
    }

    public String getPhone() {
        return phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

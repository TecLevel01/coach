package models;

import java.io.Serializable;

public class Driver implements Serializable {
    String name;
    String phone;
    String email;
    String password;
    String uid;

    public Driver() {
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

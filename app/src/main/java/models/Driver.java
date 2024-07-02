package models;

import java.io.Serializable;

public class Driver implements Serializable {
    String name;
    String phone;
    String email;
    String model;
    String plate;
    String password;
    String uid;
    String token;

    public void setName(String name) {
        this.name = name;
    }

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

    public String getModel() {
        return model;
    }

    public String getPlate() {
        return plate;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

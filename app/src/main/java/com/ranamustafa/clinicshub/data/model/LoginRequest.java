package com.ranamustafa.clinicshub.data.model;

public class LoginRequest {

    String username  ;
    String password ;

    public LoginRequest(String username, String s) {
        this.username = username;
        this.password = s;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

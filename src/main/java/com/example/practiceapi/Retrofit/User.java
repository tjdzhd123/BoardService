package com.example.practiceapi.Retrofit;

import java.sql.Timestamp;
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    private Timestamp createDate;
    private String birth;
    //Post
    public User( int id, String username, String password, String email, String role,
                 Timestamp createDate, String birth) {
        this.id = id;
        this.username =username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createDate = createDate;
        this.birth = birth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}

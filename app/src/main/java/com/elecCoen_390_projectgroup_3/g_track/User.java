package com.elecCoen_390_projectgroup_3.g_track;

public class User {
    private String Surname;
    private String Name;
    private String username;
    private String Email;
    private String password;

    public User() {
    }

    public User(String surname, String name, String username, String email) {
        Surname = surname;
        Name = name;
        this.username = username;
        Email = email;
    }

    public User(String surname, String name, String username, String email, String password) {
        Surname = surname;
        Name = name;
        this.username = username;
        Email = email;
        this.password = password;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

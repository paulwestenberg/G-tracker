package com.elecCoen_390_projectgroup_3.g_track.model;

public class User {
    private String Surname;
    private String Name;
    private String Email;
    private String password;

    public User() {
    }

    public User(String surname, String name, String email) {
        Surname = surname;
        Name = name;
        Email = email;
    }

    public User(String surname, String name, String email, String password) {
        Surname = surname;
        Name = name;
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

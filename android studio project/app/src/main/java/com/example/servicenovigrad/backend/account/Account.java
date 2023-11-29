package com.example.servicenovigrad.backend.account;

import java.util.ArrayList;
import java.util.List;

public class Account implements Comparable<Account> {
    private String username;
    private String nom;
    private String prenom;
    private String role;
    private String password;
    private final List<String> notifications = new ArrayList<>();

    // Empty constructor needed for retrieval from database
    public Account() {}

    // Other constructor for everything else
    public Account(String username, String nom, String prenom, String role, String password) {
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {return username;}
    public String getNom() {return nom;}
    public String getPrenom() {return prenom;}
    public String getRole() {return role;}
    public String getPassword() {return password;}
    public List<String> getNotifications() {return notifications;}
    public void setUsername(String v) {this.username = v;}
    public void setNom(String v) {this.nom = v;}
    public void setPrenom(String v) {this.prenom = v;}
    public void setRole(String v) {this.role = v;}
    public void setPassword(String v) {this.password = v;}
    public void setNotifications(List<String> notifications) {this.notifications.clear(); if (notifications != null) {this.notifications.addAll(notifications);}}

    // Comparison method for ordering in a PriorityQueue (In non-matching non-admin cases, uses String.compareTo on the roles)
    // admin > employee > client
    public int compareTo(Account b) {
        if (role.equals(b.role)) {return sign(nom.compareTo(b.nom));}

        if (role.toLowerCase().charAt(0) == 'a') {return -1;}
        else if (b.role.toLowerCase().charAt(0) == 'a') {return 1;}

        return -sign(role.compareTo(b.role));
    }

    private int sign(int a) {
        if (a == 0) {return 0;}
        return a / Math.abs(a);
    }
}

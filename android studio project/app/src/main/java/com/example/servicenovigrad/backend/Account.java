package com.example.servicenovigrad.backend;

public class Account implements Comparable<Account> {
    private String username;
    private String nom;
    private String prenom;
    private String role;
    private String password;

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
    public void setUsername(String v) {this.username = v;}
    public void setNom(String v) {this.nom = v;}
    public void setPrenom(String v) {this.prenom = v;}
    public void setRole(String v) {this.role = v;}
    public void setPassword(String v) {this.password = v;}

    // Comparison method for ordering in a PriorityQueue (In non-matching non-admin cases, uses String.compareTo on the roles)
    // admin > employee > client
    public int compareTo(Account b) {
        if (role.equals(b.role)) {return 0;}

        if (role.toLowerCase().charAt(0) == 'a') {return 1;}
        else if (b.role.toLowerCase().charAt(0) == 'a') {return -1;}

        return -role.compareTo(b.role);
    }
}

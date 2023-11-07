package com.example.servicenovigrad.backend;

public class Account implements Comparable<Account> {
    private String username;
    private String nom;
    private String prenom;
    private String role;
    private String password;

    public Account() {}
    public Account(String username, String nom, String prenom, String role, String password) {
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.password = password;
    }
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

    // Comparison method for ordering in a PriorityQueue (simply use String.compareTo on the roles)
    public int compareTo(Account b) {
        return -role.compareTo(b.role);
    }
}

package com.example.servicenovigrad.backend.account;

import com.example.servicenovigrad.backend.services.FilledForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BranchAccount extends Account {
    private final HashMap<String, Boolean> serviceMap = new HashMap<>();
    private final List<Boolean> daysList = new ArrayList<>(7);
    private int openingHours = 0;
    private int closingHours = 8;
    private String address;
    private final List<FilledForm> requests = new ArrayList<>();
    public BranchAccount() {}
    public BranchAccount(String username, String nom, String prenom, String role, String password) {
        super(username, nom, prenom, role, password);
    }
    public HashMap<String, Boolean> getServiceMap() {return serviceMap;}
    public List<Boolean> getDaysList() {return daysList;}
    public int getOpeningHours() {return openingHours;}
    public int getClosingHours() {return closingHours;}
    public String getAddress() {return address;}
    public List<FilledForm> getRequests() {return requests;}
    public void setServiceMap(HashMap<String, Boolean> serviceMap) {
        if (serviceMap != null) {
            this.serviceMap.clear();
            this.serviceMap.putAll(serviceMap);
        }
    }
    public void setDaysList(List<Boolean> daysList) {
        if (daysList != null) {
            this.daysList.clear();
            this.daysList.addAll(daysList);
        }
    }
    public void setOpeningHours(int openingHours) {this.openingHours = openingHours;}
    public void setClosingHours(int closingHours) {this.closingHours = closingHours;}
    public void setAddress(String address) {this.address = address;}
    public void setRequests(List<FilledForm> requests) {this.requests.clear(); if (requests != null) {this.requests.addAll(requests);}}
}

package com.example.servicenovigrad.backend.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompleteBranch {
    private final HashMap<String, Boolean> serviceMap = new HashMap<>();
    private List<Boolean> daysList;
    private int openingHours;
    private int closingHours;
    private String address;
    private String username;
    private final HashMap<Integer, Integer> ratingSpread = new HashMap<>();
    public CompleteBranch() {}
    public CompleteBranch(BranchAccount base) {
        serviceMap.clear();
        serviceMap.putAll(base.getServiceMap());
        daysList = base.getDaysList();
        openingHours = base.getOpeningHours();
        closingHours = base.getClosingHours();
        address = base.getAddress();
        username = base.getUsername();
    }

    public HashMap<String,Boolean> getServiceMap() {return serviceMap;}
    public List<Boolean> getDaysList() {return daysList;}
    public int getOpeningHours() {return openingHours;}
    public int getClosingHours() {return closingHours;}
    public String getAddress() {return address;}
    public String getUsername() {return username;}
    public HashMap<Integer, Integer> getRatingSpread() {return ratingSpread;}
    public void setServiceMap(HashMap<String,Boolean> serviceMap) {
        this.serviceMap.clear();
        if (serviceMap != null) {
            this.serviceMap.putAll(serviceMap);
        }
    }
    public void setDaysList(List<Boolean> daysList) {this.daysList = daysList;}
    public void setOpeningHours(int openingHours) {this.openingHours = openingHours;}
    public void setClosingHours(int closingHours) {this.closingHours = closingHours;}
    public void setAddress(String address) {this.address = address;}
    public void setUsername(String username) {this.username = username;}
    public void setRatingSpread(HashMap<Integer, Integer> ratingSpread) {
        this.ratingSpread.clear();
        if (ratingSpread != null) {
            this.ratingSpread.putAll(ratingSpread);
        }
    }
    public double rating() {
        if (ratingSpread.keySet().size() == 0) {return -1;}
        double sum = 0f;
        int totalRatings = 0;
        for (int key : ratingSpread.keySet()) {
            int numRatings = ratingSpread.get(key);
            sum += key * numRatings;
            totalRatings += numRatings;
        }
        return sum / totalRatings;
    }
}

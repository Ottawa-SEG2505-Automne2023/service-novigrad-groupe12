package com.example.servicenovigrad.backend.account;

import java.util.HashMap;
import java.util.List;

public class CompleteBranch {
    private final HashMap<String, Boolean> serviceMap = new HashMap<>();
    private List<Boolean> daysList;
    private int openingHours;
    private int closingHours;
    private String address;
    private String username;
    private final HashMap<String, Integer> ratingSpread = new HashMap<>();
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
    public HashMap<String, Integer> getRatingSpread() {return ratingSpread;}
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
    public void setRatingSpread(HashMap<String, Integer> ratingSpread) {
        this.ratingSpread.clear();
        if (ratingSpread != null) {
            this.ratingSpread.putAll(ratingSpread);
        }
    }
    public double rating() {
        if (ratingSpread.keySet().size() == 0) {return -1;}
        double sum = 0f;
        int totalRatings = 0;
        for (String key : ratingSpread.keySet()) {
            int numRatings = ratingSpread.get(key);
            sum += Integer.parseInt(""+key.charAt(1)) * numRatings;
            totalRatings += numRatings;
        }
        return sum / totalRatings;
    }

    public boolean isOpenAt(int hour) {
        return hour >= openingHours && hour < closingHours;
    }

    public static int realHoursToStoredHours(String real) {
        switch (real) {
            case "9:00":
                return 0;
            case "10:00":
                return 1;
            case "11:00":
                return 2;
            case "12:00":
                return 3;
            case "1:00":
                return 4;
            case "2:00":
                return 5;
            case "3:00":
                return 6;
            case "4:00":
                return 7;
            case "5:00":
                return 8;
            default:
                return -1;
        }
    }
}

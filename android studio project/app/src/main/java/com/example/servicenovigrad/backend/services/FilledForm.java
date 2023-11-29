package com.example.servicenovigrad.backend.services;

import com.example.servicenovigrad.backend.account.Account;

import java.util.ArrayList;
import java.util.List;

public class FilledForm {
    private Account source;
    private String name;
    private final List<String> textSequence = new ArrayList<>();
    public FilledForm() {}
    public void setSource(Account source) {this.source = source;}
    public void setName(String name) {this.name = name;}
    public void setTextSequence(List<String> textSequence) {this.textSequence.clear(); if (textSequence != null) {this.textSequence.addAll(textSequence);}}
    public Account getSource() {return source;}
    public String getName() {return name;}
    public List<String> getTextSequence() {return textSequence;}
}

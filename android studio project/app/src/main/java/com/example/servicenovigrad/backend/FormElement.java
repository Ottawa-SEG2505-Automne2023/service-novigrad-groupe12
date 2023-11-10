package com.example.servicenovigrad.backend;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FormElement {
    private ElementType type;
    private String label;
    private final ArrayList<String> extra = new ArrayList<>();
    public FormElement() {}
    public FormElement(ElementType type, String label, List<String> extra) {
        setType(type);
        setLabel(label);
        setExtra(extra);
    }
    public void setType(ElementType type) {this.type = type;}
    public void setLabel(String label) {this.label = label;}
    public void setExtra(List<String> extra) {if (extra != null) {this.extra.clear(); this.extra.addAll(extra);}}
    public ElementType getType() {return type;}
    public String getLabel() {return label;}
    public ArrayList<String> getExtra() {return extra;}
}

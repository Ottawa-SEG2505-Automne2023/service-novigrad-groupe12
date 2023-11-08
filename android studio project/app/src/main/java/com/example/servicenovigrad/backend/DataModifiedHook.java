package com.example.servicenovigrad.backend;

public class DataModifiedHook {
    private final Callable method;
    private final String key;

    // Constructor, method can be a lambda
    public DataModifiedHook(Callable method, String key) {
        this.method = method;
        this.key = key;
    }

    // Calling & getting
    public void call() {method.call(null);}
    public String getKey() {return key;}
}

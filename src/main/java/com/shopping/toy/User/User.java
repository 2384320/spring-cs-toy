package com.shopping.toy.User;

public class User {
    private final String name;
    private final String email;
    private final String d;

    private User(String name, String email, String d) {
        this.name = name;
        this.email = email;
        this.d = d;
    }

    public User of(String name, String email, String d) {
        return new User(name, email, d);
    }
}

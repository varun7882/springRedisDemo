package com.varun.redisDemo.model;

import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private String mobile;
    private int maxGuessAllowed;
    private Home home;
}

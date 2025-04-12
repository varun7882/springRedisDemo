package com.varun.redisDemo.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String mobile;
    private Home home;
}

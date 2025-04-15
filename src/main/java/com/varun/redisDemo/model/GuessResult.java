package com.varun.redisDemo.model;

import lombok.Data;

import java.util.List;

@Data
public class GuessResult {
    private String userId;
    private boolean isCorrect;
    private List<Integer> guesses;
}
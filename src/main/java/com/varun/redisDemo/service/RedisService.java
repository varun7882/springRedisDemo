package com.varun.redisDemo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varun.redisDemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // 1. Strings
    public void setString(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getString(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    // 2. Hashes
    public void setHash(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public String getHash(String key, String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }

    public Map<Object, Object> getAllHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    // 3. Lists
    public void addToList(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value); // Add to end
    }

    public List<Object> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1); // All elements
    }

    // 4. Sets
    public void addToSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public Set<Object> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // 5. Sorted Sets
    public void addToSortedSet(String key, String value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public Set<Object> getSortedSet(String key, double minScore, double maxScore) {
        return redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
    }

    public void setUserString(String key, User user) {
        redisTemplate.opsForValue().set(key, user);
    }

    public void setUserStringWithTTL(String key, User user, long timeout) {
        redisTemplate.opsForValue().set(key, user, timeout, TimeUnit.SECONDS);
    }

    public User getUserString(String key) {
        return (User) redisTemplate.opsForValue().get(key);
    }

    // 2. Hashes (User object as value)
    public void setUserHash(String key, String field, User user) {
        redisTemplate.opsForHash().put(key, field, user);
    }

    public User getUserHash(String key, String field) {
        return (User) redisTemplate.opsForHash().get(key, field);
    }

    public Map<Object, Object> getAllUserHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    // 3. Lists (User object)
    public void addUserToList(String key, User user) {
        redisTemplate.opsForList().rightPush(key, user);
    }

    public List<Object> getUserList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    // 4. Sets (User object)
    public void addUserToSet(String key, User user) {
        redisTemplate.opsForSet().add(key, user);
    }

    public Set<User> getUserSet(String key) {
        Set<Object> set = redisTemplate.opsForSet().members(key);
        System.out.println("fetched set is "+set);
        if(set != null) {
           Set<User> userSet = new HashSet<>();
           for(Object obj:set) {
               userSet.add(objectMapper.convertValue(obj,User.class));
           }
           return userSet;
        } else {
            return null;
        }
    }

    // 5. Sorted Sets (User object with score)
    public void addUserToSortedSet(String key, User user, double score) {
        redisTemplate.opsForZSet().add(key, user, score);
    }

    public Set<User> getUserSortedSet(String key, double minScore, double maxScore) {
        Set<Object> userSet = redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
        if(userSet != null) {
            Set<User> res = new HashSet<>();
            for(Object obj:userSet) {
                res.add(objectMapper.convertValue(obj,User.class));
            }
            return res;
        } else {
            return null;
        }
    }

    // Utility: Delete key (for cleanup or updates)
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}
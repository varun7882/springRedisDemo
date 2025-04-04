package com.varun.redisDemo.controller;

import com.varun.redisDemo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/redis")
public class DataController {

    @Autowired
    private RedisService redisService;

    // 1. Strings
    @PostMapping("/string")
    public ResponseEntity<String> setString(@RequestParam String key, @RequestParam String value) {
        redisService.setString(key, value);
        return ResponseEntity.ok("String set: " + key + " -> " + value);
    }

    @GetMapping("/string")
    public ResponseEntity<String> getString(@RequestParam String key) {
        String value = redisService.getString(key);
        return ResponseEntity.ok(value != null ? value : "Key not found");
    }

    // 2. Hashes
    @PostMapping("/hash")
    public ResponseEntity<String> setHash(@RequestParam String key, @RequestParam String field, @RequestParam String value) {
        redisService.setHash(key, field, value);
        return ResponseEntity.ok("Hash set: " + key + " [" + field + " -> " + value + "]");
    }

    @GetMapping("/hash/field")
    public ResponseEntity<String> getHashField(@RequestParam String key, @RequestParam String field) {
        String value = redisService.getHash(key, field);
        return ResponseEntity.ok(value != null ? value : "Field not found");
    }

    @GetMapping("/hash/all")
    public ResponseEntity<Map<Object, Object>> getAllHash(@RequestParam String key) {
        Map<Object, Object> hash = redisService.getAllHash(key);
        return ResponseEntity.ok(hash.isEmpty() ? Map.of("message", "Hash empty or not found") : hash);
    }

    // 3. Lists
    @PostMapping("/list")
    public ResponseEntity<String> addToList(@RequestParam String key, @RequestParam String value) {
        redisService.addToList(key, value);
        return ResponseEntity.ok("Added to list: " + key + " -> " + value);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Object>> getList(@RequestParam String key) {
        List<Object> list = redisService.getList(key);
        return ResponseEntity.ok(list.isEmpty() ? List.of("List empty or not found") : list);
    }

    // 4. Sets
    @PostMapping("/set")
    public ResponseEntity<String> addToSet(@RequestParam String key, @RequestParam String value) {
        redisService.addToSet(key, value);
        return ResponseEntity.ok("Added to set: " + key + " -> " + value);
    }

    @GetMapping("/set")
    public ResponseEntity<Set<Object>> getSet(@RequestParam String key) {
        Set<Object> set = redisService.getSet(key);
        return ResponseEntity.ok(set.isEmpty() ? Set.of("Set empty or not found") : set);
    }

    // 5. Sorted Sets
    @PostMapping("/sorted-set")
    public ResponseEntity<String> addToSortedSet(@RequestParam String key, @RequestParam String value, @RequestParam double score) {
        redisService.addToSortedSet(key, value, score);
        return ResponseEntity.ok("Added to sorted set: " + key + " -> " + value + " (score: " + score + ")");
    }

    @GetMapping("/sorted-set")
    public ResponseEntity<Set<Object>> getSortedSet(@RequestParam String key,
                                                    @RequestParam double minScore,
                                                    @RequestParam double maxScore) {
        Set<Object> sortedSet = redisService.getSortedSet(key, minScore, maxScore);
        return ResponseEntity.ok(sortedSet.isEmpty() ? Set.of("Sorted set empty or not found") : sortedSet);
    }

    // Delete (for cleanup)
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteKey(@RequestParam String key) {
        redisService.deleteKey(key);
        return ResponseEntity.ok("Deleted key: " + key);
    }
}

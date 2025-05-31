package com.example.eventsAmoBE.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SimpleHealthcheck {

    @GetMapping("/ping")
    public ResponseEntity<String> hc() {
        return ResponseEntity.ok("pong");
    }
}
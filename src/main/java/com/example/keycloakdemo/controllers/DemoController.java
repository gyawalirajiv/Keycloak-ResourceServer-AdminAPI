package com.example.keycloakdemo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @CrossOrigin
    @GetMapping
    @PreAuthorize("hasRole('client_user')")
    public String hello() {
        return "Hello from Spring boot & Keycloak";
    }

    @CrossOrigin
    @GetMapping("/hello-2")
    @PreAuthorize("hasRole('Manager')")
    public String hello2() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "Hello from Spring boot & Keycloak - ADMIN";
    }
}

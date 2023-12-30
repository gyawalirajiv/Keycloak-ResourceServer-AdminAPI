package com.example.keycloakdemo.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminClientService {

    private static final Logger logger = LoggerFactory.getLogger(AdminClientService.class);
    private static final String REALM_NAME = "your_realm_name";

    @Autowired
    private Keycloak keycloak;

    @PostConstruct
    public void searchUsers() {
        logger.info("Searching users in Keycloak {}", keycloak.serverInfo()
                .getInfo()
                .getSystemInfo()
                .getVersion());
//        searchByUsername("rajiv", true);
//        searchByUsername("user", false);
//        searchByUsername("1", false);
//        searchByEmail("email@gmail.com", true);
//        searchByAttributes("DOB:2000-01-05");
//        searchByRole("admin");
//        findByUserId("user_id");
//        getUserRealmRoles("user_id");
        setUserRealmRoles("user_id", "Manager");
    }

    private void searchByUsername(String username, boolean exact) {
        logger.info("Searching by username: {} (exact {})", username, exact);
        List<UserRepresentation> users = keycloak.realm(REALM_NAME)
                .users()
                .searchByUsername(username, exact);
        logger.info("Users found by username {}", users.stream()
                .map(user -> user.getUsername())
                .collect(Collectors.toList()));
    }

    private UserRepresentation findByUserId(String userId) {
        UserRepresentation user = keycloak.realm(REALM_NAME)
                .users().get(userId).toRepresentation();
        user.setEmailVerified(true);
        keycloak.realm(REALM_NAME).users().get(userId).update(user);
        return user;
    }

    private void getUserRealmRoles(String userId) {
        List<RoleRepresentation> realmRoles = keycloak.realm(REALM_NAME)
                .users().get(userId).roles().getAll().getRealmMappings();
        System.out.println(realmRoles);
    }

    private void setUserRealmRoles(String userId, String realmRoleName) {
        RoleRepresentation role = keycloak.realm(REALM_NAME)
                .roles()
                .get(realmRoleName)
                .toRepresentation();

        keycloak.realm(REALM_NAME)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(Collections.singletonList(role));

//        keycloak.realm(REALM_NAME)
//                .users()
//                .get(userId)
//                .roles()
//                .realmLevel()
//                .remove(Collections.singletonList(role));
    }

    private void searchByEmail(String email, boolean exact) {
        logger.info("Searching by email: {} (exact {})", email, exact);
        List<UserRepresentation> users = keycloak.realm(REALM_NAME)
                .users()
                .searchByEmail(email, exact);
        logger.info("Users found by email {}", users.stream()
                .map(user -> user.getEmail())
                .collect(Collectors.toList()));
    }

    private void searchByAttributes(String query) {
        logger.info("Searching by attributes: {}", query);
        List<UserRepresentation> users = keycloak.realm(REALM_NAME)
                .users()
                .searchByAttributes(query);
        logger.info("Users found by attributes {}", users.stream()
                .map(user -> user.getUsername() + " " + user.getAttributes())
                .collect(Collectors.toList()));
    }

    private void searchByGroup(String groupId) {
        logger.info("Searching by group: {}", groupId);
        List<UserRepresentation> users = keycloak.realm(REALM_NAME)
                .groups()
                .group(groupId)
                .members();
        logger.info("Users found by group {}", users.stream()
                .map(user -> user.getUsername())
                .collect(Collectors.toList()));
    }

    private void searchByRole(String roleName) {
        logger.info("Searching by role: {}", roleName);
        List<UserRepresentation> users = keycloak.realm(REALM_NAME)
                .roles()
                .get(roleName)
                .getUserMembers();
        logger.info("Users found by role {}", users.stream()
                .map(user -> user.getUsername())
                .collect(Collectors.toList()));
    }
}
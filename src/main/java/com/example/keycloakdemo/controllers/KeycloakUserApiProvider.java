package com.example.keycloakdemo.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.resource.RealmResourceProvider;

import java.util.Optional;
import java.util.stream.Stream;

public class KeycloakUserApiProvider implements RealmResourceProvider {
    private final KeycloakSession session;

    public KeycloakUserApiProvider(KeycloakSession session) {
        this.session = session;
    }

    public void close() {
    }

    public Object getResource() {
        return this;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Stream<UserRepresentation> searchUsersByGroupAndRoleName(@QueryParam("groupName") @NotNull String groupName, @QueryParam("roleName") @NotBlank String roleName) {
        RealmModel realm = session.getContext().getRealm();

        Optional<GroupModel> groupByName = session.groups()
                .getGroupsStream(realm)
                .filter(group -> group.getName().equals(groupName))
                .findAny();

        GroupModel group = groupByName.orElseThrow(() -> new NotFoundException("Group not found with name " + groupName));

        return session.users()
                .getGroupMembersStream(realm, group)
                .filter(user -> user.getRealmRoleMappingsStream().anyMatch(role -> role.getName().equals(roleName)))
                .map(user -> ModelToRepresentation.toBriefRepresentation(user));
    }
}
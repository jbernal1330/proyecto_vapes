package com.project.vapes.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.project.vapes.model.UserPermission.ADMIN_CREATE;
import static com.project.vapes.model.UserPermission.ADMIN_DELETE;
import static com.project.vapes.model.UserPermission.ADMIN_READ;
import static com.project.vapes.model.UserPermission.ADMIN_UPDATE;

@RequiredArgsConstructor
public enum UserRole {

    USER(
            Set.of(
                    ADMIN_READ,
                    ADMIN_CREATE)),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE));

    @Getter
    private final Set<UserPermission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}

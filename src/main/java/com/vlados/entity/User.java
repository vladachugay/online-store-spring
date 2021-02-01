package com.vlados.entity;

import com.vlados.dto.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Data
@Entity(name = "users")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"})
})
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, name = "username")
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean active;
    private boolean locked;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public User(UserDTO userDTO) {
         this.username = userDTO.getUsername();
        this.password = userDTO.getPassword();
        this.fullName = userDTO.getFullName();
        this.email = userDTO.getEmail();
        this.phoneNumber = userDTO.getPhoneNumber();
        this.active = true;
        this.locked = userDTO.isLocked();
        this.roles = Collections.singleton(Role.valueOf(userDTO.getRole()));
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}

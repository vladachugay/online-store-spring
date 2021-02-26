package com.vlados.service;

import com.vlados.dto.UserDTO;
import com.vlados.entity.Role;
import com.vlados.entity.User;
import com.vlados.exception.store_exc.DuplicateUsernameException;
import com.vlados.exception.store_exc.login_exc.UserDoesntExist;
import com.vlados.repository.UserRepository;
import com.vlados.util.ExceptionKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("User with username {} doesnt exits", username);
            throw new UserDoesntExist(ExceptionKeys.USER_DOESNT_EXIST);
        });
    }

    public User getCurrentUser() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> {
                    log.error("Can't get current user");
                    throw new RuntimeException();
                });
    }

    public void saveUser(UserDTO userDTO) {
        userDTO.setActive(true);
        userDTO.setRole(Role.ROLE_USER.name());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        try {
            userRepository.save(new User(userDTO));
        } catch (Exception e) {
            log.error("{} while saving new user", e.getMessage());
            throw new DuplicateUsernameException(ExceptionKeys.DUPLICATE_USERNAME);
        }
    }

    public List<User> getUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("{} while getting all user", e.getMessage());
            throw new RuntimeException();
        }
    }

    public void lockUser(User user) {
        try {
            userRepository.lockUser(user.getUsername());
        } catch (Exception e) {
            log.error("{} while trying to lock user {}", e.getMessage(), user.getUsername());
            throw new RuntimeException();
        }
    }

    public void unlockUser(User user) {
        try {
            userRepository.unlockUser(user.getUsername());
        } catch (Exception e) {
            log.error("{} while trying to unlock user {}", e.getMessage(), user.getUsername());
            throw new RuntimeException();
        }
    }
}

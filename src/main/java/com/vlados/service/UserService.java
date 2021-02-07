package com.vlados.service;

import com.vlados.dto.UserDTO;
import com.vlados.entity.User;
import com.vlados.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Username " + username + "not found"));

    }

    public User getCurrentUser()  {
        //TODO handle exception
        //TODO avoid returning null
        try {
            return userRepository.findByUsername(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            ).orElseThrow(Exception::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUser(UserDTO userDTO) {
        User user = new User(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            //TODO add exception
            System.err.println("Cant add new user");
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void lockUser(User user) {
        userRepository.lockUser(user.getUsername());
    }

    public void unlockUser(User user) {
        userRepository.unlockUser(user.getUsername());
    }
}

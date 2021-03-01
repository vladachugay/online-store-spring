package com.vlados.service;

import com.vlados.dto.UserDTO;
import com.vlados.entity.Role;
import com.vlados.entity.User;
import com.vlados.exception.store_exc.DuplicateUsernameException;
import com.vlados.repository.UserRepository;
import com.vlados.util.ExceptionKeys;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {

    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encoded password";
    private static final String ROLE_USER = Role.ROLE_USER.name();

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDTO userDTO;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService testInstance;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldSaveUser() {
        when(userDTO.getPassword()).thenReturn(PASSWORD);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userDTO.getRole()).thenReturn(ROLE_USER);

        testInstance.saveUser(userDTO);

        verify(userDTO).setActive(true);
        verify(userDTO).setRole(ROLE_USER);
        verify(userDTO).setPassword(ENCODED_PASSWORD);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void shouldThrowDuplicateUsernameException() {
        when(userDTO.getPassword()).thenReturn(PASSWORD);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userDTO.getRole()).thenReturn(ROLE_USER);
        doThrow(RuntimeException.class).when(userRepository).save(any(User.class));
        thrown.expect(DuplicateUsernameException.class);
        thrown.expectMessage(ExceptionKeys.DUPLICATE_USERNAME);

        testInstance.saveUser(userDTO);
    }
}

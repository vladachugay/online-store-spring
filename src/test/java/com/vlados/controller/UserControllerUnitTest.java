package com.vlados.controller;

import com.vlados.dto.UserDTO;
import com.vlados.exception.store_exc.DuplicateUsernameException;
import com.vlados.service.UserService;
import com.vlados.util.ExceptionKeys;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerUnitTest {

    private static final String REDIRECT_TO_LOGIN = "redirect:/login";
    private static final String REGISTRATION = "registration";
    private static final String ERROR_ATTRIBUTE_NAME = "error_msg";
    private static final String DUPLICATE_USERNAME = ExceptionKeys.DUPLICATE_USERNAME;

    @Mock
    private UserDTO userDTO;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Model model;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController testInstance;


    @Test
    public void shouldReturnRedirectToLoginPageWhenUserRegistered() {
        String result = testInstance.register(userDTO, bindingResult, model);

        assertEquals(REDIRECT_TO_LOGIN, result);
        verify(userService).saveUser(userDTO);
    }

    @Test
    public void shouldReturnRegistrationPageWhenBindingResultHasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = testInstance.register(userDTO, bindingResult, model);

        assertEquals(REGISTRATION, result);
    }

    @Test
    public void shouldReturnRegistrationPageWhenDuplicateUsername() {
        doThrow(new DuplicateUsernameException(DUPLICATE_USERNAME)).when(userService).saveUser(userDTO);

        String result = testInstance.register(userDTO, bindingResult, model);

        assertEquals(REGISTRATION, result);
        verify(model).addAttribute(ERROR_ATTRIBUTE_NAME, DUPLICATE_USERNAME);
    }
}

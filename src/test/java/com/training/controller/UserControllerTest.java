package com.training.controller;

import com.training.service.ErrorsHandlerService;
import com.training.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@WebMvcTest(UserController.class)
@MockBeans({@MockBean(UserService.class), @MockBean(ErrorsHandlerService.class)})
class UserControllerTest {

    @Autowired
    private UserService userService;
    @Autowired
    private ErrorsHandlerService errorsHandlerService;
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void getUsers() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void testCreateUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}

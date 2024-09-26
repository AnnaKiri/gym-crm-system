package com.kirillova.gymcrmsystem;

import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.security.JWTProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_2;
import static com.kirillova.gymcrmsystem.UserTestData.USER_3;
import static com.kirillova.gymcrmsystem.UserTestData.USER_4;
import static com.kirillova.gymcrmsystem.UserTestData.USER_5;
import static com.kirillova.gymcrmsystem.UserTestData.USER_6;
import static com.kirillova.gymcrmsystem.UserTestData.USER_7;
import static com.kirillova.gymcrmsystem.UserTestData.USER_8;
import static com.kirillova.gymcrmsystem.UserTestData.USER_9;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("local")
public abstract class BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTProvider jwtProvider;

    protected Map<String, String> tokens;

    @PersistenceContext
    protected EntityManager entityManager;

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    @BeforeEach
    public void filTokensMap() {
        tokens = new HashMap<>();
        for (User user : List.of(USER_1, USER_2, USER_3, USER_4, USER_5, USER_6, USER_7, USER_8, USER_9)) {
            tokens.put(user.getUsername(), jwtProvider.createToken(user.getUsername()));
        }
    }
}

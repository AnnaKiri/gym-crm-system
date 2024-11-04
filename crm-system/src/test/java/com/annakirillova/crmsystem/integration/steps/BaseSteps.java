package com.annakirillova.crmsystem.integration.steps;

import com.annakirillova.common.dto.TokenResponseDto;
import com.annakirillova.common.dto.UserDto;
import com.annakirillova.crmsystem.models.User;
import com.annakirillova.crmsystem.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.annakirillova.crmsystem.UserTestData.USERS_PASSWORDS;

public abstract class BaseSteps {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    protected ResultActions resultActions;
    protected UserDto userDto;

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    protected TokenResponseDto getTokensForUser(User user) {
        String username = user.getUsername();
        return tokenService.getUserToken(username, USERS_PASSWORDS.get(username));
    }

    protected TokenResponseDto getTokensForUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        return getTokensForUser(user);
    }
}

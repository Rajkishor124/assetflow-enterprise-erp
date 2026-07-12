package com.assetflow.security;

import com.assetflow.auth.dto.LoginRequestDTO;
import com.assetflow.auth.dto.SignupRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = "/seed_roles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testAuthenticationFlow() throws Exception {
        // 1. Signup
        SignupRequestDTO signup = new SignupRequestDTO();
        signup.setFirstName("John");
        signup.setLastName("Doe");
        signup.setEmail("john.doe@example.com");
        signup.setPassword("StrongPassword123!");

        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isCreated());

        // 2. Login
        LoginRequestDTO login = new LoginRequestDTO("john.doe@example.com", "StrongPassword123!");
        
        String responseContent = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andReturn().getResponse().getContentAsString();

        // 3. Invalid Login (wrong password)
        LoginRequestDTO invalidLogin = new LoginRequestDTO("john.doe@example.com", "Wrong123!");
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLogin)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));

        // 4. Invalid Login (wrong email)
        LoginRequestDTO invalidEmail = new LoginRequestDTO("nobody@example.com", "Wrong123!");
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmail)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));

        // 5. Access without token -> 401
        mockMvc.perform(get("/api/v1/dummy"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authentication required. Please provide a valid access token."));
    }
}

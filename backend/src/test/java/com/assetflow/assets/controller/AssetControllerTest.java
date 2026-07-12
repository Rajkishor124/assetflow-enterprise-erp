package com.assetflow.assets.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    @Sql(scripts = "/seed_roles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void givenEmployeeRole_whenGetAllAssets_thenSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/assets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "/seed_roles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void givenAdminRole_whenGetAllAssets_thenSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/assets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

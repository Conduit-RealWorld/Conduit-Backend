package com.conduit;
import com.conduit.application.user.UserService;
import com.conduit.common.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JwtUtil jwtUtil;

    @MockBean
    private UserService userService;

    @Test
    void testGetUserProfile_Unauthorized() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetUserProfile_Success() throws Exception {
        UUID testUserId = UUID.fromString("e6fb0948-c45e-4483-95c1-f5224ab1f79e");
        String jwtToken = jwtUtil.generateToken(testUserId);
        mockMvc.perform(get("/user").header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUser_success() throws Exception {
        String json = """
        {
          "user": {
            "username": "Jacob",
            "email": "jake@jake.jake",
            "password": "jakejake"
          }
        }
        """;
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUser_unprocessableEntity_nullUsername() throws Exception {
        String json = """
        {
          "user": {
            "username": null,
            "email": "jake@jake.jake",
            "password": "jakejake"
          }
        }
        """;
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testCreateUser_unprocessableEntity_nullPassword() throws Exception {
        String json = """
        {
          "user": {
            "username": "Jacob",
            "email": "jake@jake.jake",
            "password": null
          }
        }
        """;
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testCreateUser_failure_invalidEmail() throws Exception {
        String json = """
        {
          "user": {
            "username": "Jacob",
            "email": "jakejake.jake",
            "password": "123"
          }
        }
        """;
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());
    }
}

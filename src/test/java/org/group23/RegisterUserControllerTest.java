package org.group23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RegisterUserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    InMemoryUserDetailsManager userDetailsManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        // Create a test user. Delete both users that could have been created.
        if (userDetailsManager.userExists("User1")) userDetailsManager.deleteUser("User1");
        if (userDetailsManager.userExists("User2")) userDetailsManager.deleteUser("User2");
        UserDetails user = User.withUsername("User1")
                .password(passwordEncoder.encode("Password1"))
                .roles("USER")
                .build();
        userDetailsManager.createUser(user);
    }

    @Test
    @DirtiesContext
    public void showRegisterPage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));
    }

    @Test
    @DirtiesContext
    public void registerNewUser() throws Exception {
        String username = "User2";
        String password = "Password2";
        assertFalse(userDetailsManager.userExists(username));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", username)
                        .param("password", password)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"));
        assertTrue(userDetailsManager.userExists(username));
    }

    @Test
    @DirtiesContext
    public void registerExistingUser() throws Exception {
        String username = "User1";
        String password = "Password2"; // Password doesn't need to be the same
        assertTrue(userDetailsManager.userExists(username));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", username)
                        .param("password", password)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/register?userExists"));
        assertTrue(userDetailsManager.userExists(username));
    }
}

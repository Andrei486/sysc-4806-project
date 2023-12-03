package org.group23.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterUserController {

    @Autowired
    InMemoryUserDetailsManager userDetailsManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password
    ) {
        if (userDetailsManager.userExists(username)) {
            return "redirect:/register?userExists";
        } else {
            UserDetails user = User.withUsername(username)
                    .password(passwordEncoder.encode(password))
                    .roles("USER")
                    .build();
            userDetailsManager.createUser(user);
        }
        return "redirect:/login";
    }
}

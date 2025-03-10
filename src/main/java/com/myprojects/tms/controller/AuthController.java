package com.myprojects.tms.controller;

import com.myprojects.tms.model.Login;
import com.myprojects.tms.model.User;
import com.myprojects.tms.repository.UserRepository;
import com.myprojects.tms.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {
    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AuthController(JwtUtil jwtUtil, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signup")
    public ResponseEntity<?> singUp(@RequestBody User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("User is already present!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User is created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        User fetchedUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if(fetchedUser == null || !passwordEncoder.matches(user.getPassword(), fetchedUser.getPassword())){
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        log.info("Generating jwt token");
        String token = jwtUtil.generateToken(user.getEmail());
        log.info("token generated");
        return ResponseEntity.ok(Map.of("token", token));
    }
}

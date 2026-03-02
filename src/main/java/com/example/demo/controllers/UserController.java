package com.example.demo.controllers;

import com.example.demo.dto.UserCreateDTO;
import com.example.demo.dto.UserCreateEnterpriseDTO;
import com.example.demo.dto.UserResponse;
import com.example.demo.entities.User;
import com.example.demo.jwt.JwtService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = userService.findyById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/register/candidate")
    public ResponseEntity<?> insertCandidate(@RequestBody UserCreateDTO objDto) {
        UserResponse userResponse = userService.insertCandidate(objDto);

        User user = userService.findyById(userResponse.id());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userResponse.id()).toUri();

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                objDto.getEmail(),
                                objDto.getPassword()
                        )
                );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "role", user.getRole().getRoleName()
                )
        ));
    }

    @PostMapping("/register/enterprise")
    public ResponseEntity<UserResponse> insertEnterprise(@RequestBody UserCreateEnterpriseDTO objDto) {
        UserResponse user = userService.insertEnterprise(objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.id()).toUri();
        return ResponseEntity.created(uri).body(user);
    }
}

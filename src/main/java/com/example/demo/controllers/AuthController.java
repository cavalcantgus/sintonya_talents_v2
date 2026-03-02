    package com.example.demo.controllers;

    import com.example.demo.dto.LoginRequest;
    import com.example.demo.entities.User;
    import com.example.demo.jwt.JwtService;
    import com.example.demo.services.UserService;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import java.util.Map;

    @RestController
    @RequestMapping("/auth")
    public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final UserService userService;
        private final JwtService  jwtService;

        public AuthController(AuthenticationManager authenticationManager,
                              UserService userService, JwtService  jwtService) {
            this.authenticationManager = authenticationManager;
            this.userService = userService;
            this.jwtService = jwtService;
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest login) {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    login.getEmail(),
                                    login.getPassword()
                            )
                    );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            User user = userService.findByEmail(userDetails.getUsername());

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
    }

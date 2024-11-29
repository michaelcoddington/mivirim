package org.mivirim.server.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mivirim.api.security.UserCredentials;
import org.mivirim.server.service.SecurityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Login Controller", description = "Direct username/password logins")
@RequestMapping("/api/v1/login")
public class LoginController {

    private static final Logger LOG = LogManager.getLogger(LoginController.class);

    private SecurityService securityService;

    public LoginController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping
    public void login(@RequestBody UserCredentials credentials) {
        LOG.info("Logging in user {}", credentials.getUsername());
        String token = securityService.accessTokenForUser(credentials);
        LOG.info("Got access token {}", token);
    }

}

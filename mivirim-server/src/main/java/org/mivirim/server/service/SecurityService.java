package org.mivirim.server.service;

import org.mivirim.api.security.UserCredentials;

public interface SecurityService {

    String accessTokenForUser(UserCredentials credentials);

    String userForAccessToken(String token);

}

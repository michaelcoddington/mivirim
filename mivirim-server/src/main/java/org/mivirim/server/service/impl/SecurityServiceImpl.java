package org.mivirim.server.service.impl;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.mivirim.api.security.UserCredentials;
import org.mivirim.server.repository.GraphRepository;
import org.mivirim.server.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SecurityServiceImpl implements SecurityService {

    private GraphTraversalSource g;

    private GraphRepository graphRepository;

    SecurityServiceImpl(GraphTraversalSource source, GraphRepository graphRepository) {
        this.g = source;
        this.graphRepository = graphRepository;
    }

    @Override
    public String accessTokenForUser(UserCredentials credentials) {
        Map<Object, Object> userProps = graphRepository.vertexWithProperties("mivir:user", Map.of("username", credentials.getUsername(), "password", credentials.getPassword()));
        if (userProps != null) {
            return (String) userProps.get("accessToken");
        } else {
            return null;
        }
    }

    @Override
    public String userForAccessToken(String token) {

        Map<Object, Object> userProps = graphRepository.vertexWithProperties("mivir:user", Map.of("accessToken", token));
        if (userProps != null) {
            return (String) userProps.get("name");
        } else {
            return null;
        }

        //return g.V().hasLabel("mivir:user").has("accessToken", token).hasNext();
    }
}

package org.mivirim.server.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.mivirim.server.service.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

    private GraphTraversalSource g;

    UserServiceImpl(GraphTraversalSource source) {
        this.g = source;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ensureAdminUser();
    }

    @Override
    public void ensureAdminUser() {
        if (!g.V().hasLabel("mivir:user").has("username", "admin").hasNext()) {
            Vertex adminUser = g.addV("mivir:user").property(Map.of("username", "admin", "password", "pw", "accessToken", UUID.randomUUID().toString())).next();
            LOG.info("Created admin user with access token {}", adminUser.property("accessToken").value());
        }
    }

}

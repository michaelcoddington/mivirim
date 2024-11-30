package org.mivirim.server;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraphFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TraversalTest {

    @Test
    @DisplayName("Traversal tests")
    void testTraversal() {

        Configuration config = new PropertiesConfiguration();
        config.setProperty("gremlin.graph", "org.janusgraph.core.JanusGraphFactory");
        config.setProperty("storage.backend", "inmemory");
        //config.setProperty("index.search.backend", "lucene");

        GraphTraversalSource traversalSource = JanusGraphFactory.open(config).traversal();

        System.out.println(traversalSource);

        traversalSource.tx().begin();

        traversalSource.addV("test").property("name", "michael").next();
        traversalSource.tx().commit();

        traversalSource.V().forEachRemaining(vertex -> {
            System.out.println(vertex.property("name").value());
        });

    }

}

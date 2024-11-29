package org.mivirim.server.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.mivirim.server.test.dto.GraphDisplayDto;
import org.mivirim.server.test.dto.LinkDto;
import org.mivirim.server.test.dto.NodeDto;
import org.mivirim.server.test.dto.VertexCriteria;
import org.mivirim.server.test.dto.VertexLinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/v1")
public class TestController {

    private static final Logger LOG = LogManager.getLogger(TestController.class);

    private GraphTraversalSource graphTraversalSource;

    private ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("groovy");

    public TestController(GraphTraversalSource graphTraversalSource) {
        this.graphTraversalSource = graphTraversalSource;
    }

    @GetMapping("/test")
    void test() {
        graphTraversalSource.addV("system").property("name", "whatever").next();

        System.out.println("Node created");

        AtomicInteger count = new AtomicInteger();
        graphTraversalSource.V().forEachRemaining(vertex -> {
            System.out.println("I see created vertex " + vertex.property("name").value());
            count.incrementAndGet();
        });

        System.out.println("Count: " + count.get());

        graphTraversalSource.tx().commit();
    }

    @DeleteMapping("/all")
    void deleteAll() {
        graphTraversalSource.V().drop().iterate();
        graphTraversalSource.tx().commit();
    }

    @PutMapping("/domain/{name}")
    void createDomain(@PathVariable("name") String name) {
        var newDomain = graphTraversalSource.addV("scholastic:domain").property("name", name).next();
        graphTraversalSource.tx().commit();
        LOG.info("Created domain vertex {}", newDomain);
    }

    @PutMapping("/program/{name}")
    void createProgram(@PathVariable("name") String name) {
        var newProgram = graphTraversalSource.addV("scholastic:program").property("name", name).next();
        graphTraversalSource.tx().commit();
        LOG.info("Created program vertex {}", newProgram);
    }

    @PutMapping("/system/{name}")
    void createSystem(@PathVariable("name") String name) {
        var newSystem = graphTraversalSource.addV("scholastic:system").property("name", name).next();
        graphTraversalSource.tx().commit();
        LOG.info("Created system vertex {}", newSystem);
    }

    @PostMapping("/script")
    ResponseEntity<Object> runScript(@RequestBody String script) throws ScriptException {
        Bindings bindings = new SimpleBindings();
        bindings.put("g", graphTraversalSource);

        String importStatement = "import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*";

        var compiler = (Compilable) scriptEngine;
        var fullScript = String.format("%s\n\n%s", importStatement, script);
        LOG.info("Running full script {}", fullScript);
        var compiledScript = compiler.compile(fullScript);

        var result = compiledScript.eval(bindings);
        LOG.info("Result is {}", result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/link")
    ResponseEntity<Object> link(@RequestBody VertexLinkRequest linkRequest) {
        LOG.info("Got link request {}", linkRequest);

        VertexCriteria source = linkRequest.source();
        String linkType = linkRequest.linkType();
        List<VertexCriteria> targets = linkRequest.targets();

        GraphTraversal<Vertex, Vertex> start = graphTraversalSource.V().hasLabel(source.vertexDomain()).has(source.propertyName(), source.propertyValue()).as("source");
        GraphTraversal<Vertex, ?> addEdge = start;
        for (VertexCriteria criteria: targets) {
            addEdge = addEdge.addE(linkType).from("source") .to(__.V().hasLabel(criteria.vertexDomain()).has(criteria.propertyName(), criteria.propertyValue()));
        }

        LOG.info("Traversal is {}", addEdge);
        addEdge.iterate();
        graphTraversalSource.tx().commit();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/graph")
    ResponseEntity<GraphDisplayDto> getGraph() {
        GraphDisplayDto dto = new GraphDisplayDto();
        try {
            GraphTraversal<Vertex, Vertex> vertexTraversal = graphTraversalSource.V().has("name");
            vertexTraversal.forEachRemaining(vertex -> {
                NodeDto nodeDto = new NodeDto();
                nodeDto.id = vertex.id().toString();
                nodeDto.type = vertex.label();
                nodeDto.label = (String) vertex.property("name").value();
                dto.nodes.add(nodeDto);
            });

            GraphTraversal<Edge, Edge> edgeTraversal = graphTraversalSource.E();
            edgeTraversal.forEachRemaining(edge -> {
                LinkDto linkDto = new LinkDto();
                linkDto.id = edge.id().toString();
                linkDto.source = edge.outVertex().id().toString();
                linkDto.target = edge.inVertex().id().toString();
                linkDto.label = edge.label();
                dto.links.add(linkDto);
            });

            return ResponseEntity.ok(dto);
        } catch (Exception er) {
            LOG.error("Error iterating vertices", er);
            return ResponseEntity.internalServerError().build();
        }
    }

}

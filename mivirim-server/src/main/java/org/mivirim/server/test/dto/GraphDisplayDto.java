package org.mivirim.server.test.dto;

import java.util.ArrayList;
import java.util.List;

public class GraphDisplayDto {

    public List<NodeDto> nodes = new ArrayList<>();

    public List<LinkDto> links = new ArrayList<>();

    public List<NodeDto> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeDto> nodes) {
        this.nodes = nodes;
    }

    public List<LinkDto> getLinks() {
        return links;
    }

    public void setLinks(List<LinkDto> links) {
        this.links = links;
    }
}

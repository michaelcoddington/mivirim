package org.mivirim.server.test.dto;

import java.util.List;

public record VertexLinkRequest(VertexCriteria source, String linkType, List<VertexCriteria> targets) {
}

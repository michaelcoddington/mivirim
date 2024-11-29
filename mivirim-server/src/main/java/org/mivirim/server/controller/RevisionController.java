package org.mivirim.server.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mivirim.api.Revision;
import org.mivirim.server.service.RevisionService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/revision")
public class RevisionController {

    private static final Logger LOG = LogManager.getLogger(RevisionController.class);

    private RevisionService revisionService;

    RevisionController(RevisionService service) {
        this.revisionService = service;
    }

    @PutMapping
    void executeRevision(Principal principal, @RequestBody Revision revision) {
        LOG.info("Got revision request {} from principal {}", revision, principal);
        revisionService.executeRevision(principal, revision);
    }

}

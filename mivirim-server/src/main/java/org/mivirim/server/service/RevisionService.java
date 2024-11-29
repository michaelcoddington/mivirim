package org.mivirim.server.service;

import org.mivirim.api.Revision;

import java.security.Principal;

public interface RevisionService {

    void executeRevision(Principal principal, Revision revision);

}

package de.bail.master.classic.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;

@ApplicationScoped
public class LinkService {

    @ConfigProperty(name = "quarkus.http.host")
    protected String host;

    @ConfigProperty(name = "quarkus.http.port")
    protected String port;

    @ConfigProperty(name = "quarkus.ssl.native")
    protected boolean sslEnabled;

    @ConfigProperty(name = "classicmodels.link.uri.relative")
    protected boolean relativeLinks;

    public Link buildLink(String path, String rel, String contentType) {
        if (relativeLinks) {
            return Link.fromUri(path)
                    .rel(rel)
                    .type(contentType)
                    .build();
        } else {
            return Link.fromUri(UriBuilder.fromUri(getBaseUri()).path(path).build().toString())
                    .rel(rel)
                    .type(contentType)
                    .build();
        }
    }

    public Link BuildLinkRelated(String path, String contentType) {
        return buildLink(path, "related", contentType);
    }

    public String getBaseUri() {
        String http;
        if (sslEnabled) {
            http = "https://";
        } else {
            http = "http://";
        }
        return UriBuilder.fromUri(http + host + ":" + port + "/").build().toString();
    }

    public String getHost() {

        return host;
    }

    public String getPort() {
        return port;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }
}

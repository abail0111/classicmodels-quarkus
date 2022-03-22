package de.bail.classicmodels.model.dto;

public class Link<T> {

    private T id;

    private String uri;

    private String rel;

    private String type;

    public void setLink(javax.ws.rs.core.Link link) {
        rel = link.getRel();
        type = link.getType();
        uri = link.getUri().toString();
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

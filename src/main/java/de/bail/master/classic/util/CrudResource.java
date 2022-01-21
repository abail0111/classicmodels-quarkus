package de.bail.master.classic.util;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

public abstract class CrudResource<T extends GenericEntity, S extends CrudService<T, ID>, ID> {

    @Inject
    public S service;

    public final String location;

    public CrudResource() {
        throw new UnsupportedOperationException("The default constructor cannot be used for abstract resource class");
    }

    protected CrudResource(String location) {
        this.location = location;
    }

    public Response create(T entity) {
        Response response;
        try {
            entity = service.create(entity);
            response = Response.status(Response.Status.CREATED)
                    .location(UriBuilder.fromUri(location + entity.getId()).build())
                    .entity(entity).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).
                    entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return response;
    }

    public Response read(ID id) {
        Response response;
        try {
            T entity = service.getEntityById(id);
            response = Response.ok(entity).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).
                    entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return response;
    }

    public Response readAll() {
        Response response;
        try {
            List<T> entities = service.getAllEntities();
            response = Response.ok(entities).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).
                    entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return response;
    }

    public Response update(ID id, T entity) {
        Response response;
        try {
            //entity.setId(id);
            entity = service.update(entity);
            response = Response.status(Response.Status.OK)
                    .location(UriBuilder.fromUri(location + entity.getId()).build())
                    .entity(entity).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).
                    entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return response;
    }

    public Response delete(ID id) {
        Response response;
        try {
            service.deleteById(id);
            response = Response.status(Response.Status.OK).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).
                    entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return response;
    }


}

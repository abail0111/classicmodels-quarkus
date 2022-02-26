package de.bail.master.classic.util;

import de.bail.master.classic.mapper.GenericMapper;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

public abstract class CrudResource<T extends GenericEntity, K, S extends CrudService<T>, M extends GenericMapper<T, K>> {

    @Inject
    public S service;

    @Inject
    public M mapper;

    public final String location;

    public CrudResource() {
        throw new UnsupportedOperationException("The default constructor cannot be used for abstract resource class");
    }

    protected CrudResource(String location) {
        this.location = location;
    }

    public abstract void linkDTO(K dto);

    public Response create(K entity) {
        Response response;
        try {
            T newEntity = service.create(mapper.toEntity(entity));
            response = Response.status(Response.Status.CREATED)
                    .location(UriBuilder.fromUri(location + newEntity.getId()).build())
                    .entity(mapper.toResource(newEntity)).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).
                    entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return response;
    }

    public Response read(Integer id) {
        Response response;
        try {
            T entity = service.getEntityById(id);
            K dto = mapper.toResource(entity);
            linkDTO(dto);
            response = Response.ok(dto).build();
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
            List<K> dto = mapper.toResourceList(entities);
            dto.forEach(this::linkDTO);
            response = Response.ok(dto).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).
                    entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return response;
    }

    public Response readAllPagination(int offset, int limit) {
        Response response;
        try {
            List<T> entities = service.getAllEntitiesPagination(offset, limit);
            List<K> dto = mapper.toResourceList(entities);
            dto.forEach(this::linkDTO);
            int count = service.count();
            response = Response.ok(dto)
                    .header("x-total-count", count).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).
                    entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return response;
    }

    public Response update(Integer id, K entity) {
        Response response;
        try {
            T updatedEntity = mapper.toEntity(entity);
            updatedEntity.setId(id);
            updatedEntity = service.update(updatedEntity);
            K dto = mapper.toResource(updatedEntity);
            linkDTO(dto);
            response = Response.status(Response.Status.OK)
                    .location(UriBuilder.fromUri(location + updatedEntity.getId()).build())
                    .entity(dto).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).
                    entity(e.getMessage()).build();
        } catch (PersistenceException e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return response;
    }

    public Response delete(Integer id) {
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

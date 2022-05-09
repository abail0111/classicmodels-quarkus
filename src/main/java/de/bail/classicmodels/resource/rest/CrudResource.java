package de.bail.classicmodels.resource.rest;

import de.bail.classicmodels.model.enities.GenericEntity;
import de.bail.classicmodels.model.mapper.GenericMapper;
import de.bail.classicmodels.service.CrudService;
import de.bail.classicmodels.service.LinkService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

/**
 * Abstract Crud Resource
 * @param <T> Basic entity
 * @param <K> DTO of basic entity
 * @param <ID> Wrapper type of entity id
 * @param <S> Service Class
 * @param <M> Mapper Class
 */
public abstract class CrudResource<T extends GenericEntity, K, ID, S extends CrudService<T, ID>, M extends GenericMapper<T, K>> {

    @Inject
    private LinkService linkService;

    @Inject
    private S service;

    @Inject
    private M mapper;

    private final String location;

    /**
     * Default constructor.
     * Cannot be used for abstract resource class. Use {@link #CrudResource(String)} instead.
     */
    public CrudResource() {
        throw new UnsupportedOperationException("The default constructor cannot be used for abstract resource class");
    }

    /**
     * Crud Resource Constructor
     * @param location path of resource. For example '/customers/'
     */
    protected CrudResource(String location) {
        this.location = location;
    }

    /**
     * create hyper media links
     * @param dto DTO that should be linked
     */
    public abstract void linkDTO(K dto);

    /**
     * Create new entity
     * @param entity New entity
     * @return Response object
     */
    public Response create(K entity) {
        T newEntity = service.create(mapper.toEntity(entity));
        return Response.status(Response.Status.CREATED)
                .location(UriBuilder.fromUri(location + newEntity.idToString()).build())
                .entity(mapper.toResource(newEntity)).build();
    }

    /**
     * Requests a entity by id
     * @param id Requested id
     * @return Response object
     */
    public Response read(ID id) {
        T entity = service.getEntityById(id);
        K dto = mapper.toResource(entity);
        linkDTO(dto);
        return Response.ok(dto).build();
    }

    /**
     * Request a list of entities
     * @return Response object
     */
    public Response readAll() {
        List<T> entities = service.getAllEntities();
        List<K> dto = mapper.toResourceList(entities);
        dto.forEach(this::linkDTO);
        return Response.ok(dto).build();
    }

    /**
     * Request a list of entities, including pagination
     * @param offset Starting position
     * @param limit The amount of entities
     * @return Response object
     */
    public Response readAllPagination(int offset, int limit) {
        List<T> entities = service.getAllEntitiesPagination(offset, limit);
        List<K> dto = mapper.toResourceList(entities);
        dto.forEach(this::linkDTO);
        int count = service.count();
        return Response.ok(dto).header("x-total-count", count).build();
    }

    /**
     * Update/replace an existing entity
     * @param id of requested entity
     * @param entity Entity to be updated/replaced
     * @return Response object
     */
    public Response update(ID id, K entity) {
        T updatedEntity = mapper.toEntity(entity);
        updatedEntity = service.update(id, updatedEntity);
        K dto = mapper.toResource(updatedEntity);
        linkDTO(dto);
        return Response.status(Response.Status.OK)
                .location(UriBuilder.fromUri(location + updatedEntity.idToString()).build())
                .entity(dto).build();
    }

    /**
     * Delete an entity by id
     * @param id of requested entity
     * @return Response object
     */
    public Response delete(ID id) {
        service.deleteById(id);
        return Response.status(Response.Status.OK).build();
    }

    public LinkService getLinkService() {
        return linkService;
    }

    public S getService() {
        return service;
    }

    public M getMapper() {
        return mapper;
    }

    public String getLocation() {
        return location;
    }
}

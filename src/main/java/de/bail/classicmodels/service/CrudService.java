package de.bail.classicmodels.service;

import de.bail.classicmodels.model.enities.GenericEntity;
import de.bail.classicmodels.util.CustomInternalServerErrorException;
import de.bail.classicmodels.util.CustomNotFoundException;
import org.eclipse.microprofile.opentracing.Traced;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

/**
 * Abstract Crud Service
 * @param <T> Basic entity
 * @param <ID> Wrapper type of entity id
 */
@Traced
public abstract class CrudService<T extends GenericEntity, ID> {

  @PersistenceContext()
  protected EntityManager em;

  private final Class<T> type;

  /**
   * Default Constructor
   * Please use {@link #CrudService(Class)}
   */
  public CrudService() {
    throw new UnsupportedOperationException("The default constructor cannot be used for service classes");
  }

  /**
   * AbstractService Constructor
   * @param type The type of Entity is needed to secure the correct implementation of the JPA access methods
   */
  protected CrudService(Class<T> type) {
    this.type = type;
  }

  /**
   * Returns custom not found exception with graphql error code
   * @param id The id of requested entity
   * @return New custom NotFoundException
   */
  public CustomNotFoundException notFoundException(ID id) {
    return new CustomNotFoundException(String.format("%s with ID '%s' could not be found.", type.getSimpleName(), id));
  }

  /**
   * Returns custom internal server error exception with graphql error code
   * @return new custom InternalServerErrorException
   */
  public CustomInternalServerErrorException internalServerErrorException() {
    return new CustomInternalServerErrorException("Something went wrong while processing your request.");
  }

  /**
   * Save a new entity to the database
   * @param entity New entity
   * @return Persisted entity
   */
  public abstract T create(T entity);

  /**
   * Persist entity
   * @param entity Entity
   */
  private void persist(T entity) {
    em.persist(entity);
  }

  /**
   * Merge
   * @param entity Entity
   */
  private void merge(T entity) {
    em.merge(entity);
  }

  /**
   * Merge
   * @param id Entity id
   * @param entity Entity
   * @return Entity
   */
  @Transactional
  public T update(ID id, T entity) {
    merge(entity);
    return entity;
  }

  /**
   * Merge
   * @param entity Entity
   * @return Entity
   */
  @Transactional
  public T update(T entity) {
    merge(entity);
    return entity;
  }

  /**
   * Get entity by id
   * @param id Entity id
   * @return Entity
   */
  public T getEntityById(ID id) {
    T entity = em.find(type, id);
    if (entity != null) {
      return entity;
    }
    throw notFoundException(id);
  }

  /**
   * Has entity
   * @param id Entity id
   * @return true if entity was found
   */
  public boolean hasEntity(ID id) {
    return getEntityById(id) != null;
  }

  /**
   * Persist new entity
   * @param entity New entity
   * @return Persisted Entity
   */
  @Transactional
  public T save(@Valid T entity) {
    persist(entity);
    return entity;
  }

  /**
   * Delete by Entity
   * @param entity Entity to be deleted
   */
  @Transactional
  public void delete(T entity) {
    if (em.contains(entity)) {
      em.remove(entity);
      em.flush();
    } else {
      throw new EntityNotFoundException();
    }
  }

  /**
   * Delete by id
   * @param id Id of the entity to be deleted
   */
  @Transactional
  public void deleteById(ID id) {
    T entity = em.find(type, id);
    if (em.find(type, id) != null) {
      em.remove(entity);
      em.flush();
    } else {
      throw notFoundException(id);
    }
  }

  /**
   * Count all entities
   * @return Amount of records
   */
  public Integer count() {
    return ((Number) em.createNamedQuery(type.getSimpleName() + ".count")
            .getSingleResult())
            .intValue();
  }

  /**
   * Get all entities
   * @return List of entities
   */
  public List<T> getAllEntities() {
    return em.createNamedQuery(type.getSimpleName() + ".getAll").getResultList();
  }

  /**
   * Get all entities with pagination parameters
   * @param offset Starting position
   * @param limit The amount of entities
   * @return List of entities
   */
  public List<T> getAllEntitiesPagination(int offset, int limit) {
    return em.createNamedQuery(type.getSimpleName() + ".getAll")
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
  }
}

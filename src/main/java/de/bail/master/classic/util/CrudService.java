package de.bail.master.classic.util;

import de.bail.master.classic.model.enities.GenericEntity;
import org.eclipse.microprofile.opentracing.Traced;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.util.List;

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
   * @param type of Entity is needed to secure the correct implementation of the JPA access methods
   */
  protected CrudService(Class<T> type) {
    this.type = type;
  }

  /**
   * Returns custom not found exception with graphql error code
   * @param id of requested entity
   * @return new custom NotFoundException
   */
  public CustomNotFoundException notFoundException(ID id) {
    return new CustomNotFoundException(String.format("%s with ID %s could not be found.", type.getSimpleName(), id));
  }

  /**
   * Returns custom internal server error exception with graphql error code
   * @return new custom InternalServerErrorException
   */
  public CustomInternalServerErrorException internalServerErrorException() {
    return new CustomInternalServerErrorException("Something went wrong while processing your request.");
  }

  public abstract T create(T entity);

  private void persist(T entity) {
    em.persist(entity);
  }

  private void merge(T entity) {
    em.merge(entity);
  }

  @Transactional
  public T update(T entity) {
    merge(entity);
    return entity;
  }

  public T getEntityById(ID id) {
    T entity = em.find(type, id);
    if (entity != null) {
      return entity;
    }
    throw notFoundException(id);
  }

  @Transactional
  public T save(@Valid T entity) {
    persist(entity);
    return entity;
  }

  @Transactional
  public void delete(T entity) {
    if (em.contains(entity)) {
      em.remove(entity);
      em.flush();
    } else {
      throw new EntityNotFoundException();
    }
  }

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

  public Integer count() {
    return ((Number) em.createNamedQuery(type.getSimpleName() + ".count")
            .getSingleResult())
            .intValue();
  }

  public List<T> getAllEntities() {
    return em.createNamedQuery(type.getSimpleName() + ".getAll").getResultList();
  }

  public List<T> getAllEntitiesPagination(int offset, int limit) {
    return em.createNamedQuery(type.getSimpleName() + ".getAll")
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
  }
}

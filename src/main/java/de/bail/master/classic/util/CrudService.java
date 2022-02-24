package de.bail.master.classic.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;


public abstract class CrudService<T extends GenericEntity> {

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

  public abstract T create(T entity);

  private void persist(T entity) {
    em.persist(entity);
  }

  private void merge(T entity) {
    em.merge(entity);
  }

  @Transactional
  public T update(T entity) {
    if (entity.getId() != null && em.find(type, entity.getId()) != null) {
      merge(entity);
    } else {
      throw new EntityNotFoundException();
    }
    return entity;
  }

  public T getEntityById(Integer id) {
    T entity = em.find(type, id);
    if (entity != null) {
      return entity;
    } else {
      throw new EntityNotFoundException(String.format("Couldn't find %s with id %s", type.getSimpleName(), id));
    }
  }

  @Transactional
  public T save(@Valid T entity) {
    if (entity.getId() != null) {
      merge(entity);
    } else {
      persist(entity);
    }
    return entity;
  }

  @Transactional
  public void delete(T entity) {
    if (em.contains(entity)) {
      em.remove(entity);
      em.flush();
    } else {
      throw new EntityNotFoundException(String.format("Couldn't find %s with id %s", type.getSimpleName(), entity.getId()));
    }
  }

  @Transactional
  public void deleteById(Integer id) {
    T entity = em.find(type, id);
    if (em.find(type, id) != null) {
      em.remove(entity);
      em.flush();
    } else {
      throw new EntityNotFoundException(String.format("Couldn't find %s with id %s", type.getSimpleName(), id));
    }
  }

  public Integer count() {
    return ((Number) em.createNamedQuery(type.getSimpleName() + ".count").getSingleResult())
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

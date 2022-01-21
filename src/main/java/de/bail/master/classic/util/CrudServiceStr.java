package de.bail.master.classic.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

/**
 * Abstract super class for all services to provide methods for saving reading and deleting
 * @param <T>  Datatype of entity
 * @param <ID> Datatype of entity id
 */
public abstract class CrudServiceStr<T extends GenericEntityStr, ID> {

  @PersistenceContext()
  protected EntityManager em;
  private final Class<T> type;

  /**
   * Default Constructor
   * Please use {@link #CrudServiceStr(Class)}
   */
  public CrudServiceStr() {
    throw new UnsupportedOperationException("The default constructor cannot be used for service classes");
  }

  /**
   * AbstractService Constructor
   * @param type of Entity is needed to secure the correct implementation of the JPA access methods
   */
  protected CrudServiceStr(Class<T> type) {
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

  public T getEntityById(ID id) {
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
  public void deleteById(ID id) {
    T entity = em.find(type, id);
    if (em.find(type, id) != null) {
      em.remove(entity);
      em.flush();
    } else {
      throw new EntityNotFoundException(String.format("Couldn't find %s with id %s", type.getSimpleName(), id));
    }
  }

//  @Transactional
//  public List<T> getPage(Integer limit, Integer offset) {
//    return em.createNamedQuery(type.getSimpleName() + ".getAll").setMaxResults(limit)
//        .setFirstResult(offset).getResultList();
//  }

  public Integer count() {
    return ((Number) em.createNamedQuery(type.getSimpleName() + ".count").getSingleResult())
        .intValue();
  }

  public List<T> getAllEntities() {
    return em.createNamedQuery(type.getSimpleName() + ".getAll").getResultList();
  }
}

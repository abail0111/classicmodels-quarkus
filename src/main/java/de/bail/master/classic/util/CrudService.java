package de.bail.master.classic.util;

import de.bail.master.classic.model.enities.GenericEntity;
import org.eclipse.microprofile.opentracing.Traced;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
    return em.find(type, id);
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
      throw new EntityNotFoundException(String.format("Couldn't find %s with id %s", type.getSimpleName(), id));
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

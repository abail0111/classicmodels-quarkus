package de.bail.classicmodels.model.mapper;

import java.util.List;

public interface GenericMapper<T, K> {

    K toResource(T entity);

    List<K> toResourceList(List<T> list);

    T toEntity(K entity);

    List<T> toEntityList(List<K> list);

}

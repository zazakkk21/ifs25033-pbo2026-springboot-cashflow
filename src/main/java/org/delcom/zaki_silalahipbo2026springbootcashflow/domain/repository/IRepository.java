package org.delcom.zaki_silalahipbo2026springbootcashflow.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface IRepository<T, ID> {
  List<T> findAll();
  Optional<T> findById(ID id);
  boolean deleteById(ID id);
  void update(T entity);
  T save(T entity);
  List<T> findBy(Predicate<T> predicate);
  
}
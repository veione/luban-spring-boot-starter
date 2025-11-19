package com.think.luban.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Table repository interface, for subclass table repository proxy.
 *
 * @param <T>
 * @param <Serializable>
 * @author veione
 */
public interface CfgRepository<T, Serializable> {

    T findById(Serializable id);

    Optional<T> findById(Predicate<T> predicate);

    List<T> getList(Predicate<T> predicate);

    List<T> getList();

    long count(Predicate<T> predicate);

    boolean contains(Serializable id);

    boolean contains(Predicate<T> predicate);

    int len();
}
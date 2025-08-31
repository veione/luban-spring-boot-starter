package cfg;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface CfgRepository<T, Serializable> {
    T findById(Serializable id);

    Optional<T> findById(Predicate<T> predicate);

    List<T> findAll(Predicate<T> predicate);

    List<T> findAll();

    long count(Predicate<T> predicate);

    boolean exists(Serializable id);

    boolean exists(Predicate<T> predicate);

    List<T> findIndexes(String indexName, Serializable indexValue);

    List<T> findIndex(Function<String, String> indexName, Serializable indexValue);
}

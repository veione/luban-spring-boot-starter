package com.think.luban.repository;

import com.think.luban.manager.TableManager;
import com.think.luban.LuBanTableProperties;
import com.think.luban.Reloadable;
import com.think.luban.TableDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractTableRepositoryInvocationHandler<T> implements CfgRepository<T, Serializable>, InvocationHandler, Reloadable {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final TableManager tableManager;
    protected final Class<T> clazz;
    protected final Class<T> tableClazz;
    protected final Map<Serializable, T> items = new HashMap<>(64);
    protected final TableDefinition definition;
    protected final LuBanTableProperties tableProperties;

    public AbstractTableRepositoryInvocationHandler(ApplicationContext applicationContext, Class<T> clazz) {
        this.tableManager = applicationContext.getBean(TableManager.class);
        this.tableProperties = applicationContext.getBean(LuBanTableProperties.class);
        this.clazz = clazz;
        this.tableClazz = getCfgBeanType(clazz);
        this.definition = new TableDefinition(tableClazz, clazz);
        this.tableManager.register(definition, this);
        this.init();
    }

    private void init() {
        reload();
    }

    public abstract void reload();

    private <T> Class<T> getCfgBeanType(Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericInterfaces()[0]; // Assuming the first interface is the one we want

        if (!(genericSuperclass instanceof ParameterizedType parameterizedType)) {
            throw new IllegalArgumentException("Supertype is not parameterized");
        }

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

        if (actualTypeArguments.length == 0) {
            throw new IllegalStateException("No generic types found");
        }

        Type beanType = actualTypeArguments[0];

        if (!(beanType instanceof Class)) {
            throw new IllegalArgumentException("First generic type is not a class");
        }

        return (Class<T>) beanType;
    }

    @Override
    public T findById(Serializable id) {
        return items.get(id);
    }

    @Override
    public Optional<T> findById(Predicate<T> predicate) {
        return items.values().stream().filter(predicate).findFirst();
    }

    @Override
    public List<T> findAll(Predicate<T> predicate) {
        return items.values().stream().filter(predicate).toList();
    }

    @Override
    public List<T> findAll() {
        return items.values().stream().toList();
    }

    @Override
    public long count(Predicate<T> predicate) {
        return items.values().stream().filter(predicate).count();
    }

    @Override
    public boolean exists(Serializable id) {
        return items.containsKey(id);
    }

    @Override
    public boolean exists(Predicate<T> predicate) {
        return items.values().stream().anyMatch(predicate);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Object 方法，走原生方法,比如hashCode()
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        // 其它走本地代理
        return method.invoke(this, args);
    }
}


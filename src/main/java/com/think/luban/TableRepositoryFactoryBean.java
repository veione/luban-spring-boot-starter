package com.think.luban;

import com.think.luban.repository.ByteTableRepositoryInvocationHandler;
import com.think.luban.repository.JsonTableRepositoryInvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;

/**
 * Table repository factory bean.
 *
 * @param <T>
 * @author veione
 */
public class TableRepositoryFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {
    private final Class<T> interfaceType;
    private ApplicationContext applicationContext;

    public TableRepositoryFactoryBean(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    public T getObject() throws Exception {
        // 因为DefaultCfgRepositoryInvocationHandler需要Class<T>作为参数,所以该类包含一个Class<T>的成员,通过构造函数初始化
        LuBanTableProperties props = applicationContext.getBean(LuBanTableProperties.class);
        return switch (props.getType()) {
            case JSON -> (T) Proxy.newProxyInstance(
                    interfaceType.getClassLoader(),
                    new Class[]{interfaceType},
                    new JsonTableRepositoryInvocationHandler<>(applicationContext, interfaceType));
            case BYTE -> (T) Proxy.newProxyInstance(
                    interfaceType.getClassLoader(),
                    new Class[]{interfaceType},
                    new ByteTableRepositoryInvocationHandler<>(applicationContext, interfaceType));
        };
    }

    @Override
    public Class<?> getObjectType() {
        // 该方法返回的getObject()方法返回对象的类型，这里是基于interfaceType生成的代理对象,所以类型就是interfaceType
        return interfaceType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

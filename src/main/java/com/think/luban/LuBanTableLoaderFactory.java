package com.think.luban;

import com.think.luban.loader.ITableLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置表加载器工厂
 *
 * @author veione
 * @version 1.0
 */
public class LuBanTableLoaderFactory implements InitializingBean, ApplicationContextAware {
    private final Map<LubanTableProperties.TableType, ITableLoader<?>> loaderMap = new HashMap<>(4);
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, ITableLoader> beans = applicationContext.getBeansOfType(ITableLoader.class);
        for (ITableLoader<?> loader : beans.values()) {
            loaderMap.put(loader.getLoaderType(), loader);
        }
    }

    public ITableLoader getTableLoader(LubanTableProperties.TableType type) {
        return loaderMap.get(type);
    }
}

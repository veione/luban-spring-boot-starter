package com.think.luban.repository;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.think.luban.loader.ITableLoader;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Default table repository proxy handler.
 *
 * @author veione
 */
public class JsonTableRepositoryInvocationHandler<T> extends AbstractTableRepositoryInvocationHandler<T> {

    public JsonTableRepositoryInvocationHandler(ApplicationContext applicationContext, Class<T> clazz) {
        super(applicationContext, clazz);
    }

    @Override
    public synchronized void reload() {
        // 先清理之前的资源
        items.clear();
        ITableLoader loader = tables.getLoader();
        String tableFileName = definition.getTableFileName();

        try {
            JsonElement buf = (JsonElement) loader.load(tableFileName);
            Method method = tableClazz.getMethod("deserialize", JsonObject.class);
            for (JsonElement ele : buf.getAsJsonArray()) {
                Object item = method.invoke(null, ele.getAsJsonObject());
                Serializable id = definition.getIdValue(item);
                if (items.containsKey(id)) {
                    throw new IllegalArgumentException(String.format("Table %s id %s duplicated", tableFileName, id));
                }
                items.put(id, (T) item);
            }
        } catch (Exception e) {
            logger.error("配置表读取失败 {} :(", tableFileName, e);
        }
    }
}

package com.think.luban.repository;

import com.think.luban.loader.ITableLoader;
import luban.ByteBuf;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Default table repository proxy handler.
 *
 * @param <T>
 * @author veione
 */
public class ByteTableRepositoryInvocationHandler<T> extends AbstractTableRepositoryInvocationHandler<T> {

    public ByteTableRepositoryInvocationHandler(ApplicationContext applicationContext, Class<T> clazz) {
        super(applicationContext, clazz);
    }

    @Override
    public synchronized void reload() {
        // 先清理之前的资源
        items.clear();
        ITableLoader loader = tables.getLoader();
        String tableFileName = definition.getTableFileName();

        try {
            ByteBuf buf = (ByteBuf) loader.load(tableFileName);
            Method method = tableClazz.getMethod("deserialize", ByteBuf.class);

            for (int n = buf.readSize(); n > 0; --n) {
                Object item = method.invoke(null, buf);
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

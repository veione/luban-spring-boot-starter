package com.think.luban.loader;

import com.think.luban.LubanTableProperties;

/**
 * 表加载器接口
 *
 * @author veione
 * @version 1.0
 */
public interface ITableLoader<T> {

    T load(String file);

    String getPath();

    void setPath(String path);

    LubanTableProperties.TableType getLoaderType();
}

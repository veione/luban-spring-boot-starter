package com.think.luban.loader;

import com.think.luban.LuBanTableProperties;

import java.io.IOException;

/**
 * 表加载器接口
 *
 * @author veione
 * @version 1.0
 */
public interface ITableLoader {

    Object load(String file) throws IOException;

    String getPath();

    void setPath(String path);

    LuBanTableProperties.TableType getLoaderType();
}

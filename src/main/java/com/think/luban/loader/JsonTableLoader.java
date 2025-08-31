package com.think.luban.loader;

import com.google.gson.JsonElement;
import com.think.luban.LubanTableProperties;

/**
 * JSON 表加载器
 *
 * @author veione
 * @version 1.0
 */
public class JsonTableLoader implements ITableLoader<JsonElement> {
    private String path;

    @Override
    public JsonElement load(String file) {
        return null;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public LubanTableProperties.TableType getLoaderType() {
        return LubanTableProperties.TableType.JSON;
    }
}

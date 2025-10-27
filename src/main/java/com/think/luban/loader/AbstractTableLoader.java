package com.think.luban.loader;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

public abstract class AbstractTableLoader implements ITableLoader {
    protected String path;
    protected final ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }
}

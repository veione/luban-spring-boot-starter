package com.think.luban.loader;

public abstract class AbstractTableLoader implements ITableLoader {
    protected String path;

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }
}

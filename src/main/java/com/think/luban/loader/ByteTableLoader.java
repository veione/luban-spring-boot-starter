package com.think.luban.loader;

import com.think.luban.ByteBuf;
import com.think.luban.LubanTableProperties;

/**
 * 二进制流表加载器
 *
 * @author veione
 * @version 1.0
 */
public class ByteTableLoader implements ITableLoader<ByteBuf> {
    private String path;

    @Override
    public ByteBuf load(String file) {
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
        return LubanTableProperties.TableType.BYTE;
    }
}

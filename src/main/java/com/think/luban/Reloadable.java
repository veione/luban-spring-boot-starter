package com.think.luban;

/**
 * 可重新加载接口
 *
 * @author veione
 */
public interface Reloadable {
    /**
     * 重新加载
     */
    default void reload() {
    }
}

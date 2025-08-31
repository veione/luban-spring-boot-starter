package com.think.luban;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * luban table properties
 *
 * @author veione
 * @version 1.0
 */
@Data
@ConfigurationProperties("luban")
public class LubanTableProperties {
    /**
     * 加载路径
     */
    private String path;
    /**
     * 配置表检查间隔时间(s)
     */
    private int interval = 5;
    /**
     * 文件类型
     */
    private TableType type;

    public enum TableType {
        /**
         * json
         */
        JSON,
        /**
         * byte
         */
        BYTE
    }
}

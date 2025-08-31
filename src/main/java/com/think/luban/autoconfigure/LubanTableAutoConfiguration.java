package com.think.luban.autoconfigure;

import com.think.luban.LuBanTableLoaderFactory;
import com.think.luban.LubanTableProperties;
import com.think.luban.TableScanImportBeanDefinitionRegister;
import com.think.luban.Tables;
import com.think.luban.loader.ITableLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 配置自动装配
 *
 * @author veione
 * @version 1.0
 */
@RequiredArgsConstructor
@AutoConfiguration
@EnableConfigurationProperties(LubanTableProperties.class)
@Import(TableScanImportBeanDefinitionRegister.class)
public class LubanTableAutoConfiguration {
    private final LubanTableProperties properties;

    @Bean
    public LuBanTableLoaderFactory luBanTableLoaderFactory() {
        return new LuBanTableLoaderFactory();
    }

    @Bean
    public Tables tables(LuBanTableLoaderFactory loaderFactory) {
        ITableLoader<?> tableLoader = loaderFactory.getTableLoader(properties.getType());
        tableLoader.setPath(properties.getPath());
        return new Tables(tableLoader);
    }
}

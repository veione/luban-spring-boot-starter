package com.think.luban.regiser;

import com.think.luban.scanner.TableClassPathBeanDefinitionScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Table scanner for bean definition register.
 *
 * @author veione
 */
public class TableScanImportBeanDefinitionRegister implements BeanDefinitionRegistryPostProcessor, BeanFactoryAware, EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(TableScanImportBeanDefinitionRegister.class);
    private BeanFactory beanFactory;
    private Environment environment;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 优先使用自定义包路径
        List<String> packagesToScan = getScanPackages();

        if (packagesToScan.isEmpty()) {
            logger.debug("No packages to scan, automatic excel table scanning disabled.");
            return;
        }

        logger.debug("Searching for repository annotated with @TableRepository");

        if (logger.isDebugEnabled()) {
            packagesToScan.forEach(pkg -> logger.debug("Using base package '{}'", pkg));
        }

        // 使用自定义扫描器扫描
        TableClassPathBeanDefinitionScanner scanner = new TableClassPathBeanDefinitionScanner(registry, false);
        scanner.doScan(StringUtils.toStringArray(packagesToScan));
    }

    private List<String> getScanPackages() {
        // 如果有自定义包，则使用自定义包
        if (environment != null) {
            try {
                List<String> customPackages = Binder.get(environment)
                        .bind("luban.scan-packages", Bindable.listOf(String.class))
                        .orElse(Collections.emptyList());

                if (!customPackages.isEmpty()) {
                    return customPackages;
                }
            } catch (Exception e) {
                logger.warn("Failed to bind luban.table.base-packages", e);
            }
        }

        // 否则使用自动配置包
        if (AutoConfigurationPackages.has(this.beanFactory)) {
            return AutoConfigurationPackages.get(this.beanFactory);
        }

        return Collections.emptyList();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

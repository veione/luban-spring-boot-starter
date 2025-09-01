package com.think.luban;

import cfg.Tables;
import com.think.luban.repository.CfgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class LuBanTableListener implements ApplicationListener<ContextRefreshedEvent> {
    private final LuBanTableProperties properties;
    private final Tables tables;
    private FileAlterationObserver fileAlterationObserver;
    private FileAlterationMonitor fileAlterationMonitor;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.startMonitor();
        log.info("Luban table loaded success :)");
    }

    /**
     * 开启文件监听
     */
    private void startMonitor() {
        // 轮询间隔 5 秒
        long interval = TimeUnit.SECONDS.toMillis(properties.getInterval());

        try {
            String path = new ClassPathResource(properties.getPath()).getFile().getAbsolutePath();
            // 创建一个文件观察器用于处理文件的格式
            fileAlterationObserver = new FileAlterationObserver(path,
                    FileFilterUtils.and(FileFilterUtils.fileFileFilter())
                            .or(FileFilterUtils.suffixFileFilter("json"))
                            .or(FileFilterUtils.suffixFileFilter("bytes")));
            //设置文件变化监听器
            fileAlterationObserver.addListener(new FileAlterationListenerAdaptor() {
                @Override
                public void onFileChange(File file) {
                    String fileName = file.getName();
                    String fileBaseName = FilenameUtils.getBaseName(fileName);
                    Class<?> clazz = tables.getTableClass(fileBaseName);
                    if (clazz == null) {
                        log.warn("Table file reload fail, table info not exist -> {}", fileBaseName);
                        return;
                    }
                    CfgRepository<?, Serializable> repository = tables.getRepository(clazz);
                    if (repository == null) {
                        log.warn("Table file reload fail, Repository not exist -> {}", fileBaseName);
                        return;
                    }
                    Reloadable reloadable = (Reloadable) repository;
                    reloadable.reload();
                    log.info("Table resource {} reload success.", fileName);
                }
            });
            fileAlterationMonitor = new FileAlterationMonitor(interval, fileAlterationObserver);
            fileAlterationMonitor.start();
            log.info("Table hotswap monitor stared :) interval(s): {}, path: {}", interval, path);
        } catch (Exception e) {
            log.error("Start table hotswap monitor failed", e);
        }
    }

    @EventListener
    public void onContextClosedEvent(ContextClosedEvent event) {
        if (fileAlterationMonitor != null) {
            try {
                fileAlterationMonitor.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (fileAlterationObserver != null) {
            try {
                fileAlterationObserver.destroy();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

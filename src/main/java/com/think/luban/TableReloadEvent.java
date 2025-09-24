package com.think.luban;

import com.think.luban.repository.CfgRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

import java.io.Serializable;

public class TableReloadEvent extends ApplicationContextEvent {
    private final String fileName;
    private final Class<?> clazz;
    private final CfgRepository<?, Serializable> repository;

    public TableReloadEvent(ApplicationContext source, String fileName, Class<?> clazz, CfgRepository<?, Serializable> repository) {
        super(source);
        this.fileName = fileName;
        this.clazz = clazz;
        this.repository = repository;
    }

    public String getFileName() {
        return fileName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public CfgRepository<?, Serializable> getRepository() {
        return repository;
    }
}

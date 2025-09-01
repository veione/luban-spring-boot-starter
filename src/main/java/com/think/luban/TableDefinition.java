package com.think.luban;

import com.think.luban.anno.TableRepository;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Table definition
 *
 * @author veione
 */
public final class TableDefinition {
    private final Class<?> clazz;
    private final String tableFileName;
    private final List<Field> indexesFields = new ArrayList<>(4);
    private Field idField;
    private final Class<?> repositoryClass;

    public TableDefinition(Class<?> clazz, Class<?> repositoryClass) {
        this.clazz = clazz;
        this.repositoryClass = repositoryClass;
        TableRepository anno = repositoryClass.getAnnotation(TableRepository.class);
        this.tableFileName = anno.file();
        this.parseIndexes(anno);
    }

    /**
     * 解析索引
     */
    private void parseIndexes(TableRepository anno) {
        String idName = anno.id();
        if (idName != null && !idName.isEmpty()) {
            idField = FieldUtils.getField(clazz, idName, true);
        } else {
            idField = FieldUtils.getField(clazz, "id", true);
        }
        idField.setAccessible(true);

        /*Field[] fields = FieldUtils.getAllFields(clazz);
        for (Field field : fields) {
            if (field.isAnnotationPresent(Indexes.class)) {
                indexesFields.add(field);
            }
        }*/
    }

    public String getTableFileName() {
        return tableFileName;
    }

    public List<Field> getIndexesFields() {
        return indexesFields;
    }

    /**
     * 获取主键ID值
     *
     * @param item
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public <T> Serializable getIdValue(T item) throws IllegalAccessException {
        return idField.getInt(item);
    }

    public Class<?> getClazz() {
        return clazz;
    }
}

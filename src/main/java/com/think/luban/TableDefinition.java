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
        if (idField == null) {
            throw new IllegalStateException("ID field not found");
        }

        Class<?> fieldType = idField.getType();
        Object value = idField.get(item);

        if (value == null) {
            return null;
        }

        // 根据字段类型返回相应的值
        if (fieldType == int.class || fieldType == Integer.class) {
            return (Integer) value;
        } else if (fieldType == long.class || fieldType == Long.class) {
            return (Long) value;
        } else if (fieldType == String.class) {
            return (String) value;
        } else if (fieldType == short.class || fieldType == Short.class) {
            return (Short) value;
        } else if (fieldType == byte.class || fieldType == Byte.class) {
            return (Byte) value;
        } else {
            // 对于其他类型，直接返回其toString()表示
            return (Serializable) value;
        }
    }

    public Class<?> getClazz() {
        return clazz;
    }
}

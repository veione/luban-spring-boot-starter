package com.think.luban.loader;

import com.think.luban.LuBanTableProperties;
import lombok.extern.slf4j.Slf4j;
import luban.ByteBuf;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 二进制流表加载器
 *
 * @author veione
 * @version 1.0
 */
@Slf4j
@Component
public class ByteTableLoader extends AbstractTableLoader {

    @Override
    public ByteBuf load(String file) throws IOException {
        String fileName = String.format("%s%s%s.bytes", path, File.separator, file);
        Resource resource = resourceLoader.getResource(fileName);
        if (resource.exists()) {
            try (InputStream inputStream = resource.getInputStream()) {
                byte[] data = IOUtils.toByteArray(inputStream);
                return new ByteBuf(data);
            }
        }
        throw new IOException("Table file " + file + " not found");
    }

    @Override
    public LuBanTableProperties.TableType getLoaderType() {
        return LuBanTableProperties.TableType.BYTE;
    }
}

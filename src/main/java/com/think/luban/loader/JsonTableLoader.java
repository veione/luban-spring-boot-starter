package com.think.luban.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.think.luban.LuBanTableProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * JSON 表加载器
 *
 * @author veione
 * @version 1.0
 */
@Slf4j
@Component
public class JsonTableLoader extends AbstractTableLoader {

    @Override
    public JsonElement load(String file) throws IOException {
        String fileName = String.format("%s%s%s.json", path, File.separator, file);
        ClassPathResource resource = new ClassPathResource(fileName);
        if (resource.exists()) {
            try (InputStream inputStream = resource.getInputStream()) {
                String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                return JsonParser.parseString(content);
            }
        }
        throw new IOException("Table file " + file + " not found");
    }

    @Override
    public LuBanTableProperties.TableType getLoaderType() {
        return LuBanTableProperties.TableType.JSON;
    }
}

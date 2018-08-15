package org.litespring.core.io;

import org.litespring.utils.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 通过 ClassLoader 读取配置文件的 Resource
 */
public class ClassPathResource implements Resource {
    private String path;
    private ClassLoader classLoader;
    public ClassPathResource(String path) {
        this(path,null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = (classLoader != null?classLoader: ClassUtils.getDefaultClassLoader());
    }

    public InputStream getInputStream() throws IOException {
        InputStream is = this.classLoader.getResourceAsStream(this.path);

        if (is == null){
            throw new FileNotFoundException(path + "can not find");
        }
        return is;
    }
    public String getDescription(){
        return this.path;
    }
}

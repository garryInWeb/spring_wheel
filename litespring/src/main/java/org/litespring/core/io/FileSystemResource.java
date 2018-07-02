package org.litespring.core.io;

import org.litespring.utils.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhengtengfei on 2018/6/28.
 */
public class FileSystemResource implements Resource {
    private final String path;
    private final File file;

    public FileSystemResource(String path) {
        Assert.notNull(path,"Path must not be null.");
        this.path = path;
        file = new File(path);
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    public String getDescription() {
        return "file["+this.file.getAbsolutePath()+"]";
    }
}

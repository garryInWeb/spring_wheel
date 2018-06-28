package org.litespring.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhengtengfei on 2018/6/27.
 */
public interface Resource {
    public InputStream getInputStream() throws IOException;
    public String getDescription();
}

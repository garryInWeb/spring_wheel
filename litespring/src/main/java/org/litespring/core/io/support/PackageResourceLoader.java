package org.litespring.core.io.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.core.io.FileSystemResource;
import org.litespring.core.io.Resource;
import org.litespring.utils.Assert;
import org.litespring.utils.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 加载包下所有文件为Resource
 */
public class PackageResourceLoader {

    private static final Log logger = LogFactory.getLog(PackageResourceLoader.class);

    private final ClassLoader classLoader;

    public PackageResourceLoader() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    public PackageResourceLoader(ClassLoader classLoader) {
        Assert.notNull(classLoader,"ResourceLoader must not be null!");
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * 获取包名下所有文件
     * @param basePackage 包名
     * @return
     * @throws IOException
     */
    public Resource[] getResources(String basePackage) throws IOException {
        Assert.notNull(basePackage,"basePackage must not be null!");
        // 包名转换为相对路径
        String location = ClassUtils.convertClassNameToResourcePath(basePackage);
        ClassLoader cl = getClassLoader();
        // 获取包名根目录File对象
        URL url = cl.getResource(location);
        File rootDir = new File(url.getFile());

        Set<File> matchingFiles = retrieveMatchingFiles(rootDir);
        Resource[] result = new Resource[matchingFiles.size()];
        int i = 0;
        for (File file : matchingFiles){
            result[i++] = new FileSystemResource(file);
        }
        return result;
    }

    /**
     * @param rootDir
     * @return
     */
    private Set<File> retrieveMatchingFiles(File rootDir) {
        if (!rootDir.exists()){
            if (logger.isDebugEnabled()){
                logger.debug("Skipping [" + rootDir.getAbsolutePath() + "] because it does not exist");
            }
            return Collections.emptySet();
        }
        if (!rootDir.isDirectory()){
            if (logger.isDebugEnabled()){
                logger.debug("Skippring [" + rootDir.getAbsolutePath() + "] because it does not denote a directory");
            }
            return Collections.emptySet();
        }
        if (!rootDir.canRead()){
            if (logger.isDebugEnabled()){
                logger.debug("Cannot search for matching files underneath directory ["
                        + rootDir.getAbsolutePath() + "] because the application is not allowed to read the directory");
            }
            return Collections.emptySet();
        }
        Set<File> result = new LinkedHashSet<>(8);
        doRetrieveMatchingFiles(rootDir,result);
        return result;
    }

    private void doRetrieveMatchingFiles(File rootDir, Set<File> result) {
        File[] dirContents = rootDir.listFiles();
        if (dirContents == null){
            if (logger.isWarnEnabled()){
                logger.warn("Cloud not retrieve contents of directory [" + rootDir.getAbsolutePath() + "]");
            }
            return;
        }
        for (File file : dirContents){
            if (file.isDirectory()){
                if (!file.canRead()){
                    if (logger.isDebugEnabled()){
                        logger.debug("Cannot search for matching files underneath directory ["
                                + rootDir.getAbsolutePath() + "] because the application is not allowed to read the directory");
                    }
                }
                else{
                    doRetrieveMatchingFiles(file,result);
                }
            }
            else{
                result.add(file);
            }
        }
    }
}

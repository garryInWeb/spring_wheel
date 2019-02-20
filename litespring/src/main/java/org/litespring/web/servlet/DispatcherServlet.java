package org.litespring.web.servlet;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanNameGenerator;
import org.litespring.context.annotation.AnnotationBeanNameGenerator;
import org.litespring.context.annotation.ScannedGenericBeanDefinition;
import org.litespring.core.io.Resource;
import org.litespring.core.io.support.PackageResourceLoader;
import org.litespring.core.type.classreading.MetadataReader;
import org.litespring.core.type.classreading.SimpleMetadataReader;
import org.litespring.stereotype.Controller;
import org.litespring.stereotype.RequestMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by zhengtengfei on 2018/10/15.
 */
public class DispatcherServlet extends HttpServlet {


    private Properties properties = new Properties();

    private PackageResourceLoader resourceLoader = new PackageResourceLoader();

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    private List<BeanDefinition> controllerBeanDefinitions = new ArrayList<>();

    private Map<String,Object> controllers = new HashMap<String,Object> ();

    private Map<String,Object> handleControllers = new HashMap<>();

    private Map<String,Method> handleMethods = new HashMap<>();
    @Override
    public void init(ServletConfig config) throws ServletException {

        doLoadConfig(config.getInitParameter("contextConfig"));

        doScanner(properties.getProperty("scanPackage"));

        doInstace();

//        doAutowired();

        initHandlerMapping();

        System.out.println("加载完成");
    }

    private void initHandlerMapping() {

        if (controllers.isEmpty()){
            return ;
        }
        for (Map.Entry<String,Object> entry : controllers.entrySet()){
            Class<? extends Object> controllerClass = entry.getValue().getClass();

            String baseUrl = "";
            if (controllerClass.isAnnotationPresent(RequestMapping.class)){
                RequestMapping requestMappingAnnotation = controllerClass.getAnnotation(RequestMapping.class);
                baseUrl = requestMappingAnnotation.value();
            }
            Method[] methods = controllerClass.getMethods();
            for (Method method : methods){
                if (!method.isAnnotationPresent(RequestMapping.class)){
                    continue;
                }
                RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                String url = methodRequestMapping.value();

                url = (baseUrl + "/" + url).replaceAll("/+","/");

                handleControllers.put(url.toString(),entry.getValue());
                handleMethods.put(url.toString(),method);

            }
        }
    }

    private void doAutowired() {


    }

    private void doInstace() {
        if (controllerBeanDefinitions.isEmpty()){
            return ;
        }
        for (BeanDefinition bd : controllerBeanDefinitions){
            String beanClassName = bd.getBeanClassName();
            try {
                Class<?> clazz = Class.forName(beanClassName);
                controllers.put(bd.getId(),clazz.newInstance());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void doScanner(String scanPackage) {
        try {
            Resource[] resources = resourceLoader.getResources(scanPackage);

            for (Resource resource : resources) {
                try {
                    // ASM 解析
                    MetadataReader metadataReader = new SimpleMetadataReader(resource);
                    if (metadataReader.getAnnotationMetadata().hasAnnotation(Controller.class.getName())) {
                        // 注解的抽象类
                        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader.getAnnotationMetadata());
                        // 获取注解的value作为bean对象的id
                        String beanName = this.beanNameGenerator.generateBeanName(sbd);
                        sbd.setId(beanName);
                        controllerBeanDefinitions.add(sbd);
                    }
                }catch (Throwable ex){
                    throw new BeanDefinitionStoreException("Faild to read candidate component class:" + resource,ex);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doLoadConfig(String contextConfig) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(contextConfig);

        try{
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            doDispatch(req,resp);
        }catch (Exception e){
            resp.getWriter().write("500");
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (handleControllers.isEmpty()){
            return ;
        }
        String url = req.getRequestURI();
        String context = req.getContextPath();

        url = url.replace(context,"").replaceAll("/+","/");
        if (!this.handleControllers.containsKey(url)){
            resp.getWriter().write("404");
            return;
        }
        Method method = this.handleMethods.get(url);
        // 获取url请求参数
        Map<String,String[]> requestParam = req.getParameterMap();
        // 获取方法请求参数
        Class<?>[] methodParam = method.getParameterTypes();
        // 保存参数值
        Object[] paramsValue = new Object[methodParam.length];
        for (int i = 0; i < methodParam.length ; i++){
            String paramType = methodParam[i].getSimpleName();

            if (paramType.equals("HttpServletRequest")){
                 paramsValue[i] = req;
                 continue;
            }

            if (paramType.equals("HttpServletResponse")){
                paramsValue[i] = resp;
                continue;
            }
            if (paramType.equals("String")){

            }

        }


    }
}

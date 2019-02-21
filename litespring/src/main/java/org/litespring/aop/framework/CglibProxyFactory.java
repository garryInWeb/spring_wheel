package org.litespring.aop.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.aop.Advice;
import org.litespring.utils.Assert;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengtengfei on 2018/9/3.
 */
public class CglibProxyFactory implements AopProxyFactory {


    // Constants for CGLIB callback array indices
    private static final int AOP_PROXY = 0;
    private static final int INVOKE_TARGET = 1;
    private static final int NO_OVERRIDE = 2;
    private static final int DISPATCH_TARGET = 3;
    private static final int DISPATCH_ADVISED = 4;
    private static final int INVOKE_EQUALS = 5;
    private static final int INVOKE_HASHCODE = 6;

    private final static Log logger = LogFactory.getLog(CglibProxyFactory.class);

    protected final AopConfig config;


    public CglibProxyFactory(AopConfig config) {
        Assert.notNull(config,"AdvisedSupport must not be null");
        if (config.getAdvices().isEmpty()){
            throw new AopConfigExcetpion("No advisors and no TargetSource specified");
        }
        this.config = config;
    }

    @Override
    public Object getProxy() {
        return getProxy(null);
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        if (logger.isDebugEnabled()){
            logger.debug("Creating CGLIB proxy: target source is " + this.config.getTargetClass());
        }
        try{
            Class<?> rootClass = this.config.getTargetClass();

            Enhancer enhancer = new Enhancer();
            if (classLoader != null){
                enhancer.setClassLoader(classLoader);
            }
            enhancer.setSuperclass(rootClass);
            enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
            enhancer.setInterceptDuringConstruction(false);

            Callback[] callbacks = getCallbacks(rootClass);

            Class<?>[] types = new Class<?>[callbacks.length];
            for (int x = 0; x < callbacks.length; x++){
                types[x] = callbacks[x].getClass();
            }
            enhancer.setCallbackFilter(new ProxyCallbackFilter(this.config));
            enhancer.setCallbackTypes(types);
            enhancer.setCallbacks(callbacks);

            Object proxy = enhancer.create();

            return proxy;
        }
        catch (CodeGenerationException ex) {
            throw new AopConfigExcetpion("Could not generate CGLIB subclass of class [" +
                    this.config.getTargetClass() + "]: " +
                    "Common causes of this problem include using a final class or a non-visible class",
                    ex);
        }
        catch (IllegalArgumentException ex) {
            throw new AopConfigExcetpion("Could not generate CGLIB subclass of class [" +
                    this.config.getTargetClass() + "]: " +
                    "Common causes of this problem include using a final class or a non-visible class",
                    ex);
        }
        catch (Exception ex) {
            // TargetSource.getTarget() failed
            throw new AopConfigExcetpion("Unexpected AOP exception", ex);
        }
    }

    private Callback[] getCallbacks(Class<?> rootClass) {
        Callback aopInterceptor = new DynamicAdvisedInterceptor(this.config);

        Callback[] callbacks = new Callback[]{
                aopInterceptor
        };
        return callbacks;
    }

    private class DynamicAdvisedInterceptor implements MethodInterceptor {

        private final AopConfig aopConfig;

        public DynamicAdvisedInterceptor(AopConfig config) {
            this.aopConfig = config;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object target = this.aopConfig.getTargetObject();

            List<Advice> chain = this.aopConfig.getAdvices(method);

            Object retVal;
            if (chain == null || (chain.isEmpty() && Modifier.isPublic(method.getModifiers()))){
                retVal = methodProxy.invoke(target,args);
            }else{
                List<org.aopalliance.intercept.MethodInterceptor> interceptors =
                        new ArrayList<org.aopalliance.intercept.MethodInterceptor>();
                interceptors.addAll(chain);

                retVal = new ReflectiveMethodInvocation(target,method,args,interceptors).proceed();
            }
            return retVal;
        }
    }

    private class ProxyCallbackFilter implements CallbackFilter {

        private final AopConfig aopConfig;

        public ProxyCallbackFilter(AopConfig config) {
            this.aopConfig = config;
        }

        @Override
        public int accept(Method method) {
            return AOP_PROXY;
        }
    }
}

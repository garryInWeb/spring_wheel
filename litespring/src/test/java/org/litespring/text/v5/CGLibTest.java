package org.litespring.text.v5;

import org.junit.Test;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * Created by zhengtengfei on 2018/8/30.
 */
public class CGLibTest {
    @Test
    public void testCallBack(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PetStoreService.class);

        enhancer.setCallback(new TransactionInterceptor());
        PetStoreService petStoreService = (PetStoreService)enhancer.create();
        petStoreService.placeOrder();
    }
    public static class TransactionInterceptor implements MethodInterceptor{

        TransactionManager tx = new TransactionManager();
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            tx.start();
            Object result = methodProxy.invokeSuper(o,objects);
            tx.commit();

            return result;
        }
    }
    @Test
    public void testFilter(){
        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(PetStoreService.class);
        enhancer.setInterceptDuringConstruction(true);

        Callback[] callbacks = new Callback[]{new TransactionInterceptor(), NoOp.INSTANCE};

        Class<?>[] types = new Class<?>[callbacks.length];

        for (int i = 0; i < callbacks.length ; i ++){
            types[i] = callbacks[i].getClass();
        }

        enhancer.setCallbackFilter(new ProxyCallbackFilter());
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackTypes(types);

        PetStoreService petStoreService = (PetStoreService)enhancer.create();
        petStoreService.placeOrder();
        petStoreService.toString();
    }

    private class ProxyCallbackFilter implements CallbackFilter {

        ProxyCallbackFilter() {
        }

        @Override
        public int accept(Method method) {
            if (method.getName().startsWith("place")){
                return 1;
            }else{
                return 0;
            }
        }
    }
}

package netty.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public class ServiceCenter implements ApplicationContextAware {
    private ApplicationContext context;

    private static Map<String, Object> serviceMap = new HashMap<>();

    public static Object getService(String serviceName) {
        return serviceMap.get(serviceName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        Map<String, Object> serviceBeans = context.getBeansWithAnnotation(RpcService.class);
        for(Map.Entry entry : serviceBeans.entrySet()) {
            String interfaceName = entry.getValue().getClass().getAnnotation(RpcService.class).value().getName();
            System.out.println("interfaceName = " + interfaceName);
            System.out.println("entry.getValue() = " + entry.getValue());
            serviceMap.put(interfaceName, entry.getValue());
        }
    }
}

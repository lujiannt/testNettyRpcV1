package netty.invoke;

import netty.core.ServiceCenter;
import netty.model.RpcMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public abstract class AbstractInvoker {

    public static Object invokeRequest(RpcMessage rpcRequestMessage) {
        Object object = null;
        String className = rpcRequestMessage.getClassName();
        String methodName = rpcRequestMessage.getMethodName();
        Class<?>[] parameterTypes = rpcRequestMessage.getParameterTypes();
        Object[] parameters = rpcRequestMessage.getParameters();

        Object obj = ServiceCenter.getService(className);
        try {
            Method method = obj.getClass().getMethod(methodName, parameterTypes);
            object = method.invoke(obj, parameters);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }
}

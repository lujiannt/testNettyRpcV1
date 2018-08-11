package netty.model;

import java.io.Serializable;

/**
 * 请求消息结构体
 */
public class RpcMessage implements Serializable {
    /**
     * 消息类型: 普通文本消息
     */
    public static final int MESSAGE_TYPE_COMMON = 0;
    /**
     * 消息类型: 心跳消息
     */
    public static final int MESSAGE_TYPE_HEART = 1;
    /**
     * 消息类型：接口请求
     */
    public static final int MESSAGE_TYPE_REQUEST = 2;
    /**
     * 消息类型：接口响应
     */
    public static final int MESSAGE_TYPE_RESPONSE = 3;


    public RpcMessage() {
        super();
    }

    public RpcMessage(int type, String content) {
        this.type = type;
        this.content = content;
    }

    /**
     * TODO 预留字段
     * 消息id
     */
    private String messageId;
    /**
     * 消息类型
     */
    private int type;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 参数值
     */
    private Object[] parameters;
    /**
     * 异常信息
     */
    private String error;
    /**
     * 响应的结果
     */
    private Object result;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

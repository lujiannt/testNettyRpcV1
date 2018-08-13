package netty.test;

import netty.core.ServerNettyMapping;
import netty.model.RpcMessage;
import netty.test.server.DemoService;
import netty.test.server.DemoServiceImpl;

public class TestDemo {
    public static void main(String[] args) {
        DemoService demoService = new DemoServiceImpl();
        RpcMessage request = new RpcMessage(RpcMessage.MESSAGE_TYPE_REQUEST, "DemoService", "demo", new Class<?>[]{String.class}, new String[]{"demo"});

        String messageId = demoService.sendRequest("1", request);
        if (messageId != null) {
            RpcMessage response = ServerNettyMapping.registerListenerAndReturn(messageId);
            System.out.println(response.getResult().toString());
        }
    }
}


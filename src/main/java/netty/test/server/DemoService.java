package netty.test.server;

import netty.model.RpcMessage;

public interface DemoService {
    String sendRequest(String parkingId, RpcMessage rpcMessage);
}

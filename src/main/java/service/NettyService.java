package service;

import netty.model.RpcMessage;

public interface NettyService {
    //TODO -- test
    void bindPort(int port);
    //TODO -- test
    int getChannelSize();
    //TODO -- test
    void sendMessage(RpcMessage message);
}

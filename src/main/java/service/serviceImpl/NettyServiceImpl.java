package service.serviceImpl;

import io.netty.channel.Channel;
import netty.core.ServerNettyMapping;
import netty.model.RpcMessage;
import netty.server.RpcServer;
import service.NettyService;

import java.util.Iterator;

public class NettyServiceImpl implements NettyService {
    @Override
    public void bindPort(int port) {
        RpcServer server = new RpcServer(port);
        server.start();
    }

    @Override
    public int getChannelSize() {
        return ServerNettyMapping.group.size();
    }

    @Override
    public void sendMessage(RpcMessage message) {
        new Thread(() -> {
            Iterator<Channel> iterator = ServerNettyMapping.group.iterator();

            while (iterator.hasNext()) {
                Channel ch = iterator.next();
                ch.writeAndFlush(message);
                System.out.println("send Success");
            }
        }).start();
    }
}

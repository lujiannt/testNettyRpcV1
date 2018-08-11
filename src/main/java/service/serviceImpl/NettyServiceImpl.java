package service.serviceImpl;

import io.netty.channel.Channel;
import netty.core.NettyMapping;
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
        return NettyMapping.group.size();
    }

    @Override
    public void sendMessage(String message) {
        new Thread(() -> {
            Iterator<Channel> iterator = NettyMapping.group.iterator();

            while (iterator.hasNext()) {
                Channel ch = iterator.next();
                ch.writeAndFlush(message);
                System.out.println("send Success");
            }
        }).start();
    }
}

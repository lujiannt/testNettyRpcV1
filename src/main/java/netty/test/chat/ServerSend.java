package netty.test.chat;

import io.netty.channel.Channel;
import netty.core.ServerNettyMapping;
import netty.model.RpcMessage;
import netty.server.RpcServer;
import service.NettyService;
import service.serviceImpl.NettyServiceImpl;

import java.util.Iterator;
import java.util.Scanner;

public class ServerSend {

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            Scanner input = new Scanner(System.in);
            String infoString = "";
            while (true) {
                while (ServerNettyMapping.group.size() > 0) {
                    System.out.println("client connected!");
                    infoString = input.nextLine();

                    Iterator<Channel> iterator = ServerNettyMapping.group.iterator();

                    while (iterator.hasNext()) {
                        Channel ch = iterator.next();
                        RpcMessage message = new RpcMessage(RpcMessage.MESSAGE_TYPE_COMMON, infoString);
                        ch.writeAndFlush(message);
                        System.out.println("yes");
                    }
                }
            }
        }).start();

        new RpcServer(10086).start();
    }
}



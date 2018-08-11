package netty.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import netty.model.RpcMessage;

import java.util.*;

public class NettyMapping {
    /**
     * 存储每一个客户端接入进来时的channel对象
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * server端停车场标识与channel键值对
     */
    public static Map<String, ChannelId> serverChannelMap = new HashMap<>();

    /**
     * client端停车场标识与channel键值对
     */
    public static Map<String, SocketChannel> clientChannelMap = new HashMap<>();


    /**
     * server端：根据停车场id获取channel
     *
     * @param parkingId
     * @return
     */
    public static Channel getServerChannel(String parkingId) {
        Channel channel = null;
        ChannelId channelId = serverChannelMap.get(parkingId);
        Iterator<Channel> iterator = NettyMapping.group.iterator();

        while (iterator.hasNext()) {
            Channel ch = iterator.next();
            if (ch.id().asShortText().equals(channelId.asShortText()) && ch.id().asLongText().equals(channelId.asLongText())) {
                channel = ch;
                break;
            }
        }

        return channel;
    }

    /**
     * 删除停车场id与channel的映射
     *
     * @param channel
     */
    public static void removeServerChannel(Channel channel) {
        ChannelId channelId = channel.id();
        Set<String> keys = NettyMapping.serverChannelMap.keySet();
        Iterator<String> iterator = keys.iterator();

        while(iterator.hasNext()) {
            ChannelId chId = NettyMapping.serverChannelMap.get(iterator.next());
            if (chId.asShortText().equals(channelId.asShortText()) && chId.asLongText().equals(channelId.asLongText())) {
                iterator.remove();
            }
        }

        NettyMapping.group.remove(channel);
    }

    /**
     * client端：根据停车场id获取channel
     *
     * @param parkingId
     * @return
     */
    public static SocketChannel getSocketChannel(String parkingId) {
        return clientChannelMap.get(parkingId);
    }

    /**
     * 发送请求信息
     *
     * @param parkingId
     * @param rpcRequestMessage
     */
    public static int sendRequestMessage(String parkingId, RpcMessage rpcRequestMessage) {
        SocketChannel socketChannel = getSocketChannel(parkingId);
        ChannelFuture cf = socketChannel.writeAndFlush(rpcRequestMessage);

        if (cf.isSuccess()) {
            System.out.println("send request success!");
            return 1;
        }

        System.out.println("send request fail!");
        return 0;
    }

    /**
     * 发送响应信息
     *
     * @param parkingId
     * @param rpcResponseMessage
     * @return
     */
    public static int sendResponseMessage(String parkingId, RpcMessage rpcResponseMessage) {
        Channel channel = getServerChannel(parkingId);
        ChannelFuture cf = channel.writeAndFlush(rpcResponseMessage);

        if (cf.isSuccess()) {
            System.out.println("send response success!");
            return 1;
        }

        System.out.println("send response fail!");
        return 0;
    }
}

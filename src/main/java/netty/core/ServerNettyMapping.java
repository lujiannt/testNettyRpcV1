package netty.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import netty.model.RpcMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ServerNettyMapping {
    /**
     * 存储每一个客户端接入进来时的channel对象
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * server端停车场标识与channel键值对
     */
    public static Map<String, ChannelId> serverChannelMap = new HashMap<>();

    /**
     * 本地端返回的消息映射
     */
    public static Map<String, RpcMessage> rpcMessageMap = new HashMap<>();

    /**
     * 注册监听返回响应消息
     *
     * @param uuid
     */
    public static RpcMessage registerListenerAndReturn(String uuid) {
        RpcMessage rpcMessage = null;
        try {
            Thread.sleep(500);
            while (rpcMessage == null) {
                rpcMessage = rpcMessageMap.get(uuid);
            }
            rpcMessageMap.remove(uuid);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return rpcMessage;
        }
    }

    /**
     * server端：根据停车场id获取channel
     *
     * @param parkingId
     * @return
     */
    public static Channel getServerChannel(String parkingId) {
        Channel channel = null;
        ChannelId channelId = serverChannelMap.get(parkingId);
        Iterator<Channel> iterator = ServerNettyMapping.group.iterator();

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
        Set<String> keys = ServerNettyMapping.serverChannelMap.keySet();
        Iterator<String> iterator = keys.iterator();

        while (iterator.hasNext()) {
            ChannelId chId = ServerNettyMapping.serverChannelMap.get(iterator.next());
            if (chId.asShortText().equals(channelId.asShortText()) && chId.asLongText().equals(channelId.asLongText())) {
                iterator.remove();
            }
        }

        ServerNettyMapping.group.remove(channel);
    }

    /**
     * 发送信息
     *
     * @param parkingId
     * @param rpcResponseMessage
     * @return
     */
    public static int sendRequestMessage(String parkingId, RpcMessage rpcResponseMessage) {
        Channel channel = getServerChannel(parkingId);
        ChannelFuture cf = channel.writeAndFlush(rpcResponseMessage);

        if (cf.isSuccess()) {
            System.out.println("send request success!");
            return 1;
        }

        System.out.println("send request fail!");
        return 0;
    }
}

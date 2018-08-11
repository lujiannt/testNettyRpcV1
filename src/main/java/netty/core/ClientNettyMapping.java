package netty.core;

import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;
import netty.model.RpcMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ClientNettyMapping {
    /**
     * client端停车场标识与channel键值对
     */
    public static Map<String, SocketChannel> clientChannelMap = new HashMap<>();

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
     * 根据停车id删除channel
     *
     * @param parkingId
     */
    public static void removeSocketChannel(String parkingId) {
        Set<String> keys = clientChannelMap.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.equals(parkingId)) {
                iterator.remove();
            }
        }
    }

    /**
     * 发送信息
     *
     * @param parkingId
     * @param rpcRequestMessage
     * @return
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
}

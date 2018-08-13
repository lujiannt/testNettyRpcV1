package netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.core.ServerNettyMapping;
import netty.model.RpcMessage;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress().toString() + " connected!");
//        ServerNettyMapping.channelMap.put("2", ch.id());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server channelRead..");

        //不同消息类型对应不同处理模式
        this.dealDifferentMsg(ctx, msg);
    }

    /**
     * 客户端与服务端断开连接时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端与服务端连接关闭...");

        ServerNettyMapping.removeServerChannel(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 处理消息
     *
     * @param ctx
     * @param msg
     */
    private void dealDifferentMsg(ChannelHandlerContext ctx, Object msg) {
        RpcMessage rpcMessage = (RpcMessage) msg;

        switch(rpcMessage.getType()){
            case RpcMessage.MESSAGE_TYPE_COMMON:
                System.out.println(ctx.channel().remoteAddress() + "->Content : " + rpcMessage.getContent());
                break;
            case RpcMessage.MESSAGE_TYPE_HEART:
                System.out.println(ctx.channel().remoteAddress() + "->Heart : " + rpcMessage.getContent());
                if (ServerNettyMapping.getServerChannel(rpcMessage.getContent()) == null) {
                    System.out.println("map is null");
                    Channel ch = ctx.channel();
                    ServerNettyMapping.group.add(ch);
                    ServerNettyMapping.serverChannelMap.put(rpcMessage.getContent(), ch.id());
                }
                break;
            case RpcMessage.MESSAGE_TYPE_RESPONSE:
                System.out.println(ctx.channel().remoteAddress() + "->Response : " + rpcMessage.getContent());

                ServerNettyMapping.rpcMessageMap.put(rpcMessage.getMessageId(), rpcMessage);
                break;
            default:
                System.err.println("unknow request!");
                break;
        }
    }

}

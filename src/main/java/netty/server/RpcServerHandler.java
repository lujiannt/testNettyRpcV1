package netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.core.NettyMapping;
import netty.model.RpcMessage;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress().toString() + " connected!");
//        NettyMapping.channelMap.put("2", ch.id());
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

        NettyMapping.removeServerChannel(ctx.channel());
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
        RpcMessage rpcRequestMessage = (RpcMessage) msg;

        switch(rpcRequestMessage.getType()){
            case RpcMessage.MESSAGE_TYPE_COMMON:
                System.out.println(ctx.channel().remoteAddress() + "->Content : " + rpcRequestMessage.getContent());
                break;
            case RpcMessage.MESSAGE_TYPE_HEART:
                System.out.println(ctx.channel().remoteAddress() + "->Heart : " + rpcRequestMessage.getContent());
                if (NettyMapping.getServerChannel(rpcRequestMessage.getContent()) == null) {
                    Channel ch = ctx.channel();
                    NettyMapping.group.add(ch);
                    NettyMapping.serverChannelMap.put(rpcRequestMessage.getContent(), ch.id());
                }
                break;
            case RpcMessage.MESSAGE_TYPE_RESPONSE:
                System.out.println(ctx.channel().remoteAddress() + "->Response : " + rpcRequestMessage.getContent());

                break;
            default:
                System.err.println("unknow request!");
                break;
        }
    }

}

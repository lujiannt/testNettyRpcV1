package netty.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import netty.core.ClientNettyMapping;
import netty.invoke.AbstractInvoker;
import netty.model.RpcMessage;

import java.util.Date;

@Sharable
public class PpcClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("激活时间是：" + new Date());
        System.out.println("PpcClientHandler channelActive");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("停止时间是：" + new Date());
        System.out.println("PpcClientHandler channelInactive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        dealDifferentMsg(ctx, msg);
        ReferenceCountUtil.release(msg);
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
                System.out.println("RpcServer : " + rpcMessage.getContent());
                break;
            case RpcMessage.MESSAGE_TYPE_REQUEST:
                System.out.println(ctx.channel().remoteAddress() + "->Request : " + rpcMessage.getClassName());
                Object object = null;
                RpcMessage rpcResponseMessage = null;
                try {
                    object = AbstractInvoker.invokeRequest(rpcMessage);
                    rpcResponseMessage = new RpcMessage();
                    rpcResponseMessage.setMessageId(rpcMessage.getMessageId());
                    rpcResponseMessage.setType(RpcMessage.MESSAGE_TYPE_RESPONSE);
                    rpcResponseMessage.setError(null);
                    rpcResponseMessage.setResult(object);

                } catch (Exception e) {
                    rpcResponseMessage.setError("errorMessage : " + e.getMessage());
                } finally {
                    ChannelFuture cf = ctx.writeAndFlush(rpcResponseMessage);
                    if (cf.isSuccess()) {
                        System.out.println("return response success");
                    } else {
                        System.out.println("return response fail");
                    }
                }
                break;
            default:
                System.err.println("unknow request!");
                break;
        }
    }
}

package netty.heart;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import netty.model.RpcMessage;

/**
 * 客户端写超时trigger
 */
@Sharable
public class ConnectorIdleStateTrigger extends ChannelInboundHandlerAdapter {
    //TODO 这边要从本地端redis中获取停车场Id
    private static final String HEARTBEAT = "1";

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // write heartbeat to server
                RpcMessage rpcRequestMessage = new RpcMessage(RpcMessage.MESSAGE_TYPE_HEART, HEARTBEAT);
                ChannelFuture cf = ctx.writeAndFlush(rpcRequestMessage);
                if(cf.isSuccess()) {
//                    System.out.println("yes");
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

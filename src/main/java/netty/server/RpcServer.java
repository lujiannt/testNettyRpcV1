package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import netty.heart.AcceptorIdleStateTrigger;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class RpcServer {
    private final AcceptorIdleStateTrigger idleStateTrigger = new AcceptorIdleStateTrigger();

    private int port;
    private ServerSocketChannel serverSocketChannel;

    public RpcServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sbs = new ServerBootstrap().group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
                    .localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast(idleStateTrigger);
                            ch.pipeline().addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE ,ClassResolvers.cacheDisabled(this
                                    .getClass().getClassLoader())));
                            ch.pipeline().addLast("encoder", new ObjectEncoder());
//                            ch.pipeline().addLast("decoder", new StringDecoder());
//                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast(new RpcServerHandler());
                        };

                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，开始接收进来的连接
            ChannelFuture future = sbs.bind(port).sync();

            if (future.isSuccess()) {
                serverSocketChannel = (ServerSocketChannel)future.channel();
                System.out.println("RpcServer start listen at " + port);
            } else {
                System.out.println("RpcServer start fail!");
            }

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new RpcServer(port).start();
    }

    /**
     * 发送消息 todo -- 测试用
     *
     * @param msg
     */
    public void sendMessage(Object msg){
        if(serverSocketChannel != null){
            serverSocketChannel.writeAndFlush(msg);
        }
    }
}

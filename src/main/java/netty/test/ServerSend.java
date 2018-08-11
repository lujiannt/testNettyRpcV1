package netty.test;

import service.NettyService;
import service.serviceImpl.NettyServiceImpl;

public class ServerSend {

    public static void main(String[] args) throws Exception {
//        new Thread(() -> {
//            Scanner input = new Scanner(System.in);
//            String infoString = "";
//            while (true) {
//                while (NettyMapping.group.size() > 0) {
//                    System.out.println("client connected!");
//                    infoString = input.nextLine();
//
//                    Iterator<Channel> iterator = NettyMapping.group.iterator();
//
//                    while (iterator.hasNext()) {
//                        Channel ch = iterator.next();
//                        ch.writeAndFlush(infoString);
//                        System.out.println("yes");
//                    }
//                }
//            }
//        }).start();

        NettyService nettyService = new NettyServiceImpl();

        new Thread(new Runnable() {
            @Override
            public void run() {
                nettyService.bindPort(10086);
            }
        }).start();


        Thread.sleep(5000);
        if (nettyService.getChannelSize() > 0) {
            System.out.println("发送");
            nettyService.sendMessage("asdasd");
        }
    }
}



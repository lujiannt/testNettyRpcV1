package netty.test.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import netty.core.RpcService;
import netty.core.ServerNettyMapping;
import netty.model.RpcMessage;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.UUID;

@RpcService(DemoService.class)
@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String sendRequest(String parkingId, RpcMessage rpcMessage) {
        ChannelId channelId = ServerNettyMapping.serverChannelMap.get(parkingId);
        String messageId = null;
        assert channelId != null;

        //发消息
        Iterator<Channel> iterator = ServerNettyMapping.group.iterator();
        while (iterator.hasNext()) {
            Channel ch = iterator.next();

            if (ch.id().asLongText().equals(channelId.asLongText()) && ch.id().asShortText().equals(channelId.asShortText())) {
                String uuid = UUID.randomUUID().toString();
                messageId = uuid;
                rpcMessage.setMessageId(messageId);
                ch.writeAndFlush(rpcMessage);
                ServerNettyMapping.rpcMessageMap.put(messageId, null);
                break;
            }
        }

        return messageId;
    }
}

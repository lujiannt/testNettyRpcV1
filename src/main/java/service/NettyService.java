package service;

public interface NettyService {
    void bindPort(int port);

    int getChannelSize();

    void sendMessage(String message);
}

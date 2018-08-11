package service;

public interface NettyService {
    //TODO -- test
    void bindPort(int port);
    //TODO -- test
    int getChannelSize();
    //TODO -- test
    void sendMessage(String message);
}

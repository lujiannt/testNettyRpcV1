package netty.test.chat;

import netty.client.RpcClient;
import netty.model.RpcMessage;

import java.util.Scanner;

public class ClientSend {
    public static void main(String[] args){
        RpcClient rpcClient = new RpcClient();
        try {
            rpcClient.connect(10086, "localhost");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner input = new Scanner(System.in);
        String infoString = "";
        while (true){
            infoString = input.nextLine();
            RpcMessage message = new RpcMessage(RpcMessage.MESSAGE_TYPE_COMMON, infoString);
            rpcClient.sendMessage(message);
        }
    }
}

